package org.cyclonedx.model.component;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.cyclonedx.Version;
import org.cyclonedx.model.VersionFilter;

public abstract class Component12Mixin
{
  @JsonProperty("author")
  @VersionFilter(Version.VERSION_12)
  abstract String getAuthor();
}
