package org.cyclonedx.model.component.crypto;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import org.cyclonedx.Version;
import org.cyclonedx.model.VersionFilter;
import org.cyclonedx.model.component.crypto.enums.CertificationLevel;
import org.cyclonedx.model.component.crypto.enums.CryptoFunction;
import org.cyclonedx.model.component.crypto.enums.ExecutionEnvironment;
import org.cyclonedx.model.component.crypto.enums.ImplementationPlatform;
import org.cyclonedx.model.component.crypto.enums.Mode;
import org.cyclonedx.model.component.crypto.enums.Padding;
import org.cyclonedx.model.component.crypto.enums.Primitive;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder({
    "primitive",
    "algorithmFamily",
    "parameterSetIdentifier",
    "curve",
    "ellipticCurve",
    "executionEnvironment",
    "implementationPlatform",
    "certificationLevel",
    "mode",
    "padding",
    "cryptoFunctions",
    "classicalSecurityLevel",
    "nistQuantumSecurityLevel",
    "relatedCryptographicAssets"
})
public class AlgorithmProperties
{
  private Primitive primitive;

  private String parameterSetIdentifier;

  @VersionFilter(Version.VERSION_17)
  private String algorithmFamily;

  @VersionFilter(Version.VERSION_17)
  private String ellipticCurve;

  private String curve;

  private ExecutionEnvironment executionEnvironment;

  private ImplementationPlatform implementationPlatform;

  private List<CertificationLevel> certificationLevel;

  private Mode mode;

  private Padding padding;

  private List<CryptoFunction> cryptoFunctions;

  private Integer classicalSecurityLevel;

  private Integer nistQuantumSecurityLevel;

  @VersionFilter(Version.VERSION_17)
  private List<RelatedCryptographicAsset> relatedCryptographicAssets;

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

  public String getAlgorithmFamily() {
    return algorithmFamily;
  }

  public void setAlgorithmFamily(final String algorithmFamily) {
    this.algorithmFamily = algorithmFamily;
  }

  public String getEllipticCurve() {
    return ellipticCurve;
  }

  public void setEllipticCurve(final String ellipticCurve) {
    this.ellipticCurve = ellipticCurve;
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

  @JacksonXmlElementWrapper(useWrapping = false)
  @JacksonXmlProperty(localName = "certificationLevel")
  @JsonProperty("certificationLevel")
  public List<CertificationLevel> getCertificationLevel() {
    return certificationLevel;
  }

  public void setCertificationLevel(final List<CertificationLevel> certificationLevel) {
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

  @JacksonXmlElementWrapper(localName = "relatedCryptographicAssets")
  @JacksonXmlProperty(localName = "relatedCryptographicAsset")
  public List<RelatedCryptographicAsset> getRelatedCryptographicAssets() {
    return relatedCryptographicAssets;
  }

  public void setRelatedCryptographicAssets(final List<RelatedCryptographicAsset> relatedCryptographicAssets) {
    this.relatedCryptographicAssets = relatedCryptographicAssets;
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
        Objects.equals(algorithmFamily, that.algorithmFamily) && Objects.equals(ellipticCurve, that.ellipticCurve) &&
        Objects.equals(curve, that.curve) && executionEnvironment == that.executionEnvironment &&
        implementationPlatform == that.implementationPlatform && certificationLevel == that.certificationLevel &&
        mode == that.mode && padding == that.padding && Objects.equals(cryptoFunctions, that.cryptoFunctions) &&
        Objects.equals(classicalSecurityLevel, that.classicalSecurityLevel) &&
        Objects.equals(nistQuantumSecurityLevel, that.nistQuantumSecurityLevel) &&
        Objects.equals(relatedCryptographicAssets, that.relatedCryptographicAssets);
  }

  @Override
  public int hashCode() {
    return Objects.hash(primitive, parameterSetIdentifier, algorithmFamily, ellipticCurve, curve, executionEnvironment,
        implementationPlatform, certificationLevel, mode, padding, cryptoFunctions, classicalSecurityLevel,
        nistQuantumSecurityLevel, relatedCryptographicAssets);
  }
}
