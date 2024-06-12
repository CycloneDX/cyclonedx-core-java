package org.cyclonedx.model.definition;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder({
    "standards"
})
public class Definition
{
  private List<Standard> standards;

  @JacksonXmlElementWrapper(localName = "standards")
  @JacksonXmlProperty(localName = "standard")
  public List<Standard> getStandards() {
    return standards;
  }

  public void setStandards(final List<Standard> standards) {
    this.standards = standards;
  }

  @Override
  public boolean equals(final Object object) {
    if (this == object) {
      return true;
    }
    if (!(object instanceof Definition)) {
      return false;
    }
    Definition that = (Definition) object;
    return Objects.equals(standards, that.standards);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(standards);
  }
}
