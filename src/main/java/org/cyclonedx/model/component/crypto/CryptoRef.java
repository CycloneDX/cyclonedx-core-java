package org.cyclonedx.model.component.crypto;

import java.util.List;
import java.util.Objects;

public class CryptoRef
{
  private List<String> ref;

  public List<String> getRef() {
    return ref;
  }

  public void setRef(final List<String> ref) {
    this.ref = ref;
  }

  @Override
  public boolean equals(final Object object) {
    if (this == object) {
      return true;
    }
    if (!(object instanceof CryptoRef)) {
      return false;
    }
    CryptoRef cryptoRef = (CryptoRef) object;
    return Objects.equals(ref, cryptoRef.ref);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(ref);
  }
}
