package org.cyclonedx.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.cyclonedx.util.deserializer.PatentItemDeserializer;
import org.cyclonedx.util.serializer.PatentItemSerializer;

import java.util.Objects;

/**
 * Discriminated union for the polymorphic patents array in definitions.
 * Can hold either a {@link Patent} or a {@link PatentFamily}.
 *
 * @since 10.0.0
 */
@JsonDeserialize(using = PatentItemDeserializer.class)
@JsonSerialize(using = PatentItemSerializer.class)
public class PatentItem {

    public enum Type {
        PATENT,
        PATENT_FAMILY
    }

    private Patent patent;
    private PatentFamily patentFamily;

    private PatentItem() {
    }

    public static PatentItem ofPatent(Patent patent) {
        PatentItem item = new PatentItem();
        item.patent = patent;
        return item;
    }

    public static PatentItem ofPatentFamily(PatentFamily patentFamily) {
        PatentItem item = new PatentItem();
        item.patentFamily = patentFamily;
        return item;
    }

    public Patent getPatent() {
        return patent;
    }

    public PatentFamily getPatentFamily() {
        return patentFamily;
    }

    @JsonIgnore
    public Type getType() {
        if (patent != null) return Type.PATENT;
        if (patentFamily != null) return Type.PATENT_FAMILY;
        return null;
    }

    @JsonIgnore
    public boolean isValid() {
        return patent != null || patentFamily != null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PatentItem)) return false;
        PatentItem that = (PatentItem) o;
        return Objects.equals(patent, that.patent) &&
            Objects.equals(patentFamily, that.patentFamily);
    }

    @Override
    public int hashCode() {
        return Objects.hash(patent, patentFamily);
    }
}
