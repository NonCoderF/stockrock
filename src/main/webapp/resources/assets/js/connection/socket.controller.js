const hostDriver = {

    unilateral: {
        passRaw: (args) => {
            socketDriver.conn.send(args)
        },
        pass: (args) => {
            socketDriver.conn.send(JSON.stringify(args))
        }
    },

    listener: {
        receive: (args) => {
            try {
                let json = JSON.parse(args);
                SysLog.out(json)
                if (typeof json.data == 'object')
                    if (json.data.hasOwnProperty("data")) {
                        switch (json.data.type) {
                            case operationSchema.chart.name:
                                controller.chart.data.collect(DATA_TYPE_COMMAND, json.data)
                                break;
                            case operationSchema.terminal.name:
                                break;
                            case operationSchema.youtube.name:
                                break;
                        }
                    }
            } catch (e) {
                console.log(e)
            }
        }
    }
}

const socketDriver = {
    conn: null,
    incoming: (data) => {
        hostDriver.listener.receive(data)
    }
}

socketDriver.conn = socketConn(socketDriver.incoming)