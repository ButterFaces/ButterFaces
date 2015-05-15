(function ($) {
    $.fn.highlight = function (searchString, highlightClassName) {
        var regex = new RegExp(searchString, "gi");
        return this.find('*').each(function () {
            var $this = $(this);

            $this.find('.' + highlightClassName).contents().unwrap();
            this.normalize();

            if (searchString && searchString !== '') {
                $this.contents().filter(function () {
                    return this.nodeType == 3 && regex.test(this.nodeValue);
                }).replaceWith(function () {
                    return (this.nodeValue || "").replace(regex, function (match) {
                        return "<span class=\"" + highlightClassName + "\">" + match + "</span>";
                    });
                });
            }
        });
    };
}(jQuery));