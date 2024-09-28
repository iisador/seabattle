package ru.isador.games.seabattle.domain.user;

import io.quarkus.elytron.security.common.BcryptUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class UserService {

    private final UserRepository userRepository;

    @Inject
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public User newUser(String username, String password) {
        User u = new User(username, BcryptUtil.bcryptHash(password), "user");
        userRepository.persist(u);
        return u;
    }

    @Transactional
    public boolean hasUser(String username) {
        return userRepository.count("name", username) > 0;
    }
}
