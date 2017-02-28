package com.craftsman.roy.sample.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.craftsman.roy.sample.model.Template;

/**
 * 
 * @author luoyh
 * @date Feb 24, 2017
 */
@Repository("templateRepository")
public interface TemplateRepository extends JpaRepository<Template, Long> {
}
