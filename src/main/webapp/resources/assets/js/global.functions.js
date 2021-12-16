var globalFunctions = {
    clearAll: function () {
        $('#chartcontent').html('')
    }
}

var fnObject = {
    ...helpFunctions,
    ...youtubeFunctions,
    ...googleFunctions,
    ...movieFunctions,
    ...dictionaryFunctions,
    ...serverTerminalFunctions,
    ...chartFunctions,
    ...globalFunctions
};