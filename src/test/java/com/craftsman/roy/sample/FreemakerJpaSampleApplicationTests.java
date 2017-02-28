package com.craftsman.roy.sample;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.craftsman.roy.sample.jpa.TemplateRepository;
import com.craftsman.roy.sample.jpa.UserRespository;
import com.craftsman.roy.sample.model.Template;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FreemakerJpaSampleApplicationTests {

	@Autowired
	UserRespository userRepository;
	@Autowired
	TemplateRepository templateRepository;
	
	@Test
	public void contextLoads() {
		Template template = new Template();
		template.setGmtCreated(new Date());
		template.setGmtModified(new Date());
		template.setTemplateName("template name ");
		template.setTemplateValue("value");
		templateRepository.save(template);
		System.out.println(templateRepository.findAll().size());
	}
	
}
