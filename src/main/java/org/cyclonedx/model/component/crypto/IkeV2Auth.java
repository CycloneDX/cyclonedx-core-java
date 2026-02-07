package org.cyclonedx.model.component.crypto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.cyclonedx.util.serializer.IkeV2TransformSerializer;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"name", "algorithm"})
@JsonSerialize(using = IkeV2TransformSerializer.class)
public class IkeV2Auth extends AbstractIkeV2Transform {

  public IkeV2Auth() {
  }

  @JsonCreator
  public IkeV2Auth(String algorithm) {
    super(algorithm);
  }

  @Override
  public boolean isStringOnly() {
    return getName() == null && getAlgorithm() != null;
  }
}
