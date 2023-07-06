package org.cyclonedx.model.component.modelCard;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.cyclonedx.model.ExtensibleElement;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class InputOutputParameter extends ExtensibleElement
{
  private String format;

  public String getFormat() {
    return format;
  }

  public void setFormat(final String format) {
    this.format = format;
  }
}
