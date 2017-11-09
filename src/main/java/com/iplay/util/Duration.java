package com.iplay.util;

import java.util.concurrent.TimeUnit;

public class Duration {
	private long duration;
	private TimeUnit timeUnit;
	
	private Duration(long duration, TimeUnit timeUnit) {
		this.duration = duration;
		this.timeUnit = timeUnit;
	}
	
	public static Duration of(long duration, TimeUnit timeUnit){
		return new Duration(duration, timeUnit);
	}

	public long getDuration() {
		return duration;
	}

	public TimeUnit getTimeUnit() {
		return timeUnit;
	}
}
