package ma.emsi.gestionnairedestaches.repository;

import ma.emsi.gestionnairedestaches.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task,Integer>{
    @Query("SELECT t FROM Task t WHERE t.projectTask.id = :id")
    List<Task> findProjectTaskById(@Param("id") Integer id);

    @Query("SELECT t FROM Task t WHERE t.projectTask.id = :id AND t.taskDone = :bl")
    List<Task> findProjectTaskByTaskDone(@Param("id") Integer id ,boolean bl);

    @Query("SELECT t FROM Task t WHERE t.id = :id")
    Task findTaskById(int id);
}
