package org.cyclonedx.util.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
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
 * For JSON: reads the array, see {@link PatentItemDeserializer}.
 * For XML: discriminates by the "patent" and "patentFamily" element names.
 */
public class PatentsDeserializer extends JsonDeserializer<List<PatentItem>> {

    @Override
    public List<PatentItem> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        final List<PatentItem> items;
        if (p instanceof FromXmlParser) {
            items = deserializeXml(p, ctxt);
        } else if (p.currentToken() == JsonToken.START_ARRAY) {
            items = DeserializerUtils.readList(p, ctxt, PatentItem.class);
        } else {
            p.skipChildren();
            items = null;
        }
        return items == null || items.isEmpty() ? null : items;
    }

    private List<PatentItem> deserializeXml(JsonParser p, DeserializationContext ctxt) throws IOException {
        final List<PatentItem> items = new ArrayList<>();
        if (p.currentToken() != JsonToken.START_OBJECT) {
            p.skipChildren();
            return items;
        }

        for (JsonToken token = p.nextToken(); token == JsonToken.FIELD_NAME; token = p.nextToken()) {
            final String fieldName = p.currentName();
            p.nextToken();

            if ("patent".equals(fieldName)) {
                final List<Patent> patents = new ArrayList<>();
                DeserializerUtils.readItemsInto(p, ctxt, Patent.class, patents);
                patents.forEach(patent -> items.add(PatentItem.ofPatent(patent)));
            } else if ("patentFamily".equals(fieldName)) {
                final List<PatentFamily> families = new ArrayList<>();
                DeserializerUtils.readItemsInto(p, ctxt, PatentFamily.class, families);
                families.forEach(family -> items.add(PatentItem.ofPatentFamily(family)));
            } else {
                p.skipChildren();
            }
        }

        return items;
    }
}
