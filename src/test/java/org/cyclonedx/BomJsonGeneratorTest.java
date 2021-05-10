package org.cyclonedx;

import org.apache.commons.io.IOUtils;
import org.cyclonedx.CycloneDxSchema.Version;
import org.cyclonedx.generators.json.BomJsonGenerator;
import org.cyclonedx.generators.json.BomJsonGenerator12;
import org.cyclonedx.model.Bom;
import org.cyclonedx.parsers.JsonParser;
import org.cyclonedx.parsers.XmlParser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.json.JsonObject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BomJsonGeneratorTest {

    private File tempFile;

    @BeforeEach
    public void before() throws IOException {
        Path path = Files.createTempDirectory("cyclonedx-core-java");
        this.tempFile = new File(path.toFile(), "bom.json");
    }

    @AfterEach
    public void after() {
        tempFile.delete();
        tempFile.getParentFile().delete();
    }

    @Test
    public void schema12GenerationTest() throws Exception {
        Bom bom =  createCommonBom("/bom-1.2.xml");
        BomJsonGenerator generator = BomGeneratorFactory.createJson(Version.VERSION_12, bom);
        assertTrue(generator instanceof BomJsonGenerator12);
        assertEquals(CycloneDxSchema.Version.VERSION_12, generator.getSchemaVersion());
        File file = writeToFile(generator.toJsonString());
        JsonParser parser = new JsonParser();
        assertTrue(parser.isValid(file, CycloneDxSchema.Version.VERSION_12, true));
    }

    @Test
    public void schema12JsonObjectGenerationTest() throws Exception {
        Bom bom = createCommonBom("/bom-1.2.xml");
        BomJsonGenerator generator = BomGeneratorFactory.createJson(Version.VERSION_12, bom);
        JsonObject obj = generator.toJsonObject();
        assertNotNull(obj);
        assertEquals("CycloneDX", obj.getString("bomFormat"));
        assertEquals("1.2", obj.getString("specVersion"));
        assertEquals("urn:uuid:3e671687-395b-41f5-a30f-a58921a69b79", obj.getString("serialNumber"));
        assertEquals(1, obj.getInt("version"));
        assertEquals(6, obj.getJsonObject("metadata").size());
        assertEquals(3, obj.getJsonArray("components").size());
    }

    private File writeToFile(String jsonString) throws Exception {
        try (FileWriter writer = new FileWriter(tempFile.getAbsolutePath())) {
            writer.write(jsonString);
        }
        return tempFile;
    }

    private Bom createCommonBom(String resource) throws Exception {
        final byte[] bomBytes = IOUtils.toByteArray(this.getClass().getResourceAsStream(resource));
        XmlParser parser = new XmlParser();
        return parser.parse(bomBytes);
    }
}
