const terminalInitiation = {

    initDimensions : () => {
        screenDimen.width = $(window).width();
        screenDimen.height = $(window).height();
    },

    initPrompt : () => {
        terminalPrompt = promptUser()
    },

    initFiglet : () => {
        loadFiglet(() => {
            $('#term').terminal(
                fnObject,
                {
                    checkArity: false,
                    autocompleteMenu: true,
                    completion: (command, callback) => {
                        autoCompletions(command, callback)
                    },
                    greetings: greetings(),
                    prompt: terminalPrompt,
                    keymap: keyMap()
                });
        })
    },

    start : () => {
        let t = terminalInitiation
        t.initDimensions()
        t.initPrompt()
        t.initFiglet()
    }

}

