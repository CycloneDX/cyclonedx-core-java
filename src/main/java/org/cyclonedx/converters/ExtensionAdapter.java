package org.cyclonedx.converters;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.cyclonedx.model.ExtensibleType;
import org.cyclonedx.model.Extension;
import org.cyclonedx.model.Extension.ExtensionType;
import org.cyclonedx.model.vulnerability.Vulnerability1_0;

public class ExtensionAdapter
    extends TypeAdapter<Map<String, Extension>>
{
  @Override
  public void write(
      final JsonWriter jsonWriter, final Map<String, Extension> stringExtensionMap) throws IOException
  {

  }

  @Override
  public Map<String, Extension> read(final JsonReader jsonReader) {
    if (jsonReader.getPath().contains(ExtensionType.VULNERABILITIES.getTypeName())) {
      Type listType = new TypeToken<ArrayList<Vulnerability1_0>>(){}.getType();
      List<? extends ExtensibleType> vulns = new Gson().fromJson(jsonReader, listType);

      final HashMap<String, Extension> extensions = new HashMap<>();
      Extension vulnerabilities = new Extension(ExtensionType.VULNERABILITIES, (List<ExtensibleType>) vulns);
      extensions.put("vulnerabilities", vulnerabilities);
      return extensions;
    }
    return null;
  }
}
