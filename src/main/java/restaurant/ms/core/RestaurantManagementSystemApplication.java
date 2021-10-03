package restaurant.ms.core;

import com.sendgrid.*;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import restaurant.ms.core.utils.Utility;

import java.util.Base64;
import java.util.Locale;
import java.util.Properties;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@SpringBootApplication
public class RestaurantManagementSystemApplication {

	public static void main(String[] args) {
		System.out.println(Utility.parseDateTimeFromString("2021-10-30T01:05","yyyy-MM-dd'T'HH:mm"));
		SpringApplication.run(RestaurantManagementSystemApplication.class, args);

	}

}
