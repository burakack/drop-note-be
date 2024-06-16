package burak.controller;

import burak.model.Notification;
import burak.service.NotificationService;
import burak.service.UserService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/notifications")
@Api(tags = "notifications")
@RequiredArgsConstructor
@Controller
public class NotificationController {

    private final NotificationService notificationService;
    private final UserService userService;


    @GetMapping("/get-my-notifications")
    public ResponseEntity<List<Notification>> getMyNotifications(HttpServletRequest req) {

        return ResponseEntity.ok(notificationService.getNotificationsByUserId(userService.getUserInformationByUsername(req.getRemoteUser()).getId()));
    }

}
