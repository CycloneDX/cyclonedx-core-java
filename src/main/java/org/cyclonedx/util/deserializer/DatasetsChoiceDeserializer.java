package org.cyclonedx.util.deserializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.cyclonedx.model.component.data.ComponentData;
import org.cyclonedx.model.component.data.DatasetChoice;

public class DatasetsChoiceDeserializer
    extends JsonDeserializer<DatasetChoice>
{
  @Override
  public DatasetChoice deserialize(JsonParser parser, DeserializationContext ctxt) throws IOException {
    final DatasetChoice datasetChoice = new DatasetChoice();

    if (parser.getCodec() instanceof XmlMapper) {
      // References are <ref> elements next to <dataset> elements, and arrive here as text.
      if (parser.currentToken().isScalarValue()) {
        datasetChoice.setRef(parser.getValueAsString());
      } else {
        datasetChoice.setComponentData(ctxt.readValue(parser, ComponentData.class));
      }

      return datasetChoice;
    }

    // For JSON, references are objects with a "ref" field.
    final TokenBuffer buffer = ctxt.bufferAsCopyOfValue(parser);
    try (final JsonParser refParser = DeserializerUtils.fieldValue(buffer, "ref")) {
      if (refParser != null) {
        datasetChoice.setRef(DeserializerUtils.readText(refParser));
        return datasetChoice;
      }
    }

    try (final JsonParser dataParser = buffer.asParserOnFirstToken()) {
      datasetChoice.setComponentData(ctxt.readValue(dataParser, ComponentData.class));
    }

    return datasetChoice;
  }
}
