package com.iplay.service.command;

import java.io.InputStream;
import java.util.Scanner;
import java.util.function.Consumer;

public class InputStreamToLinesConsumer implements Consumer<InputStream>{
	
	private Consumer<String> lineConsumer;

	public InputStreamToLinesConsumer(Consumer<String> lineConsumer) {
		super();
		this.lineConsumer = lineConsumer;
	}

	@Override
	public void accept(InputStream t) {
		Scanner scanner = new Scanner(t);
		while(scanner.hasNextLine()){
			lineConsumer.accept(scanner.nextLine());
		}
		scanner.close();
	}

}
