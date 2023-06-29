package org.cyclonedx.model.evidence;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import org.cyclonedx.model.ExtensibleElement;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Frame extends ExtensibleElement
{
  private String packageFrame;

  private String module;

  private String function;

  private List<String> parameters;

  private Integer line;

  private Integer column;

  private String fullFilename;

  public String getPackageFrame() {
    return packageFrame;
  }

  public void setPackageFrame(final String packageFrame) {
    this.packageFrame = packageFrame;
  }

  public String getModule() {
    return module;
  }

  public void setModule(final String module) {
    this.module = module;
  }

  public String getFunction() {
    return function;
  }

  public void setFunction(final String function) {
    this.function = function;
  }

  @JsonProperty("parameters")
  @JacksonXmlElementWrapper(localName = "parameters")
  @JacksonXmlProperty(localName = "parameter")
  public List<String> getParameters() {
    return parameters;
  }

  public void setParameters(final List<String> parameters) {
    this.parameters = parameters;
  }

  public Integer getLine() {
    return line;
  }

  public void setLine(final Integer line) {
    this.line = line;
  }

  public Integer getColumn() {
    return column;
  }

  public void setColumn(final Integer column) {
    this.column = column;
  }

  public String getFullFilename() {
    return fullFilename;
  }

  public void setFullFilename(final String fullFilename) {
    this.fullFilename = fullFilename;
  }
}
