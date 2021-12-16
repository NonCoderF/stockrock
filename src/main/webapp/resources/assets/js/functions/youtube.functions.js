const youtubeFunctions = {
    youtube :  function (...args) {
        const options = $.terminal.parse_options(args);
        findVideos(options)
    }
}

const findVideos = (args) => {
    var search = ''
    var word = args._.join('+')

    if (word.length === 0) {
        SysErrorLog("No search query specified")
        return;
    } else {
        search += word
    }

    var url = "https://www.googleapis.com/youtube/v3/search?part=snippet&type=video&maxResults=5&q=" + search + "&key=AIzaSyCyitvXPV2BuaJ1WrAyRSrmnjLbfLhUHjc"

    SysOkayLog("Searching " + word + "...")

    Logger.spinnerStart()

    $.getJSON(url, function (data) {

        SysOkayLog("Showing first 5 videos")

        let videos = '<div style="font-family: monospace" " >'

        for (let i = 0; i < data.items.length; i++) {
            let v = data.items[i]
            videos += '<div style="color: #aaa; display: flex; margin: 5px">'
            videos += '<div style="width: 300px"><iframe src="https://www.youtube.com/embed/'+ v.id.videoId +'" allowfullscreen></iframe></div>'
            videos +='<div style="margin: 10px; height: 20vh">'
            videos += '<div>' + v.snippet.title + '</div>'
            videos += '<br>'
            videos += '<div>' + v.snippet.channelTitle + '</div>'
            // videos += '<h5>' + v.snippet.description + '</h5>'
            videos += '</div>'
            videos += '</div>'
        }

        videos += '</div>'

        if ($('#youtube-window').length === 0){
            addOtherWindowToStack('Youtube', '<div id="youtube-window" style="overflow-y: scroll; height: inherit" >' + videos + '</div>')
        }else{
            $('#youtube-window').html(videos)
        }

        Logger.spinnerStop()

    });

}