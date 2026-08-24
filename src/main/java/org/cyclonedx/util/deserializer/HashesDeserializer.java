package org.cyclonedx.util.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.cyclonedx.model.Hash;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HashesDeserializer
    extends JsonDeserializer<List<Hash>>
{
  @Override
  public List<Hash> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
    if (p.currentToken() == JsonToken.START_ARRAY) {
      return readHashArray(p);
    }
    if (p.currentToken() == JsonToken.START_OBJECT) {
      return readHashesElement(p);
    }

    return new ArrayList<>();
  }

  /**
   * Reads hashes from a JSON array:
   * <pre>{@code
   *   [
   *     { "alg": "...", "content": "..." },
   *     { "alg": "...", "content": "..." }
   *   ]
   * }</pre>
   */
  private List<Hash> readHashArray(JsonParser p) throws IOException {
    List<Hash> hashes = new ArrayList<>();

    while (p.nextToken() != JsonToken.END_ARRAY) {
      Hash hash = readHash(p);
      if (hash != null) {
        hashes.add(hash);
      }
    }

    return hashes;
  }

  /**
   * Reads hashes from an XML {@code <hashes>} element:
   * <pre>{@code
   *   <hashes>
   *     <hash alg="...">...</hash>
   *     <hash alg="...">...</hash>
   *   </hashes>
   * }</pre>
   * <p>
   * Note that a deserializer higher up the chain may have materialized the tree already,
   * in which case the structure handed to this method looks like this:
   * <pre>{@code
   *   {
   *     "hash": [
   *       { "alg": "...", "": "..." },
   *       { "alg": "...", "": "..." }
   *     ]
   *   }
   * }</pre>
   */
  private List<Hash> readHashesElement(JsonParser p) throws IOException {
    List<Hash> hashes = new ArrayList<>();

    while (p.nextToken() == JsonToken.FIELD_NAME) {
      final String fieldName = p.currentName();
      p.nextToken();

      if (!"hash".equals(fieldName)) {
        p.skipChildren();
        continue;
      }

      if (p.currentToken() == JsonToken.START_ARRAY) {
        hashes.addAll(readHashArray(p));
      }
      else {
        Hash hash = readHash(p);
        if (hash != null) {
          hashes.add(hash);
        }
      }
    }

    return hashes;
  }

  private Hash readHash(JsonParser p) throws IOException {
    if (p.currentToken() != JsonToken.START_OBJECT) {
      p.skipChildren();
      return null;
    }

    String algorithm = null;
    String value = null;
    boolean hasKnownField = false;

    while (p.nextToken() == JsonToken.FIELD_NAME) {
      final String fieldName = p.currentName();
      final JsonToken valueToken = p.nextToken();

      // None of the known fields have structured values.
      if (!valueToken.isScalarValue()) {
        p.skipChildren();
        continue;
      }

      switch (fieldName) {
        case "alg":
          algorithm = p.getValueAsString();
          break;
        // NB: For XML, content is the element's text, which gets translated
        // to a field with empty name.
        case "content":
        case "":
          value = p.getValueAsString();
          break;
        default:
          continue;
      }

      hasKnownField = true;
    }

    return hasKnownField ? new Hash(algorithm, value) : null;
  }
}
