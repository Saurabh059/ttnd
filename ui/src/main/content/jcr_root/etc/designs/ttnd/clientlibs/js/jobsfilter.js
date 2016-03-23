window.isDefined = function (target) {
    return (target != null && target != 'null' && typeof(target) != 'undefined')
};

var jobsData=function(){
    var tempData;
    $.ajax({
        type:'GET',
        async:false,
        url:'/services/ttnd/jobDetail',
        dataType: 'text',
        success:function(data){
            tempData = data;
        }
    });
    return tempData;
}

var jobs = JSON.parse(jobsData());
var colorMap = ["blue-box", 'red-box', 'orange-box', 'brown-box', 'sky-blue-box', 'purple-box', 'pink-box', 'light-blue-box', 'dark-red-box'];

function getFunctions() {
    var functions = [];
    $(jobs.data).each(function () {
        if (functions.indexOf(this.function) == -1)
            functions.push(this.function);
    });
    return functions;
}
function getRegions() {
    var regions = [];
    $(jobs.data).each(function () {
        $(this.location).each(function (k, v) {
            if (regions.indexOf(v) == -1)
                regions.push(v);
        });
    });
    return regions;
}


function findByFunction(Function) {
    return $.grep(jobs.data, function (value, i) {
        return value.function.toLowerCase() == Function.toLowerCase()
    });
}
function findByRegion(region) {
    return $.grep(jobs.data, function (value, i) {
        var flag = false;
        $(value.location).each(function (k, v) {
            if (v.toLowerCase() == region.toLowerCase()) {
                flag = true;
            }
        });
        return flag;
    });
}

function findByFunctions(functions) {
    var data = [];
    $(functions).each(function () {
        data = findByFunction(this).concat(data)
    });
    return data;
}
function findByRegions(regions) {
    var data = [];
    $(regions).each(function () {
        data = findByRegion(this).concat(data)
    });
    return data;
}

function filterWrapper(selector) {
    $('body').addClass('loading');
    filter(selector);
    $('body').removeClass('loading');
}
function filter(selectorElement) {
    var functions = [];
    var regions = [];
    var functionCount = 0;
    var regionCount = 0;
    $('.functionFilter :checked').each(function () {
        functionCount++;
        functions.push($(this).next('label').text());
    });
    $('.regionFilter :checked').each(function () {
        regionCount++;
        regions.push($(this).next('label').text());
    });
    if (functionCount + regionCount == 0) {
        functions = getFunctions();
        regions = getRegions();
    }
    $('.jobs').empty();
    var jobs = $.unique(findJobs(functions, regions));
    addJobs(jobs);
    console.log(selectorElement);
    if (isDefined(selectorElement)) {
        if (selectorElement.is(":checked")) {
            addAlertFilterBox(selectorElement.next('label').text());
        }
        else {
            removeAlertFilterBox(selectorElement.next('label').text());
        }
    }
}

function findUnique(jobsData){
    var data = [];
    var ids = [];
    for(i = 0;i<jobsData.length; i++){
        if(ids.indexOf(jobsData[i].id) == -1) {
            data.push(jobsData[i]);
            ids.push(jobsData[i].id);
        }
    }
    return data;
}
function findJobs(functions, regions) {
    var functionJobs = findByFunctions(functions);
    var regionJobs = findByRegions(regions);
    return findUnique(functionJobs.concat(regionJobs));
    //return arrayIntersection(functionJobs, regionJobs);
}

function arrayIntersection(array1, array2) {
    var common = $.grep(array1, function (element) {
        return $.inArray(element, array2) !== -1;
    });
    return common;
}

function addJobs(jobsData) {
    var count = 0;
    var $jobRow;
    $('.jobs').empty();
    var number = jobsData.length;
    if (number == jobs.data.length) {
        number = "All"
    }
    $('.resultCount').text(number + " Result" + (number != 1 ? "s" : ""));
    $(jobsData).each(function () {
        var $jobs = $('.job-template>div').clone();
        $jobs.find(".job-title").text(this.job);
        $jobs.find(".job-experience").text(this.experience);
        $jobs.find(".job-location").text(this.location.join(", "));
        $jobs.find(".job-link").prop('href', this.url);
        var $jobsRow = $(".jobs .row :last");
        if (count % 3 == 0) {
            $('.jobs').append("<div class='row'></div>");
            $caseStudyRow = $('.jobs .row :last');
        }
        var color = colorMap[Math.floor((Math.random() * 10)) % 9];
        $jobs.find('.job-type').addClass(color);
        $caseStudyRow.append($jobs);
        count = count + 1;
    });
}

$(document).ready(function () {

    // populate functions in html from json data
    $(getFunctions()).each(function (k, v) {
        $('.functionFilter ul').append("\n<li><input id='" + v + "' type='checkbox'><label for='" + v + "'>"+this+"</label></li>\n");
    });

    // populate regions in html from json data
    $(getRegions()).each(function (k, v) {
        $('.regionFilter ul').append("\n<li><input id='" + v + "' type='checkbox'><label for='" + v + "'>"+this+"</label></li>\n");
    });
    filterWrapper();
    //filter();
});
function loading(callback) {
    $('body').addClass('loading');
    setTimeout(function () {
        $('body').removeClass('loading');
        callback();
    }, 1500);
}

$(document).on('change', '.job-filter :checkbox', function () {
    filterWrapper($(this));
});

$(document).on('click', '.clearAll', clearAll);

function clearAll() {
    $(this).parent().find(":checked").each(function () {
        $(this).click();
    });
}

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

// filter menu open and close events:
$(document).on('click', '.job-filter >ul :first li :not(:first)', function () {
    if ($(this).hasClass('active')) {
        closeMenu($(this));
    }
    else {
        openMenu($(this));
    }
});
function closeMenu($filterMenu) {
    $filterMenu.removeClass('active');
    $filterMenu.find('i').removeClass('filter-arrow-up');
    $filterMenu.find('i').addClass('filter-arrow-down');
    $('.' + $filterMenu.prop('class') + "Filter").slideUp();
//    location.hash = "";
}
function openMenu($filterMenu) {
    $filterMenu.siblings().each(function () {
        if ($filterMenu != $(this))
            closeMenu($(this));
    });
    $('.' + $filterMenu.prop('class') + "Filter").slideDown();
    $filterMenu.addClass('active');
    $filterMenu.find('i').removeClass('filter-arrow-down');
    $filterMenu.find('i').addClass('filter-arrow-up');
}
