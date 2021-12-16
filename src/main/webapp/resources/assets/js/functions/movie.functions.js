const movieFunctions = {
    movie: function (...args) {
        const options = $.terminal.parse_options(args);
        findMovies(options)
    }
}

const findMovies = (args) => {
    var url = "https://yts.mx/api/v2/list_movies.json"
    var movie = args._.join('+')
    if (movie !== "latest") {
        url += '?query_term=' + movie
    }

    SysOkayLog("Searching movie " + movie + "...")

    console.log(url)

    Logger.spinnerStart()

    $.getJSON(url, function (data) {

        if (data.status === 'ok') {
            SysOkayLog("Found " + data.data.movie_count + " results")

            var movieCount = '<div style="color: #aaaaaa"> >>> Movies found ' + data.data.movie_count + '</div>'

            var movies = '<br>'

            if (data.data.movies != null)
            for (let i = 0; i < data.data.movies.length; i++) {
                let m = data.data.movies[i];

                movies += '<div style="color: #aaaaaa"><strong><u>' + m.title + '-' + m.year + '</u></strong>(' + m.genres.join(',') + ')</div>' +
                    '<div style="color: #aaa; display: flex; margin: 10px">' +
                    '<img src="' + m.medium_cover_image + '" width="100" style="margin-right: 10px;"/>' + m.synopsis +
                    '</div>'

                movies += '<div>';

                movies += '<span style="color: black; background-color: #d3d3d396" >IMDB(' + m.rating + '/10)</span>'

                for (let j = 0; j < m.torrents.length; j++) {
                    let t = m.torrents[j];
                    movies += '<a target="_blank" href="' + t.url + '" style="color: darkcyan !important; margin: 5px">' + t.quality + ' ' + t.type + ' ' + '(' + t.size + ')' + '</a>'
                }

                movies += '<a target="_blank" href="https://www.youtube.com/watch?v=' + m.yt_trailer_code + '" style="color: darkcyan !important; margin: 5px">Watch YT Trailer</a>'
                movies += '</div>'
                movies += '<br>'
            }

            if ($('#movie-window').length === 0){
                addOtherWindowToStack('Movies', '<div id="movie-window" style="margin: 10px; overflow-y: scroll; height: inherit" >' + status + movieCount + movies + '</div>')
            }else{
                $('#movie-window').html(status + movieCount + movies)
            }

            Logger.spinnerStop()

        } else {
            Logger.spinnerStop()
            SysErrorLog(data.status + ", " + data.status_message)
        }
    })
}