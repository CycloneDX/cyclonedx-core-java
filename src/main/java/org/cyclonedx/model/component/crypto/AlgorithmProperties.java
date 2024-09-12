package org.cyclonedx.model.component.crypto;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import org.cyclonedx.model.component.crypto.enums.CertificationLevel;
import org.cyclonedx.model.component.crypto.enums.CryptoFunction;
import org.cyclonedx.model.component.crypto.enums.ExecutionEnvironment;
import org.cyclonedx.model.component.crypto.enums.ImplementationPlatform;
import org.cyclonedx.model.component.crypto.enums.Mode;
import org.cyclonedx.model.component.crypto.enums.Padding;
import org.cyclonedx.model.component.crypto.enums.Primitive;
import org.cyclonedx.util.deserializer.CertificationLevelDeserializer;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder({
    "primitive",
    "parameterSetIdentifier",
    "curve",
    "executionEnvironment",
    "implementationPlatform",
    "certificationLevel",
    "mode",
    "padding",
    "cryptoFunctions",
    "classicalSecurityLevel", "nistQuantumSecurityLevel"
})
public class AlgorithmProperties
{
  private Primitive primitive;

  private String parameterSetIdentifier;

  private String curve;

  private ExecutionEnvironment executionEnvironment;

  private ImplementationPlatform implementationPlatform;

  @JsonDeserialize(using = CertificationLevelDeserializer.class)
  private CertificationLevel certificationLevel;

  private Mode mode;

  private Padding padding;

  private List<CryptoFunction> cryptoFunctions;

  private Integer classicalSecurityLevel;

  private Integer nistQuantumSecurityLevel;

  public Primitive getPrimitive() {
    return primitive;
  }

  public void setPrimitive(final Primitive primitive) {
    this.primitive = primitive;
  }

  public String getParameterSetIdentifier() {
    return parameterSetIdentifier;
  }

  public void setParameterSetIdentifier(final String parameterSetIdentifier) {
    this.parameterSetIdentifier = parameterSetIdentifier;
  }

  public String getCurve() {
    return curve;
  }

  public void setCurve(final String curve) {
    this.curve = curve;
  }

  public ExecutionEnvironment getExecutionEnvironment() {
    return executionEnvironment;
  }

  public void setExecutionEnvironment(final ExecutionEnvironment executionEnvironment) {
    this.executionEnvironment = executionEnvironment;
  }

  public ImplementationPlatform getImplementationPlatform() {
    return implementationPlatform;
  }

  public void setImplementationPlatform(final ImplementationPlatform implementationPlatform) {
    this.implementationPlatform = implementationPlatform;
  }

  public CertificationLevel getCertificationLevel() {
    return certificationLevel;
  }

  public void setCertificationLevel(final CertificationLevel certificationLevel) {
    this.certificationLevel = certificationLevel;
  }

  public Mode getMode() {
    return mode;
  }

  public void setMode(final Mode mode) {
    this.mode = mode;
  }

  public Padding getPadding() {
    return padding;
  }

  public void setPadding(final Padding padding) {
    this.padding = padding;
  }

  @JacksonXmlElementWrapper(localName = "cryptoFunctions")
  @JacksonXmlProperty(localName = "cryptoFunction")
  public List<CryptoFunction> getCryptoFunctions() {
    return cryptoFunctions;
  }

  public void setCryptoFunctions(final List<CryptoFunction> cryptoFunctions) {
    this.cryptoFunctions = cryptoFunctions;
  }

  public Integer getClassicalSecurityLevel() {
    return classicalSecurityLevel;
  }

  public void setClassicalSecurityLevel(final Integer classicalSecurityLevel) {
    this.classicalSecurityLevel = classicalSecurityLevel;
  }

  public Integer getNistQuantumSecurityLevel() {
    return nistQuantumSecurityLevel;
  }

  public void setNistQuantumSecurityLevel(final Integer nistQuantumSecurityLevel) {
    this.nistQuantumSecurityLevel = nistQuantumSecurityLevel;
  }

  @Override
  public boolean equals(final Object object) {
    if (this == object) {
      return true;
    }
    if (!(object instanceof AlgorithmProperties)) {
      return false;
    }
    AlgorithmProperties that = (AlgorithmProperties) object;
    return primitive == that.primitive && Objects.equals(parameterSetIdentifier, that.parameterSetIdentifier) &&
        Objects.equals(curve, that.curve) && executionEnvironment == that.executionEnvironment &&
        implementationPlatform == that.implementationPlatform && certificationLevel == that.certificationLevel &&
        mode == that.mode && padding == that.padding && Objects.equals(cryptoFunctions, that.cryptoFunctions) &&
        Objects.equals(classicalSecurityLevel, that.classicalSecurityLevel) &&
        Objects.equals(nistQuantumSecurityLevel, that.nistQuantumSecurityLevel);
  }

  @Override
  public int hashCode() {
    return Objects.hash(primitive, parameterSetIdentifier, curve, executionEnvironment, implementationPlatform,
        certificationLevel, mode, padding, cryptoFunctions, classicalSecurityLevel, nistQuantumSecurityLevel);
  }
}
