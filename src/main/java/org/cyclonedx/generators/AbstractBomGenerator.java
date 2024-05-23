package org.cyclonedx.generators;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.cyclonedx.CycloneDxSchema;
import org.cyclonedx.Version;
import org.cyclonedx.model.Bom;
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
    licenseModule.addSerializer(new LicenseChoiceSerializer(isXml));
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
  }
}
