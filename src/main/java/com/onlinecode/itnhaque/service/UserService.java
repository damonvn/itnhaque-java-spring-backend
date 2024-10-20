package com.onlinecode.itnhaque.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.onlinecode.itnhaque.domain.Role;
import com.onlinecode.itnhaque.domain.User;
import com.onlinecode.itnhaque.domain.response.ResCreateUserDTO;
import com.onlinecode.itnhaque.domain.response.ResUpdateUserDTO;
import com.onlinecode.itnhaque.domain.response.ResUserDTO;
import com.onlinecode.itnhaque.domain.response.ResultPaginationDTO;
import com.onlinecode.itnhaque.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleService roleService;

    public UserService(UserRepository userRepository, RoleService roleService) {
        this.userRepository = userRepository;
        this.roleService = roleService;
    }

    public User handleCreateUser(User user) {
        // check role
        if (user.getRole() != null) {
            Role r = this.roleService.fetchById(user.getRole().getId());
            user.setRole(r != null ? r : null);
        }
        return this.userRepository.save(user);
    }

    public User handleUpdateUser(User reqUser) {
        User userDB = this.fetchUserById(reqUser.getId());
        if (userDB != null) {
            userDB.setPhone(reqUser.getPhone());
            userDB.setAddress(reqUser.getAddress());
            userDB.setGender(reqUser.getGender());
            userDB.setAge(reqUser.getAge());
            userDB.setName(reqUser.getName());
            if (reqUser.getRole() != null) {
                Role r = this.roleService.fetchById(reqUser.getRole().getId());
                userDB.setRole(r != null ? r : null);
            }

            // update
            userDB = this.userRepository.save(userDB);
        }
        return userDB;
    }

    public void handleDeleteUser(long id) {
        this.userRepository.deleteById(id);
    }

    public User fetchUserById(long id) {
        Optional<User> userOptional = this.userRepository.findById(id);
        if (userOptional.isPresent()) {
            return userOptional.get();
        }
        return null;
    }

    public ResultPaginationDTO fetchAllUser(Specification<User> spec, Pageable pageable) {
        Page<User> pageUser = this.userRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());
        mt.setPages(pageUser.getTotalPages());
        mt.setTotal(pageUser.getTotalElements());
        rs.setMeta(mt);

        // remove sensitive data
        List<ResUserDTO> listUser = pageUser.getContent()
                .stream().map(item -> this.convertToResUserDTO(item))
                .collect(Collectors.toList());
        rs.setResult(listUser);

        return rs;
    }

    public User handleGetUserByEmail(String email) {
        return this.userRepository.findByEmail(email);
    }

    public boolean isEmailExist(String email) {
        return this.userRepository.existsByEmail(email);
    }

    public User handleGetUserByUsername(String username) {
        return this.userRepository.findByEmail(username);
    }

    public ResCreateUserDTO convertToResCreateUserDTO(User user) {
        ResCreateUserDTO res = new ResCreateUserDTO();

        res.setId(user.getId());
        res.setEmail(user.getEmail());
        res.setName(user.getName());
        res.setAge(user.getAge());
        res.setCreatedAt(user.getCreatedAt());
        res.setGender(user.getGender());
        res.setAddress(user.getAddress());

        return res;
    }

    public ResUpdateUserDTO convertToResUpdateUserDTO(User user) {
        ResUpdateUserDTO res = new ResUpdateUserDTO();

        res.setId(user.getId());
        res.setName(user.getName());
        res.setAge(user.getAge());
        res.setUpdatedAt(user.getUpdatedAt());
        res.setGender(user.getGender());
        res.setAddress(user.getAddress());
        return res;
    }

    public ResUserDTO convertToResUserDTO(User user) {
        ResUserDTO res = new ResUserDTO();
        ResUserDTO.RoleUser roleUser = new ResUserDTO.RoleUser();

        if (user.getRole() != null) {
            roleUser.setId(user.getRole().getId());
            roleUser.setName(user.getRole().getName());
            res.setRole(roleUser);
        }

        res.setId(user.getId());
        res.setEmail(user.getEmail());
        res.setName(user.getName());
        res.setAge(user.getAge());
        res.setUpdatedAt(user.getUpdatedAt());
        res.setCreatedAt(user.getCreatedAt());
        res.setGender(user.getGender());
        res.setAddress(user.getAddress());
        return res;
    }

    public void updateUserToken(String email, String token) {
        User currentUser = this.handleGetUserByUsername(email);
        if (currentUser != null) {
            currentUser.setRefreshToken(token);
            this.userRepository.save(currentUser);
        }
    }

    public User getUserByRefreshTokenAndEmail(String token, String email) {
        return this.userRepository.findByRefreshTokenAndEmail(token, email);
    }
}