package az.code.touragent.controllers;

import az.code.touragent.services.OfferService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/offer")
public class OfferController {

    OfferService offerService;

    public OfferController(OfferService offerService) {
        this.offerService = offerService;
    }

    @PostMapping("/make")
    public ResponseEntity<?> makeOffer(@RequestBody String price, String dateInterval, String tourInformation, Long requestId){
        return ResponseEntity.ok(offerService.makeOffer(price, dateInterval, tourInformation, requestId));
    }
}
