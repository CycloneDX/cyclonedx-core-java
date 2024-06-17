package org.cyclonedx.model.component.crypto;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder({"name", "algorithms", "identifiers"})
public class CipherSuite
{

  private String name;

  private List<String> algorithms;

  private List<String> identifiers;

  public String getName() {
    return name;
  }

  public void setName(final String name) {
    this.name = name;
  }

  @JacksonXmlElementWrapper(localName = "algorithms")
  @JacksonXmlProperty(localName = "algorithm")
  public List<String> getAlgorithms() {
    return algorithms;
  }

  public void setAlgorithms(final List<String> algorithms) {
    this.algorithms = algorithms;
  }

  @JacksonXmlElementWrapper(localName = "identifiers")
  @JacksonXmlProperty(localName = "identifier")
  public List<String> getIdentifiers() {
    return identifiers;
  }

  public void setIdentifiers(final List<String> identifiers) {
    this.identifiers = identifiers;
  }

  @Override
  public boolean equals(final Object object) {
    if (this == object) {
      return true;
    }
    if (!(object instanceof CipherSuite)) {
      return false;
    }
    CipherSuite that = (CipherSuite) object;
    return Objects.equals(name, that.name) && Objects.equals(algorithms, that.algorithms) &&
        Objects.equals(identifiers, that.identifiers);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, algorithms, identifiers);
  }
}
