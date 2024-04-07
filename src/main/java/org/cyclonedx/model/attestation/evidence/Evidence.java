package org.cyclonedx.model.attestation.evidence;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import org.cyclonedx.model.OrganizationalContact;
import org.cyclonedx.model.Signature;
import org.cyclonedx.util.serializer.CustomDateSerializer;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder({
    "type",
    "description",
    "data",
    "created",
    "expires",
    "author",
    "reviewer",
    "signature"
})
public class Evidence
{
  @JacksonXmlProperty(isAttribute = true, localName = "bom-ref")
  @JsonProperty("bom-ref")
  private String bomRef;

  private String type;

  private String description;

  private List<Data> data;

  @JsonSerialize(using = CustomDateSerializer.class)
  private Date created;

  @JsonSerialize(using = CustomDateSerializer.class)
  private Date expires;

  private OrganizationalContact author;

  private OrganizationalContact reviewer;

  private Signature signature;

  public String getBomRef() {
    return bomRef;
  }

  public void setBomRef(final String bomRef) {
    this.bomRef = bomRef;
  }

  public String getType() {
    return type;
  }

  public void setType(final String type) {
    this.type = type;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(final String description) {
    this.description = description;
  }

  @JacksonXmlElementWrapper(useWrapping = false)
  @JacksonXmlProperty(localName = "data")
  //@JsonDeserialize(using = DataDeserializer.class)
  @JsonProperty("data")
  public List<Data> getData() {
    return data;
  }

  public void setData(final List<Data> data) {
    this.data = data;
  }

  public Date getCreated() {
    return created;
  }

  public void setCreated(final Date created) {
    this.created = created;
  }

  public Date getExpires() {
    return expires;
  }

  public void setExpires(final Date expires) {
    this.expires = expires;
  }

  public OrganizationalContact getAuthor() {
    return author;
  }

  public void setAuthor(final OrganizationalContact author) {
    this.author = author;
  }

  public OrganizationalContact getReviewer() {
    return reviewer;
  }

  public void setReviewer(final OrganizationalContact reviewer) {
    this.reviewer = reviewer;
  }

  public Signature getSignature() {
    return signature;
  }

  public void setSignature(final Signature signature) {
    this.signature = signature;
  }
}
