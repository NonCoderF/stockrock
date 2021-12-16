const graphRenderer = {

    render: (key, obj) => {
        let update = $("#chart-" + obj.id).length !== 0;
        let visible = graphRenderer.isVisible(obj.chartObj)

        if (obj.rendering) return; obj.rendering = true;

        if (!update && visible) {
            document.addEventListener(key, function (e) {
                graphRenderer.draw(e.type, e.detail)
                graphSchemas[e.type].rendering = false
            }, false);
            addChartWindowToStack(key, obj.id, obj.symbol)
        } else {
            graphRenderer.draw(key, obj)
            graphSchemas[key].rendering = false
        }
    },

    draw: (key, obj) => {
        let id = obj.id;
        let chartObj = obj.chartObj;

        let update = $("#chart-" + id).length !== 0;
        let visible = graphRenderer.isVisible(chartObj)

        if (!visible) {
            destroyWindowFromStack(key)
            return
        }

        obj.dim = getGraphDims(obj.width, obj.height, obj.aspectRatio);
        let width = obj.dim.width;
        let height = obj.dim.height;

        var dim = getChartDimen(width, height);
        var axes = getChartAxes(dim, chartObj);
        var svg = getChartSVG(dim, id, update);
        var accessor = getCandleStick(axes).accessor();

        /**********Data Variables*********/
        var count = graphRenderer.getDataSize(obj.symbol)
        var indicatorData = graphRenderer.getIndicatorData(obj.symbol)
        var ohlcData = graphRenderer.getOHLCData(obj.symbol)

        var titles = {}
        /*********OHLC Lines******************/
        var curves = {};

        var ohlcShow = false;
        var volumeShow = false;

        for (let k in chartObj) {
            let key = k.split('#')[1]
            if (chartObj[k].visible) {
                let type = chartObj[k].type
                if (key === 'candlestick' || key === 'close' || key === 'ohlc') ohlcShow = true
                if (key === 'volume') volumeShow = true
                if (key === 'candlestick') {
                    curves[key] = { curve : getCandleStick(axes, type), type : 'other', priority :  type, key : key}
                    titles[key] = { name : k, color : '#FFFFFF' }
                }
                if (key === 'close'){
                    curves[key] = { curve : getLine(axes, type, key, key), type : 'line', color : '#FFFFFF', priority :  type, key : key}
                    titles[key] = { name : k, color : '#FFFFFF' }
                }
                if (key === 'ohlc'){
                    curves[key] = { curve : getOHLC(axes, type), type : 'other', priority :  type, key : key}
                    titles[key] = { name : k, color : '#FFFFFF' }
                }
                if (key === 'volume'){
                    curves['volume'] = { curve : getLine(axes, type, key, key), type : 'line', color : '#FFFFFF', priority :  type, key : key}
                    titles[key] = { name : k, color : '#FFFFFF' }
                }
            }
        }

        /*****************Indicator lines************/
        var indicatorLines = {}

        for (let k in chartObj){
            let key = k.split('#')[1]
            if (indicatorData.hasOwnProperty(key)){
                if (chartObj[k].visible){
                    let type = chartObj[k].type
                    let color = chartObj[k].color;
                    let title = k.replace('#', ' ')
                    title = title.replace('_', ' ')
                    indicatorLines[key] = { curve : getLine(axes, type, key, key), type : 'line', color : color, priority :  type, key : key }
                    titles[key] = { name : title, color : color }
                }
            }
        }

        /********************False Positive**********************/

        if (Object.keys({...curves, ...indicatorLines}).length === 0){
            destroyWindowFromStack(key)
            return
        }

        /********************Populate Data************************/
        var data = []
        for (var index = 0; index < count; index++) {
            var datum = {
                date: new Date(ohlcData.endTimeSeries[index].replace('IST', '')),
            }
            var ohlc = {
                open: +ohlcData.open[index],
                close: +ohlcData.close[index],
                high: +ohlcData.high[index],
                low: +ohlcData.low[index],
            }
            var volume = {
                volume: +ohlcData.volume[index]
            }
            if (ohlcShow) datum = {...datum, ...ohlc}
            if (volumeShow) datum = {...datum, ...volume}

            var indicators = {}
            for (let key in indicatorData) {
                indicators[key] = +indicatorData[key][index]
            }

            datum = {...datum, ...indicators}

            data.push(datum)
        }

        data = data.sort(function (a, b) {
            return d3.ascending(accessor.d(a), accessor.d(b));
        });

        /********Zoom Variables********/
        var zoomableInit;
        var zoomed = () => {
            scaleAxesCurves(axes, zoomableInit,{...curves, ...indicatorLines});
            refresh(svg, {...curves, ...indicatorLines}, axes);
        };
        var zoom = getZoom(zoomed);

        /**********Render Chart**************/
        setScaleDomain(axes, data, chartObj, obj.symbol);

        drawChartTitles(svg, titles)

        drawAxes(dim, axes, svg);
        drawCurves(data, svg, {...curves, ...indicatorLines});
        drawCrosshair(dim, axes, svg, zoom);

        var trends = graphRenderer.getTrendData(obj.symbol)
        if (trends != null){
            var lows = trends.lows
            var highs = trends.highs
            var pairs = trends.pairs
            var pattern = trends.patterns

            var highTrendies = [];
            var lowTrendies = [];
            for (let i = 0; i < highs.length; i++){
                highTrendies.push({date : new Date(highs[i].date.replace('IST', '')), value: highs[i].value })
            }
            for (let i = 0; i < lows.length; i++){
                lowTrendies.push({date : new Date(lows[i].date.replace('IST', '')), value: lows[i].value })
            }

            drawLines(dim, svg, highTrendies, "highs", axes, false, "#00ff00")
            drawLines(dim, svg, lowTrendies, "lows", axes, false, "#ff0000")

            for (let k in pairs){
                var pair = pairs[k]
                var block = []
                block.push({ date : new Date(pair[0].date.replace('IST', '')), value: pair[0].value })
                block.push({ date : new Date(pair[1].date.replace('IST', '')), value: pair[1].value })
                block.push({ date : new Date(pair[3].date.replace('IST', '')), value: pair[3].value })
                block.push({ date : new Date(pair[2].date.replace('IST', '')), value: pair[2].value })
                block.push({ date : new Date(pair[0].date.replace('IST', '')), value: pair[0].value })

                // drawLines(dim, svg, block, "trendblock_" + k, axes, true)
            }


        }

        zoomableInit = axes.scales.x.zoomable().clamp(true).copy();

    },

    getIndicatorData: (symbol) => {
        return dataSchemas[symbol].indicatorSeries;
    },

    getOHLCData: (symbol) => {
        return dataSchemas[symbol].ohlcSeries
    },

    getDataSize: (symbol) => {
        return dataSchemas[symbol].ohlcSeries.close.length
    },

    getTrendData: (symbol) => {
        if (dataSchemas[symbol].hasOwnProperty("trendData")){
            return dataSchemas[symbol].trendData;
        }else{
            return null;
        }
    },

    isVisible: (chartObj) => {
        let visible = false;
        for (let key in chartObj){
            if (chartObj[key].visible){
                visible = true
                break
            }
        }
        return visible;
    },

}