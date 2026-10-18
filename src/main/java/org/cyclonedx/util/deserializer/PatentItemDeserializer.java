package org.cyclonedx.util.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import org.cyclonedx.model.Patent;
import org.cyclonedx.model.PatentFamily;
import org.cyclonedx.model.PatentItem;

import java.io.IOException;
import java.util.Set;

/**
 * Deserializes a JSON object that could be either a Patent or a PatentFamily.
 * Discrimination heuristic: if the object contains "familyId" or "members", it's a PatentFamily.
 */
public class PatentItemDeserializer extends JsonDeserializer<PatentItem> {

    @Override
    public boolean isCachable() {
        return true;
    }

    @Override
    public PatentItem deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        final TokenBuffer buffer = ctxt.bufferAsCopyOfValue(p);
        final Set<String> fieldNames = DeserializerUtils.fieldNames(buffer);

        try (final JsonParser itemParser = buffer.asParserOnFirstToken()) {
            if (fieldNames.contains("familyId") || fieldNames.contains("members")) {
                return PatentItem.ofPatentFamily(ctxt.readValue(itemParser, PatentFamily.class));
            }

            return PatentItem.ofPatent(ctxt.readValue(itemParser, Patent.class));
        }
    }
}
