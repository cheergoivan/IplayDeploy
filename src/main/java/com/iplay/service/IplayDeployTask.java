package com.iplay.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import com.iplay.configuration.IplayDeployConfigurationProperties;

@Service
@EnableConfigurationProperties(IplayDeployConfigurationProperties.class)
public class IplayDeployTask implements Runnable {
	private static final Logger LOGGER = LoggerFactory.getLogger(IplayDeployTask.class);

	private static final String BUILD_SUCCESS_TAG = "BUILD SUCCESS";

	@Autowired
	private GitService gitService;

	@Autowired
	private CmdService cmdService;

	@Autowired
	private MailService mailService;

	@Value("${spring.mail.username}")
	private String sender;

	private IplayDeployConfigurationProperties iplayDeployConfigurationProperties;

	@Autowired
	public IplayDeployTask(IplayDeployConfigurationProperties iplayDeployConfigurationProperties) {
		this.iplayDeployConfigurationProperties = iplayDeployConfigurationProperties;
	}

	@Override
	public void run() {
		Path logDir = Paths.get(iplayDeployConfigurationProperties.getWorkspace() + "/" + "log/"
				+ iplayDeployConfigurationProperties.getProject());
		Path log = logDir
				.resolve(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss")) + ".log");
		try {
			if (!Files.exists(logDir))
				Files.createDirectories(logDir);
			if (!Files.exists(log))
				Files.createFile(log);

			ByteArrayOutputStream gitOutput = new ByteArrayOutputStream();
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(gitOutput));
			File localRepository = new File(iplayDeployConfigurationProperties.getWorkspace() + "/"
					+ iplayDeployConfigurationProperties.getProject());
			if (!localRepository.exists()) {
				gitService.clone(
						iplayDeployConfigurationProperties.getWorkspace() + "/"
								+ iplayDeployConfigurationProperties.getProject(),
						iplayDeployConfigurationProperties.getRemoteRepository(),
						iplayDeployConfigurationProperties.getCredential(), pw);
			} else {
				gitService.pull(localRepository, iplayDeployConfigurationProperties.getCredential(), pw);
			}
			Files.write(log, Arrays.asList(new String(gitOutput.toByteArray())), StandardOpenOption.APPEND);

			// maven build
			boolean[] buildSuccess = new boolean[] { false };
			List<String> output = new LinkedList<>();
			output.add(System.getProperty("line.separator"));
			String projectWorkspace = iplayDeployConfigurationProperties.getWorkspace() + "/"
					+ iplayDeployConfigurationProperties.getProject();
			Map<String, String> environment = new HashMap<>();
			environment.put("WORKSPACE", projectWorkspace);
			cmdService.executeCmd(new File(projectWorkspace), environment, "mvn clean package", line -> {
				//System.out.println("print: " + line);
				output.add(line);
				if (!buildSuccess[0] && line.contains(BUILD_SUCCESS_TAG))
					buildSuccess[0] = true;
			});
			Files.write(log, output, StandardOpenOption.APPEND);
			if (!buildSuccess[0])
				throw new Exception("Maven Build Failure!");

			// build success
			List<String> output2 = new LinkedList<>();
			output2.add(System.getProperty("line.separator"));
			cmdService.executeCmd(new File(projectWorkspace), environment,
					iplayDeployConfigurationProperties.getPostStep(), line -> {
						System.out.println("print: " + line);
						output2.add(line);
					});
			Files.write(log, output2, StandardOpenOption.APPEND);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			try {
				e.printStackTrace(new PrintStream(new FileOutputStream(log.toFile(), true)));
			} catch (FileNotFoundException e1) {
				LOGGER.error(e1.getMessage(), e1);
			}
			handleDeployFailure(log);
		}
	}

	private void handleDeployFailure(Path log) {
		try {
			String content = new String(Files.readAllBytes(log));
			for (String email : iplayDeployConfigurationProperties.getEmailAddresses()) {
				mailService.sendMail(sender, email, iplayDeployConfigurationProperties.getProject() + " BUILD FAILURE",
						content);
			}
		} catch (IOException e1) {
			LOGGER.error(e1.getMessage(), e1);
		}
	}

}
