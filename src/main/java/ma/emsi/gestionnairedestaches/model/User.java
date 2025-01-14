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

    private String firstName;
    private String lastName;
    private String username;
    private String password;

    @Temporal(TemporalType.DATE)
    private Date dateOfBirth;

    private String gender;

    @Column(name = "ROLE")
    private String role;

    @Column(name = "photo", nullable = true)
    private String profilePicture;

    @OneToMany(mappedBy = "projectOwner")
    private Collection<Project> projects;

    @OneToMany(mappedBy = "userTask")
    private Collection<Task> tasks;

    @OneToMany(mappedBy = "leader")
    private Collection<Team> myTeams;

    @ManyToMany(mappedBy = "members")
    private Collection<Team> teams;

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
                " | FirstName='" + firstName + '\'' +
                " | LastName='" + lastName + '\'' +
                " | username='" + username + '\'' +
                '}';
    }
}
