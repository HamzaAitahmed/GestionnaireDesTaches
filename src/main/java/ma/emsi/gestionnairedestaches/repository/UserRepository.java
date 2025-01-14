package ma.emsi.gestionnairedestaches.repository;

import ma.emsi.gestionnairedestaches.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {

    @Query("SELECT u FROM User u WHERE u.email = :email")
    User findUserByEmail(@Param("email") String email);

    @Query("SELECT u FROM User u WHERE u.id = :id")
    User findUserById(@Param("id") int id);

    @Query("select u from User u Join Team On u IN ( Select t.members from Team t where t.id = :teamId ) ")
    List<User> findMembersByTeamId( Integer teamId);

    @Query("select u from User u Join Team On u Not IN ( Select t.members from Team t where t.id = :teamId ) ")
    List<User> findNotMembersByTeamId( Integer teamId);

}
