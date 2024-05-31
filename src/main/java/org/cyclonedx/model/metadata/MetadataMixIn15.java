package org.cyclonedx.model.metadata;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import org.cyclonedx.Version;
import org.cyclonedx.model.ToolInformation;
import org.cyclonedx.model.VersionFilter;

public abstract class MetadataMixIn15
{
    @VersionFilter(Version.VERSION_15)
    @JsonProperty("tools")
    @JacksonXmlElementWrapper(localName = "tools")
    @JacksonXmlProperty(localName = "tool")
    abstract ToolInformation getTools();
}
