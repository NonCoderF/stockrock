// const SMALL = 0.5;
// const LARGE = 1;
// var chartWidthPercent = .50;
// var chartWidthScale = 1;
// var chartHeightScale = 6/16;
//
// var getChartDim = (size) => {
//     var heightInc = size === SMALL ? 1.25 : 1;
//     return {
//         'width': $(window).width() * (chartWidthPercent - 0.02) * chartWidthScale * size,
//         'height': $(window).width() * (chartWidthPercent - 0.02) * chartHeightScale * size * heightInc
//     };
// };
//
//
// var chartObj = [
//     {
//         'id' : 3,
//         'title' : 'Microsoft',
//         'canvas' : {'rendering' : false, 'renderedOnce' : false},
//         'charts' : {
//             'a' : ['candlestick'],
//             'b' : ['volume']
//         },
//         'size' : LARGE
//     },
//     {
//         'id' : 1,
//         'title' : 'Facebook',
//         'canvas' : {'rendering' : false, 'renderedOnce' : false},
//         'charts' : {
//             'a' : ['close'],
//             'b' : []
//         },
//         'size' : SMALL
//     },
//     {
//         'id' : 2,
//         'title' : 'Microsoft',
//         'canvas' : {'rendering' : false, 'renderedOnce' : false},
//         'charts' : {
//             'a' : ['close'],
//             'b' : []
//         },
//         'size' : SMALL
//     },
//     {
//         'id' : 4,
//         'title' : 'Microsoft',
//         'canvas' : {'rendering' : false, 'renderedOnce' : false},
//         'charts' : {
//             'a' : ['close'],
//             'b' : []
//         },
//         'size' : LARGE
//     }
//
//
// ];
//
// var chartFns = {
//
//     'init' : () => {
//         document.addEventListener("WinResize", (e) => {
//             for (let i = 0; i < chartObj.length; i++) {
//                 chartObj[i].dim.width = e.width * chartWidthScale;
//                 chartObj[i].dim.height = e.width * chartHeightScale;
//             }
//             chartFns.render();
//
//         })
//     },
//
//     'render' : () => {
//         for (let i = 0; i < chartObj.length; i++) {
//             renderUpdateChart(chartObj[i]);
//         }
//     }
//
// };
//
// const attachChart = () => {
//     chartFns.init();
//     chartFns.render();
//
// };
//
//
// // const renderUpdateChart = (obj) => {
// //
// //     obj.dim = getChartDim(obj.size);
// //
// //     let id = obj.id;
// //     let title = obj.title;
// //     let width = obj.dim.width;
// //     let height = obj.dim.height;
// //     let update = obj.canvas.renderedOnce;
// //     let charts = obj.charts.a;
// //     let excharts = obj.charts.b;
// //
// //     obj.canvas.renderedOnce = true;
// //
// //     if (obj.canvas.rendering) {
// //         return;
// //     }
// //
// //     obj.canvas.rendering = true;
// //
// //     if (!update) {
// //         console.log("came up here");
// //         $('#chartcontent').append('<div id="chart-' + id + '" style="display: inline-block" ></div>');
// //     }
// //
// //     var dim = getChartDimen(width, height);
// //     var axes = getChartAxes(dim, excharts);
// //     var svg = getChartSVG(dim, id, update);
// //
// //     var curves = [];
// //     var excurves = [];
// //
// //
// //
// //
// //     if (charts.includes('candlestick')) {
// //         curves['candlestick'] = {
// //             'curve' : getCandleStick(axes), visible : true
// //         }
// //     }
// //     if (charts.includes('close')) {
// //         curves['close'] = {
// //             'curve' : getClose(axes), visible : true
// //         }
// //     }
// //     if (charts.includes('ohlc')) {
// //         curves['ohlc'] = {
// //             'curve' : getOHLC(axes), visible : true
// //         }
// //     }
// //     if (charts.includes('atr')) {
// //         curves['atr'] = {
// //             'curve' : getAtr(axes), visible : true
// //         }
// //     }
// //
// //
// //
// //     if (excharts.includes('volume')) {
// //         excurves['volume'] = {
// //             'curve' : getVolume(axes), visible : true
// //         }
// //     }
// //
// //     if (excharts.includes('macd')) {
// //         excurves['macd'] = {
// //             'curve' : getMACD(axes), visible : true
// //         }
// //     }
// //     if (excharts.includes('adx')) {
// //         excurves['adx'] = {
// //             'curve' : getADX(axes), visible : true
// //         }
// //     }
// //     if (excharts.includes('aroon')) {
// //         excurves['aroon'] = {
// //             'curve' : getAroon(axes), visible : true
// //         }
// //     }
// //
// //     var parseDate = d3.timeParse("%d-%b-%y");
// //
// //     var accessor = getCandleStick(axes).accessor();
// //
// //     var zoomableInit;
// //
// //     var zoomed = () => {
// //
// //         scaleAxesCurves(axes, zoomableInit, { 'a' : curves, 'b' : excurves });
// //
// //         refresh(svg, { 'a' : curves, 'b' : excurves }, axes);
// //     };
// //
// //     var zoom = getZoom(zoomed);
// //
// //     d3.csv("resources/data.csv", function(error, data) {
// //
// //         data = data.slice(0, 100).map(function(d) {
// //             return {
// //                 date: parseDate(d.Date),
// //                 open: +d.Open,
// //                 high: +d.High,
// //                 low: +d.Low,
// //                 close: +d.Close,
// //                 volume: +d.Volume
// //             };
// //         }).sort(function(a, b) { return d3.ascending(accessor.d(a), accessor.d(b)); });
// //
// //         setScaleDomain(axes, data, accessor);
// //
// //         drawChartTitle(svg, title);
// //
// //         drawAxes(dim, axes, svg);
// //         drawCurves(data, svg, curves);
// //         drawCurves(data, svg, excurves);
// //         drawCrosshair(dim, axes, svg, zoom);
// //
// //         zoomableInit = axes.scales.x.zoomable().clamp(true).copy();
// //
// //     });
// //
// //     obj.canvas.rendering = false;
// //
// //
// //
// // };