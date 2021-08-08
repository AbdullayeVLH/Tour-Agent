package az.code.touragent.listeners;

import az.code.touragent.configs.RabbitMQConfig;
import az.code.touragent.enums.RequestStatus;
import az.code.touragent.models.Accepted;
import az.code.touragent.models.AgentRequests;
import az.code.touragent.repositories.AcceptedRepository;
import az.code.touragent.repositories.AgentRequestsRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class AcceptedQueueListener {

    private final AcceptedRepository acceptedRepo;
    private final AgentRequestsRepository agentRequestsRepo;

    public AcceptedQueueListener(AcceptedRepository acceptedRepo, AgentRequestsRepository agentRequestsRepo) {
        this.acceptedRepo = acceptedRepo;
        this.agentRequestsRepo = agentRequestsRepo;
    }

    @RabbitListener(queues = RabbitMQConfig.ACCEPTED_QUEUE)
    public void stopRequestListener(Accepted acceptedOffer) {
        if (acceptedOffer != null) {
            Accepted accepted = Accepted.builder()
                    .requestId(acceptedOffer.getRequestId())
                    .agentEmail(acceptedOffer.getAgentEmail())
                    .contactInfo(acceptedOffer.getContactInfo())
                    .build();
            acceptedRepo.save(accepted);
            AgentRequests acceptedRequest = agentRequestsRepo.getAgentRequestsByRequestIdAndUserEmail(acceptedOffer.getRequestId(), acceptedOffer.getAgentEmail());
            acceptedRequest.setStatus(RequestStatus.ACCEPTED);
            agentRequestsRepo.save(acceptedRequest);
        }
    }
}
