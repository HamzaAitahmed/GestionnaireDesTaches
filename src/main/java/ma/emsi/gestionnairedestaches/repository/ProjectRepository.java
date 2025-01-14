package ma.emsi.gestionnairedestaches.repository;

import ma.emsi.gestionnairedestaches.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project,Integer> {

    @Query("SELECT p FROM Project p WHERE p.projectOwner.id = :id")
    List<Project> findByProjectOwner(@Param("id") Integer id);

    @Query("SELECT p FROM Project p JOIN Team t ON p.projectTeam = t JOIN User u ON t.leader = u OR t IN ( SELECT tm.teams  FROM t.members tm WHERE u = tm ) WHERE u.id = :id")
    List<Project> findProjectTeamByUserId(int id); // il affiche les projets ou le user est membre ou leader de la team qui est associer aux projets avec et meme si il est le projectOwner

    @Query("SELECT p FROM Project p JOIN Team t ON p.projectOwner.id != :id AND p.projectTeam = t JOIN User u ON t.leader = u OR t IN ( SELECT tm.teams  FROM t.members tm WHERE u = tm ) WHERE u.id = :id")
    List<Project> findOtherProjectByUserId(int id);// il affiche les projets ou le user est membre ou leader de la team qui est associer aux projets avec mais il n'est pas le projectOwner

    @Query("SELECT p FROM Project p JOIN Team t ON p.projectTeam = t Or p.projectOwner.id = :id JOIN User u ON  t.leader = u OR t IN ( SELECT tm.teams  FROM t.members tm WHERE u = tm ) WHERE u.id = :id  ")
    List<Project> findAllProjectByUserId(int id);

    @Query("SELECT u.projects FROM User u")
    List<Project> findProjectByUser();


    @Query("SELECT p FROM Project p where p.projectTeam is null ")
    List<Project> findProjectWithoutTeam();

    Project findProjectById(Integer id);


}
