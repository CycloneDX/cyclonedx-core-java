package org.cyclonedx.model;

import java.util.ArrayList;
import java.util.List;

public class Extension
{
  public enum ExtensionType {
    VULNERABILITIES("vulnerabilities");

    private final String type;

    public String getTypeName() {
      return this.type;
    }

    ExtensionType(String type) {
      this.type = type;
    }
  }

  private String namespaceURI;
  private String prefix;

  private List<ExtensibleType> extensions;

  public Extension() {
  }

  public Extension(final ExtensionType type, final List<ExtensibleType> extensions) {
    this.extensions = extensions;
  }

  public List<ExtensibleType> getExtensions() {
    return extensions;
  }

  public void setExtensions(final List<ExtensibleType> extensions) {
    this.extensions = extensions;
  }

  public void addExtension(ExtensibleType extensibleType) {
    if (this.extensions == null) {
      this.extensions = new ArrayList<>();
    }
    this.extensions.add(extensibleType);
  }

  public String getPrefix() {
    return prefix;
  }

  public String getNamespaceURI() {
    return namespaceURI;
  }
}
