package com.app.ecom;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.autoconfigure.WebMvcProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // mappings
    private List<User> userList = new ArrayList<>();

    @GetMapping("/api/users")
    public ResponseEntity<List<User>> getAllUsers(){
        return new ResponseEntity<>(userService.fetchallUsers(),HttpStatus.OK);
       // return ResponseEntity.ok(userService.fetchallUsers());
    }

    //adding users
    @PostMapping("/api/users")
    public ResponseEntity<String> createUsers(@RequestBody  User user){
        userService.createUser(user);
        return new ResponseEntity<>("User added successfully",HttpStatus.OK);
    }

    @GetMapping("/api/users/{id}")
    public ResponseEntity<User> getAllUsers(@PathVariable long id){
        User user = userService.fetchUser(id);
        if (user==null){
            return ResponseEntity.notFound().build();
        }
        return new ResponseEntity<>(userService.fetchUser(id),HttpStatus.OK);

    }
}
