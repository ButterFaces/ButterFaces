if (typeof butter === 'undefined') {
    butter = {};
}
butter.util = {};

butter.util.chain = function(source, event) {

    if (arguments.length < 3) {
        return true;
    }

    // RELEASE_PENDING rogerk - shouldn't this be getElementById instead of null
    var thisArg = (typeof source === 'object') ? source : null;

    // Call back any scripts that were passed in
    for (var i = 2; i < arguments.length; i++) {

        var f = new Function("event", arguments[i]);
        var returnValue = f.call(thisArg, event);

        if (returnValue === false) {
            return false;
        }
    }
    return true;

};

