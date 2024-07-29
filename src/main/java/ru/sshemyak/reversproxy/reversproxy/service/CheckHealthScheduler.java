package ru.sshemyak.reversproxy.reversproxy.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CheckHealthScheduler {

    private static final Logger logger = LoggerFactory.getLogger(CheckHealthScheduler.class);
    private final RegistryService registryService;

    public CheckHealthScheduler(RegistryService registryService) {
        this.registryService = registryService;
    }

    @Scheduled(fixedDelay = 60000)
    public void checkHealth() {
        logger.info("Start health check");
        registryService.healthCheckServices();
    }
}
