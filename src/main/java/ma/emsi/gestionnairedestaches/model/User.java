package ma.emsi.gestionnairedestaches.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique=true)
    private String email;

    private String FirstName, LastName, username, password;

    @Temporal(TemporalType.DATE)
    private Date dateOfBirth;

    private String Gender;

    @Column(name = "ROLE")
    private String role;

    @Column(name = "photo", nullable = true)
    private String profilePicture;

    @OneToMany(mappedBy = "ProjectOwner")
    private Collection<Project> Projects;

    @OneToMany(mappedBy = "UserTask")
    private Collection<Task> Tasks;

    @OneToMany(mappedBy = "Leader")
    private Collection<Team> MyTeams;

    @ManyToMany(mappedBy = "Members")
    private Collection<Team> Teams;

    public User(String email, String password, String username) {
        this.email = email;
        this.password = password;
        this.username = username;
    }

    @Override
    public String toString() {
        return "User{" +
                " id=" + id +
                " | email='" + email + '\'' +
                " | FirstName='" + FirstName + '\'' +
                " | LastName='" + LastName + '\'' +
                " | username='" + username + '\'' +
                '}';
    }
}
