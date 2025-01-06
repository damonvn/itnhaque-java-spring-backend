package com.onlinecode.itnhaque.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.onlinecode.itnhaque.domain.Skill;
import com.onlinecode.itnhaque.service.SkillService;
import com.onlinecode.itnhaque.util.annotation.ApiMessage;
import com.onlinecode.itnhaque.util.error.IdInvalidException;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/v1")
public class SkillController {
    private final SkillService skillService;

    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }

    @PostMapping("/skill")
    @ApiMessage("Create a skill")
    public ResponseEntity<Skill> create(@Valid @RequestBody Skill s) throws IdInvalidException {
        if (this.skillService.existByValue(s.getValue())) {
            throw new IdInvalidException("Role with value = " + s.getValue() + " already existed");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(this.skillService.create(s));
    }

    @GetMapping("/skill")
    @ApiMessage("get all skills")
    public ResponseEntity<List<Skill>> getMethodName() {
        return ResponseEntity.ok(this.skillService.fetchAllSkills());
    }
}
