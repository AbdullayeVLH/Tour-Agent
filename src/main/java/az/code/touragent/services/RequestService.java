package az.code.touragent.services;

import az.code.touragent.models.Request;

import java.util.List;
import java.util.UUID;

public interface RequestService {
    void makeAgentRequests();

    Request getRequest(UUID requestId);

    List<Request> getAllRequests();
}
