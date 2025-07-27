package com.taxi.order_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;

@SpringBootApplication
public class OrderServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrderServiceApplication.class, args);

		System.out.println("""
            
            ╔════════════════════════════════════════════════════════════════╗
            ║                      ORDER SERVICE STARTED                     ║
            ║                                                                ║
            ║  📝 Handles order creation and management                      ║
            ║  🔗 Communicates with Pricing service                          ║
            ║  💾 Uses in-memory storage for simplicity                      ║
            ║                                                                ║
            ║  Available at: http://localhost:8081/api/orders                ║
            ╚════════════════════════════════════════════════════════════════╝
            """);
	}

	@Bean
	@LoadBalanced // Enables service discovery - uses service names like "pricing-service" instead of hardcoded URLs
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
