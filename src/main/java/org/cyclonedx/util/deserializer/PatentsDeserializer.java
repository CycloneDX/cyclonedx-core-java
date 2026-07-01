package org.cyclonedx.util.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser;
import org.cyclonedx.model.Patent;
import org.cyclonedx.model.PatentFamily;
import org.cyclonedx.model.PatentItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Deserializes the polymorphic patents array which can contain
 * both Patent and PatentFamily objects.
 *
 * For JSON: reads the array and discriminates by presence of "familyId" or "members".
 * For XML: reads the tree and groups by "patent" and "patentFamily" element names.
 */
public class PatentsDeserializer extends JsonDeserializer<List<PatentItem>> {

    @Override
    public List<PatentItem> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        if (p instanceof FromXmlParser) {
            return deserializeXml(p, ctxt);
        }
        return deserializeJson(p, ctxt);
    }

    private List<PatentItem> deserializeJson(JsonParser p, DeserializationContext ctxt) throws IOException {
        List<PatentItem> items = new ArrayList<>();
        ObjectMapper mapper = (ObjectMapper) p.getCodec();

        if (p.currentToken() == JsonToken.START_ARRAY) {
            while (p.nextToken() != JsonToken.END_ARRAY) {
                JsonNode node = mapper.readTree(p);
                if (node.has("familyId") || node.has("members")) {
                    PatentFamily pf = mapper.treeToValue(node, PatentFamily.class);
                    items.add(PatentItem.ofPatentFamily(pf));
                } else {
                    Patent patent = mapper.treeToValue(node, Patent.class);
                    items.add(PatentItem.ofPatent(patent));
                }
            }
        }

        return items.isEmpty() ? null : items;
    }

    private List<PatentItem> deserializeXml(JsonParser p, DeserializationContext ctxt) throws IOException {
        List<PatentItem> items = new ArrayList<>();

        // For XML, we're inside the <patents> wrapper element.
        // Children can be <patent> or <patentFamily> elements.
        // Jackson XML presents them as field names within the current object.
        if (p.currentToken() == JsonToken.START_OBJECT) {
            while (p.nextToken() != JsonToken.END_OBJECT) {
                if (p.currentToken() == JsonToken.FIELD_NAME) {
                    String fieldName = p.currentName();
                    p.nextToken();

                    if ("patent".equals(fieldName)) {
                        if (p.currentToken() == JsonToken.START_ARRAY) {
                            while (p.nextToken() != JsonToken.END_ARRAY) {
                                Patent patent = ctxt.readValue(p, Patent.class);
                                items.add(PatentItem.ofPatent(patent));
                            }
                        } else {
                            Patent patent = ctxt.readValue(p, Patent.class);
                            items.add(PatentItem.ofPatent(patent));
                        }
                    } else if ("patentFamily".equals(fieldName)) {
                        if (p.currentToken() == JsonToken.START_ARRAY) {
                            while (p.nextToken() != JsonToken.END_ARRAY) {
                                PatentFamily pf = ctxt.readValue(p, PatentFamily.class);
                                items.add(PatentItem.ofPatentFamily(pf));
                            }
                        } else {
                            PatentFamily pf = ctxt.readValue(p, PatentFamily.class);
                            items.add(PatentItem.ofPatentFamily(pf));
                        }
                    } else {
                        p.skipChildren();
                    }
                }
            }
        }

        return items.isEmpty() ? null : items;
    }
}
