package com.hanbat.zanbanzero;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class ZanbanzeroApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZanbanzeroApplication.class, args);
	}

}
