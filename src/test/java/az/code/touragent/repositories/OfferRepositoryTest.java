package az.code.touragent.repositories;

import az.code.touragent.models.Offer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class OfferRepositoryTest {
    @Autowired
    private OfferRepository offerRepo;

    @Test
    void itShouldGetOfferByRequestId() {
        UUID requestId = UUID.randomUUID();
        Offer testOffer = Offer.builder().requestId(requestId)
                .userEmail("test@gmail.com")
                .dateInterval("12.11.2021-19.11.2021")
                .price("1500 Azn")
                .tourInformation("Tour in Prague").build();
        offerRepo.save(testOffer);

        Offer result = offerRepo.getOfferByRequestId(requestId);

        assertThat(result).isEqualTo(testOffer);
    }

    @AfterEach
    void tearDown() {
        offerRepo.deleteAll();
    }
}