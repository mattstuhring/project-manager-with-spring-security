package com.stuhring.projectmanager.service;

import com.stuhring.projectmanager.exception.ProjectIdException;
import com.stuhring.projectmanager.model.Backlog;
import com.stuhring.projectmanager.model.Project;
import com.stuhring.projectmanager.respository.BacklogRepository;
import com.stuhring.projectmanager.respository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// Services are used to write business logic in a different layer; specifically from the controller in our use case
@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private BacklogRepository backlogRepository;

    /**
     * Saves Project object in DB.
     * @param project Project object model
     * @return Project object that is now persisted in db
     */
    public Project saveOrUpdateProject(Project project) {
        String projectIdentifier = project.getProjectIdentifier().toUpperCase();

        try {
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
    public Project findProjectByIdentifier(String projectId) {
        Project project = projectRepository.findByProjectIdentifier(projectId.toUpperCase());

        if (project == null) {
            throw new ProjectIdException("Project ID '" + projectId + "' does not exist");
        }

        return projectRepository.findByProjectIdentifier(projectId.toUpperCase());
    }

    /**
     * Find all Project objects in DB by Project Identifier.
     * @return All Projects
     */
    public Iterable<Project> findAllProjects() {
        return projectRepository.findAll();
    }

    /**
     * Delete Project object by Project Identifier.
     * @param projectId Project Identifier
     * @return void
     */
    public void deleteProjectByIdentifier(String projectId) {
        Project project = findProjectByIdentifier(projectId);
        if (project == null) {
            throw new ProjectIdException("Cannot delete Project ID '" + projectId + "'. This project does not exist");
        }

        projectRepository.delete(project);
    }
}
