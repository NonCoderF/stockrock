const controller = {
    global : {
        send : (args) => {
            hostDriver.unilateral.pass(args)
        }
    },
    chart : {
        command : {
            checkout : (args) => {
                hostDriver.unilateral.pass(args)
            }
        },
        data : {
            collect : (datatype, args) => {
                let model = new CollectorModel(datatype, args)
                chartCollector.analyse(model)
                controller.chart.graph.analyze(model.data.data.symbol)
            }
        },
        graph : {
            analyze : (symbol) => {
                graphDataAnalyzer.analyze(symbol)
                graphDataAnalyzer.logs()
                controller.chart.graph.visualize()
            },
            visualize : () => {
                graphFunctions.clear()
                graphFunctions.render()
            }
        }

    }
}