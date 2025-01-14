package ma.emsi.gestionnairedestaches.controller;

import jakarta.servlet.http.HttpSession;
import ma.emsi.gestionnairedestaches.model.Project;
import ma.emsi.gestionnairedestaches.model.Team;
import ma.emsi.gestionnairedestaches.model.User;
import ma.emsi.gestionnairedestaches.repository.ProjectRepository;
import ma.emsi.gestionnairedestaches.repository.TeamRepository;
import ma.emsi.gestionnairedestaches.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class TeamController {

    private final TeamRepository teamrepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    TeamController(TeamRepository teamrepository, ProjectRepository projectRepository, UserRepository userRepository) {
        this.teamrepository = teamrepository;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/team")
    public String listTeams(@RequestParam(name = "search" , defaultValue = "My Teams") String search, HttpSession session, Model model)
    {
        User user = (User) session.getAttribute("connectedUser");

        List<Team> teams = null;
        if(search.equals("My Teams")){
            teams =  teamrepository.findTeamsByLeader(user.getId());
        }
        if(search.equals("Other Team")){
            teams =  teamrepository.findTeamsByMember(user.getId());
        }
        if(search.equals("All Teams")){
            teams =  teamrepository.findTeamsByUser(user.getId());
        }

        List<User> users = userRepository.findAll();

        model.addAttribute("TeamList", teams);
        model.addAttribute("search", search);
        model.addAttribute("user", user);
        model.addAttribute("users", users);
        return "Main/TeamPages/team";
    }

    @GetMapping("/TeamMembers")
    public String teamMembers(Model model, HttpSession session, @RequestParam(name = "Team_id") int Team_id )
    {
        User user = (User) session.getAttribute("connectedUser");
        Team team = teamrepository.findTeamById(Team_id);
        List<User> users = userRepository.findNotMembersByTeamId(Team_id);

        model.addAttribute("user", user);
        model.addAttribute("users", users);
        model.addAttribute("CurrentTeam", team ) ;
        model.addAttribute("Members", userRepository.findMembersByTeamId(Team_id));
        return "Main/TeamPages/team-members";
    }

    @GetMapping("/TeamProjects")
    public String teamProjects(Model model, HttpSession session, @RequestParam(name = "Team_id") int Team_id )
    {
        User user = (User) session.getAttribute("connectedUser");

        Team team = teamrepository.findTeamById(Team_id);
        List<Project> Projects = projectRepository.findProjectWithoutTeam();

        model.addAttribute("user", user);
        model.addAttribute("Projects", Projects);
        model.addAttribute("CurrentTeam", team ) ;
        model.addAttribute("Projects", team.getProjects() ) ;
        return "Main/TeamPages/team-projects";
    }

    @GetMapping("/DeleteMember")
    public String deleteMember(@RequestParam(name = "Team_id") int Team_id , @RequestParam(name = "Member_id") int Member_id)
    {

        Team team = teamrepository.findTeamById(Team_id);
        team.getMembers().remove(userRepository.findById(Member_id).get());
        teamrepository.save(team);
        return "redirect:/TeamMembers?Team_id=" + Team_id ;
    }

    @PostMapping(path="/AddMember")
    public String addMember(@RequestParam(name = "Team_id") int Team_id ,
                            @RequestParam(name = "AddMemberId") int AddMemberId ) {

        Team team = teamrepository.findTeamById(Team_id);
        team.getMembers().add(userRepository.findById(AddMemberId).get());
        teamrepository.save(team);
        return "redirect:/TeamMembers?Team_id=" + Team_id ;
    }

    @GetMapping("/{id}")
    public String viewTeam(@PathVariable Integer id, Model model, HttpSession session) {
        User user = (User) session.getAttribute("connectedUser");
        if (user == null) {
            return "redirect:/login";
        }
        Team team = teamrepository.findTeamById(id);
        if (team!=null) {
            model.addAttribute("team", team);
            model.addAttribute("user", user);
            return "Main/TeamPages/team";
        } else {
            return "redirect:/teams";
        }
    }

    @PostMapping(path="/create-new-team")
    public String createNewTeam(HttpSession session ,Model model, @RequestParam(name = "nom") String teamName)
    {
        User user = (User) session.getAttribute("connectedUser");
        model.addAttribute("user",user);
        try {
            Team team=new Team();
            team.setNom(teamName);
            team.setLeader(user);
            teamrepository.save(team);
            return "redirect:/team";

        } catch (Exception e){
            return "redirect:/team?error";

        }

    }

    @GetMapping("/NewTeam")
    public String newTeam(HttpSession session,Model model,
                          @RequestParam(name = "search") String search) {

        Team NewTeam = new Team();
        List<User> users = userRepository.findAll();
        User user = (User) session.getAttribute("connectedUser");

        model.addAttribute("search", search);
        model.addAttribute("user", user);
        model.addAttribute("users", users);
        model.addAttribute("NewTeam", NewTeam);
        return "Main/TeamPages/AddTeam";
    }

    @PostMapping(path="/NewTeam")
    public String newTeamP(HttpSession session,@ModelAttribute("NewTeam") Team NewTeam ) {

        User user = (User) session.getAttribute("connectedUser");

        if (NewTeam == null) {
            return "redirect:/NewTeam?error=notfound";
        }
        NewTeam.setLeader(user);
        NewTeam.setMembers(null);
        NewTeam.setProjects(null);
        teamrepository.save(NewTeam);

        return "redirect:/team";
    }

    @GetMapping("/edit-team")
    public String editTeamForm(@RequestParam("Team_id") Integer teamId, Model model,
                               HttpSession session) {
        User user = (User) session.getAttribute("connectedUser");
        if (user == null) {
            return "redirect:/login";
        }
        Team team = teamrepository.findById(teamId).orElse(null);
        if (team == null) {
            return "redirect:/team?error=notfound";
        }
        model.addAttribute("user", user);
        model.addAttribute("team", team);
        return "Main/TeamPages/EditTeam";
    }

    @GetMapping("/update-team")
    public String updateTeam(HttpSession session,Model model,
                             @RequestParam("id") Integer id,
                             @RequestParam("nom") String name)
    {
        User user = (User) session.getAttribute("connectedUser");
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", user);
        try {
            Team team = teamrepository.findById(id).orElse(null);
            if (team == null) {
                return "redirect:/team?error=notfound";
            }
            team.setNom(name);
            team.setLeader(user);
            teamrepository.save(team);
            return "redirect:/team";
        } catch (Exception e) {
            return "redirect:/team?error=update";
        }

    }

    @GetMapping("DeleteTeam")
    public String deleteTeam( Integer Team_id, HttpSession session) {
        User user = (User) session.getAttribute("connectedUser");
        if (user == null) {
            return "redirect:/login";
        }
        // Find the team by id
        Team team = teamrepository.findById(Team_id).orElse(null);
        if (team != null) {
            // Disassociate projects
            for (Project project : team.getProjects()) {
                project.setProjectTeam(null);
                projectRepository.save(project);
            }
            // Now delete the team
            teamrepository.delete(team);}

        return "redirect:/team";
    }
}
