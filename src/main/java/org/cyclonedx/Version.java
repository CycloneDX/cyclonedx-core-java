package org.cyclonedx;

import java.util.EnumSet;

import static org.cyclonedx.Format.*;

public enum Version
{
  VERSION_10(CycloneDxSchema.NS_BOM_10, "1.0", 1.0, EnumSet.of(XML)),
  VERSION_11(CycloneDxSchema.NS_BOM_11, "1.1", 1.1, EnumSet.of(XML)),
  VERSION_12(CycloneDxSchema.NS_BOM_12, "1.2", 1.2, EnumSet.of(XML, JSON)),
  VERSION_13(CycloneDxSchema.NS_BOM_13, "1.3", 1.3, EnumSet.of(XML, JSON)),
  VERSION_14(CycloneDxSchema.NS_BOM_14, "1.4", 1.4, EnumSet.of(XML, JSON)),
  VERSION_15(CycloneDxSchema.NS_BOM_15, "1.5", 1.5, EnumSet.of(XML, JSON)),
  VERSION_16(CycloneDxSchema.NS_BOM_16, "1.6", 1.6, EnumSet.of(XML, JSON));

  private final String namespace;

  private final String versionString;

  private final double version;

  private final EnumSet<Format> formats;

  public String getNamespace() {
    return this.namespace;
  }

  public String getVersionString() {
    return versionString;
  }

  public double getVersion() {
    return version;
  }

  public EnumSet<Format> getFormats() {
    return formats;
  }

  Version(String namespace, String versionString, double version, EnumSet<Format> formats) {
    this.namespace = namespace;
    this.versionString = versionString;
    this.version = version;
    this.formats = formats;
  }
}
