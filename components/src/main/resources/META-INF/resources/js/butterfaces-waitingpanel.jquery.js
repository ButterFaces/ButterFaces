/**
 * jQuery-Plugin "Animate Tree" for tree animation. It is used for the JSF-Component "b:tree".
 * Works with at least jQuery 1.3.2.
 *
 * How to use:
 * jQuery("#someTreeSelector").butterTree({expansionClass: 'glyphicon-chevron-down', collapsingClass: 'glyphicon-chevron-up'});
 */
(function ($) {
    // extend jQuery --------------------------------------------------------------------
    $.fn.waitingPanel = function () {

        function processAjaxUpdate(mggId) {
            function processEvent(data) {
                var msg = document.getElementById(mggId);
                if (data.status == 'begin') {
                    console.log('Begin ajax event');
                    msg.style.display = '';
                } else if (data.status == 'success') {
                    console.log('End ajax event');
                    msg.style.display = 'none';
                }
            }

            return processEvent;
        }

        return this.each(function () {
            var $originalElement = $(this);
            var _elementId = $originalElement.attr('id')

            console.log('Register: ' + _elementId);

            jsf.ajax.addOnEvent(processAjaxUpdate(_elementId));
        });

    };
}(jQuery));