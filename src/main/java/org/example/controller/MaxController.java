package org.example.controller;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.example.props.LoadProps;
import org.example.service.MaxRecorder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/metrics")
public class MaxController {

    private final MaxRecorder max;
    private final LoadProps loadProps;

    @GetMapping("/max")
    public Map<String, Object> getMax() {
        return Map.of(
                "millisMax", max.getMax(),
                "millisAvg", max.getAvg(),
                "count", max.getCount(),
                "errorCount", max.getErrors(),
                "mod", loadProps.getMod()
        );
    }

    @PostMapping("/reset")
    public Map<String, String> reset() {
        max.reset();
        return Map.of("status", "ok");
    }

}
