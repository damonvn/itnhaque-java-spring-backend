package com.onlinecode.itnhaque.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import com.onlinecode.itnhaque.domain.Skill;
import com.onlinecode.itnhaque.repository.SkillRepository;

@Service
public class SkillService {
    private final SkillRepository skillRepository;

    public SkillService(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    public Skill create(Skill s) {
        return this.skillRepository.save(s);
    }

    public List<Skill> fetchAllSkills() {
        return this.skillRepository.findAll();
    }

    public boolean existByValue(String value) {
        return this.skillRepository.existsByValue(value);
    }

    public Skill fetchById(int id) {
        Optional<Skill> roleOptional = this.skillRepository.findById(id);
        if (roleOptional.isPresent())
            return roleOptional.get();
        return null;
    }
}
