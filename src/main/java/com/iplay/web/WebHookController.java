package com.iplay.web;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iplay.configuration.IplayDeployConfigurationProperties;
import com.iplay.service.IplayDeployTask;

@RestController("/api/webhook")
@EnableConfigurationProperties(IplayDeployConfigurationProperties.class)
public class WebHookController {

	@Autowired
	private IplayDeployTask iplayDeployTask;

	private static final ExecutorService executor = Executors.newSingleThreadExecutor();

	@PostMapping
	public void triggerDeploy() throws IOException {
		executor.execute(iplayDeployTask);
	}
}
