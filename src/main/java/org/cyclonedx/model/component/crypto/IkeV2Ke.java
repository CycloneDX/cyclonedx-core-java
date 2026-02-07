package org.cyclonedx.model.component.crypto;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.cyclonedx.util.serializer.IkeV2TransformSerializer;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"group", "algorithm"})
@JsonSerialize(using = IkeV2TransformSerializer.class)
public class IkeV2Ke extends AbstractIkeV2Transform {

  private Integer group;

  public IkeV2Ke() {
  }

  @JsonCreator
  public IkeV2Ke(String algorithm) {
    super(algorithm);
  }

  public Integer getGroup() {
    return group;
  }

  public void setGroup(final Integer group) {
    this.group = group;
  }

  @Override
  public boolean isStringOnly() {
    return group == null && getAlgorithm() != null;
  }

  @Override
  public boolean equals(final Object object) {
    if (this == object) {
      return true;
    }
    if (!(object instanceof IkeV2Ke)) {
      return false;
    }
    if (!super.equals(object)) {
      return false;
    }
    IkeV2Ke that = (IkeV2Ke) object;
    return Objects.equals(group, that.group);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), group);
  }
}
