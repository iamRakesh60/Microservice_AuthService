package com.apiGateway.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Component
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {
    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);


    private static final String SECRET_KEY = "my-super-secret-key";

    // Because gateway receives /auth1/**, use auth1-prefixed entries OR strip prefix.
    private static final List<String> openApiPathsPrefixes = List.of(
            "/auth1/api/v1/welcome/welcome",
            "/auth1/api/v1/auth/register",
            "/auth1/api/v1/auth/login",
           "/micro1/api/m2/test/welcome"
    );

    // Map protected prefixes to allowed roles
    private static final Map<String, List<String>> protectedEndpointsWithRoles = Map.of(
            "/auth1/api/v1/welcome/hello", List.of("ROLE_USER"),
            "/auth1/api/v1/welcome/admin", List.of("ROLE_ADMIN"),
           // "/micro1/api/m2/test/hello", List.of("ROLE_USER"),
            "/micro1/api/m2/test", List.of("ROLE_ADMIN")
    );

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String requestPath = exchange.getRequest().getURI().getPath();

        if (isPublicEndpoint(requestPath)) {
            return chain.filter(exchange);
        }

        String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
        String token = authHeader.substring(7);
        log.info("JWT token verify in apiGate Way");

        try {
            DecodedJWT jwt = JWT.require(Algorithm.HMAC256(SECRET_KEY))
                    .build()
                    .verify(token);

            String roleClaim = jwt.getClaim("role").asString(); // could be "ROLE_USER" or "ROLE_USER,ROLE_ADMIN"
            if (roleClaim == null || roleClaim.isBlank()) {
                exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                return exchange.getResponse().setComplete();
            }

            // If token had multiple roles joined by comma, split them
            List<String> tokenRoles = List.of(roleClaim.split(","));

            if (!isAuthorized(requestPath, tokenRoles)) {
                exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                return exchange.getResponse().setComplete();
            }

            // pass role header(s) downstream
            exchange = exchange.mutate()
                    .request(r -> r.header("X-User-Role", String.join(",", tokenRoles)))
                    .build();

        } catch (JWTVerificationException e) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        return chain.filter(exchange);
    }

    private boolean isPublicEndpoint(String path) {
        return openApiPathsPrefixes.stream().anyMatch(path::startsWith);
    }

    private boolean isAuthorized(String path, List<String> tokenRoles) {
        for (Map.Entry<String, List<String>> entry : protectedEndpointsWithRoles.entrySet()) {
            String protectedPrefix = entry.getKey();
            List<String> allowedRoles = entry.getValue();
            if (path.startsWith(protectedPrefix)) {
                // if any role in tokenRoles matches allowedRoles, OK
                return tokenRoles.stream().anyMatch(allowedRoles::contains);
            }
        }
        // if not in protected map, allow (or change to deny-by-default by returning false)
        return true;
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
