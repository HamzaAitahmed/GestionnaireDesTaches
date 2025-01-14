package ma.emsi.gestionnairedestaches.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity

public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique=true)
    private String nom;

    private String Description;

    @OneToMany(mappedBy = "ProjectTask")
    private Collection<Task> Tasks;

    @ManyToOne
    private User ProjectOwner;

    @ManyToOne
    private Team ProjectTeam;

    @Override
    public String toString() {
        return "Project{ " +
                "id = " + id +
                " | nom = '" + nom + '\'' +
                " | Description = '" + Description + '\'' +
//                ", Tasks=" + Tasks.toString() +
//                " | ProjectOwner { id = " + ProjectOwner.getId() +" | username  = " + ProjectOwner.getUsername() + " } " +
//                ", ProjectTeam=" + ProjectTeam.getNom() +
                '}';
    }

}
