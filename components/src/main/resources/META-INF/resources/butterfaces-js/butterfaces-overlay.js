if (typeof butter === 'undefined') {
    butter = {};
}
butter.overlay = {};

butter.overlay.show = function(/* object */ options) {
    if ($('body').find('.butter-component-overlay').length === 0) {
        var delay = 500;
        // var blockpage = true;

        if (options !== undefined) {
            if (options.delay !== undefined) {
                delay = options.delay;
            }
            //if (options.blockpage !== undefined) {
            //    blockpage = options.blockpage;
            //}
        }

        console.log('Opening overlay with delay: ' + delay);
        // console.log(blockpage);

        var $spinner = $('<div class="butter-component-spinner" />');
        var $overlay = $('<div class="butter-component-overlay" />');
        $('body').append($overlay);
        $overlay.show();

        setTimeout(function () {
            $overlay.css('background-color', 'rgba(0, 0, 0, 0.5)');
            $overlay.append($spinner);
        }, delay);
    }
};

butter.overlay.hide = function() {
    $('body>.butter-component-overlay').fadeOut(300, function() {
        $(this).remove();
    });
};