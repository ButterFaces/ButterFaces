if (typeof butter === 'undefined') {
    butter = {};
}
butter.prettyprint = {};

/**
 * calls pretty print javascript framework and removes first and last empty children.
 */
butter.prettyprint.cleanup = function() {
    prettyPrint();

    jQuery('.butter-component-prettyprint').each(function () {
        var $firstPreChild = jQuery(this).find("pre span").first();
        var $lastPreChild = jQuery(this).find("pre span").last();


        if (!(typeof $firstPreChild.html() === "undefined")) {
            if (!$firstPreChild.html().trim()) {
                $firstPreChild.remove();
            }
        }
        if (!(typeof $lastPreChild.html() === "undefined")) {
            if (!$lastPreChild.html().trim()) {
                $lastPreChild.remove();
            }
        }
    });
};