/*
 * This file is part of CycloneDX Core (Java).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (c) OWASP Foundation. All Rights Reserved.
 */
package org.cyclonedx.model;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"altIds", "licensor", "licensee", "purchaser", "purchaseOrder", "licenseTypes", "lastRenewal", "expiration"})
@JsonRootName("licensing")
public class Licensing extends ExtensibleElement
{
  public enum LicensingType {
    @JsonProperty("academic")
    ACADEMIC("academic"),
    @JsonProperty("appliance")
    APPLIANCE("appliance"),
    @JsonProperty("client-access")
    CLIENT_ACCESS("client-access"),
    @JsonProperty("concurrent-user")
    CONCURRENT_USER("concurrent-user"),
    @JsonProperty("core-points")
    CORE_POINTS("core-points"),
    @JsonProperty("custom-metric")
    CUSTOM_METRIC("custom-metric"),
    @JsonProperty("device")
    DEVICE("device"),
    @JsonProperty("evaluation")
    EVALUATION("evaluation"),
    @JsonProperty("named-user")
    NAMED_USER("named-user"),
    @JsonProperty("node-locked")
    NODE_LOCKED("node-locked"),
    @JsonProperty("oem")
    OEM("oem"),
    @JsonProperty("perpetual")
    PERPETUAL("perpetual"),
    @JsonProperty("processor-points")
    PROCESSOR_POINTS("processor-points"),
    @JsonProperty("subscription")
    SUBSCRIPTION("subscription"),
    @JsonProperty("user")
    USER("user"),
    @JsonProperty("other")
    OTHER("other");

    private final String name;

    public String getLicensingType() {
      return this.name;
    }

    LicensingType(String name) {
      this.name = name;
    }
  }

  private List<String> altIds;

  private OrganizationalEntity licensor;

  private OrganizationalEntity licensee;

  private OrganizationalEntity purchaser;

  private String purchaseOrder;

  private List<LicensingType> licenseTypes;

  private Date lastRenewal = new Date();

  private Date expiration = new Date();

  public List<String> getAltIds() {
    return altIds;
  }

  public void setAltIds(final List<String> altIds) {
    this.altIds = altIds;
  }

  public OrganizationalEntity getLicensor() {
    return licensor;
  }

  public void setLicensor(final OrganizationalEntity licensor) {
    this.licensor = licensor;
  }

  public OrganizationalEntity getLicensee() {
    return licensee;
  }

  public void setLicensee(final OrganizationalEntity licensee) {
    this.licensee = licensee;
  }

  public OrganizationalEntity getPurchaser() {
    return purchaser;
  }

  public void setPurchaser(final OrganizationalEntity purchaser) {
    this.purchaser = purchaser;
  }

  public String getPurchaseOrder() {
    return purchaseOrder;
  }

  public void setPurchaseOrder(final String purchaseOrder) {
    this.purchaseOrder = purchaseOrder;
  }

  public List<LicensingType> getLicenseTypes() {
    return licenseTypes;
  }

  public void setLicenseTypes(final List<LicensingType> licenseTypes) {
    this.licenseTypes = licenseTypes;
  }
}
