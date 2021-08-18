package restaurant.ms.core.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import restaurant.ms.core.configrations.MessageEnvelope;
import restaurant.ms.core.dto.responses.FileRs;
import restaurant.ms.core.entities.FileDb;
import restaurant.ms.core.services.FileDbService;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

@CrossOrigin
@Controller
@RequestMapping("/file")
public class FileController {

    @Autowired
    private FileDbService fileDbService;

    @RequestMapping(value = "/upload",method = RequestMethod.POST)
    public ResponseEntity<MessageEnvelope> uploadFile(HttpServletRequest httpServletRequest,
                                                      @RequestParam("file") MultipartFile file) {
        Locale locale = httpServletRequest.getLocale();

        FileRs fileRs = fileDbService.store(file,locale);

        MessageEnvelope messageEnvelope = new MessageEnvelope(HttpStatus.OK, "success", fileRs, locale);

        return new ResponseEntity<>(messageEnvelope, HttpStatus.OK);
    }

    @RequestMapping(value = "/download/{id}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> getFile(@PathVariable String id) {

        FileDb fileDB = fileDbService.getFile(id);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDB.getName() + "\"")
                .body(fileDB.getData());
    }
}
