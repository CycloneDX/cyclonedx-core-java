package org.cyclonedx.util.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import org.cyclonedx.Version;
import org.cyclonedx.model.PatentItem;

import javax.xml.namespace.QName;
import java.io.IOException;

import static org.cyclonedx.util.serializer.SerializerUtils.shouldSerializeField;

/**
 * Serializes a PatentItem by flattening — writing the inner Patent or PatentFamily
 * object directly. For XML, sets the correct element name.
 */
public class PatentItemSerializer extends JsonSerializer<PatentItem> {

    private final Version version;

    public PatentItemSerializer() {
        this(null);
    }

    public PatentItemSerializer(Version version) {
        this.version = version;
    }

    @Override
    public void serialize(PatentItem value, JsonGenerator gen, SerializerProvider provider)
        throws IOException
    {
        if (gen instanceof ToXmlGenerator) {
            serializeXml(value, (ToXmlGenerator) gen, provider);
        } else {
            serializeJson(value, gen, provider);
        }
    }

    private void serializeJson(PatentItem value, JsonGenerator gen, SerializerProvider provider)
        throws IOException
    {
        if (value.getPatent() != null && shouldSerializeField(value, version, "patent")) {
            provider.defaultSerializeValue(value.getPatent(), gen);
        } else if (value.getPatentFamily() != null && shouldSerializeField(value, version, "patentFamily")) {
            provider.defaultSerializeValue(value.getPatentFamily(), gen);
        } else {
            gen.writeNull();
        }
    }

    private void serializeXml(PatentItem value, ToXmlGenerator gen, SerializerProvider provider)
        throws IOException
    {
        if (value.getPatentFamily() != null && shouldSerializeField(value, version, "patentFamily")) {
            gen.setNextName(new QName("patentFamily"));
            provider.defaultSerializeValue(value.getPatentFamily(), gen);
        } else if (value.getPatent() != null && shouldSerializeField(value, version, "patent")) {
            // patent is the default element name from @JacksonXmlProperty
            provider.defaultSerializeValue(value.getPatent(), gen);
        } else {
            gen.writeNull();
        }
    }
}
