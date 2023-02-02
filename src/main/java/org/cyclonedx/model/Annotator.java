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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"organization", "individual", "component", "service"})
public class Annotator extends ExtensibleElement
{
  private OrganizationalEntity organization;

  private OrganizationalContact individual;

  private Component component;

  private Service service;

  public OrganizationalEntity getOrganization() {
    return organization;
  }

  public void setOrganization(final OrganizationalEntity organization) {
    this.organization = organization;
  }

  public OrganizationalContact getIndividual() {
    return individual;
  }

  public void setIndividual(final OrganizationalContact individual) {
    this.individual = individual;
  }

  public Component getComponent() {
    return component;
  }

  public void setComponent(final Component component) {
    this.component = component;
  }

  public Service getService() {
    return service;
  }

  public void setService(final Service service) {
    this.service = service;
  }
}
