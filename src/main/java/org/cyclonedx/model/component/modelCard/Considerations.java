package org.cyclonedx.model.component.modelCard;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import org.cyclonedx.Version;
import org.cyclonedx.model.ExtensibleElement;
import org.cyclonedx.model.VersionFilter;
import org.cyclonedx.model.component.modelCard.consideration.EnvironmentalConsideration;
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

  @VersionFilter(Version.VERSION_16)
  private EnvironmentalConsideration environmentalConsiderations;

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

  public EnvironmentalConsideration getEnvironmentalConsiderations() {
    return environmentalConsiderations;
  }

  public void setEnvironmentalConsiderations(final EnvironmentalConsideration environmentalConsiderations) {
    this.environmentalConsiderations = environmentalConsiderations;
  }

  @Override
  public boolean equals(final Object object) {
    if (this == object) {
      return true;
    }
    if (!(object instanceof Considerations)) {
      return false;
    }
    Considerations that = (Considerations) object;
    return Objects.equals(users, that.users) && Objects.equals(useCases, that.useCases) &&
        Objects.equals(technicalLimitations, that.technicalLimitations) &&
        Objects.equals(performanceTradeoffs, that.performanceTradeoffs) &&
        Objects.equals(ethicalConsiderations, that.ethicalConsiderations) &&
        Objects.equals(environmentalConsiderations, that.environmentalConsiderations) &&
        Objects.equals(fairnessAssessments, that.fairnessAssessments);
  }

  @Override
  public int hashCode() {
    return Objects.hash(users, useCases, technicalLimitations, performanceTradeoffs, ethicalConsiderations,
        environmentalConsiderations, fairnessAssessments);
  }
}
