const prepareCommandSchema = (cntxt, args) => {

    let conditionRule = (input) => {
        return input !== "";
    }

    let symbolCallback = (args) => {
        if (isExists(args.symbol)) {
            backoffPromptToOperation(cntxt)
            updateCommandSchema(true, args)
            checkOut(args.symbol)
        } else {
            let dayCallback = (args) => {
                intrrComplete(args)
            }
            let toCallback = (args) => {
                intrrComplete(args)
            }
            let intrrComplete = (args) => {
                backoffPromptToOperation(cntxt)
                updateCommandSchema(false, args)
                checkOut(args.symbol)
            }

            const dayIntrr = () => interrogate(cntxt, args, "day", "Enter days", conditionRule, dayCallback)
            const toIntrr = () => interrogate(cntxt, args, "to", "Enter to date", conditionRule, toCallback)

            if (args.hasOwnProperty('day')) {
                intrrComplete(args)
            } else if (args.hasOwnProperty("from") && args.hasOwnProperty("to")) {
                intrrComplete(args)
            } else if (args.hasOwnProperty("from")) {
                toIntrr()
            } else {
                dayIntrr()
            }
        }
    }

    let isExists = (symbol) => {
        return dataSchemas.hasOwnProperty(symbol);
    }

    if (args.hasOwnProperty('symbol')) {
        symbolCallback(args)
    } else {
        const symIntrr = () => interrogate(cntxt, args, "symbol", "Enter symbol", conditionRule, symbolCallback)
        symIntrr()
    }
}

const updateCommandSchema = (alreadyExists, args) => {
    let commandSchema = {};
    if (alreadyExists) {
        commandSchema = commandSchemas[args.symbol]
    } else {
        commandSchema.data = {}
    }

    commandSchema.operation = args.operation
    commandSchema.command = args.command
    commandSchema.data.symbol = args.symbol

    if (args.hasOwnProperty("day")) {
        commandSchema.data.day = args.day;
    }
    if (args.hasOwnProperty("from") && args.hasOwnProperty("to")) {
        commandSchema.data.from = args.from;
        commandSchema.data.to = args.to;

        delete commandSchema.data.day
    }
    if (args.hasOwnProperty("interval")) {
        commandSchema.data.interval = args.interval;
    }
    let indicator = null;
    if (args.hasOwnProperty("indicators")) {
        indicator = {name: args.indicators, data: args._}
    }
    if (args.hasOwnProperty("realtime")){
        commandSchema.data.realtime = true;
    }

    let prevIndicators = []
    let newIndicators = []
    if (indicator != null) {
        for (let key in commandSchema.data.indicators) {
            if (commandSchema.data.indicators[key].name != null)
                if (indicator.name !== commandSchema.data.indicators[key].name) {
                    prevIndicators.push(commandSchema.data.indicators[key])
                }
        }
        newIndicators.push(indicator)
    } else {
        newIndicators = []
    }

    let commands = operationSchema.chart.commands;

    switch (commandSchema.command) {
        case commands.open.name :
            break;
        case commands.close.name :
            break;
        case commands.add.name :
            commandSchema.data.indicators = [...newIndicators, ...prevIndicators]
            break;
        case commands.set.name :
            commandSchema.data.indicators = [...newIndicators];
            break;
        case commands.remove.name :
            commandSchema.data.indicators = [...prevIndicators]
            break;
    }

    if (!alreadyExists) {
        commandSchemas[args.symbol] = commandSchema;
    }
}

const checkOut = (symbol) => {
    controller.chart.command.checkout(commandSchemas[symbol])
}

const switchChart = (args, cntxt) => {
    if (args['_'].length === 0) {
        throwError(cntxt, "No symbol specified")
    } else {
        let symbol = args['_'][0];
        if (dataSchemas.hasOwnProperty(symbol)) {
            backoffPromptToOperation(cntxt)
            setChartSymbolPrompt(cntxt, symbol)
        } else {
            throwError(cntxt, "Invalid symbol specified")
        }
    }
}

const closeChart = (cntxt) => {
    if (currentOperation + '>' === currentOperationPromptCleared) {
        setPrompt(cntxt, promptUser())
        currentOperation = null;
        currentOperationPrompt = null;
        currentOperationPromptCleared = null;
    } else {
        setChartPrompt(cntxt)
    }
}

const setChartPrompt = (cntxt) => {
    currentOperation = operationSchema.chart.name;
    currentOperationPromptCleared = operationSchema.chart.name + ">"
    currentOperationPrompt = greenText(currentOperationPromptCleared);
    setPrompt(cntxt, currentOperationPrompt);
}

const setChartSymbolPrompt = (cntxt, symbol) => {
    currentOperation = operationSchema.chart.name;
    currentOperationPromptCleared = operationSchema.chart.name + '@' + symbol + ">"
    currentOperationPrompt = greenText(currentOperationPromptCleared);
    setPrompt(cntxt, currentOperationPrompt);
}

const displayGraphDetails = (cntxt) => {
    let text = ''
    for (let key in graphSchemas) {
        for (let k in graphSchemas[key].chartObj) {
            text += key + '/' + k
                + ' ' + (' v --') + graphSchemas[key].chartObj[k].visible
                + ' ' + (' t --') + graphSchemas[key].chartObj[k].type
                + '\n'
        }
    }

    if (text.length > 0) cntxt.echo(text)
    else cntxt.echo('No charts found.')
}

const changeGraphSettings = (cntxt, args) => {
    const update = () => {
        updateGraphs()
    }
    if (args.hasOwnProperty('type')) {
        if (args.type === 0 || args.type === 1) {
            if (args._.length === 0) {
                throwError(cntxt, "No chart id specified. Type 'graphs' to get list of graphs'")
                return;
            }
            let g = args._[0].split("/")
            if (graphSchemas.hasOwnProperty(g[0])) {
                if (graphSchemas[g[0]].chartObj.hasOwnProperty(g[1])) {
                    graphSchemas[g[0]].chartObj[g[1]].type = args.type;
                    update()
                } else {
                    throwError(cntxt, "Invalid chart id. FUCK YOU!!!")
                }
            } else {
                throwError(cntxt, "Invalid chart id. FUCK YOU!!!")
            }
        } else {
            throwError(cntxt, "Invalid param value. Param values '0' or '1'")
        }
    } else if (args.hasOwnProperty('show')) {
        if (args.show === 'true' || args.show === 'false') {
            if (args._.length === 0) {
                throwError(cntxt, "No chart id specified. Type 'graphs' to get list of graphs'")
                return;
            }
            let visible = false;
            if (args.show == 'true') visible = true;
            let g = args._[0].split("/")
            if (graphSchemas.hasOwnProperty(g[0])) {
                if (graphSchemas[g[0]].chartObj.hasOwnProperty(g[1])) {
                    graphSchemas[g[0]].chartObj[g[1]].visible = visible;
                    update()
                } else {
                    throwError(cntxt, "Invalid chart id. FUCK YOU!!!")
                }
            } else {
                throwError(cntxt, "Invalid chart id. FUCK YOU!!!")
            }
        } else {
            throwError(cntxt, "Invalid param value. Param values 'true' or 'false'")
        }
    } else if (args.hasOwnProperty('pos')) {
        if (args.pos === true) {
            throwError(cntxt, "Invalid param value.")
            return;
        }
        try {
            let positionId = args.pos.split("/")
            let toPositionIds = []
            for (let i = 0; i < args._.length; i++) {
                let toPositionId = args._[i].split("/")
                if (positionId[0].split('_')[0] === toPositionId[0].split('_')[0]) {
                    toPositionIds.push(toPositionId)
                } else {
                    throwError(cntxt, "Some ids are different. Dont mix up different symbols BITCH!!")
                }
            }
            for (let i = 0; i < toPositionIds.length; i++) {
                if (graphSchemas.hasOwnProperty(positionId[0])) {
                    if (graphSchemas.hasOwnProperty(toPositionIds[i][0])) {
                        if (graphSchemas[toPositionIds[i][0]].chartObj[toPositionIds[i][1]]) {
                            let obj = graphSchemas[toPositionIds[i][0]].chartObj[toPositionIds[i][1]]
                            graphSchemas[positionId[0]].chartObj[toPositionIds[i][1]] = obj;
                            delete graphSchemas[toPositionIds[i][0]].chartObj[toPositionIds[i][1]]
                            if (Object.keys(graphSchemas[toPositionIds[i][0]].chartObj).length === 0) {
                                delete graphSchemas[toPositionIds[i][0]]
                                destroyWindowFromStack(toPositionIds[i][0])
                            }
                        }
                    }
                }
            }
            update()
        } catch (e) {
            console.log(e)
            throwError(cntxt, "Some ids are not valid")
        }
    } else if (args.hasOwnProperty('split')) {
        if (args.split === true) {
            throwError(cntxt, "Invalid  graph id specified")
            return
        }
        let positionId = args.split.split('/');
        if (graphSchemas.hasOwnProperty(positionId[0])) {
            if (Object.keys(graphSchemas[positionId[0]].chartObj).length === 1) {
                throwError(cntxt, "Graph has a single curve only BITCH!!!")
                return;
            }

            let graphsToSplit = []
            if (args._.length === 0) {
                let graphIds = Object.keys(graphSchemas[positionId[0]].chartObj)
                graphIds.shift()
                graphsToSplit = graphIds
            } else {
                for (let k in args._) {
                    graphsToSplit.push(args._[k].split('/')[1])
                }
            }

            let symbol = positionId[0].split('_')[0]
            let positions = [];
            for (let key in graphSchemas) {
                if (positionId[0].split('_')[0] === key.split('_')[0]) {
                    positions.push(parseInt(key.split('_')[1]));
                }
            }
            const nextPosition = () => {
                let pos = 0;
                for (let i = 0; i < 999999; i++) {
                    if (!positions.includes(i)) {
                        pos = i;
                        break
                    }
                }
                return pos;
            }

            for (let i = 0; i < graphsToSplit.length; i++) {
                let nextPos = nextPosition();
                positions.push(nextPos);
                let obj = graphSchemas[positionId[0]]
                let newObj = {
                    chartObj: {},
                    symbol: symbol,
                    aspectRatio: chartHeightScale,
                    id: obj.id.split('_')[0] + "_" + nextPos,
                    rendering: false
                }

                newObj.drawEvent = new CustomEvent(symbol + '_' + nextPos, {detail: newObj})

                if (chartContainers.hasOwnProperty(symbol + "_" + nextPos)) {
                    newObj.width = chartContainers[symbol + "_" + nextPos].width
                    newObj.height = chartContainers[symbol + "_" + nextPos].height
                }

                newObj.chartObj[graphsToSplit[i]] = obj.chartObj[graphsToSplit[i]]
                graphSchemas[symbol + '_' + nextPos] = newObj
                delete graphSchemas[positionId[0]].chartObj[graphsToSplit[i]]
            }

            update()

        } else {
            throwError(cntxt, "Invalid chart id. FUCK YOU!!!")
        }
    } else if (args.hasOwnProperty('aspectRatio')) {
        if (args.aspectRatio > 0 || args.aspectRatio === 'def') {
            if (args._.length === 0) {
                throwError(cntxt, "No chart id specified. Type 'graphs' to get list of graphs'")
                return;
            }
            let g = args._[0].split("/")
            if (graphSchemas.hasOwnProperty(g[0])) {
                graphSchemas[g[0]].aspectRatio = args.aspectRatio === 'def' ? chartHeightScale : args.aspectRatio
                update()
            } else {
                throwError(cntxt, "Invalid chart id. FUCK YOU!!!")
            }
        } else {
            throwError(cntxt, "Invalid param value. Param values > '0'")
        }
    } else if (args.hasOwnProperty('allDefAspectRatio')) {
        for (let key in graphSchemas) {
            graphSchemas[key].aspectRatio = chartHeightScale
        }
        update()
    } else {
        throwError(cntxt, "Invalid param please type help for command usage.")
    }

}

const updateGraphs = (fullUpdate) => {
    graphObjects.reset()
    graphObjects.resetAutocompletions()
    graphFunctions.clear();
    graphFunctions.render()
}

const clearLogs = () => {
    $('#logger-body').html('')
}

const surfaceChart = (cntxt, args) => {
    if (!args.hasOwnProperty('symbol')) {
        throwError(cntxt, "No symbol specified")
        return
    }
    if (args._.length === 0) {
        throwError(cntxt, "No data specified")
        return
    }

    var z_data = []

    for (let i = 0; i < args._.length; i++) {
        if (Object.keys(dataSchemas[args.symbol].ohlcSeries).includes(args._[i])) {
            z_data.push(dataSchemas[args.symbol].ohlcSeries[args._[i]])
        }
        if (Object.keys(dataSchemas[args.symbol].indicatorSeries).includes(args._[i])) {
            z_data.push(dataSchemas[args.symbol].indicatorSeries[args._[i]])
        }

    }

    if (z_data.length < 3) {
        throwError(cntxt, "Need three datas")
        return;
    }

    var mode = 'lines';
    if (args.hasOwnProperty("mode")){
        mode = args.mode;
    }

    var trace1 = {
        x: z_data[0],
        y: z_data[1],
        z: z_data[2],
        mode: mode,
        marker: {
            size: 7,
            line: {
                color: 'rgba(217, 217, 217, 0.14)',
                width: 0.5
            },
            opacity: 0.8
        },
        line: {
            width: 4,
            color: 'rgb(211,255,203)',
            reversescale: false
        },
        type: 'scatter3d'
    };

    var data = [trace1];

    var layout = {
        "title": args._.join('/'),
        "scene": {
            "xaxis": {"type": "linear", "title": {"font": {"color": "rgb(255, 255, 255)"}}},
            "camera": {
                "up": {"x": 0, "y": 0, "z": 1},
                "eye": {"x": 1.5446328587476053, "y": -0.8239643212077259, "z": 1.2738493353037839},
                "center": {"x": 0, "y": 0, "z": 0},
                "projection": {"type": "perspective"}
            },
            "aspectmode": "auto",
            "aspectratio": {"x": 1, "y": 1, "z": 1}
        },
        "xaxis": {"range": [-1, 6], "autorange": true},
        "yaxis": {"range": [-1, 4], "autorange": true},
        "modebar": {"bgcolor": "rgba(17, 17, 17, 0.5)"},
        "autosize": true,
        "template": {
            "data": {
                "bar": [{"type": "bar", "marker": {"colorbar": {"ticks": "", "outlinewidth": 0}}}],
                "table": [{
                    "type": "table",
                    "cells": {"fill": {"color": "#506784"}, "line": {"color": "rgb(17,17,17)"}},
                    "header": {"fill": {"color": "#2a3f5f"}, "line": {"color": "rgb(17,17,17)"}}
                }],
                "carpet": [{
                    "type": "carpet",
                    "aaxis": {
                        "gridcolor": "#506784",
                        "linecolor": "#506784",
                        "endlinecolor": "#A2B1C6",
                        "minorgridcolor": "#506784",
                        "startlinecolor": "#A2B1C6"
                    },
                    "baxis": {
                        "gridcolor": "#506784",
                        "linecolor": "#506784",
                        "endlinecolor": "#A2B1C6",
                        "minorgridcolor": "#506784",
                        "startlinecolor": "#A2B1C6"
                    }
                }],
                "mesh3d": [{"type": "mesh3d", "colorbar": {"ticks": "", "outlinewidth": 0}}],
                "contour": [{"type": "contour", "colorbar": {"ticks": "", "outlinewidth": 0}, "autocolorscale": true}],
                "heatmap": [{"type": "heatmap", "colorbar": {"ticks": "", "outlinewidth": 0}, "autocolorscale": true}],
                "scatter": [{"type": "scatter", "marker": {"colorbar": {"ticks": "", "outlinewidth": 0}}}],
                "surface": [{"type": "surface", "colorbar": {"ticks": "", "outlinewidth": 0}}],
                "heatmapgl": [{"type": "heatmapgl", "colorbar": {"ticks": "", "outlinewidth": 0}}],
                "histogram": [{"type": "histogram", "marker": {"colorbar": {"ticks": "", "outlinewidth": 0}}}],
                "parcoords": [{"line": {"colorbar": {"ticks": "", "outlinewidth": 0}}, "type": "parcoords"}],
                "scatter3d": [{"type": "scatter3d", "marker": {"colorbar": {"ticks": "", "outlinewidth": 0}}}],
                "scattergl": [{"type": "scattergl", "marker": {"colorbar": {"ticks": "", "outlinewidth": 0}}}],
                "choropleth": [{"type": "choropleth", "colorbar": {"ticks": "", "outlinewidth": 0}}],
                "scattergeo": [{"type": "scattergeo", "marker": {"colorbar": {"ticks": "", "outlinewidth": 0}}}],
                "histogram2d": [{
                    "type": "histogram2d",
                    "colorbar": {"ticks": "", "outlinewidth": 0},
                    "autocolorscale": true
                }],
                "scatterpolar": [{"type": "scatterpolar", "marker": {"colorbar": {"ticks": "", "outlinewidth": 0}}}],
                "contourcarpet": [{"type": "contourcarpet", "colorbar": {"ticks": "", "outlinewidth": 0}}],
                "scattercarpet": [{"type": "scattercarpet", "marker": {"colorbar": {"ticks": "", "outlinewidth": 0}}}],
                "scattermapbox": [{"type": "scattermapbox", "marker": {"colorbar": {"ticks": "", "outlinewidth": 0}}}],
                "scatterpolargl": [{
                    "type": "scatterpolargl",
                    "marker": {"colorbar": {"ticks": "", "outlinewidth": 0}}
                }],
                "scatterternary": [{
                    "type": "scatterternary",
                    "marker": {"colorbar": {"ticks": "", "outlinewidth": 0}}
                }],
                "histogram2dcontour": [{
                    "type": "histogram2dcontour",
                    "colorbar": {"ticks": "", "outlinewidth": 0},
                    "autocolorscale": true
                }]
            }, "layout": {
                "geo": {
                    "bgcolor": "rgb(17,17,17)",
                    "showland": true,
                    "lakecolor": "rgb(17,17,17)",
                    "landcolor": "rgb(17,17,17)",
                    "showlakes": true,
                    "subunitcolor": "#506784"
                },
                "font": {"color": "#f2f5fa"},
                "polar": {
                    "bgcolor": "rgb(17,17,17)",
                    "radialaxis": {"ticks": "", "gridcolor": "#506784", "linecolor": "#506784"},
                    "angularaxis": {"ticks": "", "gridcolor": "#506784", "linecolor": "#506784"}
                },
                "scene": {
                    "xaxis": {
                        "ticks": "",
                        "gridcolor": "#506784",
                        "gridwidth": 2,
                        "linecolor": "#506784",
                        "zerolinecolor": "#C8D4E3",
                        "showbackground": true,
                        "backgroundcolor": "rgb(17,17,17)"
                    },
                    "yaxis": {
                        "ticks": "",
                        "gridcolor": "#506784",
                        "gridwidth": 2,
                        "linecolor": "#506784",
                        "zerolinecolor": "#C8D4E3",
                        "showbackground": true,
                        "backgroundcolor": "rgb(17,17,17)"
                    },
                    "zaxis": {
                        "ticks": "",
                        "gridcolor": "#506784",
                        "gridwidth": 2,
                        "linecolor": "#506784",
                        "zerolinecolor": "#C8D4E3",
                        "showbackground": true,
                        "backgroundcolor": "rgb(17,17,17)"
                    }
                },
                "title": {"x": 0.05},
                "xaxis": {
                    "ticks": "",
                    "gridcolor": "#283442",
                    "linecolor": "#506784",
                    "automargin": true,
                    "zerolinecolor": "#283442",
                    "zerolinewidth": 2
                },
                "yaxis": {
                    "ticks": "",
                    "gridcolor": "#283442",
                    "linecolor": "#506784",
                    "automargin": true,
                    "zerolinecolor": "#283442",
                    "zerolinewidth": 2
                },
                "ternary": {
                    "aaxis": {"ticks": "", "gridcolor": "#506784", "linecolor": "#506784"},
                    "baxis": {"ticks": "", "gridcolor": "#506784", "linecolor": "#506784"},
                    "caxis": {"ticks": "", "gridcolor": "#506784", "linecolor": "#506784"},
                    "bgcolor": "rgb(17,17,17)"
                },
                "colorway": ["#636efa", "#EF553B", "#00cc96", "#ab63fa", "#19d3f3", "#e763fa", "#fecb52", "#ffa15a", "#ff6692", "#b6e880"],
                "hovermode": "closest",
                "colorscale": {
                    "diverging": [[0, "#8e0152"], [0.1, "#c51b7d"], [0.2, "#de77ae"], [0.3, "#f1b6da"], [0.4, "#fde0ef"], [0.5, "#f7f7f7"], [0.6, "#e6f5d0"], [0.7, "#b8e186"], [0.8, "#7fbc41"], [0.9, "#4d9221"], [1, "#276419"]],
                    "sequential": [[0, "#0508b8"], [0.0893854748603352, "#1910d8"], [0.1787709497206704, "#3c19f0"], [0.2681564245810056, "#6b1cfb"], [0.3575418994413408, "#981cfd"], [0.44692737430167595, "#bf1cfd"], [0.5363128491620112, "#dd2bfd"], [0.6256983240223464, "#f246fe"], [0.7150837988826816, "#fc67fd"], [0.8044692737430168, "#fe88fc"], [0.8938547486033519, "#fea5fd"], [0.9832402234636871, "#febefe"], [1, "#fec3fe"]],
                    "sequentialminus": [[0, "#0508b8"], [0.0893854748603352, "#1910d8"], [0.1787709497206704, "#3c19f0"], [0.2681564245810056, "#6b1cfb"], [0.3575418994413408, "#981cfd"], [0.44692737430167595, "#bf1cfd"], [0.5363128491620112, "#dd2bfd"], [0.6256983240223464, "#f246fe"], [0.7150837988826816, "#fc67fd"], [0.8044692737430168, "#fe88fc"], [0.8938547486033519, "#fea5fd"], [0.9832402234636871, "#febefe"], [1, "#fec3fe"]]
                },
                "plot_bgcolor": "#222222",
                "paper_bgcolor": "#222222",
                "shapedefaults": {"line": {"width": 0}, "opacity": 0.4, "fillcolor": "#f2f5fa"},
                "sliderdefaults": {
                    "bgcolor": "#C8D4E3",
                    "tickwidth": 0,
                    "bordercolor": "rgb(17,17,17)",
                    "borderwidth": 1
                },
                "annotationdefaults": {"arrowhead": 0, "arrowcolor": "#f2f5fa", "arrowwidth": 1},
                "updatemenudefaults": {"bgcolor": "#506784", "borderwidth": 0}
            }, "themeRef": "PLOTLY_DARK"
        },
        "showlegend": false,
        "height": 600,
        "paper_bgcolor": "#222222",
    };

    addSurfaceWindowToStack(Math.random().toString(), data, layout)

}

const snap = (cntxt, args) => {
    if (args._.length === 0) {
        throwError(cntxt, "No chart id specified")
        return
    }

    let x = args._[0].split('/')[0]

    if (graphSchemas.hasOwnProperty(x)){
        let c = graphSchemas[x]
        let title = x.split('_')
        if ($("#chart-" + c.id).length === 0){
            throwError(cntxt, "No visible chart. Maybe the chart is not visible. Please show the chart first")
        }else{
            let html = $("#chart-" + c.id).html();
            let newHtml = '<div style="display: inline-block; width: 100%; height: inherit; overflow-y: scroll" >' + html + '</div>';

            addOtherWindowToStack(title[0] + "(" + title[1] + ")-SNAP", newHtml)
        }
    }else{
        throwError(cntxt, "Invalid chart id")
    }

}