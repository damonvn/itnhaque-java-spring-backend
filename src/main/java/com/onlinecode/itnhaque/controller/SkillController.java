package com.onlinecode.itnhaque.controller;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.onlinecode.itnhaque.domain.Skill;
import com.onlinecode.itnhaque.domain.response.ResultPaginationDTO;
import com.onlinecode.itnhaque.service.SkillService;
import com.onlinecode.itnhaque.util.annotation.ApiMessage;
import com.onlinecode.itnhaque.util.error.IdInvalidException;
import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

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
        if (this.skillService.existsByValue(s.getValue())) {
            throw new IdInvalidException("Role with value = " + s.getValue() + " already existed");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(this.skillService.create(s));
    }

    @GetMapping("/skill/{id}")
    @ApiMessage("Fetch skill by id")
    public ResponseEntity<Skill> getById(@PathVariable("id") int id) throws IdInvalidException {
        Skill skill = this.skillService.fetchById(id);
        if (skill == null) {
            throw new IdInvalidException("id = " + id + " does not exist");
        }
        return ResponseEntity.ok().body(skill);
    }

    @PutMapping("/skill")
    @ApiMessage("Update a skill")
    public ResponseEntity<Skill> update(@Valid @RequestBody Skill s) throws IdInvalidException {
        Skill skillDB = this.skillService.fetchById(s.getId());
        // check id
        if (skillDB == null) {
            throw new IdInvalidException("Skill with id = " + s.getId() + " does not exist");
        }

        String name = skillDB.getName();
        if (!name.equals(s.getName()) && this.skillService.existsByName(s.getName())) {
            throw new IdInvalidException("Name = " + s.getName() + " already exists");
        }

        String value = skillDB.getValue();
        if (!value.equals(s.getValue()) && this.skillService.existsByValue(s.getValue())) {
            throw new IdInvalidException("Value = " + s.getValue() + " already exists");
        }

        skillDB.setName(s.getName());
        skillDB.setValue(s.getValue());
        return ResponseEntity.ok().body(this.skillService.update(skillDB));
    }

    @GetMapping("/skill/all")
    @ApiMessage("get all skills")
    public ResponseEntity<List<Skill>> getMethodName() {
        return ResponseEntity.ok(this.skillService.fetchAllSkills());
    }

    @GetMapping("/skill")
    @ApiMessage("Fetch categories pagination")
    public ResponseEntity<ResultPaginationDTO> fetchSkill(
            @Filter Specification<Skill> spec, Pageable pageable) {
        return ResponseEntity.ok(this.skillService.fetchSkillsPagination(spec,
                pageable));
    }

    @DeleteMapping("/skill/{id}")
    @ApiMessage("Delete a skill")
    public ResponseEntity<Void> delete(@PathVariable("id") int id) throws IdInvalidException {
        // check id
        if (this.skillService.fetchById(id) == null) {
            throw new IdInvalidException("Skill with id = " + id + " does not exist");
        }
        this.skillService.delete(id);
        return ResponseEntity.ok().body(null);
    }
}
