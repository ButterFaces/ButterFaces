function disableOnClick(data) {
    var status = data.status;

    console.log(data.source.id);

    var $commandLink = $(document.getElementById(data.source.id));

    switch (status) {
        case "begin": // Before the ajax request is sent.
            console.log('ajax request begin');
            $commandLink.addClass("disabled");
            break;

        case "complete": // After the ajax response is arrived.
            console.log('ajax request complete');
            $commandLink.removeClass("disabled");
            break;

        case "success": // After update of HTML DOM based on ajax response..
            console.log('ajax request success');
            break;
    }
}