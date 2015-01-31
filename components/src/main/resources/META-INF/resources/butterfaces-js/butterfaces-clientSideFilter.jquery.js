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
                        } else {
                            $filterableItem.addClass("hidden");
                        }

                        $filterableItem.find('.search-highlighted').contents().unwrap();
                        $filterableItem[0].normalize()
                        if (filterValue !== '') {
                            $filterableItem.add($filterableItem.find('*')).highlight(filterValue, "search-highlighted");
                        }
                    });
                }, 300);
            });
        });
    };
}(jQuery));


(function ($) {
    $.fn.highlight = function (str, className) {
        var regex = new RegExp(str, "gi");
        return this.each(function () {
            $(this).contents().filter(function() {
                return this.nodeType == 3 && regex.test(this.nodeValue);
            }).replaceWith(function() {
                return (this.nodeValue || "").replace(regex, function(match) {
                    return "<span class=\"" + className + "\">" + match + "</span>";
                });
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