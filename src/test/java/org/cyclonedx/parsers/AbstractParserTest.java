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
package org.cyclonedx.parsers;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;

import org.apache.commons.io.IOUtils;
import org.cyclonedx.CycloneDxSchema.Version;
import org.cyclonedx.exception.ParseException;
import org.cyclonedx.model.Annotation;
import org.cyclonedx.model.Annotator;
import org.cyclonedx.model.Bom;
import org.cyclonedx.model.Component;
import org.cyclonedx.model.Component.Type;
import org.cyclonedx.model.Composition;
import org.cyclonedx.model.Composition.Aggregate;
import org.cyclonedx.model.Evidence;
import org.cyclonedx.model.ExternalReference;
import org.cyclonedx.model.License;
import org.cyclonedx.model.LicenseChoice;
import org.cyclonedx.model.Licensing;
import org.cyclonedx.model.LifecycleChoice;
import org.cyclonedx.model.LifecycleChoice.Phase;
import org.cyclonedx.model.Metadata;
import org.cyclonedx.model.OrganizationalChoice;
import org.cyclonedx.model.OrganizationalContact;
import org.cyclonedx.model.OrganizationalEntity;
import org.cyclonedx.model.Property;
import org.cyclonedx.model.ReleaseNotes;
import org.cyclonedx.model.ReleaseNotes.Notes;
import org.cyclonedx.model.ReleaseNotes.Resolves;
import org.cyclonedx.model.Service;
import org.cyclonedx.model.ServiceData;
import org.cyclonedx.model.Tool;
import org.cyclonedx.model.component.evidence.Callstack;
import org.cyclonedx.model.component.evidence.Frame;
import org.cyclonedx.model.component.evidence.Identity;
import org.cyclonedx.model.component.evidence.Occurrence;
import org.cyclonedx.model.formulation.Formula;
import org.cyclonedx.model.formulation.Workflow;
import org.cyclonedx.model.formulation.common.InputType;
import org.cyclonedx.model.formulation.common.OutputType;
import org.cyclonedx.model.formulation.common.ResourceReferenceChoice;
import org.cyclonedx.model.formulation.task.Command;
import org.cyclonedx.model.formulation.task.Step;
import org.cyclonedx.model.formulation.task.Task;
import org.cyclonedx.model.formulation.trigger.Condition;
import org.cyclonedx.model.formulation.trigger.Event;
import org.cyclonedx.model.formulation.trigger.Trigger;
import org.cyclonedx.model.formulation.workspace.Volume;
import org.cyclonedx.model.formulation.workspace.Workspace;
import org.cyclonedx.model.vulnerability.Vulnerability;
import org.cyclonedx.model.vulnerability.Vulnerability.Analysis.Justification;
import org.cyclonedx.model.vulnerability.Vulnerability.Analysis.State;
import org.cyclonedx.model.vulnerability.Vulnerability.Rating.Method;
import org.cyclonedx.model.vulnerability.Vulnerability.Rating.Severity;
import org.cyclonedx.model.vulnerability.Vulnerability.Version.Status;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AbstractParserTest
{
  void assertBomProperties(Bom bom, String specVersion) {
    assertEquals(3, bom.getComponents().size());
    assertEquals(specVersion, bom.getSpecVersion());
    assertEquals(1, bom.getVersion());
    assertNotNull(bom.getMetadata());
    assertNotNull(bom.getMetadata().getTimestamp());
  }

  void assertMetadata(Bom bom, Version version) {
    assertBomProperties(bom, version.getVersionString());

    // Assertions for bom.metadata.tools
    assertToolsMetadata(bom.getMetadata().getTools().get(0)
    );

    // Assertions for bom.metadata.authors
    assertAuthorMetadata(bom.getMetadata().getAuthors().get(0)
    );

    // Assertions for bom.metadata.component
    assertComponentMetadata(bom.getMetadata().getComponent());

    // Assertions for bom.metadata.manufacture
    assertManufacturerMetadata(bom.getMetadata().getManufacture()
    );

    // Assertions for bom.metadata.supplier
    assertSupplierMetadata(bom.getMetadata().getSupplier()
    );
  }

  void assertToolsMetadata(Tool tool)
  {
    assertEquals("Awesome Vendor", tool.getVendor());
    assertEquals("Awesome Tool", tool.getName());
    assertEquals("9.1.2", tool.getVersion());
    assertEquals(2, tool.getHashes().size());
    assertEquals("SHA-1", tool.getHashes().get(0).getAlgorithm());
    assertEquals("25ed8e31b995bb927966616df2a42b979a2717f0", tool.getHashes().get(0).getValue());
  }

  void assertAuthorMetadata(OrganizationalContact author) {
    assertEquals("Samantha Wright", author.getName());
    assertEquals("samantha.wright@example.com", author.getEmail());
    assertEquals("800-555-1212", author.getPhone());
  }

  void assertComponentMetadata(Component component) {
    assertEquals("Acme Application", component.getName());
    assertEquals("9.1.1", component.getVersion());
    assertEquals(Type.APPLICATION, component.getType());
    assertNotNull(component.getSwid());
    assertEquals("Acme Application", component.getSwid().getName());
    assertEquals("9.1.1", component.getSwid().getVersion());
    assertEquals(0, component.getSwid().getTagVersion());
    assertFalse(component.getSwid().isPatch());
  }

  void assertManufacturerMetadata(
      OrganizationalEntity manufacturer)
  {
    assertEquals("Acme, Inc.", manufacturer.getName());
    assertEquals("https://example.com", manufacturer.getUrls().get(0));
    assertEquals("Acme Professional Services", manufacturer.getContacts().get(0).getName());
    assertEquals("professional.services@example.com", manufacturer.getContacts().get(0).getEmail());
  }

  void assertSupplierMetadata(
      OrganizationalEntity supplier)
  {
    assertEquals("Acme, Inc.", supplier.getName());
    assertEquals("https://example.com", supplier.getUrls().get(0));
    assertEquals("Acme Distribution", supplier.getContacts().get(0).getName());
    assertEquals("distribution@example.com", supplier.getContacts().get(0).getEmail());
  }

  void assertComponent(
      Component component,
      Type type,
      String purl)
  {
    assertEquals("com.acme", component.getGroup());
    assertEquals("tomcat-catalina", component.getName());
    assertEquals("9.0.14", component.getVersion());
    assertEquals(type, component.getType());
    assertEquals(purl, component.getPurl());
  }

  void assertFormulation(final Bom bom, final Version version) {
    if (version == Version.VERSION_15) {
      List<Formula> formulas = bom.getFormulation();

      assertEquals(1, formulas.size());

      Formula formula = formulas.get(0);
      assertNotNull(formula.getBomRef());
      assertNotNull(formula.getComponents());
      assertNull(formula.getServices());
      assertNull(formula.getProperties());
      assertWorkflows(formula.getWorkflows());
    }
    else {
      assertNull(bom.getFormulation());
    }
  }

  private void assertWorkflows(List<Workflow> workflows) {
    assertEquals(workflows.size(), 1);

    Workflow workflow = workflows.get(0);
    assertNotNull(workflow.getBomRef());
    assertNotNull(workflow.getUid());
    assertNotNull(workflow.getName());
    assertNotNull(workflow.getDescription());
    assertNotNull(workflow.getResourceReferences());

    assertNotNull(workflow.getTimeEnd());
    assertNotNull(workflow.getTimeStart());
    assertNotNull(workflow.getRuntimeTopology());
    assertNotNull(workflow.getTaskDependencies());

    assertTasks(workflow.getTasks());
    assertSteps(workflow.getSteps());
    assertTrigger(workflow.getTrigger());
    assertInputs(workflow.getInputs());
    assertOutputs(workflow.getOutputs());
    assertProperties(workflow.getProperties());
    assertWorkspaces(workflow.getWorkspaces());
  }

  private void assertWorkspaces(List<Workspace> workspaces) {
    assertEquals(workspaces.size(), 1);
    assertNotNull(workspaces);

    Workspace workspace = workspaces.get(0);
    assertNotNull(workspace.getBomRef());
    assertNotNull(workspace.getUid());
    assertNotNull(workspace.getName());
    assertNotNull(workspace.getDescription());
    assertNotNull(workspace.getResourceReferences());

    assertNotNull(workspace.getAccessMode());
    assertNotNull(workspace.getMountPath());
    assertNotNull(workspace.getManagedDataType());
    assertNotNull(workspace.getVolumeRequest());

    assertVolume(workspace.getVolume());

    assertProperties(workspace.getProperties());
  }

  private void assertVolume(Volume volume) {
    assertNotNull(volume);

    assertNotNull(volume.getUid());
    assertNotNull(volume.getName());
    assertNotNull(volume.getMode());
    assertNotNull(volume.getPath());
    assertNotNull(volume.getSizeAllocated());
    assertNotNull(volume.getPersistent());
    assertNotNull(volume.getRemote());

    assertProperties(volume.getProperties());
  }

  private void assertTrigger(Trigger trigger) {
    assertNotNull(trigger);

    assertNotNull(trigger.getBomRef());
    assertNotNull(trigger.getUid());
    assertNotNull(trigger.getName());
    assertNotNull(trigger.getDescription());
    assertNotNull(trigger.getResourceReferences());
    assertNotNull(trigger.getType());
    assertNotNull(trigger.getTimeActivated());

    //Event
    assertEvent(trigger.getEvent());
    //Conditions
    assertConditions(trigger.getConditions());
    //Inputs
    assertInputs(trigger.getInputs());
    //Outputs
    assertOutputs(trigger.getOutputs());

    assertProperties(trigger.getProperties());
  }

  private void assertOutputs(List<OutputType> outputs) {
    assertNotNull(outputs);

    OutputType outputType = outputs.get(0);
    //Source
    assertResourceReference(outputType.getSource());
    //Target
    assertResourceReference(outputType.getTarget());
    assertOutputData(outputType);

    assertProperties(outputType.getProperties());
  }

  private void assertOutputData(OutputType outputType) {
    if (outputType.getResource() != null) {
      assertNull(outputType.getData());
      assertNull(outputType.getEnvironmentVars());
    }
    else if (outputType.getData() != null) {
      assertNull(outputType.getResource());
      assertNull(outputType.getEnvironmentVars());
    }
    else if (outputType.getEnvironmentVars() != null) {
      assertNull(outputType.getResource());
      assertNull(outputType.getData());
    }
    else {
      assertNull(outputType.getResource());
      assertNull(outputType.getData());
      assertNull(outputType.getEnvironmentVars());
    }
  }

  private void assertInputs(List<InputType> inputs) {
    assertNotNull(inputs);

    InputType inputType = inputs.get(0);
    //Source
    assertResourceReference(inputType.getSource());
    //Target
    assertResourceReference(inputType.getTarget());
    assertInputData(inputType);

    assertProperties(inputType.getProperties());
  }

  private void assertInputData(InputType inputType) {
    if (inputType.getResource() != null) {
      assertNull(inputType.getParameters());
      assertNull(inputType.getData());
      assertNull(inputType.getEnvironmentVars());
    }
    else if (inputType.getParameters() != null) {
      assertNull(inputType.getResource());
      assertNull(inputType.getData());
      assertNull(inputType.getEnvironmentVars());
    }
    else if (inputType.getData() != null) {
      assertNull(inputType.getResource());
      assertNull(inputType.getParameters());
      assertNull(inputType.getEnvironmentVars());
    }
    else if (inputType.getEnvironmentVars() != null) {
      assertNull(inputType.getResource());
      assertNull(inputType.getParameters());
      assertNull(inputType.getData());
    }
    else {
      assertNull(inputType.getResource());
      assertNull(inputType.getParameters());
      assertNull(inputType.getData());
      assertNull(inputType.getEnvironmentVars());
    }
  }

  private void assertConditions(List<Condition> conditions) {
    assertEquals(conditions.size(), 1);

    Condition condition = conditions.get(0);

    assertNotNull(condition.getDescription());
    assertNotNull(condition.getExpression());

    assertProperties(condition.getProperties());
  }

  private void assertEvent(Event event) {
    assertNotNull(event);

    assertNotNull(event.getUid());
    assertNotNull(event.getDescription());
    assertNotNull(event.getTimeReceived());
    assertNotNull(event.getData());

    //Source
    assertResourceReference(event.getSource());
    //Target
    assertResourceReference(event.getTarget());

    assertProperties(event.getProperties());
  }

  private void assertResourceReference(ResourceReferenceChoice resourceReferenceChoice) {
    if (resourceReferenceChoice != null) {

      if (resourceReferenceChoice.getExternalReference() != null) {
        assertNull(resourceReferenceChoice.getRef());
      }
      else {
        assertNull(resourceReferenceChoice.getExternalReference());
      }
    }
  }

  private void assertTasks(List<Task> tasks) {
    assertEquals(tasks.size(), 1);

    Task task = tasks.get(0);
    assertNotNull(task.getBomRef());
    assertNotNull(task.getUid());
    assertNotNull(task.getName());
    assertNotNull(task.getDescription());
    assertNotNull(task.getResourceReferences());
    assertNotNull(task.getTaskTypes());
  }

  private void assertSteps(List<Step> steps) {
    assertEquals(steps.size(), 1);

    Step step = steps.get(0);
    assertNotNull(step.getName());
    assertNotNull(step.getDescription());
    assertCommands(step.getCommands());
    assertProperties(step.getProperties());
  }

  private void assertCommands(List<Command> commands) {
    assertEquals(commands.size(), 1);

    Command command = commands.get(0);
    assertNotNull(command.getExecuted());
    assertProperties(command.getProperties());
  }

  private void assertProperties(List<Property> properties) {
    if (properties != null) {
      Property property = properties.get(0);
      assertNotNull(property.getName());
      assertNotNull(property.getValue());
    }
  }

  void assertCompositions(final Bom bom, final Version version) {
    final List<Composition> compositions = bom.getCompositions();
    assertEquals(3, compositions.size());
    Composition composition = compositions.get(0);

    assertEquals(composition.getAggregate(), Aggregate.COMPLETE);
    assertNotNull(composition.getAssemblies());
    assertNotNull(composition.getDependencies());

    //Assert Vulnerability Rejected
    if (version == Version.VERSION_15) {
      composition = compositions.get(2);
      assertNotNull(composition.getVulnerabilities());
      assertEquals("6eee14da-8f42-4cc4-bb65-203235f02415", composition.getVulnerabilities().get(0).getRef());
    }
    else {
      assertNull(composition.getVulnerabilities());
      assertNull(composition.getBomRef());
    }
  }

  void assertAnnotations(final Bom bom, final Version version) {

    if (version == Version.VERSION_15) {
      List<Annotation> annotations = bom.getAnnotations();

      assertEquals(annotations.size(), 1);

      Annotation annotation = annotations.get(0);
      assertNotNull(annotation.getBomRef());
      assertNotNull(annotation.getText());
      assertNotNull(annotation.getTimestamp());

      assertEquals(annotation.getSubjects().size(), 1);
      assertAnnotator(annotation.getAnnotator());
    }
    else {
      assertNull(bom.getAnnotations());
    }
  }

  private void assertAnnotator(final Annotator annotator) {
    assertNotNull(annotator);
    assertNull(annotator.getIndividual());
    assertNull(annotator.getComponent());
    assertNull(annotator.getService());

    assertNotNull(annotator.getOrganization());
    assertEquals(annotator.getOrganization().getName(), "Acme, Inc.");
    assertEquals(annotator.getOrganization().getContacts().size(), 1);
    assertEquals(annotator.getOrganization().getUrls().size(), 1);
  }

  void assertVulnerabilities(final Bom bom, final Version version) {
    final List<Vulnerability> vulnerabilities = bom.getVulnerabilities();
    assertEquals(1, vulnerabilities.size());
    Vulnerability vuln = vulnerabilities.get(0);

    assertEquals("6eee14da-8f42-4cc4-bb65-203235f02415", vuln.getBomRef());
    assertEquals("SONATYPE-2021123", vuln.getId());
    assertEquals("Description", vuln.getDescription());
    assertEquals("Detail", vuln.getDetail());
    assertEquals("Upgrade", vuln.getRecommendation());
    assertEquals(184, vuln.getCwes().get(0));
    assertNotNull(vuln.getCreated());
    assertNotNull(vuln.getPublished());
    assertNotNull(vuln.getUpdated());

    //Assert Vulnerability Rejected
    if (version != Version.VERSION_14) {
      assertNotNull(vuln.getRejected());
    }
    else {
      assertNull(vuln.getRejected());
    }

    //Source
    assertEquals("Sonatype", vuln.getSource().getName());
    assertEquals("https://www.vuln.com", vuln.getSource().getUrl());

    //References
    assertEquals(1, vuln.getReferences().size());
    assertEquals("CVE-2018-7489", vuln.getReferences().get(0).getId());
    assertEquals("NVD", vuln.getReferences().get(0).getSource().getName());
    assertEquals("https://nvd.nist.gov/vuln/detail/CVE-2019-9997",
        vuln.getReferences().get(0).getSource().getUrl());

    //Ratings
    assertEquals(1, vuln.getRatings().size());
    assertEquals("NVD", vuln.getRatings().get(0).getSource().getName());
    assertEquals(
        "https://nvd.nist.gov/vuln-metrics/cvss/v3-calculator?vector=AV:N/AC:L/PR:N/UI:N/S:U/C:H/I:H/A:H&version=3.0",
        vuln.getRatings().get(0).getSource().getUrl());
    assertEquals(9.8, vuln.getRatings().get(0).getScore());
    assertEquals(Severity.CRITICAL, vuln.getRatings().get(0).getSeverity());
    assertEquals(Method.CVSSV3, vuln.getRatings().get(0).getMethod());
    assertEquals("AN/AC:L/PR:N/UI:N/S:U/C:H/I:H/A:H", vuln.getRatings().get(0).getVector());
    assertEquals("An optional reason for rating the vulnerability as it was",
        vuln.getRatings().get(0).getJustification());

    //Advisories
    assertEquals(1, vuln.getAdvisories().size());
    assertEquals("GitHub Commit", vuln.getAdvisories().get(0).getTitle());
    assertEquals("https://github.com/FasterXML/jackson-databind/commit/6799f8f10cc78e9af6d443ed6982d00a13f2e7d2",
        vuln.getAdvisories().get(0).getUrl());

    //Credits
    assertEquals(1, vuln.getCredits().getIndividuals().size());
    assertEquals(1, vuln.getCredits().getOrganizations().size());

    assertEquals("Jane Doe", vuln.getCredits().getIndividuals().get(0).getName());
    assertEquals("jane.doe@example.com", vuln.getCredits().getIndividuals().get(0).getEmail());

    assertEquals("Acme, Inc.", vuln.getCredits().getOrganizations().get(0).getName());
    assertEquals("https://example.com", vuln.getCredits().getOrganizations().get(0).getUrls().get(0));

    //Tools
    assertEquals(1, vuln.getTools().size());
    assertEquals("Sonatype CLI", vuln.getTools().get(0).getName());
    assertEquals("Sonatype", vuln.getTools().get(0).getVendor());
    assertEquals("1.131", vuln.getTools().get(0).getVersion());
    assertEquals(1, vuln.getTools().get(0).getHashes().size());

    //Analysis
    assertEquals(State.NOT_AFFECTED, vuln.getAnalysis().getState());
    assertEquals(Justification.CODE_NOT_REACHABLE, vuln.getAnalysis().getJustification());
    assertEquals("An optional explanation of why the application is not affected by the vulnerable component.",
        vuln.getAnalysis().getDetail());
    assertEquals("update", vuln.getAnalysis().getResponses().get(0).getResponseName());

    //Vulnerability Analysis Timestamp 1.5
    if (version != Version.VERSION_14) {
      assertNotNull(vuln.getAnalysis().getFirstIssued());
      assertNotNull(vuln.getAnalysis().getLastUpdated());
    }
    else {
      assertNull(vuln.getAnalysis().getFirstIssued());
      assertNull(vuln.getAnalysis().getLastUpdated());
    }

    //Affects
    assertEquals(1, vuln.getAffects().size());
    assertEquals("pkg:maven/com.acme/jackson-databind@2.9.9", vuln.getAffects().get(0).getRef());

    assertEquals("vers:semver/<2.6.7.5", vuln.getAffects().get(0).getVersions().get(0).getRange());
    assertEquals(Status.AFFECTED, vuln.getAffects().get(0).getVersions().get(0).getStatus());

    assertEquals("2.9.9", vuln.getAffects().get(0).getVersions().get(1).getVersion());
    assertEquals(Status.AFFECTED, vuln.getAffects().get(0).getVersions().get(1).getStatus());
  }

  void assertServices(final Bom bom) {
    //Services
    List<Service> services = bom.getServices();
    assertEquals(1, services.size());
    Service s = services.get(0);
    OrganizationalEntity provider = s.getProvider();
    assertNotNull(provider);
    assertEquals("Partner Org", provider.getName());
    List<String> urls = provider.getUrls();
    assertEquals(1, urls.size());
    assertEquals("https://partner.org", urls.get(0));

    List<OrganizationalContact> contacts = provider.getContacts();
    assertEquals(1, contacts.size());
    OrganizationalContact contact = contacts.get(0);
    assertEquals("Support", contact.getName());
    assertEquals("support@partner", contact.getEmail());
    assertEquals("800-555-1212", contact.getPhone());
    assertEquals("org.partner", s.getGroup());
    assertEquals("Stock ticker service", s.getName());
    assertEquals("2020-Q2", s.getVersion());
    assertEquals("Provides real-time stock information", s.getDescription());

    List<String> endpoints = s.getEndpoints();
    assertEquals(2, endpoints.size());
    assertEquals("https://partner.org/api/v1/lookup", endpoints.get(0));
    assertEquals("https://partner.org/api/v1/stock", endpoints.get(1));
    assertNotNull(s.getAuthenticated());
    assertNotNull(s.getxTrustBoundary());
    assertTrue(s.getAuthenticated());
    assertTrue(s.getxTrustBoundary());

    List<ServiceData> data = s.getData();
    assertEquals(4, data.size());
    assertEquals("inbound", data.get(0).getFlow().getFlowName());
    assertEquals("PII", data.get(0).getClassification());
    assertEquals(ServiceData.Flow.OUTBOUND, data.get(1).getFlow());
    assertEquals("PIFI", data.get(1).getClassification());
    assertEquals(ServiceData.Flow.BI_DIRECTIONAL, data.get(2).getFlow());
    assertEquals("public", data.get(2).getClassification());
    assertNotNull(s.getLicense());
    assertEquals(1, s.getLicense().getLicenses().size());
    assertEquals("Partner license", s.getLicense().getLicenses().get(0).getName());
    assertEquals(2, s.getExternalReferences().size());
    assertEquals(ExternalReference.Type.WEBSITE, s.getExternalReferences().get(0).getType());
    assertEquals("http://partner.org", s.getExternalReferences().get(0).getUrl());
    assertEquals(ExternalReference.Type.DOCUMENTATION, s.getExternalReferences().get(1).getType());
    assertEquals("http://api.partner.org/swagger", s.getExternalReferences().get(1).getUrl());
  }

  void assertComponent(final Bom bom, final Version version) {
    final List<Component> components = bom.getComponents();
    assertEquals(1, components.size());
    Component component = components.get(0);
    assertEquals("com.acme", component.getGroup());
    assertEquals("jackson-databind", component.getName());
    assertEquals("2.9.4", component.getVersion());
    assertEquals(Component.Type.LIBRARY, component.getType());
    assertEquals("pkg:maven/com.acme/jackson-databind@2.9.4", component.getPurl());
    assertEquals(12, component.getHashes().size());

    //Licenses
    assertLicense(component.getLicenseChoice(), version);

    assertEquals("Copyright Example Inc. All rights reserved.", component.getCopyright());
    assertEquals("Acme Application", component.getSwid().getName());
    assertEquals("9.1.1", component.getSwid().getVersion());
    assertEquals("swidgen-242eb18a-503e-ca37-393b-cf156ef09691_9.1.1", component.getSwid().getTagId());

    if (version == Version.VERSION_14) {
      assertEquals(1, component.getExternalReferences().size());
    }
    else {
      assertEquals(2, component.getExternalReferences().size());
      //Security Contact
      assertSecurityContact(component.getExternalReferences().get(1));
    }

    //Evidence
    assertEvidence(component.getEvidence(), version);
  }

  private void assertEvidence(final Evidence evidence, final Version version) {
    assertNotNull(evidence);
    assertEquals("Copyright 2012 Google Inc. All Rights Reserved.", evidence.getCopyright().get(0).getText());
    assertEquals("Apache-2.0", evidence.getLicenseChoice().getLicenses().get(0).getId());
    assertEquals("http://www.apache.org/licenses/LICENSE-2.0",
        evidence.getLicenseChoice().getLicenses().get(0).getUrl());

    if(version== Version.VERSION_15) {
      assertCallStack(evidence.getCallstack());
      assertOccurrences(evidence.getOccurrences());
      assertIdentity(evidence.getIdentity());
    } else {
      assertNull(evidence.getCallstack());
      assertNull(evidence.getIdentity());
      assertNull(evidence.getOccurrences());
    }
  }

  private void assertOccurrences(final List<Occurrence> occurrences){
    assertEquals(occurrences.size(), 1);
    Occurrence occurrence = occurrences.get(0);

    assertNotNull(occurrence.getBomRef());
    assertNotNull(occurrence.getLocation());
  }

  private void assertCallStack(final Callstack callstack){
    assertNotNull(callstack);

    assertEquals(callstack.getFrames().size(), 1);
    Frame frame = callstack.getFrames().get(0);

    assertNotNull(frame.getColumn());
    assertNotNull(frame.getLine());
    assertNull(frame.getPackageFrame());
    assertNull(frame.getFunction());
    assertNull(frame.getFullFilename());
    assertNull(frame.getParameters());
    assertNotNull(frame.getModule());
  }

  private void assertIdentity(final Identity identity){
    assertNotNull(identity);

    assertNotNull(identity.getField());
    assertNotNull(identity.getConfidence());
    assertNotNull(identity.getMethods());
    assertNotNull(identity.getTools());

    assertNotNull(identity.getTools().get(0).getRef());
  }

  private void assertSecurityContact(ExternalReference externalReference) {
    assertEquals(externalReference.getType(), ExternalReference.Type.SECURITY_CONTACT);
    assertEquals(externalReference.getComment(), "test");
    assertEquals(externalReference.getUrl(), "http://example.org/docs");
  }

  private void assertLicense(LicenseChoice licenseChoice, final Version version) {
    assertNotNull(licenseChoice);
    assertNull(licenseChoice.getExpression());

    assertEquals(1, licenseChoice.getLicenses().size());
    License license = licenseChoice.getLicenses().get(0);
    assertNotNull(license);

    if (version == Version.VERSION_14) {
      assertNull(license.getProperties());
      assertNull(license.getBomRef());
    }
    else {
      //Dev Licensing
      assertLicensing(license.getLicensing());
      assertEquals(license.getProperties().size(), 1);
      assertNotNull(license.getBomRef());
    }
  }

  private void assertLicensing(final Licensing licensing) {
    assertNotNull(licensing);

    assertEquals(1, licensing.getAltIds().size());
    assertNotNull(licensing.getPurchaseOrder());
    assertLicensor(licensing.getLicensor());
    assertLicensee(licensing.getLicensee());
    assertPurchaser(licensing.getPurchaser());
    assertNotNull(licensing.getExpiration());
    assertNotNull(licensing.getLastRenewal());
    assertEquals(licensing.getLicenseTypes().size(), 1);
  }

  private void assertLicensor(OrganizationalChoice licensor) {
    assertNull(licensor.getIndividual());
    assertNotNull(licensor.getOrganization());
    assertEquals(licensor.getOrganization().getName(), "Acme Inc");
    assertEquals(licensor.getOrganization().getContacts().size(), 2);
    assertNull(licensor.getOrganization().getUrls());
  }

  private void assertLicensee(OrganizationalChoice licensee) {
    assertNull(licensee.getIndividual());
    assertNotNull(licensee.getOrganization());
    assertEquals(licensee.getOrganization().getName(), "Example Co.");
    assertNull(licensee.getOrganization().getContacts());
    assertNull(licensee.getOrganization().getUrls());
  }

  private void assertPurchaser(OrganizationalChoice purchaser) {
    assertNull(purchaser.getOrganization());
    assertNotNull(purchaser.getIndividual());
    assertEquals(purchaser.getIndividual().getName(), "Samantha Wright");
    assertEquals(purchaser.getIndividual().getEmail(), "samantha.wright@gmail.com");
    assertEquals(purchaser.getIndividual().getPhone(), "800-555-1212");
  }

  void assertMetadata(final Metadata metadata, final Version version) {
    assertNotNull(metadata);
    assertNotNull(metadata.getTimestamp());

    //Lifecycles
    if (version == Version.VERSION_15) {
      assertNotNull(metadata.getLifecycles());
      assertEquals(2, metadata.getLifecycles().getLifecycleChoice().size());
      LifecycleChoice firstChoice = metadata.getLifecycles().getLifecycleChoice().get(0);
      assertEquals(Phase.BUILD.getPhaseName(), firstChoice.getPhase().getPhaseName());
      assertNull(firstChoice.getName());
      assertNull(firstChoice.getDescription());

      LifecycleChoice secondChoice = metadata.getLifecycles().getLifecycleChoice().get(1);
      assertEquals("platform-integration-testing", secondChoice.getName());
      assertNotNull(secondChoice.getDescription());
      assertNull(secondChoice.getPhase());
    }
    else {
      assertNull(metadata.getLifecycles());
    }

    //License
    if (version.getVersion() > Version.VERSION_12.getVersion()) {
      assertNotNull(metadata.getLicenseChoice());
    }
    else {
      assertNull(metadata.getLicenseChoice());
    }

    //Tool
    assertEquals(1, metadata.getTools().size());
    assertEquals("Awesome Vendor", metadata.getTools().get(0).getVendor());
    assertEquals("Awesome Tool", metadata.getTools().get(0).getName());
    assertEquals("9.1.2", metadata.getTools().get(0).getVersion());
    assertEquals(1, metadata.getTools().get(0).getHashes().size());
    assertEquals("SHA-1", metadata.getTools().get(0).getHashes().get(0).getAlgorithm());
    assertEquals("25ed8e31b995bb927966616df2a42b979a2717f0",
        metadata.getTools().get(0).getHashes().get(0).getValue());

    //Author
    assertEquals(1, metadata.getAuthors().size());
    assertEquals("Samantha Wright", metadata.getAuthors().get(0).getName());
    assertEquals("samantha.wright@example.com", metadata.getAuthors().get(0).getEmail());
    assertEquals("800-555-1212", metadata.getAuthors().get(0).getPhone());

    //Component
    Component component = metadata.getComponent();
    assertEquals("Acme Application", component.getName());
    assertEquals("9.1.1", component.getVersion());
    assertEquals(Component.Type.APPLICATION, component.getType());
    assertNotNull(component.getSwid());
    assertEquals("swidgen-242eb18a-503e-ca37-393b-cf156ef09691_9.1.1", component.getSwid().getTagId());
    assertEquals("Acme Application", component.getSwid().getName());
    assertEquals("9.1.1", component.getSwid().getVersion());
    assertEquals(0, component.getSwid().getTagVersion());
    assertFalse(component.getSwid().isPatch());

    //Release Notes
    ReleaseNotes releaseNotes = metadata.getComponent().getReleaseNotes();
    assertEquals("major", releaseNotes.getType());
    assertEquals("My new release", releaseNotes.getTitle());
    assertNotNull(releaseNotes.getDescription());
    assertNotNull(releaseNotes.getSocialImage());
    assertNotNull(releaseNotes.getTimestamp());
    assertEquals(1, releaseNotes.getAliases().size());
    assertEquals(1, releaseNotes.getTags().size());

    //Resolves
    assertEquals(1, releaseNotes.getResolves().size());
    Resolves resolves = releaseNotes.getResolves().get(0);
    assertEquals(Resolves.Type.SECURITY, resolves.getType());
    assertEquals("CVE-2019-9997", resolves.getId());
    assertEquals("A security issue was fixed that did something bad", resolves.getDescription());
    assertEquals("CVE-2019-9997", resolves.getName());

    //Notes
    assertEquals(1, releaseNotes.getNotes().size());
    Notes note = releaseNotes.getNotes().get(0);
    assertEquals("en-US", note.getLocale());
    assertNotNull(note.getText());
    assertEquals("PGgxPk15IG5ldyByZWxlYXNlPGgxPgo8cD5SZWxlYXNlIG5vdGVzIGhlcmU8L3A+", note.getText().getText());
    assertEquals("text/html", note.getText().getContentType());
    assertEquals("base64", note.getText().getEncoding());

    //Manufacture
    assertEquals("Acme, Inc.", metadata.getManufacture().getName());
    assertEquals("https://example.com", metadata.getManufacture().getUrls().get(0));
    assertEquals(1, metadata.getManufacture().getContacts().size());
    assertEquals("Acme Professional Services", metadata.getManufacture().getContacts().get(0).getName());
    assertEquals("professional.services@example.com", metadata.getManufacture().getContacts().get(0).getEmail());
    assertEquals("Acme, Inc.", metadata.getSupplier().getName());

    //Supplier
    assertEquals("https://example.com", metadata.getSupplier().getUrls().get(0));
    assertEquals(1, metadata.getSupplier().getContacts().size());
    assertEquals("Acme Distribution", metadata.getSupplier().getContacts().get(0).getName());
    assertEquals("distribution@example.com", metadata.getSupplier().getContacts().get(0).getEmail());
  }

  void assertMetadata_validTools(final Metadata metadata) {
    assertNotNull(metadata);
    assertNotNull(metadata.getTimestamp());

    assertNull(metadata.getLifecycles());
    assertNull(metadata.getLicenseChoice());
    assertNull(metadata.getAuthors());
    assertNull(metadata.getComponent());
    assertNull(metadata.getManufacture());
    assertNull(metadata.getAuthors());
    assertNull(metadata.getSupplier());
    assertNull(metadata.getProperties());
    assertNull(metadata.getSupplier());
    assertNull(metadata.getTools());

    assertEquals(1, metadata.getToolChoice().getComponents().size());
    assertEquals(1, metadata.getToolChoice().getServices().size());
  }

  void assertCommonBomProperties(Bom bom, Version version) {
    assertEquals(version.getVersionString(), bom.getSpecVersion());
    assertEquals(1, bom.getVersion());
  }

  Bom getXmlBom(String filename) throws ParseException, IOException {
    final byte[] bomBytes = getBomBytes(filename);
    final XmlParser parser = new XmlParser();
    return parser.parse(bomBytes);
  }

  Bom getJsonBom(String filename) throws ParseException, IOException {
    final byte[] bomBytes = getBomBytes(filename);
    final JsonParser parser = new JsonParser();
    return parser.parse(bomBytes);
  }

  private byte[] getBomBytes(String filename) throws IOException {
    final InputStream inputStream = this.getClass().getResourceAsStream("/" + filename);
    return IOUtils.toByteArray(Objects.requireNonNull(inputStream));
  }
}
