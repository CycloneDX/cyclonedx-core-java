package org.cyclonedx.model.component.modelCard.consideration;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.cyclonedx.model.ExtensibleElement;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class FairnessAssessment extends ExtensibleElement
{
  private String groupAtRisk;

  private String benefits;

  private String harms;

  private String mitigationStrategy;

  public String getGroupAtRisk() {
    return groupAtRisk;
  }

  public void setGroupAtRisk(final String groupAtRisk) {
    this.groupAtRisk = groupAtRisk;
  }

  public String getBenefits() {
    return benefits;
  }

  public void setBenefits(final String benefits) {
    this.benefits = benefits;
  }

  public String getHarms() {
    return harms;
  }

  public void setHarms(final String harms) {
    this.harms = harms;
  }

  public String getMitigationStrategy() {
    return mitigationStrategy;
  }

  public void setMitigationStrategy(final String mitigationStrategy) {
    this.mitigationStrategy = mitigationStrategy;
  }
}
