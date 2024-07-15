package org.cyclonedx;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.apache.commons.io.IOUtils;
import org.cyclonedx.generators.BomGeneratorFactory;
import org.cyclonedx.exception.GeneratorException;
import org.cyclonedx.generators.json.BomJsonGenerator;
import org.cyclonedx.generators.xml.BomXmlGenerator;
import org.cyclonedx.model.Bom;
import org.cyclonedx.model.Component;
import org.cyclonedx.model.ExternalReference;
import org.cyclonedx.model.Hash;
import org.cyclonedx.parsers.JsonParser;
import org.cyclonedx.parsers.Parser;
import org.cyclonedx.parsers.XmlParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;


public class Issue214RegressionTest
{
    static Stream<Arguments> testData() {
        return Stream.of(
            Arguments.of(Version.VERSION_16),
            Arguments.of(Version.VERSION_15),
            Arguments.of(Version.VERSION_14),
            Arguments.of(Version.VERSION_13)
        );
    }

    @ParameterizedTest
    @MethodSource("testData")
    public void testObjectGeneration(Version version) throws IOException, ReflectiveOperationException, GeneratorException {
        performJsonTest(version);
        performXmlTest(version);
    }

    private void performXmlTest(final Version pSpecVersion)
        throws GeneratorException, ReflectiveOperationException, IOException
    {
        final Bom inputBom = createIssue214Bom();
        BomXmlGenerator generator = BomGeneratorFactory.createXml(pSpecVersion, inputBom);

        Assertions.assertTrue(BomXmlGenerator.class.isAssignableFrom(generator.getClass()));
        Assertions.assertEquals(pSpecVersion, generator.getSchemaVersion());

        final String actual = generator.toXmlString();
        final String expected = readFixture("/regression/issue214-expected-output.xml", pSpecVersion);
        Assertions.assertEquals(expected, actual);
        validate(actual, XmlParser.class, pSpecVersion);
    }

    private void performJsonTest(final Version pSpecVersion)
            throws IOException, ReflectiveOperationException, GeneratorException {
        final Bom inputBom = createIssue214Bom();
        BomJsonGenerator generator = BomGeneratorFactory.createJson(pSpecVersion, inputBom);

        Assertions.assertTrue(BomJsonGenerator.class.isAssignableFrom(generator.getClass()));
        Assertions.assertEquals(pSpecVersion, generator.getSchemaVersion());

        final String actual = generator.toJsonString().trim();
        final String expected = readFixture("/regression/issue214-expected-output.json", pSpecVersion);
        Assertions.assertEquals(expected, actual);
        validate(actual, JsonParser.class, pSpecVersion);
    }

    private String readFixture(final String pPath, final Version pSpecVersion)
    {
        try (InputStream is = getClass().getResourceAsStream(pPath)) {
            if (is != null) {
                String result = IOUtils.toString(is, StandardCharsets.UTF_8);
                result = result.replaceAll(Pattern.quote("${specVersion}"), pSpecVersion.getVersionString());
                return result;
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
        final Version pSpecVersion)
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
