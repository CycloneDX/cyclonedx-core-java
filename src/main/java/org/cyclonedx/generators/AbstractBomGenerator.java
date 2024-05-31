package org.cyclonedx.generators;

import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.cyclonedx.CycloneDxSchema;
import org.cyclonedx.Version;
import org.cyclonedx.model.Bom;
import org.cyclonedx.model.Metadata;
import org.cyclonedx.model.component.VulnerabilityMixIn14;
import org.cyclonedx.model.component.VulnerabilityMixIn15;
import org.cyclonedx.model.metadata.MetadataMixIn12;
import org.cyclonedx.model.metadata.MetadataMixIn15;
import org.cyclonedx.model.vulnerability.Vulnerability;
import org.cyclonedx.util.deserializer.ToolsDeserializer;
import org.cyclonedx.util.deserializer.VulnerabilityDeserializer;
import org.cyclonedx.util.serializer.EvidenceSerializer;
import org.cyclonedx.util.serializer.InputTypeSerializer;
import org.cyclonedx.util.serializer.LicenseChoiceSerializer;
import org.cyclonedx.util.serializer.LifecycleSerializer;
import org.cyclonedx.util.serializer.MetadataSerializer;
import org.cyclonedx.util.serializer.OutputTypeSerializer;
import org.cyclonedx.util.serializer.SignatorySerializer;

public abstract class AbstractBomGenerator extends CycloneDxSchema
{
  protected ObjectMapper mapper;

  protected final Version version;

  protected Bom bom;

  public AbstractBomGenerator(final Version version, final Bom bom) {
    this.mapper = new ObjectMapper();
    this.version = version;
    this.bom = bom;
  }

  /**
   * Returns the version of the CycloneDX schema used by this instance
   * @return a CycloneDxSchemaVersion enum
   */
  public Version getSchemaVersion() {
    return version;
  }

  protected void setupObjectMapper(boolean isXml) {
    SimpleModule licenseModule = new SimpleModule();
    licenseModule.addSerializer(new LicenseChoiceSerializer(isXml, version));
    mapper.registerModule(licenseModule);

    SimpleModule lifecycleModule = new SimpleModule();
    lifecycleModule.addSerializer(new LifecycleSerializer(isXml));
    mapper.registerModule(lifecycleModule);

    SimpleModule metadataModule = new SimpleModule();
    metadataModule.addSerializer(new MetadataSerializer(isXml, getSchemaVersion()));
    mapper.registerModule(metadataModule);

    SimpleModule inputTypeModule = new SimpleModule();
    inputTypeModule.addSerializer(new InputTypeSerializer(isXml));
    mapper.registerModule(inputTypeModule);

    SimpleModule outputTypeModule = new SimpleModule();
    outputTypeModule.addSerializer(new OutputTypeSerializer(isXml));
    mapper.registerModule(outputTypeModule);

    SimpleModule evidenceModule = new SimpleModule();
    evidenceModule.addSerializer(new EvidenceSerializer(isXml, getSchemaVersion()));
    mapper.registerModule(evidenceModule);

    SimpleModule signatoryModule = new SimpleModule();
    signatoryModule.addSerializer(new SignatorySerializer(isXml));
    mapper.registerModule(signatoryModule);

    mapper.addMixIn(Vulnerability.class, VulnerabilityMixIn14.class);
    mapper.addMixIn(Vulnerability.class, VulnerabilityMixIn15.class);

    mapper.addMixIn(Metadata.class, MetadataMixIn12.class);
    mapper.addMixIn(Metadata.class, MetadataMixIn15.class);

    SimpleModule module = new SimpleModule();
    module.addDeserializer(Object.class, new VulnerabilityDeserializer());
    module.addDeserializer(Object.class, new ToolsDeserializer());
    mapper.registerModule(module);
  }
}
