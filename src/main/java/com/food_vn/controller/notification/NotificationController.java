package com.food_vn.controller.notification;

import com.food_vn.lib.app_const.API_RESPONSE;
import com.food_vn.model.api_responses.ApiResponse;
import com.food_vn.model.users.User;
import com.food_vn.model.users.UserPrinciple;
import com.food_vn.model.notification.Notification;
import com.food_vn.repository.users.UserRepository;
import com.food_vn.service.notification.impl.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/notifications")
public class NotificationController {
    @Autowired
    private NotificationService notificationService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/user")
    public ResponseEntity<?> getNotificationsByUser(@RequestParam(defaultValue = "day") String filter) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            ApiResponse<User> response = new ApiResponse<>(
                    API_RESPONSE.UNAUTHORIZED_MESSAGE,
                    HttpStatus.UNAUTHORIZED.value()
            );
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
        UserPrinciple userDetails = (UserPrinciple) authentication.getPrincipal();
        User user = userRepository.findById(userDetails.getId()).orElse(null);
        ApiResponse<?> response = new ApiResponse<>(
                API_RESPONSE.FETCHED_SUCCESS_MESSAGE,
                notificationService.getNotificationsByUserWithFilter(user, filter),
                HttpStatus.OK.value()
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/admin")
    public ResponseEntity<?> getNotificationsForAdmin(@RequestParam(defaultValue = "day") String filter) {
        ApiResponse<?> response = new ApiResponse<>(
                API_RESPONSE.FETCHED_SUCCESS_MESSAGE,
                notificationService.getNotificationsForAdminWithFilter(filter),
                HttpStatus.OK.value()
        );
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<?> markNotificationAsRead(@PathVariable("id") Long id) {
        Notification notification = notificationService.markAsRead(id);
        ApiResponse<?> response = new ApiResponse<>(
            API_RESPONSE.SAVED_SUCCESS_MESSAGE,
            notification,
            HttpStatus.OK.value()
        );
        return ResponseEntity.ok(response);
    }
}

