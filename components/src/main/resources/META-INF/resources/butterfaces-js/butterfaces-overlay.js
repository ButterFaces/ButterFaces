if (typeof butter === "undefined") {
    butter = {};
}
butter.overlay = {};

butter.overlay.show = function (/* object */ options) {
    if ($("body").find(".butter-component-overlay").length === 0) {
        var delay = 500;
        var blockpage = true;

        if (options !== undefined) {
            if (options.delay !== undefined) {
                delay = options.delay;
            }
            if (options.blockpage !== undefined) {
                blockpage = options.blockpage;
            }
        }

        //console.log("butter.overlay.show - appending not displayed overlay to body");
        var $overlay = $('<div class="butter-component-overlay"><div class="butter-component-spinner" /></div>');
        $("body").append($overlay);

        if (blockpage) {
            //console.log("butter.overlay.show - showing transparent overlay direcly if blockpage is true");
            $overlay.show();
        }

        window.setTimeout(function () {
            if (!blockpage) {
                //console.log("butter.overlay.show - blockpage is false, showing transparent overlay after delay");
                $overlay.show();
            }
            //console.log("butter.overlay.show - starting animation to make overlay intransparent");
            $overlay
                    .stop(true)
                    .animate({
                        opacity: 1
                    }, 300, function () {
                        //console.log("butter.overlay.show - animation ended to make overlay intransparent");
                    });
        }, delay);
    }
};

butter.overlay.hide = function () {
    //console.log("butter.overlay.hide - starting animation to make overlay transparent");
    $("body > .butter-component-overlay")
            .stop(true)
            .animate({
                opacity: 0
            }, 300, function () {
                //console.log("butter.overlay.hide - animation ended to make overlay transparent, removing overlay from DOM");
                $(this).remove();
            });
};