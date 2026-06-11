package com.piggymetrics.account.client;

import com.piggymetrics.account.domain.Account;
import com.piggymetrics.account.config.TestSecurityConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(OutputCaptureExtension.class)
@SpringBootTest(properties = {
        "spring.cloud.openfeign.circuitbreaker.enabled=true"
})
@Import(TestSecurityConfig.class)
public class StatisticsServiceClientFallbackTest {
    @Autowired
    private StatisticsServiceClient statisticsServiceClient;

    @Test
    public void testUpdateStatisticsWithFailFallback(CapturedOutput output) {
        statisticsServiceClient.updateStatistics("test", new Account());

        assertTrue(output.getAll().contains("Error during update statistics for account: test"));
    }

}
