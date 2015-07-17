/**
 * jQuery-Plugin to handle selection style classes on JSF-Component "b:table".
 * Works with at least jQuery 1.3.2.
 *
 * How to use:
 * jQuery("#someTreeSelector").selectRow( {rowIndex: '6'} );
 */
(function ($) {
    // extend jQuery --------------------------------------------------------------------
    $.fn.selectRow = function (data) {

        return this.each(function () {
            var $originalElement = $(this);

            // console.log('Selected row: ' + data.rowIndex);
            // console.log($originalElement.find('tr[rowindex=' + data.rowIndex + ']'));

            $originalElement.find('tr').removeClass('butter-table-row-selected');
            var listItems = $originalElement.find('tr[rowindex=' + data.rowIndex + ']');
            $(listItems[listItems.length - 1]).addClass('butter-table-row-selected');
        });
    };

    $.fn.toggleColumnVisibilty = function (renderIds, disableRenderIds, columnNumber) {

        return this.each(function () {
            var $originalElement = $(this);

            jsf.ajax.request($originalElement.attr('id'), 'toggle_' + columnNumber, {
                "javax.faces.behavior.event": 'toggle_' + columnNumber,
                render: renderIds.join(", "),
                params: createColumnVisibilty($originalElement),
                onevent: (function (data) {
                    //console.log(data);
                    if (disableRenderIds) {
                        butter.ajax.disableElementsOnRequest(data, renderIds);
                    }
                })
            });
        });

        function createColumnVisibilty($toolbar) {
            var columns = [];

            $toolbar.find('.butter-table-toolbar-column-option input[type=checkbox]').each(function (index, checkbox) {
                var $checkbox = $(checkbox).parent('.butter-table-toolbar-column-option');
                columns.push({
                    idenfifier: $checkbox.attr('data-column-model-identifier'),
                    visible: $checkbox.is(':checked')
                });
            });

            return columns;
        }
    };

    $.fn.orderColumnUp = function (data) {
        return this.each(function () {
            var $toolbar = $(this);

            //console.log($toolbar);

            var $column = $toolbar.find('li[data-original-column="' + data.column + '"]');
            var $nextColumn = $column.prev();

            //console.log($column);
            //console.log($nextColumn);

            var $detachtedColumn = $column.detach();
            $nextColumn.before($detachtedColumn);
        });
    };

    $.fn.orderColumnDown = function (data) {
        return this.each(function () {
            var $toolbar = $(this);

            //console.log($toolbar);

            var $column = $toolbar.find('li[data-original-column="' + data.column + '"]');
            var $nextColumn = $column.next();

            //console.log($column);
            //console.log($nextColumn);

            var $detachtedColumn = $column.detach();
            $nextColumn.after($detachtedColumn);
        });
    };
}(jQuery));