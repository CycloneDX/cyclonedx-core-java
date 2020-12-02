package org.cyclonedx.model;

import java.util.List;

import org.cyclonedx.generators.xml.SbomNamespaceMapper;

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


  private String namespaceUri;
  private String prefix;

  private List<ExtensibleType> extensions;

  public Extension(final ExtensionType extensionType, final List<ExtensibleType> extensions) {
    if(extensionType == ExtensionType.VULNERABILITIES){
      this.namespaceUri = SbomNamespaceMapper.VULN_URI;
      this.prefix = SbomNamespaceMapper.VULN_PREFIX;
    }

    this.extensions = extensions;
  }

  public List<ExtensibleType> getExtensions() {
    return extensions;
  }

  public void setExtensions(final List<ExtensibleType> extensions) {
    this.extensions = extensions;
  }

  public String getNamespaceUri() {
    return namespaceUri;
  }

  public String getPrefix() {
    return prefix;
  }

}
