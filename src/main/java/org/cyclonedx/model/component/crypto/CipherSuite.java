package org.cyclonedx.model.component.crypto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

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

  public List<String> getAlgorithms() {
    return algorithms;
  }

  public void setAlgorithms(final List<String> algorithms) {
    this.algorithms = algorithms;
  }

  public List<String> getIdentifiers() {
    return identifiers;
  }

  public void setIdentifiers(final List<String> identifiers) {
    this.identifiers = identifiers;
  }
}
