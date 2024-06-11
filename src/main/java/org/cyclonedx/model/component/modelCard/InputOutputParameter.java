package org.cyclonedx.model.component.modelCard;

import java.util.Objects;

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

  @Override
  public boolean equals(final Object object) {
    if (this == object) {
      return true;
    }
    if (!(object instanceof InputOutputParameter)) {
      return false;
    }
    InputOutputParameter that = (InputOutputParameter) object;
    return Objects.equals(format, that.format);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(format);
  }
}
