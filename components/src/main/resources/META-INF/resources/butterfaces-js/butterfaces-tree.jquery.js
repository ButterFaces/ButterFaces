/**
 * jQuery-Plugin "Animate Tree" for tree animation. It is used for the JSF-Component "b:tree".
 * Works with at least jQuery 1.3.2.
 *
 * How to use:
 * jQuery("#someTreeSelector").butterTree({expansionClass: 'glyphicon-chevron-down', collapsingClass: 'glyphicon-chevron-up', treeIconsEnabled: 'true', treeSelectionEnabled: 'true'});
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

            // console.log("treeSelectionEnabled: " + data.treeSelectionEnabled);
            // console.log("treeIconsEnabled: " + data.treeIconsEnabled);
            // console.log("Collapsing class: "+ data.collapsingClass);
            // console.log("Expansion class: "+ data.expansionClass);

            if (data.treeIconsEnabled === 'false') {
                $originalElement.addClass("butter-component-tree-no-icons");
            }

            if (data.treeSelectionEnabled === 'false') {
                $originalElement.addClass("butter-component-tree-no-selection");
            }

            $originalElement.find('li:has(ul)').addClass('parent_li').find(' > span').attr('title', 'Collapse this branch');
            $originalElement.find('li.parent_li > .butter-component-tree-row span.butter-component-tree-jquery-marker').on('click', function (e) {
                var children = $(this).parent().parent('li.parent_li').find(' > ul > li');
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

/**
 * jQuery-Plugin to handle selection style classes on JSF-Component "b:tree".
 * Works with at least jQuery 1.3.2.
 *
 * How to use:
 * jQuery("#someTreeSelector").selectTreeNode( {nodeNumber: '6'} );
 */
(function ($) {
    // extend jQuery --------------------------------------------------------------------
    $.fn.selectTreeNode = function (data) {

        return this.each(function () {
            var $originalElement = $(this);

            // console.log('Selected node: ' + data.nodeNumber);
            // console.log($originalElement.find('li:has(span[treenode=' + data.nodeNumber + '])'));

            $originalElement.find('li').removeClass('butter-component-tree-node-selected');
            var listItems = $originalElement.find('li:has(span[treenode=' + data.nodeNumber + '])');
            $(listItems[listItems.length-1]).addClass('butter-component-tree-node-selected');
        });
    };
}(jQuery));