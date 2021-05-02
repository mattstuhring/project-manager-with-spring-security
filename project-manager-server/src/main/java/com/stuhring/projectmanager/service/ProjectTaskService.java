package com.stuhring.projectmanager.service;

import com.stuhring.projectmanager.exception.ProjectNotFoundException;
import com.stuhring.projectmanager.model.Backlog;
import com.stuhring.projectmanager.model.Project;
import com.stuhring.projectmanager.model.ProjectTask;
import com.stuhring.projectmanager.respository.BacklogRepository;
import com.stuhring.projectmanager.respository.ProjectRepository;
import com.stuhring.projectmanager.respository.ProjectTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectTaskService {

    @Autowired
    private BacklogRepository backlogRepository;

    @Autowired
    private ProjectTaskRepository projectTaskRepository;

    @Autowired
    private ProjectRepository projectRepository;

    public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask) {
        try {
            // Get the Backlog for the specified Project Identifier
            Backlog backlog = backlogRepository.findByProjectIdentifier(projectIdentifier);

            // Set the Backlog to Project Task
            projectTask.setBacklog(backlog);

            // Get the Backlog sequence, and then update the sequence
            Integer backlogSequence = backlog.getPTSequence();
            backlogSequence++;
            backlog.setPTSequence(backlogSequence);

            // Add sequence to Project Task
            projectTask.setProjectSequence(projectIdentifier + "-" + backlogSequence.toString());
            projectTask.setProjectIdentifier(projectIdentifier);

            // INITIAL priority when priority is null; 3 is lowest
            if (projectTask.getPriority() == null || projectTask.getPriority() == 0) {
                projectTask.setPriority(3);
            }

            // INITIAL status when status is null
            if (projectTask.getStatus() == null || projectTask.getStatus().equals("")) {
                projectTask.setStatus("TO_DO");
            }

            return projectTaskRepository.save(projectTask);
        } catch (Exception e) {
            throw new ProjectNotFoundException("Project not found");
        }
    }

    public Iterable<ProjectTask> findBacklogById(String id) {

        Project project = projectRepository.findByProjectIdentifier(id);
        if (project == null) {
            throw new ProjectNotFoundException("Project with '" + id + "' does not exist");
        }

        return projectTaskRepository.findByProjectIdentifierOrderByPriority(id);
    }

    public ProjectTask findProjectTaskByProjectSequence(String backlog_id, String pt_sequence) {

        // Backlog for Project must exist
        Backlog backlog = backlogRepository.findByProjectIdentifier(backlog_id);
        if (backlog == null) {
            throw new ProjectNotFoundException("Project with '" + backlog_id + "' does not exist");
        }

        // Project Task must exist
        ProjectTask projectTask = projectTaskRepository.findByProjectSequence(pt_sequence);
        if (projectTask == null) {
            throw new ProjectNotFoundException("Project Task '" + pt_sequence + "' not found");
        }

        // Backlog ID / Project Task Sequence in the path must correspond to the right project
        if (!projectTask.getBacklog().getProjectIdentifier().equals(backlog_id)) {
            throw new ProjectNotFoundException("Project Task '" + pt_sequence + "' does not exist in project: " + backlog_id);
        }

        return projectTask;
    }

    public ProjectTask updateByProjectTaskSequence(ProjectTask updatedTask, String backlog_id, String pt_sequence) {

        ProjectTask projectTask = findProjectTaskByProjectSequence(backlog_id, pt_sequence);

        projectTask = updatedTask;

        return projectTaskRepository.save(projectTask);
    }

    public void deleteProjectTaskByProjectTaskSequence(String backlog_id, String pt_sequence) {
        ProjectTask projectTask = findProjectTaskByProjectSequence(backlog_id, pt_sequence);

        projectTaskRepository.delete(projectTask);
    }
}
