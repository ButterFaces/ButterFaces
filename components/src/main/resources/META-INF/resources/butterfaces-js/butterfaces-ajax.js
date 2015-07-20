if (typeof butter === 'undefined') {
    butter = {};
}
butter.ajax = {};

butter.ajax.disableElementsOnRequest =  function(/* object */ data, /* array of string */ ids) {
    var status = data.status;

    // console.log(data);
    // console.log(ids);

    switch (status) {
        case "begin": // Before the ajax request is sent.
            // console.log('ajax request begin');

            for(i=0;i<ids.length;i++){
                var $elementToDisable = $(document.getElementById(ids[i]));

                if ($elementToDisable.length !== 0) {
                    //console.log('disable ' + ids[i]);
                    $(document.getElementById(ids[i])).butterDisableElements();
                    //console.log('disablee ' + ids[i]);
                }
            }

            break;

        case "complete": // After the ajax response is arrived.
            // console.log('ajax request complete');
            break;

        case "success": // After update of HTML DOM based on ajax response..
            // console.log('ajax request success');

            for(i=0;i<ids.length;i++){
                var $elementToEmable = $(document.getElementById(ids[i]));

                if ($elementToEmable.length !== 0) {
                    //console.log('enable ' + ids[i]);
                    $(document.getElementById(ids[i])).butterEnableElements();
                    //console.log('enabled ' + ids[i]);
                }
            }

            break;
    }
};

butter.ajax.sendRequest = function(/* string */ clientId, /* string */ event, /* array */ renderIds, /*optional string */ params, /* boolean */ disableRenderIds) {
    jsf.ajax.request(clientId, event, {
        "javax.faces.behavior.event": event,
        render: renderIds.join(", "),
        params: params,
        onevent: (function (data) {
            //console.log(data);
            if (disableRenderIds) {
                butter.ajax.disableElementsOnRequest(data, renderIds);
            }
        })
    });
};

