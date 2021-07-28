package az.code.touragent.services;

import az.code.touragent.enums.RequestStatus;
import az.code.touragent.models.AgentRequests;
import az.code.touragent.models.Offer;
import az.code.touragent.models.Request;
import az.code.touragent.repositories.AgentRequestsRepository;
import az.code.touragent.repositories.OfferRepository;
import az.code.touragent.repositories.RequestRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class OfferServiceImpl implements OfferService {

    private final OfferRepository offerRepo;
    private final RequestRepository requestRepo;
    private final AgentRequestsRepository agentRequestsRepo;
    private final RabbitMQService mqService;

    public OfferServiceImpl(OfferRepository offerRepo, RequestRepository requestRepo,
                            AgentRequestsRepository agentRequestsRepo, RabbitMQService mqService) {
        this.offerRepo = offerRepo;
        this.requestRepo = requestRepo;
        this.agentRequestsRepo = agentRequestsRepo;
        this.mqService = mqService;
    }

    @Override
    public Offer makeOffer(String price, String dateInterval, String tourInformation, UUID requestId) {
        Request request = requestRepo.findById(requestId).get();
        AgentRequests agentRequest = agentRequestsRepo.getAgentRequestsByRequestId(request.getRequestId());

        if (agentRequest != null) {
            if (!agentRequest.getStatus().equals("OFFERED")) {
                if (!request.getExpired()) {
                    Offer requestOffer = Offer.builder().requestId(request.getRequestId())
                            .price(price)
                            .dateInterval(dateInterval)
                            .tourInformation(tourInformation)
                            .build();
                    offerRepo.save(requestOffer);
                    agentRequest.setStatus(RequestStatus.OFFERED);
                    mqService.sendToQueue(requestOffer);
                    return requestOffer;
                }
            }
        }
        return null;
    }

    @Override
    public Offer getOffer(Long offerId) {
        return offerRepo.findById(offerId).get();
    }

    @Override
    public List<Offer> getAllOffers() {
        return offerRepo.findAll();
    }
}
