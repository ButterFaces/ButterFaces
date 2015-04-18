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

            //console.log('placement: ' + placement);
            //console.log('trigger: ' + trigger);

            if (data.popoverMaxWidth) {
                root.attr('data-tooltip-min-vertical-offset', data.minVerticalOffset);
            }
            if (data.popoverMaxWidth) {
                root.attr('data-tooltip-min-horizontal-offset', data.minHorizontalOffset);
            }

            jQuery('#hoverBtn').popover({
                trigger: trigger,
                placement: placement,
                title: data.title,
                html: 'true',
                content: data.content,
                viewport: 'html'
            });
        });
    };

    $.fn.butterTooltipDeprecated = function () {
        return this.each(function () {
            //console.log("initializing tooltip");
            var root = $(this);
            var valueElement = root.find(".butter-component-value");
            var tooltip = root.find(".butter-component-tooltip");

            if (tooltip.length > 0) {
                //console.log("found tooltip element");

                var _positionTooltip = function () {
                    var tooltipWidth = tooltip.outerWidth();
                    var winWidth = $(window).width();

                    if (root.offset().left + root.outerWidth() + tooltipWidth < winWidth) {
                        // try position right
                        tooltip
                            .removeClass("butter-component-tooltip-up")
                            .addClass("butter-component-tooltip-right")
                            .css({
                                position: "absolute",
                                top: 0,
                                left: root.outerWidth(),
                                zIndex: 1000
                            });
                    } else {
                        // try position at bottom
                        tooltip
                            .removeClass("butter-component-tooltip-right")
                            .addClass("butter-component-tooltip-up")
                            .css({
                                position: "absolute",
                                bottom: (tooltip.outerHeight() + 18) * -1,
                                right: 0,
                                zIndex: 1000
                            });
                    }

                };

                var _hideTooltip = function () {
                    //console.log("hiding tooltip");
                    tooltip.addClass("butter-component-tooltip-hidden");
                    tooltip.removeClass("butter-component-tooltip-shown");
                    tooltip.data("tooltip-visible", false);
                    tooltip.attr("style", null);
                    //$(window).off("resize");
                };

                var _showTooltip = function () {
                    //console.log("showing tooltip");
                    if (!tooltip.data("tooltip-visible")) {
                        tooltip.removeClass("butter-component-tooltip-hidden");
                        tooltip.addClass("butter-component-tooltip-shown");
                        _positionTooltip();
                        tooltip.data("tooltip-visible", true);
                        //$(window).on("resize", _positionTooltip);
                    }
                };

                valueElement
                    .mouseenter(function () {
                        _showTooltip();
                    })
                    .mouseleave(function () {
                        if (!tooltip.data("tooltip-permanent")) {
                            _hideTooltip();
                        }
                    })
                    .focus(function () {
                        tooltip.data("tooltip-permanent", true);
                        _showTooltip();
                    })
                    .blur(function () {
                        tooltip.data("tooltip-permanent", false);
                        _hideTooltip();
                    });
            }
        });
    };
}(jQuery));