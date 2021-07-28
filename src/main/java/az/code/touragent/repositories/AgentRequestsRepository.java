package az.code.touragent.repositories;

import az.code.touragent.models.AgentRequests;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AgentRequestsRepository extends JpaRepository<AgentRequests, Long> {

    AgentRequests getAgentRequestsByRequestId(UUID requestId);
}
