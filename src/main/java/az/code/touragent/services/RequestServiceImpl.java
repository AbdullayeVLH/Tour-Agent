package az.code.touragent.services;

import az.code.touragent.enums.RequestStatus;
import az.code.touragent.exceptions.AlreadyArchived;
import az.code.touragent.exceptions.DeleteException;
import az.code.touragent.exceptions.RequestExpired;
import az.code.touragent.models.*;
import az.code.touragent.repositories.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class RequestServiceImpl implements RequestService {

    private final UserRepository userRepo;
    private final RequestRepository requestRepo;
    private final AgentRequestsRepository agentRequestsRepo;
    private final ArchiveRepository archiveRepo;

    public RequestServiceImpl(UserRepository userRepo, RequestRepository requestRepo,
                              AgentRequestsRepository agentRequestsRepo, ArchiveRepository archiveRepo) {
        this.userRepo = userRepo;
        this.requestRepo = requestRepo;
        this.agentRequestsRepo = agentRequestsRepo;
        this.archiveRepo = archiveRepo;
    }

    @Override
    @Transactional
    public void makeAgentRequests() {
        List<Request> requests = requestRepo.findAll();
        List<User> users = userRepo.findAll();
        requests.forEach(request -> {
            if (request.getStatus().equals(RequestStatus.LISTENED) && !request.getExpired()) {
                users.forEach(user -> {
                    if (agentRequestsRepo.getAgentRequestsByRequestId(request.getRequestId()).isEmpty())
                        agentRequestsRepo.save(new AgentRequests().toBuilder()
                                .userEmail(user.getEmail())
                                .requestId(request.getRequestId())
                                .status(RequestStatus.UNSEEN)
                                .build());
                });
            }
        });
    }

    @Override
    @Transactional(readOnly = true)
    public Request getRequest(UUID requestId, String email) {
        AgentRequests requests = agentRequestsRepo.getAgentRequestsByRequestIdAndUserEmail(requestId, email);
        Request request = requestRepo.getById(requestId);
        if (!requests.getStatus().equals(RequestStatus.DELETED) && !request.getExpired()) {
            requests.setStatus(RequestStatus.SEEN);
            return request;
        }
        throw new RequestExpired();
    }

    @Override
    public List<Request> getAllRequests(String email) {
        List<Request> requests = requestRepo.getNotExpiredRequests();
        List<Request> results = new ArrayList<>();
        requests.forEach(request -> {
            AgentRequests agentRequests = agentRequestsRepo.getAgentRequestsByRequestIdAndUserEmail(request.getRequestId(), email);
            if (!agentRequests.getStatus().equals(RequestStatus.DELETED) && !request.getExpired()) {
                results.add(request);
            }
        });
        return results;
    }

    @Override
    public String deleteRequest(UUID requestId, String email) {
        AgentRequests agentRequest = agentRequestsRepo.getAgentRequestsByRequestIdAndUserEmail(requestId, email);
        if (agentRequest != null) {
            agentRequest.setStatus(RequestStatus.DELETED);
            agentRequestsRepo.save(agentRequest);
            return "Request is deleted";
        }
        throw new DeleteException();
    }

    @Override
    public List<Request> getOfferedRequests(String email) {
        List<AgentRequests> agentRequests = agentRequestsRepo.getAgentRequestsByUserEmail(email);
        List<Request> requests = new ArrayList<>();
        agentRequests.forEach(agentRequest -> {
            if (agentRequest.getStatus().equals(RequestStatus.OFFERED)) {
                requests.add(requestRepo.getById(agentRequest.getRequestId()));
            }
        });
        return requests;
    }

    @Override
    public String sendToArchive(UUID requestId, String email) {
        AgentRequests request = agentRequestsRepo.getAgentRequestsByRequestIdAndUserEmail(requestId, email);
        Archive previous = archiveRepo.getArchiveByRequestId(requestId);
        if (previous==null && request != null && !request.getStatus().equals(RequestStatus.DELETED)) {
            Archive archive = Archive.builder()
                    .userEmail(email)
                    .requestId(requestId)
                    .status(request.getStatus().toString())
                    .build();
            archiveRepo.save(archive);
            return "Request saved";
        }
        throw new AlreadyArchived();
    }

    @Override
    public List<Request> getArchiveRequests(String email) {
        List<Archive> archives = archiveRepo.getArchivesByUserEmail(email);
        List<Request> archiveRequests = new ArrayList<>();
        archives.forEach(archive -> archiveRequests.add(requestRepo.getById(archive.getRequestId())));
        return archiveRequests;
    }
}
