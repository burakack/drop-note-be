package burak.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Type;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Setter

@Getter

@AllArgsConstructor

@NoArgsConstructor

@ToString
@SQLDelete(sql = "UPDATE app_user SET is_deleted = true WHERE id=?")
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 4, max = 255, message = "Minimum username length: 4 characters")
    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true)
    private String email;

    @Column
    private int age;

    @Column
    private String gender;

    @Column
    private String description;

    @Column(name = "is_accepted_mail_updates", nullable = false)
    @JsonIgnore
    private boolean isAcceptedMailUpdates = Boolean.FALSE;

    @Column(name = "is_deleted", nullable = false)
    @JsonIgnore
    private boolean isDeleted = Boolean.FALSE;

    @Column(name = "token")
    @JsonIgnore
    private String token;

    @Column(name = "token_creation_date")
    @JsonIgnore
    private LocalDateTime tokenCreationDate; // Corrected column definition

    @Column
    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private Set<Note> note = new HashSet<Note>();

    @ManyToMany(mappedBy = "likedBy")
    @JsonIgnore
    Set<Note> likes = new HashSet<Note>();

    @ManyToMany(mappedBy = "dislikedBy")
    @JsonIgnore
    Set<Note> dislikes = new HashSet<Note>();

    @Size(min = 8, message = "Minimum password length: 8 characters")
    @Column(nullable = false)
    @JsonIgnore
    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "app_user_roles", joinColumns = @JoinColumn(name = "app_user_id"))
    @Enumerated(EnumType.STRING)
    @JsonIgnore
    private List<AppUserRole> appUserRoles;

    @OneToMany(mappedBy = "friendRequester")
    @JsonIgnore
    private Set<Friendship> requestedFriends;

    @OneToMany(mappedBy = "friendReceiver")
    @JsonIgnore
    private Set<Friendship> receivedFriends;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private Set<Notification> notifications;

    @Lob
    @Column(name = "user_image")
    @Type(type = "org.hibernate.type.BinaryType")
    private byte[] userImage;
}
