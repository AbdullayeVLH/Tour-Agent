package az.code.touragent.repositories;

import az.code.touragent.models.Accepted;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class AcceptedRepositoryTest {

    @Autowired
    private AcceptedRepository acceptedRepo;

    @Test
    void itShouldGetAcceptedOffersByAgentEmail() {
        List<Accepted> testAcceptedOffers = new ArrayList<>();
        UUID firstRequestId = UUID.randomUUID();
        UUID secondRequestId = UUID.randomUUID();
        Accepted firstTestAcceptedOffer = Accepted.builder()
                .contactInfo("+994502556478")
                .requestId(firstRequestId)
                .agentEmail("test@gmail.com").build();

        Accepted secondTestAcceptedOffer = Accepted.builder()
                .contactInfo("+994702556478")
                .requestId(secondRequestId)
                .agentEmail("test@gmail.com").build();
        testAcceptedOffers.add(firstTestAcceptedOffer);
        testAcceptedOffers.add(secondTestAcceptedOffer);
        acceptedRepo.save(firstTestAcceptedOffer);
        acceptedRepo.save(secondTestAcceptedOffer);

        List<Accepted> result = acceptedRepo.getAcceptedsByAgentEmail("test@gmail.com");

        assertThat(result.size()).isEqualTo(testAcceptedOffers.size());
    }

    @Test
    void itShouldGetAcceptedOfferByAgentEmailAndRequestId() {
        UUID requestId = UUID.randomUUID();
        Accepted testAcceptedOffer = Accepted.builder()
                .contactInfo("+994502556478")
                .requestId(requestId)
                .agentEmail("test@gmail.com").build();
        acceptedRepo.save(testAcceptedOffer);

        Accepted result = acceptedRepo.getAcceptedByAgentEmailAndRequestId("test@gmail.com", requestId);

        assertThat(result.getContactInfo()).isEqualTo(testAcceptedOffer.getContactInfo());
    }

    @AfterEach
    void tearDown() {
        acceptedRepo.deleteAll();
    }
}