package az.code.touragent.listeners;

import az.code.touragent.configs.RabbitMQConfig;
import az.code.touragent.enums.RequestStatus;
import az.code.touragent.models.AgentRequests;
import az.code.touragent.models.Request;
import az.code.touragent.models.Session;
import az.code.touragent.repositories.AgentRequestsRepository;
import az.code.touragent.repositories.RequestRepository;
import az.code.touragent.services.RequestService;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class StopQueueListener {

    RequestRepository requestRepo;
    RequestService requestService;
    AgentRequestsRepository agentRequestsRepo;

    public StopQueueListener(RequestRepository requestRepo, RequestService requestService, AgentRequestsRepository agentRequestsRepo) {
        this.requestRepo = requestRepo;
        this.requestService = requestService;
        this.agentRequestsRepo = agentRequestsRepo;
    }

    @RabbitListener(queues = RabbitMQConfig.STOP_QUEUE)
    public void stopRequestListener(Session session) {
        Optional<Request> stopRequest = requestRepo.getRequestByChatId(session.getChatId());
        if (stopRequest.isPresent()) {
            stopRequest.get().setExpired(true);
            stopRequest.get().setStatus(RequestStatus.DELETED);
            requestRepo.save(stopRequest.get());
            List<AgentRequests> agentRequests = agentRequestsRepo.getAgentRequestsByRequestId(stopRequest.get().getRequestId());
            agentRequests.forEach(agentRequest -> agentRequest.setStatus(RequestStatus.DELETED));
            agentRequestsRepo.saveAll(agentRequests);
        }
    }
}
