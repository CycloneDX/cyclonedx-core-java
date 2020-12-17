package org.cyclonedx.util;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser;
import org.cyclonedx.model.Extension;
import org.cyclonedx.model.vulnerability.Vulnerability1_0;

public class ExtensionDeserializer extends StdDeserializer<Map<String, Extension>>
{
  private ObjectMapper objectMapper;

  public ExtensionDeserializer() {
    this(Extension.class);
  }

  public ExtensionDeserializer(final Class vc) {
    super(vc);
    this.objectMapper = new ObjectMapper();
  }

  @Override
  public Map<String, Extension> deserialize(
      final JsonParser p, final DeserializationContext ctxt) throws IOException, JsonProcessingException
  {
    if (p.currentName().equals("vulnerabilities")) {
      if (p instanceof FromXmlParser) {
        TreeNode on =  p.readValueAsTree();
        TreeNode vn = on.get("vulnerability");
        if (vn.isArray()) {
          Vulnerability1_0[] vulns = objectMapper.readValue(vn.toString(), Vulnerability1_0[].class);

          Extension ext = new Extension();
          ext.setExtensions(Arrays.asList(vulns));

          Map<String, Extension> extension = new HashMap<>();
          extension.put("vulnerabilities", ext);

          return extension;
        }
      }
    }
    return null;
  }
}
