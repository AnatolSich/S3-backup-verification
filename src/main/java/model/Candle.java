package model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Candle {
    private String exchange;
    private int token;
    private double open;
    private double low;
    private double high;
    private double close;
}
