package gs.nysub.controllers;

import gs.nysub.components.NYSubWebService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NYSubWebController {

    @Autowired
    private NYSubWebService nySubWebService;

    @RequestMapping(value = "/status", method = RequestMethod.GET)
    public ResponseEntity getMtaStatusWebVersion() {
        String response = nySubWebService.generateStatus();
        return new ResponseEntity(response, HttpStatus.OK);
    }
}
