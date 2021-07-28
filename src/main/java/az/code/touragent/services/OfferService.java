package az.code.touragent.services;

import az.code.touragent.models.Offer;

import java.util.List;
import java.util.UUID;

public interface OfferService {

    Offer makeOffer(String price, String dateInterval, String tourInformation, UUID requestId);

    Offer getOffer(Long offerId);

    List<Offer> getAllOffers();
}
