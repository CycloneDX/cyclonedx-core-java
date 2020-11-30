package org.cyclonedx.generators.xml;

import com.sun.xml.internal.bind.marshaller.NamespacePrefixMapper;

public class SbomNamespaceMapper extends NamespacePrefixMapper
{
  private static final String VULN_URI = "http://cyclonedx.org/schema/ext/vulnerability/1.0";
  private static final String VULN_PREFIX = "v";

  @Override
  public String getPreferredPrefix(final String namespaceUri, final String suggestion, final boolean requirePrefix) {
    if (VULN_URI.equals(namespaceUri)) {
      return VULN_PREFIX;
    }

    return suggestion;
  }
}
