/*
 * This file is part of CycloneDX Core (Java).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (c) OWASP Foundation. All Rights Reserved.
 */
package org.cyclonedx.util.deserializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import org.cyclonedx.model.AttachmentText;
import org.cyclonedx.model.formulation.common.OutputType;
import org.cyclonedx.model.formulation.common.OutputType.OutputTypeEnum;

public class OutputTypeDeserializer
    extends AbstractDataTypeDeserializer<OutputType> {

  @Override
  public OutputType deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
      throws IOException
  {
    JsonNode node = jsonParser.getCodec().readTree(jsonParser);
    OutputType outputType = new OutputType();

    setSourceAndTarget(node, outputType);
    createOutputDataInfo(node, outputType, deserializationContext, jsonParser);
    setProperties(node, outputType);

    if(node.has("type")) {
      JsonNode typeNode = node.get("type");
      OutputTypeEnum type = objectMapper.treeToValue(typeNode, OutputTypeEnum.class);
      outputType.setType(type);
    }

    return outputType;
  }

  private void createOutputDataInfo(JsonNode node, OutputType outputType, DeserializationContext ctxt, JsonParser jsonParser)
      throws IOException
  {
    if (node.has("resource")) {
      setResource(node, outputType);
    }
    else if (node.has("environmentVars")) {
      setEnvironmentVars(node, outputType, jsonParser, ctxt);
    }
    else if (node.has("data")) {
      JsonNode dataNode = node.get("data");
      AttachmentText data = objectMapper.treeToValue(dataNode, AttachmentText.class);
      outputType.setData(data);
    }
  }
}
