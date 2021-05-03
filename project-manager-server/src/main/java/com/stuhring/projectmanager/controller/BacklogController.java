package com.stuhring.projectmanager.controller;

import com.stuhring.projectmanager.model.ProjectTask;
import com.stuhring.projectmanager.service.ProjectTaskService;
import com.stuhring.projectmanager.service.ValidationErrorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping("/api/v1/backlog")
@CrossOrigin
public class BacklogController {

    @Autowired
    private ProjectTaskService projectTaskService;

    @Autowired
    private ValidationErrorService validationErrorService;

    @PostMapping("/{backlog_id}")
    public ResponseEntity<?> addProjectTaskToBacklog(@Valid @RequestBody ProjectTask projectTask,
                                                     BindingResult result,
                                                     @PathVariable String backlog_id,
                                                     Principal principal) {

        ResponseEntity<?> errorMap = validationErrorService.validationService(result);
        if (errorMap != null) {
            return errorMap;
        }

        ProjectTask pt = projectTaskService.addProjectTask(backlog_id, projectTask, principal.getName());

        return new ResponseEntity<ProjectTask>(pt, HttpStatus.CREATED);
    }

    @GetMapping("/{backlog_id}")
    public Iterable<ProjectTask> getProjectBacklog(@PathVariable String backlog_id, Principal principal) {

        return projectTaskService.findBacklogById(backlog_id, principal.getName());
    }

    @GetMapping("/{backlog_id}/{pt_sequence}")
    public ResponseEntity<?> getProjectTask(@PathVariable String backlog_id, @PathVariable String pt_sequence, Principal principal) {

        ProjectTask pt = projectTaskService.findProjectTaskByProjectSequence(backlog_id, pt_sequence, principal.getName());

        return new ResponseEntity<ProjectTask>(pt, HttpStatus.OK);
    }

    @PatchMapping("/{backlog_id}/{pt_sequence}")
    public ResponseEntity<?> updateProjectTask(@Valid @RequestBody ProjectTask projectTask,
                                               BindingResult result,
                                               @PathVariable String backlog_id,
                                               @PathVariable String pt_sequence,
                                               Principal principal) {

        ResponseEntity<?> errorMap = validationErrorService.validationService(result);
        if (errorMap != null) {
            return errorMap;
        }

        ProjectTask updatedTask = projectTaskService.updateByProjectTaskSequence(projectTask, backlog_id, pt_sequence, principal.getName());

        return new ResponseEntity<ProjectTask>(updatedTask, HttpStatus.OK);
    }

    @DeleteMapping("/{backlog_id}/{pt_sequence}")
    public ResponseEntity<?> deleteProjectTask(@PathVariable String backlog_id, @PathVariable String pt_sequence, Principal principal) {
        projectTaskService.deleteProjectTaskByProjectTaskSequence(backlog_id, pt_sequence, principal.getName());

        return new ResponseEntity<String>("Project Task '" + pt_sequence + "' was deleted successfully", HttpStatus.OK);
    }
}
