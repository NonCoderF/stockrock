const serverTerminalFunctions = {
    terminal : function () {
        currentOperation = operationSchema.terminal.name;
        currentOperationPrompt = greenTextBold(operationSchema.terminal.name + ">");
        setPrompt(this, currentOperationPrompt)
    },

    exec : function (...args) {
        if (currentOperation === operationSchema.terminal.name){
            executeServerCommand(args)
        }else{
            throwError(this, INVALID_COMMAND)
        }
    }
}