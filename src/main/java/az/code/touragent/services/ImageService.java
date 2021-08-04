package az.code.touragent.services;

import az.code.touragent.models.Offer;
import net.sf.jasperreports.engine.JRException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public interface ImageService {
    File convertOfferToImage(Offer offer, String email) throws FileNotFoundException, JRException;

    byte[] extractBytes (Offer offer, String email) throws IOException;
}
