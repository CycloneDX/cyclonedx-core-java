package org.cyclonedx.model.component;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import org.cyclonedx.Version;
import org.cyclonedx.model.OrganizationalContact;
import org.cyclonedx.model.VersionFilter;

public abstract class ComponentMixin
{
  // Handle 'author' for version 12 (deprecated but still valid)
  @JsonProperty("author")
  @JacksonXmlProperty(localName = "author")
  @VersionFilter(Version.VERSION_12)
  abstract String getAuthor();

  // Handle 'authors' for version 16 (List)
  @JsonProperty("authors")
  @JacksonXmlElementWrapper(localName = "authors")
  @JacksonXmlProperty(localName = "author")
  @VersionFilter(Version.VERSION_16)
  abstract List<OrganizationalContact> getAuthorsList();
}
