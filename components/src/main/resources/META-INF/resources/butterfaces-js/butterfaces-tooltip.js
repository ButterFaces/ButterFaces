if (typeof butter === 'undefined') {
    butter = {};
}
butter.tooltip = {};

butter.tooltip.calculateTooltipPosition = function(popover, source) {
    var popoverMaxWidthByBootstrap = 276;
    var popoverMaxHeightByButterFaces = 110;

    var offsetBottom = $(window).height() - $(source).offset().top - $(source).outerHeight();
    var offsetRight = $(window).width() - $(source).offset().left - $(source).outerWidth();

    //console.log($(source).offset().top);
    //console.log(offsetRight);
    //console.log(offsetBottom);
    //console.log($(source).offset().left);

    if (offsetBottom < popoverMaxHeightByButterFaces) {
        if (offsetRight < popoverMaxWidthByBootstrap) {
            if ($(source).offset().left < popoverMaxWidthByBootstrap) {
                return 'top';
            }
            return 'left';
        }
        return 'right';
    }

    return 'bottom';
};