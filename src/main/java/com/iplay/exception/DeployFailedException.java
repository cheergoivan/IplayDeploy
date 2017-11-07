package com.iplay.exception;

import java.nio.file.Path;

public class DeployFailedException extends RuntimeException{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Path log;

	public DeployFailedException(Path log) {
		this.log = log;
	}

	public Path getLog() {
		return log;
	}
}
