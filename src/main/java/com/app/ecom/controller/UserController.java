package com.app.ecom.controller;
import com.app.ecom.dto.UserRequest;
import com.app.ecom.dto.UserResponse;
import com.app.ecom.models.User;
import com.app.ecom.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    // mappings
//    private List<User> userList = new ArrayList<>();

    @GetMapping
//    @RequestMapping(value = "/api/users",method=RequestMethod.GET)
    public ResponseEntity<List<UserResponse>> getAllUsers(){
        return new ResponseEntity<>(userService.fetchallUsers(),HttpStatus.OK);
       // return ResponseEntity.ok(userService.fetchallUsers());
    }

    //adding users
    @PostMapping
    public ResponseEntity<String> createUsers(@RequestBody UserRequest userRequest){
        userService.createUser(userRequest);
        return new ResponseEntity<>("User added successfully",HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getAllUsers(@PathVariable long id){
//        User user = userService.fetchUser(id);
//        if (user==null){
//            return ResponseEntity.notFound().build();
//        }
//        return new ResponseEntity<>(userService.fetchUser(id),HttpStatus.OK);
        return userService.fetchUser(id)
                .map(ResponseEntity::ok)
                .orElseGet(()->ResponseEntity.notFound().build());

    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateUsers(@PathVariable long id,@RequestBody UserRequest updatedUserRequest){
        if(userService.updateUser(id,updatedUserRequest)){
            return new ResponseEntity<>("User updated successfully",HttpStatus.OK);
        }
        return ResponseEntity.notFound().build();
    }
}
