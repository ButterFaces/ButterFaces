function disableOnClick(data, showDots, linkText, linkProcessingText, hideGlyphicon, disableRenderRegionsIds) {
    var status = data.status;

    // console.log(data.source.id);

    var $commandLink = $(document.getElementById(data.source.id));

    switch (status) {
        case "begin": // Before the ajax request is sent.
            // console.log('ajax request begin');
            $commandLink.addClass("disabled");
            if (showDots) {
                $commandLink.find('.butter-component-glyphicon-processing').startDots();
                $commandLink.find('.butter-component-glyphicon-processing').css('display', 'inline-block');
                $commandLink.find('.butter-component-glyphicon-text').html(linkProcessingText);
                if (hideGlyphicon) {
                    $commandLink.find('.butter-component-glyphicon ').hide();
                }
            }
            if (disableRenderRegionsIds != 'undefined') {
                // console.log('Disable field');
                $(disableRenderRegionsIds).butterDisableElements();
            }
            break;

        case "complete": // After the ajax response is arrived.
            // console.log('ajax request complete');
            break;

        case "success": // After update of HTML DOM based on ajax response..
            // console.log('ajax request success');
            $commandLink.removeClass("disabled");
            if (showDots) {
                $commandLink.find('.butter-component-glyphicon-processing').stopDots();
                $commandLink.find('.butter-component-glyphicon-processing').css('display', 'none');
                $commandLink.find('.butter-component-glyphicon-text').html(linkText);
            }
            if (hideGlyphicon) {
                $commandLink.find('.butter-component-glyphicon ').show();
            }
            if (disableRenderRegionsIds != 'undefined') {
                // console.log('Enable field');
                $(disableRenderRegionsIds).butterEnableElements();
            }
            break;
    }
}