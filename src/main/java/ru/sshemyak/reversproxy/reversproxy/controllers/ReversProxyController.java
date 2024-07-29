package ru.sshemyak.reversproxy.reversproxy.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.sshemyak.reversproxy.reversproxy.service.RegistryService;

import java.io.IOException;
import java.util.Objects;

@Controller
public class ReversProxyController {

    private final RegistryService server;

    public ReversProxyController(RegistryService server) {
        this.server = server;
    }

    @RequestMapping("/**")
    public void handleAllRequest(HttpServletRequest request,
                                 HttpServletResponse response) throws IOException, InterruptedException {
        if (Objects.equals(request.getRequestURI(), "/registry")
                && Objects.equals(request.getMethod(), "POST")) {
            server.registry(request);
        } else {
            server.redirect(request, response);
        }
    }
}
