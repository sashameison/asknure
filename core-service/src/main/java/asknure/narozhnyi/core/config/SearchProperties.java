package asknure.narozhnyi.core.config;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@NoArgsConstructor
@Component
@ConfigurationProperties(prefix = "search")
public class SearchProperties {

  private List<String> fields;
}
