package com.codetree.CodeTreeHRM;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.codetree.CodeTreeHRM")
public class CodeTreeHrmApplication {

	public static void main(String[] args) {
		SpringApplication.run(CodeTreeHrmApplication.class, args);
	}

}
