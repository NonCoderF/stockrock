package com.stockrock.analysis.utils;

import com.stockrock.analysis.constants.CommandConstants;
import org.apache.commons.math3.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TrendUtils {

    public static enum TrendPatterns{
        rectangle, pricingChannel, risingWedge, fallingWedge, descendingTriangle, ascendingTriangle, symmetricalTriangle, nil
    }

    public static Pair<Integer, Integer> getHighestHighLowestLow(List<Double> price, int fromIndex, int toIndex) {
        Object[] a = Arrays.copyOfRange(price.toArray(), fromIndex, toIndex);
        List<Double> priceSplit = new ArrayList<>();
        for (Object o : a) {
            priceSplit.add((Double) o);
        }
        return new Pair<>(priceSplit.indexOf(Collections.max(priceSplit)) + fromIndex, priceSplit.indexOf(Collections.min(priceSplit)) + fromIndex);
    }

    public static List<Integer> getRatingRelativePoint(String type, List<Double> price) {
        int coverage;
        List<Integer> max_N = Arrays.asList(new Integer[price.size()]);
        for (int index = 1; index < price.size(); index++) {
            int days = 0;
            if (index <= price.size() - index) coverage = index - 1;
            else coverage = price.size() - index;
            switch (type) {
                case "high":
                    for (int j = 1; j < coverage; j++) {
                        if (price.get(index) <= price.get(index - j) || price.get(index) <= price.get(index + j)) break;
                        days = j;
                    }
                    break;
                case "low":
                    for (int j = 1; j < coverage; j++) {
                        if (price.get(index) >= price.get(index - j) || price.get(index) >= price.get(index + j)) break;
                        days = j;
                    }
                    break;
            }
            max_N.set(index, days);
        }
        return max_N;
    }

    public static List<Integer> getHighsAndLows(List<Integer> max_N, Integer count, Integer min_dist, Integer max_dist){
        int index = 0;
        List<Integer> trend = new ArrayList<>();
        while (index < count - max_dist){
            index = index + 1;
            int best_so_far = 0;
            int idx_best_so_far = 0;
            for (int i = index + min_dist; i < index + max_dist; i++){
                if (max_N.get(i) > best_so_far){
                    idx_best_so_far = i;
                    best_so_far = max_N.get(i);
                }
            }
            if (idx_best_so_far > 0)
                index = idx_best_so_far;
            trend.add(index);
        }
        return trend;
    }

    public static List<List<Integer>> generatePairs(List<Integer> highIdx, List<Integer> lowIdx) {
        int first = 1;
        List<List<Integer>> trendline_pairs = new ArrayList<>();
        for (int i = 1; i < highIdx.size() - 1; i++) {
            for (int j = first; j < lowIdx.size() - 1; j++) {
                if (lowIdx.get(j) < highIdx.get(i + 1) && highIdx.get(i) < lowIdx.get(j + 1)) {
                    List<Integer> pairs = new ArrayList<>();
                    pairs.add(highIdx.get(i));
                    pairs.add(highIdx.get(i + 1));
                    pairs.add(lowIdx.get(j));
                    pairs.add(lowIdx.get(j + 1));

                    trendline_pairs.add(pairs);
                    first = j;
                } else if (lowIdx.get(j) > highIdx.get(i + 1)) {
                    break;
                }
            }
        }
        return trendline_pairs;
    }

    public static List<List<Integer>> morphPairs(List<List<Integer>> trendPairs){
        if (trendPairs.size() < 2){
            return trendPairs;
        }
        List<List<Integer>> tempTrendPairs = new ArrayList<>();
        Collections.reverse(trendPairs);

        for (int i = 2; i < trendPairs.size(); i+=5){
            List<Integer> pair = new ArrayList<>();

            pair.add(trendPairs.get(i).get(0));
            pair.add(trendPairs.get(2).get(1));
            pair.add(trendPairs.get(i).get(2));
            pair.add(trendPairs.get(2).get(3));

            tempTrendPairs.add(pair);
        }
        Collections.reverse(tempTrendPairs);
        tempTrendPairs.add(trendPairs.get(0));

        return tempTrendPairs;
    }

    public static List<Pair<Double, Double>> getSlopes(List<List<Integer>> trendlinePairs, List<Double> prices) {
        List<Pair<Double, Double>> slopes = new ArrayList<>();
        for (List<Integer> pairs : trendlinePairs) {
            double x1 = pairs.get(0);
            double x2 = pairs.get(1);
            double y1 = prices.get(pairs.get(0));
            double y2 = prices.get(pairs.get(1));

            double x3 = pairs.get(2);
            double x4 = pairs.get(3);
            double y3 = prices.get(pairs.get(2));
            double y4 = prices.get(pairs.get(3));
            double slope1 = Math.toDegrees(Math.atan((y1 - y2) / (x1 - x2)));
            double slope2 = Math.toDegrees(Math.atan((y3 - y4) / (x3 - x4)));

            if (Double.isNaN(slope1)) slope1 = 0.0;
            if (Double.isNaN(slope2)) slope2 = 0.0;

            slopes.add(new Pair<>(slope1, slope2));
        }

        return slopes;
    }

    public static List<TrendPatterns> getTrendPatterns(List<Pair<Double, Double>> slopes, Integer lineTolereance, Integer pairTolerance) {
        List<TrendPatterns> patterns = new ArrayList<>();
        for (Pair<Double, Double> slope : slopes) {
            double m1 = slope.getFirst();
            double m2 = slope.getSecond();

            if (Math.abs(m1) <= lineTolereance) m1 = 0;
            if (Math.abs(m2) <= lineTolereance) m2 = 0;

            if (m1 == 0 && m2 == 0) patterns.add(TrendPatterns.rectangle);
            else if (Math.abs(m1 - m2) <= pairTolerance) patterns.add(TrendPatterns.pricingChannel);

            else if (m1 > 0 && m2 > 0 && Math.abs(m2) > Math.abs(m1)) patterns.add(TrendPatterns.risingWedge);
            else if (m1 < 0 && m2 < 0 && Math.abs(m2) < Math.abs(m1)) patterns.add(TrendPatterns.fallingWedge);

            else if (m2 == 0 && m1 < 0) patterns.add(TrendPatterns.descendingTriangle);
            else if (m1 == 0 && m2 > 0) patterns.add(TrendPatterns.ascendingTriangle);
            else if (m1 < 0 && m2 > 0 && Math.abs(m1 + m2) <= pairTolerance) patterns.add(TrendPatterns.symmetricalTriangle);
            else patterns.add(TrendPatterns.nil);
        }
        return patterns;
    }

}
