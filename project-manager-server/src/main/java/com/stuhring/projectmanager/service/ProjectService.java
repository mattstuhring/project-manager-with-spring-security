package com.stuhring.projectmanager.service;

import com.stuhring.projectmanager.exception.ProjectIdException;
import com.stuhring.projectmanager.exception.ProjectNotFoundException;
import com.stuhring.projectmanager.model.Backlog;
import com.stuhring.projectmanager.model.Project;
import com.stuhring.projectmanager.model.User;
import com.stuhring.projectmanager.respository.BacklogRepository;
import com.stuhring.projectmanager.respository.ProjectRepository;
import com.stuhring.projectmanager.respository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// Services are used to write business logic in a different layer; specifically from the controller in our use case
@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private BacklogRepository backlogRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Saves Project object in DB.
     * @param project Project object model
     * @return Project object that is now persisted in db
     */
    public Project saveOrUpdateProject(Project project, String username) {
        String projectIdentifier = project.getProjectIdentifier().toUpperCase();

        if (project.getId() != null) {
            Project existingProject = projectRepository.findByProjectIdentifier(project.getProjectIdentifier());

            if (existingProject != null && (!existingProject.getProjectLeader().equals(username))) {
                throw new ProjectNotFoundException("Project not found in your account");
            } else if (existingProject == null) {
                throw new ProjectNotFoundException("Project with ID: '" + project.getProjectIdentifier() + "' cannot be updated because it does not exist");
            }
        }

        try {
            User user = userRepository.findByUsername(username);

            project.setUser(user);
            project.setProjectLeader(user.getUsername());
            project.setProjectIdentifier(projectIdentifier);

            // Create Backlog only for new projects!
            if (project.getId() == null) {
                Backlog backlog = new Backlog();
                project.setBacklog(backlog);
                backlog.setProject(project);
                backlog.setProjectIdentifier(projectIdentifier);
            }

            if (project.getId() != null) {
                project.setBacklog(backlogRepository.findByProjectIdentifier(projectIdentifier));
            }

            return projectRepository.save(project);
        } catch (Exception e) {
            throw new ProjectIdException("Project ID '" + projectIdentifier + "' already exists");
        }
    }

    /**
     * Finds single Project object in DB by Project Identifier.
     * @param projectId Project Identifier
     * @return Project object with matching identifier
     */
    public Project findProjectByIdentifier(String projectId, String username) {
        Project project = projectRepository.findByProjectIdentifier(projectId.toUpperCase());

        if (project == null) {
            throw new ProjectIdException("Project ID '" + projectId + "' does not exist");
        }

        if (!project.getProjectLeader().equals(username)) {
            throw new ProjectNotFoundException("Project not found in your account");
        }

        return projectRepository.findByProjectIdentifier(projectId.toUpperCase());
    }

    /**
     * Find all Project objects in DB by Project Identifier.
     * @return All Projects
     */
    public Iterable<Project> findAllProjects(String username) {
        return projectRepository.findAllByProjectLeader(username);
    }

    /**
     * Delete Project object by Project Identifier.
     * @param projectId Project Identifier
     * @return void
     */
    public void deleteProjectByIdentifier(String projectId, String username) {
        Project project = findProjectByIdentifier(projectId, username);

        projectRepository.delete(project);
    }
}
