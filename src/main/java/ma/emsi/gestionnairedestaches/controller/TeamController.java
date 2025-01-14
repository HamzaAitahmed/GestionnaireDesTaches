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
    public static final String CONNECTED_USER = "connectedUser";
    public static final String USERS = "users";
    public static final String MY_TEAMS = "My Teams";
    public static final String OTHER_TEAMS = "Other Team";
    public static final String ALL_TEAMS = "All Teams";

    TeamController(TeamRepository teamrepository, ProjectRepository projectRepository, UserRepository userRepository) {
        this.teamrepository = teamrepository;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/team")
    public String listTeams(@RequestParam(name = "search" , defaultValue = MY_TEAMS) String search, HttpSession session, Model model)
    {
        User user = (User) session.getAttribute(CONNECTED_USER);

        List<Team> teams = null;
        if(search.equals(MY_TEAMS)){
            teams =  teamrepository.findTeamsByLeader(user.getId());
        }
        if(search.equals(OTHER_TEAMS)){
            teams =  teamrepository.findTeamsByMember(user.getId());
        }
        if(search.equals(ALL_TEAMS)){
            teams =  teamrepository.findTeamsByUser(user.getId());
        }

        List<User> users = userRepository.findAll();

        model.addAttribute("TeamList", teams);
        model.addAttribute("search", search);
        model.addAttribute("user", user);
        model.addAttribute(USERS, users);
        return "Main/TeamPages/team";
    }

    @GetMapping("/TeamMembers")
    public String teamMembers(Model model, HttpSession session, @RequestParam(name = "Team_id") int teamId )
    {
        User user = (User) session.getAttribute(CONNECTED_USER);
        Team team = teamrepository.findTeamById(teamId);
        List<User> users = userRepository.findNotMembersByTeamId(teamId);

        model.addAttribute("user", user);
        model.addAttribute(USERS, users);
        model.addAttribute("CurrentTeam", team ) ;
        model.addAttribute("Members", userRepository.findMembersByTeamId(teamId));
        return "Main/TeamPages/team-members";
    }

    @GetMapping("/TeamProjects")
    public String teamProjects(Model model, HttpSession session, @RequestParam(name = "Team_id") int teamId )
    {
        User user = (User) session.getAttribute(CONNECTED_USER);

        Team team = teamrepository.findTeamById(teamId);
        List<Project> projects = projectRepository.findProjectWithoutTeam();

        model.addAttribute("user", user);
        model.addAttribute("Projects", projects);
        model.addAttribute("CurrentTeam", team ) ;
        model.addAttribute("Projects", team.getProjects() ) ;
        return "Main/TeamPages/team-projects";
    }

    @GetMapping("/DeleteMember")
    public String deleteMember(@RequestParam(name = "Team_id") int teamId , @RequestParam(name = "Member_id") int memberId)
    {

        Team team = teamrepository.findTeamById(teamId);
        team.getMembers().remove(userRepository.findUserById(memberId));
        teamrepository.save(team);
        return "redirect:/TeamMembers?Team_id=" + teamId ;
    }

    @PostMapping(path="/AddMember")
    public String addMember(@RequestParam(name = "Team_id") int teamId ,
                            @RequestParam(name = "AddMemberId") int addMemberId ) {

        Team team = teamrepository.findTeamById(teamId);
        team.getMembers().add(userRepository.findUserById(addMemberId));
        teamrepository.save(team);
        return "redirect:/TeamMembers?Team_id=" + teamId ;
    }


    @GetMapping("/NewTeam")
    public String newTeam(HttpSession session,Model model,
                          @RequestParam(name = "search") String search) {

        Team newTeam = new Team();
        List<User> users = userRepository.findAll();
        User user = (User) session.getAttribute(CONNECTED_USER);

        model.addAttribute("search", search);
        model.addAttribute("user", user);
        model.addAttribute(USERS, users);
        model.addAttribute("NewTeam", newTeam);
        return "Main/TeamPages/AddTeam";
    }

    @PostMapping(path="/NewTeam")
    public String newTeamP(HttpSession session,@ModelAttribute("NewTeam") Team newTeam ) {

        User user = (User) session.getAttribute(CONNECTED_USER);

        if (newTeam == null) {
            return "redirect:/NewTeam?error=notfound";
        }
        newTeam.setLeader(user);
        newTeam.setMembers(null);
        newTeam.setProjects(null);
        teamrepository.save(newTeam);

        return "redirect:/team";
    }


    @GetMapping("DeleteTeam")
    public String deleteTeam( Integer teamId, HttpSession session) {
        User user = (User) session.getAttribute(CONNECTED_USER);
        if (user == null) {
            return "redirect:/login";
        }
        // Find the team by id
        Team team = teamrepository.findById(teamId).orElse(null);
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
