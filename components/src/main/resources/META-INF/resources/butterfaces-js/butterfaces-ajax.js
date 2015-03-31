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
                $(document.getElementById(ids[i])).butterDisableElements();
            }

            break;

        case "complete": // After the ajax response is arrived.
            // console.log('ajax request complete');
            break;

        case "success": // After update of HTML DOM based on ajax response..
            // console.log('ajax request success');

            for(i=0;i<ids.length;i++){
                $(document.getElementById(ids[i])).butterEnableElements();
            }

            break;
    }
};