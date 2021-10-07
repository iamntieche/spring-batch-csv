package com.mfoumgroup.batch;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.PlatformTransactionManager;

import com.mfoumgroup.batch.dto.ConvertedInputData;
import com.mfoumgroup.batch.dto.InputData;
import com.mfoumgroup.batch.processor.BatchProcessor;
import com.mfoumgroup.batch.reader.BatchReader;
import com.mfoumgroup.batch.utils.BatchJobListener;
import com.mfoumgroup.batch.utils.BatchStepSkipper;
import com.mfoumgroup.batch.writer.BatchWriter;



@SpringBootApplication
@EnableBatchProcessing
public class BatchApplication {

	public static void main(String[] args) {
		SpringApplication.run(BatchApplication.class, args);
	}

	@Value("${path.to.the.work.dir}")
	private String workDirPath;
	
	@Autowired
	private DataSource dataSource;
	
	@Autowired
	PlatformTransactionManager transactionManager;
	
	@Bean
	public JobRepository jobRepositoryObj() throws Exception {
		JobRepositoryFactoryBean jobRepositoryFactory = new JobRepositoryFactoryBean();
		jobRepositoryFactory.setTransactionManager(transactionManager);
		jobRepositoryFactory.setDataSource(dataSource);
		return jobRepositoryFactory.getObject();
	}
	
	@Autowired
	JobBuilderFactory jobBuilderFactory;
	@Autowired
	StepBuilderFactory stepBuilderFactory;
	
	@Bean
	public BatchReader batchReader() {
		return new BatchReader(workDirPath);
	}
	@Bean
	public BatchProcessor batchProcessor() {
		return new BatchProcessor();
	}
	@Bean
	public BatchWriter batchWriter() {
		return new BatchWriter();
	}
	@Bean
	public BatchJobListener batchJobListener() {
		return new BatchJobListener();
	}
	@Bean
	public BatchStepSkipper batchStepSkipper() {
		return new BatchStepSkipper();
	}
	@Bean
	public Step batchStep() {
		return stepBuilderFactory.get("stepDatawarehouseLoader").transactionManager(transactionManager)
				.<InputData, ConvertedInputData>chunk(1)
				.reader(batchReader()).processor(batchProcessor()).writer(batchWriter()).faultTolerant()
				.skipPolicy(batchStepSkipper()).build();
				
	}
	
	@Bean
	public Job jobStep() throws Exception {
		return jobBuilderFactory.get("jobDatawarehouseLoader")
				.repository(jobRepositoryObj()).incrementer(new RunIdIncrementer())
				.listener(batchJobListener())
				.flow(batchStep()).end().build();
	}
}
