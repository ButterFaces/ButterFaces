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

    $.fn.toggleColumnVisibilty = function (data) {

        return this.each(function () {
            var $originalElement = $(this);

            // console.log('Toggle column: ' + data.columnIndex);

            var $columnHeader = $originalElement.find(".butter-component-table-column-header[columnnumber='" + data.columnIndex + "']");
            var $columnBody = $originalElement.find(".butter-component-table-column[columnnumber='" + data.columnIndex + "']");
            var $colGroup = $originalElement.find(".butter-table-colgroup[columnnumber='" + data.columnIndex + "']");

            var checked = $columnHeader.css('display') != 'none';

            // console.log('Column checked: ' + checked);

            if (checked) {
                $columnHeader.css('display', 'none');
                $columnBody.css('display', 'none');
                $colGroup.css('display', 'none');
            } else {
                $columnHeader.css('display', '');
                $columnBody.css('display', '');
                $colGroup.css('display', '');
            }

            // console.log($originalElement.find('tr[rowindex=' + data.rowIndex + ']'));
        });
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