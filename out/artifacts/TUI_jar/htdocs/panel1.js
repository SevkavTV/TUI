$(function(){
    var message = window.onload;
    $('.panel1').tabSlideOut({
        tabHandle: '.handle1',
        pathToTabImage: '1.png',
        imageHeight: '200px',
        imageWidth: '200px',
        tabLocation: 'left',
        speed: 300,
        action: 'click',
        topPos: '190px',
        fixedPosition: true						//Позиционирование блока false - position: absolute, true - position: fixed
    });
});
(function($){
    $.fn.tabSlideOut = function(callerSettings) {
        var settings = $.extend({

        }, callerSettings||{});

        settings.tabHandle = $(settings.tabHandle);
        var obj = this;
        obj.a = 1;
        if (settings.pathToTabImage == '3.png') obj.a = 2;
        if (settings.fixedPosition === true) {
            settings.positioning = 'fixed';
        } else {
            settings.positioning = 'absolute';
        }

        //ie6 doesn't do well with the fixed option
        if (document.all && !window.opera && !window.XMLHttpRequest) {
            settings.positioning = 'absolute';
        }

        //set initial tabHandle css
        settings.tabHandle.css({
            'display': 'block',
            'width' : settings.imageWidth,
            'height': settings.imageHeight,
            'textIndent' : '-99999px',
            'background' : 'url('+ settings.pathToTabImage +') no-repeat',
            'outline' : 'none',
            'position' : 'absolute'
        });

        obj.css({
            'line-height' : '1',
            'position' : settings.positioning
        });


        var properties = {
            containerWidth: parseInt(obj.outerWidth(), 10) + 'px',
            containerHeight: parseInt(obj.outerHeight(), 10) + 'px',
            tabWidth: parseInt(settings.tabHandle.outerWidth(), 10) + 'px',
            tabHeight: parseInt(settings.tabHandle.outerHeight(), 10) + 'px'
        };

        //set calculated css
        if(settings.tabLocation === 'top' || settings.tabLocation === 'bottom') {
            obj.css({'left' : settings.leftPos});
            settings.tabHandle.css({'right' : 0});
        }

        if(settings.tabLocation === 'top') {
            obj.css({'top' : '-' + properties.containerHeight});
            settings.tabHandle.css({'bottom' : '-' + properties.tabHeight});
        }

        if(settings.tabLocation === 'bottom') {
            obj.css({'bottom' : '-' + properties.containerHeight, 'position' : 'fixed'});
            settings.tabHandle.css({'top' : '-' + properties.tabHeight});

        }

        if(settings.tabLocation === 'left' || settings.tabLocation === 'right') {
            obj.css({
                'height' : properties.containerHeight,
                'top' : settings.topPos
            });

            settings.tabHandle.css({'top' : 0});
        }

        if(settings.tabLocation === 'left') {
            obj.css({ 'left': '-' + properties.containerWidth});
            settings.tabHandle.css({'right' : '-' + properties.tabWidth});
        }

        if(settings.tabLocation === 'right') {
            obj.css({ 'right': '-' + properties.containerWidth});
            settings.tabHandle.css({'left' : '-' + properties.tabWidth});

            $('html').css('overflow-x', 'hidden');
        }

        //functions for animation events

        settings.tabHandle.click(function(event){
            event.preventDefault();
        });

        var slideIn = function() {

            if (settings.tabLocation === 'top') {
                obj.animate({top:'-' + properties.containerHeight}, settings.speed).removeClass('open');
            } else if (settings.tabLocation === 'left') {
                obj.animate({left: '-' + properties.containerWidth}, settings.speed).removeClass('open');
            } else if (settings.tabLocation === 'right') {
                obj.animate({right: '-' + properties.containerWidth}, settings.speed).removeClass('open');
            } else if (settings.tabLocation === 'bottom') {
                obj.animate({bottom: '-' + properties.containerHeight}, settings.speed).removeClass('open');
            }
            if (obj.a == 2) {
                document.getElementById("cool1").style.backgroundImage = 'url(3.png)';
            }
            else {
                document.getElementById("cool2").style.backgroundImage = 'url(1.png)';
            }
            //alert("open");
            //settings.tabHandle.animate({background: 'url('+ settings.pathToTabImage +') no-repeat'}, settings.speed);
            //alert(document.getElementById("cool1").style.backgroundImage);
            //alert(cool);
        };

        var slideOut = function() {

            if (settings.tabLocation == 'top') {
                obj.animate({top:'-3px'},  settings.speed).addClass('open');
            } else if (settings.tabLocation == 'left') {
                obj.animate({left:'-3px'},  settings.speed).addClass('open');
            } else if (settings.tabLocation == 'right') {
                obj.animate({right:'-3px'},  settings.speed).addClass('open');
            } else if (settings.tabLocation == 'bottom') {
                obj.animate({bottom:'-3px'},  settings.speed).addClass('open');
            }
            if (obj.a == 2) {
                document.getElementById("cool1").style.backgroundImage = 'url(2.png)';
            }
            else {
                document.getElementById("cool2").style.backgroundImage = 'url(4.png)';
            }
        };

        var clickScreenToClose = function() {
            obj.click(function(event){
                event.stopPropagation();
            });
            $(document).click(function(){
                slideIn();
            });
        };

        var clickAction = function(){
            settings.tabHandle.click(function(event){
                if (obj.hasClass('open')) {
                    slideIn();
                } else {
                    slideOut();
                }
            });

            clickScreenToClose();
        };

        var hoverAction = function(){
            obj.hover(
                function(){
                    slideOut();
                },

                function(){
                    slideIn();
                });

            settings.tabHandle.click(function(event){
                if (obj.hasClass('open')) {
                    slideIn();
                }
            });
            clickScreenToClose();

        };

        //choose which type of action to bind
        if (settings.action === 'click') {
            clickAction();
        }

        if (settings.action === 'hover') {
            hoverAction();
        }
    };
})(jQuery);
