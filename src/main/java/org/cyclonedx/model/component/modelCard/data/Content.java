package org.cyclonedx.model.component.modelCard.data;

import java.util.List;
import java.util.Objects;
import java.util.Properties;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import org.cyclonedx.model.AttachmentText;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Content
{
  private AttachmentText attachment;

  private String url;

  private List<Properties> properties;

  public AttachmentText getAttachment() {
    return attachment;
  }

  public void setAttachment(final AttachmentText attachment) {
    this.attachment = attachment;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(final String url) {
    this.url = url;
  }

  @JacksonXmlElementWrapper(localName = "properties")
  @JacksonXmlProperty(localName = "property")
  public List<Properties> getProperties() {
    return properties;
  }

  public void setProperties(final List<Properties> properties) {
    this.properties = properties;
  }

  @Override
  public boolean equals(final Object object) {
    if (this == object) {
      return true;
    }
    if (!(object instanceof Content)) {
      return false;
    }
    Content content = (Content) object;
    return Objects.equals(attachment, content.attachment) && Objects.equals(url, content.url) &&
        Objects.equals(properties, content.properties);
  }

  @Override
  public int hashCode() {
    return Objects.hash(attachment, url, properties);
  }
}
