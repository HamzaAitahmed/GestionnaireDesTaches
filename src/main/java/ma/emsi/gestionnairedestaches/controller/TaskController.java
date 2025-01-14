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

    public static final String CONNECTED_USER = "connectedUser";
    public static final String LIST_TASK_DONE = "ListTaskDone";
    public static final String LIST_NOT_TASK_DONE = "ListTaskNotDone";
    public static final String CURRENT_PROJECT = "CurrentProject";
    public static final String PROJECT_ID = "Project_id";
    public static final String TASK_ID = "Task_id";
    public static final String REDIRECT_TASK = "redirect:/task";

    TaskController(TaskRepository taskRepository, ProjectRepository projectRepository, UserRepository userRepository ){
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }
    
    @GetMapping(path="/task")
    public String task(Model model, @RequestParam(name = PROJECT_ID , defaultValue = "-1" ) int projectId, @SessionAttribute("connectedUser" ) User user)
    {
        Project project;
        if(projectId == -1)
        {
            List<Project> projects = projectRepository.findByProjectOwner(user.getId());
            if (projects.isEmpty())
            {
                project = null;
            }else {
                project = projects.get(0);
                projectId = project.getId();
            }
        }else{
            project = projectRepository.findProjectById(projectId);
        }


        List<Project> listProject = projectRepository.findByProjectOwner(user.getId());
        List<Task> listTaskDone = taskRepository.findProjectTaskByTaskDone(projectId,true);
        List<Task> listTaskNotDone = taskRepository.findProjectTaskByTaskDone(projectId,false);

        model.addAttribute("user", user);
        model.addAttribute(LIST_TASK_DONE, listTaskDone);
        model.addAttribute(LIST_NOT_TASK_DONE, listTaskNotDone);
        model.addAttribute("ListProject", listProject);
        model.addAttribute(CURRENT_PROJECT,project);

        return "Main/TaskPages/task";
    }

    @GetMapping(path="/TaskStatus")
    public String taskStatus(RedirectAttributes redirectAttributes,@RequestParam(name = PROJECT_ID ) int projectId,@RequestParam(name = TASK_ID ) int taskId)
    {
        Task taskCheck = taskRepository.findTaskById(taskId);
        taskCheck.setTaskDone( ! ( taskCheck.isTaskDone() ) ) ; // convert task status if its Done or Not Done
        taskRepository.save(taskCheck);
        redirectAttributes.addAttribute(PROJECT_ID, projectId);
        return REDIRECT_TASK;
    }

    @GetMapping(path="/DeleteTask")
    public String deleteTask(RedirectAttributes redirectAttributes,@RequestParam(name = PROJECT_ID ) int projectId,@RequestParam(name = TASK_ID ) int taskId){
        taskRepository.deleteById(taskId);
        redirectAttributes.addAttribute(PROJECT_ID, projectId);
        return "redirect:/task?Project_id="+projectId;
    }

    @GetMapping(path="/NewTask")
    public String newTask(HttpSession session , @RequestParam(name = PROJECT_ID) int projectId, Model model)
    {
        Project objProject = projectRepository.findProjectById(projectId);
        User user = (User) session.getAttribute(CONNECTED_USER);

        Task newTask = new Task();
        newTask.setProjectTask(objProject);

        Team team = objProject.getProjectTeam();
        Collection<User> users = null;
        if(team!=null)
        {
            users = team.getMembers();
            users.add(team.getLeader());
        }

        List<Task> listTaskDone = taskRepository.findProjectTaskByTaskDone(projectId,true);
        List<Task> listTaskNotDone = taskRepository.findProjectTaskByTaskDone(projectId,false);

        model.addAttribute("NewTask",newTask);
        model.addAttribute(PROJECT_ID,projectId);
        model.addAttribute("users",users);
        model.addAttribute("user",user);
        model.addAttribute(LIST_TASK_DONE, listTaskDone);
        model.addAttribute(LIST_NOT_TASK_DONE, listTaskNotDone);
        model.addAttribute(CURRENT_PROJECT,objProject);
        return "Main/TaskPages/AddTask";
    }

    @PostMapping(path="/NewTask")
    public String addNewTask(RedirectAttributes redirectAttributes,@ModelAttribute("NewTask") Task newTask ,@ModelAttribute(PROJECT_ID) Integer projectId )
    {

        try {
            if(newTask == null){
                redirectAttributes.addAttribute(PROJECT_ID, projectId);
                return "redirect:/NewTask";
            }
            taskRepository.save(newTask);
            projectRepository.save(projectRepository.findProjectById(projectId));
            redirectAttributes.addAttribute(PROJECT_ID, projectId);
            return REDIRECT_TASK;

        } catch (Exception e){
            redirectAttributes.addAttribute(PROJECT_ID, projectId);
            return "redirect:/NewTask";
        }

    }


    @GetMapping(path="/EditTask")
    public String editTask(HttpSession session , @RequestParam(name = PROJECT_ID) int projectId,@RequestParam(name = TASK_ID) int taskId, Model model)
    {
        User user = (User) session.getAttribute(CONNECTED_USER);

        Project objProject = projectRepository.findProjectById(projectId);
        Task objTask = taskRepository.findTaskById(taskId);

        Team team = objProject.getProjectTeam();
        Collection<User> users = null;
        if(team!=null)
        {
            users = team.getMembers();
            users.add(team.getLeader());
        }

        List<Task> listTaskDone = taskRepository.findProjectTaskByTaskDone(projectId,true);
        List<Task> listTaskNotDone = taskRepository.findProjectTaskByTaskDone(projectId,false);

        model.addAttribute("EditTask",objTask);
        model.addAttribute(TASK_ID,taskId);
        model.addAttribute(PROJECT_ID,projectId);
        model.addAttribute("users",users);
        model.addAttribute("user",user);
        model.addAttribute(LIST_TASK_DONE, listTaskDone);
        model.addAttribute(LIST_NOT_TASK_DONE, listTaskNotDone);
        model.addAttribute(CURRENT_PROJECT,objProject);
        return "Main/TaskPages/EditTask";
    }

    @PostMapping(path="/EditTask")
    public String editTask( RedirectAttributes redirectAttributes,
                            @RequestParam(name = TASK_ID) int taskId ,
                            @RequestParam(name = "nom") String nom ,
                            @RequestParam(name = "users" , defaultValue = "-1" ) int userId ,
                            @RequestParam(name = "description") String description ,
                            @ModelAttribute(PROJECT_ID) Integer projectId )
    {
        Task editTask = taskRepository.findTaskById(taskId);
        try {
            if(editTask == null){
                redirectAttributes.addAttribute(PROJECT_ID, projectId);
                redirectAttributes.addAttribute(TASK_ID, taskId);
                return "redirect:/EditTask";
            }
            editTask.setNom(nom);
            editTask.setDescription(description);
            if(userId != -1)
                editTask.setUserTask( userRepository.findUserById(userId) );
            else
                editTask.setUserTask(null);

            taskRepository.save(editTask);
            redirectAttributes.addAttribute(PROJECT_ID, projectId);
            return REDIRECT_TASK;

        } catch (Exception e){
            redirectAttributes.addAttribute(PROJECT_ID, projectId);
            redirectAttributes.addAttribute(TASK_ID, taskId);
            return "redirect:/EditTask";
        }

    }


}
