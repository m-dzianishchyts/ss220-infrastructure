package club.ss220.storage.central.spring.web.v1;

import club.ss220.storage.central.spring.web.config.CentralApiConfig;
import jakarta.validation.Valid;
import lombok.Getter;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Getter
@Component
public class CentralApiV1 {

    private final RestTemplate restTemplate;
    private final String baseUrl;

    public CentralApiV1(@Valid CentralApiConfig centralApiConfig) {
        this.baseUrl = centralApiConfig.getEndpoint();
        this.restTemplate = new RestTemplate();
        this.restTemplate.setInterceptors(List.of((request, body, execution) -> {
            request.getHeaders().setContentType(MediaType.APPLICATION_JSON);
            request.getHeaders().setBearerAuth(centralApiConfig.getToken());
            return execution.execute(request, body);
        }));
    }

    public String resolveUrl(String path) {
        return baseUrl + path;
    }
}
