package ru.sshemyak.reversproxy.reversproxy.dto;

import java.util.Objects;

public class DiscoveryService {

    private String serviceName;
    private int port;
    private int requestCount;

    public DiscoveryService(String serviceName, int port, int requestCount) {
        this.serviceName = serviceName;
        this.port = port;
        this.requestCount = requestCount;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getRequestCount() {
        return requestCount;
    }

    public void setRequestCount(int requestCount) {
        this.requestCount = requestCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DiscoveryService that = (DiscoveryService) o;

        if (port != that.port) return false;
        if (requestCount != that.requestCount) return false;
        return Objects.equals(serviceName, that.serviceName);
    }

    @Override
    public int hashCode() {
        int result = serviceName != null ? serviceName.hashCode() : 0;
        result = 31 * result + port;
        result = 31 * result + requestCount;
        return result;
    }

    @Override
    public String toString() {
        return "DiscoveryService{" +
                "serviceName='" + serviceName + '\'' +
                ", port=" + port +
                '}';
    }
}
