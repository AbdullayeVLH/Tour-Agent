package az.code.touragent.services;

import az.code.touragent.dtos.MakeOfferDto;
import az.code.touragent.models.Offer;
import net.sf.jasperreports.engine.JRException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface OfferService {

    Offer makeOffer(MakeOfferDto dto, UUID requestId, String email) throws JRException, IOException;

    Offer getOffer(Long offerId);

    List<Offer> getAllOffers();
}
