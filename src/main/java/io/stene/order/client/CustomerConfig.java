package io.stene.order.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@AllArgsConstructor
@NoArgsConstructor
public class CustomerConfig {
    @Value("${provider.url}")
    private String url;
}