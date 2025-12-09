package com.cristian.etl;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ETLPipeline {
    private static final Logger log = LoggerFactory.getLogger(ETLPipeline.class);
    private final TransformationService transformer;

    public ETLPipeline(TransformationService transformer) {
        this.transformer = transformer;
    }

    public List<String[]> processCsv(Path inputCsv, Path outputCsv) throws IOException {
        List<String[]> out = new ArrayList<>();
        try (Reader reader = Files.newBufferedReader(inputCsv)) {
            Iterable<CSVRecord> records = CSVFormat.DEFAULT
                    .withFirstRecordAsHeader()
                    .parse(reader);

            for (CSVRecord r : records) {
                String name = transformer.clean(r.get("name"));
                String email = transformer.clean(r.get("email"));
                String country = transformer.clean(r.get("country"));

                String[] row = new String[] { name, email.toLowerCase(), country.toUpperCase() };
                out.add(row);
            }
        }

        // write output
        try (Writer writer = Files.newBufferedWriter(outputCsv);
             CSVPrinter printer = CSVFormat.DEFAULT.withHeader("name","email","country").print(writer)) {
            for (String[] row : out) {
                printer.printRecord((Object[]) row);
            }
        }
        log.info("ETL processed {} records -> {}", out.size(), outputCsv);
        return out;
    }
}
