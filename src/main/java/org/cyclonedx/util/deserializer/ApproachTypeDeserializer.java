package org.cyclonedx.util.deserializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.cyclonedx.model.component.modelCard.ModelParameters.Approach.ApproachType;

public class ApproachTypeDeserializer extends JsonDeserializer<ApproachType>
{
  @Override
  public ApproachType deserialize(JsonParser jsonParser, DeserializationContext ctxt)
      throws IOException
  {
    String value = jsonParser.getText();
    return ApproachType.valueOf(value.toUpperCase());
  }
}
