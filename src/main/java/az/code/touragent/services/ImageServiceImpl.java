package az.code.touragent.services;

import az.code.touragent.dtos.MakeOfferDto;
import az.code.touragent.models.Offer;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;

@Service
public class ImageServiceImpl implements ImageService {

    private final RabbitMQService service;

    public ImageServiceImpl(RabbitMQService service) {
        this.service = service;
    }

    @Override
    public File convertOfferToImage(Offer offer, String email) throws FileNotFoundException, JRException {
        File file = ResourceUtils.getFile("classpath:offer.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        List<MakeOfferDto> offers = new ArrayList<>();
        offers.add(MakeOfferDto.builder()
                .price(offer.getPrice())
                .dateInterval(offer.getDateInterval())
                .tourInformation(offer.getTourInformation())
                .build());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(offers);
        Map<String, Object> parameters = new HashMap<>();
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
        try{
            File out = new File( offer.getRequestId() + email +  "." + "jpg");
            BufferedImage image = (BufferedImage) JasperPrintManager.printPageToImage(jasperPrint, 0,1f);
            ImageIO.write(image, "jpg", out);
            return out;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public byte[] extractBytes (Offer offer, String email) throws IOException {
        byte[] buffer = new byte[1024];
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        FileInputStream fis = new FileInputStream(offer.getRequestId() + email + ".jpg");
        int read;
        while ((read = fis.read(buffer)) != -1){
            outputStream.write(buffer, 0, read);
        }
        fis.close();
        outputStream.close();
        service.sendToOfferQueue(outputStream.toByteArray());
        return outputStream.toByteArray();
    }
}