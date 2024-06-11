package org.cyclonedx.model.attestation.evidence;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.cyclonedx.model.AttachmentText;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder({
    "attachment",
    "url",
})
public class Contents
{
  private AttachmentText attachment;

  private String url;

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

  @Override
  public boolean equals(final Object object) {
    if (this == object) {
      return true;
    }
    if (!(object instanceof Contents)) {
      return false;
    }
    Contents contents = (Contents) object;
    return Objects.equals(attachment, contents.attachment) && Objects.equals(url, contents.url);
  }

  @Override
  public int hashCode() {
    return Objects.hash(attachment, url);
  }
}
