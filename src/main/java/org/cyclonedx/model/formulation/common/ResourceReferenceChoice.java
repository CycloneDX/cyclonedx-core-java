package org.cyclonedx.model.formulation.common;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.cyclonedx.model.ExternalReference;
import org.cyclonedx.util.ResourceReferenceChoiceDeserializer;

@JsonDeserialize(using = ResourceReferenceChoiceDeserializer.class)
public class ResourceReferenceChoice {
  private String ref;
  private ExternalReference externalReference;

  public void setRef(final String ref) {
  }

  public void setExternalReference(final ExternalReference externalReference) {
  }

  public String getRef() {
    return ref;
  }

  public ExternalReference getExternalReference() {
    return externalReference;
  }
}