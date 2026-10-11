package com.financeiro.financas_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FinancasApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(FinancasApiApplication.class, args);
    }

}