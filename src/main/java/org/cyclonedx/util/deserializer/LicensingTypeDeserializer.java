package org.cyclonedx.util.deserializer;

import org.cyclonedx.model.Licensing.LicensingType;

public class LicensingTypeDeserializer extends WrappedListDeserializer<LicensingType> {

    public LicensingTypeDeserializer() {
        super("licenseType", LicensingType.class);
    }
}
