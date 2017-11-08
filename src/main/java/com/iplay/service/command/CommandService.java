package com.iplay.service.command;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

import org.springframework.stereotype.Service;

import com.iplay.util.Duration;

@Service
public class CommandService {

	/**
	 * @param directory
	 * @param environment
	 * @param command
	 * @param timeout
	 * @param inputStreamConsumer
	 * @return 0 indicates normal termination and -1 indicates timeout
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public int executeCommand(File directory, Map<String, String> environment, String command, Duration timeout,
			Consumer<InputStream> inputStreamConsumer) throws IOException, InterruptedException {
		Objects.requireNonNull(timeout);
		return executeCommandWithTimeoutLimit(directory, environment, command, timeout, inputStreamConsumer);
	}

	/**
	 * @param directory
	 * @param environment
	 * @param command
	 * @param inputStreamConsumer
	 * @return 0 indicates normal termination
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public int executeCommand(File directory, Map<String, String> environment, String command,
			Consumer<InputStream> inputStreamConsumer) throws IOException, InterruptedException {
		return executeCommandWithTimeoutLimit(directory, environment, command, null, inputStreamConsumer);
	}

	private int executeCommandWithTimeoutLimit(File directory, Map<String, String> environment, String command,
			Duration timeout, Consumer<InputStream> inputStreamConsumer) throws IOException, InterruptedException {
		ProcessBuilder pb = new ProcessBuilder("sh", "-c", command);
		pb.directory(directory);
		Optional.ofNullable(environment).ifPresent(en -> {
			en.forEach((k, v) -> pb.environment().put(k, v));
		});
		pb.redirectErrorStream(true);
		Process p = pb.start();
		inputStreamConsumer.accept(p.getInputStream());
		if(timeout == null)
			p.waitFor();
		else{
			boolean terminated = p.waitFor(timeout.getDuration(), timeout.getTimeUnit());
			if (!terminated)
				return -1;
		}
		return p.exitValue();
	}
}
