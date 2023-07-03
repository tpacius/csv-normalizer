package org.example;

import com.opencsv.bean.*;
import com.opencsv.exceptions.CsvConstraintViolationException;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

public class App 
{
    public static void main( String[] args )
    {

        //Ingest stdin
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        //Parse to defined CSVRecord bean
        CsvToBean<CSVRecord> csvReader = new CsvToBeanBuilder<CSVRecord>(br)
                .withType(CSVRecord.class)
                .withSeparator(',')
                .withIgnoreLeadingWhiteSpace(true)
                .withIgnoreEmptyLine(true)
                .build();

        List<CSVRecord> records = csvReader.parse();

        records.forEach(x -> {
            x.setTotalDuration(x.getBarDuration() + x.getFooDuration());
            System.out.println(x);
        });
    }

    @Data
    public class CSVRecord {
        @CsvCustomBindByName(column = "Timestamp", converter = ZonedDateTimeConverter.class)
        private ZonedDateTime timestamp;
        @CsvCustomBindByName(column = "ZIP", converter = StringConverter.class)
        private String zip;
        @CsvCustomBindByName(column = "FullName", converter = StringConverter.class)
        private String fullName;
        @CsvCustomBindByName(column = "Address", converter = StringConverter.class)
        private String address;
        @CsvCustomBindByName(column = "FooDuration", converter = TimeConverter.class)
        private int fooDuration;
        @CsvCustomBindByName(column = "BarDuration", converter = TimeConverter.class)
        private int barDuration;
        @CsvBindByName(column = "TotalDuration")
        private int totalDuration;
        @CsvBindByName(column = "Notes")
        private String notes;

        @Override
        public String toString() {
            return getTimestamp().toString() + "," + getZip() + "," + getFullName() + "," + getAddress() + "," + getFooDuration() + "," + getBarDuration() + "," + getTotalDuration() + "," + getNotes();
        }
    }

    @NoArgsConstructor
    public class StringConverter extends AbstractBeanField {

        @Override
        protected Object convert(String value) throws CsvDataTypeMismatchException, CsvConstraintViolationException {
            try {
                return convertUnicode(value);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    @NoArgsConstructor
    public class TimeConverter extends AbstractBeanField {
        @Override
        protected Object convert(String value) throws CsvDataTypeMismatchException, CsvConstraintViolationException {
            String parsedString = convertUnicode(value);
            try {
                LocalTime lt = LocalTime.parse(parsedString);
                return lt.toSecondOfDay();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    @NoArgsConstructor
    public class ZonedDateTimeConverter extends AbstractBeanField {
        @Override
        protected Object convert(String value) throws CsvDataTypeMismatchException, CsvConstraintViolationException {
            String parsedString = convertUnicode(value);
            try {
                ZonedDateTime dateTime = ZonedDateTime.parse(parsedString);
                ZonedDateTime easternZoneDateTime = dateTime.withZoneSameInstant(ZoneId.of("America/New_York"));
                return easternZoneDateTime;
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    //Helper to handle UTF-8 encoding
    public static String convertUnicode(String string) {
        byte[] rawString = string.getBytes(StandardCharsets.UTF_8);
        String parsedString = new String(rawString, StandardCharsets.UTF_8);
        return parsedString;
    }
}
