package org.cyclonedx;

import org.apache.commons.io.IOUtils;
import org.cyclonedx.generators.json.BomJsonGenerator;
import org.cyclonedx.generators.json.BomJsonGenerator12;
import org.cyclonedx.model.Bom;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class BomJsonGeneratorTest {

    private File tempFile;

    @Before
    public void before() throws IOException {
        Path path = Files.createTempDirectory("cyclonedx-core-java");
        this.tempFile = new File(path.toFile(), "bom.json");
    }

    @After
    public void after() {
        tempFile.delete();
        tempFile.getParentFile().delete();
    }

    @Test
    public void schema12GenerationTest() throws Exception {
        Bom bom =  createCommonBom("/bom-1.2.xml");
        BomJsonGenerator generator = BomGeneratorFactory.createJson(CycloneDxSchema.Version.VERSION_12, bom);
        generator.generate();
        Assert.assertTrue(generator instanceof BomJsonGenerator12);
        Assert.assertEquals(CycloneDxSchema.Version.VERSION_12, generator.getSchemaVersion());
        File file = writeToFile(generator.toJsonString());
        BomParser parser = new BomParser();
        Assert.assertTrue(parser.isValidJson(file, CycloneDxSchema.Version.VERSION_12, true));
    }

    private File writeToFile(String jsonString) throws Exception {
        try (FileWriter writer = new FileWriter(tempFile.getAbsolutePath())) {
            writer.write(jsonString);
        }
        return tempFile;
    }

    private Bom createCommonBom(String resource) throws Exception {
        final byte[] bomBytes = IOUtils.toByteArray(this.getClass().getResourceAsStream(resource));
        BomParser parser = new BomParser();
        return parser.parse(bomBytes);
    }
}
