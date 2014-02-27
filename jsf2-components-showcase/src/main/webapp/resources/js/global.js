function setActiveNavigation(id) {
    setNotActiveNavigation();
    jQuery('#' + id).addClass('active');
};

function setNotActiveNavigation() {
    jQuery('#text').removeAttr('class');
    jQuery('#textarea').removeAttr('class');
    jQuery('#secret').removeAttr('class');
    jQuery('#checkbox').removeAttr('class');
    jQuery('#combobox').removeAttr('class');
};