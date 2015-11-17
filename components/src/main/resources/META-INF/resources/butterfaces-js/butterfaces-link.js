if (typeof butter === 'undefined') {
    butter = {};
}
butter.link = {};

butter.link.disableOnClick = function(data, showDots, linkText, linkProcessingText, linkGlyphicon, linkProcessingGlyphicon, hideGlyphicon, disableRenderRegionsIds) {
    var status = data.type === "error" ? "error" : data.status;

    // console.log(data.source.id);

    var $commandLink = $(document.getElementById(data.source.id));

    switch (status) {
        case "begin": // Before the ajax request is sent.
            // console.log('ajax request begin');
            $commandLink.addClass("disabled");

            var $glyphicon = $commandLink.find('.butter-component-glyphicon');

            if (hideGlyphicon) {
                $glyphicon.hide();
            }
            if (linkProcessingGlyphicon.length > 0) {
                $glyphicon.removeAttr('class');
                $glyphicon.addClass('butter-component-glyphicon');
                $glyphicon.addClass(linkProcessingGlyphicon);
                if (linkProcessingText.length > 0 && linkGlyphicon.length == 0) {
                    // glyphicon only appears on ajax request
                    $glyphicon.addClass('butter-component-glyphicon-width-margin');
                }
            }

            if (showDots) {
                $commandLink.find('.butter-component-glyphicon-processing').startDots();
                $commandLink.find('.butter-component-glyphicon-processing').css('display', 'inline-block');
                $commandLink.find('.butter-component-glyphicon-text').html(linkProcessingText);
            }
            if (disableRenderRegionsIds != 'undefined') {
                // console.log('Disable field');
                new ButterFaces.Overlay(0, false, disableRenderRegionsIds).show();
            }
            break;

        case "complete": // After the ajax response is arrived.
            // console.log('ajax request complete');
            break;

        case "success": // After update of HTML DOM based on ajax response..
        case "error": // After update of HTML DOM based on ajax response..
            // console.log('ajax request success');
            $commandLink.removeClass("disabled");
            if (showDots) {
                $commandLink.find('.butter-component-glyphicon-processing').stopDots();
                $commandLink.find('.butter-component-glyphicon-processing').css('display', 'none');
                $commandLink.find('.butter-component-glyphicon-text').html(linkText ? linkText : '');
            }

            var $glyphicon = $commandLink.find('.butter-component-glyphicon');
            $glyphicon.removeAttr('class');
            $glyphicon.addClass('butter-component-glyphicon');

            if (hideGlyphicon) {
                $glyphicon.show();
            }
            if (linkGlyphicon.length > 0) {
                $glyphicon.addClass(linkGlyphicon);
            }

            if (disableRenderRegionsIds != 'undefined') {
                // console.log('Enable field');
                new ButterFaces.Overlay(0, false, disableRenderRegionsIds).hide();
            }
            break;
    }
};