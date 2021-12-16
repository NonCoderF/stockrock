package com.stockrock.analysis.service.ohlc;

import com.stockrock.analysis.constants.MessageStatus;
import com.stockrock.analysis.loaders.TwelveDataFinanceLoader;
import com.stockrock.analysis.model.Data;
import com.stockrock.analysis.model.Message;
import com.stockrock.analysis.model.OhlcSeries;
import com.stockrock.analysis.service.ServiceInterface;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import static com.stockrock.analysis.constants.CommandConstants.Services.ohlcService;
import static com.stockrock.analysis.service.configs.ExecutorConfig.THREAD_POOL;

@Service
public class OhlcService extends ServiceInterface {

    @Override
    @Async(THREAD_POOL)
    public void fetchBarsSeries(Data data) {
        super.fetchBarsSeries(data);

        TwelveDataFinanceLoader.getInstance().loadSeries(data,
                new TwelveDataFinanceLoader.TwelveDataFinanceLoaderListener() {
                    @Override
                    public void onReady(OhlcSeries ohlcSeries) {
                        data.setOhlcSeries(ohlcSeries);
                        getObserver().observe(ohlcService.name(), data.getSessionId(), data, MessageStatus.CONNECTED);
                    }

                    @Override
                    public void onFailed(String error) {
                        getObserver().observe(ohlcService.name(), data.getSessionId(), null, error);
                    }
                });

    }
}
