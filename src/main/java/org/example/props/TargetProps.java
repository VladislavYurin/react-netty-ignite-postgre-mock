package org.example.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "target")
public class TargetProps {

    private String baseUrl;
    private String pathPg;
    private String pathIg;
    private String pathMp;


}
