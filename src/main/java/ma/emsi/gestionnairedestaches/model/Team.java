package ma.emsi.gestionnairedestaches.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity

public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique=true)
    private String nom;

    @ManyToOne
    private User Leader;

    @OneToMany(mappedBy = "ProjectTeam")
    private Collection<Project> Projects;

    @ManyToMany
    private  Collection<User> Members;

    public Team(Integer id, String nom, User leader) {
        this.id = id;
        this.nom = nom;
        Leader = leader;
    }

    @Override
    public String toString() {
        return "Team{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
//                ", Leader=" + Leader +
//                ", Projects=" + Projects.toString() +
//                ", Members=" + Members.toString() +
                '}';
    }
    
}
