package burak.controller;

import burak.dto.ChatNotification;
import burak.dto.MessageDto;
import burak.model.AppUser;
import burak.model.ChatMessage;
import burak.service.ChatMessageService;
import burak.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ChatController {


    private final ChatMessageService chatMessageService;
    private final UserService userService;
    private final SimpMessagingTemplate messagingTemplate;

    @PreAuthorize("hasRole('ROLE_USER')")
    @MessageMapping("/chat")
    public void processMessage(@Payload MessageDto chatMessage, HttpServletRequest req) {


        AppUser user = userService.getUserInformationByUsername(req.getRemoteUser());

        ChatMessage savedMsg = new ChatMessage(user.getId(), chatMessage.getRecipientId(), chatMessage.getContent());

        savedMsg = chatMessageService.save(savedMsg);

        messagingTemplate.convertAndSendToUser(
                String.valueOf(chatMessage.getRecipientId()), "/queue/messages",
                new ChatNotification(
                        savedMsg.getId(),
                        savedMsg.getSenderId(),
                        savedMsg.getRecipientId(),
                        savedMsg.getContent()
                )
        );
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/messages/{senderId}/{recipientId}")
    public ResponseEntity<List<ChatMessage>> findChatMessages(@PathVariable Long recipientId, HttpServletRequest req) {
        AppUser user = userService.getUserInformationByUsername(req.getRemoteUser());
        return ResponseEntity
                .ok(chatMessageService.findChatMessages(user.getId(), recipientId));
    }
}