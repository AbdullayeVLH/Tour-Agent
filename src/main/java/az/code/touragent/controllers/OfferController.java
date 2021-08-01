package az.code.touragent.controllers;

import az.code.touragent.dtos.MakeOfferDto;
import az.code.touragent.dtos.UserData;
import az.code.touragent.exceptions.AlreadyOffered;
import az.code.touragent.exceptions.RequestExpired;
import az.code.touragent.models.Offer;
import az.code.touragent.services.OfferService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.LoginException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/offer")
public class OfferController {

    OfferService offerService;

    public OfferController(OfferService offerService) {
        this.offerService = offerService;
    }

    @ExceptionHandler(AlreadyOffered.class)
    public ResponseEntity<String> handleNotFound(AlreadyOffered e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RequestExpired.class)
    public ResponseEntity<String> handleNotFound(RequestExpired e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/make/{requestId}")
    public ResponseEntity<Offer> makeOffer(@RequestBody MakeOfferDto dto,
                                           @PathVariable UUID requestId,
                                           @RequestAttribute("user") UserData user) {
        return ResponseEntity.ok(offerService.makeOffer(dto, requestId, user.getEmail()));
    }

    @GetMapping("/{offerId}")
    public ResponseEntity<Offer> getOffer(@PathVariable Long offerId) {
        return ResponseEntity.ok(offerService.getOffer(offerId));
    }

    @GetMapping("/all")
    public ResponseEntity<List<Offer>> getAllOffers() {
        return ResponseEntity.ok(offerService.getAllOffers());
    }
}
