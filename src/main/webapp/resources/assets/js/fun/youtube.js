const launchYoutube = (args) => {
    var strWindowFeatures = "location=yes,height=570,width=520,scrollbars=yes,status=yes";
    var URL = "https://www.youtube.com/results?search_query=" + args._.join('+');
    var win = window.open(URL);
}