package org.cyclonedx.model.metadata;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import org.cyclonedx.Version;
import org.cyclonedx.model.Tool;
import org.cyclonedx.model.VersionFilter;

public abstract class MetadataMixIn12
{
  @VersionFilter(Version.VERSION_12)
  @JsonProperty("tools")
  @JacksonXmlElementWrapper(localName = "tools")
  @JacksonXmlProperty(localName = "tool")
  abstract List<Tool> getDeprecatedTools();
}
