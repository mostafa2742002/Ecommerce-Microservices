package com.micro.gatewayservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class GatewayserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayserviceApplication.class, args);
	}

	// the services we have is cartservice, inventoryservice, orderservice, paymentservice, productservice, userservice

	@Bean
	public RouteLocator ecommerceRouteConfig(RouteLocatorBuilder routeLocatorBuilder){
		return routeLocatorBuilder.routes()
				.route(p -> p
					.path("/e-commerce/cartservice/**")
					.filters(f -> f.rewritePath("/e-commerce/cartservice/(?<segment>.*)", "/${segment}")
					.circuitBreaker(config -> config.setName("cartcircuitbreaker").setFallbackUri("forward:/fallback/cart")))					
					.uri("lb://cartservice"))
				.route(p -> p
					.path("/e-commerce/inventoryservice/**")
					.filters(f -> f.rewritePath("/e-commerce/inventoryservice/(?<segment>.*)", "/${segment}")
					.circuitBreaker(config -> config.setName("inventorycircuitbreaker").setFallbackUri("forward:/fallback/inventory")))
					.uri("lb://inventoryservice"))
				.route(p -> p 
					.path("/e-commerce/orderservice/**")
					.filters(f -> f.rewritePath("/e-commerce/orderservice/(?<segment>.*)", "/${segment}")
					.circuitBreaker(config -> config.setName("ordercircuitbreaker").setFallbackUri("forward:/fallback/order")))
					.uri("lb://orderservice"))
				.route(p -> p
					.path("/e-commerce/paymentservice/**")
					.filters(f -> f.rewritePath("/e-commerce/paymentservice/(?<segment>.*)", "/${segment}")
					.circuitBreaker(config -> config.setName("paymentcircuitbreaker").setFallbackUri("forward:/fallback/payment")))
					.uri("lb://paymentservice"))
				.route(p -> p
					.path("/e-commerce/productservice/**")
					.filters(f -> f.rewritePath("/e-commerce/productservice/(?<segment>.*)", "/${segment}")
					.circuitBreaker(config -> config.setName("productcircuitbreaker").setFallbackUri("forward:/fallback/product")))
					.uri("lb://productservice"))
				.route(p -> p
					.path("/e-commerce/userservice/**")
					.filters(f -> f.rewritePath("/e-commerce/userservice/(?<segment>.*)", "/${segment}")
					.circuitBreaker(config -> config.setName("usercircuitbreaker").setFallbackUri("forward:/fallback/user")))
					.uri("lb://userservice"))
				.build();
	}

}
