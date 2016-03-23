var $flag = false;

$(document).ready(function () {
    var scrollPaneHandler;
    $.fn.fadeSlideRight = function (speed, fn) {
        return $(this).show().animate({
            'opacity': 1,
            'min-width': '300px',
            'max-width': '500px'
        }, speed || 400, function () {
            $.isFunction(fn) && fn.call(this);
        });
    };

    $.fn.fadeSlideLeft = function (speed, fn) {
        return $(this).animate({
            'opacity': 0,
            'min-width': '0px',
            'max-width': '0px'
        }, speed || 400, function () {
            $.isFunction(fn) && fn.call(this);
        });
    };


    $('.menu_icon').click(function () {
        if ($('.menu_icon').hasClass('menu-open')) {
            $('.menu_icon').removeClass('menu-open');
            $('#m-nav').fadeSlideLeft();
            $('.opened-sub-menu').slideToggle('slow');
            $('.opened-sub-menu').removeClass('opened-sub-menu');
            $('.opened-sub-sub-menu').slideToggle('slow');
            $('.opened-sub-sub-menu').removeClass('opened-sub-sub-menu');
            $('body').css({'position': 'initial'});

        }
        else {
            $('#m-nav').fadeSlideRight();
            $('#m-nav').addClass('scroll-pane');
            $('#m-nav').addClass('menu-open');
            $('.menu_icon').addClass('menu-open');
            $('#m-nav').css({'overflow-y': 'scroll', 'height': '60%', 'overflow-x': 'hidden'});
            $('body').css({'position': 'fixed'});
        }

        $('.menu_icon').click(function (event) {
            event.stopPropagation();
        });

        $("#m-nav").click(function (event) {
            event.stopPropagation()
        })

        $("body").on('click', function () {
            if (($(".menu_icon").hasClass("menu-open")) && $flag == true) {
                $('.menu_icon').removeClass('menu-open');
                $('#m-nav').fadeSlideLeft();
                $('.opened-sub-menu').slideToggle('slow');
                $('.opened-sub-menu').removeClass('opened-sub-menu');
                $('.opened-sub-sub-menu').slideToggle('slow');
                $('.opened-sub-sub-menu').removeClass('opened-sub-sub-menu');
                $('body').css({'position': 'initial'});
            }
            $flag = true;

        });
    });
    /*$('#m-nav .crs-btn').click(function () {
     //        var api = scrollPaneHandler.data('jsp');
     //        api.destroy();
     $('#m-nav').fadeSlideLeft();
     $('#m-nav .crs-btn').css({'left': '0px'});
     $('.opened-sub-menu').slideToggle('slow');
     $('.opened-sub-menu').removeClass('opened-sub-menu');
     $('.opened-sub-sub-menu').slideToggle('slow');
     $('.opened-sub-sub-menu').removeClass('opened-sub-sub-menu');
     });*/
    $("#m-nav ul li a.firstLevelHeader").on('click', function () {
        $(this).next().slideToggle('slow');
        $('.active').removeClass('active');
        if (!($(this).next().hasClass('opened-sub-menu'))) {
            $('.opened-sub-menu').slideToggle('slow');
            $('.opened-sub-menu').removeClass('opened-sub-menu');
            $(this).next().addClass('opened-sub-menu');
        }
        else {
            $('.opened-sub-menu').removeClass('opened-sub-menu');
        }
    });

    $("#m-nav ul li span").on('click', function () {
        console.log('called s');
        $(this).next().slideToggle('slow');
        if (!($(this).next().hasClass('opened-sub-sub-menu'))) {
            $('.opened-sub-sub-menu').slideToggle('slow');
            $('.opened-sub-sub-menu').removeClass('opened-sub-sub-menu');
            $(this).next().addClass('opened-sub-sub-menu');
            $('.active').removeClass('active');
            $(this).addClass('active');
        }
        else {
            $('.opened-sub-sub-menu').removeClass('opened-sub-sub-menu');
            $(this).removeClass('active');
        }
    });

//$('#m-nav .crs-btn').css({'left': '0px'});

    /*$(window).scroll(function () {
     console.log('called');
     if ($('#m-nav').hasClass('menu-open')) {
     //$('#m-nav').removeClass('menu-open');
     console.log("Vikram hello");
     //$('#m-nav .crs-btn').css({'left': '-35px','position':'relative'});
     $('#m-nav').css({'overflow':'auto','height':'75%'});
     }

     });*/

    /* function touchScroll(id){
     if(isTouchDevice()){ //if touch events exist...
     var el=document.getElementById(id);
     var scrollStartPos=0;

     document.getElementById(id).addEventListener("touchstart", function(event) {
     scrollStartPos=this.scrollTop+event.touches[0].pageY;
     event.preventDefault();
     },false);

     document.getElementById(id).addEventListener("touchmove", function(event) {
     this.scrollTop=scrollStartPos-event.touches[0].pageY;
     event.preventDefault();
     },false);
     }
     }*/

    /*if ($('#m-nav').hasClass('menu-open')) {
     //$('#m-nav').removeClass('menu-open');
     console.log("Vikram hello");
     $('#m-nav .crs-btn').css({'left': '-35px','position':'fixed'});
     $('#m-nav').css({'overflow':'auto'});
     }else{
     console.log("byee")
     }*/

})
;
