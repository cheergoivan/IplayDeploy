package com.iplay.service;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Scanner;

import org.springframework.stereotype.Service;

@Service
public class CmdService {

	public void executeShell(CmdOutputHandler handler, String shellFile, String... args)
			throws IOException, InterruptedException {
		// System.out.println("execute shell file " + shellFile);
		StringBuilder sb = new StringBuilder();
		sb.append("sh " + shellFile);
		for (String arg : args) {
			sb.append(" " + arg);
		}
		executeCmd(handler, sb.toString());
	}

	public void executeCmd(CmdOutputHandler handler, String cmd) throws IOException, InterruptedException {
		String[] cmds = new String[] { "sh", "-c", cmd };
		ProcessBuilder pb = new ProcessBuilder(cmds);
		//pb.inheritIO();
		pb.redirectErrorStream(true);
		Process p = pb.start();
		Scanner scanner = new Scanner(p.getInputStream());
		while (scanner.hasNextLine()) {
			handler.handle(scanner.nextLine());
		}
		scanner.close();
		p.waitFor();
	}

	public void executeCmd(File directory, Map<String, String> environment, String cmd, CmdOutputHandler handler)
			throws IOException, InterruptedException {
		String[] cmds = new String[] { "sh", "-c", cmd };
		ProcessBuilder pb = new ProcessBuilder(cmds);
		if (directory != null)
			pb.directory(directory);
		environment.forEach((k, v) -> pb.environment().put(k, v));
		pb.redirectErrorStream(true);
		Process p = pb.start();
		Scanner scanner = new Scanner(p.getInputStream());
		while (scanner.hasNextLine()) {
			handler.handle(scanner.nextLine());
		}
		scanner.close();
		p.waitFor();
	}
}
