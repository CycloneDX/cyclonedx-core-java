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
 * Copyright (c) Steve Springett. All Rights Reserved.
 */
package org.cyclonedx.util;

import java.util.HashMap;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLInputFactory;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.enums.EnumToStringConverter;
import com.thoughtworks.xstream.io.xml.QNameMap;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import com.thoughtworks.xstream.mapper.CannotResolveClassException;
import com.thoughtworks.xstream.mapper.MapperWrapper;
import org.cyclonedx.CycloneDxSchema;
import org.cyclonedx.CycloneDxSchema.Version;
import org.cyclonedx.converters.AttachmentTextConverter;
import org.cyclonedx.converters.DateConverter;
import org.cyclonedx.converters.DependencyConverter;
import org.cyclonedx.converters.HashConverter;
import org.cyclonedx.converters.ModifiedConverter;
import org.cyclonedx.model.AuthorContact;
import org.cyclonedx.model.Bom;
import org.cyclonedx.model.Commit;
import org.cyclonedx.model.Component;
import org.cyclonedx.model.Component.Scope;
import org.cyclonedx.model.Component.Type;
import org.cyclonedx.model.Dependency;
import org.cyclonedx.model.ExternalReference;
import org.cyclonedx.model.Hash;
import org.cyclonedx.model.License;
import org.cyclonedx.model.LicenseChoice;
import org.cyclonedx.model.Metadata;
import org.cyclonedx.model.OrganizationalContact;
import org.cyclonedx.model.OrganizationalEntity;
import org.cyclonedx.model.Pedigree;
import org.cyclonedx.model.Swid;
import org.cyclonedx.model.Tool;

public class XStreamUtils
{
  public static XStream mapObjectModelBom1_0(XStream xstream) {
    xstream.alias("bom", Bom.class);
    xstream.aliasAttribute(Bom.class, "version", "version");
    xstream.aliasAttribute(Bom.class, "serialNumber", "serialNumber");
    xstream.registerConverter(new DateConverter());
    xstream.registerLocalConverter(Component.class, "modified", new ModifiedConverter(Version.VERSION_10));

    xstream.alias("component", Component.class);
    xstream.aliasAttribute(Component.class, "type", "type");
    xstream.aliasAttribute(Component.class, "bomRef", "bom-ref");
    xstream.alias("scope", Scope.class);
    xstream.registerConverter(new EnumToStringConverter<>(Component.Type.class, getComponentTypeMapping()));
    xstream.registerConverter(new EnumToStringConverter<>(Component.Scope.class, getComponentScopeMapping()));

    xstream.alias("hash", Hash.class);
    xstream.registerConverter(new HashConverter());

    xstream.alias("commit", Commit.class);

    xstream.alias("reference", ExternalReference.class);
    xstream.aliasAttribute(ExternalReference.class, "type", "type");
    xstream.registerConverter(new EnumToStringConverter<>(ExternalReference.Type.class, getExternalReferenceTypeMapping()));

    xstream.aliasField("licenses", Component.class, "licenseChoice");
    xstream.alias("license", License.class);
    xstream.addImplicitCollection(LicenseChoice.class, "licenses");
    xstream.registerConverter(new AttachmentTextConverter());
    xstream.aliasField("text", License.class, "attachmentText");

    xstream.alias("pedigree", Pedigree.class);

    return xstream;
  }

  public static XStream mapObjectModelBom1_1(XStream xstream) {
    xstream.alias("bom", Bom.class);
    xstream.aliasAttribute(Bom.class, "version", "version");
    xstream.aliasAttribute(Bom.class, "serialNumber", "serialNumber");
    xstream.registerConverter(new DateConverter());

    xstream.alias("component", Component.class);
    xstream.aliasAttribute(Component.class, "type", "type");
    xstream.aliasAttribute(Component.class, "bomRef", "bom-ref");
    xstream.alias("scope", Scope.class);
    xstream.registerConverter(new EnumToStringConverter<>(Component.Type.class, getComponentTypeMapping()));
    xstream.registerConverter(new EnumToStringConverter<>(Component.Scope.class, getComponentScopeMapping()));

    xstream.alias("hash", Hash.class);
    xstream.registerConverter(new HashConverter());

    xstream.alias("commit", Commit.class);

    xstream.alias("reference", ExternalReference.class);
    xstream.aliasAttribute(ExternalReference.class, "type", "type");
    xstream.registerConverter(new EnumToStringConverter<>(ExternalReference.Type.class, getExternalReferenceTypeMapping()));

    xstream.aliasField("licenses", Component.class, "licenseChoice");
    xstream.alias("license", License.class);
    xstream.addImplicitCollection(LicenseChoice.class, "licenses");
    xstream.registerConverter(new AttachmentTextConverter());
    xstream.aliasField("text", License.class, "attachmentText");

    xstream.alias("pedigree", Pedigree.class);

    xstream.registerLocalConverter(Bom.class, "dependencies", new DependencyConverter());

    return xstream;
  }

  public static XStream mapObjectModelBom1_2(XStream xstream) {
    xstream.alias("bom", Bom.class);
    xstream.aliasAttribute(Bom.class, "version", "version");
    xstream.aliasAttribute(Bom.class, "serialNumber", "serialNumber");
    xstream.registerConverter(new DateConverter());

    xstream.alias("component", Component.class);
    xstream.aliasAttribute(Component.class, "type", "type");
    xstream.aliasAttribute(Component.class, "bomRef", "bom-ref");
    xstream.aliasAttribute(Component.class, "mimeType", "mime-type");
    xstream.alias("scope", Scope.class);
    xstream.registerConverter(new EnumToStringConverter<>(Component.Type.class, getComponentTypeMapping()));
    xstream.registerConverter(new EnumToStringConverter<>(Component.Scope.class, getComponentScopeMapping()));

    xstream.alias("hash", Hash.class);
    xstream.registerConverter(new HashConverter());

    xstream.alias("swid", Swid.class);
    xstream.aliasAttribute(Swid.class, "tagId", "tagId");
    xstream.aliasAttribute(Swid.class, "name", "name");
    xstream.aliasAttribute(Swid.class, "version", "version");
    xstream.aliasAttribute(Swid.class, "tagVersion", "tagVersion");
    xstream.aliasAttribute(Swid.class, "patch", "patch");
    xstream.registerConverter(new AttachmentTextConverter());
    xstream.aliasField("text", Swid.class, "attachmentText");

    xstream.alias("commit", Commit.class);

    xstream.alias("reference", ExternalReference.class);
    xstream.aliasAttribute(ExternalReference.class, "type", "type");
    xstream.registerConverter(new EnumToStringConverter<>(ExternalReference.Type.class, getExternalReferenceTypeMapping()));

    xstream.aliasField("licenses", Component.class, "licenseChoice");
    xstream.alias("license", License.class);
    xstream.addImplicitCollection(LicenseChoice.class, "licenses");
    xstream.registerConverter(new AttachmentTextConverter());
    xstream.aliasField("text", License.class, "attachmentText");

    xstream.alias("pedigree", Pedigree.class);

    xstream.addImplicitCollection(Dependency.class, "dependencies");
    xstream.alias("dependency", Dependency.class);
    xstream.aliasAttribute(Dependency.class, "ref", "ref");

    xstream.alias("metadata", Metadata.class);
    xstream.alias("tool", Tool.class);
    xstream.alias("author", AuthorContact.class);
    xstream.alias("manufacture", OrganizationalEntity.class);
    xstream.alias("supplier", OrganizationalEntity.class);
    xstream.alias("contact", OrganizationalContact.class);
    xstream.addImplicitCollection(OrganizationalEntity.class, "contacts");

    return xstream;
  }

  private static Map<String, Type> getComponentTypeMapping() {
    final Map<String, Component.Type> map = new HashMap<>();
    map.put(Component.Type.APPLICATION.getTypeName(), Component.Type.APPLICATION);
    map.put(Component.Type.FRAMEWORK.getTypeName(), Component.Type.FRAMEWORK);
    map.put(Component.Type.LIBRARY.getTypeName(), Component.Type.LIBRARY);
    map.put(Component.Type.CONTAINER.getTypeName(), Component.Type.CONTAINER);
    map.put(Component.Type.OPERATING_SYSTEM.getTypeName(), Component.Type.OPERATING_SYSTEM);
    map.put(Component.Type.FIRMWARE.getTypeName(), Component.Type.FIRMWARE);
    map.put(Component.Type.DEVICE.getTypeName(), Component.Type.DEVICE);
    map.put(Component.Type.FILE.getTypeName(), Component.Type.FILE);
    return map;
  }

  private static Map<String, Component.Scope> getComponentScopeMapping() {
    final Map<String, Component.Scope> map = new HashMap<>();
    map.put(Component.Scope.REQUIRED.getScopeName(), Component.Scope.REQUIRED);
    map.put(Component.Scope.EXCLUDED.getScopeName(), Component.Scope.EXCLUDED);
    map.put(Component.Scope.OPTIONAL.getScopeName(), Component.Scope.OPTIONAL);
    return map;
  }

  private static Map<String, ExternalReference.Type> getExternalReferenceTypeMapping() {
    final Map<String, ExternalReference.Type> map = new HashMap<>();
    map.put(ExternalReference.Type.VCS.getTypeName(), ExternalReference.Type.VCS);
    map.put(ExternalReference.Type.ISSUE_TRACKER.getTypeName(), ExternalReference.Type.ISSUE_TRACKER);
    map.put(ExternalReference.Type.WEBSITE.getTypeName(), ExternalReference.Type.WEBSITE);
    map.put(ExternalReference.Type.ADVISORIES.getTypeName(), ExternalReference.Type.ADVISORIES);
    map.put(ExternalReference.Type.BOM.getTypeName(), ExternalReference.Type.BOM);
    map.put(ExternalReference.Type.MAILING_LIST.getTypeName(), ExternalReference.Type.MAILING_LIST);
    map.put(ExternalReference.Type.SOCIAL.getTypeName(), ExternalReference.Type.SOCIAL);
    map.put(ExternalReference.Type.CHAT.getTypeName(), ExternalReference.Type.CHAT);
    map.put(ExternalReference.Type.DOCUMENTATION.getTypeName(), ExternalReference.Type.DOCUMENTATION);
    map.put(ExternalReference.Type.SUPPORT.getTypeName(), ExternalReference.Type.SUPPORT);
    map.put(ExternalReference.Type.DISTRIBUTION.getTypeName(), ExternalReference.Type.DISTRIBUTION);
    map.put(ExternalReference.Type.LICENSE.getTypeName(), ExternalReference.Type.LICENSE);
    map.put(ExternalReference.Type.BUILD_META.getTypeName(), ExternalReference.Type.BUILD_META);
    map.put(ExternalReference.Type.BUILD_SYSTEM.getTypeName(), ExternalReference.Type.BUILD_SYSTEM);
    map.put(ExternalReference.Type.OTHER.getTypeName(), ExternalReference.Type.OTHER);
    return map;
  }

  public static XStream createXStream() {
    return createXStream(CycloneDxSchema.NS_BOM_LATEST, null);
  }

  public static XStream createXStream(final String namespace, QName dependencies) {
    final QName qname = new QName(namespace);
    final QNameMap nsm = new QNameMap();
    nsm.registerMapping(dependencies, Bom.class);
    nsm.registerMapping(dependencies, "dependency");
    nsm.setDefaultNamespace(namespace);

    StaxDriver staxDriver = new StaxDriver(nsm);
    staxDriver.getInputFactory().setProperty(XMLInputFactory.IS_NAMESPACE_AWARE, true);
    final XStream xstream = new XStream(staxDriver) {
      @Override
      protected MapperWrapper wrapMapper(MapperWrapper next) {
        return new MapperWrapper(next) {
          @Override
          public boolean shouldSerializeMember(Class definedIn, String fieldName) {
            try {
              return definedIn != Object.class || realClass(fieldName) != null;
            } catch(CannotResolveClassException e) {
              return false;
            }
          }
          @Override
          public Class realClass(String elementName) {
            try {
              return super.realClass(elementName);
            } catch(CannotResolveClassException e) {
              return null;
            }
          }
        };
      }
    };
    XStream.setupDefaultSecurity(xstream);
    xstream.allowTypesByWildcard(new String[] {
        "org.cyclonedx.model.**"
    });
    return xstream;
  }
}
