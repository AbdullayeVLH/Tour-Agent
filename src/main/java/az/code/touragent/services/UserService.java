package az.code.touragent.services;

import az.code.touragent.models.User;

public interface UserService {

    User findByEmail(String email);

    void saveUser(User user);
}
