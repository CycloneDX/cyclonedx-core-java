package org.cyclonedx.model.formulation.workspace;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.cyclonedx.model.ExtensibleElement;
import org.cyclonedx.model.Property;

public class Volume extends ExtensibleElement
{
  private String uid;

  private String name;

  private Mode mode = Mode.FILESYSTEM;

  private String path;

  private String sizeAllocated;

  private Boolean persistent;

  private Boolean remote;

  private List<Property> properties;

  public enum Mode {

    @JsonProperty("filesystem")
    FILESYSTEM("filesystem"),
    @JsonProperty("block")
    BLOCK("block");

    private final String name;

    public String getMode() {
      return this.name;
    }

    Mode(String name) {
      this.name = name;
    }
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

  public Mode getMode() {
    return mode;
  }

  public void setMode(final Mode mode) {
    this.mode = mode;
  }

  public String getPath() {
    return path;
  }

  public void setPath(final String path) {
    this.path = path;
  }

  public String getSizeAllocated() {
    return sizeAllocated;
  }

  public void setSizeAllocated(final String sizeAllocated) {
    this.sizeAllocated = sizeAllocated;
  }

  public Boolean getPersistent() {
    return persistent;
  }

  public void setPersistent(final Boolean persistent) {
    this.persistent = persistent;
  }

  public Boolean getRemote() {
    return remote;
  }

  public void setRemote(final Boolean remote) {
    this.remote = remote;
  }

  public List<Property> getProperties() {
    return properties;
  }

  public void setProperties(final List<Property> properties) {
    this.properties = properties;
  }
}
