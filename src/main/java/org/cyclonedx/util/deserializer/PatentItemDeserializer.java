package org.cyclonedx.util.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.cyclonedx.model.Patent;
import org.cyclonedx.model.PatentFamily;
import org.cyclonedx.model.PatentItem;

import java.io.IOException;

/**
 * Deserializes a JSON object that could be either a Patent or a PatentFamily.
 * Discrimination heuristic: if the object contains "familyId" or "members", it's a PatentFamily.
 */
public class PatentItemDeserializer extends JsonDeserializer<PatentItem> {

    @Override
    public PatentItem deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.readValueAsTree();
        ObjectMapper mapper = (ObjectMapper) p.getCodec();

        if (node.has("familyId") || node.has("members")) {
            PatentFamily pf = mapper.treeToValue(node, PatentFamily.class);
            return PatentItem.ofPatentFamily(pf);
        } else {
            Patent patent = mapper.treeToValue(node, Patent.class);
            return PatentItem.ofPatent(patent);
        }
    }
}
