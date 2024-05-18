package burak.model;


import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String chatId;
    private Long senderId;
    private Long recipientId;
    private String content;
    private Date timestamp;


    public ChatMessage(Long id, Long recipientId, String content) {
        this.id = id;
        this.recipientId = recipientId;
        this.content = content;
        this.timestamp = new Date();
    }
}