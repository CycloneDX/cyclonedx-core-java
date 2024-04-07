package org.cyclonedx.model.component.evidence;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import org.cyclonedx.Version;
import org.cyclonedx.model.ExtensibleElement;
import org.cyclonedx.model.VersionFilter;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Occurrence extends ExtensibleElement
{
  @JacksonXmlProperty(isAttribute = true, localName = "bom-ref")
  @JsonProperty("bom-ref")
  private String bomRef;

  private String location;

  @VersionFilter(value = Version.VERSION_16)
  private Integer line;

  @VersionFilter(value = Version.VERSION_16)
  private Integer offset;

  @VersionFilter(value = Version.VERSION_16)
  private Integer symbol;

  @VersionFilter(value = Version.VERSION_16)
  private String additionalContext;

  public String getBomRef() {
    return bomRef;
  }

  public void setBomRef(final String bomRef) {
    this.bomRef = bomRef;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(final String location) {
    this.location = location;
  }

  public Integer getLine() {
    return line;
  }

  public void setLine(final Integer line) {
    this.line = line;
  }

  public Integer getOffset() {
    return offset;
  }

  public void setOffset(final Integer offset) {
    this.offset = offset;
  }

  public Integer getSymbol() {
    return symbol;
  }

  public void setSymbol(final Integer symbol) {
    this.symbol = symbol;
  }

  public String getAdditionalContext() {
    return additionalContext;
  }

  public void setAdditionalContext(final String additionalContext) {
    this.additionalContext = additionalContext;
  }
}
