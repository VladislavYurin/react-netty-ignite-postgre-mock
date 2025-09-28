package org.example.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "load")
public class LoadProps {

    private int tps = 100;
    private int concurrency = 64;
    private String mod = "pg";

}
