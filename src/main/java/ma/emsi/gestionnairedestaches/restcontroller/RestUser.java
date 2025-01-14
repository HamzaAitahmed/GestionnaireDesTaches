package ma.emsi.gestionnairedestaches.restcontroller;


import ma.emsi.gestionnairedestaches.model.User;
import ma.emsi.gestionnairedestaches.repository.UserRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RestUser {
    UserRepository userRepository;

    RestUser (UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping(path="/Users")
    public List<User> users()
    {
        return userRepository.findAll();
    }

    @GetMapping(path="/Users/{id}")
    public User usersById(@PathVariable int id)
    {
        return userRepository.findById(id).orElse(null);
    }

    @PostMapping(path="/AddUser")
    public User addUser(@RequestBody User user)
    {
        return userRepository.save(user);
    }

    @PutMapping(path="/UpdateUser/{id}")
    public User updateUser(@PathVariable int id, @RequestBody User user)
    {
        return userRepository.findById(id).orElse(null);
    }


    @DeleteMapping(path="/DeleteUser/{id}")
    public void deleteUsersById(@PathVariable int id)
    {
        userRepository.deleteById(id);
    }
}
