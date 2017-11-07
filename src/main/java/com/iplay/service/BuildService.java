package com.iplay.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;

import org.springframework.stereotype.Service;

@Service
public class BuildService {

	public Path generateMavenShell(String workspace, String project, String mavenCmd)
			throws IOException {
		String projectWorkspace = workspace + "/" + project;
		Path buildFile = Paths.get(workspace + "/" + project + ".sh");
		if (Files.exists(buildFile)) {
			Files.delete(buildFile);
		}
		return Files.write(buildFile,
				Arrays.asList("export WORKSPACE=" + projectWorkspace, "cd " + projectWorkspace, mavenCmd),
				StandardOpenOption.CREATE);
	}
	
	public Path generatePostStepShell(String workspace, String project, String postStep) throws IOException{
		String projectWorkspace = workspace + "/" + project;
		Path buildFile = Paths.get(workspace + "/" + project + ".sh");
		if (Files.exists(buildFile)) {
			Files.delete(buildFile);
		}
		return Files.write(buildFile,
				Arrays.asList("export WORKSPACE=" + projectWorkspace, postStep),
				StandardOpenOption.CREATE);
	}
}
