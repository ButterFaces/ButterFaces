/**
 * jQuery-Plugin "Animate Tree" for tree animation. It is used for the JSF-Component "b:tree".
 * Works with at least jQuery 1.3.2.
 *
 * How to use:
 * jQuery("#someTreeSelector").butterTree({expansionClass: 'glyphicon-chevron-down', collapsingClass: 'glyphicon-chevron-up'});
 */
(function ($) {
    // extend jQuery --------------------------------------------------------------------
    var EVENT_REGISTERED = false;
    var INTERVAL_TRIGGER = null;

    $.fn.waitingPanel = function (data) {

        function processAjaxUpdate(mggId) {
            var msg = document.getElementById(mggId);
            var $modalComponent = $(msg);
            var $modalComponentInterval = $modalComponent.find('.butter-component-waitingPanel-processing');
            var ajaxRequestRunning = false;
            var waitingPanelDelay = data.waitingPanelDelay;

            function showWaitingPanel() {
                $modalComponent.modal({show: true, keyboard: false, backdrop: 'static'});
                INTERVAL_TRIGGER = setInterval(function () {
                    $modalComponentInterval.append('.');

                    if ($modalComponentInterval.html().length > 5) {
                        $modalComponentInterval.html('');
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
                            showWaitingPanel();
                        }

                    }, waitingPanelDelay);

                } else if (data.status == 'success') {
                    console.log('End ajax event');
                    ajaxRequestRunning = false;
                    $modalComponent.modal('hide');
                    window.clearInterval(INTERVAL_TRIGGER);
                    $modalComponentInterval.html('');
                }
            }

            return processEvent;
        }

        return this.each(function () {
            var $originalElement = $(this);
            var _elementId = $originalElement.attr('id')

            if (!EVENT_REGISTERED) {
                console.log('Register: ' + _elementId);

                jsf.ajax.addOnEvent(processAjaxUpdate(_elementId));
                EVENT_REGISTERED = true;
            }
        });

    };
}(jQuery));