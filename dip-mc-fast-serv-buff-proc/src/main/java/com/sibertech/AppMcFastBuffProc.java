package com.sibertech;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// что бы при запуске приложения не нужно было указывать класс и его метод main(), 
// нужно в pom.xml проекта указать этот класс, вот так:
//      <properties>
//          <start-class>com.sibertech.AppDipGen</start-class>
//      </properties>

// после сборки с атрибутом "start-class", на выполнение jar-ник запускается просто по имени этого jar-ника, 
// вот так:
// java -jar dip.mc-etalon-1.0.jar

// С учетом того, что консоль у винды в древней кодировке 866, а есть желание видить в консоли русские буквочки, 
// запускать нужно так:
//      chcp 65001
//      java -Dfile.encoding=UTF-8 -Dsun.stdout.encoding=UTF-8 -jar dip.mc-fast-serv-buff-proc-1.0.jar


@SpringBootApplication
public class AppMcFastBuffProc {

	public static void main(String[] args) {
		SpringApplication.run(AppMcFastBuffProc.class, args);
                System.out.println("\n\nAppMcFastBuffProc: Микросервис-обработчик буферных таблиц стартовал\n");
	}

}
