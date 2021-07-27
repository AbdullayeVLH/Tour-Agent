package az.code.touragent.repositories;

import az.code.touragent.models.AgentRequests;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AgentRequestsRepository extends JpaRepository<AgentRequests, Long> {

    AgentRequests getAgentRequestsByRequestId(Long requestId);
}
