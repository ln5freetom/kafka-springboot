package com.example.ecommerce.config;

import com.example.ecommerce.batch.OrderExportCsvRecord;
import com.example.ecommerce.batch.OrderExportItemProcessor;
import com.example.ecommerce.dto.OrderExportCompletedNotification;
import com.example.ecommerce.entity.Order;
import com.example.ecommerce.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.PlatformTransactionManager;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
@EnableScheduling
public class BatchConfig {

    @Value("${app.export.directory:./exports}")
    private String exportDir;

    @Value("${app.kafka.email-topic}")
    private String emailTopic;

    private final OrderRepository orderRepository;
    private final KafkaTemplate<String, OrderExportCompletedNotification> kafkaTemplate;

    public BatchConfig(OrderRepository orderRepository, KafkaTemplate<String, OrderExportCompletedNotification> kafkaTemplate) {
        this.orderRepository = orderRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Bean
    public RepositoryItemReader<Order> orderItemReader() {
        RepositoryItemReader<Order> reader = new RepositoryItemReader<>();
        reader.setRepository(orderRepository);
        reader.setMethodName("findAll");
        reader.setPageSize(100);
        return reader;
    }

    @Bean
    public OrderExportItemProcessor orderExportItemProcessor() {
        return new OrderExportItemProcessor();
    }

    @Bean
    public FlatFileItemWriter<OrderExportCsvRecord> orderExportCsvWriter() {
        String fileName = exportDir + File.separator + "orders-export-" + LocalDate.now() + ".csv";
        return new FlatFileItemWriterBuilder<OrderExportCsvRecord>()
                .name("orderExportCsvWriter")
                .resource(new FileSystemResource(fileName))
                .delimited()
                .delimiter(",")
                .names("orderId", "orderNumber", "status", "totalAmount", "customerEmail", "customerName", "orderDate")
                .headerCallback(writer -> writer.write("orderId,orderNumber,status,totalAmount,customerEmail,customerName,orderDate"))
                .build();
    }

    @Bean
    public Step exportOrdersStep(JobRepository jobRepository,
                                 PlatformTransactionManager transactionManager,
                                 RepositoryItemReader<Order> orderItemReader,
                                 OrderExportItemProcessor processor,
                                 FlatFileItemWriter<OrderExportCsvRecord> writer) {
        return new StepBuilder("exportOrdersStep", jobRepository)
                .<Order, OrderExportCsvRecord>chunk(100, transactionManager)
                .reader(orderItemReader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Bean
    public Job orderExportJob(JobRepository jobRepository,
                              @Qualifier("exportOrdersStep") Step exportOrdersStep) {
        return new JobBuilder("orderExportJob", jobRepository)
                .start(exportOrdersStep)
                .listener(new org.springframework.batch.core.JobExecutionListener() {
                    @Override
                    public void afterJob(org.springframework.batch.core.JobExecution jobExecution) {
                        long total = jobExecution.getStepExecutions().stream()
                                .mapToLong(step -> step.getWriteCount())
                                .sum();
                        int orderCount = (int) total;
                        String filePath = exportDir + File.separator + "orders-export-" + LocalDate.now() + ".csv";
                        OrderExportCompletedNotification notification = new OrderExportCompletedNotification(
                                filePath,
                                LocalDate.now(),
                                "admin@example.com",
                                orderCount
                        );
                        kafkaTemplate.send(emailTopic, notification);
                    }
                })
                .build();
    }
}
