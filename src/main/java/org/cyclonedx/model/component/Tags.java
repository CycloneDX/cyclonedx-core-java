package org.cyclonedx.model.component;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import org.cyclonedx.util.deserializer.TagsDeserializer;

@JsonInclude(Include.NON_EMPTY)
@JsonDeserialize(using = TagsDeserializer.class)
public class Tags
{
  private List<String> tags;

  public Tags() {
  }

  public Tags(List<String> tags){
    this.tags = tags;
  }

  @JacksonXmlElementWrapper(localName = "tags")
  @JacksonXmlProperty(localName = "tag")
  @JsonProperty("tags")
  public List<String> getTags() {
    return tags;
  }

  public void setTags(final List<String> tags) {
    this.tags = tags;
  }

  @Override
  public boolean equals(final Object object) {
    if (this == object) {
      return true;
    }
    if (!(object instanceof Tags)) {
      return false;
    }
    Tags tags1 = (Tags) object;
    return Objects.equals(tags, tags1.tags);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(tags);
  }
}
