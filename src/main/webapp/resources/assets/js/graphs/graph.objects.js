const graphFunctions = {

    render: () => {
        for (let key in graphSchemas) {
            let obj = graphSchemas[key]
            graphRenderer.render(key, obj)
        }
    },

    clear: () => {
        for (let key in graphSchemas) {
            $('.' + graphSchemas[key].symbol).html('')
        }
    },

};

let graphObjects = {
    charts: [],

    reset: () => {
        graphObjects.charts = []

        for(let key in graphSchemas){
            if (graphSchemas[key].chartObj !== undefined && Object.keys(graphSchemas[key].chartObj).length > 0){
                graphObjects.charts.push(graphSchemas[key].chartObj)
            }
        }
    },
    add: (graph, type, visible) => {
        let doExists = false;
        for (let i = 0; i < graphObjects.charts.length; i++){
            if (graphObjects.charts[i] !== undefined){
                if (Object.keys(graphObjects.charts[i]).includes(graph)){
                    doExists = true;
                    break;
                }
            }
        }

        if (!doExists){
            let g = {}
            g[graph] = {visible: visible, type: type, color : randomColor()}
            graphObjects.charts.push(g)
        }
    },

    clearResiduals : (symbol) => {
        graphObjects.clearResidualsFromSchema(symbol)
    },

    clearResidualsFromSchema : (symbol) => {
        let prevKeys = []
        for (let key in graphSchemas) if (key.split('_')[0] === symbol) prevKeys.push(key)

        prevKeys.map(function (e) {
            delete graphSchemas[e]
        })

        let keys = [...Object.values(dataSchemas[symbol].availableCurves),
            ...Object.keys(dataSchemas[symbol].indicatorSeries)]

        for (let k in graphObjects.charts){
            for (let l in graphObjects.charts[k]){
                let m = l.split('#');
                if (m[0] === symbol){
                    if (!keys.includes(m[1])){
                        delete graphObjects.charts[k][l]
                    }
                }
            }
        }

        graphObjects.charts.filter((e) => {
            return Object.keys(e).length > 0
        })

    },

    clearWindows : () => {
        let keys = Object.keys(graphSchemas)
        for (let k in chartContainers){
            if (!keys.includes(k)){
                destroyWindowFromStack(k)
                delete chartContainers[k]
            }
        }
    },

    resetAutocompletions : () => {
        additionalCompletions = []
        for (let key in graphSchemas) {
            for (let k in graphSchemas[key].chartObj) {
                additionalCompletions.push(key + '/' + k)
            }
        }
    }
}