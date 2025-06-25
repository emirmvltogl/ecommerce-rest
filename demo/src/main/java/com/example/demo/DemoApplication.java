package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class DemoApplication {

	// @Autowired
	// private static UserRepo repo;
		public static void main(String[] args) {
	
			SpringApplication.run(DemoApplication.class, args);
			BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
			boolean a = encoder.matches("test123",
					"$2a$10$Eig/Y4iKPihYplx3WK7ZMeuSHtx6Gj9ybEb4JcAR997qgXHUa4S.y");
			System.out.println(a);
	
	}

}
