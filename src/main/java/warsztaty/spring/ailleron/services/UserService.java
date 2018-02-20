package warsztaty.spring.ailleron.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import warsztaty.spring.ailleron.model.User;
import warsztaty.spring.ailleron.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private UserRepository repository;

    @Autowired
    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public List<User> getAllUsers() {
        return repository.findAll();
    }

    public Optional<User> getUserByName(String name) {
        return repository.getUserByName(name);
    }

    public Optional<User> addUser(User user) {
        Optional<User> existingUser = getUserByName(user.getName());
        if (existingUser.isPresent()) {
            return Optional.empty();
        }
        user = repository.save(user);
        return Optional.of(user);
    }

    public Optional<User> modifyUser(User user, Long id) {
        Optional<User> modifiedUser = getUserById(id);
        if (!modifiedUser.isPresent()) {
            return Optional.empty();
        }

        modifiedUser.get().setAge(user.getAge());
        modifiedUser.get().setName(user.getName());
        modifiedUser.get().setSurname(user.getSurname());

        return modifiedUser;
    }

    public Optional<User> getUserById(Long id) {
        return repository.findById(id);
    }

    public void deleteUser(Long id) {
        Optional<User> user = getUserById(id);
        if (user.isPresent()) {
            repository.deleteById(id);
        }
    }
}
