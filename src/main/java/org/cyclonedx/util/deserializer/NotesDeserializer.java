package org.cyclonedx.util.deserializer;

import org.cyclonedx.model.ReleaseNotes.Notes;

public class NotesDeserializer extends WrappedListDeserializer<Notes> {

    public NotesDeserializer() {
        super("note", Notes.class);
    }
}
