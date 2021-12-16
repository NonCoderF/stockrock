const SOCKET_CONN_OPEN = "socketOpen";
const SOCKET_CONN_CLOSED = "socketClose"
let socketStatus = "";
const socketUrl = 'ws://' + window.location.host + '/stockrock/term';

const socketConn = (onMessage) => {
    var ws = new WebSocket(socketUrl)

    ws.onopen = () => {
        socketStatus = SOCKET_CONN_OPEN;
    }

    ws.onmessage = (event) => {
        onMessage(event.data)
    }

    ws.onclose = () => {
        socketStatus = SOCKET_CONN_CLOSED
    }

    return ws;

}