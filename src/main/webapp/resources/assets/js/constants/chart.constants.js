const operationSchema = {
    chart: {
        name : "chart",
        desc : "Open Chart. usage type [[b;#fff;]chart]",
        commands : {
            open : {
                name : "open",
                desc : "Opens chart data. Usage type [[b;#fff;]open] --symbol {XXX} --from {XXX} --to {XXX} ... "
            },
            add : {
                name : "add",
                desc : "Add new data. Usage [[b;#fff;]add] --symbol {XXX} --indicators {XXX} {XX} {XX} ... "
            },
            set : {
                name : "set",
                desc : "Sets new data. Usage [[b;#fff;]set] --symbol {XXX} --indicators {XXX} {XX} {XX} ... "
            },
            remove : {
                name : "remove",
                desc : "Remove previous data. Usage [[b;#fff;]remove] --symbol {XXX} --indicators {XXX} {XX} {XX} ... "
            },
            update : {
                name : "update",
                desc : "Update all graphs chart"
            },
            close : {
                name : "close",
                desc : "Closes current chart"
            },
            switch : {
                name : "switch",
                desc : "Switches between charts"
            },
            clearLogs : {
                name : "clearLogs",
                desc : "Clears system Logs"
            },
            graphs : {
                name : 'graphs',
                desc : "Displays details of all graphs"
            },
            prop : {
                name : 'prop',
                desc : "Changes graph properties. Usage --type, --show, --pos, --split, --aspectRatio, --allDefAspectRatio. Eg : prop --show true {XXX}"
            },
            surface : {
                name : 'surface',
                desc : "Creates 3D chart. Usage surface --mode {XXX} {X-axis data} {Y-axis data} {Z-axis data}. mode : lines, scatter3d --optional"
            },
            snap : {
                name : 'snap',
                desc : 'Usage snap {XXX}. Create a snapshot window of the current viewport of the chart'
            }
        }
    },
    help: {
        name : "help",
        desc : "Type [[b;#fff;]help] for list of available commands"
    },
    youtube: {
        name : "Youtube",
        desc : "Usage [[b;#fff;]youtube] {XXX}"
    },
    movie: {
        name : "movie",
        desc : "Usage [[b;#fff;]movie] {XXX}"
    },
    dict: {
        name : "dict",
        desc : "Usage [[b;#fff;]dict] {XXX}"
    },
    google: {
        name : "Google",
        desc : "Usage [[b;#fff;]google] {XXX}"
    },
    visit: {
        name : "Visit any URL",
        desc : "Usage [[b;#fff;]visit] {XXX}"
    },
    terminal: {
        name : "terminal",
        desc : "Server terminal to exec commands on the server",
        commands : {
            exec : {
                name : "exec",
                desc : "Executes a command"
            }
        }
    }
}