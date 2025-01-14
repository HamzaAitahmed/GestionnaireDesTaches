package ma.emsi.gestionnairedestaches.controller;

import jakarta.servlet.http.HttpSession;
import ma.emsi.gestionnairedestaches.model.*;
import ma.emsi.gestionnairedestaches.repository.ProjectRepository;
import ma.emsi.gestionnairedestaches.repository.TaskRepository;
import ma.emsi.gestionnairedestaches.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collection;
import java.util.List;

@Controller
public class TaskController {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;


    TaskController(TaskRepository taskRepository, ProjectRepository projectRepository, UserRepository userRepository ){
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }
    
    @GetMapping(path="/task")
    public String task(Model model, @RequestParam(name = "Project_id" , defaultValue = "-1" ) int project_id, @SessionAttribute("connectedUser" ) User user)
    {
        Project project;
        if(project_id == -1)
        {
            List<Project> projects = projectRepository.findByProjectOwner(user.getId());
            if (projects.isEmpty())
            {
                project = null;
            }else {
                project = projects.get(0);
                project_id = project.getId();
            }
        }else{
            project = projectRepository.findProjectById(project_id);
        }


        List<Project> listProject = projectRepository.findByProjectOwner(user.getId());
        List<Task> listTaskDone = taskRepository.findProjectTaskByTaskDone(project_id,true);
        List<Task> listTaskNotDone = taskRepository.findProjectTaskByTaskDone(project_id,false);

        model.addAttribute("user", user);
        model.addAttribute("ListTaskDone", listTaskDone);
        model.addAttribute("ListTaskNotDone", listTaskNotDone);
        model.addAttribute("ListProject", listProject);
        model.addAttribute("CurrentProject",project);

        return "Main/TaskPages/task";
    }

    @GetMapping(path="/TaskStatus")
    public String taskStatus(@RequestParam(name = "Project_id" ) int project_id,@RequestParam(name = "Task_id" ) int task_id)
    {
        Task taskCheck = taskRepository.findTaskById(task_id);
        taskCheck.setTaskDone( ! ( taskCheck.isTaskDone() ) ) ; // convert task status if its Done or Not Done
        taskRepository.save(taskCheck);
        return "redirect:/task?Project_id="+project_id;
    }

    @GetMapping(path="/DeleteTask")
    public String deleteTask(@RequestParam(name = "Project_id" ) int project_id,@RequestParam(name = "Task_id" ) int task_id){
        taskRepository.deleteById(task_id);
        return "redirect:/task?Project_id="+project_id;
    }

    @GetMapping(path="/NewTask")
    public String newTask(HttpSession session , @RequestParam(name = "Project_id") int project_id, Model model)
    {
        Project ObjProject = projectRepository.findProjectById(project_id);
        User user = (User) session.getAttribute("connectedUser");

        Task newTask = new Task();
        newTask.setProjectTask(ObjProject);

        Team team = ObjProject.getProjectTeam();
        Collection<User> users = null;
        if(team!=null)
        {
            users = team.getMembers();
            users.add(team.getLeader());
        }

        List<Task> listTaskDone = taskRepository.findProjectTaskByTaskDone(project_id,true);
        List<Task> listTaskNotDone = taskRepository.findProjectTaskByTaskDone(project_id,false);

        model.addAttribute("NewTask",newTask);
        model.addAttribute("Project_id",project_id);
        model.addAttribute("users",users);
        model.addAttribute("user",user);
        model.addAttribute("ListTaskDone", listTaskDone);
        model.addAttribute("ListTaskNotDone", listTaskNotDone);
        model.addAttribute("CurrentProject",ObjProject);
        return "Main/TaskPages/AddTask";
    }

    @PostMapping(path="/NewTask")
    public String addNewTask(@ModelAttribute("NewTask") Task newTask ,@ModelAttribute("Project_id") Integer project_id )
    {

        try {
            if(newTask == null){
                return "redirect:/NewTask?Project_id="+project_id;
            }
            taskRepository.save(newTask);
            projectRepository.save(projectRepository.findProjectById(project_id));
            return "redirect:/task?Project_id="+project_id;

        } catch (Exception e){
            return "redirect:/NewTask?Project_id="+project_id;
        }

    }


    @GetMapping(path="/EditTask")
    public String editTask(HttpSession session , @RequestParam(name = "Project_id") int project_id,@RequestParam(name = "Task_id") int task_id, Model model)
    {
        User user = (User) session.getAttribute("connectedUser");

        Project objProject = projectRepository.findProjectById(project_id);
        Task objTask = taskRepository.findTaskById(task_id);

        Team team = objProject.getProjectTeam();
        Collection<User> users = null;
        if(team!=null)
        {
            users = team.getMembers();
            users.add(team.getLeader());
        }

        List<Task> listTaskDone = taskRepository.findProjectTaskByTaskDone(project_id,true);
        List<Task> listTaskNotDone = taskRepository.findProjectTaskByTaskDone(project_id,false);

        model.addAttribute("EditTask",objTask);
        model.addAttribute("Task_id",task_id);
        model.addAttribute("Project_id",project_id);
        model.addAttribute("users",users);
        model.addAttribute("user",user);
        model.addAttribute("ListTaskDone", listTaskDone);
        model.addAttribute("ListTaskNotDone", listTaskNotDone);
        model.addAttribute("CurrentProject",objProject);
        return "Main/TaskPages/EditTask";
    }

    @PostMapping(path="/EditTask")
    public String editTask( RedirectAttributes redirectAttributes,
                            @RequestParam(name = "Task_id") int task_id ,
                            @RequestParam(name = "nom") String nom ,
                            @RequestParam(name = "users" , defaultValue = "-1" ) int user_id ,
                            @RequestParam(name = "description") String description ,
                            @ModelAttribute("Project_id") Integer project_id )
    {
        Task editTask = taskRepository.findTaskById(task_id);
        try {
            if(editTask == null){
                redirectAttributes.addAttribute("Project_id", project_id);
                redirectAttributes.addAttribute("Task_id", task_id);
                return "redirect:/EditTask";
            }
            editTask.setNom(nom);
            editTask.setDescription(description);
            if(user_id != -1)
                editTask.setUserTask( userRepository.findUserById(user_id) );
            else
                editTask.setUserTask(null);

            taskRepository.save(editTask);
            redirectAttributes.addAttribute("Project_id", project_id);
            return "redirect:/task";

        } catch (Exception e){
            redirectAttributes.addAttribute("Project_id", project_id);
            redirectAttributes.addAttribute("Task_id", task_id);
            return "redirect:/EditTask";
        }

    }


}
