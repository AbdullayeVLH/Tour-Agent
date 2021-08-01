package az.code.touragent.services;

import az.code.touragent.dtos.MakeOfferDto;
import az.code.touragent.models.Offer;

import java.util.List;
import java.util.UUID;

public interface OfferService {

    Offer makeOffer(MakeOfferDto dto, UUID requestId, String email);

    Offer getOffer(Long offerId);

    List<Offer> getAllOffers();
}
