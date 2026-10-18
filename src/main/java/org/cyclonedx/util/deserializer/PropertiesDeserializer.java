package org.cyclonedx.util.deserializer;

import org.cyclonedx.model.Property;

public class PropertiesDeserializer extends WrappedListDeserializer<Property> {

    public PropertiesDeserializer() {
        super("property", Property.class);
    }
}
