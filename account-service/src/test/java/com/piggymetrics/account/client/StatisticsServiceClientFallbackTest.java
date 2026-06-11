package com.piggymetrics.account.client;

import com.piggymetrics.account.domain.Account;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(OutputCaptureExtension.class)
@SpringBootTest(properties = {
        "spring.cloud.circuitbreaker.resilience4j.enabled=true"
})
public class StatisticsServiceClientFallbackTest {
    @Autowired
    private StatisticsServiceClient statisticsServiceClient;

    @Test
    public void testUpdateStatisticsWithFailFallback(CapturedOutput output) {
        statisticsServiceClient.updateStatistics("test", new Account());

        assertTrue(output.getAll().contains("Error during update statistics for account: test"));
    }

}
