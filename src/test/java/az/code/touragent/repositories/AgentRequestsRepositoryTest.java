package az.code.touragent.repositories;

import az.code.touragent.enums.RequestStatus;
import az.code.touragent.models.AgentRequests;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class AgentRequestsRepositoryTest {

    @Autowired
    private AgentRequestsRepository agentRequestsRepo;

    @Test
    void itShouldGetAgentRequestsByRequestIdAndUserEmail() {
        UUID requestId = UUID.randomUUID();
        AgentRequests testAgentRequest = AgentRequests.builder()
                .requestId(requestId)
                .userEmail("test@gmail.com")
                .status(RequestStatus.UNSEEN).build();

        agentRequestsRepo.save(testAgentRequest);

        AgentRequests result = agentRequestsRepo.getAgentRequestsByRequestIdAndUserEmail(requestId, "test@gmail.com");

        assertThat(result).isEqualTo(testAgentRequest);
    }

    @Test
    void itShouldGetListOfAgentRequestsByUserEmail() {
        List<AgentRequests> agentRequestList = new ArrayList<>();
        AgentRequests firstTestAgentRequest = AgentRequests.builder()
                .requestId(UUID.randomUUID())
                .userEmail("test@gmail.com")
                .status(RequestStatus.UNSEEN).build();
        agentRequestsRepo.save(firstTestAgentRequest);
        AgentRequests secondTestAgentRequest = AgentRequests.builder()
                .requestId(UUID.randomUUID())
                .userEmail("test@gmail.com")
                .status(RequestStatus.OFFERED).build();
        agentRequestsRepo.save(secondTestAgentRequest);
        agentRequestList.add(firstTestAgentRequest);
        agentRequestList.add(secondTestAgentRequest);

        List<AgentRequests> result = agentRequestsRepo.getAgentRequestsByUserEmail("test@gmail.com");

        assertThat(result).isEqualTo(agentRequestList);

    }

    @Test
    void itShouldGetListOfAgentRequestsByRequestId() {
        UUID requestId = UUID.randomUUID();
        List<AgentRequests> agentRequestList = new ArrayList<>();
        AgentRequests firstTestAgentRequest = AgentRequests.builder()
                .requestId(requestId)
                .userEmail("first@gmail.com")
                .status(RequestStatus.UNSEEN).build();
        agentRequestsRepo.save(firstTestAgentRequest);
        AgentRequests secondTestAgentRequest = AgentRequests.builder()
                .requestId(requestId)
                .userEmail("second@gmail.com")
                .status(RequestStatus.OFFERED).build();
        agentRequestsRepo.save(secondTestAgentRequest);
        agentRequestList.add(firstTestAgentRequest);
        agentRequestList.add(secondTestAgentRequest);

        List<AgentRequests> result = agentRequestsRepo.getAgentRequestsByRequestId(requestId);

        assertThat(result).isEqualTo(agentRequestList);
    }

    @AfterEach
    void tearDown() {
        agentRequestsRepo.deleteAll();
    }
}