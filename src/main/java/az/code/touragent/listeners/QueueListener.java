package az.code.touragent.listeners;

import az.code.touragent.configs.RabbitMQConfig;
import az.code.touragent.enums.RequestStatus;
import az.code.touragent.models.Request;
import az.code.touragent.models.Session;
import az.code.touragent.repositories.AgentRequestsRepository;
import az.code.touragent.repositories.RequestRepository;
import az.code.touragent.services.RequestService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;


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
            Request request = Request.builder()
                    .requestId(session.getSessionId())
                    .chatId(session.getChatId())
                    .lang(session.getLang())
                    .data(session.getData())
                    .expired(false)
                    .status(RequestStatus.LISTENED)
                    .build();
            requestRepo.save(request);
        requestService.makeAgentRequests();
    }
}
