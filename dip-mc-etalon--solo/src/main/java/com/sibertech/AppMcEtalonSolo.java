package com.sibertech;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AppMcEtalonSolo {

	public static void main(String[] args) {
		SpringApplication.run(AppMcEtalonSolo.class, args);
                System.out.println("\n\nAppMcEtalonSolo: Микросервис эталонной вставки напрямую в БД стартовал\n");
	}

}
