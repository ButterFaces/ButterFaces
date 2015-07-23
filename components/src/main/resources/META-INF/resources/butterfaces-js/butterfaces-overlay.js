if (typeof butter === "undefined") {
    butter = {};
}
butter.overlay = {};

butter.overlay._isHiding = false;

butter.overlay.show = function (/* object */ options) {
    if ($("body").find(".butter-component-overlay").length === 0) {
        var delay = 500;
        var isTransparentBlockingOverlayActive = true;
        butter.overlay._isHiding = false;

        if (options !== undefined) {
            if (options.delay !== undefined) {
                delay = options.delay;
            }
            if (options.blockpage !== undefined) {
                isTransparentBlockingOverlayActive = options.blockpage;
            }
        }

        //console.log("butter.overlay.show - appending not displayed overlay to body");
        var $overlay = $('<div class="butter-component-overlay"><div class="butter-component-spinner" /></div>');
        $("body").append($overlay);

        if (isTransparentBlockingOverlayActive) {
            //console.log("butter.overlay.show - isTransparentBlockingOverlayActive is true, showing transparent overlay direcly");
            $overlay.show();
        }

        window.setTimeout(function () {
            if (!isTransparentBlockingOverlayActive) {
                //console.log("butter.overlay.show - deferred: isTransparentBlockingOverlayActive is false, showing transparent overlay after delay");
                $overlay.show();
            }

            if (!butter.overlay._isHiding) {
                //console.log("butter.overlay.show - deferred: starting animation to make overlay intransparent");
                $overlay
                        .stop(true)
                        .animate({
                            opacity: 1
                        }, 300, function () {
                            //console.log("butter.overlay.show - deferred: animation ended to make overlay intransparent");
                        });
            }
        }, delay);

    }
};

butter.overlay.hide = function () {
    //console.log("butter.overlay.hide - starting animation to make overlay transparent");
    var $overlay = $("body > .butter-component-overlay");
    butter.overlay._isHiding = true;
    $overlay
            .stop(true)
            .animate({
                opacity: 0
            }, 300, function () {
                $(this).remove();
                //console.log("butter.overlay.hide - animation ended to make overlay transparent, OVERLAY REMOVED");
            });
};