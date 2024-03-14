package org.cyclonedx.model.component.crypto.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum ProtocolType
{
  @JsonProperty("tls")
  TLS("tls", "Transport Layer Security"),
  @JsonProperty("ssh")
  SSH("ssh", "Secure Shell"),
  @JsonProperty("ipsec")
  IPSEC("ipsec", "Internet Protocol Security"),
  @JsonProperty("ike")
  IKE("ike", "Internet Key Exchange"),
  @JsonProperty("sstp")
  SSTP("sstp", "Secure Socket Tunneling Protocol"),
  @JsonProperty("wpa")
  WPA("wpa", "Wi-Fi Protected Access"),
  @JsonProperty("other")
  OTHER("other", "Another protocol type"),
  @JsonProperty("unknown")
  UNKNOWN("unknown", "The protocol type is not known");

  private final String name;
  private final String description;

  ProtocolType(String name, String description) {
    this.name = name;
    this.description = description;
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }
}
