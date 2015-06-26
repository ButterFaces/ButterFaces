if (typeof butter === 'undefined') {
    butter = {};
}
butter.overlay = {};

butter.overlay.show = function (/* object */ options) {
    if ($('body').find('.butter-component-overlay').length === 0) {
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

        // console.log('Opening overlay with delay: ' + delay);
        // console.log(blockpage);

        var $overlay = $('<div class="butter-component-overlay"><div class="butter-component-spinner" /></div>');
        $('body').append($overlay);

        // show transparent overlay direcly if blockpage is true
        if (blockpage) {
            $overlay.show();
        }

        window.setTimeout(function () {
            // show overlay after delay if blockpage is false
            if (!blockpage) {
                $overlay.show();
            }
            $overlay
                    .stop(true)
                    .animate({
                        opacity: 1
                    }, 300);
        }, delay);
    }
};

butter.overlay.hide = function () {
    $('body > .butter-component-overlay')
            .stop(true)
            .animate({
                opacity: 0
            }, 300, function () {
                $(this).remove();
            });
};