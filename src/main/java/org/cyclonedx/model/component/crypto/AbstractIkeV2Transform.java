package org.cyclonedx.model.component.crypto;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Abstract base class for IKEv2 transform types.
 * All IKEv2 transforms share algorithm and name fields.
 * Subclasses add type-specific fields (keyLength, group, etc.).
 *
 * @since 10.0.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class AbstractIkeV2Transform {

  private String name;
  private String algorithm;

  protected AbstractIkeV2Transform() {
  }

  protected AbstractIkeV2Transform(String algorithm) {
    this.algorithm = algorithm;
  }

  public String getName() {
    return name;
  }

  public void setName(final String name) {
    this.name = name;
  }

  public String getAlgorithm() {
    return algorithm;
  }

  public void setAlgorithm(final String algorithm) {
    this.algorithm = algorithm;
  }

  /**
   * Returns true if this transform was constructed from a plain string value
   * (1.6 backward compatibility format) and should be serialized as a string.
   */
  public abstract boolean isStringOnly();

  @Override
  public boolean equals(final Object object) {
    if (this == object) {
      return true;
    }
    if (!(object instanceof AbstractIkeV2Transform)) {
      return false;
    }
    AbstractIkeV2Transform that = (AbstractIkeV2Transform) object;
    return Objects.equals(name, that.name) && Objects.equals(algorithm, that.algorithm);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, algorithm);
  }
}
