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

            // console.log("Collapsing class: "+ data.collapsingClass);
            // console.log("Expansion class: "+ data.expansionClass);

            $originalElement.find('li:has(ul)').addClass('parent_li').find(' > span').attr('title', 'Collapse this branch');
            $originalElement.find('li.parent_li > span.butter-component-tree-jquery-marker').on('click', function (e) {
                var children = $(this).parent('li.parent_li').find(' > ul > li');
                if (children.is(":visible")) {
                    children.hide('fast');
                    // console.log("Collapsing branch");
                    $(this).attr('title', 'Expand this branch').removeClass(data.collapsingClass).addClass(data.expansionClass);
                } else {
                    children.show('fast');
                    // console.log("Expanding branch");
                    $(this).attr('title', 'Collapse this branch').removeClass(data.expansionClass).addClass(data.collapsingClass);
                }
                e.stopPropagation();
            });
        });

    };
}(jQuery));