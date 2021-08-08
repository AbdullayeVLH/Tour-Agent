package az.code.touragent.repositories;

import az.code.touragent.models.Accepted;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AcceptedRepository extends JpaRepository<Accepted, UUID> {

    List<Accepted> getAcceptedsByAgentEmail(String email);

    Accepted getAcceptedByAgentEmailAndRequestId(String email, UUID requestId);
}
