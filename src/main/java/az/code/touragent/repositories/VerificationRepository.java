package az.code.touragent.repositories;

import az.code.touragent.models.Verification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationRepository extends JpaRepository<Verification, String> {
}
