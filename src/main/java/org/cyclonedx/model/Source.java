package org.cyclonedx.model;

import java.net.URL;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class Source {
  @JacksonXmlProperty(localName = "name", isAttribute = true)
  private String name;
  private URL url;

  public URL getUrl() {
    return url;
  }

  public void setUrl(final URL url) {
    this.url = url;
  }

  public String getName() {
    return name;
  }

  public void setName(final String name) {
    this.name = name;
  }
}