const googleFunctions = {
    google :  function (...args) {
        const options = $.terminal.parse_options(args);
        launchGoogle(options)
    },
    visit : function(...args) {
        const options = $.terminal.parse_options(args);
        launchSite(options)
    }
}