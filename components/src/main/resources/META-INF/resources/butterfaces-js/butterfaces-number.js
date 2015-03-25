if (typeof butter === 'undefined') {
    butter = {};
}
butter.number = {};

butter.number.handleSpinner = function(e, component, min, max) {
    if (!isNaN(component.value)) {
        if (e.keyCode == 38) {
            if (!max || max && component.value < max) {
                component.value = parseInt(component.value) + 1;
                e.preventDefault();
            }
            e.preventDefault();
        } else if (e.keyCode == 40) {
            if (!min || min && component.value > min) {
                component.value = parseInt(component.value) - 1;
                e.preventDefault();
            }
        }
    }
};