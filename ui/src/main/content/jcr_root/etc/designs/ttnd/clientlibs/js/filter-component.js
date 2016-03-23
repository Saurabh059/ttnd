var regionFilters = [];

$(document).ready(function () {
    $('.filter-drop-list ul li input[type="checkbox"]').change(function () {
        getValueUsingClass();
    });
    $(document).on('change', '.job-filter :checkbox', function () {
        getResultCount();
    });
    $('.regionfilter ul li').map(function () {
        regionFilters.push($.trim($('label', this).text().toLowerCase()));
    })
});

function getValueUsingClass() {
    var chkArray = [];
    $(".chk:checked").each(function () {
        chkArray.push($(this).val());
    });
    console.log("Check Array length :: " + chkArray.length);
    var service = []
    if (chkArray.length > 0) {
        $("input[type='hidden']").parent('.col-xs-12').css("display", "none");
        $.each(chkArray, function (index, value) {
            var temp = value.toLowerCase();
            if ($("input[name='" + temp + "']").length > 0) {
                console.log("length of " + temp);
                $("input[name='" + temp + "']").parent().css("display", "block");
            }
            if (regionFilters.indexOf(temp) != -1) {
                service.push(temp)
            }
        });
        if (service.length) {
            $('.function-type').parents('.col-xs-12').hide();
            $.each(service, function (index, value) {
                $('.function-type:contains(' + value + ')').parents('.col-xs-12').show();
            })
        }
    }
    if (chkArray.length <= 0) {
        $("input[type='hidden']").parent('.col-xs-12').css("display", "block");
    }
    var selected;
    selected = chkArray.join(',') + ",";
    if (selected.length > 1) {
        console.log("You have selected " + selected);

    } else {
        console.log("Please at least one of the checkbox");
    }
}

function getResultCount() {
    if ($("#output .col-xs-12:not([style='display: none;'])").size() != $("#output .col-xs-12").size()) {
        $('.resultCount').html($("#output .col-xs-12:not([style='display: none;'])").size() + " Results");
    }
    else {
        $('.resultCount').html("All Results");
    }
}
// filter menu open and close events:

$(document).on('click', '.job-filter > ul > li.function', function () {
    if ($(this).hasClass('active')) {
        console.log("open")
        closeMenu($(this));
    }
    else {
        console.log("close")
        openMenu($(this));
    }
});

$(document).on('click', '.job-filter > ul > li.region', function () {
    if ($(this).hasClass('active')) {
        console.log("open")
        closeMenu($(this));
    }
    else {
        console.log("close")
        openMenu($(this));
    }
});

function closeMenu($filterMenu) {
    $filterMenu.removeClass('active');
    $filterMenu.find('i').removeClass('filter-arrow-up');
    $filterMenu.find('i').addClass('filter-arrow-down');
    $('.' + $filterMenu.prop('class') + "Filter").slideUp();
}

function openMenu($filterMenu) {
    console.log($filterMenu.innerHTML);
    $filterMenu.siblings().each(function () {
        if ($filterMenu != $(this))
            closeMenu($(this));
    });
    $(".clearfix .Resource").show();

    console.log($("'" + $filterMenu + "'").innerHTML);
    $('.' + $filterMenu.prop('class') + "Filter").slideDown();
    $filterMenu.addClass('active');
    $filterMenu.find('i').removeClass('filter-arrow-down');
    $filterMenu.find('i').addClass('filter-arrow-up');
}


$(document).on('click', '.clearAll', clearAll);
$(document).on("click", ".alert :button", removeFromFilterBox);
function addAlertFilterBox(text) {
    var alertBox = ' <div class="alert alert-warning alert-dismissible" role="alert" style="margin-right:5px; padding: 5px; display: inline-block"> \
    <button type="button" class="close" data-dismiss="alert" aria-label="Close" onclick=""><span aria-hidden="true">&times;</span></button> \
    <span style="padding: 0 10px">' + text + '</span> \
    </div>';
    $('.selectedItems').append(alertBox);
}

function removeAlertFilterBox(text) {
    $('.selectedItems .alert').each(function () {
        if ($(this).find('span').text().substring(1) == text)
            $(this).remove();
    })
}
function removeFromFilterBox() {
    // remove filter by clicking close button on alert
    var text = $(this).next('span').text();
    $('.job-filter :checked').each(function () {
        if ($(this).next('label').text() == text) {
            $(this).click();
        }
    });
}
