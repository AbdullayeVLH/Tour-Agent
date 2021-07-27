package az.code.touragent.services;

import az.code.touragent.models.Offer;

public interface OfferService {

    Offer makeOffer(String price, String dateInterval, String tourInformation, Long requestId);
}
