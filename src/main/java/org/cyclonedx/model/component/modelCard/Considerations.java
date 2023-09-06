package org.cyclonedx.model.component.modelCard;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import org.cyclonedx.model.ExtensibleElement;
import org.cyclonedx.model.component.modelCard.consideration.FairnessAssessment;
import org.cyclonedx.model.component.modelCard.consideration.Risk;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Considerations extends ExtensibleElement
{
  private List<String> users;

  private List<String> useCases;

  private List<String> technicalLimitations;

  private List<String> performanceTradeoffs;

  private List<Risk> ethicalConsiderations;


  private List<FairnessAssessment> fairnessAssessments;

  @JacksonXmlElementWrapper(localName = "users")
  @JacksonXmlProperty(localName = "user")
  public List<String> getUsers() {
    return users;
  }

  public void setUsers(final List<String> users) {
    this.users = users;
  }

  @JacksonXmlElementWrapper(localName = "useCases")
  @JacksonXmlProperty(localName = "useCase")
  public List<String> getUseCases() {
    return useCases;
  }

  public void setUseCases(final List<String> useCases) {
    this.useCases = useCases;
  }

  @JacksonXmlElementWrapper(localName = "technicalLimitations")
  @JacksonXmlProperty(localName = "technicalLimitation")
  public List<String> getTechnicalLimitations() {
    return technicalLimitations;
  }

  public void setTechnicalLimitations(final List<String> technicalLimitations) {
    this.technicalLimitations = technicalLimitations;
  }

  @JacksonXmlElementWrapper(localName = "performanceTradeoffs")
  @JacksonXmlProperty(localName = "performanceTradeoff")
  public List<String> getPerformanceTradeoffs() {
    return performanceTradeoffs;
  }

  public void setPerformanceTradeoffs(final List<String> performanceTradeoffs) {
    this.performanceTradeoffs = performanceTradeoffs;
  }

  @JacksonXmlElementWrapper(localName = "ethicalConsiderations")
  @JacksonXmlProperty(localName = "ethicalConsideration")
  public List<Risk> getEthicalConsiderations() {
    return ethicalConsiderations;
  }

  public void setEthicalConsiderations(final List<Risk> ethicalConsiderations) {
    this.ethicalConsiderations = ethicalConsiderations;
  }

  @JacksonXmlElementWrapper(localName = "fairnessAssessments")
  @JacksonXmlProperty(localName = "fairnessAssessment")
  public List<FairnessAssessment> getFairnessAssessments() {
    return fairnessAssessments;
  }

  public void setFairnessAssessments(final List<FairnessAssessment> fairnessAssessments) {
    this.fairnessAssessments = fairnessAssessments;
  }
}
