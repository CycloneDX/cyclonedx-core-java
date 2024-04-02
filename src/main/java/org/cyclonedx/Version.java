package org.cyclonedx;

public enum Version
{
  VERSION_10(CycloneDxSchema.NS_BOM_10, "1.0", 1.0),
  VERSION_11(CycloneDxSchema.NS_BOM_11, "1.1", 1.1),
  VERSION_12(CycloneDxSchema.NS_BOM_12, "1.2", 1.2),
  VERSION_13(CycloneDxSchema.NS_BOM_13, "1.3", 1.3),
  VERSION_14(CycloneDxSchema.NS_BOM_14, "1.4", 1.4),
  VERSION_15(CycloneDxSchema.NS_BOM_15, "1.5", 1.5),
  VERSION_16(CycloneDxSchema.NS_BOM_16, "1.6", 1.6);

  private final String namespace;

  private final String versionString;

  private final double version;

  public String getNamespace() {
    return this.namespace;
  }

  public String getVersionString() {
    return versionString;
  }

  public double getVersion() {
    return version;
  }

  Version(String namespace, String versionString, double version) {
    this.namespace = namespace;
    this.versionString = versionString;
    this.version = version;
  }
}