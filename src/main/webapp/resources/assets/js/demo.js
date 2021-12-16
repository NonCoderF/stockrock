var exampfn = {
    hello: function (what, which) {
        this.echo('Hello, ' + what + which +
            '. Wellcome to this terminal.');
    },
    cat: function (width, height) {
        var img = $('<img src="https://placekitten.com/' +
            width + '/' + height + '">');
        this.echo(img);
    },
    catasync: function (width, height) {
        return get_image('https://placekitten.com/' + width +
            '/' + height);
    },
    title: function () {
        return fetch('https://terminal.jcubic.pl')
            .then(function (r) {
                return r.text()
            })
            .then(function (html) {
                return html.match(/<title>([^>]+)<\/title>/)[1]
            });
    },
    titledyn: function (...args) {
        const options = $.terminal.parse_options(args);
        return fetch(options.url || 'https://terminal.jcubic.pl')
            .then(r => r.text())
            .then(html => html.match(/<title>([^>]+)<\/title>/)[1]);
    },


    chart: function () {
        var iframe = $('<iframe style="background: white; width: 50%; height: 50vh" src="chart.jsp" title="W3Schools Free Online Web Tutorials"></iframe>');
        this.echo(iframe);

        //window.open("chart.jsp", "", "200", "200");
    },

    name: function (name) {
        this.read('last name: ').then(last_name => this.echo('Your name is ' + name + ' ' + last_name));
    },

    link: function () {
        this.echo('Executing commands').exec([
            'git clone https://github.com/jcubic/jquery.terminal.git',
            'cd jquery.terminal',
            'git checkout devel'
        ]);
    }

}

function get_image(url) {
    return new Promise(function (resolve, reject) {
        var img = $('<img src="' + url + '"/>');
        img.on('load', function () {
            resolve(img)
        });
        img.on('error', reject);
    });
}