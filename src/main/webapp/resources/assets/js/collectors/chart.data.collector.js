const chartCollector = {
    analyse: (model) => {
        switch (model.dataType) {
            case DATA_TYPE_COMMAND:
                chartDatAnalyzer.oneShotAnalyse(model.data.data)
                break;
            case DATA_TYPE_REALTIME:
                chartDatAnalyzer.multiSlotsAnalyse(model.data.data)
                break;
        }
    }
}

const chartDatAnalyzer = {

    oneShotAnalyse: (data) => {
        let symbol = data.symbol
        if (commandSchemas.hasOwnProperty(symbol)){
            commandSchemas[symbol].data.id = data.id
        }
        dataSchemas[symbol] = data;

    },

    multiSlotsAnalyse: (data) => {
    }

}