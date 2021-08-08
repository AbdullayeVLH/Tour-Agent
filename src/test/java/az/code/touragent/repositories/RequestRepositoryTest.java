package az.code.touragent.repositories;

import az.code.touragent.enums.RequestStatus;
import az.code.touragent.models.Request;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
@DataJpaTest
class RequestRepositoryTest {

    @Autowired
    private RequestRepository requestRepo;

    @Test
    void itShouldGetNotExpiredRequests() {
        Request firstTestRequest = Request.builder().requestId(UUID.randomUUID())
                .expired(false).lang("az").chatId(1L)
                .status(RequestStatus.UNSEEN).build();
        Request secondTestRequest = Request.builder().requestId(UUID.randomUUID())
                .expired(false).lang("az").chatId(2L)
                .status(RequestStatus.SEEN).build();
        Request thirdTestRequest = Request.builder().requestId(UUID.randomUUID())
                .expired(true).lang("az").chatId(3L)
                .status(RequestStatus.UNSEEN).build();
        requestRepo.save(firstTestRequest);
        requestRepo.save(secondTestRequest);
        requestRepo.save(thirdTestRequest);

        List<Request> result = requestRepo.getNotExpiredRequests();

        assertThat(result.size()).isEqualTo(2);

    }

    @Test
    void itShouldGetRequestByRequestId() {
        UUID requestId = UUID.randomUUID();
        Request testRequest = Request.builder().requestId(requestId)
                .expired(false).lang("az").chatId(1L)
                .status(RequestStatus.UNSEEN).build();
        requestRepo.save(testRequest);

        Request result = requestRepo.getRequestByRequestId(requestId);

        assertThat(result).isEqualTo(testRequest);

    }

    @Test
    void itShouldGetRequestByChatId() {
        Request firstTestRequest = Request.builder().requestId(UUID.randomUUID())
                .expired(false).lang("az").chatId(1L)
                .status(RequestStatus.UNSEEN).build();
        requestRepo.save(firstTestRequest);

        Optional<Request> result = requestRepo.getRequestByChatId(1L);

        assertThat(result).isEqualTo(firstTestRequest);

    }

    @AfterEach
    void tearDown() {
        requestRepo.deleteAll();
    }
}