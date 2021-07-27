package az.code.touragent.services;

import az.code.touragent.enums.RequestStatus;
import az.code.touragent.models.AgentRequests;
import az.code.touragent.models.Offer;
import az.code.touragent.models.Request;
import az.code.touragent.repositories.AgentRequestsRepository;
import az.code.touragent.repositories.OfferRepository;
import az.code.touragent.repositories.RequestRepository;
import org.springframework.stereotype.Service;

@Service
public class OfferServiceImpl implements OfferService {

    private final OfferRepository offerRepo;
    private final RequestRepository requestRepo;
    private final AgentRequestsRepository agentRequestsRepo;

    public OfferServiceImpl(OfferRepository offerRepo, RequestRepository requestRepo,
                            AgentRequestsRepository agentRequestsRepo) {
        this.offerRepo = offerRepo;
        this.requestRepo = requestRepo;
        this.agentRequestsRepo = agentRequestsRepo;
    }

    @Override
    public Offer makeOffer(String price, String dateInterval, String tourInformation, Long requestId) {
        Request request = requestRepo.findById(requestId).get();
        AgentRequests agentRequest = agentRequestsRepo.getAgentRequestsByRequestId(request.getId());
        Offer requestOffer = new Offer();
        if (agentRequest!=null) {
            if (!agentRequest.getStatus().equals("OFFERED")) {
                if (!request.getExpired()) {
                    requestOffer.toBuilder().requestId(request.getId())
                            .price(price)
                            .dateInterval(dateInterval)
                            .tourInformation(tourInformation)
                            .build();
                    offerRepo.save(requestOffer);
                    agentRequest.setStatus(RequestStatus.OFFERED);
                    return requestOffer;
                }
            }
        }
        return null;
    }
}
