package at.tugraz.sss.adapter.rest.v3;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import org.hibernate.validator.constraints.NotEmpty;

public class SSAdapterRestDropwizardConf extends Configuration {
  
  @NotEmpty
  private String template;
  
  @JsonProperty
  public String getTemplate() {
    return template;
  }
  
  @JsonProperty
  public void setTemplate(String template) {
    this.template = template;
  }
}