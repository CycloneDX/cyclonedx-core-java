package org.cyclonedx.model.definition;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import org.cyclonedx.Version;
import org.cyclonedx.model.Patent;
import org.cyclonedx.model.PatentFamily;
import org.cyclonedx.model.VersionFilter;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder({
    "standards", "patents", "patentFamilies"
})
public class Definition
{
  private List<Standard> standards;

  @VersionFilter(Version.VERSION_17)
  private List<Patent> patents;

  @VersionFilter(Version.VERSION_17)
  private List<PatentFamily> patentFamilies;

  @JacksonXmlElementWrapper(localName = "standards")
  @JacksonXmlProperty(localName = "standard")
  public List<Standard> getStandards() {
    return standards;
  }

  public void setStandards(final List<Standard> standards) {
    this.standards = standards;
  }

  @JacksonXmlElementWrapper(localName = "patents")
  @JacksonXmlProperty(localName = "patent")
  @VersionFilter(Version.VERSION_17)
  public List<Patent> getPatents() {
    return patents;
  }

  public void setPatents(final List<Patent> patents) {
    this.patents = patents;
  }

  @JacksonXmlElementWrapper(useWrapping = false)
  @JacksonXmlProperty(localName = "patentFamily")
  @VersionFilter(Version.VERSION_17)
  public List<PatentFamily> getPatentFamilies() {
    return patentFamilies;
  }

  public void setPatentFamilies(final List<PatentFamily> patentFamilies) {
    this.patentFamilies = patentFamilies;
  }

  @Override
  public boolean equals(final Object object) {
    if (this == object) {
      return true;
    }
    if (!(object instanceof Definition)) {
      return false;
    }
    Definition that = (Definition) object;
    return Objects.equals(standards, that.standards) &&
        Objects.equals(patents, that.patents) &&
        Objects.equals(patentFamilies, that.patentFamilies);
  }

  @Override
  public int hashCode() {
    return Objects.hash(standards, patents, patentFamilies);
  }
}
