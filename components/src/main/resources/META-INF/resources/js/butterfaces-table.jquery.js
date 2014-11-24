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

            $originalElement.find('tr').removeClass('butter-component-table-row-selected');
            var listItems = $originalElement.find('tr[rowindex=' + data.rowIndex + ']');
            $(listItems[listItems.length-1]).addClass('butter-component-table-row-selected');
        });
    };
}(jQuery));