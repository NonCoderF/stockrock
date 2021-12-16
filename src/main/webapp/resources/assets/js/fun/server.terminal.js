
const executeServerCommand = (args) => {
    let obj = {
        operation : "terminal",
        command : args.join(" ")
    }
    controller.global.send(obj)
}