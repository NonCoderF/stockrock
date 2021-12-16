const destroyWindowFromStack = (key) => {
    if (chartContainers.hasOwnProperty(key)) {
        chartContainers[key].close()
    }
}

const addChartWindowToStack = (key, id, symbol) => {
    var title = key.split("_")
    var newItemConfig = {
        title: title[0] + '(' + title[1] + ')',
        type: 'component',
        componentName: 'charts',
        componentState: {key: key, id: id, symbol: symbol, type : 'graph'}
    };

    myLayout.root.contentItems[0].contentItems[0].addChild(newItemConfig);
}

const addOtherWindowToStack = (title, html) => {
    var newItemConfig = {
        title: title,
        type: 'component',
        componentName: 'charts',
        componentState: {html : html, type : 'other'}
    };

    myLayout.root.contentItems[0].contentItems[0].addChild(newItemConfig);
}

const addSurfaceWindowToStack = (div, data, layout) => {
    var newItemConfig = {
        title: "3D Chart",
        type: 'component',
        componentName: 'charts',
        componentState: {div : div, data : data, layout : layout, type : 'surface'}
    };

    myLayout.root.contentItems[0].contentItems[0].addChild(newItemConfig);
}

const charts = function (container, state) {

    if (state.type === 'graph'){
        chartContainers[state.key] = container

        container.on('destroy', function (e) {
            let key = e.config.componentState.key;
            if (graphSchemas.hasOwnProperty(key)) {
                for (let k in graphSchemas[key].chartObj) {
                    graphSchemas[key].chartObj[k].visible = false;
                }
            }
            if (chartContainers.hasOwnProperty(key)) {
                delete chartContainers[key]
            }
        })
        container.on('resize', function () {

            if (graphSchemas.hasOwnProperty(state.key)){
                if (graphSchemas[state.key].width === undefined){
                    graphSchemas[state.key].width = container.width
                    graphSchemas[state.key].height = container.height
                }else{
                    graphSchemas[state.key].prevWidth = graphSchemas[state.key].width
                    graphSchemas[state.key].prevHeight = graphSchemas[state.key].height
                    graphSchemas[state.key].width = container.width
                    graphSchemas[state.key].height = container.height
                }
                document.dispatchEvent(graphSchemas[state.key].drawEvent)
            }
        })

        container.getElement().html('<div id="chart-' + state.id + '" class="' + state.symbol + '" style="display: inline-block; width: 100%; height: inherit; overflow-y: scroll" ></div>');

    }
    else if (state.type === 'surface'){
        container.getElement().html('<div id="' + state.div + '" ></div>')

        setTimeout(() => {
            Plotly.newPlot(state.div, state.data, state.layout);
        }, 500)
    }
    else{
        container.getElement().html(state.html)
    }
};

const terminal = function (container, state) {
    container.getElement().html(
        '<div id="term"></div>'
    );
    $(document).ready(function () {
        App.start()
    });
};

const logs = function (container, state) {
    container.getElement().html(
        '<div id="logger" class="shell" >' +
        '<div id="logger-body" class="shell-body shell-body-logger" ></div>' +
        '</div>'
    );
};

var config = {
    content: [{
        settings: {
            showPopoutIcon: false,
            showMaximiseIcon: true,
            showCloseIcon: true,
            closePopoutsOnUnload: false
        },
        type: 'row',
        content: [
            {
                isClosable: false,
                type: "stack",
                content: []
            },
            {
                isClosable: false,
                type: 'column',
                content: [
                    {
                        id: "terminal",
                        title: "Terminal",
                        isClosable: false,
                        type: 'component',
                        componentName: 'terminal',
                    },
                    {
                        title: "Logs",
                        isClosable: false,
                        type: 'component',
                        componentName: 'logs',
                    }
                ]
            }]
    }]
};
var chartContainers = {}

var myLayout = new GoldenLayout(config, '#container');

myLayout.registerComponent('charts', charts);
myLayout.registerComponent('terminal', terminal);
myLayout.registerComponent('logs', logs);
myLayout.init();