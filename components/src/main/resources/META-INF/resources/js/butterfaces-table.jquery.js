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

            console.log('Toggle column: ' + data.columnIndex);

            var $toolbarColumn = $originalElement.find('.butter-table-toolbar').find('.butter-table-toolbar-columns').find("input[columnnumber='" + data.columnIndex + "']");
            var $columnHeader = $originalElement.find(".butter-component-table-column-header[columnnumber='" + data.columnIndex + "']");
            var $columnBody = $originalElement.find(".butter-component-table-column[columnnumber='" + data.columnIndex + "']");

            var checked = $toolbarColumn.attr('checked');

            console.log('Column checked: ' + checked);

            if (checked) {
                $toolbarColumn.removeAttr('checked');
                $columnHeader.css('display', 'none');
                $columnBody.css('display', 'none');
            } else {
                $toolbarColumn.attr('checked', 'checked');
                $columnHeader.css('display', '');
                $columnBody.css('display', '');
            }

            // console.log($originalElement.find('tr[rowindex=' + data.rowIndex + ']'));
        });
    };
}(jQuery));