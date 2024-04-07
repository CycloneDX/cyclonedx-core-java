package org.cyclonedx.model.definition;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

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
}
