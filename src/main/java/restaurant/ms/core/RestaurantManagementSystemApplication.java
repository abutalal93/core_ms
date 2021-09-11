package restaurant.ms.core;

import com.sendgrid.*;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Base64;
import java.util.Locale;
import java.util.Properties;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@SpringBootApplication
public class RestaurantManagementSystemApplication {

	public static void main(String[] args) {

		SpringApplication.run(RestaurantManagementSystemApplication.class, args);

	}

}
