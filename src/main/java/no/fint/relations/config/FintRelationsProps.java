package no.fint.relations.config;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;

public class FintRelationsProps {

    @Value("${server.port:0}")
    private int serverPort;

    @Value("${local.server.port:0}")
    private int localServerPort;

    @Getter
    private int port = 0;

    @Getter
    @Value("${fint.relations.default-base-url:https://api.felleskomponent.no}")
    private String relationBase;

    @Getter
    @Value("${fint.relations.force-https:true}")
    private String forceHttps;

    @PostConstruct
    public void init() {
        if (localServerPort > 0) {
            port = localServerPort;
        } else if (serverPort > 0) {
            port = serverPort;
        }
    }
}
