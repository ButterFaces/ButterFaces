/**
 * jQuery-Plugin "Animate Tree" for tree animation. It is used for the JSF-Component "b:tree".
 * Works with at least jQuery 1.3.2.
 *
 * How to use:
 * jQuery("#someTreeSelector").waitingPanel();
 */
(function ($) {
    // extend jQuery --------------------------------------------------------------------
    var EVENT_REGISTERED = false;
    var INTERVAL_TRIGGER = null;
    var MODAL_DIALOG = null;
    var WAITING_PANEL_DELAY = null;
    var WAITING_PANEL_INTERVAL_ELEMENT = null;

    $.fn.waitingPanel = function (data) {

        function processAjaxUpdate() {
            var ajaxRequestRunning = false;

            console.log('Setting waiting panel delay to ' + WAITING_PANEL_DELAY);

            function showWaitingPanel() {
                MODAL_DIALOG.removeClass('butter-component-waitingPanel-hide');
                INTERVAL_TRIGGER = setInterval(function () {
                    WAITING_PANEL_INTERVAL_ELEMENT.append('.');

                    if (WAITING_PANEL_INTERVAL_ELEMENT.html().length > 5) {
                        WAITING_PANEL_INTERVAL_ELEMENT.html('');
                    }
                }, 200);
            }

            function processEvent(data) {
                if (data.status == 'begin') {
                    console.log('Begin ajax event');
                    ajaxRequestRunning = true;
                    setTimeout(function () {
                        console.log('Ajax request running: ' + ajaxRequestRunning);
                        if (ajaxRequestRunning) {
                            console.log('Ajax request is running. Showing modal panel');
                            showWaitingPanel();
                        } else {
                            console.log('Ajax request is not running. Not showing modal panel');
                        }

                    }, WAITING_PANEL_DELAY);

                } else if (data.status == 'success') {
                    console.log('End ajax event');
                    ajaxRequestRunning = false;
                    MODAL_DIALOG.addClass('butter-component-waitingPanel-hide');
                    window.clearInterval(INTERVAL_TRIGGER);
                    WAITING_PANEL_INTERVAL_ELEMENT.html('');
                }
            }

            return processEvent;
        }

        return this.each(function () {
            var $originalElement = $(this);
            var _elementId = $originalElement.attr('id')
            var _msg = document.getElementById(_elementId);

            MODAL_DIALOG = $(_msg);
            WAITING_PANEL_INTERVAL_ELEMENT = MODAL_DIALOG.find('.butter-component-waitingPanel-processing');
            WAITING_PANEL_DELAY = data.waitingPanelDelay;

            // I found no way to remove event listener from jsf js.
            // I tried to register a callback once and change it on render waiting panel but after this
            // no waiting panel appears anymore.
            // Actually on each rendering of this component a new callback is put on event listener collection.
            if (!EVENT_REGISTERED) {
                console.log('Register: ' + _elementId);

                jsf.ajax.addOnEvent(processAjaxUpdate());
                EVENT_REGISTERED = true;
            }
        });

    };
}(jQuery));