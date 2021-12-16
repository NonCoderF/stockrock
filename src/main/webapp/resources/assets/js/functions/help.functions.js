const helpFunctions = {
    help : function () {
        if (terminalPrompt === promptUser()){
            showTermResult(this, getCommandDesc(operationSchema))
        }else{
            showTermResult(this, getCommandDesc(operationSchema[currentOperation].commands));
        }
    }
}