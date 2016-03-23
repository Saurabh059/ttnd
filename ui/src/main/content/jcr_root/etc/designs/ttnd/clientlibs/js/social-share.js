$(document).ready(function () {
    var shareType = ['twitter', 'facebook', 'google_plusone_share', 'linkedin'];
    var i = 0;
    $(".case-study-shelf .about .share li").each(function () {
        var url = location.href;
        var title = document.title;
        $(this).find('a').attr('href', 'https://api.addthis.com/oexchange/0.8/forward/' + shareType[i] + '/offer?url=' + url + '&title=' + title + '&pco=tbxnj-1.0');
        i++;
        $(this).find('a').popupWindow({height: 500, width: 800, top: 50, left: 50});
    });
});
