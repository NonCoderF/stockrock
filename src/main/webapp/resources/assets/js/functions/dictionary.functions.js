const dictionaryFunctions = {
    dict: function (...args) {
        const options = $.terminal.parse_options(args);
        findWord(options)
    }
}

const findWord = (args) => {
    var url = "https://api.dictionaryapi.dev/api/v2/entries/en/"
    var word = args._.join('')

    if (word.length === 0) {
        SysErrorLog("No word specified")
        return;
    } else {
        url += word
    }

    SysOkayLog("Searching " + word + "...")

    Logger.spinnerStart()

    $.getJSON(url, function (data) {
        if (data instanceof Array) {
            let wordlist = "<div style='margin: 10px'>";

            SysOkayLog("Found " + data.length + " results");

            for (let i = 0; i < data.length; i++) {
                let w = data[i]
                wordlist += '<div style="color: #aaaaaa" > >>> ' + w.word + '</div>'

                for (let j = 0; j < w.meanings.length; j++) {
                    let m = w.meanings[j]
                    wordlist += '<div style="width:fit-content; color: black; background-color: #aaa; padding-left: 5px; padding-right: 5px" ><strong>' + m.partOfSpeech + '</strong></div>'
                    for (let k = 0; k < m.definitions.length; k++) {
                        wordlist += '<div style="color: #aaa" >Def : ' + m.definitions[k].definition + '</div>'
                        if (m.definitions[k].hasOwnProperty('synonyms'))
                            wordlist += '<div style="color: #aaa" >Synonyms : ' + m.definitions[k].synonyms.join(', ') + '</div>'
                        if (m.definitions[k].hasOwnProperty('example'))
                            wordlist += '<div style="font-style: italic; color: #aaa" >Eg : ' + m.definitions[k].example + '</div>'

                        wordlist += '<br>'
                    }
                    wordlist += '<br>'
                }

            }

            wordlist += "</div>"

            if ($('#dictionary-window').length === 0){
                addOtherWindowToStack('Dictionary', '<div id="dictionary-window" style="overflow-y: scroll; height: inherit" >' + wordlist + '</div>')
            }else{
                $('#dictionary-window').html(wordlist)
            }

            Logger.spinnerStop()

        }
    })
}