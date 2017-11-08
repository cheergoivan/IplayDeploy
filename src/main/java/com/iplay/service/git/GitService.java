package com.iplay.service.git;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PullCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.lib.TextProgressMonitor;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.springframework.stereotype.Service;

import com.iplay.entity.Credential;

@Service
public class GitService {
	
	public void clone(String localRepository, String remoteRepository, Credential credential, Writer out)
			throws IOException, InvalidRemoteException, TransportException, GitAPIException {
		CloneCommand cloneCommand = Git.cloneRepository().setURI(remoteRepository);
		out.write("Clone from " + remoteRepository + System.getProperty("line.separator"));
		cloneCommand.setCredentialsProvider(
				new UsernamePasswordCredentialsProvider(credential.getUsername(), credential.getPassword()));
		cloneCommand.setProgressMonitor(new TextProgressMonitor(out));
		Path localRepo = Paths.get(localRepository);
		if (!Files.exists(localRepo))
			Files.createDirectories(localRepo);
		cloneCommand.setDirectory(localRepo.toFile()).call();
	}

	public void pull(File localRepository, Credential credential, Writer out) throws Exception {
		Git git = Git.open(localRepository);
		out.write("Fetch from " + getOriginRemoteURL(git) + System.getProperty("line.separator"));
		PullCommand pullCommand = git.pull();
		pullCommand.setCredentialsProvider(
				new UsernamePasswordCredentialsProvider(credential.getUsername(), credential.getPassword()));
		pullCommand.setProgressMonitor(new TextProgressMonitor(out));
		pullCommand.call();
	}

	private String getOriginRemoteURL(Git git) {
		return git.getRepository().getConfig().getString("remote", "origin", "url");
	}

}
