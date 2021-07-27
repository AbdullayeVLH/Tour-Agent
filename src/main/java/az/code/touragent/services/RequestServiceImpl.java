package az.code.touragent.services;

import az.code.touragent.enums.RequestStatus;
import az.code.touragent.models.AgentRequests;
import az.code.touragent.models.Request;
import az.code.touragent.models.User;
import az.code.touragent.repositories.AgentRequestsRepository;
import az.code.touragent.repositories.RequestRepository;
import az.code.touragent.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RequestServiceImpl implements RequestService{

    private final UserRepository userRepo;
    private final RequestRepository requestRepo;
    private final AgentRequestsRepository agentRequestsRepo;

    public RequestServiceImpl(UserRepository userRepo, RequestRepository requestRepo,
                              AgentRequestsRepository agentRequestsRepo) {
        this.userRepo = userRepo;
        this.requestRepo = requestRepo;
        this.agentRequestsRepo = agentRequestsRepo;
    }

    @Override
    public void makeAgentRequests() {
        List<Request> requests = requestRepo.findAll();
        List<User> users = userRepo.findAll();
        AgentRequests agentRequest = new AgentRequests();
        requests.forEach(request -> {
            if (!request.getExpired()) {
                users.forEach(user -> agentRequest.toBuilder()
                        .userEmail(user.getEmail())
                        .requestId(request.getId())
                        .status(RequestStatus.UNSEEN)
                        .build());
                agentRequestsRepo.save(agentRequest);
            }
        });
    }
}
