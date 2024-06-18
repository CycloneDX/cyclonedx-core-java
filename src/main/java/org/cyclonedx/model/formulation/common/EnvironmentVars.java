package org.cyclonedx.model.formulation.common;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.cyclonedx.model.Property;
import org.cyclonedx.util.deserializer.EnvironmentVarsDeserializer;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "environmentVars", propOrder = {
    "choices"
})
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonDeserialize(using = EnvironmentVarsDeserializer.class)
public class EnvironmentVars
{
  @XmlElements({
      @XmlElement(name = "environmentVar", type = Property.class),
      @XmlElement(name = "value", type = String.class)
  })
  private List<Object> choices;

  public List<Object> getChoices() {
    return choices;
  }

  public void setChoices(List<Object> choices) {
    this.choices = choices;
  }
}