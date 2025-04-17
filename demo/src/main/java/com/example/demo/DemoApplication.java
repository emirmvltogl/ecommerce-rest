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
			boolean a = encoder.matches("enes123",
					"$2a$10$PIiUCZZh7p8HQjIQwqfPOuB5DCZ/tecqsblON00tp2scwpGbkTk8y");
			System.out.println(a);
	
						
			//  User user = repo.findByEmail("eogemir@gmail.com").get();

		// User user2 = repo.findByUsername("hakan").get();

		// User user3 = repo.findByResetToken("null").get();

		// System.out.println(user2 + " \n");+

		// System.out.println(user + " \n");

		// System.out.println(user3 + " \n");

	}

}
