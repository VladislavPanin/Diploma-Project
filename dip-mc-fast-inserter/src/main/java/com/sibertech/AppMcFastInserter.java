package com.sibertech;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AppMcFastInserter {

	public static void main(String[] args) {
		SpringApplication.run(AppMcFastInserter.class, args);
                System.out.println("\n\nAppMcFastInserter: Микросервис-генератор отсылки на сервис-приемник данных стартовал\n");
	}

}
