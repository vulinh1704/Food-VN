package com.food_vn.service.users.impl;

import com.food_vn.model.orders.Orders;
import com.food_vn.model.users.Role;
import com.food_vn.model.users.User;
import com.food_vn.model.users.UserDTO;
import com.food_vn.model.users.UserPrinciple;
import com.food_vn.repository.orders.OrdersRepository;
import com.food_vn.repository.users.RoleRepository;
import com.food_vn.repository.users.UserRepository;
import com.food_vn.service.users.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private OrdersRepository ordersRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        if (this.checkLogin(user)) {
            return UserPrinciple.build(user);
        }
        boolean enable = false;
        boolean accountNonExpired = false;
        boolean credentialsNonExpired = false;
        boolean accountNonLocked = false;
        return new org.springframework.security.core.userdetails.User(user.getUsername(),
                user.getPassword(), enable, accountNonExpired, credentialsNonExpired,
                accountNonLocked, null);
    }

    public User register(User user) {
        User userReq = this.findByUsername(user.getUsername());
        if (userReq != null) throw new RuntimeException("Username already exists");
        User userEmail = userRepository.findByEmail(user.getEmail());
        if (userEmail != null) throw new RuntimeException("Email already exists");
        user.setConfirmPassword(user.getPassword());
        if (!this.isCorrectConfirmPassword(user)) throw new RuntimeException("confirm_password_incorrect");
        user.setRoles(this.getUserRoles());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User output = this.userRepository.save(user);
        Orders orders = new Orders();
        orders.setUser(output);
        orders.setTotal(0.0);
        orders.setStatus(1);
        ordersRepository.save(orders);
        return output;
    }

    @Override
    public User save(UserDTO userDTO) {
        User existingUser = userRepository.findById(userDTO.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        User userReq = userRepository.findByEmailAndIdNot(userDTO.getEmail(), userDTO.getId());
        if (userReq != null) throw new RuntimeException("Email already exists");
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setAvatar(userDTO.getAvatar());
        user.setId(userDTO.getId());
        user.setGender(userDTO.getGender());
        user.setEmail(userDTO.getEmail());
        user.setDateOfBirth(userDTO.getDateOfBirth());
        user.setPhoneNumber(userDTO.getPhoneNumber());
        user.setPassword(existingUser.getPassword());
        user.setConfirmPassword(existingUser.getConfirmPassword());
        user.setRoles(existingUser.getRoles());
        return userRepository.save(user);
    }

    @Override
    public Iterable<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User getCurrentUser() {
        User user;
        String userName;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            userName = ((UserDetails) principal).getUsername();
        } else {
            userName = principal.toString();
        }
        user = this.findByUsername(userName);
        return user;
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public UserDetails loadUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new NullPointerException();
        }
        return UserPrinciple.build(user.get());
    }

    @Override
    public boolean checkLogin(User user) {
        Iterable<User> users = this.findAll();
        boolean isCorrectUser = false;
        for (User currentUser : users) {
            if (currentUser.getUsername().equals(user.getUsername()) && user.getPassword().equals(currentUser.getPassword()) && currentUser.isEnabled()) {
                isCorrectUser = true;
                break;
            }
        }
        return isCorrectUser;
    }

    @Override
    public boolean isRegister(User user) {
        boolean isRegister = false;
        Iterable<User> users = this.findAll();
        for (User currentUser : users) {
            if (user.getUsername().equals(currentUser.getUsername())) {
                isRegister = true;
                break;
            }
        }
        return isRegister;
    }

    @Override
    public boolean isCorrectConfirmPassword(User user) {
        return user.getPassword().equals(user.getConfirmPassword());
    }

    @Override
    public boolean changePassword(Long userId, String oldPassword, String newPassword) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) return false;
        User user = userOpt.get();
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            return false;
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        return true;
    }

    private Set<Role> getUserRoles() {
        Role role = this.roleRepository.findByName("ROLE_USER");
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        return roles;
    }
}
