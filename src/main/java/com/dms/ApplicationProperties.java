package com.dms;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "dms-properties")
public class ApplicationProperties {

	private long schedulerInterval;

	public long getSchedulerInterval() {
		return schedulerInterval;
	}

	public void setSchedulerInterval(long schedulerInterval) {
		this.schedulerInterval = schedulerInterval;
	}

}