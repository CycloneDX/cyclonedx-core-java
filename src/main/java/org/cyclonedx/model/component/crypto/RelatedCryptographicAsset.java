package org.cyclonedx.model.component.crypto;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder({"type", "ref"})
public class RelatedCryptographicAsset
{
  private String type;

  @JsonProperty("ref")
  private String ref;

  public String getType() {
    return type;
  }

  public void setType(final String type) {
    this.type = type;
  }

  public String getRef() {
    return ref;
  }

  public void setRef(final String ref) {
    this.ref = ref;
  }

  @Override
  public boolean equals(final Object object) {
    if (this == object) return true;
    if (!(object instanceof RelatedCryptographicAsset)) return false;
    RelatedCryptographicAsset that = (RelatedCryptographicAsset) object;
    return Objects.equals(type, that.type) && Objects.equals(ref, that.ref);
  }

  @Override
  public int hashCode() {
    return Objects.hash(type, ref);
  }
}