package com.piggymetrics.notification;

import com.piggymetrics.notification.config.TestSecurityConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;


@ExtendWith(org.springframework.test.context.junit.jupiter.SpringExtension.class)
@SpringBootTest
@Import(TestSecurityConfig.class)
public class NotificationServiceApplicationTests {

	@Test
	public void contextLoads() {
	}

}
