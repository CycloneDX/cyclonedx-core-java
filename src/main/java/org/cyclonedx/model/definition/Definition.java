package org.cyclonedx.model.definition;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import org.cyclonedx.Version;
import org.cyclonedx.model.Patent;
import org.cyclonedx.model.PatentFamily;
import org.cyclonedx.model.PatentItem;
import org.cyclonedx.model.VersionFilter;
import org.cyclonedx.util.deserializer.PatentsDeserializer;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder({
    "standards", "patents"
})
public class Definition
{
  private List<Standard> standards;

  @VersionFilter(Version.VERSION_17)
  @JsonDeserialize(using = PatentsDeserializer.class)
  private List<PatentItem> patents;

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
  public List<PatentItem> getPatents() {
    return patents;
  }

  public void setPatents(final List<PatentItem> patents) {
    this.patents = patents;
  }

  /**
   * @deprecated Use {@link #getPatents()} and filter by type instead.
   */
  @Deprecated
  @JsonIgnore
  public List<Patent> getPatentList() {
    if (patents == null) return null;
    List<Patent> result = patents.stream()
        .filter(item -> item.getPatent() != null)
        .map(PatentItem::getPatent)
        .collect(Collectors.toList());
    return result.isEmpty() ? null : result;
  }

  /**
   * @deprecated Use {@link #getPatents()} and filter by type instead.
   */
  @Deprecated
  @JsonIgnore
  public List<PatentFamily> getPatentFamilyList() {
    if (patents == null) return null;
    List<PatentFamily> result = patents.stream()
        .filter(item -> item.getPatentFamily() != null)
        .map(PatentItem::getPatentFamily)
        .collect(Collectors.toList());
    return result.isEmpty() ? null : result;
  }

  public void addPatent(Patent patent) {
    if (this.patents == null) {
      this.patents = new ArrayList<>();
    }
    this.patents.add(PatentItem.ofPatent(patent));
  }

  public void addPatentFamily(PatentFamily patentFamily) {
    if (this.patents == null) {
      this.patents = new ArrayList<>();
    }
    this.patents.add(PatentItem.ofPatentFamily(patentFamily));
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
        Objects.equals(patents, that.patents);
  }

  @Override
  public int hashCode() {
    return Objects.hash(standards, patents);
  }
}
