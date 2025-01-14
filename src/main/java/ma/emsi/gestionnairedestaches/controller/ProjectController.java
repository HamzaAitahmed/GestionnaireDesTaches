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

    ProjectController( ProjectRepository projectRepository, TeamRepository teamRepository, TaskRepository taskRepository) {
        this.projectRepository = projectRepository;
        this.teamRepository = teamRepository;
        this.taskRepository = taskRepository;
    }
    @GetMapping(path="/project")
    public String project(@RequestParam(name = "search" , defaultValue = "My Projects") String search , HttpSession session, Model model )
    {
        User user = (User) session.getAttribute("connectedUser");
        List<Project> otherProjects = projectRepository.findOtherProjectByUserId(user.getId());

        List<Project> myProjects = projectRepository.findByProjectOwner(user.getId());

        List<Project> allProjects = projectRepository.findAllProjectByUserId(user.getId());

//        Collections.sort(allProjects , Comparator.comparingLong(Project::getId)); // sort List by Project Id

        if(search.equals("My Projects")){ // Done
            model.addAttribute("PorjectList", myProjects);
        }
        if(search.equals("Other Projects")){
            model.addAttribute("PorjectList", otherProjects);
        }
        if(search.equals("All Projects")){
            model.addAttribute("PorjectList", allProjects);
        }
        model.addAttribute("search", search);
        model.addAttribute("user", user);

        return "Main/ProjectPages/project";
    }

    @GetMapping(path="/deleteProject")
    public String deleteProject(Integer project_id, String search){
        List<Task> tasks = taskRepository.findProjectTaskById(project_id);
        for(Task task : tasks){
            task.setProjectTask(null);
        }
        projectRepository.deleteById(project_id);

        return "redirect:/project?search="+search;
    }

    @GetMapping(path="/NewProject")
    public String newProject(RedirectAttributes redirectAttributes , HttpSession session , String search , Model model){

        User user = (User) session.getAttribute("connectedUser");

        List<Team> teams = teamRepository.findAll();
        if(teams.isEmpty())
        {
            teams = null;
        }

        List<Project> otherProjects = projectRepository.findOtherProjectByUserId(user.getId());

        List<Project> myProjects = projectRepository.findByProjectOwner(user.getId());

        List<Project> allProjects = projectRepository.findAllProjectByUserId(user.getId());

//        Collections.sort(allProjects , Comparator.comparingLong(Project::getId)); // sort List by Project Id


        if(search.equals("My Projects")){ // Done
            model.addAttribute("PorjectList", myProjects);
        }
        if(search.equals("Other Projects")){
            model.addAttribute("PorjectList", otherProjects);
        }
        if(search.equals("All Projects")){
            model.addAttribute("PorjectList", allProjects);
        }

        Project newProject = new Project();
        model.addAttribute("Project",newProject);
        model.addAttribute("user",user);
        model.addAttribute("search",search);
        model.addAttribute("ListTeams",teams);
        redirectAttributes.addAttribute("search", search);
        return "Main/ProjectPages/AddProject";
    }

    @PostMapping(path="/NewProject")
    public String createNewProject(HttpSession session , @ModelAttribute("Project") Project newProject , String search , Model model)
    {
        User user = (User) session.getAttribute("connectedUser");
        model.addAttribute("user",user);
        try {
            if(newProject == null){
                return "redirect:/NewProject?error";
            }
            newProject.setProjectOwner(user);
            projectRepository.save(newProject);
            return "redirect:/project?search="+search;

        } catch (Exception e){
            return "redirect:/NewProject?error";
        }
    }

    @GetMapping(path="/EditProject")
    public String editProject(RedirectAttributes redirectAttributes , int project_id , String search , HttpSession session , Model model)
    {
        User user = (User) session.getAttribute("connectedUser");
        List<Team> teams = teamRepository.findAll();
        if(teams.isEmpty())
        {
            teams = null;
        }
        Project editProject = projectRepository.findProjectById(project_id);

        List<Project> otherProjects = projectRepository.findOtherProjectByUserId(user.getId());

        List<Project> myProjects = projectRepository.findByProjectOwner(user.getId());

        List<Project> allProjects = projectRepository.findAllProjectByUserId(user.getId());

//        Collections.sort(allProjects , Comparator.comparingLong(Project::getId)); // sort List by Project Id


        if(search.equals("My Projects")){ // Done
            model.addAttribute("PorjectList", myProjects);
        }
        if(search.equals("Other Projects")){
            model.addAttribute("PorjectList", otherProjects);
        }
        if(search.equals("All Projects")){
            model.addAttribute("PorjectList", allProjects);
        }

        model.addAttribute("Project",editProject);
        model.addAttribute("user",user);
        model.addAttribute("ListTeams",teams);
        model.addAttribute("search", search);
        redirectAttributes.addAttribute("search", search);

        return "Main/ProjectPages/EditProject";
    }

    @PostMapping(path="/EditProject")
    public String editProject(@RequestParam(name = "nom" ) String nom, HttpSession session ,Model model ,
                              @RequestParam(name = "Project_id" ) int project_id,
                              @RequestParam(name = "description" ) String description,
                              @RequestParam(name = "ProjectTeam" ,defaultValue = "-1") int projectTeam,
                              @ModelAttribute("search" ) String search)
    {
        User user = (User) session.getAttribute("connectedUser");

        model.addAttribute("user",user);
        Project editProject = projectRepository.findProjectById(project_id);
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
//                    taskRepository.save(task);
                }
            }
            projectRepository.save(editProject);
            return "redirect:/project?search="+search;

        } catch (Exception e){
            return "redirect:/EditProject?error";

        }

    }
}

