package az.code.touragent.repositories;

import az.code.touragent.models.Archive;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ArchiveRepository extends JpaRepository<Archive, Long> {
    List<Archive> getArchivesByUserEmail(String email);

    Archive getArchiveByRequestId(UUID requestId);
}
