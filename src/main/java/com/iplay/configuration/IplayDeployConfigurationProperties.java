package com.iplay.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.iplay.entity.Credential;

@ConfigurationProperties("iplay.deploy")
public class IplayDeployConfigurationProperties {
	
	private String workspace = "/root/.fenkins";
	
	private String project = "FeastBooking";
	
	private String remoteRepository = "https://gitee.com/cheegoivan/FeastBooking";
	
	private Credential credential = new Credential("1179573968@qq.com", "asdfghjkl456");
	
	private String postStep = "sh /usr/local/spring-boot-jenkins.sh FeastBooking 8083 devRemote";
	
	private String emailAddresses[];
	
	public String getWorkspace() {
		return workspace;
	}

	public void setWorkspace(String workspace) {
		this.workspace = workspace;
	}

	public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
	}
	
	public String getRemoteRepository() {
		return remoteRepository;
	}

	public void setRemoteRepository(String remoteRepository) {
		this.remoteRepository = remoteRepository;
	}

	public Credential getCredential() {
		return credential;
	}

	public void setCredential(Credential credential) {
		this.credential = credential;
	}

	public String getPostStep() {
		return postStep;
	}

	public void setPostStep(String postStep) {
		this.postStep = postStep;
	}

	public String[] getEmailAddresses() {
		return emailAddresses;
	}

	public void setEmailAddresses(String[] emailAddresses) {
		this.emailAddresses = emailAddresses;
	}
}
