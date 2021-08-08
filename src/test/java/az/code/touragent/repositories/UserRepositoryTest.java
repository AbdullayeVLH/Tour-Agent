package az.code.touragent.repositories;

import az.code.touragent.models.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepo;

    @Test
    void itShouldFindUserByEmail() {
        User testUser = User.builder().email("user@gmail.com")
                .firstName("User").lastName("User12")
                .companyName("Tour").voen("123456789")
                .build();
        userRepo.save(testUser);
        User result = userRepo.findByEmail("user@gmail.com");
        assertThat(result.getFirstName()).isEqualTo(testUser.getFirstName());
    }

    @AfterEach
    void tearDown() {
        userRepo.deleteAll();
    }
}