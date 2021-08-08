package az.code.touragent.repositories;

import az.code.touragent.models.Archive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class ArchiveRepositoryTest {

    @Autowired
    private ArchiveRepository archiveRepo;

    @Test
    void itShouldGetRequestsSentToArchiveByUserEmail() {
        List<Archive> testArchiveList = new ArrayList<>();
        Archive firstTestArchive = Archive.builder().id(1L)
                .status("ACCEPTED")
                .userEmail("test@gmail.com")
                .requestId(UUID.randomUUID()).build();
        archiveRepo.save(firstTestArchive);
        Archive secondTestArchive = Archive.builder().id(2L)
                .status("OFFERED")
                .userEmail("test@gmail.com")
                .requestId(UUID.randomUUID()).build();
        archiveRepo.save(secondTestArchive);
        testArchiveList.add(firstTestArchive);
        testArchiveList.add(secondTestArchive);

        List<Archive> result = archiveRepo.getArchivesByUserEmail("test@gmail.com");

        assertThat(result.size()).isEqualTo(testArchiveList.size());

    }

    @Test
    void itShouldGetRequestSentToArchiveByRequestId() {
        UUID requestId = UUID.randomUUID();
        Archive testArchive = Archive.builder().id(1L)
                .status("ACCEPTED")
                .userEmail("test@gmail.com")
                .requestId(requestId).build();
        archiveRepo.save(testArchive);

        Archive result = archiveRepo.getArchiveByRequestId(requestId);

        assertThat(result.getStatus()).isEqualTo(testArchive.getStatus());
    }

    @AfterEach
    void tearDown() {
        archiveRepo.deleteAll();
    }
}