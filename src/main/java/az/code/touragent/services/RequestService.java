package az.code.touragent.services;

import az.code.touragent.models.Request;

import java.util.List;
import java.util.UUID;

public interface RequestService {
    void makeAgentRequests();

    Request getRequest(UUID requestId, String email);

    List<Request> getAllRequests(String email);

    String deleteRequest(UUID requestId, String email);

    List<Request> getOfferedRequests(String email);

    String sendToArchive(UUID requestId, String email);

    List<Request> getArchiveRequests(String email);
}
