/**
 * jQuery-Plugin to handle selection style classes on JSF-Component "b:table".
 * Works with at least jQuery 1.3.2.
 *
 * How to use:
 * jQuery("#someTreeSelector").selectRow( {rowIndex: '6'} );
 */
(function ($) {
    // extend jQuery --------------------------------------------------------------------
    $.fn.toggleColumnVisibilty = function (renderIds, disableRenderIds) {

        return this.each(function () {
            var $toolbar = $(this);

            var json = JSON.stringify(createColumnVisibilty($toolbar));
            ButterFaces.Ajax.sendRequest($toolbar.attr('id'), 'toggle', renderIds, json, disableRenderIds);
        });

        function createColumnVisibilty($toolbar) {
            var columns = [];

            $toolbar.find('.butter-table-toolbar-column-option input[type=checkbox]').each(function (index, checkbox) {
                var $checkbox = $(checkbox).parent('.butter-table-toolbar-column-option');
                columns.push({
                    identifier: $checkbox.attr('data-column-model-identifier'),
                    visible: $checkbox.find('input[type=checkbox]').is(':checked')
                });
            });

            return columns;
        }
    };

    $.fn.orderColumn = function (renderIds, disableRenderIds, toLeft, columnNumber) {

        return this.each(function () {
            var $toolbar = $(this);

            if (toLeft) {
                //console.log('order column ' + columnNumber + ' to left');
                orderColumnLeft($toolbar, columnNumber);
            } else {
                //console.log('order column ' + columnNumber + ' to right');
                orderColumnRight($toolbar, columnNumber);
            }

            var json = JSON.stringify(createColumnOrder($toolbar));
            ButterFaces.Ajax.sendRequest($toolbar.attr('id'), 'order', renderIds, json, disableRenderIds);
        });

        function createColumnOrder($toolbar) {
            var columns = [];

            $toolbar.find('.butter-table-toolbar-column-option input[type=checkbox]').each(function (index, checkbox) {
                var $checkbox = $(checkbox).parent('.butter-table-toolbar-column-option');
                columns.push({
                    identifier: $checkbox.attr('data-column-model-identifier'),
                    position: index
                });
            });

            return columns;
        }

        function orderColumnLeft(/* jquery toolbar */ $toolbar, columnNumber) {
            //console.log($toolbar);

            var $column = $toolbar.find('li[data-original-column="' + columnNumber + '"]');
            var $nextColumn = $column.prev();

            //console.log($column);
            //console.log($nextColumn);

            var $detachtedColumn = $column.detach();
            $nextColumn.before($detachtedColumn);
        }

        function orderColumnRight(/* jquery toolbar */ $toolbar, columnNumber) {
            //console.log($toolbar);

            var $column = $toolbar.find('li[data-original-column="' + columnNumber + '"]');
            var $nextColumn = $column.next();

            //console.log($column);
            //console.log($nextColumn);

            var $detachtedColumn = $column.detach();
            $nextColumn.after($detachtedColumn);
        }
    };
}(jQuery));