package ma.emsi.gestionnairedestaches.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity

public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique=true) // unique seulement dans le projet
    private String nom;
    private String description;
    private boolean TaskDone=false;

    @ManyToOne
    private User UserTask;

    @ManyToOne
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Project ProjectTask;

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", description='" + description + '\'' +
                ", TaskDone=" + TaskDone +
//                ", UserTask=" + UserTask +
//                ", ProjectTask=" + ProjectTask +
                '}';
    }
}
