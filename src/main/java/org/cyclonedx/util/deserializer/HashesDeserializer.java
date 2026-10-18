package org.cyclonedx.util.deserializer;

import org.cyclonedx.model.Hash;

public class HashesDeserializer extends WrappedListDeserializer<Hash> {

    public HashesDeserializer() {
        super("hash", Hash.class);
    }
}
