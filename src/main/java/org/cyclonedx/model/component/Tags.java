package org.cyclonedx.model.component;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import org.cyclonedx.util.deserializer.TagsDeserializer;

@JsonInclude(JsonInclude.Include.NON_NULL)
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
}
