// const gap = 2.5;
//
// const drawChartTitle = (svg, text) => {
//     svg.append('text')
//         .attr("x", 5)
//         .attr("y", 15)
//         .text(text);
// };
//
// const getChartDimen = (width, height) => {
//     const dim = {
//         width: width,
//         height: height,
//         margin: {top: 40, right: 50, bottom: 30, left: 50},
//     };
//
//     dim.plot = {
//         width: dim.width - dim.margin.left - dim.margin.right,
//         height: dim.height - dim.margin.top - dim.margin.bottom
//     };
//
//     dim.heightplus = 0;
//
//     dim.heightpluspercent = 0.45;
//
//     return dim;
// };
//
// const getChartAxes = (dim, excharts) => {
//     var x = techan.scale.financetime().range([0, dim.plot.width]);
//     var y = d3.scaleLinear().range([dim.plot.height, 0]);
//
//     var xTopAxis = d3.axisTop(x);
//     var xBottomAxis = d3.axisBottom(x);
//     var yRightAxis = d3.axisRight(y);
//     var yLeftAxis = d3.axisLeft(y);
//
//     var axes = {
//         'scales': {'x': x, 'y': y},
//         'axes': {'left': yLeftAxis, 'top': xTopAxis, 'right': yRightAxis, 'bottom': xBottomAxis},
//         'ex_scales': {},
//         'ex_axes': {}
//     };
//
//
//     for (let i = 0; i < excharts.length; i++) {
//         let yy = d3.scaleLinear().range([y(-1 * dim.heightpluspercent * (i + 1)), y(-1 * dim.heightpluspercent * (i))]);
//         axes.ex_scales[excharts[i]] = yy;
//         axes.ex_axes['left_' + excharts[i]] = d3.axisLeft(yy).ticks(7).tickFormat(d3.format(",.3s"));
//         axes.ex_axes['right_' + excharts[i]] = d3.axisRight(yy).ticks(7).tickFormat(d3.format(",.3s"));
//         dim.heightplus += dim.plot.height * dim.heightpluspercent;
//     }
//
//     return axes;
// };
//
// const getChartSVG = (dim, id, update) => {
//     if (update) {
//         var svg = d3.select("#chart-" + id);
//         svg.selectAll("*").remove();
//     }
//
//     return d3.select("#chart-" + id).append("svg")
//         .attr("width", dim.plot.width + dim.margin.left + dim.margin.right)
//         .attr("height", dim.plot.height + dim.margin.top + dim.margin.bottom + dim.heightplus)
//         .append("g")
//         .attr("transform", "translate(" + dim.margin.left + "," + dim.margin.top + ")");
// };
//
// const getCandleStick = (axes) => {
//     return techan.plot.candlestick()
//         .xScale(axes.scales.x)
//         .yScale(axes.scales.y);
// };
//
// const getClose = (axes) => {
//     return techan.plot.close()
//         .xScale(axes.scales.x)
//         .yScale(axes.scales.y);
// };
//
// const getOHLC = (axes) => {
//     return techan.plot.ohlc()
//         .xScale(axes.scales.x)
//         .yScale(axes.scales.y);
// };
//
// const getAtr = (axes) => {
//     return techan.plot.atr()
//         .xScale(axes.scales.x)
//         .yScale(axes.scales.y);
// };
//
// const getVolume = (axes) => {
//     return techan.plot.volume()
//         .accessor(techan.accessor.ohlc())
//         .xScale(axes.scales.x)
//         .yScale(axes.ex_scales.volume);
// };
//
// const getMACD = (axes) => {
//     return techan.plot.macd()
//         .xScale(axes.scales.x)
//         .yScale(axes.ex_scales.macd);
// };
//
// const getADX = (axes) => {
//     return techan.plot.adx()
//         .xScale(axes.scales.x)
//         .yScale(axes.ex_scales.adx);
// };
//
// const getZoom = (zoomCallback) => {
//     return d3.zoom()
//         .on("zoom", zoomCallback);
// };
//
// const drawAxes = (dim, axes, svg) => {
//
//     var drawAxis = (name, translate, axis) => {
//         svg.append("g")
//             .attr("class", name)
//             .attr("transform", translate)
//             .call(axis);
//     };
//
//     for (let key in axes.ex_axes) {
//         let k = key.split('_');
//
//         let left = "translate(" + (-1 * gap) + ", " + gap + ")";
//         let right = "translate(" + (dim.plot.width + gap) + ", " + gap + ")";
//
//         if (k[0] === 'left') drawAxis("y left_" + k[1] + " axis", left, axes.ex_axes["left_" + k[1]]);
//         if (k[0] === 'right') drawAxis("y right_" + k[1] + " axis", right, axes.ex_axes["right_" + k[1]]);
//
//     }
//
//     drawAxis("x top axis", "translate(0, " + (-1 * gap) + ")", axes.axes.top);
//     drawAxis("x bottom axis", "translate(0, " + (dim.plot.height + dim.heightplus) + ")", axes.axes.bottom);
//     drawAxis("y left axis", "translate(" + (-1 * gap) + ", 0)", axes.axes.left);
//     drawAxis("y right axis", "translate(" + (dim.plot.width + gap) + ",0)", axes.axes.right);
//
// };
//
// const drawCrosshair = (dim, axes, svg, zoom) => {
//
//     function enter() {
//         coordsText.style("display", "inline");
//     }
//
//     function out() {
//         coordsText.style("display", "none");
//     }
//
//     function move(coords) {
//         if (coords !== undefined) {
//             coordsText.text(
//                 d3.timeFormat('%Y-%m-%d')(coords.x) + ", " + d3.format(',.3s')(coords.y)
//             );
//         }
//     }
//
//     var coordsText = svg.append('text')
//         .style("text-anchor", "end")
//         .attr("class", "coords")
//         .attr("x", dim.plot.width - 5)
//         .attr("y", 15);
//
//     var annotation = (axis, orient, format, translate) => {
//         return techan.plot.axisannotation()
//             .axis(axis)
//             .orient(orient)
//             .format(format)
//             .width(65)
//             .translate(translate);
//     };
//
//     var crosshair = (xScale, yScale, xAnnot, yAnnot, enter, move, out) => {
//         return techan.plot.crosshair()
//             .xScale(xScale)
//             .yScale(yScale)
//             .xAnnotation(xAnnot)
//             .yAnnotation(yAnnot)
//             .verticalWireRange([0, dim.plot.height + dim.heightplus])
//             .on("enter", enter)
//             .on("out", out)
//             .on("move", move);
//     };
//
//     var drawCrossHair = (name, crosshair, move) => {
//         svg.append('g')
//             .attr("class", name)
//             .call(crosshair)
//             .call(zoom)
//             .each(function (d) {
//                 move(d);
//             });
//
//     };
//
//     var ohlcleft = annotation(axes.axes.left, 'left', d3.format(',.2f'), [0, 0]);
//     var ohlcRight = annotation(axes.axes.right, 'right', d3.format(',.2f'), [dim.plot.width, 0]);
//     var timeTop = annotation(axes.axes.top, 'top', d3.timeFormat('%Y-%m-%d'), [0, 0]);
//     var timeBottom = annotation(axes.axes.bottom, 'bottom', d3.timeFormat('%Y-%m-%d'), [0, dim.plot.height + dim.heightplus]);
//
//     var crosshair1 = crosshair(axes.scales.x, axes.scales.y, [timeTop, timeBottom], [ohlcleft, ohlcRight], enter, move, out);
//
//     drawCrossHair('crosshair', crosshair1, move);
//
//     for (let key in axes.ex_scales) {
//         var l = annotation(axes.ex_axes['left_' + key], 'left', d3.format(',.3s'), [0, 0]);
//         var r = annotation(axes.ex_axes['right_' + key], 'right', d3.format(',.3s'), [dim.plot.width, 0]);
//         var c = crosshair(axes.scales.x, axes.ex_scales[key], [timeTop, timeBottom], [l, r], enter, move, out);
//         drawCrossHair('crosshair ' + key, c, move);
//     }
//
// };
//
// const drawCurves = (data, svg, curves) => {
//     for (let key in curves) {
//
//         if (key === 'atr') {
//
//         }
//         if (key === 'macd') {
//             data = techan.indicator.macd()(data)
//         }
//         if (key === 'adx') {
//             data = techan.indicator.adx()(data);
//         }
//         if (key === 'aroon') {
//             data = techan.indicator.aroon()(data);
//         }
//
//         if (curves[key].visible) {
//             svg.append("g")
//                 .datum(data)
//                 .attr("class", key)
//                 .call(curves[key].curve);
//         }
//     }
// };
//
// const setScaleDomain = (axes, data, accessor) => {
//
//     axes.scales.x.domain(techan.scale.plot.time(data).domain());
//     axes.scales.y.domain(techan.scale.plot.ohlc(data).domain());
//
//     for (let key in axes.ex_scales) {
//         if (key === 'atr') {
//             var atrtrailingstopData = techan.indicator.atrtrailingstop()(data);
//             axes.ex_scales.atr.domain(techan.scale.plot.atrtrailingstop(atrtrailingstopData).domain());
//         }
//         else if (key === 'volume') {
//             axes.ex_scales.volume.domain(techan.scale.plot.volume(data).domain());
//         }
//         else if (key === 'macd') {
//             var macdData = techan.indicator.macd()(data);
//             axes.ex_scales.macd.domain(techan.scale.plot.macd(macdData).domain());
//         }
//         else if (key === 'adx') {
//             var adxData = techan.indicator.adx()(data);
//             axes.ex_scales.adx.domain(techan.scale.plot.adx(adxData).domain());
//         }
//         else if (key === 'aroon') {
//             var aroonData = techan.indicator.aroon()(data);
//             axes.ex_scales.aroon.domain(techan.scale.plot.aroon(data).domain());
//         }
//     }
//
// };
//
// const scaleAxesCurves = (axes, zoomableInit, curves) => {
//     var rescaledX = d3.event.transform.rescaleX(zoomableInit).domain();
//     var rescaledY = d3.event.transform.rescaleY(axes.scales.y);
//
//     axes.scales.x.zoomable().domain(rescaledX);
//
//     axes.axes.left.scale(rescaledY);
//     axes.axes.right.scale(rescaledY);
//
//     for (let key in curves.a) {
//         if (curves.a[key].visible) {
//             curves.a[key].curve.yScale(rescaledY);
//         }
//     }
//
//
// };
//
// const refresh = (svg, curves, axes) => {
//
//     svg.select("g.x.top.axis").call(axes.axes.top);
//     svg.select("g.x.bottom.axis").call(axes.axes.bottom);
//     svg.select("g.y.left.axis").call(axes.axes.left);
//     svg.select("g.y.right.axis").call(axes.axes.right);
//
//     for (let key in curves.a) {
//         if (curves.a[key].visible) {
//             svg.select("g." + key).call(curves.a[key].curve.refresh);
//         }
//     }
//
//     for (let key in curves.b) {
//         if (curves.b[key].visible) {
//             svg.select("g." + key).call(curves.b[key].curve.refresh);
//         }
//     }
// };
//
//
//
//
//
//
//
//
//
//
//
