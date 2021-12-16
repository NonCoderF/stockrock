const graphDataAnalyzer = {

    logs: () => {
        resetSymbolHeads(Object.keys(dataSchemas))
    },

    analyze: (symbol) => {
        graphObjects.add(symbol + "#candlestick", 0, false)
        graphObjects.add(symbol + "#close", 0, true)
        graphObjects.add(symbol + "#ohlc", 0, false)
        graphObjects.add(symbol + "#volume", 0, false)

        let dataObject = dataSchemas[symbol];

        Object.keys(dataObject.indicatorSeries).map((key) => {
            graphObjects.add(symbol + '#' + key, 0, true)
        })

        graphObjects.clearResiduals(symbol)

        for (let i = 0; i < graphObjects.charts.length; i++) {
            if (Object.keys(graphObjects.charts[i]).length > 0){
                if (Object.keys(graphObjects.charts[i])[0].split('#')[0] === symbol){
                    let chartObject = {}
                    chartObject.id = dataObject.id + "_" + i;
                    chartObject.rendering = false;
                    chartObject.aspectRatio = chartHeightScale
                    chartObject.chartObj = graphObjects.charts[i]
                    chartObject.symbol = symbol

                    chartObject.drawEvent = new CustomEvent(symbol + "_" + i, { detail : chartObject })

                    if (chartContainers.hasOwnProperty(symbol + "_" + i)){
                        chartObject.width = chartContainers[symbol + "_" + i].width
                        chartObject.height = chartContainers[symbol + "_" + i].height
                    }

                    graphSchemas[symbol + "_" + i] = chartObject;
                }
            }
        }

        graphObjects.clearWindows()

        graphObjects.resetAutocompletions()

    },


}