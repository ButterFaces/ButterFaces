(function ($) {
    // extend jQuery --------------------------------------------------------------------
    $.fn.markdownReadonly = function () {
        var root = $(this);

        var $readonlyMarkdown = root.find('.butter-component-value-readonly-wrapper');
        var markdownText = $readonlyMarkdown.html()
            .replace('&amp;', '&')
            .replace('&lt;', '<')
            .replace('&gt;', '>');
        var markdownTextToHtml = new showdown.Converter().makeHtml(markdownText);

        $readonlyMarkdown.empty();
        $readonlyMarkdown.append(markdownTextToHtml);
    };
}(jQuery));