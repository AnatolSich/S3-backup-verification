package service;

import lombok.RequiredArgsConstructor;
import model.Candle;
import org.apache.log4j.Logger;
import utilities.TimestampFileAppender;

import java.util.Properties;

public class LogService {

    private static final int  OPEN_SEEK = 3;
    private static final int  LOW_SEEK = 5;
    private static final int  HIGH_SEEK = 4;
    private static final int  CLOSE_SEEK = 6;

    @SuppressWarnings("RedundantThrows")
    public Candle parseLineToCandle(String line) throws Exception {
        String[] splitLine = line.split(",");
        String key = splitLine[1];
        key = key.replace('|', ',');
        String[] splitKey = key.split(",");
        String exchange = splitKey[0];
        int token = Integer.parseInt(splitKey[1]);

        return Candle.builder()
                .exchange(exchange)
                .token(token)
                .open((Double.parseDouble(splitLine[OPEN_SEEK])))
                .low((Double.parseDouble(splitLine[LOW_SEEK])))
                .high((Double.parseDouble(splitLine[HIGH_SEEK])))
                .close((Double.parseDouble(splitLine[CLOSE_SEEK])))
                .build();
    }

    public String getLogFileName() {
        TimestampFileAppender timestampFileAppender = (TimestampFileAppender) Logger.getRootLogger().getAppender("rollingFile");
        return timestampFileAppender.getFileName();
    }
}
