package com.craftsman.roy.sample.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * @author luoyh
 * @date Feb 24, 2017
 */
@Entity
@Table(name = "template")
public class Template {

	@Id
	@GeneratedValue
	private long id;
	
	@Column(nullable = false, name = "template_name", length = 32)
	private String templateName;
	
	@Column(nullable = false, name = "template_value", length = 128)
	private String templateValue;
	
	@Column(nullable = false, name = "gmt_created", columnDefinition = "datetime")
	private Date gmtCreated;

	@Column
	private Date gmtModified;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public String getTemplateValue() {
		return templateValue;
	}

	public void setTemplateValue(String templateValue) {
		this.templateValue = templateValue;
	}

	public Date getGmtCreated() {
		return gmtCreated;
	}

	public void setGmtCreated(Date gmtCreated) {
		this.gmtCreated = gmtCreated;
	}

	public Date getGmtModified() {
		return gmtModified;
	}

	public void setGmtModified(Date gmtModified) {
		this.gmtModified = gmtModified;
	}
}
