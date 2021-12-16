package com.stockrock.analysis.service.ohlc;

import com.stockrock.analysis.constants.MessageStatus;
import com.stockrock.analysis.model.Data;
import com.stockrock.analysis.service.ServiceInterface;
import com.stockrock.analysis.utils.SysOut;
import com.stockrock.analysis.utils.TrendUtils;
import org.apache.commons.math3.util.Pair;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.stockrock.analysis.constants.CommandConstants.Services.trendlineService;
import static com.stockrock.analysis.service.configs.ExecutorConfig.THREAD_POOL;
import static com.stockrock.analysis.utils.TrendUtils.TrendPatterns.*;

@Service
public class TrendlineService extends ServiceInterface {

    @Override
    @Async(THREAD_POOL)
    public void processTrendlines(Data data) {
        super.processTrendlines(data);

        List<Double> prices = data.getOhlcSeries().getClose();
        List<String> times = data.getOhlcSeries().getEndTimeSeries();

        long startTime = System.currentTimeMillis();
        generateTrendData(prices, times, data);
        long endTime = System.currentTimeMillis();

        long duration = (endTime - startTime);

        SysOut.d("Duration of generation : " + duration + " ms");

    }

    private void generateTrendData(List<Double> prices, List<String> times, Data data){
        SysOut.d("Data size is : " + prices.size());

        List<Integer> relhighs = TrendUtils.getRatingRelativePoint("high", prices);
        List<Integer> rellows = TrendUtils.getRatingRelativePoint("low", prices);

        //High Low Indexes
        List<Integer> highIdx = TrendUtils.getHighsAndLows(relhighs, prices.size(), 4, prices.size()/10);
        List<Integer> lowIdx = TrendUtils.getHighsAndLows(rellows, prices.size(), 4, prices.size()/10);

        //Emerging Indexes
        Pair<Integer, Integer> o = TrendUtils.getHighestHighLowestLow(prices, highIdx.get(highIdx.size() - 1), prices.size() - 1);
        highIdx.add(o.getFirst());
        lowIdx.add(o.getSecond());

        List<List<Integer>> trendPairs = TrendUtils.generatePairs(highIdx, lowIdx);
        List<List<Integer>> morphedTrendPairs = TrendUtils.morphPairs(trendPairs);
        List<Pair<Double, Double>> slopes = TrendUtils.getSlopes(trendPairs, prices);
        List<TrendUtils.TrendPatterns> patterns = TrendUtils.getTrendPatterns(slopes, 12, 15);

        populateData(prices, times, data, highIdx, lowIdx, morphedTrendPairs, slopes, patterns);

    }



    private void populateData(
            List<Double> prices, List<String> times, Data data,
            List<Integer> highIdx, List<Integer> lowIdx, List<List<Integer>> trendPairs,
            List<Pair<Double, Double>> slopes, List<TrendUtils.TrendPatterns> patterns
    ){
        List<Map<String, Object>> highs = new ArrayList<>();
        List<Map<String, Object>> lows = new ArrayList<>();

        List<List<Map<String, Object>>> pairs = new ArrayList<>();

        for (Integer idx : highIdx) {
            Map<String, Object> point = new HashMap<>();
            point.put("index", idx);
            point.put("value", prices.get(idx));
            point.put("date", times.get(idx));
            highs.add(point);
        }

        for (Integer idx : lowIdx) {
            Map<String, Object> point = new HashMap<>();
            point.put("index", idx);
            point.put("value", prices.get(idx));
            point.put("date", times.get(idx));
            lows.add(point);
        }

        for (List<Integer> trendlinePair : trendPairs) {
            List<Map<String, Object>> trendBlock = new ArrayList<>();

            for (Integer integer : trendlinePair) {
                Map<String, Object> point = new HashMap<>();
                point.put("index", integer);
                point.put("date", times.get(integer));
                point.put("value", prices.get(integer));
                trendBlock.add(point);
            }

            pairs.add(trendBlock);

        }

        HashMap<String, Object> trendData = new HashMap<>();

        trendData.put("highs", highs);
        trendData.put("lows", lows);
        trendData.put("pairs", pairs);
        trendData.put("slopes", slopes);
        trendData.put("patterns", patterns);

        data.setTrendData(trendData);

        getObserver().observe(trendlineService.name(), data.getSessionId(), data, MessageStatus.OKAY);
    }



}
