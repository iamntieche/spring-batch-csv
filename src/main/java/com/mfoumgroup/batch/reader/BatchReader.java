package com.mfoumgroup.batch.reader;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.core.io.FileSystemResource;
import org.springframework.validation.BindException;

import com.mfoumgroup.batch.dto.InputData;

/**
 * Spring batch reader : read all the input data files
 * 
 * @author mfoum
 *
 */
public class BatchReader extends MultiResourceItemReader<InputData> {

	Logger log = LoggerFactory.getLogger(BatchReader.class);

	public BatchReader(String workDirPath) {
		log.info("Batch reader starting to read input data in repository : " + workDirPath);
		this.setResources(getInputResources(workDirPath));
		this.setDelegate(readOneFile());

	}

	/**
	 * Get all the resources (files) to be readen by this spring batch reader.
	 * 
	 * @param workDirPath
	 * @return
	 */
	private FileSystemResource[] getInputResources(String workDirPath) {
		
		List<FileSystemResource> inputResources = new ArrayList<FileSystemResource>();
		File inputDir = new File(workDirPath);
		
		if(inputDir != null && inputDir.isDirectory()) {
			File[] inputFiles = inputDir.listFiles();
			if(inputFiles != null) {
				for (File file : inputFiles) {
					log.info("Reading file : " + file.getAbsolutePath());
					FileSystemResource resource = new FileSystemResource(file);
					inputResources.add(resource);
				}
			}
		}
		
		return inputResources.toArray(new FileSystemResource[inputResources.size()]);
	}

	/*
	 * private FileSystemResource[] getInputResources(String workDirPath) {
	 * 
	 * File inputDir = new File(workDirPath); File[] files = inputDir.listFiles();
	 * List<File> filesList = Arrays.asList(files);
	 * 
	 * List<FileSystemResource> inputResources = filesList.stream().filter(file ->
	 * file != null && file.isFile()) .peek(file -> log.info("Reading file : " +
	 * file.getAbsolutePath())) .map(file -> new
	 * FileSystemResource(file)).collect(Collectors.toList());
	 * 
	 * return inputResources.toArray(new FileSystemResource[inputResources.size()]);
	 * }
	 */

	private FlatFileItemReader<InputData> readOneFile() {
		FlatFileItemReader<InputData> resourceReader = new FlatFileItemReader<InputData>();

		// Skip the first line wich is the header
		resourceReader.setLinesToSkip(1);

		resourceReader.setLineMapper(new DefaultLineMapper<InputData>() {

			private DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer(";");
			private FieldSetMapper<InputData> fieldSetMapper = new BeanWrapperFieldSetMapper<InputData>() {

				@Override
				public InputData mapFieldSet(FieldSet pFielSet) throws BindException {

					InputData inputData = new InputData();
					inputData.setProductName(pFielSet.readString("productName"));
					inputData.setProductEanCode(pFielSet.readString("productEanCode"));
					inputData.setProductType(pFielSet.readString("productType"));
					inputData.setProductAmount(pFielSet.readDouble("productAmount"));
					inputData.setProductQuantity(pFielSet.readInt("productQuantity"));
					inputData.setSupplierName(pFielSet.readString("supplierName"));
					inputData.setSupplierAddress(pFielSet.readString("supplierAddress"));
					inputData.setPurchaserEmail(pFielSet.readString("purchaserEmail"));
					inputData.setPurchaserFirstName(pFielSet.readString("purchaserFirstName"));
					inputData.setPurchaserLastName(pFielSet.readString("purchaserLastName"));
					inputData.setTransactionDate(pFielSet.readDate("transactionDate"));
					return inputData;
				}

			};

			@Override
			public InputData mapLine(String pLine, int pLineNumber) throws Exception {

				lineTokenizer.setNames(new String[] { "productName", "productEanCode", "productType", "productQuantity",
						"productAmount", "supplierName", "supplierAddress", "purchaserFirstName", "purchaserLastName",
						"purchaserEmail", "transactionDate" });
				return fieldSetMapper.mapFieldSet(lineTokenizer.tokenize(pLine));
			}

		});
		return resourceReader;
	}

}
