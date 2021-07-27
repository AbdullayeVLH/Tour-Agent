package az.code.touragent.repositories;

import az.code.touragent.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String>{

    User findByEmail(String email);
}
