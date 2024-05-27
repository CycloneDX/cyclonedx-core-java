package org.cyclonedx.model.component;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import org.cyclonedx.Version;
import org.cyclonedx.model.OrganizationalContact;
import org.cyclonedx.model.VersionFilter;

public abstract class Component16Mixin
{
  @JsonProperty("authors")
  @JacksonXmlElementWrapper(localName = "authors")
  @JacksonXmlProperty(localName = "author")
  @VersionFilter(Version.VERSION_16)
  abstract List<OrganizationalContact> getAuthors();
}
