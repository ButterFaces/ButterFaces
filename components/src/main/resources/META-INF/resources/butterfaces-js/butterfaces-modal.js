if (typeof butter === 'undefined') {
    butter = {};
}
butter.modal = {};

butter.modal.open = function (/* string */ modalPanelId) {
    // console.log('Opening modal panel with data-modal-id ' + modalPanelId);
    $('.butter-modal[data-modal-id=' + modalPanelId + ']').modal({show: true, keyboard: false, backdrop: 'static'})
};

butter.modal.close = function (/* string */ modalPanelId) {
    // console.log('Closing modal panel with data-modal-id ' + modalPanelId);
    $('.butter-modal[data-modal-id=' + modalPanelId + ']').modal('hide');
};