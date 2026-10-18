package org.cyclonedx.util.deserializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLStreamReader;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser;

public class DeserializerUtils
{
  private static final String CYCLONEDX_BOM_NAMESPACE_PREFIX = "http://cyclonedx.org/schema/bom/";

  public static ArrayNode getArrayNode(JsonNode nodes, final ObjectMapper objectMapper) {
    if (objectMapper != null) {
      return nodes.isArray() ? (ArrayNode) nodes : new ArrayNode(objectMapper.getNodeFactory()).add(nodes);
    }
    else {
      return nodes.isArray() ? (ArrayNode) nodes : new ArrayNode(null).add(nodes);
    }
  }

  static <T> List<T> readList(JsonParser parser, DeserializationContext ctxt, Class<T> type) throws IOException {
    return ctxt.readValue(parser, ctxt.getTypeFactory().constructCollectionType(ArrayList.class, type));
  }

  static boolean isForeignXmlElement(final JsonParser parser) {
    if (!(parser instanceof FromXmlParser)) {
      return false;
    }

    final XMLStreamReader reader = ((FromXmlParser) parser).getStaxReader();
    if (!reader.isStartElement() && !reader.isEndElement()) {
      // Reader already advanced to the element's first child or end tag in the same namespace.
      return false;
    }

    final String namespace = reader.getNamespaceURI();
    return namespace != null && !namespace.startsWith(CYCLONEDX_BOM_NAMESPACE_PREFIX);
  }
}
