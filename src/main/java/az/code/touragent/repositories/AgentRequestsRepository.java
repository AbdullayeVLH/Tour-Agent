package az.code.touragent.repositories;

import az.code.touragent.models.AgentRequests;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AgentRequestsRepository extends JpaRepository<AgentRequests, Long> {

    AgentRequests getAgentRequestsByRequestIdAndUserEmail(UUID requestId, String email);

    List<AgentRequests> getAgentRequestsByUserEmail(String email);

    List<AgentRequests> getAgentRequestsByRequestId(UUID requestId);

}
