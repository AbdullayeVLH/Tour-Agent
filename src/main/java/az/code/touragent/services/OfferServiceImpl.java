package az.code.touragent.services;

import az.code.touragent.dtos.AcceptedOfferDto;
import az.code.touragent.dtos.MakeOfferDto;
import az.code.touragent.enums.RequestStatus;
import az.code.touragent.exceptions.AlreadyOffered;
import az.code.touragent.exceptions.NotAnyAcceptedOffer;
import az.code.touragent.exceptions.RequestExpired;
import az.code.touragent.models.Accepted;
import az.code.touragent.models.AgentRequests;
import az.code.touragent.models.Offer;
import az.code.touragent.models.Request;
import az.code.touragent.repositories.AcceptedRepository;
import az.code.touragent.repositories.AgentRequestsRepository;
import az.code.touragent.repositories.OfferRepository;
import az.code.touragent.repositories.RequestRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class OfferServiceImpl implements OfferService {

    private final OfferRepository offerRepo;
    private final RequestRepository requestRepo;
    private final AgentRequestsRepository agentRequestsRepo;
    private final RabbitMQService service;
    private final AcceptedRepository acceptedRepo;

    public OfferServiceImpl(OfferRepository offerRepo, RequestRepository requestRepo,
                            AgentRequestsRepository agentRequestsRepo, RabbitMQService service, AcceptedRepository acceptedRepo) {
        this.offerRepo = offerRepo;
        this.requestRepo = requestRepo;
        this.agentRequestsRepo = agentRequestsRepo;
        this.service = service;
        this.acceptedRepo = acceptedRepo;
    }

    @Override
    public Offer makeOffer(MakeOfferDto dto, UUID requestId, String email) {
        Request request = requestRepo.getRequestByRequestId(requestId);
        AgentRequests agentRequest = agentRequestsRepo.getAgentRequestsByRequestIdAndUserEmail(request.getRequestId(), email);
        if (!agentRequest.getStatus().equals(RequestStatus.OFFERED) && !agentRequest.getStatus().equals(RequestStatus.DELETED)) {
            Boolean isExpired = request.getExpired();
            if (!isExpired) {
                Offer requestOffer = Offer.builder().requestId(request.getRequestId())
                        .price(dto.getPrice())
                        .dateInterval(dto.getDateInterval())
                        .tourInformation(dto.getTourInformation())
                        .userEmail(email)
                        .build();
                offerRepo.save(requestOffer);
                agentRequest.setStatus(RequestStatus.OFFERED);
                agentRequestsRepo.save(agentRequest);
                service.sendToOfferQueue(requestOffer);
                return requestOffer;
            }
            throw new RequestExpired();
        }
        throw new AlreadyOffered();
    }

    @Override
    public Offer getOffer(Long offerId) {
        offerRepo.getById(offerId);
        return offerRepo.getById(offerId);
    }

    @Override
    public List<Offer> getAllOffers() {
        return offerRepo.findAll();
    }

    @Override
    public List<AcceptedOfferDto> getAcceptedOffers(String email) {
        List<Accepted> acceptedOffers = acceptedRepo.getAcceptedsByAgentEmail(email);
        List<AcceptedOfferDto> result = new ArrayList<>();
        acceptedOffers.forEach(acceptedOffer -> {
            Offer offer = offerRepo.getOfferByRequestId(acceptedOffer.getRequestId());
            Request request = requestRepo.getRequestByRequestId(acceptedOffer.getRequestId());
            MakeOfferDto offerDto = MakeOfferDto.builder()
                    .price(offer.getPrice())
                    .dateInterval(offer.getDateInterval())
                    .tourInformation(offer.getTourInformation())
                    .build();
            AcceptedOfferDto dto = AcceptedOfferDto.builder()
                    .contactInfo(acceptedOffer.getContactInfo())
                    .data(request.getData())
                    .offer(offerDto)
                    .build();
            result.add(dto);
        });
        return result;
    }

    @Override
    public AcceptedOfferDto getUserContactInfo(UUID requestId, String email) {
        Request request = requestRepo.getRequestByRequestId(requestId);
        Offer offer = offerRepo.getOfferByRequestId(requestId);
        MakeOfferDto offerDto = MakeOfferDto.builder()
                .tourInformation(offer.getTourInformation())
                .dateInterval(offer.getDateInterval())
                .price(offer.getPrice())
                .build();
        Accepted accepted = acceptedRepo.getAcceptedByAgentEmailAndRequestId(email, requestId);
        if (accepted != null) {
            return AcceptedOfferDto.builder()
                    .data(request.getData())
                    .contactInfo(accepted.getContactInfo())
                    .offer(offerDto)
                    .build();
        }
        throw new NotAnyAcceptedOffer();
    }
}
