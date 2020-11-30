package org.cyclonedx.model;

import java.util.List;

public class Extension
{
  private String namespaceUri;
  private String prefix;

  private List<ExtensibleExtension> extensions;

  public Extension(final String namespaceUri, final String prefix, final List<ExtensibleExtension> extensions) {
    this.namespaceUri = namespaceUri;
    this.prefix = prefix;
    this.extensions = extensions;
  }

  public List<ExtensibleExtension> getExtensions() {
    return extensions;
  }

  public void setExtensions(final List<ExtensibleExtension> extensions) {
    this.extensions = extensions;
  }

  public String getNamespaceUri() {
    return namespaceUri;
  }

  public void setNamespaceUri(final String namespaceUri) {
    this.namespaceUri = namespaceUri;
  }

  public String getPrefix() {
    return prefix;
  }

  public void setPrefix(final String prefix) {
    this.prefix = prefix;
  }
}
