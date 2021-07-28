package az.code.touragent.controllers;

import az.code.touragent.models.Offer;
import az.code.touragent.services.OfferService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/offer")
public class OfferController {

    OfferService offerService;

    public OfferController(OfferService offerService) {
        this.offerService = offerService;
    }

    @PostMapping("/make/{requestId}")
    public ResponseEntity<?> makeOffer(@RequestBody String price, String dateInterval, String tourInformation,
                                       @PathVariable UUID requestId) {
        return ResponseEntity.ok(offerService.makeOffer(price, dateInterval, tourInformation, requestId));
    }

    @GetMapping("/{offerId}")
    public ResponseEntity<Offer> getOffer(@PathVariable Long offerId){
        return ResponseEntity.ok(offerService.getOffer(offerId));
    }

    @GetMapping("/all")
    public ResponseEntity<List<Offer>> getAllOffers(){
        return ResponseEntity.ok(offerService.getAllOffers());
    }
}
