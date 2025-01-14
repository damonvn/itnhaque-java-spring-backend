package com.onlinecode.itnhaque.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.onlinecode.itnhaque.domain.User;
import com.onlinecode.itnhaque.domain.request.ReqChangeUserPasswordDTO;
import com.onlinecode.itnhaque.domain.response.ResCreateUserDTO;
import com.onlinecode.itnhaque.domain.response.ResUpdateUserDTO;
import com.onlinecode.itnhaque.domain.response.ResultPaginationDTO;
import com.onlinecode.itnhaque.service.UserService;
import com.onlinecode.itnhaque.util.annotation.ApiMessage;
import com.onlinecode.itnhaque.util.error.IdInvalidException;
import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class UserController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/user")
    @ApiMessage("Create a new user")
    public ResponseEntity<ResCreateUserDTO> createNewUser(@Valid @RequestBody User reqUser)
            throws IdInvalidException {
        boolean isEmailExist = this.userService.isEmailExist(reqUser.getEmail());
        if (isEmailExist) {
            throw new IdInvalidException(
                    "Email " + reqUser.getEmail() + "đã tồn tại, vui lòng sử dụng email khác.");
        }

        String hashPassword = this.passwordEncoder.encode(reqUser.getPassword());
        reqUser.setPassword(hashPassword);
        User resUser = this.userService.handleCreateUser(reqUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(this.userService.convertToResCreateUserDTO(resUser));
    }

    @PutMapping("/user/password")
    @ApiMessage("Change user password")
    public ResponseEntity<Void> changePassword(@Valid @RequestBody ReqChangeUserPasswordDTO reqUser)
            throws IdInvalidException {

        User userDB = this.userService.fetchUserById(reqUser.getId());
        if (userDB == null) {
            throw new IdInvalidException("this user does not exist");
        }

        if (!this.passwordEncoder.matches(reqUser.getOldPassword(), userDB.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        String hashNewPassword = this.passwordEncoder.encode(reqUser.getNewPassword());

        userDB.setPassword(hashNewPassword);
        return ResponseEntity.status(HttpStatus.OK).body(this.userService.handleChangePassword(userDB));
    }

    @GetMapping("user/{id}")
    @ApiMessage("Fetch user by id")
    public ResponseEntity<User> fetchById(@PathVariable("id") int id) throws IdInvalidException {
        User user = this.userService.fetchUserById(id);
        if (user == null) {
            throw new IdInvalidException("id = " + id + " does not exist");
        }
        return ResponseEntity.ok().body(user);
    }

    @PutMapping("/user")
    @ApiMessage("Update a user")
    public ResponseEntity<ResUpdateUserDTO> updateUser(@RequestBody User reqUser) throws IdInvalidException {
        User resUser = this.userService.handleUpdateUser(reqUser);
        if (resUser == null) {
            throw new IdInvalidException("User với id = " + reqUser.getId() + " không tồn tại");
        }
        User updateUser = this.userService.handleUpdateUser(resUser);
        return ResponseEntity.ok().body(this.userService.convertToResUpdateUserDTO(updateUser));
    }

    @DeleteMapping("/user/{id}")
    @ApiMessage("Delete a user")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") int id)
            throws IdInvalidException {
        User currentUser = this.userService.fetchUserById(id);
        if (currentUser == null) {
            throw new IdInvalidException("User với id = " + id + " không tồn tại");
        }

        this.userService.handleDeleteUser(id);
        return ResponseEntity.ok(null);
    }

    @GetMapping("/user")
    @ApiMessage("Get all users")
    public ResponseEntity<ResultPaginationDTO> getUsers(@Filter Specification<User> spec, Pageable pageable) {
        return ResponseEntity.ok(this.userService.fetchAllUsers(spec, pageable));
    }
}