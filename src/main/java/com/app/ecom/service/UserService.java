package com.app.ecom.service;

import com.app.ecom.dto.AddressDTO;
import com.app.ecom.dto.UserRequest;
import com.app.ecom.dto.UserResponse;
import com.app.ecom.models.Address;
import com.app.ecom.repository.UserRepository;
import com.app.ecom.models.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public List<UserResponse> fetchallUsers(){

        return userRepository.findAll().
                stream()
                .map(this::mapToUserResponse)
                .collect(Collectors.toList());
    }

    public void createUser(@RequestBody UserRequest userRequest){
        User user=new User();
        updateUserFromResquest(user,userRequest);
        userRepository.save(user);
    }

    private void updateUserFromResquest(User user, UserRequest userRequest) {
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setEmail(userRequest.getEmail());
        user.setPhone(userRequest.getPhone());

        if (userRequest.getAddress()!=null){
            Address address = new Address();
            address.setCity(userRequest.getAddress().getCity());
            address.setCountry(userRequest.getAddress().getCountry());
            address.setState(userRequest.getAddress().getState());
            address.setStreet(userRequest.getAddress().getStreet());
            address.setZipcode(userRequest.getAddress().getZipcode());
            user.setAddress(address);
        }
    }

    public Optional<UserResponse> fetchUser(Long id){

        return userRepository.findById(id)
                .map(this::mapToUserResponse);

    }

    public boolean updateUser(Long id,UserRequest updatedUserRequest){

        return userRepository.findById(id)
                .map(existingUser -> {
                    updateUserFromResquest(existingUser,updatedUserRequest);
                    userRepository.save(existingUser);
                    return true;
                }).orElse(false);
    }
    private UserResponse mapToUserResponse(User user){
        UserResponse response = new UserResponse();
        response.setId(String.valueOf(user.getId()));
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setEmail(user.getEmail());
        response.setPhone(user.getPhone());
        response.setRole(user.getRole());

        if (user.getAddress()!=null){
            AddressDTO addressDTO=new AddressDTO();
            addressDTO.setCity(user.getAddress().getCity());
            addressDTO.setCountry(user.getAddress().getCountry());
            addressDTO.setState(user.getAddress().getState());
            addressDTO.setStreet(user.getAddress().getStreet());
            addressDTO.setZipcode(user.getAddress().getZipcode());
            response.setAddress(addressDTO);
        }
        return response;

    }

}
