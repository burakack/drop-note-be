package burak.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data // Create getters and setters
@NoArgsConstructor
@SQLDelete(sql = "UPDATE friendship SET deleted = true WHERE id=?")
public class Friendship implements Serializable {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @ManyToOne
    AppUser friendRequester;

    @ManyToOne
    AppUser friendReceiver;

    @Column(columnDefinition = "boolean default false")
    boolean accepted;

    @Column(columnDefinition = "boolean default false")
    boolean deleted;

    @Temporal(TemporalType.TIMESTAMP)
    Date date;


}