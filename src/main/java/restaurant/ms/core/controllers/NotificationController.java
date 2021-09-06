package restaurant.ms.core.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import restaurant.ms.core.configrations.MessageEnvelope;
import com.sendgrid.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

@CrossOrigin
@Controller
@RequestMapping("/email")
public class NotificationController {


    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    public ResponseEntity<MessageEnvelope> sendEmail(HttpServletRequest httpServletRequest,
                                                     @RequestParam(value = "name", required = false) String name,
                                                     @RequestParam(value = "email", required = false) String email){

        Locale locale = httpServletRequest.getLocale();

        String html = getContent(name);

        Email from = new Email("w.info@wajdtuha.com");
        Email to = new Email(email);
        Content content = new Content("text/html", html);
        Mail mail = new Mail(from, "test", to, content);

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


        MessageEnvelope messageEnvelope = new MessageEnvelope(HttpStatus.OK, "success", null, locale);

        return new ResponseEntity<>(messageEnvelope, HttpStatus.OK);
    }

    private String getContent(String fullName){
        String html = "<!DOCTYPE html>\n" +
                "\n" +
                "<title></title>\n" +
                "\n" +
                "<head>\n" +
                "    <link href='https://fonts.googleapis.com/css?family=Cairo' rel='stylesheet'>\n" +
                "    <style>\n" +
                "    body {\n" +
                "        font-family: 'Cairo';\n" +
                "    }\n" +
                "    </style>\n" +
                "</head>\n" +
                "\n" +
                "<body>\n" +
                "\n" +
                "    <div style=\"direction: ltr;\">\n" +
                "        <h3>Dear PARAM</h3>\n" +
                "    \n" +
                "        <p>\n" +
                "            By receiving this email, you confirm that you have accepted the terms and conditions of the company's official logo design competition and have allowed you to participate in this competition.        \n" +
                "        </p>\n" +
                "\n" +
                "        <p>\n" +
                "         Wajdtuha wishing you all the best.\n" +
                "        </p>\n" +
                "\n" +
                "    \n" +
                "        <p>\n" +
                "            \"Yes, you can.\"\n" +
                "        </p>\n" +
                "        <p>\n" +
                "            Wajdtuha Team\n" +
                "        </p>\n" +
                "    \n" +
                "    </div>\n" +
                "\n" +
                "    <div style=\"direction: rtl;\">\n" +
                "        <h3>عزيزي PARAM</h3>\n" +
                "    \n" +
                "        <p>\n" +
                "            بتلقيك هذا البريد الإلكتروني، فأنت تؤكد قبولك المسبق للشروط والأحكام الخاصة بمسابقة تصميم شعار شركة وجدتها الرسمي، وسمح لك بالمشاركة في هذه المسابقة.\n" +
                "        </p>\n" +
                "    \n" +
                "    \n" +
                "        <p>\n" +
                "            وجدتها تتمنى لكم كل التوفيق.\n" +
                "        </p>\n" +
                "\n" +
                "    \n" +
                "        <p>\n" +
                "            \"نعم يمكنك\"\n" +
                "        </p>\n" +
                "        <p>\n" +
                "            فريق وجدتها.\n" +
                "        </p>\n" +
                "    \n" +
                "    </div>\n" +
                "\n" +
                "  \n" +
                "</body>\n" +
                "\n" +
                "</html>";

        return html.replaceAll("PARAM",fullName);
    }
}
