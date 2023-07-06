package org.cyclonedx.model.component.modelCard;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.cyclonedx.util.deserializer.DatasetsChoiceDeserializer;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonDeserialize(using = DatasetsChoiceDeserializer.class)
public class DatasetChoice
{
  private String ref;
  @JsonUnwrapped
  private ComponentData componentData;

  public String getRef() {
    return ref;
  }

  public void setRef(final String ref) {
    this.ref = ref;
  }

  public ComponentData getComponentData() {
    return componentData;
  }

  public void setComponentData(final ComponentData componentData) {
    this.componentData = componentData;
  }
}
