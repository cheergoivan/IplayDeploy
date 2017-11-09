package com.iplay.entity;

public class Project {
	private String name;
	private Git git = new Git();
	private Build build = new Build();
	private Deployment deployment = new Deployment();
	private EmailNotification emailNotification = new EmailNotification();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Git getGit() {
		return git;
	}

	public void setGit(Git git) {
		this.git = git;
	}

	public Build getBuild() {
		return build;
	}

	public void setBuild(Build build) {
		this.build = build;
	}

	public Deployment getDeployment() {
		return deployment;
	}

	public void setDeployment(Deployment deployment) {
		this.deployment = deployment;
	}

	public EmailNotification getEmailNotification() {
		return emailNotification;
	}

	public void setEmailNotification(EmailNotification emailNotification) {
		this.emailNotification = emailNotification;
	}

	public static class Build {
		private String command;
		private long timeout = -1;

		public String getCommand() {
			return command;
		}

		public void setCommand(String command) {
			this.command = command;
		}

		public long getTimeout() {
			return timeout;
		}

		public void setTimeout(long timeout) {
			this.timeout = timeout;
		}
	}

	public static class Deployment {
		private String command;
		private long timeout = -1;

		public String getCommand() {
			return command;
		}

		public void setCommand(String command) {
			this.command = command;
		}

		public long getTimeout() {
			return timeout;
		}

		public void setTimeout(long timeout) {
			this.timeout = timeout;
		}
	}

	public static class EmailNotification {
		private Trigger trigger;
		private String[] emailList;

		public Trigger getTrigger() {
			return trigger;
		}

		public void setTrigger(Trigger trigger) {
			this.trigger = trigger;
		}

		public String[] getEmailList() {
			return emailList;
		}

		public void setEmailList(String[] emailList) {
			this.emailList = emailList;
		}

		public static enum Trigger {
			ALWAYS, NEVER, ONLY_FAILURE
		}
	}
}
