package org.cyclonedx;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.regex.Pattern;
import javax.xml.XMLConstants;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.io.IOUtils;
import org.cyclonedx.generators.json.AbstractBomJsonGenerator;
import org.cyclonedx.generators.json.BomJsonGenerator;
import org.cyclonedx.generators.json.BomJsonGenerator13;
import org.cyclonedx.generators.json.BomJsonGenerator14;
import org.cyclonedx.generators.xml.AbstractBomXmlGenerator;
import org.cyclonedx.generators.xml.BomXmlGenerator;
import org.cyclonedx.generators.xml.BomXmlGenerator13;
import org.cyclonedx.generators.xml.BomXmlGenerator14;
import org.cyclonedx.generators.xml.BomXmlGenerator15;
import org.cyclonedx.model.Bom;
import org.cyclonedx.model.Component;
import org.cyclonedx.model.ExternalReference;
import org.cyclonedx.model.Hash;
import org.cyclonedx.parsers.JsonParser;
import org.cyclonedx.parsers.Parser;
import org.cyclonedx.parsers.XmlParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;


public class Issue214RegressionTest
{
    @Test
    public void schema13JsonObjectGenerationTest()
        throws IOException, ReflectiveOperationException
    {
        performJsonTest(CycloneDxSchema.Version.VERSION_13, BomJsonGenerator13.class);
    }

    @Test
    public void schema14JsonObjectGenerationTest()
        throws IOException, ReflectiveOperationException
    {
        performJsonTest(CycloneDxSchema.Version.VERSION_14, BomJsonGenerator14.class);
    }

    @Test
    public void schema13XmlObjectGenerationTest()
        throws ParserConfigurationException, IOException, ReflectiveOperationException
    {
        performXmlTest(CycloneDxSchema.Version.VERSION_13, BomXmlGenerator13.class);
    }

    @Test
    public void schema14XmlObjectGenerationTest()
        throws ParserConfigurationException, IOException, ReflectiveOperationException
    {
        performXmlTest(CycloneDxSchema.Version.VERSION_14, BomXmlGenerator14.class);
    }

    @Test
    public void schema15XmlObjectGenerationTest()
        throws ParserConfigurationException, IOException, ReflectiveOperationException
    {
        performXmlTest(CycloneDxSchema.Version.VERSION_15, BomXmlGenerator15.class);
    }

    private <G extends AbstractBomXmlGenerator> void performXmlTest(final CycloneDxSchema.Version pSpecVersion,
        final Class<G> pExpectedGeneratorClass)
        throws ParserConfigurationException, IOException, ReflectiveOperationException
    {
        final Bom inputBom = createIssue214Bom();
        BomXmlGenerator generator = BomGeneratorFactory.createXml(pSpecVersion, inputBom);
        Document doc = generator.generate();

        Assertions.assertTrue(pExpectedGeneratorClass.isAssignableFrom(generator.getClass()));
        Assertions.assertEquals(pSpecVersion, generator.getSchemaVersion());

        final String actual = xmlDocumentToString(doc);
        final String expected = readFixture("/regression/issue214-expected-output.xml", pSpecVersion);
        Assertions.assertEquals(expected, actual);
        validate(actual, XmlParser.class, pSpecVersion);
    }

    private <G extends AbstractBomJsonGenerator> void performJsonTest(final CycloneDxSchema.Version pSpecVersion,
        final Class<G> pExpectedGeneratorClass)
        throws IOException, ReflectiveOperationException
    {
        final Bom inputBom = createIssue214Bom();
        BomJsonGenerator generator = BomGeneratorFactory.createJson(pSpecVersion, inputBom);

        Assertions.assertTrue(pExpectedGeneratorClass.isAssignableFrom(generator.getClass()));
        Assertions.assertEquals(pSpecVersion, generator.getSchemaVersion());

        final String actual = generator.toJsonString().trim();
        final String expected = readFixture("/regression/issue214-expected-output.json", pSpecVersion);
        Assertions.assertEquals(expected, actual);
        validate(actual, JsonParser.class, pSpecVersion);
    }

    private String xmlDocumentToString(final Document doc)
    {
        Assertions.assertNotNull(doc);
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer;
        try {
            tf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            transformer = tf.newTransformer();

            transformer.setOutputProperty(OutputKeys.ENCODING, StandardCharsets.UTF_8.name());
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

            StringWriter sw = new StringWriter();
            transformer.transform(new DOMSource(doc), new StreamResult(sw));
            return sw.getBuffer().toString().trim();
        }
        catch (TransformerException ex) {
            Assertions.fail("Failed to serialize XML document", ex);
        }
        return null;
    }

    private String readFixture(final String pPath, final CycloneDxSchema.Version pSpecVersion)
    {
        try (InputStream is = getClass().getResourceAsStream(pPath)) {
            if (is != null) {
                String result = IOUtils.toString(is, StandardCharsets.UTF_8);
                result = result.replaceAll(Pattern.quote("${specVersion}"), pSpecVersion.getVersionString());
                return result.trim();
            }
            else {
                Assertions.fail("failed to read expected data file: " + pPath);
            }
        }
        catch (IOException e) {
            Assertions.fail("failed to read expected data file: " + pPath, e);
        }
        return null;
    }

    private Bom createIssue214Bom()
    {
        ExternalReference extRef = new ExternalReference();
        extRef.setType(ExternalReference.Type.BOM);
        extRef.setUrl("https://example.org/support/sbom/portal-server/1.0.0");
        extRef.setComment("An external SBOM that describes what this component includes");
        Hash md5 = new Hash(Hash.Algorithm.MD5, "2cd42512b65500dc7ba0ff13490b0b73");
        Hash sha1 = new Hash(Hash.Algorithm.SHA1, "226247b40160f2892fa4c7851b5b913d5d10912d");
        Hash sha256 = new Hash(Hash.Algorithm.SHA_256,
            "09a72795a920c1a9c0209cfb8395f8d97089832d249cba8c0938a3423b3ed1d1");
        extRef.setHashes(Arrays.asList(md5, sha1, sha256));

        Component component = new Component();
        component.setGroup("org.example");
        component.setName("mylibrary");
        component.setType(Component.Type.LIBRARY);
        component.setVersion("1.0.0");
        component.addExternalReference(extRef);

        Bom bom = new Bom();
        bom.addComponent(component);

        return bom;
    }

    private <P extends Parser> void validate(final String pDocument, final Class<P> pParserType,
        final CycloneDxSchema.Version pSpecVersion)
        throws IOException, ReflectiveOperationException
    {
        File tempFile = null;
        try {
            tempFile = writeToFile(pDocument, pParserType.isAssignableFrom(JsonParser.class) ? "json" : "xml");
            P parser = pParserType.newInstance();
            Assertions.assertTrue(parser.isValid(tempFile, pSpecVersion));
        }
        finally {
            if (tempFile != null) {
                //noinspection ResultOfMethodCallIgnored
                tempFile.delete();
            }
        }
    }

    private File writeToFile(final String pDocument, final String pSuffix)
        throws IOException
    {
        File tempFile = File.createTempFile("cyclonedx-core-java-test-", "." + pSuffix);
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write(pDocument);
        }
        return tempFile;
    }
}
