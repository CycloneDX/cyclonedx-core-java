package org.cyclonedx.model.formulation.trigger;

import java.util.List;

import org.cyclonedx.model.AttachmentText;
import org.cyclonedx.model.Property;
import org.cyclonedx.model.formulation.common.ResourceReferenceChoice;

public class Event {
  private String uid;
  private String description;
  private String timeReceived;
  private AttachmentText data;
  private ResourceReferenceChoice source;
  private ResourceReferenceChoice target;
  private List<Property> properties;

  public String getUid() {
    return uid;
  }

  public void setUid(final String uid) {
    this.uid = uid;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(final String description) {
    this.description = description;
  }

  public String getTimeReceived() {
    return timeReceived;
  }

  public void setTimeReceived(final String timeReceived) {
    this.timeReceived = timeReceived;
  }

  public AttachmentText getData() {
    return data;
  }

  public void setData(final AttachmentText data) {
    this.data = data;
  }

  public ResourceReferenceChoice getSource() {
    return source;
  }

  public void setSource(final ResourceReferenceChoice source) {
    this.source = source;
  }

  public ResourceReferenceChoice getTarget() {
    return target;
  }

  public void setTarget(final ResourceReferenceChoice target) {
    this.target = target;
  }

  public List<Property> getProperties() {
    return properties;
  }

  public void setProperties(final List<Property> properties) {
    this.properties = properties;
  }
}