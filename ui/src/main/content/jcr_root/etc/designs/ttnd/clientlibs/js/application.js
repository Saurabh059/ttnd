var menuTimer;

var subscribeModal = '<div class="modal fade" id="subModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">\
    <div class="modal-dialog">\
    <div class="subscribe-popup">\
    <form action="#" method="post">\
    <div class="white-box">\
    <div class="heading">Yes, I want to subscribe to your blog!</div>\
    <label><input type="checkbox" value="Technology" name="subscription_type" class="sub-checkbox">Technology</label>\
        <label><input type="checkbox" value="Marketing" name="subscription_type" class="sub-checkbox">Marketing</label>\
            <ul>\
                <li><input type="text" placeholder="Email" id="subscribe-email"></li>\
                <li><p>Our <a href="/privacy">Privacy</a> is well-detailed.<br/>\
                    We are good people. We will not share your details!</p></li>\
                </ul>\
            </div>\
            <div class="submit-subs"><input type="submit" class="subscribe-btn" value="Subscribe"></div>\
            </form>\
        </div>\
    </div>';


function showMenu() {
    var menuClass = jQuery(this).prop('class');
    jQuery('nav >ul').find('.sub-nav').filter(function (i, value) {
        return jQuery(this).parent().prop('class') != menuClass;
    }).hide();
    jQuery(this).find('.sub-nav').slideDown();
    clearTimeout(menuTimer);
}
function hideMenu() {
    var menu = jQuery(this).find('.sub-nav');
    menuTimer = setTimeout(function () {
        menu.hide();
    }, 100);
}

var flag = true;
jQuery(document).ready(function () {
    makeHeaderTitleActive();
    jQuery(document).on('mouseenter', 'nav >ul >li', showMenu);
    jQuery(document).on('mouseleave', 'nav >ul >li', hideMenu);
    jQuery('.aboutheader, .industryHeader, .mediaheader').hover(function () {
        jQuery(this).find('.sub-list').stop().slideToggle();
    });
    jQuery('#bottom .servicesSection a').click(function () {
        flag = false;
    });
    jQuery("#bottom .servicesSection").click(function () {
        if (flag) {
            jQuery(this).toggleClass('active');
            jQuery(this).find('span :first').toggleClass('active');
            jQuery("#bottom ul").slideToggle(jQuery(this).hasClass('active') ? 0 : 'slow');

            jQuery("html, body").animate({
                scrollTop: jQuery(document).height()
            }, 1000);
        }
        flag = true;
    });

    jQuery("#email1").focus(function () {
        jQuery("#messageBox").text("");
    });

    jQuery('.submit').click(function(){
        emailId = document.getElementById('email');
        if(!emailId.value.match('^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,4}$')){
            alert('Please enter valid email id.');
            emailId.value="";
            return false;
        }
        jQuery.ajax({
            type: 'GET',
            url:'/bin/ttnd/emailServices',
            data:{emailId: emailId.value},
            success:function(){
                alert("Successfully Subscribed.");
                emailId.value="";
            },
            error: function() {
                alert("Unable to process the request");
            }
        });
        return false;
    });

    jQuery("#talkToUs").submit(function(){
        var emailId = document.getElementById('emailId').value;
        var fullname = document.getElementById('fullname').value;
        var message = document.getElementById('message').value;
        if(!emailId || !fullname || !message){
        	alert("Name, EmailId and Message can not be blank!!");
            return false;
        }
        if(!emailId.match('^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,4}$')){
            alert('Please enter valid email id.');
            return false;
        }
    });
    
    jQuery("#sub").on("click", function () {
        jQuery('#subModal').remove();
        jQuery('body').append(subscribeModal);
        jQuery("#subModal").modal('show');
    });

    jQuery(".subscribe-btn").on("click", function () {
        jQuery('#subModal').remove();
        jQuery('body').append(subscribeModal);
        jQuery("#subModal").modal('show');
    });

    jQuery(document).on('click', "#subscribe-submit", function () {
        var jQueryemail = jQuery("#subscribe-email").val();
        var data = {email: jQueryemail, pageTitle: window.location.href, subscription_type: jQuery('input[name=subscription_type]:checked').val()};
        if (!validateEmail(jQueryemail)) {
            console.log("invalid");
            var msg = '<p style="margin-left: 81px;margin-top:12px;"><b>Enter valid email id</b></p>';
            jQuery("#modalMessageBox").html(msg);
        }
        else {
            jQuery.ajax({
                type: "POST",
                url: "/blogSubscription.php",
                data: data,
                success: function (data) {
                    console.log("form saved \n " + data);
                    window.location.href = "/subscription-thanks-form.html";
                }
            });
            console.log(jQueryemail)
        }
    });
});

function makeHeaderTitleActive() {
    var headerType = jQuery('#pageHeaderType').val();
    if (headerType) {
        var headerElement = jQuery('header li.' + headerType);
        jQuery('header li.active').removeClass('active');
        if (headerElement) {
            headerElement.addClass('active');
        }
    }
}

function validateEmail(email) {
    var expr = /^([\w-]+(?:\.[\w-]+)*)@((?:[\w-]+\.)*\w[\w-]{0,66})\.([a-z]{2,6}(?:\.[a-z]{2})?)$/;
    return expr.test(email);
}

jQuery(document).ready(function () {
    jQuery("body a").each(function (index, value) {
        var aLink = jQuery(this).attr('href');
        if (aLink) {
            var hasHTTP = aLink.match(/^http:/);
            var hasHTTPS = aLink.match(/^https/);
            // no follow for all external links and blog links, excepts blog links on blog pages
            //var hasttn = aLink.match(/^http:\/\/tothenew\.com/) ||  aLink.match(/^http:\/\/www\.tothenew\.com/) || aLink.match(/^http:\/\/uat\.tothenew\.com/);
            var hasttn = aLink.match(/^http(s)?:\/\/(uat\.|www\.)?tothenew\.com/);
            var isBlogLink = aLink.match(/^http(s)?:\/\/(uat\.|www\.)tothenew\.com\/blog/);
            var isBlogPage = location.href.match(/^http(s)?:\/\/(uat\.|www\.)tothenew\.com\/blog/);
            var hasNoFollow = jQuery(this).attr('rel');
            if (hasHTTP || hasHTTPS) {
                if (!hasttn || (Boolean(isBlogLink) && !(isBlogPage))) {
                    if (!hasNoFollow) {
                        jQuery(this).attr('rel', 'nofollow');
                    }
                }
            }
        }
    });

    jQuery(document).on('change', '.sub-checkbox', function () {
        var isChecked = jQuery(this).is(':checked');
        jQuery('.sub-checkbox').removeAttr('checked');
        if (isChecked) {
            jQuery(this).attr('checked', 'checked');
        }
    });



    //event handler for search blog component
    jQuery('#searchform').on('submit',function(e){
        console.log('searchform handler from inside application.js file');
        var search = $(this).find('#s').val().trim();
        if(search =='' && search.length==0){
            e.preventDefault();
            alert("Please enter a query to search");
            console.log('inside else statement of validation');
        }
    });

    /*  search listing js script   */

    var myBgColors=['#363996','#ec2141','#f4b32a','#f47b2a','#00a9e4','#ae1884','#d5056f','#37beba','#ec2141'];
    var i=0;
    jQuery('div.blog-box').each(function(){
        jQuery(this).css('background-color',myBgColors[i]);
        i=(i+1)%myBgColors.length;
    });
    var myStripColors=['#b0b2f2','#f7d1d7','#f3ede0','#f8e4d6','#def0f7','#f1d7e5','#f4d6e5','#e7f9f8','#f7d1d7'];
    var mytextColors=['#363996','#ec2141','#f4b32a','#f47b2a','#00a9e4','#ae1884','#d5056f','#37beba','#ec2141'];
    var i1=0;
    var i2=0;
    jQuery('div.blog-box span').each(function(){
        jQuery(this).css('background-color',myStripColors[i1]);
        jQuery(this).css('color',mytextColors[i2]);
        jQuery(this).find('a').css('color',mytextColors[i2]);
        i1=(i1+1)%myStripColors.length;
        i2=(i2+1)%mytextColors.length;
    });
    var myCatColors=['#686bd1','#e9adb6','#eee5d1','#ecc3a8','#afd4e2','#d194b5','#e5b9cf','#d6f3f2','#eb818f'];
    var i3=0;
    $('div.blog-box p.bottom').each(function(){
        jQuery(this).css('color',myCatColors[i3]);
        jQuery(this).find('a').css('color',myCatColors[i3]);
        i3=(i3+1)%myCatColors.length;
    });
    jQuery('.heading a').each(function(){
        var that=jQuery(this),title=that.text(),chars=title.length;
        if(chars>33){
            var newTitle=title.substring(0,42)+"...";
            that.text(newTitle);
        }
    });
    jQuery('.sides a small').each(function(){
        var that=jQuery(this),title=that.text(),chars=title.length;
        if(chars>33){
            var newTitle=title.substring(0,33)+"...";
            that.text(newTitle);
        }
    });

    /* search listing js script ends */

    /* faq js script starts here */

    jQuery('.question .open-ans').on('click',function(e){
        e.preventDefault();
        var $question = $(this).parent();
        $question.find('.plus-icon').toggleClass('active');
        $question.find('.data').slideToggle();
    });

    /* faq js script ends here */

});
