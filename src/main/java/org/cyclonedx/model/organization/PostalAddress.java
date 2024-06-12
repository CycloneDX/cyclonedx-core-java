package org.cyclonedx.model.organization;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder({"country", "region", "locality", "postOfficeBoxNumber", "postalCode", "streetAddress"})
public class PostalAddress
{
  @JacksonXmlProperty(isAttribute = true, localName = "bom-ref")
  @JsonProperty("bom-ref")
  private String bomRef;

  private String country;

  private String region;

  private String locality;

  private String postOfficeBoxNumber;

  private String postalCode;

  private String streetAddress;

  public String getBomRef() {
    return bomRef;
  }

  public void setBomRef(final String bomRef) {
    this.bomRef = bomRef;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(final String country) {
    this.country = country;
  }

  public String getRegion() {
    return region;
  }

  public void setRegion(final String region) {
    this.region = region;
  }

  public String getLocality() {
    return locality;
  }

  public void setLocality(final String locality) {
    this.locality = locality;
  }

  public String getPostOfficeBoxNumber() {
    return postOfficeBoxNumber;
  }

  public void setPostOfficeBoxNumber(final String postOfficeBoxNumber) {
    this.postOfficeBoxNumber = postOfficeBoxNumber;
  }

  public String getPostalCode() {
    return postalCode;
  }

  public void setPostalCode(final String postalCode) {
    this.postalCode = postalCode;
  }

  public String getStreetAddress() {
    return streetAddress;
  }

  public void setStreetAddress(final String streetAddress) {
    this.streetAddress = streetAddress;
  }

  @Override
  public boolean equals(final Object object) {
    if (this == object) {
      return true;
    }
    if (!(object instanceof PostalAddress)) {
      return false;
    }
    PostalAddress that = (PostalAddress) object;
    return Objects.equals(bomRef, that.bomRef) && Objects.equals(country, that.country) &&
        Objects.equals(region, that.region) && Objects.equals(locality, that.locality) &&
        Objects.equals(postOfficeBoxNumber, that.postOfficeBoxNumber) &&
        Objects.equals(postalCode, that.postalCode) &&
        Objects.equals(streetAddress, that.streetAddress);
  }

  @Override
  public int hashCode() {
    return Objects.hash(bomRef, country, region, locality, postOfficeBoxNumber, postalCode, streetAddress);
  }
}
