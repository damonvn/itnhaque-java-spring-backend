package com.onlinecode.itnhaque.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import com.onlinecode.itnhaque.domain.Skill;
import com.onlinecode.itnhaque.domain.response.ResultPaginationDTO;
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

    public Skill update(Skill updateSkill) {
        return this.skillRepository.save(updateSkill);
    }

    public List<Skill> fetchAllSkills() {
        return this.skillRepository.findAll();
    }

    public boolean existsByName(String name) {
        return this.skillRepository.existsByName(name);
    }

    public boolean existsByValue(String value) {
        return this.skillRepository.existsByValue(value);
    }

    public Skill fetchById(int id) {
        Optional<Skill> roleOptional = this.skillRepository.findById(id);
        if (roleOptional.isPresent())
            return roleOptional.get();
        return null;
    }

    public ResultPaginationDTO fetchSkillsPagination(Specification<Skill> spec, Pageable pageable) {
        Page<Skill> pSkill = this.skillRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());

        mt.setPages(pSkill.getTotalPages());
        mt.setTotal(pSkill.getTotalElements());

        rs.setMeta(mt);
        rs.setResult(pSkill.getContent());
        return rs;
    }

    public void delete(int id) {
        this.skillRepository.deleteById(id);
    }
}
