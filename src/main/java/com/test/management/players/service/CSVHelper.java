package com.test.management.players.service;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.*;

import com.test.management.players.controller.PlayerCSV;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.QuoteMode;

public class CSVHelper {
    public static List<PlayerCSV> csvToTutorials(InputStream is) {
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
             CSVParser csvParser = new CSVParser(fileReader,
                     CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());) {
            List<PlayerCSV> playerCSVS = new ArrayList<>();
            Iterable<CSVRecord> csvRecords = csvParser.getRecords();
            for (CSVRecord csvRecord : csvRecords) {
                try {
                    PlayerCSV tutorial = new PlayerCSV(Long.parseLong(csvRecord.get("Id")), csvRecord.get("nickName"));
                    playerCSVS.add(tutorial);
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
            }
            return playerCSVS;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse CSV file: " + e.getMessage());
        }
    }

    public static ByteArrayInputStream tutorialsToCSV(List<Map> tutorials) {
        final CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.MINIMAL);
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(out), format)) {
            csvPrinter.printRecord(Arrays.asList("id", "first_name", "last_name"));
            for (Map tutorial : tutorials) {
                List<Object> data = Arrays.asList(
                        tutorial.containsKey("id") ? String.valueOf(tutorial.get("id")) : "",
                        tutorial.containsKey("first_name") ? tutorial.get("first_name") : "",
                        tutorial.containsKey("last_name") ? tutorial.get("last_name") : "");
                csvPrinter.printRecord(data);
                //add all other data
            }
            csvPrinter.flush();
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("fail to import data to CSV file: " + e.getMessage());
        }
    }
}