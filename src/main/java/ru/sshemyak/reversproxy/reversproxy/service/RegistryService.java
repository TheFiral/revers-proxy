package ru.sshemyak.reversproxy.reversproxy.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.sshemyak.reversproxy.reversproxy.dto.DiscoveryService;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RegistryService {

    private static final Logger logger = LoggerFactory.getLogger(RegistryService.class);
    private final HttpClient client = HttpClient.newHttpClient();
    private final Map<String, List<DiscoveryService>> discoveryServices = new HashMap<>();

    public void registry(HttpServletRequest request) {
        String name = request.getParameter("service");
        int port = request.getRemotePort();
        if (discoveryServices.containsKey(name)) {
            List<DiscoveryService> list = discoveryServices.get(name);
            list.add(new DiscoveryService(name, port, 0));
        } else {
            ArrayList<DiscoveryService> list = new ArrayList<>();
            list.add(new DiscoveryService(name, port, 0));
            discoveryServices.put(name, list);
        }
        logger.info("Registered new service with name {} on port {}", name, port);
    }

    public void redirect(HttpServletRequest request, HttpServletResponse response) throws IOException, InterruptedException {
        DiscoveryService discoverService = findDiscoverService(request.getRequestURI());
        HttpRequest redirectRequest = HttpRequest.newBuilder(URI.create("http://localhost:" + discoverService.getPort() + request.getRequestURI()))
                .method(request.getMethod(), HttpRequest.BodyPublishers.ofInputStream(() -> {
                    try {
                        return request.getInputStream();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }))
                .build();
        HttpResponse<String> redirectResponse = client.send(redirectRequest, HttpResponse.BodyHandlers.ofString());
        response.getWriter().print(redirectResponse.body());
    }

    public void healthCheckServices() {
        discoveryServices.forEach((key, value) -> {
            logger.info("Check services with name {}", key);
            List<DiscoveryService> activeServices = value.stream()
                    .filter(discoveryService -> {
                        HttpRequest request = HttpRequest.newBuilder(
                                        URI.create("http://localhost:" + discoveryService.getPort() + "/actuator/health"))
                                .GET()
                                .build();
                        try {
                            HttpResponse<InputStream> response = client.send(request, HttpResponse.BodyHandlers.ofInputStream());
                            return response.statusCode() == 200;
                        } catch (Exception e) {
                            logger.info("Services {} not registry", discoveryService);
                            return false;
                        }
                    }).toList();
            discoveryServices.put(key, activeServices);
        });
    }

    private DiscoveryService findDiscoverService(String uri) {
        String serviceName = uri.split("/")[1];
        List<DiscoveryService> services = discoveryServices.get(serviceName);
        if (services == null || services.isEmpty()) {
            throw new RuntimeException();
        }
        return discoveryServices.get(serviceName)
                .stream()
                .findFirst()
                .orElseThrow();
    }
}
