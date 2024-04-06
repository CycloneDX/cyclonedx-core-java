package org.cyclonedx.generators;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.cyclonedx.CycloneDxSchema;
import org.cyclonedx.Version;
import org.cyclonedx.model.Bom;
import org.cyclonedx.util.serializer.InputTypeSerializer;
import org.cyclonedx.util.serializer.LifecycleSerializer;
import org.cyclonedx.util.serializer.MetadataSerializer;
import org.cyclonedx.util.serializer.OutputTypeSerializer;

public abstract class AbstractBomGenerator extends CycloneDxSchema
{
  protected ObjectMapper mapper;

  protected final Version version;

  protected final Bom bom;

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
  }
}
