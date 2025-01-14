package ma.emsi.gestionnairedestaches;

import ma.emsi.gestionnairedestaches.controller.ProjectController;
import ma.emsi.gestionnairedestaches.model.Project;
import ma.emsi.gestionnairedestaches.model.Task;
import ma.emsi.gestionnairedestaches.model.Team;
import ma.emsi.gestionnairedestaches.model.User;
import ma.emsi.gestionnairedestaches.repository.ProjectRepository;
import ma.emsi.gestionnairedestaches.repository.TaskRepository;
import ma.emsi.gestionnairedestaches.repository.TeamRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.mock.web.MockHttpSession;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProjectController.class)
@Import(TestSecurityConfig.class)
class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProjectRepository projectRepository;

    @MockBean
    private TeamRepository teamRepository;

    @MockBean
    private TaskRepository taskRepository;

    private MockHttpSession session;
    private User testUser;
    private Project testProject;
    private Team testTeam;

    @BeforeEach
    void setUp() {
        session = new MockHttpSession();
        testUser = new User();
        testTeam = new Team();
        testProject = new Project();

        testUser.setId(1);
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");

        testProject.setId(1);
        testProject.setNom("Test Project");
        testProject.setProjectOwner(testUser);
        testProject.setProjectTeam(testTeam);

        testTeam.setId(1);
        testTeam.setNom("Test Team");
        testTeam.setLeader(testUser);

        session.setAttribute("connectedUser", testUser);
    }

    @Test
    @WithMockUser(username = "testuser")
    void testProjectPageMyProjects() throws Exception {
        List<Project> myProjects = Arrays.asList(testProject);
        when(projectRepository.findByProjectOwner(testUser.getId())).thenReturn(myProjects);
        when(projectRepository.findOtherProjectByUserId(testUser.getId())).thenReturn(new ArrayList<>());
        when(projectRepository.findAllProjectByUserId(testUser.getId())).thenReturn(myProjects);

        mockMvc.perform(get("/project")
                        .param("search", "My Projects")
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("PorjectList"));
    }

    @Test
    @WithMockUser(username = "testuser")
    void testProjectPageOtherProjects() throws Exception {
        List<Project> otherProjects = Arrays.asList(testProject); // create projects for this scenario
        when(projectRepository.findOtherProjectByUserId(testUser.getId())).thenReturn(otherProjects);
        when(projectRepository.findByProjectOwner(testUser.getId())).thenReturn(new ArrayList<>());
        when(projectRepository.findAllProjectByUserId(testUser.getId())).thenReturn(otherProjects);

        mockMvc.perform(get("/project")
                        .param("search", "Other Projects")
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("PorjectList"));
    }

    @Test
    @WithMockUser(username = "testuser")
    void testProjectPageAllProjects() throws Exception {
        List<Project> allProjects = Arrays.asList(testProject);
        when(projectRepository.findAllProjectByUserId(testUser.getId())).thenReturn(allProjects);

        mockMvc.perform(get("/project")
                        .param("search", "All Projects")
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("PorjectList"));
    }

    @Test
    @WithMockUser(username = "testuser")
    void testNewProjectGet() throws Exception {
        List<Team> teams = Arrays.asList(new Team());
        when(teamRepository.findAll()).thenReturn(teams);

        mockMvc.perform(get("/NewProject")
                        .param("search", "My Projects")
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("Project"))
                .andExpect(model().attributeExists("ListTeams"));
    }

    @Test
    @WithMockUser(username = "testuser")
    void testCreateNewProject() throws Exception {
        when(projectRepository.save(testProject)).thenReturn(testProject);

        mockMvc.perform(post("/NewProject")
                        .session(session)
                        .param("nom", "Test Project")
                        .param("description", "Test description")
                        .param("search", "My Projects"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/project?search=My+Projects"));
    }

    @Test
    @WithMockUser(username = "testuser")
    void testCreateNewProjectError() throws Exception {
        when(projectRepository.save(testProject)).thenThrow(new RuntimeException("Error saving project"));

        mockMvc.perform(post("/NewProject")
                        .session(session)
                        .param("nom", "Test Project")
                        .param("description", "Test description"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/project"));
    }

    @Test
    @WithMockUser(username = "testuser")
    void testEditProjectGet() throws Exception {
        when(projectRepository.findProjectById(1)).thenReturn(testProject);
        when(teamRepository.findAll()).thenReturn(Arrays.asList(new Team()));

        mockMvc.perform(get("/EditProject")
                        .param("projectId", "1")
                        .param("search", "My Projects")
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("Project"))
                .andExpect(model().attributeExists("ListTeams"));
    }

    @Test
    @WithMockUser(username = "testuser")
    void testEditProjectPost() throws Exception {
        when(projectRepository.findProjectById(1)).thenReturn(testProject);
        when(teamRepository.findTeamById(1)).thenReturn(testTeam);

        mockMvc.perform(post("/EditProject")
                        .param("Project_id", "1")
                        .param("nom", "Updated Project")
                        .param("description", "Updated description")
                        .param("ProjectTeam", "1")
                        .param("search", "My Projects")
                        .session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/project?search=My+Projects"));
    }

    @Test
    @WithMockUser(username = "testuser")
    void testDeleteProject() throws Exception {
        List<Task> tasks = new ArrayList<>();
        when(taskRepository.findProjectTaskById(1)).thenReturn(tasks);

        mockMvc.perform(get("/deleteProject")
                        .param("projectId", "1")
                        .param("search", "My Projects")
                        .session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/project?search=My+Projects"));
    }
}
