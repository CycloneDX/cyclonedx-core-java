package org.cyclonedx.converters;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import org.cyclonedx.model.Dependency;

public class DependencyConverter
    implements Converter
{
  @Override
  public boolean canConvert(Class clazz) {
    return clazz.equals(ArrayList.class);
  }

  @Override
  public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
    return null;
  }

  @Override
  public void marshal(final Object o, final HierarchicalStreamWriter writer, final MarshallingContext context)
  {
    List<Dependency> dependencies = (List<Dependency>) o;

    if(dependencies!= null && !dependencies.isEmpty()) {
      marshalDependencies(dependencies, writer);
    }
  }

  private void marshalDependencies(final List<Dependency> dependencies, final HierarchicalStreamWriter writer) {
    for (Dependency dependency : dependencies) {
      writer.startNode("dg:dependency");
      writer.addAttribute("ref", dependency.getRef());
      if(dependency.getDependencies()!=null && !dependency.getDependencies().isEmpty()) {
        marshalDependencies(dependency.getDependencies(), writer);
      }
      writer.endNode();
    }
  }
}
