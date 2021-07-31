package az.code.touragent.listener;

import az.code.touragent.configs.RabbitMQConfig;
import az.code.touragent.enums.RequestStatus;
import az.code.touragent.models.AgentRequests;
import az.code.touragent.models.Request;
import az.code.touragent.models.Session;
import az.code.touragent.repositories.AgentRequestsRepository;
import az.code.touragent.repositories.RequestRepository;
import az.code.touragent.services.RequestService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class QueueListener {

    RequestRepository requestRepo;
    RequestService requestService;
    AgentRequestsRepository agentRequestsRepo;

    public QueueListener(RequestRepository requestRepo, RequestService requestService, AgentRequestsRepository agentRequestsRepo) {
        this.requestRepo = requestRepo;
        this.requestService = requestService;
        this.agentRequestsRepo = agentRequestsRepo;
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE)
    public void requestListener(Session session) {
        if (session != null) {
            Request request = Request.builder()
                    .requestId(session.getSessionId())
                    .chatId(session.getChatId())
                    .lang(session.getLang())
                    .data(session.getData())
                    .expired(false)
                    .status(RequestStatus.LISTENED)
                    .build();
            requestRepo.save(request);
        }
        requestService.makeAgentRequests();
    }

//    @RabbitListener(queues = RabbitMQConfig.STOP_QUEUE)
//    public void stopRequestListener(Session session) {
//        Request stopRequest = requestRepo.getById(session.getSessionId());
//        stopRequest.setExpired(true);
//        stopRequest.setStatus(RequestStatus.DELETED);
//        requestRepo.save(stopRequest);
//        List<AgentRequests> agentRequests = agentRequestsRepo.getAgentRequestsByRequestId(session.getSessionId());
//        agentRequests.forEach(agentRequest -> agentRequest.setStatus(RequestStatus.DELETED));
//        agentRequestsRepo.saveAll(agentRequests);
//    }
}
