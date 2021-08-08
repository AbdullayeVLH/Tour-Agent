package az.code.touragent.services;

import az.code.touragent.dtos.MakeOfferDto;
import az.code.touragent.enums.RequestStatus;
import az.code.touragent.models.AgentRequests;
import az.code.touragent.models.Offer;
import az.code.touragent.models.Request;
import az.code.touragent.models.User;
import az.code.touragent.repositories.AcceptedRepository;
import az.code.touragent.repositories.AgentRequestsRepository;
import az.code.touragent.repositories.OfferRepository;
import az.code.touragent.repositories.RequestRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class OfferServiceImplTest {

    @Mock private OfferRepository offerRepo;
    @Mock private RequestRepository requestRepo;
    @Mock private AgentRequestsRepository agentRequestsRepo;
    @Mock private AcceptedRepository acceptedRepo;

    private OfferServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new OfferServiceImpl(offerRepo, requestRepo, agentRequestsRepo, null, acceptedRepo);
    }

    @Test
    @Disabled
    void makeOffer() {
        UUID requestId = UUID.randomUUID();
        AgentRequests firstTestAgentRequest = AgentRequests.builder()
                .requestId(requestId)
                .userEmail("first@gmail.com")
                .status(RequestStatus.UNSEEN).build();
        agentRequestsRepo.save(firstTestAgentRequest);

        Request firstTestRequest = Request.builder().requestId(requestId)
                .expired(false).lang("az").chatId(1L)
                .status(RequestStatus.UNSEEN).build();
        requestRepo.save(firstTestRequest);

        MakeOfferDto testDto = MakeOfferDto.builder().tourInformation("Prague").dateInterval("123").price("100").build();

        service.makeOffer(testDto, requestId, firstTestAgentRequest.getUserEmail());

        ArgumentCaptor<Offer> offerArgumentCaptor = ArgumentCaptor.forClass(Offer.class);

        verify(offerRepo).save(offerArgumentCaptor.capture());

        Offer result = offerArgumentCaptor.getValue();

        assertThat(result).isEqualTo(service.makeOffer(testDto, requestId, firstTestAgentRequest.getUserEmail()));
    }

    @Test
    @Disabled
    void getOffer() {
        Offer testOffer = Offer.builder()
                .id(1L)
                .price("1").dateInterval("1")
                .requestId(UUID.randomUUID()).userEmail("test@gmail.com")
                .tourInformation("1").build();
        offerRepo.save(testOffer);

        Offer result = service.getOffer(1L);

        assertThat(result).isEqualTo(testOffer);
    }

    @Test
    void isShouldGetAllOffers() {
        service.getAllOffers();
        verify(offerRepo).findAll();
    }

    @Test
    @Disabled
    void getAcceptedOffers() {
    }

    @Test
    @Disabled
    void getUserContactInfo() {
    }
}