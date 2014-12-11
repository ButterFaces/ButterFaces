/**
 * jQuery-Plugin "Expanded TextAreas" for expandable text areas. It is used for the JSF-Component "b:textarea".
 * Works with at least jQuery 1.3.2.
 *
 * How to use:
 * jQuery("#someTextAreaSelector").butterExpandable();
 */
(function ($) {
    // extend jQuery --------------------------------------------------------------------
    $.fn.butterExpandable = function () {
        var EXPAND_HEIGHT = 250; //in px
        var EXPAND_WIDTH = 500; //in px
        var ANIMATION_DURATION = 200; //in ms
        var EASING = "swing";

        return this.each(function () {
            var $originalElement = $(this).find("textarea");
            $originalElement.addClass("butter-component-expandable-original");

            //do nothing, if original is not visible
            if (!$originalElement.is(":visible")) {
                // console.log("element is not visible, do nothing");
                return;
            }

            // console.log("element is visible, initialize it");

            var $ghostElement = null;
            var blockBlurEventOnOriginal = true;
            var blockFocusEventOnOriginal = false;
            var initialHeight;
            var initialWidth;
            var initialOffset;
            var positionTriggerInterval;

            var expandElement = function (event) {
                if (blockFocusEventOnOriginal) {
                    event.preventDefault();
                    return;
                }

                // console.log("expanding element");

                blockBlurEventOnOriginal = true;

                initialHeight = $originalElement.outerHeight();
                initialWidth = $originalElement.outerWidth();
                initialOffset = $originalElement.offset();

                //create a ghost element that be animated on gets the focus
                $ghostElement = $("<textarea>")
                    .val($originalElement.val()) //transfer value from original to ghost
                    .css("width", initialWidth)
                    .css("height", initialHeight)
                    .css("position", "absolute")
                    .css("top", initialOffset.top)
                    .css("left", initialOffset.left)
                    .css("z-index", 2000)
                    .css("box-shadow", "5px 5px 5px 0 #999")
                    .addClass("butter-component-expandable-ghost")
                    .blur(collapseElement)
                    .appendTo($("body"))
                    .focus() //set cursor into the ghost element
                    .animate({
                        height: EXPAND_HEIGHT,
                        width: EXPAND_WIDTH
                    }, ANIMATION_DURATION, EASING);

                //make original invisible
                $originalElement.css("opacity", 0);

                //keep track of the orginal element's position
                positionTriggerInterval = window.setInterval(repositionGhostElement, 30);
            };

            var collapseElement = function () {
                // console.log("collapsing element");

                //make original visible again
                $originalElement.css("opacity", 1);

                $ghostElement.animate({
                    height: initialHeight,
                    width: initialWidth
                }, ANIMATION_DURATION, EASING, function () {
                    //on animation complete

                    //transfer value back from ghost to original
                    $originalElement.val($ghostElement.val())

                    //delete the ghost element
                    $ghostElement.remove();
                    $ghostElement = null;

                    //delete position trigger timeout
                    window.clearInterval(positionTriggerInterval);

                    // trigger blur and keyup event on original textarea and don't block
                    // it for jsf
                    blockBlurEventOnOriginal = false;
                    blockFocusEventOnOriginal = true;
                    // defer the events a little bit, look at
                    // (http://stackoverflow.com/questions/8380759/why-isnt-this-textarea-focusing-with-focus#8380785)
                    window.setTimeout(function () {
                        $originalElement.focus();
                        blockFocusEventOnOriginal = false;
                        $originalElement.keyup();
                        $originalElement.blur();
                    }, 50);
                });
            };

            var handleBlurEvent = function (event) {
                if (blockBlurEventOnOriginal) {
                    // prevent blur event bubbling, so it will not be triggered in jsf
                    event.preventDefault();
                }
            };

            var repositionGhostElement = function () {
                //keep track of window resizing and reposition the ghost element
                if ($ghostElement !== undefined && $ghostElement != null) {
                    initialOffset = $originalElement.offset();
                    $ghostElement
                        .css("top", initialOffset.top)
                        .css("left", initialOffset.left);
                }
            };

            $originalElement.focus(expandElement);
            $originalElement.blur(handleBlurEvent);
            $(window).resize(repositionGhostElement);
        });
    };
}(jQuery));