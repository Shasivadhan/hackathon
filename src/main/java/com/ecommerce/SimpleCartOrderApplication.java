package com.ecommerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.ecommerce.feign")
public class SimpleCartOrderApplication {
  public static void main(String[] args) {
    SpringApplication.run(SimpleCartOrderApplication.class, args);
  }
}
