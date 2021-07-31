package az.code.touragent.repositories;

import az.code.touragent.models.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface RequestRepository extends JpaRepository<Request, UUID> {
    @Query("SELECT request FROM Request request " +
            "WHERE request.expired=false ")
    List<Request> getNotExpiredRequests();

    Request getRequestByRequestId(UUID requestId);
}
