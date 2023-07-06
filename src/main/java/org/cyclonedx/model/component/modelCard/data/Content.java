package org.cyclonedx.model.component.modelCard.data;

import java.util.List;
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
}
