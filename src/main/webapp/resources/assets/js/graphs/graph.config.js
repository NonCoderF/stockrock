const getGraphDims = (width, height, aspectRatio) => {

    let defAspectRatio = height / width;

    return {
        'width': width - 30,
        // 'height': (width - 30) * aspectRatio
        'height': defAspectRatio > aspectRatio ? (width - 30) * aspectRatio : height - 30
    };
};

const drawChartTitles = (svg, titles) => {
    let i = 0;
    Object.values(titles).map(title => {
        svg.append("rect")
            .attr("x", 5)
            .attr("y", 10 + i * 12)
            .attr("width", 7)
            .attr("height", 7)
            .attr("fill", title.color)
        svg.append('text')
            .attr("fill", title.color)
            .attr("x", 20)
            .attr("y", 17 + i * 12)
            .text('#' + title.name)
        i++;
    })
}

const getChartDimen = (width, height) => {
    const dim = {
        width: width,
        height: height,
        margin: {top: 40, right: 50, bottom: 30, left: 50},
    };

    dim.plot = {
        width: dim.width - dim.margin.left - dim.margin.right,
        height: dim.height - dim.margin.top - dim.margin.bottom
    };

    dim.heightplus = 0;

    dim.heightpluspercent = 0.45;

    return dim;
};

const getChartAxes = (dim, chartObj) => {
    var x = techan.scale.financetime().range([0, dim.plot.width]);
    var y = d3.scaleLinear().range([dim.plot.height, 0]);

    var xTopAxis = d3.axisTop(x)
    var xBottomAxis = d3.axisBottom(x)
    var yRightAxis = d3.axisRight(y).tickFormat(d3.format(",.3s"))
    var yLeftAxis = d3.axisLeft(y).tickFormat(d3.format(",.3s"))

    var axes = {
        'scales': {'x': x, 'y': y},
        'axes': {'left': yLeftAxis, 'top': xTopAxis, 'right': yRightAxis, 'bottom': xBottomAxis},
        'ex_scales': {},
        'ex_axes': {}
    };

    let i = 0;
    for (let k in chartObj) {
        let key = k.split('#')[1]
        if (chartObj[k].visible && chartObj[k].type === 1) {
            let yy = d3.scaleLinear().range([y(-1 * dim.heightpluspercent * (i + 1)), y(-1 * dim.heightpluspercent * (i))]);
            axes.ex_scales[key] = yy;
            axes.ex_axes['left_' + key] = d3.axisLeft(yy).ticks(7).tickFormat(d3.format(",.3s"));
            axes.ex_axes['right_' + key] = d3.axisRight(yy).ticks(7).tickFormat(d3.format(",.3s"));
            dim.heightplus += dim.plot.height * dim.heightpluspercent;
            i++;
        }
    }

    return axes;
};

const getChartSVG = (dim, id, update) => {
    if (update) {
        var svg = d3.select("#chart-" + id);
        svg.selectAll("*").remove();
    }

    return d3.select("#chart-" + id).append("svg")
        .attr("width", dim.plot.width + dim.margin.left + dim.margin.right)
        .attr("height", dim.plot.height + dim.margin.top + dim.margin.bottom + dim.heightplus)
        .append("g")
        .attr("transform", "translate(" + dim.margin.left + "," + dim.margin.top + ")");
};

const getCandleStick = (axes, type) => {
    let yScale = type === 0 ? axes.scales.y : axes.ex_scales.candlestick
    return techan.plot.candlestick().xScale(axes.scales.x).yScale(yScale);
};

const getArrow = (axes, zoom) => {
    return techan.plot.tradearrow()
        .xScale(axes.scales.x)
        .yScale(axes.scales.y)
        .orient(function (d) {
            return d.type.startsWith("buy") ? "up" : "down";
        })
}

const getClose = (axes, type) => {
    let yScale = type === 0 ? axes.scales.y : axes.ex_scales.close
    return techan.plot.close().xScale(axes.scales.x).yScale(yScale);

};

const getOHLC = (axes, type) => {
    let yScale = type === 0 ? axes.scales.y : axes.ex_scales.ohlc
    return techan.plot.ohlc().xScale(axes.scales.x).yScale(yScale);
};

const getVolume = (axes, type) => {
    let yScale = type === 0 ? axes.scales.y : axes.ex_scales.volume
    return techan.plot.volume().accessor(techan.accessor.ohlc()).xScale(axes.scales.x).yScale(yScale);
};

const getLine = (axes, type, key, keyName) => {
    return d3.line()
        .defined(function (d) {
            return !isNaN(d[key]);
        })
        .x(function (d) {
            return axes.scales.x(d.date);
        })
        .y(function (d) {
            if (type === 0) return axes.scales.y(d[key]);
            else return axes.ex_scales[keyName](d[key]);
        })
}


const getZoom = (zoomCallback) => {
    return d3.zoom().on("zoom", zoomCallback);
};

const drawAxes = (dim, axes, svg) => {

    var drawAxis = (name, translate, axis) => {
        svg.append("g")
            .attr("class", name)
            .attr("transform", translate)
            .call(axis);
    };

    for (let key in axes.ex_axes) {
        let k = key.split('_');

        let left = "translate(" + (-1 * gap) + ", " + gap + ")";
        let right = "translate(" + (dim.plot.width + gap) + ", " + gap + ")";

        if (k[0] === 'left') drawAxis("y " + key + " axis", left, axes.ex_axes[key]);
        if (k[0] === 'right') drawAxis("y " + key + " axis", right, axes.ex_axes[key]);
    }

    drawAxis("x top axis", "translate(0, " + (-1 * gap) + ")", axes.axes.top);
    drawAxis("x bottom axis", "translate(0, " + (dim.plot.height + dim.heightplus) + ")", axes.axes.bottom);
    drawAxis("y left axis", "translate(" + (-1 * gap) + ", 0)", axes.axes.left);
    drawAxis("y right axis", "translate(" + (dim.plot.width + gap) + ",0)", axes.axes.right);

};

const drawCrosshair = (dim, axes, svg, zoom) => {

    function enter() {
        coordsText.style("display", "inline");
    }

    function out() {
        coordsText.style("display", "none");
    }

    function move(coords) {
        if (coords !== undefined) {
            coordsText.text(
                d3.timeFormat('%Y-%m-%d')(coords.x) + ", " + d3.format(',.3s')(coords.y)
            );
        }
    }

    var coordsText = svg.append('text')
        .style("text-anchor", "end")
        .attr("class", "coords")
        .attr("x", dim.plot.width - 5)
        .attr("y", 15);

    var annotation = (axis, orient, format, translate) => {
        return techan.plot.axisannotation()
            .axis(axis)
            .orient(orient)
            .format(format)
            .width(65)
            .translate(translate);
    };

    var crosshair = (xScale, yScale, xAnnot, yAnnot, enter, move, out) => {
        return techan.plot.crosshair()
            .xScale(xScale)
            .yScale(yScale)
            .xAnnotation(xAnnot)
            .yAnnotation(yAnnot)
            .verticalWireRange([0, dim.plot.height + dim.heightplus])
            .on("enter", enter)
            .on("out", out)
            .on("move", move);
    };

    var drawCrossHair = (name, crosshair, move) => {
        svg.append('g')
            .attr("class", name)
            .call(crosshair)
            .call(zoom)
            .each(function (d) {
                move(d);
            });

    };

    var ohlcleft = annotation(axes.axes.left, 'left', d3.format(',.2f'), [0, 0]);
    var ohlcRight = annotation(axes.axes.right, 'right', d3.format(',.2f'), [dim.plot.width, 0]);
    var timeTop = annotation(axes.axes.top, 'top', d3.timeFormat('%Y-%m-%d'), [0, 0]);
    var timeBottom = annotation(axes.axes.bottom, 'bottom', d3.timeFormat('%Y-%m-%d'), [0, dim.plot.height + dim.heightplus]);

    var crosshair1 = crosshair(axes.scales.x, axes.scales.y, [timeTop, timeBottom], [ohlcleft, ohlcRight], enter, move, out);

    drawCrossHair('crosshair', crosshair1, move);

    for (let key in axes.ex_scales) {
        var l = annotation(axes.ex_axes['left_' + key], 'left', d3.format(',.3s'), [0, 0]);
        var r = annotation(axes.ex_axes['right_' + key], 'right', d3.format(',.3s'), [dim.plot.width, 0]);
        var c = crosshair(axes.scales.x, axes.ex_scales[key], [timeTop, timeBottom], [l, r], enter, move, out);
        drawCrossHair('crosshair ' + key, c, move);
    }

};

const drawCurves = (data, svg, curves) => {
    for (let key in curves) {
        if (curves[key].type === 'other') {
            svg.append("g").datum(data).attr("class", key).call(curves[key].curve)
        }
        if (curves[key].type === 'line') {
            svg.append("g").attr("id", key).append("path").attr("stroke", curves[key].color).attr("fill", "none").attr("class", "area");
            svg.select("g#" + key + " path").datum(data).attr("d", curves[key].curve);
        }
    }
};

const drawArrows = (svg, data, tradeArrow, zoom) => {
    svg.append("g").datum(data).attr("class", "tradearrow").call(zoom).call(tradeArrow);
}

const setScaleDomain = (axes, data, chartObj, symbol) => {

    let min = 999999999
    let max = -999999999

    let exmins = {}
    let exmaxs = {}

    for (let i = 0; i < data.length; i++) {
        let d = data[i]
        for (let key in d) {
            if (key !== 'date' && key !== 'open' && key !== 'high' && key !== 'low')
                if (chartObj.hasOwnProperty(symbol + '#' + key)) {
                    if (chartObj[symbol + '#' + key].type === 0) {
                        if (d[key] < min) min = d[key]
                        if (d[key] > max) max = d[key]
                    } else {
                        if (exmins[key] === undefined) {
                            exmins[key] = d[key]
                        } else {
                            if (d[key] < exmins[key]) exmins[key] = d[key]
                        }
                        if (exmaxs[key] === undefined) {
                            exmaxs[key] = d[key]
                        } else {
                            if (d[key] > exmaxs[key]) exmaxs[key] = d[key]
                        }
                    }
                }
        }
    }

    for (let key in axes.ex_scales) {
        if (['candlestick', 'ohlc'].includes(key)) {
            axes.ex_scales[key].domain(techan.scale.plot.ohlc(data).domain());
        } else {
            let gapPercent = (exmaxs[key] - exmins[key]) * 0.1
            axes.ex_scales[key].domain([exmins[key] - gapPercent, exmaxs[key] + gapPercent])
        }
    }

    if (min === 999999999) min = 0;
    if (max === -999999999) max = 0;

    axes.scales.x.domain(techan.scale.plot.time(data).domain());
    if (min === 0 && max === 0) {
        axes.scales.y.domain(techan.scale.plot.ohlc(data).domain());
    } else {
        let gapPercent = (max - min) * 0.1
        axes.scales.y.domain([min - gapPercent, max + gapPercent]);
    }
};

const scaleAxesCurves = (axes, zoomableInit, curves) => {
    var rescaledX = d3.event.transform.rescaleX(zoomableInit).domain();
    var rescaledY = d3.event.transform.rescaleY(axes.scales.y);


    axes.scales.x.zoomable().domain(rescaledX);

    for (let key in curves) {
        if (curves[key].priority === 0) {
            if (curves[key].type === 'other') {
                axes.axes.left.scale(rescaledY);
                axes.axes.right.scale(rescaledY);
                curves[key].curve.yScale(rescaledY)
            }
            if (curves[key].type === 'line') {
                // axes.axes.left.scale(rescaledY);
                // axes.axes.right.scale(rescaledY);
                // curves[key].curve.y(rescaledY)
            }
        }
    }

};

const refresh = (svg, curves, axes) => {

    svg.select("g.x.top.axis").call(axes.axes.top);
    svg.select("g.x.bottom.axis").call(axes.axes.bottom);
    svg.select("g.y.left.axis").call(axes.axes.left);
    svg.select("g.y.right.axis").call(axes.axes.right);

    for (let key in curves) {
        if (curves[key].type === 'other') {
            svg.select("g." + key).call(curves[key].curve.refresh);
        }
        if (curves[key].type === 'line') {
            svg.select("g#" + key + " path").attr("d", curves[key].curve);
        }
    }
};

const drawLines = (dim, svg, data, key, axes, fill, linecolor) => {
    var lineInfo = svg.append('text')
        .style("text-anchor", "end")
        .attr("class", "coords")
        .attr("x", dim.plot.width - 5)
        .attr("y", 15);

    var color = linecolor != null ? linecolor : "#ffffff"
    var alpha = "40";
    if (!fill){
        alpha = "00"
    }
    svg.append("g").attr("id", key).append("path").attr("stroke", color).attr("fill", color + alpha).attr("class", "area");
    svg.select("g#" + key + " path").datum(data).attr("d", d3.line()
        .x(function (d) {
            return axes.scales.x(d.date);
        })
        .y(function (d) {
            return axes.scales.y(d.value);
        }));
    if (fill){
        svg.select("g#" + key + " path")
            .on('mouseover', function (d, i) {
                d3.select(this).transition()
                    .duration('50')
                    .attr('stroke', '#00e800')
                    .attr('fill', '#00e800');
                lineInfo.text(randomColor())
            })
            .on('mouseout', function (d, i) {
                d3.select(this).transition()
                    .duration('5')
                    .attr('stroke', color)
                    .attr('fill', color + "40");
                lineInfo.text("")
            })
    }
}