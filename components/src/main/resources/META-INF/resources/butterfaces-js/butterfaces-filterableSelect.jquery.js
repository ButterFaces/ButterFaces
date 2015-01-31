(function ($) {
    // extend jQuery --------------------------------------------------------------------
    $.fn.butterFilterableSelect = function () {

        var _formatDisplayValue = function(value){
            return value;
        };

        return this.each(function () {
            var $select = $(this).find("select");
            var $resultContainer = null;
            var $resultListContainer = null;
            var $options = $select.find("option");
            var resultSelected = false;
            var optionList = [];

            var _selectParent = $select.parent();
            if(!_selectParent.hasClass("input-group")){
                _selectParent.addClass("input-group");
            }

            $ghostInput = $("<input>")
                    .attr("id", "costCenterSelectionComponent_ghost")
                    .attr("type", "text")
                    .attr("autocomplete", "off")
                    .addClass("filterableSelect-input ")
                    .addClass($select.attr("class"))
                    .val(_formatDisplayValue($select.find("option:selected").text()))
                    .on("keyup", function (event) {
                        optionList = [];
                        var searchText = $(this).val();
                        $options.each(function () {
                            var $option = $(this);
                            if ($option.text().toLowerCase().indexOf(searchText.toLowerCase()) >= 0) {
                                optionList.push($option);
                            }
                        });
                        _renderResult();
                    })
                // handle return button
                    .on("keydown", function (event) {
                        if (event.keyCode === 13) {
                            event.preventDefault();
                            if (optionList.length > 0) {
                                $select
                                        .val(optionList[0].val())
                                        .change();
                                $ghostInput.val(_formatDisplayValue(optionList[0].text()));
                                resultSelected = true;
                                _removeResultList();
                            }
                        }
                    })
                    .on("focus", function () {
                        // automatically select whole text on focus
                        this.setSelectionRange(0, $(this).val().length)
                    })
                    .on("mouseup", function (event) {
                        // in safari the mouseup event unselects the text, so we have to prevent this
                        event.preventDefault();
                    })
                    .on("blur", function () {
                        _removeResultList();
                    })
                    .insertBefore($select);

            $select.hide();

            $("<span>")
                    .addClass("input-group-btn")
                    .append(
                    $("<button>")
                            .addClass("btn btn-default")
                            .html("&nbsp;")
                            .append(
                            $("<i>").addClass("fa fa-chevron-down")
                    )
            )
                    .on("click", function (event) {
                        event.preventDefault();
                        optionList = [];
                        $options.each(function () {
                            var $option = $(this);
                            optionList.push($option);
                        });
                        _renderResult();
                        $ghostInput.focus();
                    })
                    .insertAfter($select);

            var _renderResult = function () {
                // create container elements if necessary
                if ($resultContainer === null) {
                    var ghostOffset = $ghostInput.offset();
                    $resultContainer = $("<div>")
                            .addClass("filterableSelect-resultContainer")
                            .css({
                                position: "absolute",
                                left: ghostOffset.left,
                                top: ghostOffset.top + $ghostInput.outerHeight(),
                                minWidth: ghostOffset.top + $ghostInput.innerWidth(),
                                zIndex: 1000
                            });
                    $resultListContainer = $("<ul>")
                            .addClass("filterableSelect-resultListContainer")
                            .appendTo($resultContainer);
                    $("body").append($resultContainer);
                }

                $resultListContainer.empty();

                if (optionList.length > 0) {
                    for (var i = 0; i < optionList.length; i++) {
                        var resultItemHtml;
                        var resultItemLabel;
                        var resultItemText = optionList[i].text();
                        var separatorIndex = resultItemText.indexOf(" - ");
                        if(separatorIndex > 0){
                            resultItemLabel = resultItemText.substring(0, separatorIndex);
                            resultItemHtml = "<b>"+resultItemLabel+"</b>";
                            resultItemHtml += "<br/>"+resultItemText.substr(separatorIndex+3);
                        }else{
                            resultItemLabel = resultItemText;
                            resultItemHtml = "<b>"+resultItemText+"</b>";
                        }

                        $("<li>")
                                .html(resultItemHtml)
                                .attr("data-select-value", optionList[i].val())
                                .attr("data-select-label", resultItemLabel)
                                .addClass("filterableSelect-resultItem")
                                .on("click", function () {
                                    $select
                                            .val($(this).attr("data-select-value"))
                                            .change();
                                    $ghostInput.val(_formatDisplayValue($(this).attr("data-select-label")));
                                    resultSelected = true;
                                })
                                .appendTo($resultListContainer);
                    }
                } else {
                    $("<li>")
                            .text("Keine Einträge vorhanden!")
                            .addClass("filterableSelect-noResultItems")
                            .appendTo($resultListContainer);
                }
            };

            var _removeResultList = function () {
                if ($resultContainer !== null) {
                    window.setTimeout(function () {
                        if (!resultSelected) {
                            _tryToSetValueAfterBlur();
                        }
                        $resultContainer.remove();
                        $resultContainer = null;
                        $resultListContainer = null;
                        resultSelected = false;
                    }, 200);
                }
            };

            var _tryToSetValueAfterBlur = function () {
                var value = $ghostInput.val();
                var valueValid = false;
                if (value !== undefined && value !== "") {
                    $options.each(function () {
                        if (!valueValid && $(this).text() === value) {
                            valueValid = true;
                            $select.val($(this).val());
                        }
                    });
                }

                if (!valueValid) {
                    // set old value
                    $ghostInput.val(_formatDisplayValue($select.find("option:selected").text()));
                }
            };
        });
    };
}(jQuery));