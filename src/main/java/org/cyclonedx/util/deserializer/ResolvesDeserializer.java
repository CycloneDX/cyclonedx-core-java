package org.cyclonedx.util.deserializer;

import org.cyclonedx.model.ReleaseNotes.Resolves;

public class ResolvesDeserializer extends WrappedListDeserializer<Resolves> {

    public ResolvesDeserializer() {
        super("issue", Resolves.class);
    }
}
