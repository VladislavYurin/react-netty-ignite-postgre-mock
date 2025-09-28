package org.example.client;

import lombok.RequiredArgsConstructor;
import org.example.dto.CheckResponse;
import org.example.props.TargetProps;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class CheckClient {

    private final WebClient webClient;
    private final TargetProps target;

    public Mono<CheckResponse> callPg(String merchant, String terminal) {
        return webClient.get()
                        .uri(
                                target.getBaseUrl() + target.getPathPg()
                                        + "?merchant={m}&terminal={t}",
                                merchant,
                                terminal
                        )
                        .retrieve()
                        .bodyToMono(CheckResponse.class);
    }

    public Mono<CheckResponse> callIg(String merchant, String terminal) {
        return webClient.get()
                        .uri(
                                target.getBaseUrl() + target.getPathIg()
                                        + "?merchant={m}&terminal={t}",
                                merchant,
                                terminal
                        )
                        .retrieve()
                        .bodyToMono(CheckResponse.class);
    }

    public Mono<CheckResponse> callMp(String merchant, String terminal) {
        return webClient.get()
                        .uri(
                                target.getBaseUrl() + target.getPathMp()
                                        + "?merchant={m}&terminal={t}",
                                merchant,
                                terminal
                        )
                        .retrieve()
                        .bodyToMono(CheckResponse.class);
    }

}
