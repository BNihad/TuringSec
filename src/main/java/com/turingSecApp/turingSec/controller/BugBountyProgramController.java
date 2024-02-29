package com.turingSecApp.turingSec.controller;

import com.turingSecApp.turingSec.dao.entities.BugBountyProgramEntity;
import com.turingSecApp.turingSec.service.ProgramsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/bug-bounty-programs")
public class BugBountyProgramController {

    @Autowired
    private ProgramsService bugBountyProgramService;

    @PostMapping
    public ResponseEntity<BugBountyProgramEntity> createBugBountyProgram(@Valid @RequestBody BugBountyProgramEntity program) {
        BugBountyProgramEntity createdProgram = bugBountyProgramService.createBugBountyProgram(program);
        return ResponseEntity.created(URI.create("/api/bug-bounty-programs/" + createdProgram.getId())).body(createdProgram);
    }

    @GetMapping
    public ResponseEntity<List<BugBountyProgramEntity>> getAllBugBountyPrograms() {
        List<BugBountyProgramEntity> programs = bugBountyProgramService.getAllBugBountyPrograms();
        return ResponseEntity.ok(programs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BugBountyProgramEntity> getBugBountyProgramById(@PathVariable Long id) {
        BugBountyProgramEntity program = bugBountyProgramService.getBugBountyProgramById(id);
        return ResponseEntity.ok(program);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BugBountyProgramEntity> updateBugBountyProgram(@PathVariable Long id, @Valid @RequestBody BugBountyProgramEntity updatedProgram) {
        BugBountyProgramEntity program = bugBountyProgramService.updateBugBountyProgram(id, updatedProgram);
        return ResponseEntity.ok(program);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBugBountyProgram(@PathVariable Long id) {
        bugBountyProgramService.deleteBugBountyProgram(id);
        return ResponseEntity.noContent().build();
    }
}