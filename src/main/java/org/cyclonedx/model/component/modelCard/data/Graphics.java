package org.cyclonedx.model.component.modelCard.data;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import org.cyclonedx.model.AttachmentText;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Graphics
{
  private String description;

  private List<Graphic> collection;

  public static class Graphic {
    private String name;

    private AttachmentText image;

    public String getName() {
      return name;
    }

    public void setName(final String name) {
      this.name = name;
    }

    public AttachmentText getImage() {
      return image;
    }

    public void setImage(final AttachmentText image) {
      this.image = image;
    }
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(final String description) {
    this.description = description;
  }

  @JacksonXmlElementWrapper(localName = "collection")
  @JacksonXmlProperty(localName = "graphic")
  public List<Graphic> getCollection() {
    return collection;
  }

  public void setCollection(final List<Graphic> collection) {
    this.collection = collection;
  }
}
