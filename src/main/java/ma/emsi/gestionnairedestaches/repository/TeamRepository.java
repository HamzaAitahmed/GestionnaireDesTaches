package ma.emsi.gestionnairedestaches.repository;

import ma.emsi.gestionnairedestaches.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamRepository extends JpaRepository<Team,Integer>{

    @Query("SELECT p.projectTeam FROM Project p WHERE p.projectTeam IS NOT NULL ")
    List<Team> findNotNullProjects();

    Team findTeamById(Integer id);

    @Query("SELECT t FROM Team t WHERE t.leader.id = :id")
    List<Team> findTeamsByLeader(@Param("id") Integer id);

    @Query("SELECT t FROM Team t WHERE t In (select u.teams from User u  where u.id = :id )")
    List<Team> findTeamsByMember(@Param("id"    ) Integer id);

    @Query("SELECT t FROM Team t WHERE t In (select u.teams from User u  where u.id = :id ) or t.leader.id = :id ")
    List<Team> findTeamsByUser(@Param("id") Integer id);

}
