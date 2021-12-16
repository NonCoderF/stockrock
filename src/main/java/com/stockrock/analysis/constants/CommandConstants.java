package com.stockrock.analysis.constants;

public class CommandConstants {

    public static final String chart = "chart";
    public static final String info = "info";
    public static final String terminal = "terminal";

    public enum Commands{
        open, add, set, remove, close, get
    }

    public enum Curves{
        candlestick, ohlc, close, volume
    }

    public enum Indicators{
        adx, closePrice, sma, ema, rsi, obv, macd, trendlines
    }

    public enum Services{
        ohlcService, indicatorServices, adxService, closePriceService,
        smaService, emaService, rsiService, obvService, macdService, trendlineService
    }

    public enum Days{
        last5mins, last10mins, last15mins, last30mins, lasthour, last2hours, last3hours, last4hours, last6hours,
        today, yesterday, daybeforeyesterday,
        last2days, last3days, lastweek, last15days, last30days,
        last2months, last3months, last6months, lastyear
    }
}
