package org.cyclonedx.model.component.evidence;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Callstack
{
  private List<Frame> frames;

  @JacksonXmlElementWrapper(localName = "frames")
  @JacksonXmlProperty(localName = "frame")
  @JsonProperty("frames")
  public List<Frame> getFrames() {
    return frames;
  }

  public void setFrames(final List<Frame> frames) {
    this.frames = frames;
  }

  @Override
  public boolean equals(final Object object) {
    if (this == object) {
      return true;
    }
    if (!(object instanceof Callstack)) {
      return false;
    }
    Callstack callstack = (Callstack) object;
    return Objects.equals(frames, callstack.frames);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(frames);
  }
}