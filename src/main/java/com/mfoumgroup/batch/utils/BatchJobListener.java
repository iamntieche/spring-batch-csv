package com.mfoumgroup.batch.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

public class BatchJobListener implements JobExecutionListener {

	Logger log = LoggerFactory.getLogger(BatchJobListener.class);

	@Override
	public void beforeJob(JobExecution jobExecution) {
		log.info("Begining of job  " + jobExecution.getJobInstance().getJobName() + " at "
				+ jobExecution.getStartTime());
	}

	@Override
	public void afterJob(JobExecution jobExecution) {
		String exitCode = jobExecution.getExitStatus().getExitCode();
		log.info("End of job  " + jobExecution.getJobInstance().getJobName() + " at " + jobExecution.getEndTime()
				+ " with status " + exitCode);
	}

}
