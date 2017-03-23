///<reference path="definitions/external/tsd.d.ts"/>
///<reference path="butterfaces-ajax.ts"/>

(function ($: any) {
    $.fn.selectTableRow = function (data: any) {

        return this.each(function () {
            let $originalElement = $(this);

            console.log("Selected row: " + data.rowIndex);
            console.log($originalElement.find("tr[rowindex=" + data.rowIndex + "]"));

            $originalElement.find("tr").removeClass("butter-table-row-selected");
            let listItems = $originalElement.find("tr[rowindex=" + data.rowIndex + "]");
            $(listItems[listItems.length - 1]).addClass("butter-table-row-selected");
        });
    };
}(jQuery));

(function ($: any) {
    $.fn.sortTableRow = function (renderIds :any, disableRenderIds :any, columnNumber :any) {
        return this.each(function () {
            let $table = $(this);

            // let rows = $table.find("tbody > tr").length;
            // let columns = $table.find("thead th").length;

            ButterFaces.Ajax.sendRequest($table.attr("id"), "sort_" + columnNumber, renderIds, columnNumber, disableRenderIds);
        });
    };
}(jQuery));