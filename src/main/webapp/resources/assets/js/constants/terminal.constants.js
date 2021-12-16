const loadFiglet = (onReady) => {
    figlet.defaults({fontPath: 'resources/assets/fonts'});
    figlet.preloadFonts(["Small"], onReady);
}

const greetings = () => {
    return greetingsTitle() + '\nWelcome to Stock Analysis\nType [[b;#fff;]help] for all list of available commands';
}

const greetingsTitle = () => {
    let x = ""
    x += "################################################"
    x += "\n"
    x += "################   STOCKROCK   #################"
    x += "\n"
    x += "################################################"
    x += "\n"
    return x;
}

const greetingsTitleBig = () => {
    return figlet.textSync("STOCKROCK", {
        font: 'Small',
        width: 50,
        whitespaceBreak: true
    })
}

const keyMap = () => {
    return {
        'CTRL+C': function (e, original) {
            original();
            if (terminalPrompt !== promptUser()){
                if (terminalPrompt === currentOperationPrompt){
                    backoffPromptToRoot(this)
                }else{
                    this.pop()
                    terminalPrompt = this.get_prompt();
                    console.log(terminalPrompt + ", " + currentOperationPrompt)
                }
            }
        }
    }
}

const promptUser = () => {
    return '[[;#00ff00;]user@server$ ]';
};

const backoffPromptToRoot = (cntxt) => {
    backoffPromptToOperation(cntxt);
    currentOperation = null;
    currentOperationPrompt = null;
    currentOperationPromptCleared = null;
    setPrompt(cntxt, promptUser())
}

const autoCompletions = (command, callback) => {
    let x = [];
    if (terminalPrompt === promptUser()) {
        x = Object.keys(operationSchema);
    } else {
        x = Object.keys(operationSchema[currentOperation].commands)
    }
    if (!x.includes("help")) x.push("help");
    x.push(...additionalCompletions)
    callback(x);
}

const setPrompt = (cntxt, prompt) => {
    cntxt.set_prompt(prompt);
    terminalPrompt = prompt;
}

const backoffPrompt = (cntxt, steps) => {
    for (let i = 0; i < steps; i++){
        cntxt.pop();
        terminalPrompt = cntxt.get_prompt();
    }
}

const backoffPromptToOperation = (cntxt) => {
    for (let i = 0; i < 3; i++){
        if (terminalPrompt !== currentOperationPrompt){
            cntxt.pop();
            terminalPrompt = cntxt.get_prompt();
        }else{
            break;
        }
    }
}

const showTermResult = (cntxt, message) => {
    cntxt.echo(message);
}

const throwError = (cntxt, message) => {
    cntxt.echo("[[;#ff0000;]" + message + "]");
};

const interrogate = (cntxt, args, parameter, message, condition, callback) => {
    if (!args.hasOwnProperty(parameter) && !args[parameter]){
        terminalPrompt =  whiteTextBold(message + " : ")
        cntxt.push(function (input) {
            if (condition(input)){
                args[parameter] = input;
                callback(args)
            }else{
                interrogate(cntxt, args, parameter, message, condition, callback)
            }
        }, {prompt : terminalPrompt})
    }else{
        callback(args)
    }
}

const getCommandDesc = (object) => {
    let desc = '';
    for (let key in object) {
        desc += object[key].name + " --> " + object[key].desc + "\n";
    }
    return desc;
}