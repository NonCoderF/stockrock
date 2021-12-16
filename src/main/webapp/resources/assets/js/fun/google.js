const launchGoogle = (args) => {
    var strWindowFeatures = "location=yes,height=570,width=520,scrollbars=yes,status=yes";
    var URL = "https://www.google.com/search?q=" + args._.join('+');
    var win = window.open(URL);
}

const launchSite = (args) => {
    var URL = "https://" + args._.join('')
    var win = window.open(URL)
}