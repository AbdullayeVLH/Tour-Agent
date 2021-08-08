package az.code.touragent.services;

import az.code.touragent.models.User;
import az.code.touragent.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock private UserRepository userRepo;

    private UserServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new UserServiceImpl(userRepo);
    }

    @Test
    void saveUser() {
        User testUser = User.builder().email("test@gmail.com")
                .firstName("Test1").lastName("Test2")
                .companyName("Test")
                .voen("Test").build();

        service.saveUser(testUser);

        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);

        verify(userRepo).save(userArgumentCaptor.capture());

        User result = userArgumentCaptor.getValue();

        assertThat(result).isEqualTo(testUser);
    }
}