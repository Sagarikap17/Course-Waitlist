package com.assessment.course.controller;

import com.assessment.course.model.CourseStateView;
import com.assessment.course.model.OperationResult;
import com.assessment.course.service.CourseWaitlistService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/course")
@CrossOrigin(origins = "http://localhost:4200")
public class CourseController {

    private final CourseWaitlistService service;

    public CourseController(CourseWaitlistService service) {
        this.service = service;
    }

    @GetMapping("/state")
    public ResponseEntity<CourseStateView> getState() {
        return ResponseEntity.ok(service.getCourseState());
    }

    @PostMapping("/enroll")
    public ResponseEntity<OperationResult> enroll(@Valid @RequestBody EnrollRequest request) {
        OperationResult result = service.enroll(request.getLearnerId());
        return ResponseEntity.ok(result);
    }

    @PostMapping("/drop")
    public ResponseEntity<OperationResult> drop(@Valid @RequestBody EnrollRequest request) {
        OperationResult result = service.drop(request.getLearnerId());
        return ResponseEntity.ok(result);
    }
}
