if (typeof butter === 'undefined') {
    butter = {};
}
butter.fix = {};

butter.fix.updateMojarraScriptSourceId = function (/*String*/ innerInputComponentId, /* array of string */ eventNames) {
    //console.log('Fix mojarra script source id');
    //console.log(innerInputComponentId);
    //console.log(eventNames);

    if (eventNames !== undefined) {
        for (var i = 0; i < eventNames.length; i++) {
            var event = 'on' + eventNames[i];
            //console.log(event);

            var onEvent = $(document.getElementById(innerInputComponentId)).find('input[' + event + ']').attr(event)

            //console.log(onEvent);

            if (onEvent !== undefined && onEvent.indexOf('mojarra.ab') > -1) {
                var replacedFunction = onEvent.replace('mojarra.ab(this,', 'mojarra.ab(\'' + innerInputComponentId + '\',');

                if (onEvent.indexOf('butter.fix.chain') > -1) {
                    replacedFunction = onEvent.replace('mojarra.ab(this,', 'mojarra.ab(\\\'' + innerInputComponentId + '\\\',');
                }

                //console.log(replacedFunction);
                $(document.getElementById(innerInputComponentId)).find('input[' + event + ']').attr(event, replacedFunction);
            }
        }
    }
};

butter.fix.chain = function(source, event) {

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