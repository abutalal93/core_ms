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

		Email from = new Email("adabbas@wajdtuha.com");
		Email to = new Email("m.omari@wajdtuha.com");
		Content content = new Content("text/html", "test");
		Mail mail = new Mail(from, "test", to, content);

		String html = "";

		SendGrid sg = new SendGrid("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\n");

		Response response = null;
		try {
			Request request = new Request();
			request.setMethod(Method.POST);
			request.setEndpoint("mail/send");
			request.setBody(mail.build());

			response = sg.api(request);

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		SpringApplication.run(RestaurantManagementSystemApplication.class, args);

	}

}
