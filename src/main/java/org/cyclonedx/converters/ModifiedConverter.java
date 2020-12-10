package org.cyclonedx.converters;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import org.cyclonedx.CycloneDxSchema;

public class ModifiedConverter implements Converter
{
  private final CycloneDxSchema.Version version;

  public ModifiedConverter(final CycloneDxSchema.Version version) {
    this.version = version;
  }

  @Override
  public void marshal(
      final Object o,
      final HierarchicalStreamWriter writer,
      final MarshallingContext context)
  {
    if (this.version.getVersion() == 1.0) {
      if (o == null || !((Boolean) o)) {
        writer.setValue("false");
      }
    }
  }

  @Override
  public Object unmarshal(
      final HierarchicalStreamReader reader, final UnmarshallingContext context)
  {
    if (this.version.getVersion() == 1.0) {
      if (reader.getValue().equals("")) {
        return false;
      }
    }
    return reader.getValue();
  }

  @Override
  public boolean canConvert(final Class clazz) {
    return clazz.equals(Boolean.class);
  }
}
