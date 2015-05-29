(function ($) {
    $.fn.highlight = function (searchString) {
        var highlightClassName = "search-highlighted";
        var regex = new RegExp(searchString, "gi");

        var elements = this.find('*');
        this.each(function () {
            elements.push(this);
            $(this).find('.' + highlightClassName).contents().unwrap();
            this.normalize();
        });

        return elements.each(function () {
            var $this = $(this);

            if (searchString && searchString !== '') {
                $this.contents()
                    .filter(function () {
                        return this.nodeType === 3 && regex.test(this.nodeValue);
                    })
                    .replaceWith(function () {
                        return (this.nodeValue || "").replace(regex, function (match) {
                            return "<span class=\"" + highlightClassName + "\">" + match + "</span>";
                        });
                    });
            }
        });
    };
}(jQuery));