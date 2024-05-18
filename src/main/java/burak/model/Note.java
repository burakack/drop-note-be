package burak.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Setter

@Getter
@AllArgsConstructor

@NoArgsConstructor

@ToString
@Entity
@SQLDelete(sql = "UPDATE note SET is_deleted = true WHERE id=?")
public class Note {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private AppUser user;

    @Column(name = "is_anonymous", nullable = false)
    private boolean isAnonymous;

    @ManyToMany
    private Set<AppUser> likedBy = new HashSet<AppUser>();

    @ManyToMany
    private Set<AppUser> dislikedBy = new HashSet<AppUser>();

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = Boolean.FALSE;


}
