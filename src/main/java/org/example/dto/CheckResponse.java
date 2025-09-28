package org.example.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.lang.Nullable;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CheckResponse {

    @JsonProperty("millis")
    private Long millis;

}
