package com.stuhring.projectmanager.controller;

import com.stuhring.projectmanager.model.Project;
import com.stuhring.projectmanager.service.ProjectService;
import com.stuhring.projectmanager.service.ValidationErrorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/project")
@CrossOrigin
public class ProjectController {

    @Autowired
    private ProjectService projectService;
    @Autowired
    private ValidationErrorService validationErrorService;

    /**
     * Valid:
     *  - When Spring Boot finds an argument annotated with @Valid, it automatically bootstraps the default JSR 380 implementation — Hibernate Validator — and validates the argument.
     *  - When the target argument fails to pass the validation, Spring Boot throws a MethodArgumentNotValidException exception.
     * BindingResult:
     *  - Spring object that holds the result of the validation and binding and contains errors that may have occurred.
     *  - The BindingResult must come right after the model object that is validated or else Spring will fail to validate the object and throw an exception.
     */
    @PostMapping("")
    public ResponseEntity<?> createNewProject(@Valid @RequestBody Project project, BindingResult result) {

        ResponseEntity<?> errorMap = validationErrorService.validationService(result);
        if (errorMap != null) {
            return errorMap;
        }

        Project p = projectService.saveOrUpdateProject(project);
        return new ResponseEntity<Project>(p, HttpStatus.CREATED);
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<?> getProjectById(@PathVariable String projectId) {

        Project project = projectService.findProjectByIdentifier(projectId);

        return new ResponseEntity<Project>(project, HttpStatus.OK);
    }

    @GetMapping("/all")
    public Iterable<Project> getAllProjects() {
        return projectService.findAllProjects();
    }

    @DeleteMapping("/{projectId}")
    public ResponseEntity<?> deleteProjectById(@PathVariable String projectId) {
        projectService.deleteProjectByIdentifier(projectId.toUpperCase());

        return new ResponseEntity<String>("Project with ID: '" + projectId.toUpperCase() + "' was deleted successfully!", HttpStatus.OK);
    }
}
