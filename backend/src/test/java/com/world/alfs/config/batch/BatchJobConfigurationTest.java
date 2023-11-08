package com.world.alfs.config.batch;

import com.world.alfs.AlfsApplication;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;


//@TestPropertySource(locations = {"classpath:/application.properties","classpath:/application-ignore.properties"})
@SpringBatchTest
@EnableBatchProcessing
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {BatchJobConfiguration.class})
public class BatchJobConfigurationTest {

    @MockBean
    private Job job; // Mock Job Bean
    @MockBean
    private Step step; // Mock Step Bean

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Test
    @DisplayName("simple job success")
    void simpleJob_success() throws Exception{
        // Configure the behavior of the mock Job and Step beans
//        when(job.execute(any())).thenReturn(new JobExecution(0L));

        // given

        // when
        JobExecution jobExecution = jobLauncherTestUtils.launchJob();

        // then
        assertEquals(BatchStatus.COMPLETED, jobExecution.getStatus());
        assertEquals(ExitStatus.COMPLETED, jobExecution.getExitStatus());
    }
}