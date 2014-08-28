jQuery(document).ready(function () {
    <!-- add bootstrap radio class to component -->
    <!-- bootstrap radio buttons are using pageDirection as default -->
    <!-- maybe use radio-inline -->
    jQuery("table.butterfaces-input-component").find("td").addClass("radio");
    jQuery("table.butterfaces-input-component").removeClass("form-control");
});