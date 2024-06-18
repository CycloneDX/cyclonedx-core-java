package org.cyclonedx.model.formulation.trigger;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import org.cyclonedx.model.AttachmentText;
import org.cyclonedx.model.Property;
import org.cyclonedx.model.formulation.common.ResourceReferenceChoice;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_EMPTY)
@JsonPropertyOrder({"uid", "description", "timeReceived", "data", "source", "target", "properties"})
@JsonRootName("event")
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

  @JacksonXmlProperty(localName = "data")
  @JsonProperty("data")
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

  @JacksonXmlElementWrapper(localName = "properties")
  @JacksonXmlProperty(localName = "property")
  public List<Property> getProperties() {
    return properties;
  }

  public void setProperties(final List<Property> properties) {
    this.properties = properties;
  }

  @Override
  public boolean equals(final Object object) {
    if (this == object) {
      return true;
    }
    if (!(object instanceof Event)) {
      return false;
    }
    Event event = (Event) object;
    return Objects.equals(uid, event.uid) && Objects.equals(description, event.description) &&
        Objects.equals(timeReceived, event.timeReceived) && Objects.equals(data, event.data) &&
        Objects.equals(source, event.source) && Objects.equals(target, event.target) &&
        Objects.equals(properties, event.properties);
  }

  @Override
  public int hashCode() {
    return Objects.hash(uid, description, timeReceived, data, source, target, properties);
  }
}