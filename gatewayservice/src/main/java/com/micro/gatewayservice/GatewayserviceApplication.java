package com.micro.gatewayservice;

import static org.springframework.security.config.Customizer.withDefaults;

import java.time.Duration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;

@SpringBootApplication
public class GatewayserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayserviceApplication.class, args);
	}

	// the services we have is cartservice, inventoryservice, orderservice,
	// paymentservice, productservice, userservice

	@Bean
	public RouteLocator ecommerceRouteConfig(RouteLocatorBuilder routeLocatorBuilder) {
		return routeLocatorBuilder.routes()
				.route(p -> p
						.path("/e-commerce/cartservice/**")
						.filters(f -> f.rewritePath("/e-commerce/cartservice/(?<segment>.*)", "/${segment}")
								.circuitBreaker(config -> config.setName("cartcircuitbreaker")
										.setFallbackUri("forward:/fallback/cart"))
								.retry(config -> config.setRetries(3).setMethods(HttpMethod.GET)
										.setBackoff(Duration.ofMillis(100), Duration.ofMillis(200), 2, true))
								.tokenRelay())
						.uri("lb://cartservice"))

				.route(p -> p
						.path("/e-commerce/inventoryservice/**")
						.filters(f -> f.rewritePath("/e-commerce/inventoryservice/(?<segment>.*)", "/${segment}")
								.circuitBreaker(config -> config.setName("inventorycircuitbreaker")
										.setFallbackUri("forward:/fallback/inventory"))
								.retry(config -> config.setRetries(3).setMethods(HttpMethod.GET)
										.setBackoff(Duration.ofMillis(100), Duration.ofMillis(200), 2, true))
								.tokenRelay())
						.uri("lb://inventoryservice"))

				.route(p -> p
						.path("/e-commerce/orderservice/**")
						.filters(f -> f.rewritePath("/e-commerce/orderservice/(?<segment>.*)", "/${segment}")
								.circuitBreaker(config -> config.setName("ordercircuitbreaker")
										.setFallbackUri("forward:/fallback/order"))
								.retry(config -> config.setRetries(3).setMethods(HttpMethod.GET)
										.setBackoff(Duration.ofMillis(100), Duration.ofMillis(200), 2, true))
								.tokenRelay())
						.uri("lb://orderservice"))

				.route(p -> p
						.path("/e-commerce/paymentservice/**")
						.filters(f -> f.rewritePath("/e-commerce/paymentservice/(?<segment>.*)", "/${segment}")
								.circuitBreaker(config -> config.setName("paymentcircuitbreaker")
										.setFallbackUri("forward:/fallback/payment"))
								.retry(config -> config.setRetries(3).setMethods(HttpMethod.GET)
										.setBackoff(Duration.ofMillis(100), Duration.ofMillis(200), 2, true))
								.tokenRelay())
						.uri("lb://paymentservice"))

				.route(p -> p
						.path("/e-commerce/productservice/**")
						.filters(f -> f.rewritePath("/e-commerce/productservice/(?<segment>.*)", "/${segment}")
								.circuitBreaker(config -> config.setName("productcircuitbreaker")
										.setFallbackUri("forward:/fallback/product"))
								.retry(config -> config.setRetries(3).setMethods(HttpMethod.GET)
										.setBackoff(Duration.ofMillis(100), Duration.ofMillis(200), 2, true))
								.tokenRelay())
						.uri("lb://productservice"))

				.route(p -> p
						.path("/e-commerce/userservice/**")
						.filters(f -> f.rewritePath("/e-commerce/userservice/(?<segment>.*)", "/${segment}")
								.circuitBreaker(config -> config.setName("usercircuitbreaker")
										.setFallbackUri("forward:/fallback/user"))
								.retry(config -> config.setRetries(3).setMethods(HttpMethod.GET)
										.setBackoff(Duration.ofMillis(100), Duration.ofMillis(200), 2, true))
								.tokenRelay())
						.uri("lb://userservice"))

				.build();
	}

	@Bean
	public Customizer<ReactiveResilience4JCircuitBreakerFactory> defaultCustomizer() {
		return factory -> factory.configureDefault(id -> new Resilience4JConfigBuilder(id)
				.timeLimiterConfig(TimeLimiterConfig.custom().timeoutDuration(Duration.ofSeconds(4)).build())
				.circuitBreakerConfig(CircuitBreakerConfig.ofDefaults())
				.build());
	}

	@Bean
	public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
		http
				// we need to allow cors for the gateway
				.cors(withDefaults())
				.authorizeExchange(exchanges -> exchanges
						.anyExchange().authenticated())
				.oauth2Login(withDefaults())
				.oauth2ResourceServer(ServerHttpSecurity.OAuth2ResourceServerSpec::jwt);
		return http.build();
	}

}
