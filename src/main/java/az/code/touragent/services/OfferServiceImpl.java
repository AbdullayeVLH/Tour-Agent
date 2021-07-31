package az.code.touragent.services;

import az.code.touragent.enums.RequestStatus;
import az.code.touragent.exceptions.AlreadyOffered;
import az.code.touragent.exceptions.RequestExpired;
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
    public Offer makeOffer(String price, String dateInterval, String tourInformation, UUID requestId, String email) {
        Request request = requestRepo.getRequestByRequestId(requestId);
        AgentRequests agentRequest = agentRequestsRepo.getAgentRequestsByRequestIdAndUserEmail(request.getRequestId(), email);
        if (!agentRequest.getStatus().equals(RequestStatus.OFFERED) && !agentRequest.getStatus().equals(RequestStatus.DELETED)) {
            Boolean isExpired = request.getExpired();
            if (!isExpired) {
                Offer requestOffer = Offer.builder().requestId(request.getRequestId())
                        .price(price)
                        .dateInterval(dateInterval)
                        .tourInformation(tourInformation)
                        .build();
                offerRepo.save(requestOffer);
                agentRequest.setStatus(RequestStatus.OFFERED);
                agentRequestsRepo.save(agentRequest);
//                mqService.sendToOfferQueue(requestOffer);
                return requestOffer;
            }
            throw new RequestExpired();
        }
        throw new AlreadyOffered();
    }

    @Override
    public Offer getOffer(Long offerId) {
        return offerRepo.getById(offerId);
    }

    @Override
    public List<Offer> getAllOffers() {
        return offerRepo.findAll();
    }
}
