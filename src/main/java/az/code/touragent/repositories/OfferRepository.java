package az.code.touragent.repositories;

import az.code.touragent.models.Offer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OfferRepository extends JpaRepository<Offer, Long> {
    Offer getOfferByRequestId(UUID requestId);
}
