package com.stockrock.analysis.service;

import com.stockrock.analysis.handlers.chart.IndicatorCmdHandler;
import com.stockrock.analysis.handlers.chart.OnChartServicesListener;
import com.stockrock.analysis.model.Data;
import com.stockrock.analysis.model.Stages;
import com.stockrock.analysis.model.indicators.Indicator;
import com.stockrock.analysis.utils.DataUtils;
import com.stockrock.analysis.utils.SeriesUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.ta4j.core.BarSeries;

import static com.stockrock.analysis.constants.CommandConstants.Services.indicatorServices;
import static com.stockrock.analysis.utils.ArrayUtils.stringToIntegerArray;

@Component
public class IndicatorServices implements ServiceObserver {

    @Qualifier("adxService")
    final ServiceInterface adxService;
    @Qualifier("closePriceService")
    final ServiceInterface closePriceService;
    @Qualifier("smaService")
    final ServiceInterface smaService;
    @Qualifier("emaService")
    final ServiceInterface emaService;
    @Qualifier("rsiService")
    final ServiceInterface rsiService;
    @Qualifier("obvService")
    final ServiceInterface obvService;
    @Qualifier("macdService")
    final ServiceInterface macdService;

    private OnChartServicesListener l;

    public IndicatorServices(
            ServiceInterface adxService,
            ServiceInterface closePriceService,
            ServiceInterface smaService,
            ServiceInterface emaService,
            ServiceInterface rsiService, ServiceInterface obvService, ServiceInterface macdService) {
        this.adxService = adxService;
        this.closePriceService = closePriceService;
        this.smaService = smaService;
        this.emaService = emaService;
        this.rsiService = rsiService;
        this.obvService = obvService;
        this.macdService = macdService;
    }

    public void init(Stages stages, OnChartServicesListener listener) {
        this.l = listener;

        this.adxService.setStages(stages);
        this.closePriceService.setStages(stages);
        this.smaService.setStages(stages);
        this.adxService.setObserver(this);
        this.closePriceService.setObserver(this);
        this.smaService.setObserver(this);
        this.emaService.setObserver(this);
        this.rsiService.setObserver(this);
        this.obvService.setObserver(this);
        this.macdService.setObserver(this);
    }

    public void initIndicators(Data data) {
        DataUtils dataUtils = new DataUtils();
        BarSeries series = SeriesUtils.ohlcToBarSeries(data.getOhlcSeries());
        IndicatorCmdHandler cmdHandler = new IndicatorCmdHandler(data);

        l.onStarted(data.getSessionId(), indicatorServices.name());

        Indicator closePrice = cmdHandler.closePrice();
        Indicator adx = cmdHandler.adx();
        Indicator sma = cmdHandler.sma();
        Indicator ema = cmdHandler.ema();
        Indicator rsi = cmdHandler.rsi();
        Indicator obv = cmdHandler.obv();
        Indicator macd = cmdHandler.macd();


        if (adx != null) {
            adxService.processAdxIndicators(data, stringToIntegerArray(adx.getData()), series);
        }
        if (closePrice != null) {
            closePriceService.processClosePriceIndicator(data, series);
        }
        if (sma != null){
            smaService.processSmaIndicator(data, stringToIntegerArray(sma.getData()), series);
        }
        if (ema != null){
            emaService.processEmaIndicator(data, stringToIntegerArray(ema.getData()), series);
        }
        if (rsi != null){
            rsiService.processRSIIndicator(data, stringToIntegerArray(rsi.getData()), series);
        }
        if (obv != null){
            obvService.processOBVIndicator(data, series);
        }
        if (macd != null){
            macdService.processMACDIndicator(data, stringToIntegerArray(macd.getData()), series);
        }


        l.onEnded(data.getSessionId(), indicatorServices.name());

    }

    @Override
    @SuppressWarnings("unchecked")
    public void observe(String serviceName, String sessionId, Object data, String message) {
        Data dataObj = (Data) data;
        l.onCompleted(sessionId, serviceName, dataObj);
    }
}
