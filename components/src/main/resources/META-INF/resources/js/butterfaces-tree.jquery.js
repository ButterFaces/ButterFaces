/**
 * jQuery-Plugin "Animate Tree" for tree animation. It is used for the JSF-Component "b:tree".
 * Works with at least jQuery 1.3.2.
 *
 * How to use:
 * jQuery("#someTreeSelector").butterTree({expansionClass: 'glyphicon-chevron-down', collapsingClass: 'glyphicon-chevron-up'});
 */
(function ($) {
    // extend jQuery --------------------------------------------------------------------
    $.fn.butterTree = function (data) {

        return this.each(function () {
            var $originalElement = $(this);

            //do nothing, if original is not visible
            if (!$originalElement.is(":visible")) {
                // console.log("element is not visible, do nothing");
                return;
            }

            $originalElement.find('li:has(ul)').addClass('parent_li').find(' > span').attr('title', 'Collapse this branch');
            $originalElement.find('li.parent_li > span.butter-component-tree-collapse').on('click', function (e) {
                var children = $(this).parent('li.parent_li').find(' > ul > li');
                if (children.is(":visible")) {
                    children.hide('fast');
                    $(this).attr('title', 'Expand this branch').addClass(data.expansionClass).removeClass(data.collapsingClass);
                } else {
                    children.show('fast');
                    $(this).attr('title', 'Collapse this branch').addClass(data.collapsingClass).removeClass(data.expansionClass);
                }
                e.stopPropagation();
            });
        });

    };
}(jQuery));