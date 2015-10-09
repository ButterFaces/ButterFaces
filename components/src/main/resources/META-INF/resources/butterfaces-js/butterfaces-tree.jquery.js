(function ($) {
    // extend jQuery --------------------------------------------------------------------
    $.fn.selectTreeNodeNew = function (/* array */ renderIds) {

        return this.each(function () {
            var $originalElement = $(this);

            var treeClientId = $originalElement.parent().attr('id');

            $originalElement.change(function () {
                console.log('Selected node: ' + $originalElement.val());
                console.log(renderIds);
                butter.ajax.sendRequest(treeClientId, 'click', [renderIds], $originalElement.val(), false);
            });
        });
    };
}(jQuery));