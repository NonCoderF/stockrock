const chartFunctions = {
    chart : function () { setChartPrompt(this) },

    switch : function (...args) {
        const options = $.terminal.parse_options(args);
        switchChart(options, this)
    },
    prop : function (...args) {
        const options = $.terminal.parse_options(args);
        changeGraphSettings(this, options)
    },

    surface : function (...args){
        const options = $.terminal.parse_options(args);
        surfaceChart(this, options)
    },

    snap : function (...args){
        const options = $.terminal.parse_options(args);
        snap(this, options)
    },

    clearLogs : function(...args) {clearLogs()},

    update : function (...args) {updateGraphs()},
    close : function(...args){ closeChart(this) },
    graphs : function(...args){ displayGraphDetails(this) },

    open : function (...args) { parseAndPrepareChartData(this, args, operationSchema.chart.commands.open.name) },
    set : function (...args) { parseAndPrepareChartData(this, args, operationSchema.chart.commands.set.name) },
    add : function (...args) { parseAndPrepareChartData(this, args, operationSchema.chart.commands.add.name) },
    remove : function (...args) { parseAndPrepareChartData(this, args, operationSchema.chart.commands.remove.name) },


}

const parseSymbol = () => {
    if (currentOperationPromptCleared === null) return null;
    let y = currentOperationPromptCleared.replace('>', '').split('@')

    if (y.length > 1){
        return y[1]
    }else{
        return null;
    }
}

const parseAndPrepareChartData = (cntxt, args, command) => {
    if (currentOperation === operationSchema.chart.name){
        const options = $.terminal.parse_options(args);
        options.operation = currentOperation
        options.command = command

        var symbol = parseSymbol();
        if (symbol != null) options.symbol = symbol;

        prepareCommandSchema(cntxt, options);
    }else{
        throwError(cntxt, INVALID_COMMAND)
    }
}