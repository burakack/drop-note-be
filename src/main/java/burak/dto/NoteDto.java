package burak.dto;

import burak.model.AppUser;
import lombok.*;


@Setter

@Getter

@AllArgsConstructor

@NoArgsConstructor

@ToString

public class NoteDto {

    private Long id;

    private String title;

    private String content;

    private AppUser user;

    private boolean isAnonymous;

}
