package org.cyclonedx.model.component.evidence;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import org.cyclonedx.Version;
import org.cyclonedx.model.ExtensibleElement;
import org.cyclonedx.model.VersionFilter;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JacksonXmlRootElement(localName = "occurrence")
public class Occurrence extends ExtensibleElement
{
  @JacksonXmlProperty(isAttribute = true, localName = "bom-ref")
  @JsonProperty("bom-ref")
  private String bomRef;

  @JsonProperty("location")
  private String location;

  @VersionFilter(Version.VERSION_16)
  private Integer line;

  @VersionFilter(Version.VERSION_16)
  private Integer offset;

  @VersionFilter(Version.VERSION_16)
  private Integer symbol;

  @VersionFilter(Version.VERSION_16)
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

  @Override
  public boolean equals(final Object object) {
    if (this == object) {
      return true;
    }
    if (!(object instanceof Occurrence)) {
      return false;
    }
    Occurrence that = (Occurrence) object;
    return Objects.equals(bomRef, that.bomRef) && Objects.equals(location, that.location) &&
        Objects.equals(line, that.line) && Objects.equals(offset, that.offset) &&
        Objects.equals(symbol, that.symbol) &&
        Objects.equals(additionalContext, that.additionalContext);
  }

  @Override
  public int hashCode() {
    return Objects.hash(bomRef, location, line, offset, symbol, additionalContext);
  }
}
