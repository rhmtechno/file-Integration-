package com.rhmtech.intrigration.api.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.file.DefaultFileNameGenerator;
import org.springframework.integration.file.FileReadingMessageSource;
import org.springframework.integration.file.FileWritingMessageHandler;
import org.springframework.integration.file.filters.CompositeFileListFilter;
import org.springframework.integration.file.filters.SimplePatternFileListFilter;

import java.io.File;
import java.util.Date;

@Configuration
@EnableIntegration
public class IntegrationConfig {
	
	private static Logger log=LoggerFactory.getLogger(IntegrationConfig.class);
	
	 @Bean
	 
	    @InboundChannelAdapter(value = "fileInputChannel", poller = @Poller(fixedRate= "100"))
	    public FileReadingMessageSource fileReadingMessageSource() {
	        CompositeFileListFilter<File> filter=new CompositeFileListFilter<>();
	        filter.addFilter(new SimplePatternFileListFilter("*"));
	        FileReadingMessageSource readder = new FileReadingMessageSource();
	        readder.setDirectory(new File("C:\\Users\\Rakib_laptop\\Desktop\\‎in"));
	        readder.setFilter(filter);
	        return readder;
	    }

	    @Bean
	    @ServiceActivator(inputChannel = "fileInputChannel")
	    public FileWritingMessageHandler fileWritingMessageHandler() {
	        FileWritingMessageHandler writter = new FileWritingMessageHandler(new File("C:\\Users\\Rakib_laptop\\Desktop\\out"));
	        writter.setAutoCreateDirectory(true);
	        writter.setExpectReply(false);
	        writter.setFileNameGenerator(defaultFileNameGenerator());
	        writter.setDeleteSourceFiles(true);
	        return writter;
	    }
	    
	    @Bean
	    public DefaultFileNameGenerator defaultFileNameGenerator() {
	        DefaultFileNameGenerator defaultFileNameGenerator = new DefaultFileNameGenerator();
	       // defaultFileNameGenerator.setExpression("headers.id + '_' + payload.name");
	        defaultFileNameGenerator.setExpression("payload.name+'.complete'");
	        //defaultFileNameGenerator.setExpression("headers['id']");
	        return defaultFileNameGenerator;
	    }
}
