$(document).ready(function () {
    var path = $("#resourcePagePath").val();
    $.ajax({
        type: 'GET',
        url: "/services/ttnd/aggregator",
        data: { path: path    },
        success: function (response) {
            console.log("shivani ;;;" + response.data.length);
            var source = $("#entry-template").html();
            var compiled = dust.compile(source, "intro");
            dust.loadSource(compiled);

            dust.render("intro", response, function (err, out) {
                $("#output").html(out);
            });
        },
        error: function (jqXHR, textStatus, errorThrown) {
            console.log("failure")
        }
    });
});

