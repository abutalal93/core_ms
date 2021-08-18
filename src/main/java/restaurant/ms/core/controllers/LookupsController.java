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
import restaurant.ms.core.dto.responses.LookupRs;
import restaurant.ms.core.entities.FileDb;
import restaurant.ms.core.services.FileDbService;
import restaurant.ms.core.services.LookupService;
import restaurant.ms.core.services.SeedService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Locale;

@CrossOrigin
@Controller
@RequestMapping("/lookups")
public class LookupsController {

    @Autowired
    private LookupService lookupService;

    @Autowired
    private SeedService seedService;

    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    public ResponseEntity<MessageEnvelope> uploadFile(HttpServletRequest httpServletRequest,
                                                      @RequestParam(value = "lookupId", required = false) Long lookupId,
                                                      @RequestParam(value = "parentId", required = false) Long parentId,
                                                      @RequestParam(value = "category", required = false) String category) {
        Locale locale = httpServletRequest.getLocale();

        List<LookupRs> lookupRsList = lookupService.findLookup(lookupId, parentId, category, locale);

        MessageEnvelope messageEnvelope = new MessageEnvelope(HttpStatus.OK, "success", lookupRsList, locale);

        return new ResponseEntity<>(messageEnvelope, HttpStatus.OK);
    }

    @RequestMapping(value = "/seed", method = RequestMethod.GET)
    public ResponseEntity<MessageEnvelope> uploadFile(HttpServletRequest httpServletRequest) {

        Locale locale = httpServletRequest.getLocale();

        seedService.seed();

        MessageEnvelope messageEnvelope = new MessageEnvelope(HttpStatus.OK, "success", null, locale);

        return new ResponseEntity<>(messageEnvelope, HttpStatus.OK);
    }
}
