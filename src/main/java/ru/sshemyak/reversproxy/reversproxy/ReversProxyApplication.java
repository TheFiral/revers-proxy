package ru.sshemyak.reversproxy.reversproxy;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ReversProxyApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(ReversProxyApplication.class)
                .web(WebApplicationType.SERVLET)
                .main(ReversProxyApplication.class)
                .build(args)
                .run(args);
    }

}
