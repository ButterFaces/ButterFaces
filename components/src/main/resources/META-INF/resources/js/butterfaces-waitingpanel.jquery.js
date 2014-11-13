/**
 * jQuery-Plugin "Animate Tree" for tree animation. It is used for the JSF-Component "b:tree".
 * Works with at least jQuery 1.3.2.
 *
 * How to use:
 * jQuery("#someTreeSelector").waitingPanel();
 */
(function ($) {
    // extend jQuery --------------------------------------------------------------------
    var $waitingPanelDialog = null;
    var $waitingPanelDotSelector = null;
    var eventRegistered = false;
    var waitingPanelOpeningDelay = null;

    $.fn.waitingPanel = function (data) {

        function processAjaxUpdate() {
            var ajaxRequestRunning = false;
            var intervallTrigger = null;

            // console.log('Setting waiting panel delay to ' + waitingPanelOpeningDelay);

            function showWaitingPanel() {
                $waitingPanelDialog.removeClass('butter-component-waitingPanel-hide');
                intervallTrigger = setInterval(function () {
                    $waitingPanelDotSelector.append('.');

                    if ($waitingPanelDotSelector.html().length > 5) {
                        $waitingPanelDotSelector.html('');
                    }
                }, 200);
            }

            function processEvent(data) {
                if (data.status == 'begin') {
                    // console.log('Begin ajax event');
                    ajaxRequestRunning = true;
                    setTimeout(function () {
                        // console.log('Ajax request running: ' + ajaxRequestRunning);
                        if (ajaxRequestRunning) {
                            // console.log('Ajax request is running. Showing modal panel');
                            showWaitingPanel();
                        } else {
                            // console.log('Ajax request is not running. Not showing modal panel');
                        }

                    }, waitingPanelOpeningDelay);

                } else if (data.status == 'success') {
                    // console.log('End ajax event');
                    ajaxRequestRunning = false;
                    $waitingPanelDialog.addClass('butter-component-waitingPanel-hide');
                    window.clearInterval(intervallTrigger);
                    $waitingPanelDotSelector.html('');
                }
            }

            return processEvent;
        }

        return this.each(function () {
            var $originalElement = $(this);
            var _elementId = $originalElement.attr('id')
            var _msg = document.getElementById(_elementId);

            $waitingPanelDialog = $(_msg);
            $waitingPanelDotSelector = $waitingPanelDialog.find('.butter-component-waitingPanel-processing');
            waitingPanelOpeningDelay = data.waitingPanelDelay;

            // I found no way to remove event listener from jsf js.
            // I tried to register a callback once and change it on render waiting panel but after this
            // no waiting panel appears anymore.
            // Actually on each rendering of this component a new callback is put on event listener collection.
            if (!eventRegistered) {
                // console.log('Register: ' + _elementId);

                jsf.ajax.addOnEvent(processAjaxUpdate());
                eventRegistered = true;
            }
        });

    };
}(jQuery));