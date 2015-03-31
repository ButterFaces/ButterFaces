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

            if (onEvent !== undefined) {
                var replacedFunction = onEvent.replace('mojarra.ab(this,', 'mojarra.ab(\'' + innerInputComponentId + '\',');
                //console.log(replacedFunction);
                $(document.getElementById(innerInputComponentId)).find('input[' + event + ']').attr(event, replacedFunction);
            }
        }
    }
};