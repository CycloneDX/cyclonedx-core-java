package org.cyclonedx.util.deserializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.stream.XMLStreamReader;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser;

public class DeserializerUtils
{
  private static final String CYCLONEDX_BOM_NAMESPACE_PREFIX = "http://cyclonedx.org/schema/bom/";

  static <T> List<T> readList(JsonParser parser, DeserializationContext ctxt, Class<T> type) throws IOException {
    return ctxt.readValue(parser, ctxt.getTypeFactory().constructCollectionType(ArrayList.class, type));
  }

  static <T> void readItemsInto(JsonParser parser, DeserializationContext ctxt, Class<T> type, List<T> items) throws IOException {
    if (parser.currentToken() == JsonToken.START_ARRAY) {
      while (parser.nextToken() != JsonToken.END_ARRAY) {
        final T item = ctxt.readValue(parser, type);
        if (item != null) {
          items.add(item);
        }
      }
    } else {
      final T item = ctxt.readValue(parser, type);
      if (item != null) {
        items.add(item);
      }
    }
  }

  static Set<String> fieldNames(TokenBuffer buffer) throws IOException {
    final Set<String> fieldNames = new HashSet<>();

    try (final JsonParser parser = buffer.asParserOnFirstToken()) {
      if (parser.currentToken() == JsonToken.START_OBJECT) {
        for (JsonToken token = parser.nextToken(); token == JsonToken.FIELD_NAME; token = parser.nextToken()) {
          fieldNames.add(parser.currentName());
          parser.nextToken();
          parser.skipChildren();
        }
      }
    }

    return fieldNames;
  }

  static JsonParser fieldValue(TokenBuffer buffer, String fieldName) throws IOException {
    final JsonParser parser = buffer.asParserOnFirstToken();
    if (parser.currentToken() == JsonToken.START_OBJECT) {
      for (JsonToken token = parser.nextToken(); token == JsonToken.FIELD_NAME; token = parser.nextToken()) {
        final String currentName = parser.currentName();
        parser.nextToken();
        if (fieldName.equals(currentName)) {
          return parser;
        }
        parser.skipChildren();
      }
    }

    parser.close();
    return null;
  }

  static String readText(JsonParser parser) throws IOException {
    if (!parser.currentToken().isScalarValue()) {
      parser.skipChildren();
      return null;
    }

    final String text = parser.getValueAsString();
    return text == null || text.isEmpty() ? null : text;
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
