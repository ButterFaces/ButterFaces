(function ($) {

    $.fn._butterTooltip = function (/* object */ data) {
        var root = $(this);

        //console.log(data);

        var content = jQuery('[name=' + data.contentByName + ']');

        var newData = {
            trigger: data.trigger,
            title: data.title,
            placement: data.placement,
            placementFunction: data.placementFunction,
            minVerticalOffset: data.minVerticalOffset,
            minHorizontalOffset : data.minHorizontalOffset,
            content: content.html().trim()
        };

        //console.log(newData);

        content.remove();

        root.butterTooltip(newData);
    };

    $.fn.butterTooltip = function (/* object */ data) {
        return this.each(function () {
            var root = $(this);
            var placement = data.placement ? data.placement : (data.placementFunction ? data.placementFunction : butter.tooltip.calculateTooltipPosition);
            var trigger = data.trigger ? data.trigger : 'hover';

            //console.log(data);
            //console.log('placement: ' + placement);
            //console.log('trigger: ' + trigger);

            if (data.minVerticalOffset) {
                root.attr('data-tooltip-min-vertical-offset', data.minVerticalOffset);
            }
            if (data.minHorizontalOffset) {
                root.attr('data-tooltip-min-horizontal-offset', data.minHorizontalOffset);
            }
            if (root.next().hasClass('popover')) {
                root.next().remove();
            }

            root.popover({
                trigger: trigger,
                placement: placement,
                title: data.title,
                html: 'true',
                content: data.content,
                viewport: 'html'
            });
        });
    };
}(jQuery));