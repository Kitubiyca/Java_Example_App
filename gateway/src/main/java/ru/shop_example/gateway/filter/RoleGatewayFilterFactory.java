package ru.shop_example.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import ru.shop_example.gateway.exception.custom.UserRoleCheckException;

import java.util.List;

@Component
public class RoleGatewayFilterFactory extends AbstractGatewayFilterFactory<RoleGatewayFilterFactory.Config> {

    public RoleGatewayFilterFactory() {
        super(RoleGatewayFilterFactory.Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String userRole = exchange.getAttribute("userRole");
            if (userRole == null) return Mono.error(new UserRoleCheckException("No role specified for this user"));
            if (!config.roles.contains(userRole)) return Mono.error(new UserRoleCheckException("Access denied for this user"));
            return chain.filter(exchange);
        };
    }

    public static class Config{
        List<String> roles;

        public Config(List<String> roles){
            this.roles = roles;
        }
    }
}
