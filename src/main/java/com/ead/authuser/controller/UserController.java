package com.ead.authuser.controller;

import com.ead.authuser.dtos.UserDTO;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.services.UserService;
import com.ead.authuser.specification.SpecificationTemplates;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping
    public ResponseEntity<Page<UserModel>> getAllUsers(SpecificationTemplates.UserSpec spec,
                                           @PageableDefault(page = 0, size = 10, sort = "userId", direction = Sort.Direction.ASC) Pageable pageable) {

        Page<UserModel>userModelsPage = userService.findAll(pageable, spec);
        if (!userModelsPage.isEmpty()) {
            for (UserModel user : userModelsPage.toList()) {
                user.add(linkTo(methodOn(UserController.class).getUserById(user.getUserId())).withSelfRel());
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(userModelsPage);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUserById(@PathVariable(value = "userId") UUID userId) {
        Optional<UserModel> userModelOptional = userService.findById(userId);

        if (userModelOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        return ResponseEntity.status(HttpStatus.OK).body(userModelOptional.get());

    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteUser(@PathVariable(value = "userId") UUID userId) {
        Optional<UserModel> userModelOptional = userService.findById(userId);

        if (userModelOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        userService.delete(userModelOptional.get());

        return ResponseEntity.status(HttpStatus.OK).body("User deleted successfully");
    }

    @PutMapping("/{userId}")
    public ResponseEntity<Object> updateUser(@PathVariable(value = "userId") UUID userId,
                                             @RequestBody @Validated(UserDTO.UserView.UserPut.class)
                                             @JsonView(UserDTO.UserView.UserPut.class) UserDTO userDTO) {
        Optional<UserModel> userModelOptional = userService.findById(userId);

        if (userModelOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        var userModel = userModelOptional.get();
        userModel.setFullName(userDTO.getFullName());
        userModel.setPhoneNumber(userDTO.getPhoneNumber());
        userModel.setCpf(userDTO.getCpf());
        userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        userService.save(userModel);

        return ResponseEntity.status(HttpStatus.OK).body(userModel);
    }

    @PutMapping("/{userId}/password")
    public ResponseEntity<Object> updatePassword(@PathVariable(value = "userId") UUID userId,
                                             @RequestBody @Validated(UserDTO.UserView.PasswordPut.class)
                                             @JsonView(UserDTO.UserView.PasswordPut.class) UserDTO userDTO) {
        Optional<UserModel> userModelOptional = userService.findById(userId);

        if (userModelOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        if (userModelOptional.get().getPassword().equals(userDTO.getOldPassword())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: Mismatched password");
        }

        var userModel = userModelOptional.get();
        userModel.setPassword(userDTO.getOldPassword());
        userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        userService.save(userModel);

        return ResponseEntity.status(HttpStatus.OK).body("Password updated successfully!!!");
    }

    @PutMapping("/{userId}/image")
    public ResponseEntity<Object> updateImage(@PathVariable(value = "userId") UUID userId,
                                             @RequestBody @Validated(UserDTO.UserView.ImagePut.class)
                                             @JsonView(UserDTO.UserView.ImagePut.class) UserDTO userDTO) {
        Optional<UserModel> userModelOptional = userService.findById(userId);

        if (userModelOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        var userModel = userModelOptional.get();
        userModel.setImageUrl(userDTO.getImageUrl());
        userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        userService.save(userModel);

        return ResponseEntity.status(HttpStatus.OK).body(userModel);
    }


}
