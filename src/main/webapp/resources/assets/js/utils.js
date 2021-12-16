const Live = (live) => {
    console.log(live)
    if (live) $("#live-icon").css("display", "block");
    else $("#live-icon").css("display", "none");
}

const isLive = () => {
    return $("#live-icon").css("display") == "none" ? false : true
}

const Logger = {
    type: 'OK',
    log: (message) => {
        let loggerBody = $('#logger-body');
        let color = '#45D40C'
        if (Logger.type !== 'OK')
            color = '#f60e0e'
        loggerBody.append('<li style="color: ' + color + '" >' + message + '</li>')
        $('#logger').scrollTop(function () {
            return this.scrollHeight;
        });
    },
    progress: (percent) => {
        let steps = 10;
        let hashSteps = parseInt(10 * percent / 100)
        let barSteps = steps - hashSteps;
        let progress = '[';
        for (let i = 0; i < hashSteps; i++) {
            progress += '#'
        }
        for (let i = 0; i < barSteps; i++) {
            progress += '-'
        }
        progress += ']';
        return progress + " " + percent + "%";
    },
    spinnerRunning : false,
    spinnerInterval : null,
    spinnerStart : () => {
        if (Logger.spinnerRunning) return;
        Logger.spinnerRunning = true;
        var h = [
            "▰▱▱▱▱▱▱▱▱▱",
            "▰▰▱▱▱▱▱▱▱▱",
            "▰▰▰▱▱▱▱▱▱▱",
            "▰▰▰▰▱▱▱▱▱▱",
            "▰▰▰▰▰▱▱▱▱▱",
            "▰▰▰▰▰▰▱▱▱▱",
            "▰▰▰▰▰▰▰▱▱▱",
            "▰▰▰▰▰▰▰▰▱▱",
            "▰▰▰▰▰▰▰▰▰▱",
            "▰▰▰▰▰▰▰▰▰▰",
        ]
        var i = 0;

        Logger.spinnerInterval = setInterval(() => {
            i = (i > 9) ? 0 : i;
            $('#spinner').html(h[i])
            i++;
        }, 25);
    },
    spinnerStop : () => {
        Logger.spinnerRunning = false;
        clearInterval(Logger.spinnerInterval)
        $('#spinner').html("▰▰▰▰▰▰▰▰▰▰")
    }
}

const SysLog = {
    out: (object) => {
        let status = object.status;
        let message = object.message;
        let data = object.data;

        Logger.type = status;
        if (status === 'ERROR') {
            Logger.log(status + ", " + data.info)
            Logger.spinnerStop()
        } else {
            if (data == null) {
                Logger.log(message);
                Logger.spinnerStop()
            } else {
                let type = data.type;
                let service = data.service;
                let status = data.status;
                let progress = data.progress;

                if (status === "COMPLETED" || status === "FAILED"){
                    Logger.spinnerStop()
                }else{
                    Logger.spinnerStart()
                }

                Logger.log(type + ">" + service + ">" + status + " " + Logger.progress(parseInt(progress)))
            }
        }
    }
}

const SysOkayLog = (message) => {
    SysLog.out({ status : 'OK', message : message })
}

const SysErrorLog = (message) => {
    SysLog.out({ status : 'ERROR', message : message })
}

const resetSymbolHeads = (symbols) => {
    $('.symbols-span').remove();
    for (let i = 0; i < symbols.length; i++){
        $('#symbols-head').append('<span class="symbols-span">' + symbols[i] + '</span>')
    }
}

const randomColor = () => {
    var letters = '66BCDEF'.split('');
    var color = '#';
    for (var i = 0; i < 6; i++ ) {
        color += letters[Math.floor(Math.random() * letters.length)];
    }
    return color;
}