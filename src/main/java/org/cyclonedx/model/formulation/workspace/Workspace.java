package org.cyclonedx.model.formulation.workspace;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import org.cyclonedx.model.ExtensibleElement;
import org.cyclonedx.model.Property;
import org.cyclonedx.model.formulation.common.ResourceReferenceChoice;

public class Workspace extends ExtensibleElement
{

  @JacksonXmlProperty(isAttribute = true, localName = "bom-ref")
  @JsonProperty("bom-ref")
  private String bomRef;

  private String uid;

  private String name;

  private List<String> aliases;

  private String description;

  private List<ResourceReferenceChoice> resourceReferences;

  private AccessMode accessMode;

  private String mountPath;

  private String managedDataType;

  private String volumeRequest;

  private Volume volume;

  private List<Property> properties;

  public enum AccessMode {

    @JsonProperty("read-only")
    READ_ONLY("read-only"),
    @JsonProperty("read-write")
    READ_WRITE("read-write"),
    @JsonProperty("read-write-once")
    READ_WRITE_ONCE("read-write-once"),
    @JsonProperty("write-once")
    WRITE_ONCE("write-once"),
    @JsonProperty("write-only")
    WRITE_ONLY("write-only");

    private final String name;

    public String getAccessMode() {
      return this.name;
    }

    AccessMode(String name) {
      this.name = name;
    }
  }

  public String getBomRef() {
    return bomRef;
  }

  public void setBomRef(final String bomRef) {
    this.bomRef = bomRef;
  }

  public String getUid() {
    return uid;
  }

  public void setUid(final String uid) {
    this.uid = uid;
  }

  public String getName() {
    return name;
  }

  public void setName(final String name) {
    this.name = name;
  }

  public List<String> getAliases() {
    return aliases;
  }

  public void setAliases(final List<String> aliases) {
    this.aliases = aliases;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(final String description) {
    this.description = description;
  }

  public List<ResourceReferenceChoice> getResourceReferences() {
    return resourceReferences;
  }

  public void setResourceReferences(final List<ResourceReferenceChoice> resourceReferences) {
    this.resourceReferences = resourceReferences;
  }

  public AccessMode getAccessMode() {
    return accessMode;
  }

  public void setAccessMode(final AccessMode accessMode) {
    this.accessMode = accessMode;
  }

  public String getMountPath() {
    return mountPath;
  }

  public void setMountPath(final String mountPath) {
    this.mountPath = mountPath;
  }

  public String getManagedDataType() {
    return managedDataType;
  }

  public void setManagedDataType(final String managedDataType) {
    this.managedDataType = managedDataType;
  }

  public String getVolumeRequest() {
    return volumeRequest;
  }

  public void setVolumeRequest(final String volumeRequest) {
    this.volumeRequest = volumeRequest;
  }

  public Volume getVolume() {
    return volume;
  }

  public void setVolume(final Volume volume) {
    this.volume = volume;
  }

  public List<Property> getProperties() {
    return properties;
  }

  public void setProperties(final List<Property> properties) {
    this.properties = properties;
  }
}
