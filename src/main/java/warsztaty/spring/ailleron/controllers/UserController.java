package warsztaty.spring.ailleron.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import warsztaty.spring.ailleron.exceptions.UserExistException;
import warsztaty.spring.ailleron.exceptions.UserNotFoundException;
import warsztaty.spring.ailleron.model.User;
import warsztaty.spring.ailleron.model.UserList;
import warsztaty.spring.ailleron.services.UserService;

import javax.validation.Valid;
import java.util.Optional;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController()
public class UserController {

    @Autowired
    private UserService service;

    @GetMapping("/users")
    public UserList getAllUsers() {
        return new UserList(service.getAllUsers());
    }



    @GetMapping(value = "/users/{name}", headers = "X-API-VERSION=2")
    public User getSurnameByName(@PathVariable() String name) throws UserNotFoundException {
        Optional<User> user = service.getUserByName(name);
        if (user.isPresent()) {
            return user.get();
        }
        throw new UserNotFoundException("Brak użytkownika o imieniu: " + name);
    }


    @GetMapping(value = "/users/{id}")
    public ResponseEntity<Resource<User>> getUserById(@PathVariable() Long id) throws UserNotFoundException {
        Optional<User> user = service.getUserById(id);
        if (!user.isPresent()) {
            throw new UserNotFoundException("Brak użytkownika o ID: " + id);
        }
        Resource<User> resource = new Resource(user);
        ControllerLinkBuilder linkTo = linkTo(methodOn(this.getClass()).getAllUsers());
        resource.add(linkTo.withRel("all-users"));
        linkTo = linkTo(methodOn(this.getClass()).getUserById(id));
        resource.add(linkTo.withSelfRel());
        return ResponseEntity.ok(resource);
    }

    @PostMapping("/users")
    public ResponseEntity<Resource<User>> addUser(@Valid @RequestBody User user) throws UserExistException, UserNotFoundException {
        Optional<User> newUser = service.addUser(user);
        if (!newUser.isPresent()) {
            throw new UserExistException("Uzytkownik o imieniu: " + user.getName() + " istnieje");
        }
        Resource<User> resource = new Resource(user);
        ControllerLinkBuilder linkTo = linkTo(methodOn(this.getClass()).getUserById(user.getId().longValue()));
        resource.add(linkTo.withSelfRel());
        return ResponseEntity.status(HttpStatus.CREATED).body(resource);
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<User> modifyUser(@Valid @RequestBody User user, @PathVariable() Long id) throws UserNotFoundException {
        Optional<User> modifiedUser = service.modifyUser(user, id);
        if (!modifiedUser.isPresent()) {
            throw new UserNotFoundException("Brak użytkownika o ID: " + id);
        }
        return ResponseEntity.ok(modifiedUser.get());
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity deleteUser(@PathVariable() Long id) {
        service.deleteUser(id);
        return ResponseEntity.ok().build();
    }
}
