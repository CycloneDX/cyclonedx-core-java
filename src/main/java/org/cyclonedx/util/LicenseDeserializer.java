package org.cyclonedx.util;

import java.io.DataInput;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser;
import org.cyclonedx.model.License;
import org.cyclonedx.model.LicenseChoice;

public class LicenseDeserializer extends JsonDeserializer<LicenseChoice>
{
  @Override
  public LicenseChoice deserialize(
      final JsonParser p, final DeserializationContext ctxt) throws IOException, JsonProcessingException
  {
    if (p instanceof FromXmlParser) {
      return p.readValueAs(LicenseChoice.class);
    }
    ArrayNode nodes = p.readValueAsTree();

    ObjectMapper mapper = new ObjectMapper();
    TypeFactory factory = TypeFactory.defaultInstance();
    MapType type = factory.constructMapType(HashMap.class, String.class, License.class);
    LicenseChoice licenseChoice = new LicenseChoice();
    for (JsonNode node : nodes) {
      HashMap<String, License> map = mapper.readValue(node.toString(), type);

      if (map.get("license") != null) {
        licenseChoice.addLicense(map.get("license"));
      }
    }

    return licenseChoice;
  }
}
