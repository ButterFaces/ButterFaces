/**
 * butterItemFilterField is a jQuery plugin that filters html element with the css class <code>filterable-item</code>.
 * It is applied to the search field.<br/>
 * If no filter text is entered, then all filterable-items are displayed. Else the search field value is matched against <b>all</b> text contained by a filterable-item.
 *
 * How to use:
 * jQuery("#someInputSelector").butterItemFilterField();
 *
 * Author: Yann Massard
 */
(function ($) {
    var delay = (function () {
        var timer = 0;
        return function (callback, ms) {
            clearTimeout(timer);
            timer = setTimeout(callback, ms);
        };
    })();

    // extend jQuery --------------------------------------------------------------------
    $.fn.butterItemFilterField = function (filterableItemContainerSelector) {
        return this.each(function () {
            var $this = $(this);
            $this.keyup(function () {
                delay(function () {
                    var filterValue = $this.val();

                    // find container again every time, because it could have been rerendered.
                    var $filterableItemContainer;
                    if (filterableItemContainerSelector) {
                        $filterableItemContainer = $(filterableItemContainerSelector);
                    } else {
                        var containerSelector = $this.attr('data-filterable-item-container');
                        $filterableItemContainer = $(containerSelector);
                    }

                    $filterableItemContainer.find('.filterable-item').each(function () {
                        var $filterableItem = $(this);
                        if ($filterableItem.is(':containsIgnoreCase(' + filterValue + ')')) {
                            $filterableItem.removeClass("hidden");
                            $filterableItem.highlight(filterValue);
                        } else {
                            $filterableItem.addClass("hidden");
                        }

                    });
                }, 300);
            });
        });
    };
}(jQuery));

(function ($) {
    $.expr[":"].containsIgnoreCase = $.expr.createPseudo(function (arg) {
        return function (elem) {
            return !arg || $(elem).text().toUpperCase().indexOf(arg.toUpperCase()) >= 0;
        };
    });
}(jQuery));