package org.example.service;

import java.time.Duration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.client.CheckClient;
import org.example.dto.CheckResponse;
import org.example.props.LoadProps;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class LoadRunner {

    private final LoadProps props;
    private final IdGenerator ids;
    private final CheckClient client;
    private final MaxRecorder max;

    @Bean
    ApplicationRunner runLoad() {
        return args -> {
            long periodMs = Math.max(1, 1000L / Math.max(1, props.getTps()));
            int concurrency = Math.max(1, props.getConcurrency());
            String mod = props.getMod();

            log.info(
                    "Load started: tps={}, periodMs={}, concurrency={}, mod={}",
                    props.getTps(), periodMs, concurrency, mod
            );

            Flux.interval(Duration.ZERO, Duration.ofMillis(periodMs))
                .onBackpressureDrop()
                .flatMap(t -> {
                    String m = ids.nextMerchant();
                    String tn = ids.nextTerminal();

                    Mono<CheckResponse> call;
                    switch (mod) {
                        case "pg": call = client.callPg(m, tn); break;
                        case "ig": call = client.callIg(m, tn); break;
                        case "mp": call = client.callMp(m, tn); break;
                        case "hz": call = client.callHz(m, tn); break;
                        default:   return Flux.error(new IllegalArgumentException("Unknown mod: " + mod));
                    }

                    log.info("call");
                    return call
                            .doOnNext(r -> max.record(r.getMillis()))
                            .doOnError(e -> max.incrementErrors())       // считаем ошибку
                            .onErrorResume(e -> Mono.empty())            // не валим поток, просто пропускаем эту итерацию
                            .then();
                }, concurrency)
                .subscribe();
        };
    }
}
