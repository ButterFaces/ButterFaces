///<reference path="definitions/external/tsd.d.ts"/>

(function ($:any) {
    $.fn.selectTableRow = function (data:any) {

        return this.each(function () {
            var $originalElement = $(this);

            console.log('Selected row: ' + data.rowIndex);
            console.log($originalElement.find('tr[rowindex=' + data.rowIndex + ']'));

            $originalElement.find('tr').removeClass('butter-table-row-selected');
            var listItems = $originalElement.find('tr[rowindex=' + data.rowIndex + ']');
            $(listItems[listItems.length - 1]).addClass('butter-table-row-selected');
        });
    };
}(jQuery));