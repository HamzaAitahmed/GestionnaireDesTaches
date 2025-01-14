package ma.emsi.gestionnairedestaches.controller;

import jakarta.servlet.http.HttpSession;
import ma.emsi.gestionnairedestaches.model.*;
import ma.emsi.gestionnairedestaches.repository.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

@Controller
public class ProjectController {


    private final ProjectRepository projectRepository;
    private final TeamRepository teamRepository;
    private final TaskRepository taskRepository;

    public static final String REDIRECT_PROJECT = "redirect:/project";
    public static final String CONNECTED_USER = "connectedUser";
    public static final String PROJECT_LIST = "PorjectList";
    public static final String MY_PROJECT = "My Projects";
    public static final String OTHER_PROJECTS = "Other Projects";
    public static final String ALL_PROJECTS = "All Projects";
    public static final String SEARCH = "search";

    ProjectController( ProjectRepository projectRepository, TeamRepository teamRepository, TaskRepository taskRepository) {
        this.projectRepository = projectRepository;
        this.teamRepository = teamRepository;
        this.taskRepository = taskRepository;
    }
    @GetMapping(path="/project")
    public String project(@RequestParam(name = SEARCH , defaultValue = MY_PROJECT) String search , HttpSession session, Model model )
    {
        User user = (User) session.getAttribute(CONNECTED_USER);
        List<Project> otherProjects = projectRepository.findOtherProjectByUserId(user.getId());

        List<Project> myProjects = projectRepository.findByProjectOwner(user.getId());

        List<Project> allProjects = projectRepository.findAllProjectByUserId(user.getId());

//        Collections.sort(allProjects , Comparator.comparingLong(Project::getId)); // sort List by Project Id

        if(search.equals(MY_PROJECT)){ // Done
            model.addAttribute(PROJECT_LIST, myProjects);
        }
        if(search.equals(OTHER_PROJECTS)){
            model.addAttribute(PROJECT_LIST, otherProjects);
        }
        if(search.equals(ALL_PROJECTS)){
            model.addAttribute(PROJECT_LIST, allProjects);
        }
        model.addAttribute(SEARCH, search);
        model.addAttribute("user", user);

        return "Main/ProjectPages/project";
    }

    @GetMapping(path="/deleteProject")
    public String deleteProject(RedirectAttributes redirectAttributes,Integer projectId, String search){
        List<Task> tasks = taskRepository.findProjectTaskById(projectId);
        for(Task task : tasks){
            task.setProjectTask(null);
        }
        projectRepository.deleteById(projectId);

        redirectAttributes.addAttribute(SEARCH, search);
        return REDIRECT_PROJECT;
    }

    @GetMapping(path="/NewProject")
    public String newProject(RedirectAttributes redirectAttributes , HttpSession session , String search , Model model){

        User user = (User) session.getAttribute(CONNECTED_USER);
        List<Team> teams = teamRepository.findAll();
        if(teams.isEmpty())
        {
            teams = null;
        }

        List<Project> otherProjects = projectRepository.findOtherProjectByUserId(user.getId());

        List<Project> myProjects = projectRepository.findByProjectOwner(user.getId());

        List<Project> allProjects = projectRepository.findAllProjectByUserId(user.getId());

//        Collections.sort(allProjects , Comparator.comparingLong(Project::getId)); // sort List by Project Id


        if(search.equals(MY_PROJECT)){ // Done
            model.addAttribute(PROJECT_LIST, myProjects);
        }
        if(search.equals(OTHER_PROJECTS)){
            model.addAttribute(PROJECT_LIST, otherProjects);
        }
        if(search.equals(ALL_PROJECTS)){
            model.addAttribute(PROJECT_LIST, allProjects);
        }

        Project newProject = new Project();
        model.addAttribute("Project",newProject);
        model.addAttribute("user",user);
        model.addAttribute(SEARCH,search);
        model.addAttribute("ListTeams",teams);
        redirectAttributes.addAttribute(SEARCH, search);
        return "Main/ProjectPages/AddProject";
    }

    @PostMapping(path="/NewProject")
    public String createNewProject(RedirectAttributes redirectAttributes,HttpSession session , @ModelAttribute("Project") Project newProject , String search , Model model)
    {
        User user = (User) session.getAttribute(CONNECTED_USER);
        model.addAttribute("user",user);
        try {
            if(newProject == null){
                return "redirect:/NewProject?error";
            }
            newProject.setProjectOwner(user);
            projectRepository.save(newProject);
            redirectAttributes.addAttribute(SEARCH, search);
            return REDIRECT_PROJECT;


        } catch (Exception e){
            return "redirect:/NewProject?error";
        }
    }

    @GetMapping(path="/EditProject")
    public String editProject(RedirectAttributes redirectAttributes , int projectId , String search , HttpSession session , Model model)
    {
        User user = (User) session.getAttribute(CONNECTED_USER);
        List<Team> teams = teamRepository.findAll();
        if(teams.isEmpty())
        {
            teams = null;
        }
        Project editProject = projectRepository.findProjectById(projectId);

        List<Project> otherProjects = projectRepository.findOtherProjectByUserId(user.getId());

        List<Project> myProjects = projectRepository.findByProjectOwner(user.getId());

        List<Project> allProjects = projectRepository.findAllProjectByUserId(user.getId());

//        Collections.sort(allProjects , Comparator.comparingLong(Project::getId)); // sort List by Project Id


        if(search.equals(MY_PROJECT)){ // Done
            model.addAttribute(PROJECT_LIST, myProjects);
        }
        if(search.equals(OTHER_PROJECTS)){
            model.addAttribute(PROJECT_LIST, otherProjects);
        }
        if(search.equals(ALL_PROJECTS)){
            model.addAttribute(PROJECT_LIST, allProjects);
        }

        model.addAttribute("Project",editProject);
        model.addAttribute("user",user);
        model.addAttribute("ListTeams",teams);
        model.addAttribute(SEARCH, search);
        redirectAttributes.addAttribute(SEARCH, search);

        return "Main/ProjectPages/EditProject";
    }

    @PostMapping(path="/EditProject")
    public String editProject(@RequestParam(name = "nom" ) String nom, HttpSession session ,Model model ,RedirectAttributes redirectAttributes,
                              @RequestParam(name = "Project_id" ) int projectId,
                              @RequestParam(name = "description" ) String description,
                              @RequestParam(name = "ProjectTeam" ,defaultValue = "-1") int projectTeam,
                              @ModelAttribute(SEARCH ) String search)
    {
        User user = (User) session.getAttribute(CONNECTED_USER);

        model.addAttribute("user",user);
        Project editProject = projectRepository.findProjectById(projectId);
        Team teams = teamRepository.findTeamById(projectTeam);

        try {
            if(editProject == null){
                return "redirect:/EditProject?error";
            }
            editProject.setNom(nom);
            editProject.setDescription(description);
            editProject.setProjectTeam(teams);
            if (teams == null)
            {
                for(Task task : editProject.getTasks())
                {
                    task.setUserTask(null);
                }
            }
            projectRepository.save(editProject);
            redirectAttributes.addAttribute(SEARCH, search);
            return REDIRECT_PROJECT;

        } catch (Exception e){
            return "redirect:/EditProject?error";

        }

    }
}

