package burak.dto;

import burak.model.AppUser;
import lombok.*;

import javax.persistence.*;


@Setter

@Getter

@AllArgsConstructor

@NoArgsConstructor

@ToString

@Entity
public class NoteDto {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    @ManyToOne(cascade = CascadeType.ALL)
    private AppUser user;

    @Column(name = "is_anonymous", nullable = false)
    private boolean isAnonymous;

}
