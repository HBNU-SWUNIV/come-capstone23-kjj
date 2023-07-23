package com.hanbat.zanbanzero;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class ZanbanzeroApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZanbanzeroApplication.class, args);
	}

}
