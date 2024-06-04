package com.sibertech;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AppMcFastCollector {

	public static void main(String[] args) {
		SpringApplication.run(AppMcFastCollector.class, args);
                System.out.println("\n\nAppMcFastCollector: Микросервис приемник данных от генератора отсылки данных стартовал\n");
	}

}
