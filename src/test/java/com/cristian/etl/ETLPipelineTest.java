package com.cristian.etl;

import org.junit.jupiter.api.Test;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class ETLPipelineTest {

    @Test
    public void testProcessCsv() throws Exception {
        Path tmpIn = Files.createTempFile("test-input", ".csv");
        Path tmpOut = Files.createTempFile("test-output", ".csv");

        String csv = "name,email,country\nAlice,ALICE@example.com,us\nBob,bob@EXAMPLE.com,uk\n";
        Files.writeString(tmpIn, csv);

        ETLPipeline pipeline = new ETLPipeline(new TransformationService());
        List<String[]> out = pipeline.processCsv(tmpIn, tmpOut);
        assertEquals(2, out.size());
        assertTrue(Files.size(tmpOut) > 0);
    }
}
