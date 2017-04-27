/*!
*
*  Copyright 2016 Yann Massard (https://github.com/yamass) and other contributors
*
*  Licensed under the Apache License, Version 2.0 (the "License");
*  you may not use this file except in compliance with the License.
*  You may obtain a copy of the License at
*
*  http://www.apache.org/licenses/LICENSE-2.0
*
*  Unless required by applicable law or agreed to in writing, software
*  distributed under the License is distributed on an "AS IS" BASIS,
*  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*  See the License for the specific language governing permissions and
*  limitations under the License.
*
*/
var TrivialComponents;
(function (TrivialComponents) {
    TrivialComponents.DEFAULT_TEMPLATES = {
        image2LinesTemplate: '<div class="tr-template-image-2-lines">' +
            '  <div class="img-wrapper" style="background-image: url({{imageUrl}})"></div>' +
            '  <div class="content-wrapper tr-editor-area"> ' +
            '    <div class="main-line">{{displayValue}}</div> ' +
            '    <div class="additional-info">{{additionalInfo}}</div>' +
            '  </div>' +
            '</div>',
        roundImage2LinesColorBubbleTemplate: '<div class="tr-template-round-image-2-lines-color-bubble">' +
            '  {{#imageUrl}}<div class="img-wrapper" style="background-image: url({{imageUrl}})"></div>{{/imageUrl}}' +
            '  <div class="content-wrapper tr-editor-area"> ' +
            '    <div class="main-line">{{displayValue}}</div> ' +
            '    <div class="additional-info">{{#statusColor}}<span class="status-bubble" style="background-color: {{statusColor}}"></span>{{/statusColor}}{{additionalInfo}}</div>' +
            '  </div>' +
            '</div>',
        icon2LinesTemplate: '<div class="tr-template-icon-2-lines">' +
            '  <div class="img-wrapper" style="background-image: url({{imageUrl}})"></div>' +
            '  <div class="content-wrapper tr-editor-area"> ' +
            '    <div class="main-line">{{displayValue}}</div> ' +
            '    <div class="additional-info">{{additionalInfo}}</div>' +
            '  </div>' +
            '</div>',
        iconSingleLineTemplate: '<div class="tr-template-icon-single-line">' +
            '  <div class="img-wrapper" style="background-image: url({{imageUrl}})"></div>' +
            '  <div class="content-wrapper tr-editor-area">{{displayValue}}</div>' +
            '</div>',
        singleLineTemplate: '<div class="tr-template-single-line">' +
            '  <div class="content-wrapper tr-editor-area"> ' +
            '    <div>{{displayValue}}</div> ' +
            '  </div>' +
            '</div>',
        currencySingleLineShortTemplate: '<div class="tr-template-currency-single-line-short">' +
            '  <div class="content-wrapper tr-editor-area"> ' +
            '    <div>{{#symbol}}<span class="currency-symbol">{{symbol}}</span>{{/symbol}} {{#code}}<span class="currency-code">{{code}}</span>{{/code}}</div> ' +
            '  </div>' +
            '</div>',
        currencySingleLineLongTemplate: '<div class="tr-template-currency-single-line-long">' +
            '  <div class="content-wrapper tr-editor-area"> ' +
            '    <div class="symbol-and-code">{{#code}}<span class="currency-code">{{code}}</span>{{/code}} {{#symbol}}<span class="currency-symbol">{{symbol}}</span>{{/symbol}}</div>' +
            '    <div class="currency-name">{{name}}</div>' +
            '  </div>' +
            '</div>',
        currency2LineTemplate: '<div class="tr-template-currency-2-lines">' +
            '  <div class="content-wrapper tr-editor-area"> ' +
            '    <div class="main-line">' +
            '      <span class="currency-code">{{code}}</span>' +
            '      <span class="currency-name">{{name}}</span>' +
            '    </div> ' +
            '    <div class="additional-info">' +
            '      <span class="currency-symbol">{{symbol}}</span>&nbsp;' +
            '      {{#exchangeRate}}' +
            '      <div class="exchange">' +
            '        = ' +
            '        <span class="exchange-rate">{{exchangeRate}}</span>' +
            '        <span class="exchange-rate-base">{{exchangeRateBase}}</span>' +
            '      </div>' +
            '      {{/exchangeRate}}' +
            '    </div>' +
            '  </div>' +
            '</div>',
        defaultSpinnerTemplate: '<div class="tr-default-spinner"><div class="spinner"></div><div>Fetching data...</div></div>',
        defaultNoEntriesTemplate: '<div class="tr-default-no-data-display"><div>No matching entries...</div></div>'
    };
    function wrapWithDefaultTagWrapper(entryTemplate) {
        return ('<div class="tr-tagbox-default-wrapper-template">' +
            '<div class="tr-tagbox-tag-content">##entryHtml##</div>' +
            '<div class="tr-remove-button"></div>' +
            '</div>').replace("##entryHtml##", entryTemplate);
    }
    TrivialComponents.wrapWithDefaultTagWrapper = wrapWithDefaultTagWrapper;
    function defaultListQueryFunctionFactory(entries, matchingOptions) {
        function filterElements(queryString) {
            var visibleEntries = [];
            for (var i = 0; i < entries.length; i++) {
                var entry = entries[i];
                var $entryElement = entry._trEntryElement;
                if (!queryString || trivialMatch($entryElement.text().trim().replace(/\s{2,}/g, ' '), queryString, matchingOptions).length > 0) {
                    visibleEntries.push(entry);
                }
            }
            return visibleEntries;
        }
        return function (queryString, resultCallback) {
            resultCallback(filterElements(queryString));
        };
    }
    TrivialComponents.defaultListQueryFunctionFactory = defaultListQueryFunctionFactory;
    function createProxy(delegate) {
        var proxyConstructor = function () {
        };
        proxyConstructor.prototype = delegate;
        var proxyConstructorTypescriptHack = proxyConstructor;
        return new proxyConstructorTypescriptHack();
    }
    TrivialComponents.createProxy = createProxy;
    function defaultEntryMatchingFunctionFactory(searchedPropertyNames, matchingOptions) {
        return function (entry, queryString, depth) {
            return searchedPropertyNames
                .some(function (propertyName) {
                var value = entry[propertyName];
                return value != null && trivialMatch(value.toString(), queryString, matchingOptions).length > 0;
            });
        };
    }
    TrivialComponents.defaultEntryMatchingFunctionFactory = defaultEntryMatchingFunctionFactory;
    function defaultTreeQueryFunctionFactory(topLevelEntries, entryMatchingFunction, childrenPropertyName, expandedPropertyName) {
        function findMatchingEntriesAndTheirAncestors(entry, queryString, nodeDepth) {
            var entryProxy = createProxy(entry);
            entryProxy[childrenPropertyName] = [];
            entryProxy[expandedPropertyName] = false;
            if (entry[childrenPropertyName]) {
                for (var i = 0; i < entry[childrenPropertyName].length; i++) {
                    var child = entry[childrenPropertyName][i];
                    var childProxy = findMatchingEntriesAndTheirAncestors(child, queryString, nodeDepth + 1);
                    if (childProxy) {
                        entryProxy[childrenPropertyName].push(childProxy);
                        entryProxy[expandedPropertyName] = true;
                    }
                }
            }
            var hasMatchingChildren = entryProxy[childrenPropertyName].length > 0;
            var matchesItself = entryMatchingFunction(entry, queryString, nodeDepth);
            if (matchesItself && !hasMatchingChildren) {
                entryProxy[childrenPropertyName] = entry[childrenPropertyName];
            }
            return matchesItself || hasMatchingChildren ? entryProxy : null;
        }
        return function (queryString, resultCallback) {
            if (!queryString) {
                resultCallback(topLevelEntries);
            }
            else {
                var matchingEntries = [];
                for (var i = 0; i < topLevelEntries.length; i++) {
                    var topLevelEntry = topLevelEntries[i];
                    var entryProxy = findMatchingEntriesAndTheirAncestors(topLevelEntry, queryString, 0);
                    if (entryProxy) {
                        matchingEntries.push(entryProxy);
                    }
                }
                resultCallback(matchingEntries);
            }
        };
    }
    TrivialComponents.defaultTreeQueryFunctionFactory = defaultTreeQueryFunctionFactory;
    function customTreeQueryFunctionFactory(topLevelEntries, childrenPropertyName, expandedPropertyName, customNodeMatchingFunction) {
        function findMatchingEntriesAndTheirAncestors(entry, queryString, nodeDepth) {
            var entryProxy = createProxy(entry);
            entryProxy[childrenPropertyName] = [];
            entryProxy[expandedPropertyName] = false;
            if (entry[childrenPropertyName]) {
                for (var i = 0; i < entry[childrenPropertyName].length; i++) {
                    var child = entry[childrenPropertyName][i];
                    var childProxy = findMatchingEntriesAndTheirAncestors(child, queryString, nodeDepth + 1);
                    if (childProxy) {
                        entryProxy[childrenPropertyName].push(childProxy);
                        entryProxy[expandedPropertyName] = true;
                    }
                }
            }
            var hasMatchingChildren = entryProxy[childrenPropertyName].length > 0;
            var matchesItself = customNodeMatchingFunction(entry, queryString, nodeDepth);
            if (matchesItself && !hasMatchingChildren) {
                entryProxy[childrenPropertyName] = entry[childrenPropertyName];
            }
            return matchesItself || hasMatchingChildren ? entryProxy : null;
        }
        return function (queryString, resultCallback) {
            if (!queryString) {
                resultCallback(topLevelEntries);
            }
            else {
                var matchingEntries = [];
                for (var i = 0; i < topLevelEntries.length; i++) {
                    var topLevelEntry = topLevelEntries[i];
                    var entryProxy = findMatchingEntriesAndTheirAncestors(topLevelEntry, queryString, 0);
                    if (entryProxy) {
                        matchingEntries.push(entryProxy);
                    }
                }
                resultCallback(matchingEntries);
            }
        };
    }
    TrivialComponents.customTreeQueryFunctionFactory = customTreeQueryFunctionFactory;
    function selectElementContents(domElement, start, end) {
        domElement = domElement.firstChild || domElement;
        end = end || start;
        var range = document.createRange();
        range.setStart(domElement, start);
        range.setEnd(domElement, end);
        var sel = window.getSelection();
        try {
            sel.removeAllRanges();
        }
        catch (e) {
        }
        sel.addRange(range);
    }
    TrivialComponents.selectElementContents = selectElementContents;
    TrivialComponents.escapeSpecialRegexCharacter = function (s) {
        return s.replace(/[-[\]{}()*+?.,\\^$|#\s]/g, "\\$&");
    };
    function objectEquals(x, y) {
        'use strict';
        if (x === null || x === undefined || y === null || y === undefined) {
            return x === y;
        }
        if (x.constructor !== y.constructor) {
            return false;
        }
        if (x instanceof Function) {
            return x === y;
        }
        if (x instanceof RegExp) {
            return x === y;
        }
        if (x === y || x.valueOf() === y.valueOf()) {
            return true;
        }
        if (Array.isArray(x) && x.length !== y.length) {
            return false;
        }
        if (x instanceof Date) {
            return false;
        }
        if (!(x instanceof Object)) {
            return false;
        }
        if (!(y instanceof Object)) {
            return false;
        }
        var p = Object.keys(x);
        return Object.keys(y).every(function (i) {
            return p.indexOf(i) !== -1;
        }) &&
            p.every(function (i) {
                return objectEquals(x[i], y[i]);
            });
    }
    TrivialComponents.objectEquals = objectEquals;
    function trivialMatch(text, searchString, options) {
        if (!searchString) {
            return [{
                    start: 0,
                    length: text.length
                }];
        }
        options = $.extend({
            matchingMode: 'contains',
            ignoreCase: true,
            maxLevenshteinDistance: 3
        }, options || null);
        if (options.ignoreCase) {
            text = text.toLowerCase();
            searchString = searchString.toLowerCase();
        }
        function findRegexMatches(regex) {
            var matches = [];
            var match;
            while (match = regex.exec(text)) {
                matches.push({
                    start: match.index,
                    length: match[0].length
                });
            }
            return matches;
        }
        function findLevenshteinMatches(text, searchString) {
            var levenshtein = new Levenshtein(text, searchString);
            if (levenshtein.distance <= options.maxLevenshteinDistance) {
                return [{
                        start: 0,
                        length: searchString.length,
                        distance: levenshtein.distance
                    }];
            }
            else {
                return [];
            }
        }
        if (options.matchingMode == 'contains') {
            searchString = searchString.replace(/[-[\]{}()*+?.,\\^$|#\s]/g, "\\$&");
            return findRegexMatches(new RegExp(searchString, "g"));
        }
        else if (options.matchingMode == 'prefix') {
            searchString = searchString.replace(/[-[\]{}()*+?.,\\^$|#\s]/g, "\\$&");
            return findRegexMatches(new RegExp('^' + searchString, "g"));
        }
        else if (options.matchingMode == 'prefix-word') {
            searchString = searchString.replace(/[-[\]{}()*+?.,\\^$|#\s]/g, "\\$&");
            if (searchString.charAt(0).match(/^\w/)) {
                return findRegexMatches(new RegExp('\\b' + searchString, "g"));
            }
            else {
                return findRegexMatches(new RegExp(searchString, "g"));
            }
        }
        else if (options.matchingMode == 'prefix-levenshtein') {
            return findLevenshteinMatches(text.substr(0, Math.min(searchString.length, text.length)), searchString);
        }
        else if (options.matchingMode == 'levenshtein') {
            return findLevenshteinMatches(text, searchString);
        }
        else {
            throw "unknown matchingMode: " + options.matchingMode;
        }
    }
    TrivialComponents.trivialMatch = trivialMatch;
})(TrivialComponents || (TrivialComponents = {}));

var TrivialComponents;
(function (TrivialComponents) {
    TrivialComponents.keyCodes = {
        backspace: 8,
        tab: 9,
        enter: 13,
        shift: 16,
        ctrl: 17,
        alt: 18,
        pause: 19,
        caps_lock: 20,
        escape: 27,
        space: 32,
        page_up: 33,
        page_down: 34,
        end: 35,
        home: 36,
        left_arrow: 37,
        up_arrow: 38,
        right_arrow: 39,
        down_arrow: 40,
        insert: 45,
        "delete": 46,
        left_window_key: 91,
        right_window_key: 92,
        select_key: 93,
        num_lock: 144,
        scroll_lock: 145,
        specialKeys: [8, 9, 13, 16, 17, 18, 19, 20, 27, 33, 34, 35, 36, 37, 38, 39, 40, 45, 46, 91, 92, 93, 144, 145],
        numberKeys: [48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 96, 97, 98, 99, 100, 101, 102, 103, 104, 105],
        isSpecialKey: function (keyCode) {
            return this.specialKeys.indexOf(keyCode) != -1;
        },
        isDigitKey: function (keyCode) {
            return this.numberKeys.indexOf(keyCode) != -1;
        },
        isModifierKey: function (e) {
            return [TrivialComponents.keyCodes.shift, TrivialComponents.keyCodes.caps_lock, TrivialComponents.keyCodes.alt, TrivialComponents.keyCodes.ctrl, TrivialComponents.keyCodes.left_window_key, TrivialComponents.keyCodes.right_window_key]
                .indexOf(e.which) != -1;
        }
    };
})(TrivialComponents || (TrivialComponents = {}));

var TrivialComponents;
(function (TrivialComponents) {
    var WeekDay;
    (function (WeekDay) {
        WeekDay[WeekDay["MONDAY"] = 1] = "MONDAY";
        WeekDay[WeekDay["TUESDAY"] = 2] = "TUESDAY";
        WeekDay[WeekDay["WEDNESDAY"] = 3] = "WEDNESDAY";
        WeekDay[WeekDay["THURSDAY"] = 4] = "THURSDAY";
        WeekDay[WeekDay["FRIDAY"] = 5] = "FRIDAY";
        WeekDay[WeekDay["SATURDAY"] = 6] = "SATURDAY";
        WeekDay[WeekDay["SUNDAY"] = 7] = "SUNDAY";
    })(WeekDay = TrivialComponents.WeekDay || (TrivialComponents.WeekDay = {}));
    var TrivialCalendarBox = (function () {
        function TrivialCalendarBox($container, options) {
            if (options === void 0) { options = {}; }
            this.$container = $container;
            this.onChange = new TrivialComponents.TrivialEvent(this);
            this.onOnEditingTimeUnitChange = new TrivialComponents.TrivialEvent(this);
            this.config = $.extend({
                selectedDate: moment(),
                firstDayOfWeek: WeekDay.MONDAY,
                mode: 'datetime',
                highlightKeyboardNavigationState: false
            }, options);
            this.keyboardNavigationState = this.config.mode == 'time' ? 'hour' : 'day';
            this.keyboardNavCssClass = this.config.highlightKeyboardNavigationState ? "keyboard-nav" : "";
            this.selectedDate = this.config.selectedDate;
            this.$calendarBox = $('<div class="tr-calendarbox"/>').appendTo(this.$container);
            this.$calendarDisplay = $('<div class="tr-calendar-display"/>');
            this.$yearDisplay = $('<div class="year"><span class="back-button"/><span class="name"/><span class="forward-button"/></div>').appendTo(this.$calendarDisplay);
            this.$monthDisplay = $('<div class="month"><span class="back-button"/><span class="name"/><span class="forward-button"/></div>').appendTo(this.$calendarDisplay);
            this.$monthTable = $('<div class="month-table">').appendTo(this.$calendarDisplay);
            this.$year = this.$yearDisplay.find(".name");
            this.$month = this.$monthDisplay.find(".name");
            this.$yearDisplay.click(this.setKeyboardNavigationState.bind(this, "year"));
            this.$yearDisplay.find('.back-button').click(this.navigateByUnit.bind(this, "year", "left", true));
            this.$yearDisplay.find('.forward-button').click(this.navigateByUnit.bind(this, "year", "right", true));
            this.$monthDisplay.click(this.setKeyboardNavigationState.bind(this, "month"));
            this.$monthDisplay.find('.back-button').click(this.navigateByUnit.bind(this, "month", "left", true));
            this.$monthDisplay.find('.forward-button').click(this.navigateByUnit.bind(this, "month", "right", true));
            this.$clockDisplay = $('<div class="tr-clock-display"/>')
                .append('<svg class="clock" viewBox="0 0 100 100" width="100" height="100"> ' +
                '<circle class="clockcircle" cx="50" cy="50" r="45"/> ' +
                '<g class="ticks" > ' +
                ' <line x1="50" y1="5.000" x2="50.00" y2="10.00"/> <line x1="72.50" y1="11.03" x2="70.00" y2="15.36"/> <line x1="88.97" y1="27.50" x2="84.64" y2="30.00"/> <line x1="95.00" y1="50.00" x2="90.00" y2="50.00"/> <line x1="88.97" y1="72.50" x2="84.64" y2="70.00"/> <line x1="72.50" y1="88.97" x2="70.00" y2="84.64"/> <line x1="50.00" y1="95.00" x2="50.00" y2="90.00"/> <line x1="27.50" y1="88.97" x2="30.00" y2="84.64"/> <line x1="11.03" y1="72.50" x2="15.36" y2="70.00"/> <line x1="5.000" y1="50.00" x2="10.00" y2="50.00"/> <line x1="11.03" y1="27.50" x2="15.36" y2="30.00"/> <line x1="27.50" y1="11.03" x2="30.00" y2="15.36"/> ' +
                '</g> ' +
                '<g class="numbers">' +
                ' <text x="50" y="22">12</text> <text x="85" y="55">3</text> <text x="50" y="88">6</text> <text x="15" y="55">9</text> ' +
                '</g> ' +
                '<g class="hands">' +
                ' <line class="minutehand" x1="50" y1="50" x2="50" y2="20"/>' +
                ' <line class="hourhand" x1="50" y1="50" x2="50" y2="26"/> ' +
                '</g> ' +
                '<g class="am-pm-box">' +
                ' <rect x="58" y="59" width="20" height="15"/>' +
                ' <text class="am-pm-text" x="60" y="70" >??</text>' +
                '</g>' +
                '</svg>').append('<div class="digital-time-display"><div class="hour-wrapper">' +
                '<div class="up-button"/><div class="hour">??</div><div class="down-button"/>' +
                '</div>:<div class="minute-wrapper">' +
                '<div class="up-button"/><div class="minute">??</div><div class="down-button"/>' +
                '</div></div>');
            this.$hourHand = this.$clockDisplay.find('.hourhand');
            this.$minuteHand = this.$clockDisplay.find('.minutehand');
            this.$amPmText = this.$clockDisplay.find('.am-pm-text');
            this.$digitalTimeHourDisplayWrapper = this.$clockDisplay.find('.digital-time-display .hour-wrapper');
            this.$digitalTimeHourDisplay = this.$clockDisplay.find('.digital-time-display .hour');
            this.$digitalTimeHourDisplayWrapper.click(this.setKeyboardNavigationState.bind(this, "hour"));
            this.$digitalTimeHourDisplayWrapper.find(".up-button").click(this.navigateByUnit.bind(this, "hour", "up", true));
            this.$digitalTimeHourDisplayWrapper.find(".down-button").click(this.navigateByUnit.bind(this, "hour", "down", true));
            this.$digitalTimeMinuteDisplayWrapper = this.$clockDisplay.find('.digital-time-display .minute-wrapper');
            this.$digitalTimeMinuteDisplay = this.$clockDisplay.find('.digital-time-display .minute');
            this.$digitalTimeMinuteDisplayWrapper.click(this.setKeyboardNavigationState.bind(this, "minute"));
            this.$digitalTimeMinuteDisplayWrapper.find(".up-button").click(this.navigateByUnit.bind(this, "minute", "up", true));
            this.$digitalTimeMinuteDisplayWrapper.find(".down-button").click(this.navigateByUnit.bind(this, "minute", "down", true));
            if (this.config.mode == 'date' || this.config.mode == 'datetime') {
                this.$calendarDisplay.appendTo(this.$calendarBox);
            }
            if (this.config.mode == 'time' || this.config.mode === 'datetime') {
                this.$clockDisplay.appendTo(this.$calendarBox);
            }
            if (this.selectedDate) {
                this.updateMonthDisplay(this.selectedDate);
                this.updateClockDisplay(this.selectedDate);
            }
            else {
                this.updateMonthDisplay(moment());
                this.updateClockDisplay(moment());
            }
        }
        TrivialCalendarBox.getDaysForCalendarDisplay = function (dateInMonthDoBeDisplayed, firstDayOfWeek) {
            var firstDayOfMonth = dateInMonthDoBeDisplayed.clone().utc().startOf('month').hour(12);
            var firstDayToBeDisplayed = firstDayOfMonth.clone().isoWeekday(firstDayOfWeek <= firstDayOfMonth.isoWeekday() ? firstDayOfWeek : firstDayOfWeek - 7);
            var daysOfMonth = [];
            for (var day = firstDayToBeDisplayed.clone(); daysOfMonth.length < 42; day.add(1, 'day')) {
                daysOfMonth.push(day.clone());
            }
            return daysOfMonth;
        };
        TrivialCalendarBox.prototype.updateMonthDisplay = function (dateInMonthToBeDisplayed) {
            var _this = this;
            this.$year.text(dateInMonthToBeDisplayed.year());
            this.$month.text(moment.months()[dateInMonthToBeDisplayed.month()]);
            this.$monthTable.remove();
            this.$monthTable = $('<div class="month-table">').appendTo(this.$calendarDisplay);
            var daysToBeDisplayed = TrivialComponents.TrivialCalendarBox.getDaysForCalendarDisplay(dateInMonthToBeDisplayed, 1);
            var $tr = $('<tr>').appendTo(this.$monthTable);
            for (var i = 0; i < 7; i++) {
                $tr.append('<th>' + moment.weekdaysMin()[(this.config.firstDayOfWeek + i) % 7] + '</th>');
            }
            for (var w = 0; w < daysToBeDisplayed.length / 7; w++) {
                $tr = $('<tr>').appendTo(this.$monthTable);
                for (var d = 0; d < 7; d++) {
                    var day = daysToBeDisplayed[w * 7 + d];
                    var $td = $('<td>' + day.date() + '</td>');
                    if (day.month() == dateInMonthToBeDisplayed.month()) {
                        $td.addClass('current-month');
                    }
                    else {
                        $td.addClass('other-month');
                    }
                    if (day.year() == moment().year() && day.dayOfYear() == moment().dayOfYear()) {
                        $td.addClass('today');
                    }
                    if (day.year() == this.selectedDate.year() && day.dayOfYear() == this.selectedDate.dayOfYear()) {
                        $td.addClass('selected');
                        if (this.keyboardNavigationState === 'day') {
                            $td.addClass(this.keyboardNavCssClass);
                        }
                    }
                    $td.click((function (day) {
                        _this.setKeyboardNavigationState("day");
                        _this.setMonthAndDay(day.month() + 1, day.date(), true);
                    }).bind(this, day));
                    $tr.append($td);
                }
            }
        };
        TrivialCalendarBox.prototype.updateClockDisplay = function (date) {
            this.$amPmText.text(date.hour() >= 12 ? 'pm' : 'am');
            var minutesAngle = date.minute() * 6;
            var hours = (date.hour() % 12) + date.minute() / 60;
            var hourAngle = hours * 30;
            this.$hourHand.attr("transform", "rotate(" + hourAngle + ",50,50)");
            this.$minuteHand.attr("transform", "rotate(" + minutesAngle + ",50,50)");
            this.$digitalTimeHourDisplay.text(date.format('HH'));
            this.$digitalTimeMinuteDisplay.text(date.format('mm'));
        };
        TrivialCalendarBox.prototype.updateDisplay = function () {
            this.updateMonthDisplay(this.selectedDate);
            this.updateClockDisplay(this.selectedDate);
        };
        ;
        TrivialCalendarBox.prototype.setSelectedDate = function (moment) {
            this.selectedDate = moment;
            this.updateDisplay();
        };
        TrivialCalendarBox.prototype.setYear = function (year, fireEvent) {
            this.selectedDate.year(year);
            this.updateDisplay();
            if (fireEvent) {
                this.onOnEditingTimeUnitChange.fire('year');
                this.fireChangeEvents('year');
            }
        };
        TrivialCalendarBox.prototype.setMonth = function (month, fireEvent) {
            this.selectedDate.month(month - 1);
            this.updateDisplay();
            if (fireEvent) {
                this.onOnEditingTimeUnitChange.fire('month');
                this.fireChangeEvents('month');
            }
        };
        TrivialCalendarBox.prototype.setDayOfMonth = function (dayOfMonth, fireEvent) {
            this.selectedDate.date(dayOfMonth);
            this.updateDisplay();
            if (fireEvent) {
                this.onOnEditingTimeUnitChange.fire('day');
                this.fireChangeEvents('day');
            }
        };
        TrivialCalendarBox.prototype.setMonthAndDay = function (month, day, fireEvent) {
            this.selectedDate.month(month - 1);
            this.selectedDate.date(day);
            this.updateDisplay();
            if (fireEvent) {
                this.onOnEditingTimeUnitChange.fire('day');
                this.fireChangeEvents('month');
                this.fireChangeEvents('day');
            }
        };
        TrivialCalendarBox.prototype.setHour = function (hour, fireEvent) {
            this.selectedDate.hour(hour);
            this.updateDisplay();
            if (fireEvent) {
                this.onOnEditingTimeUnitChange.fire('hour');
                this.fireChangeEvents('hour');
            }
        };
        TrivialCalendarBox.prototype.setMinute = function (minute, fireEvent) {
            this.selectedDate.minute(minute);
            this.updateDisplay();
            if (fireEvent) {
                this.onOnEditingTimeUnitChange.fire('minute');
                this.fireChangeEvents('minute');
            }
        };
        TrivialCalendarBox.prototype.fireChangeEvents = function (timeUnit) {
            this.$calendarBox.trigger("change");
            this.onChange.fire({
                value: this.getSelectedDate(),
                timeUnitEdited: timeUnit
            });
        };
        TrivialCalendarBox.prototype.setKeyboardNavigationState = function (newKeyboardNavigationState) {
            this.keyboardNavigationState = newKeyboardNavigationState;
            if (this.config.highlightKeyboardNavigationState) {
                var me_1 = this;
                $(this.$yearDisplay).add(this.$monthDisplay).add(this.$monthTable.find('td.' + this.keyboardNavCssClass)).add(this.$hourHand).add(this.$digitalTimeHourDisplayWrapper).add(this.$minuteHand).add(this.$digitalTimeMinuteDisplayWrapper)
                    .each(function () {
                    $(this).attr("class", $(this).attr("class").replace(me_1.keyboardNavCssClass, ''));
                });
                if (this.keyboardNavigationState == 'year') {
                    this.$yearDisplay.addClass(this.keyboardNavCssClass);
                }
                else if (this.keyboardNavigationState == 'month') {
                    this.$monthDisplay.addClass(this.keyboardNavCssClass);
                }
                else if (this.keyboardNavigationState == 'day') {
                    this.$monthTable.find(".selected").addClass(this.keyboardNavCssClass);
                }
                else if (this.keyboardNavigationState == 'hour') {
                    this.$hourHand.attr("class", "hourhand " + this.keyboardNavCssClass);
                    this.$digitalTimeHourDisplayWrapper.addClass(this.keyboardNavCssClass);
                }
                else if (this.keyboardNavigationState == 'minute') {
                    this.$minuteHand.attr("class", "minutehand " + this.keyboardNavCssClass);
                    this.$digitalTimeMinuteDisplayWrapper.addClass(this.keyboardNavCssClass);
                }
            }
        };
        TrivialCalendarBox.prototype.getSelectedDate = function () {
            return this.selectedDate;
        };
        ;
        TrivialCalendarBox.prototype.navigateByUnit = function (unit, direction, fireEvent) {
            if (fireEvent === void 0) { fireEvent = false; }
            if (unit == 'year') {
                if (direction == 'down' || direction == 'left') {
                    this.setYear(this.selectedDate.year() - 1, fireEvent);
                }
                else if (direction == 'up' || direction == 'right') {
                    this.setYear(this.selectedDate.year() + 1, fireEvent);
                }
                fireEvent && this.fireChangeEvents('year');
                return true;
            }
            else if (unit == 'month') {
                if (direction == 'down' || direction == 'left') {
                    this.setMonth(this.selectedDate.month(), fireEvent);
                }
                else if (direction == 'up' || direction == 'right') {
                    this.setMonth(this.selectedDate.month() + 2, fireEvent);
                }
                fireEvent && this.fireChangeEvents('month');
                return true;
            }
            else if (unit == 'day') {
                if (direction == 'down') {
                    this.selectedDate.dayOfYear(this.selectedDate.dayOfYear() + 7);
                }
                else if (direction == 'left') {
                    this.selectedDate.dayOfYear(this.selectedDate.dayOfYear() - 1);
                }
                else if (direction == 'up') {
                    this.selectedDate.dayOfYear(this.selectedDate.dayOfYear() - 7);
                }
                else if (direction == 'right') {
                    this.selectedDate.dayOfYear(this.selectedDate.dayOfYear() + 1);
                }
                this.updateDisplay();
                fireEvent && this.fireChangeEvents('day');
                return true;
            }
            else if (unit == 'hour') {
                if (direction == 'down' || direction == 'left') {
                    this.setHour(this.selectedDate.hour() - 1, fireEvent);
                }
                else if (direction == 'up' || direction == 'right') {
                    this.setHour(this.selectedDate.hour() + 1, fireEvent);
                }
                fireEvent && this.fireChangeEvents('hour');
                return true;
            }
            else if (unit == 'minute') {
                if (direction == 'down' || direction == 'left') {
                    this.setMinute(this.selectedDate.minute() - (this.selectedDate.minute() % 5) - 5, fireEvent);
                }
                else if (direction == 'up' || direction == 'right') {
                    this.setMinute(this.selectedDate.minute() - (this.selectedDate.minute() % 5) + 5, fireEvent);
                }
                fireEvent && this.fireChangeEvents('minute');
                return true;
            }
        };
        TrivialCalendarBox.prototype.navigate = function (direction) {
            this.navigateByUnit(this.keyboardNavigationState, direction);
        };
        ;
        TrivialCalendarBox.prototype.getMainDomElement = function () {
            return this.$calendarBox[0];
        };
        return TrivialCalendarBox;
    }());
    TrivialComponents.TrivialCalendarBox = TrivialCalendarBox;
})(TrivialComponents || (TrivialComponents = {}));

var TrivialComponents;
(function (TrivialComponents) {
    var TrivialComboBox = (function () {
        function TrivialComboBox(originalInput, options) {
            if (options === void 0) { options = {}; }
            var _this = this;
            this.$spinners = $();
            this.onSelectedEntryChanged = new TrivialComponents.TrivialEvent(this);
            this.onFocus = new TrivialComponents.TrivialEvent(this);
            this.onBlur = new TrivialComponents.TrivialEvent(this);
            this.isDropDownOpen = false;
            this.isEditorVisible = false;
            this.lastQueryString = null;
            this.lastCompleteInputQueryString = null;
            this.selectedEntry = null;
            this.lastCommittedValue = null;
            this.blurCausedByClickInsideComponent = false;
            this.autoCompleteTimeoutId = -1;
            this.doNoAutoCompleteBecauseBackspaceWasPressed = false;
            this.listBoxDirty = true;
            this.usingDefaultQueryFunction = false;
            this.config = $.extend({
                valueFunction: function (entry) { return entry ? entry.id : null; },
                entryRenderingFunction: function (entry) {
                    var template = entry.template || TrivialComponents.DEFAULT_TEMPLATES.image2LinesTemplate;
                    return Mustache.render(template, entry);
                },
                selectedEntryRenderingFunction: function (entry) {
                    if (entry.selectedEntryTemplate) {
                        return Mustache.render(entry.selectedEntryTemplate, entry);
                    }
                    else {
                        return _this.config.entryRenderingFunction(entry);
                    }
                },
                selectedEntry: undefined,
                spinnerTemplate: TrivialComponents.DEFAULT_TEMPLATES.defaultSpinnerTemplate,
                noEntriesTemplate: TrivialComponents.DEFAULT_TEMPLATES.defaultNoEntriesTemplate,
                textHighlightingEntryLimit: 100,
                entries: null,
                emptyEntry: {
                    _isEmptyEntry: true
                },
                queryFunction: null,
                autoComplete: true,
                autoCompleteDelay: 0,
                entryToEditorTextFunction: function (entry) {
                    return entry["displayValue"];
                },
                autoCompleteFunction: function (editorText, entry) {
                    if (editorText) {
                        for (var propertyName in entry) {
                            if (entry.hasOwnProperty(propertyName)) {
                                var propertyValue = entry[propertyName];
                                if (propertyValue && propertyValue.toString().toLowerCase().indexOf(editorText.toLowerCase()) === 0) {
                                    return propertyValue.toString();
                                }
                            }
                        }
                        return null;
                    }
                    else {
                        return _this.config.entryToEditorTextFunction(entry);
                    }
                },
                allowFreeText: false,
                freeTextEntryFactory: function (freeText) {
                    return {
                        displayValue: freeText,
                        _isFreeTextEntry: true
                    };
                },
                showClearButton: false,
                showTrigger: true,
                matchingOptions: {
                    matchingMode: 'contains',
                    ignoreCase: true,
                    maxLevenshteinDistance: 2
                },
                editingMode: 'editable',
                showDropDownOnResultsOnly: false
            }, options);
            if (!this.config.queryFunction) {
                this.config.queryFunction = TrivialComponents.defaultListQueryFunctionFactory(this.config.entries || [], this.config.matchingOptions);
                this.usingDefaultQueryFunction = true;
            }
            this.entries = this.config.entries;
            this.$originalInput = $(originalInput);
            this.$comboBox = $('<div class="tr-combobox tr-input-wrapper"/>')
                .insertAfter(this.$originalInput);
            this.$selectedEntryWrapper = $('<div class="tr-combobox-selected-entry-wrapper"/>').appendTo(this.$comboBox);
            if (this.config.showClearButton) {
                this.$clearButton = $('<div class="tr-remove-button">').appendTo(this.$comboBox);
                this.$clearButton.mousedown(function () {
                    _this.$editor.val("");
                    _this.setSelectedEntry(null, true, true);
                });
            }
            if (this.config.showTrigger) {
                this.$trigger = $('<div class="tr-trigger"><span class="tr-trigger-icon"/></div>').appendTo(this.$comboBox);
                this.$trigger.mousedown(function () {
                    if (_this.isDropDownOpen) {
                        _this.showEditor();
                        _this.closeDropDown();
                    }
                    else {
                        setTimeout(function () {
                            _this.showEditor();
                            _this.$editor.select();
                            _this.openDropDown();
                            _this.query();
                        });
                    }
                });
            }
            this.$dropDown = $('<div class="tr-dropdown"></div>')
                .scroll(function () {
                return false;
            });
            this.$dropDownTargetElement = $("body");
            this.setEditingMode(this.config.editingMode);
            this.$originalInput.addClass("tr-original-input");
            this.$editor = $('<input type="text" autocomplete="off"/>');
            this.$editor.prependTo(this.$comboBox).addClass("tr-combobox-editor tr-editor")
                .focus(function () {
                if (_this.blurCausedByClickInsideComponent) {
                }
                else {
                    _this.$originalInput.triggerHandler('focus');
                    _this.onFocus.fire();
                    _this.$comboBox.addClass('focus');
                    _this.showEditor();
                }
            })
                .blur(function () {
                if (_this.blurCausedByClickInsideComponent) {
                    _this.$editor.focus();
                }
                else {
                    _this.$originalInput.triggerHandler('blur');
                    _this.onBlur.fire();
                    _this.$comboBox.removeClass('focus');
                    if (_this.editorContainsFreeText()) {
                        if (!TrivialComponents.objectEquals(_this.getSelectedEntry(), _this.lastCommittedValue)) {
                            _this.setSelectedEntry(_this.getSelectedEntry(), true, true);
                        }
                    }
                    else {
                        _this.$editor.val("");
                        _this.setSelectedEntry(_this.lastCommittedValue, false, true);
                    }
                    _this.hideEditor();
                    _this.closeDropDown();
                }
            })
                .keydown(function (e) {
                if (TrivialComponents.keyCodes.isModifierKey(e)) {
                    return;
                }
                else if (e.which == TrivialComponents.keyCodes.tab) {
                    var highlightedEntry = _this.listBox.getHighlightedEntry();
                    if (_this.isDropDownOpen && highlightedEntry) {
                        _this.setSelectedEntry(highlightedEntry, true, true);
                    }
                    else if (!_this.$editor.val()) {
                        _this.setSelectedEntry(null, true, true);
                    }
                    else if (_this.config.allowFreeText) {
                        _this.setSelectedEntry(_this.getSelectedEntry(), true, true);
                    }
                    return;
                }
                else if (e.which == TrivialComponents.keyCodes.left_arrow || e.which == TrivialComponents.keyCodes.right_arrow) {
                    _this.showEditor();
                    return;
                }
                if (e.which == TrivialComponents.keyCodes.backspace || e.which == TrivialComponents.keyCodes.delete) {
                    _this.doNoAutoCompleteBecauseBackspaceWasPressed = true;
                }
                if (e.which == TrivialComponents.keyCodes.up_arrow || e.which == TrivialComponents.keyCodes.down_arrow) {
                    if (!_this.isEditorVisible) {
                        _this.$editor.select();
                        _this.showEditor();
                    }
                    var direction = e.which == TrivialComponents.keyCodes.up_arrow ? -1 : 1;
                    if (!_this.isDropDownOpen) {
                        _this.query(direction);
                        if (!_this.config.showDropDownOnResultsOnly) {
                            _this.openDropDown();
                        }
                    }
                    else {
                        _this.listBox.highlightNextEntry(direction);
                        _this.autoCompleteIfPossible(_this.config.autoCompleteDelay);
                    }
                    return false;
                }
                else if (e.which == TrivialComponents.keyCodes.enter) {
                    if (_this.isEditorVisible || _this.editorContainsFreeText()) {
                        e.preventDefault();
                        var highlightedEntry = _this.listBox.getHighlightedEntry();
                        if (_this.isDropDownOpen && highlightedEntry) {
                            _this.setSelectedEntry(highlightedEntry, true, true);
                        }
                        else if (!_this.$editor.val()) {
                            _this.setSelectedEntry(null, true, true);
                        }
                        else if (_this.config.allowFreeText) {
                            _this.setSelectedEntry(_this.getSelectedEntry(), true, true);
                        }
                        _this.closeDropDown();
                        _this.hideEditor();
                    }
                }
                else if (e.which == TrivialComponents.keyCodes.escape) {
                    e.preventDefault();
                    if (!(_this.editorContainsFreeText() && _this.isDropDownOpen)) {
                        _this.hideEditor();
                        _this.$editor.val("");
                        _this.entries = null;
                        _this.setSelectedEntry(_this.lastCommittedValue, false, true);
                    }
                    _this.closeDropDown();
                }
                else {
                    if (!_this.isEditorVisible) {
                        _this.showEditor();
                        _this.$editor.select();
                    }
                    if (!_this.config.showDropDownOnResultsOnly) {
                        _this.openDropDown();
                    }
                    setTimeout(function () {
                        if (_this.$editor.val()) {
                            _this.query(1);
                        }
                        else {
                            _this.query(0);
                            _this.listBox.setHighlightedEntry(null);
                        }
                    });
                }
            })
                .keyup(function (e) {
                if (!TrivialComponents.keyCodes.isModifierKey(e) && [TrivialComponents.keyCodes.enter, TrivialComponents.keyCodes.escape, TrivialComponents.keyCodes.tab].indexOf(e.which) === -1 && _this.isEntrySelected() && _this.$editor.val() !== _this.config.entryToEditorTextFunction(_this.selectedEntry)) {
                    _this.setSelectedEntry(null, false, true);
                }
            })
                .mousedown(function () {
                if (!_this.config.showDropDownOnResultsOnly) {
                    _this.openDropDown();
                }
                _this.query();
            });
            if (this.$originalInput.attr("tabindex")) {
                this.$editor.attr("tabindex", this.$originalInput.attr("tabindex"));
            }
            if (this.$originalInput.attr("autofocus")) {
                this.$editor.focus();
            }
            this.$comboBox.add(this.$dropDown).mousedown(function () {
                if (_this.$editor.is(":focus")) {
                    _this.blurCausedByClickInsideComponent = true;
                }
            }).mouseup(function () {
                if (_this.blurCausedByClickInsideComponent) {
                    _this.$editor.focus();
                    _this.blurCausedByClickInsideComponent = false;
                }
            }).mouseout(function () {
                if (_this.blurCausedByClickInsideComponent) {
                    _this.$editor.focus();
                    _this.blurCausedByClickInsideComponent = false;
                }
            });
            var configWithoutEntries = $.extend({}, this.config);
            configWithoutEntries.entries = [];
            this.listBox = new TrivialComponents.TrivialListBox(this.$dropDown, configWithoutEntries);
            this.listBox.onSelectedEntryChanged.addListener(function (selectedEntry) {
                if (selectedEntry) {
                    _this.setSelectedEntry(selectedEntry, true, !TrivialComponents.objectEquals(selectedEntry, _this.lastCommittedValue));
                    _this.listBox.setSelectedEntry(null);
                    _this.closeDropDown();
                }
                _this.hideEditor();
            });
            this.setSelectedEntry(this.config.selectedEntry, true, false);
            this.$selectedEntryWrapper.click(function () {
                _this.showEditor();
                _this.$editor.select();
                if (!_this.config.showDropDownOnResultsOnly) {
                    _this.openDropDown();
                }
                _this.query();
            });
            this.$comboBox.data("trivialComboBox", this);
        }
        TrivialComboBox.prototype.query = function (highlightDirection) {
            var _this = this;
            setTimeout(function () {
                var queryString = _this.getNonSelectedEditorValue();
                var completeInputString = _this.$editor.val();
                if (_this.lastQueryString !== queryString || _this.lastCompleteInputQueryString !== completeInputString) {
                    if (_this.$spinners.length === 0) {
                        var $spinner = $(_this.config.spinnerTemplate).appendTo(_this.$dropDown);
                        _this.$spinners = _this.$spinners.add($spinner);
                    }
                    _this.config.queryFunction(queryString, function (newEntries) {
                        _this.updateEntries(newEntries, highlightDirection);
                        if (_this.config.showDropDownOnResultsOnly && newEntries && newEntries.length > 0 && _this.$editor.is(":focus")) {
                            _this.openDropDown();
                        }
                    });
                    _this.lastQueryString = queryString;
                    _this.lastCompleteInputQueryString = completeInputString;
                }
            }, 0);
        };
        TrivialComboBox.prototype.fireChangeEvents = function (entry) {
            this.$originalInput.trigger("change");
            this.onSelectedEntryChanged.fire(entry);
        };
        TrivialComboBox.prototype.setSelectedEntry = function (entry, commit, fireEvent) {
            if (commit === void 0) { commit = true; }
            if (fireEvent === void 0) { fireEvent = false; }
            if (entry == null) {
                this.$originalInput.val(this.config.valueFunction(null));
                this.selectedEntry = null;
                var $selectedEntry = $(this.config.selectedEntryRenderingFunction(this.config.emptyEntry))
                    .addClass("tr-combobox-entry")
                    .addClass("empty");
                this.$selectedEntryWrapper.empty().append($selectedEntry);
            }
            else {
                this.$originalInput.val(this.config.valueFunction(entry));
                this.selectedEntry = entry;
                var $selectedEntry = $(this.config.selectedEntryRenderingFunction(entry))
                    .addClass("tr-combobox-entry");
                this.$selectedEntryWrapper.empty().append($selectedEntry);
                this.$editor.val(this.config.entryToEditorTextFunction(entry));
            }
            if (commit) {
                this.lastCommittedValue = entry;
                if (fireEvent) {
                    this.fireChangeEvents(entry);
                }
            }
            if (this.$clearButton) {
                this.$clearButton.toggle(entry != null);
            }
            if (this.isEditorVisible) {
                this.showEditor();
            }
            if (this.isDropDownOpen) {
                this.repositionDropDown();
            }
        };
        TrivialComboBox.prototype.isEntrySelected = function () {
            return this.selectedEntry != null && this.selectedEntry !== this.config.emptyEntry;
        };
        TrivialComboBox.prototype.showEditor = function () {
            var $editorArea = this.$selectedEntryWrapper.find(".tr-editor-area");
            if ($editorArea.length === 0) {
                $editorArea = this.$selectedEntryWrapper;
            }
            this.$editor
                .css({
                "width": Math.min($editorArea[0].offsetWidth, this.$trigger ? this.$trigger[0].offsetLeft - $editorArea[0].offsetLeft : 99999999) + "px",
                "height": ($editorArea[0].offsetHeight) + "px"
            })
                .position({
                my: "left top",
                at: "left top",
                of: $editorArea
            });
            this.isEditorVisible = true;
        };
        TrivialComboBox.prototype.editorContainsFreeText = function () {
            return this.config.allowFreeText && this.$editor.val().length > 0 && !this.isEntrySelected();
        };
        ;
        TrivialComboBox.prototype.hideEditor = function () {
            this.$editor.width(0).height(0);
            this.isEditorVisible = false;
        };
        TrivialComboBox.prototype.repositionDropDown = function () {
            var _this = this;
            this.$dropDown
                .show()
                .position({
                my: "left top",
                at: "left bottom",
                of: this.$comboBox,
                collision: "flip",
                using: function (calculatedPosition, info) {
                    if (info.vertical === "top") {
                        _this.$comboBox.removeClass("dropdown-flipped");
                        _this.$dropDown.removeClass("flipped");
                    }
                    else {
                        _this.$comboBox.addClass("dropdown-flipped");
                        _this.$dropDown.addClass("flipped");
                    }
                    _this.$dropDown.css({
                        left: calculatedPosition.left + 'px',
                        top: calculatedPosition.top + 'px'
                    });
                }
            })
                .width(this.$comboBox.width());
        };
        ;
        TrivialComboBox.prototype.openDropDown = function () {
            if (this.isDropDownNeeded()) {
                if (this.listBoxDirty) {
                    this.updateListBoxEntries();
                }
                this.$comboBox.addClass("open");
                this.repositionDropDown();
                this.isDropDownOpen = true;
            }
        };
        TrivialComboBox.prototype.closeDropDown = function () {
            this.$comboBox.removeClass("open");
            this.$dropDown.hide();
            this.isDropDownOpen = false;
        };
        TrivialComboBox.prototype.getNonSelectedEditorValue = function () {
            return this.$editor.val().substring(0, this.$editor[0].selectionStart);
        };
        TrivialComboBox.prototype.autoCompleteIfPossible = function (delay) {
            var _this = this;
            if (this.config.autoComplete) {
                clearTimeout(this.autoCompleteTimeoutId);
                var highlightedEntry_1 = this.listBox.getHighlightedEntry();
                if (highlightedEntry_1 && !this.doNoAutoCompleteBecauseBackspaceWasPressed) {
                    this.autoCompleteTimeoutId = setTimeout(function () {
                        var currentEditorValue = _this.getNonSelectedEditorValue();
                        var autoCompleteString = _this.config.autoCompleteFunction(currentEditorValue, highlightedEntry_1) || currentEditorValue;
                        _this.$editor.val(currentEditorValue + autoCompleteString.substr(currentEditorValue.length));
                        if (_this.$editor.is(":focus")) {
                            _this.$editor[0].setSelectionRange(currentEditorValue.length, autoCompleteString.length);
                        }
                    }, delay || 0);
                }
                this.doNoAutoCompleteBecauseBackspaceWasPressed = false;
            }
        };
        TrivialComboBox.prototype.updateListBoxEntries = function () {
            this.listBox.updateEntries(this.entries);
            this.listBoxDirty = false;
        };
        TrivialComboBox.prototype.isDropDownNeeded = function () {
            return this.editingMode == 'editable' && (this.config.entries && this.config.entries.length > 0 || !this.usingDefaultQueryFunction || this.config.showTrigger);
        };
        TrivialComboBox.prototype.setEditingMode = function (newEditingMode) {
            this.editingMode = newEditingMode;
            this.$comboBox.removeClass("editable readonly disabled").addClass(this.editingMode);
            if (this.isDropDownNeeded()) {
                this.$dropDown.appendTo(this.$dropDownTargetElement);
            }
        };
        TrivialComboBox.prototype.updateEntries = function (newEntries, highlightDirection) {
            this.entries = newEntries;
            this.$spinners.remove();
            this.$spinners = $();
            if (this.isDropDownOpen) {
                this.updateListBoxEntries();
            }
            else {
                this.listBoxDirty = true;
            }
            var nonSelectedEditorValue = this.getNonSelectedEditorValue();
            this.listBox.highlightTextMatches(newEntries.length <= this.config.textHighlightingEntryLimit ? nonSelectedEditorValue : null);
            if (highlightDirection == null) {
                if (this.selectedEntry) {
                    this.listBox.setHighlightedEntry(null);
                }
                else {
                    this.listBox.highlightNextEntry(1);
                }
            }
            else if (highlightDirection === 0) {
                this.listBox.setHighlightedEntry(null);
            }
            else {
                this.listBox.highlightNextEntry(highlightDirection);
            }
            this.autoCompleteIfPossible(this.config.autoCompleteDelay);
            if (this.isDropDownOpen) {
                this.openDropDown();
            }
        };
        TrivialComboBox.prototype.getSelectedEntry = function () {
            if (this.selectedEntry == null && (!this.config.allowFreeText || !this.$editor.val())) {
                return null;
            }
            else if (this.selectedEntry == null && this.config.allowFreeText) {
                return this.config.freeTextEntryFactory(this.$editor.val());
            }
            else {
                var selectedEntryToReturn = jQuery.extend({}, this.selectedEntry);
                selectedEntryToReturn._trEntryElement = undefined;
                return selectedEntryToReturn;
            }
        };
        ;
        TrivialComboBox.prototype.focus = function () {
            this.showEditor();
            this.$editor.select();
        };
        ;
        TrivialComboBox.prototype.getDropDown = function () {
            return this.$dropDown;
        };
        ;
        TrivialComboBox.prototype.destroy = function () {
            this.$originalInput.removeClass('tr-original-input').insertBefore(this.$comboBox);
            this.$comboBox.remove();
            this.$dropDown.remove();
        };
        ;
        TrivialComboBox.prototype.getMainDomElement = function () {
            return this.$comboBox[0];
        };
        return TrivialComboBox;
    }());
    TrivialComponents.TrivialComboBox = TrivialComboBox;
})(TrivialComponents || (TrivialComponents = {}));


var TrivialComponents;
(function (TrivialComponents) {
    var Mode;
    (function (Mode) {
        Mode[Mode["MODE_CALENDAR"] = 0] = "MODE_CALENDAR";
        Mode[Mode["MODE_DATE_LIST"] = 1] = "MODE_DATE_LIST";
        Mode[Mode["MODE_TIME_LIST"] = 2] = "MODE_TIME_LIST";
    })(Mode || (Mode = {}));
    var TrivialDateTimeField = (function () {
        function TrivialDateTimeField(originalInput, options) {
            if (options === void 0) { options = {}; }
            var _this = this;
            this.dateIconTemplate = "<svg viewBox=\"0 0 540 540\" width=\"22\" height=\"22\" class=\"calendar-icon\">\n        <defs>\n            <linearGradient id=\"Gradient1\" x1=\"0\" x2=\"0\" y1=\"0\" y2=\"1\">\n                <stop class=\"calendar-symbol-ring-gradient-stop1\" offset=\"0%\"/>\n                <stop class=\"calendar-symbol-ring-gradient-stop2\" offset=\"50%\"/>\n                <stop class=\"calendar-symbol-ring-gradient-stop3\" offset=\"100%\"/>\n            </linearGradient>\n        </defs>        \n        <g id=\"layer1\">\n            <rect class=\"calendar-symbol-page-background\" x=\"90\" y=\"90\" width=\"360\" height=\"400\" ry=\"3.8\"></rect>\n            <rect class=\"calendar-symbol-color\" x=\"90\" y=\"90\" width=\"360\" height=\"100\" ry=\"3.5\"></rect>\n            <rect class=\"calendar-symbol-page\" x=\"90\" y=\"90\" width=\"360\" height=\"395\" ry=\"3.8\"></rect>\n            <rect class=\"calendar-symbol-ring\" fill=\"url(#Gradient2)\" x=\"140\" y=\"30\" width=\"40\" height=\"120\" ry=\"30.8\"></rect>\n            <rect class=\"calendar-symbol-ring\" fill=\"url(#Gradient2)\" x=\"250\" y=\"30\" width=\"40\" height=\"120\" ry=\"30.8\"></rect>\n            <rect class=\"calendar-symbol-ring\" fill=\"url(#Gradient2)\" x=\"360\" y=\"30\" width=\"40\" height=\"120\" ry=\"30.8\"></rect>\n            <text class=\"calendar-symbol-date\" x=\"270\" y=\"415\" text-anchor=\"middle\">{{weekDay}}</text>\n        </g>\n    </svg>";
            this.dateTemplate = '<div class="tr-template-icon-single-line">'
                + this.dateIconTemplate
                + '<div class="content-wrapper tr-editor-area">{{displayString}}</div>'
                + '</div>';
            this.timeIconTemplate = '<svg class="clock-icon night-{{isNight}}" viewBox="0 0 110 110" width="22" height="22"> ' +
                '<circle class="clockcircle" cx="55" cy="55" r="45"/>' +
                '<g class="hands">' +
                ' <line class="hourhand" x1="55" y1="55" x2="55" y2="35" {{#hourAngle}}transform="rotate({{hourAngle}},55,55)"{{/hourAngle}}/> ' +
                ' <line class="minutehand" x1="55" y1="55" x2="55" y2="22" {{#minuteAngle}}transform="rotate({{minuteAngle}},55,55)"{{/minuteAngle}}/>' +
                '</g> ' +
                '</svg>';
            this.timeTemplate = '<div class="tr-template-icon-single-line">' +
                this.timeIconTemplate +
                '  <div class="content-wrapper tr-editor-area">{{displayString}}</div>' +
                '</div>';
            this.onChange = new TrivialComponents.TrivialEvent(this);
            this.isDropDownOpen = false;
            this.dateValue = null;
            this.timeValue = null;
            this.blurCausedByClickInsideComponent = false;
            this.focusGoesToOtherEditor = false;
            this.autoCompleteTimeoutId = -1;
            this.doNoAutoCompleteBecauseBackspaceWasPressed = false;
            this.calendarBoxInitialized = false;
            this.dropDownMode = Mode.MODE_CALENDAR;
            options = options || {};
            this.config = $.extend({
                dateFormat: "MM/DD/YYYY",
                timeFormat: "HH:mm",
                autoComplete: true,
                autoCompleteDelay: 0,
                showTrigger: true,
                editingMode: "editable"
            }, options);
            this.$originalInput = $(originalInput).addClass("tr-original-input");
            this.$dateTimeField = $('<div class="tr-datetimefield tr-input-wrapper"/>')
                .addClass(this.config.editingMode)
                .insertAfter(this.$originalInput);
            var $editorWrapper = $('<div class="tr-editor-wrapper">').appendTo(this.$dateTimeField);
            this.$dateIconWrapper = $('<div class="tr-date-icon-wrapper"/>').appendTo($editorWrapper);
            this.$dateEditor = $('<div class="tr-date-editor" contenteditable="true"/>').appendTo($editorWrapper);
            this.$timeIconWrapper = $('<div class="tr-time-icon-wrapper"/>').appendTo($editorWrapper);
            this.$timeEditor = $('<div class="tr-time-editor" contenteditable="true"/>').appendTo($editorWrapper);
            this.$dateIconWrapper.click(function () {
                _this.$activeEditor = _this.$dateEditor;
                _this.setDropDownMode(Mode.MODE_CALENDAR);
                _this.openDropDown();
                TrivialComponents.selectElementContents(_this.$dateEditor[0], 0, _this.$dateEditor.text().length);
            });
            this.$timeIconWrapper.click(function () {
                _this.$activeEditor = _this.$timeEditor;
                _this.setDropDownMode(Mode.MODE_CALENDAR);
                TrivialComponents.selectElementContents(_this.$timeEditor[0], 0, _this.$timeEditor.text().length);
            });
            this.$dateEditor.focus(function () {
                _this.$activeEditor = _this.$dateEditor;
                if (!_this.blurCausedByClickInsideComponent || _this.focusGoesToOtherEditor) {
                    TrivialComponents.selectElementContents(_this.$dateEditor[0], 0, _this.$dateEditor.text().length);
                }
            });
            this.$timeEditor.focus(function () {
                _this.$activeEditor = _this.$timeEditor;
                if (!_this.blurCausedByClickInsideComponent || _this.focusGoesToOtherEditor) {
                    TrivialComponents.selectElementContents(_this.$timeEditor[0], 0, _this.$timeEditor.text().length);
                }
            });
            if (this.config.showTrigger) {
                var $trigger = $('<div class="tr-trigger"><span class="tr-trigger-icon"/></div>').appendTo(this.$dateTimeField);
                $trigger.mousedown(function () {
                    if (_this.isDropDownOpen) {
                        _this.closeDropDown();
                    }
                    else {
                        setTimeout(function () {
                            _this.setDropDownMode(Mode.MODE_CALENDAR);
                            _this.calendarBox.setSelectedDate(_this.dateValue ? _this.dateValue.moment : moment());
                            _this.$activeEditor = _this.$dateEditor;
                            TrivialComponents.selectElementContents(_this.$dateEditor[0], 0, _this.$dateEditor.text().length);
                            _this.openDropDown();
                        });
                    }
                });
            }
            this.$dropDown = $('<div class="tr-dropdown"></div>')
                .scroll(function () {
                return false;
            });
            this.dropdownNeeded = this.config.editingMode == 'editable';
            if (this.dropdownNeeded) {
                this.$dropDown.appendTo("body");
            }
            var $dateListBox = $('<div class="date-listbox">').appendTo(this.$dropDown);
            this.dateListBox = new TrivialComponents.TrivialListBox($dateListBox, {
                entryRenderingFunction: function (entry) {
                    return Mustache.render(_this.dateTemplate, entry);
                }
            });
            this.dateListBox.onSelectedEntryChanged.addListener(function (selectedEntry) {
                if (selectedEntry) {
                    _this.setDate(selectedEntry, selectedEntry.displayString != (_this.dateValue && _this.dateValue.displayString));
                    _this.dateListBox.setSelectedEntry(null);
                    _this.closeDropDown();
                }
            });
            var $timeListBox = $('<div class="time-listbox">').appendTo(this.$dropDown);
            this.timeListBox = new TrivialComponents.TrivialListBox($timeListBox, {
                entryRenderingFunction: function (entry) {
                    return Mustache.render(_this.timeTemplate, entry);
                }
            });
            this.timeListBox.onSelectedEntryChanged.addListener(function (selectedEntry) {
                if (selectedEntry) {
                    _this.setTime(selectedEntry, selectedEntry.displayString != (_this.timeValue && _this.timeValue.displayString));
                    _this.dateListBox.setSelectedEntry(null);
                    _this.closeDropDown();
                }
            });
            this.$calendarBox = $('<div class="calendarbox">').appendTo(this.$dropDown);
            this.$dateEditor
                .add(this.$timeEditor)
                .focus(function () {
                _this.$dateTimeField.addClass('focus');
            })
                .blur(function () {
                if (!_this.blurCausedByClickInsideComponent) {
                    _this.$dateTimeField.removeClass('focus');
                    _this.updateDisplay();
                    _this.closeDropDown();
                }
            })
                .keydown(function (e) {
                if (TrivialComponents.keyCodes.isModifierKey(e)) {
                    return;
                }
                else if (e.which == TrivialComponents.keyCodes.tab) {
                    _this.selectHighlightedListBoxEntry();
                    return;
                }
                else if (e.which == TrivialComponents.keyCodes.left_arrow || e.which == TrivialComponents.keyCodes.right_arrow) {
                    if (_this.getActiveEditor() === _this.$timeEditor && e.which == TrivialComponents.keyCodes.left_arrow && window.getSelection().focusOffset === 0) {
                        e.preventDefault();
                        TrivialComponents.selectElementContents(_this.$dateEditor[0], 0, _this.$dateEditor.text().length);
                    }
                    else if (_this.getActiveEditor() === _this.$dateEditor && e.which == TrivialComponents.keyCodes.right_arrow && window.getSelection().focusOffset === _this.$dateEditor.text().length) {
                        e.preventDefault();
                        TrivialComponents.selectElementContents(_this.$timeEditor[0], 0, _this.$timeEditor.text().length);
                    }
                    return;
                }
                if (e.which == TrivialComponents.keyCodes.backspace || e.which == TrivialComponents.keyCodes.delete) {
                    _this.doNoAutoCompleteBecauseBackspaceWasPressed = true;
                }
                if (e.which == TrivialComponents.keyCodes.up_arrow || e.which == TrivialComponents.keyCodes.down_arrow) {
                    _this.getActiveEditor().select();
                    var direction = e.which == TrivialComponents.keyCodes.up_arrow ? -1 : 1;
                    if (_this.isDropDownOpen) {
                        _this.setDropDownMode(e.currentTarget === _this.$dateEditor[0] ? Mode.MODE_DATE_LIST : Mode.MODE_TIME_LIST);
                        _this.query(direction);
                        _this.openDropDown();
                    }
                    else {
                        if (_this.dropDownMode !== Mode.MODE_CALENDAR) {
                            _this.getActiveBox().navigate(direction === 1 ? 'down' : 'up');
                            _this.autoCompleteIfPossible(_this.config.autoCompleteDelay);
                        }
                    }
                    return false;
                }
                else if (e.which == TrivialComponents.keyCodes.enter) {
                    if (_this.isDropDownOpen) {
                        e.preventDefault();
                        _this.selectHighlightedListBoxEntry();
                        TrivialComponents.selectElementContents(_this.getActiveEditor()[0], 0, _this.getActiveEditor().text().length);
                        _this.closeDropDown();
                    }
                }
                else if (e.which == TrivialComponents.keyCodes.escape) {
                    e.preventDefault();
                    if (_this.isDropDownOpen) {
                        _this.updateDisplay();
                        TrivialComponents.selectElementContents(_this.getActiveEditor()[0], 0, _this.getActiveEditor().text().length);
                    }
                    _this.closeDropDown();
                }
                else {
                    _this.setDropDownMode(e.currentTarget === _this.$dateEditor[0] ? Mode.MODE_DATE_LIST : Mode.MODE_TIME_LIST);
                    _this.query(1);
                    _this.openDropDown();
                }
            });
            if (this.$originalInput.val()) {
                this.setValue(moment(this.$originalInput.val()));
            }
            else {
                this.setValue(null);
            }
            if (this.$originalInput.attr("tabindex")) {
                this.$dateEditor.add(this.$timeEditor).attr("tabindex", this.$originalInput.attr("tabindex"));
            }
            if (this.$originalInput.attr("autofocus")) {
                this.$dateEditor.focus();
            }
            this.$dateTimeField.add(this.$dropDown).mousedown(function (e) {
                if (_this.$dateEditor.is(":focus") || _this.$timeEditor.is(":focus")) {
                    _this.blurCausedByClickInsideComponent = true;
                }
                if (e.target === _this.$dateEditor[0]
                    || e.target === _this.$timeEditor[0]
                    || e.target === _this.$dateIconWrapper[0]
                    || e.target === _this.$timeIconWrapper[0]) {
                    _this.focusGoesToOtherEditor = true;
                }
            }).on('mouseup mouseout', function () {
                if (_this.blurCausedByClickInsideComponent && !_this.focusGoesToOtherEditor) {
                    _this.getActiveEditor().focus();
                }
                _this.blurCausedByClickInsideComponent = false;
                _this.focusGoesToOtherEditor = false;
            });
            this.$activeEditor = this.$dateEditor;
        }
        TrivialDateTimeField.prototype.setDropDownMode = function (mode) {
            var _this = this;
            this.dropDownMode = mode;
            if (!this.calendarBoxInitialized && mode === Mode.MODE_CALENDAR) {
                this.calendarBox = new TrivialComponents.TrivialCalendarBox(this.$calendarBox, {
                    firstDayOfWeek: 1,
                    mode: 'date'
                });
                this.calendarBox.setKeyboardNavigationState('month');
                this.calendarBox.onChange.addListener(function (_a) {
                    var value = _a.value, timeUnitEdited = _a.timeUnitEdited;
                    _this.setDate(TrivialDateTimeField.createDateComboBoxEntry(value, _this.config.dateFormat));
                    if (timeUnitEdited === 'day') {
                        _this.closeDropDown();
                        _this.$activeEditor = _this.$timeEditor;
                        TrivialComponents.selectElementContents(_this.$timeEditor[0], 0, _this.$timeEditor.text().length);
                        _this.fireChangeEvents();
                    }
                });
                this.calendarBoxInitialized = true;
            }
            this.calendarBoxInitialized && $(this.calendarBox.getMainDomElement()).toggle(mode === Mode.MODE_CALENDAR);
            $(this.dateListBox.getMainDomElement()).toggle(mode === Mode.MODE_DATE_LIST);
            $(this.timeListBox.getMainDomElement()).toggle(mode === Mode.MODE_TIME_LIST);
        };
        TrivialDateTimeField.prototype.getActiveBox = function () {
            if (this.dropDownMode === Mode.MODE_CALENDAR) {
                return this.calendarBox;
            }
            else if (this.dropDownMode === Mode.MODE_DATE_LIST) {
                return this.dateListBox;
            }
            else {
                return this.timeListBox;
            }
        };
        TrivialDateTimeField.prototype.getActiveEditor = function () {
            return this.$activeEditor;
        };
        TrivialDateTimeField.prototype.selectHighlightedListBoxEntry = function () {
            if (this.dropDownMode === Mode.MODE_DATE_LIST || this.dropDownMode === Mode.MODE_TIME_LIST) {
                var highlightedEntry = this.getActiveBox().getHighlightedEntry();
                if (this.isDropDownOpen && highlightedEntry) {
                    if (this.getActiveEditor() === this.$dateEditor) {
                        this.setDate(highlightedEntry, true);
                    }
                    else {
                        this.setTime(highlightedEntry, true);
                    }
                }
            }
        };
        TrivialDateTimeField.prototype.query = function (highlightDirection) {
            var _this = this;
            setTimeout(function () {
                var queryString = _this.getNonSelectedEditorValue();
                if (_this.getActiveEditor() === _this.$dateEditor) {
                    TrivialDateTimeField.dateQueryFunction(queryString, function (newEntries) {
                        _this.updateEntries(newEntries, highlightDirection);
                    }, _this.config.dateFormat);
                }
                else {
                    TrivialDateTimeField.timeQueryFunction(queryString, function (newEntries) {
                        _this.updateEntries(newEntries, highlightDirection);
                    }, _this.config.timeFormat);
                }
            }, 0);
        };
        TrivialDateTimeField.prototype.getValue = function () {
            if (this.dateValue == null && this.timeValue == null) {
                return null;
            }
            else if (this.dateValue == null) {
                return null;
            }
            else if (this.timeValue == null) {
                return moment([
                    this.dateValue.year,
                    this.dateValue.month - 1,
                    this.dateValue.day
                ]).startOf('day');
            }
            else {
                return moment([
                    this.dateValue.year,
                    this.dateValue.month - 1,
                    this.dateValue.day,
                    this.timeValue.hour,
                    this.timeValue.minute
                ]);
            }
        };
        ;
        TrivialDateTimeField.prototype.fireChangeEvents = function () {
            this.$originalInput.trigger("change");
            this.onChange.fire(this.getValue());
        };
        TrivialDateTimeField.prototype.setDate = function (newDateValue, fireEvent) {
            if (fireEvent === void 0) { fireEvent = false; }
            this.dateValue = newDateValue;
            this.updateDisplay();
            if (fireEvent) {
                this.fireChangeEvents();
            }
        };
        TrivialDateTimeField.prototype.setTime = function (newTimeValue, fireEvent) {
            if (fireEvent === void 0) { fireEvent = false; }
            this.timeValue = newTimeValue;
            this.updateDisplay();
            if (fireEvent) {
                this.fireChangeEvents();
            }
        };
        TrivialDateTimeField.prototype.updateDisplay = function () {
            if (this.dateValue) {
                this.$dateEditor.text(moment([this.dateValue.year, this.dateValue.month - 1, this.dateValue.day]).format(this.config.dateFormat));
                this.$dateIconWrapper.empty().append(Mustache.render(this.dateIconTemplate, this.dateValue));
            }
            else {
                this.$dateEditor.text("");
                this.$dateIconWrapper.empty().append(Mustache.render(this.dateIconTemplate, {}));
            }
            if (this.timeValue) {
                this.$timeEditor.text(moment([1970, 0, 1, this.timeValue.hour, this.timeValue.minute]).format(this.config.timeFormat));
                this.$timeIconWrapper.empty().append(Mustache.render(this.timeIconTemplate, this.timeValue));
            }
            else {
                this.$timeEditor.text("");
                this.$timeIconWrapper.empty().append(Mustache.render(this.timeIconTemplate, {}));
            }
        };
        TrivialDateTimeField.prototype.setValue = function (mom) {
            this.setDate(mom && TrivialDateTimeField.createDateComboBoxEntry(mom, this.config.dateFormat));
            this.setTime(mom && TrivialDateTimeField.createTimeComboBoxEntry(mom.hour(), mom.minute(), this.config.timeFormat));
        };
        TrivialDateTimeField.prototype.repositionDropDown = function () {
            var _this = this;
            this.$dropDown
                .show()
                .position({
                my: "left top",
                at: "left bottom",
                of: this.$dateTimeField,
                collision: "flip",
                using: function (calculatedPosition, info) {
                    if (info.vertical === "top") {
                        _this.$dateTimeField.removeClass("dropdown-flipped");
                        _this.$dropDown.removeClass("flipped");
                    }
                    else {
                        _this.$dateTimeField.addClass("dropdown-flipped");
                        _this.$dropDown.addClass("flipped");
                    }
                    _this.$dropDown.css({
                        left: calculatedPosition.left + 'px',
                        top: calculatedPosition.top + 'px'
                    });
                }
            })
                .width(this.$dateTimeField.width());
        };
        TrivialDateTimeField.prototype.openDropDown = function () {
            if (this.dropdownNeeded) {
                this.$dateTimeField.addClass("open");
                this.repositionDropDown();
                this.isDropDownOpen = true;
            }
        };
        TrivialDateTimeField.prototype.closeDropDown = function () {
            this.$dateTimeField.removeClass("open");
            this.$dropDown.hide();
            this.isDropDownOpen = false;
        };
        TrivialDateTimeField.prototype.getNonSelectedEditorValue = function () {
            var editorText = this.getActiveEditor().text().replace(String.fromCharCode(160), " ");
            var selection = window.getSelection();
            if (selection.anchorOffset != selection.focusOffset) {
                return editorText.substring(0, Math.min(selection.anchorOffset, selection.focusOffset));
            }
            else {
                return editorText;
            }
        };
        TrivialDateTimeField.prototype.autoCompleteIfPossible = function (delay) {
            var _this = this;
            if (this.config.autoComplete && (this.dropDownMode === Mode.MODE_DATE_LIST || this.dropDownMode === Mode.MODE_TIME_LIST)) {
                clearTimeout(this.autoCompleteTimeoutId);
                var listBox = this.getActiveBox();
                var highlightedEntry = listBox.getHighlightedEntry();
                if (highlightedEntry && this.doNoAutoCompleteBecauseBackspaceWasPressed) {
                    var autoCompletingEntryDisplayValue_1 = highlightedEntry.displayString;
                    if (autoCompletingEntryDisplayValue_1) {
                        this.autoCompleteTimeoutId = setTimeout(function () {
                            var oldEditorValue = _this.getNonSelectedEditorValue();
                            var newEditorValue;
                            if (autoCompletingEntryDisplayValue_1.toLowerCase().indexOf(oldEditorValue.toLowerCase()) === 0) {
                                newEditorValue = oldEditorValue + autoCompletingEntryDisplayValue_1.substr(oldEditorValue.length);
                            }
                            else {
                                newEditorValue = _this.getNonSelectedEditorValue();
                            }
                            _this.getActiveEditor().text(newEditorValue);
                            if (_this.getActiveEditor().is(":focus")) {
                                TrivialComponents.selectElementContents(_this.getActiveEditor()[0], oldEditorValue.length, newEditorValue.length);
                            }
                        }, delay || 0);
                    }
                }
                this.doNoAutoCompleteBecauseBackspaceWasPressed = false;
            }
        };
        TrivialDateTimeField.prototype.updateEntries = function (newEntries, highlightDirection) {
            var listBox = this.getActiveBox();
            highlightDirection = highlightDirection === undefined ? 1 : highlightDirection;
            listBox.updateEntries(newEntries);
            listBox.highlightTextMatches(this.getNonSelectedEditorValue());
            listBox.highlightNextEntry(highlightDirection);
            this.autoCompleteIfPossible(this.config.autoCompleteDelay);
            if (this.isDropDownOpen) {
                this.openDropDown();
            }
        };
        TrivialDateTimeField.prototype.focus = function () {
            TrivialComponents.selectElementContents(this.getActiveEditor()[0], 0, this.getActiveEditor().text().length);
        };
        TrivialDateTimeField.prototype.destroy = function () {
            this.$originalInput.removeClass('tr-original-input').insertBefore(this.$dateTimeField);
            this.$dateTimeField.remove();
            this.$dropDown.remove();
        };
        TrivialDateTimeField.dateQueryFunction = function (searchString, resultCallback, dateFormat) {
            var suggestions;
            if (searchString.match(/[^\d]/)) {
                var fragments = searchString.split(/[^\d]/).filter(function (f) {
                    return !!f;
                });
                suggestions = TrivialDateTimeField.createSuggestionsForFragments(fragments, moment());
            }
            else {
                suggestions = TrivialDateTimeField.generateSuggestionsForDigitsOnlyInput(searchString, moment());
            }
            var seenMoments = [];
            suggestions = suggestions.filter(function (s) {
                if (seenMoments.filter(function (seenMoment) {
                    return s.moment.isSame(seenMoment, 'day');
                }).length > 0) {
                    return false;
                }
                else {
                    seenMoments.push(s.moment);
                    return true;
                }
            });
            var preferredYmdOrder = TrivialDateTimeField.dateFormatToYmdOrder(dateFormat);
            suggestions.sort(function (a, b) {
                if (preferredYmdOrder.indexOf(a.ymdOrder) === -1 && preferredYmdOrder.indexOf(b.ymdOrder) !== -1) {
                    return 1;
                }
                else if (preferredYmdOrder.indexOf(a.ymdOrder) !== -1 && preferredYmdOrder.indexOf(b.ymdOrder) === -1) {
                    return -1;
                }
                else if (a.ymdOrder.length != b.ymdOrder.length) {
                    return a.ymdOrder.length - b.ymdOrder.length;
                }
                else if (a.ymdOrder !== b.ymdOrder) {
                    return new Levenshtein(a.ymdOrder, preferredYmdOrder).distance - new Levenshtein(b.ymdOrder, preferredYmdOrder).distance;
                }
                else {
                    var today = moment();
                    return a.moment.diff(today, 'days') - b.moment.diff(today, 'days');
                }
            });
            resultCallback(suggestions.map(function (s) {
                return TrivialDateTimeField.createDateComboBoxEntry(s.moment, dateFormat);
            }));
        };
        TrivialDateTimeField.createDateComboBoxEntry = function (m, dateFormat) {
            return {
                moment: m,
                day: m.date(),
                weekDay: m.format('dd'),
                month: m.month() + 1,
                year: m.year(),
                displayString: m.format(dateFormat)
            };
        };
        TrivialDateTimeField.dateFormatToYmdOrder = function (dateFormat) {
            var ymdIndexes = {
                D: dateFormat.indexOf("D"),
                M: dateFormat.indexOf("M"),
                Y: dateFormat.indexOf("Y")
            };
            return ["D", "M", "Y"].sort(function (a, b) {
                return ymdIndexes[a] - ymdIndexes[b];
            }).join("");
        };
        TrivialDateTimeField.createDateParts = function (moment, ymdOrder) {
            return { moment: moment, ymdOrder: ymdOrder };
        };
        TrivialDateTimeField.generateSuggestionsForDigitsOnlyInput = function (input, today) {
            if (!input) {
                var result = [];
                for (var i = 0; i < 7; i++) {
                    result.push(TrivialDateTimeField.createDateParts(moment(today).add(i, "day"), ""));
                }
                return result;
            }
            else if (input.length > 8) {
                return [];
            }
            var suggestions = [];
            for (var i = 1; i <= input.length; i++) {
                for (var j = Math.min(input.length, i + 1); j <= input.length; j - i === 2 ? j += 2 : j++) {
                    suggestions = suggestions.concat(TrivialDateTimeField.createSuggestionsForFragments([input.substring(0, i), input.substring(i, j), input.substring(j, input.length)], today));
                }
            }
            return suggestions;
        };
        TrivialDateTimeField.createSuggestionsForFragments = function (fragments, today) {
            var todayOrFuture = function (m) {
                return today.isBefore(m, 'day') || today.isSame(m, 'day');
            };
            var numberToYear = function (n) {
                var shortYear = today.year() % 100;
                var yearSuggestionBoundary = (shortYear + 20) % 100;
                var currentCentury = Math.floor(today.year() / 100) * 100;
                if (n < yearSuggestionBoundary) {
                    return currentCentury + n;
                }
                else if (n < 100) {
                    return currentCentury - 100 + n;
                }
                else if (n > today.year() - 100 && n < today.year() + 100) {
                    return n;
                }
                else {
                    return null;
                }
            };
            var s1 = fragments[0];
            var s2 = fragments[1], s3 = fragments[2];
            var n1 = parseInt(s1), n2 = parseInt(s2), n3 = parseInt(s3);
            var suggestions = [];
            if (s1 && !s2 && !s3) {
                var momentInCurrentMonth = moment([today.year(), today.month(), s1]);
                if (momentInCurrentMonth.isValid() && todayOrFuture(momentInCurrentMonth)) {
                    suggestions.push(TrivialDateTimeField.createDateParts(momentInCurrentMonth, "D"));
                }
                else {
                    var momentInNextMonth = moment([today.year() + (today.month() == 11 ? 1 : 0), (today.month() + 1) % 12, s1]);
                    if (momentInNextMonth.isValid()) {
                        suggestions.push(TrivialDateTimeField.createDateParts(momentInNextMonth, "D"));
                    }
                }
            }
            else if (s1 && s2 && !s3) {
                var mom = void 0;
                mom = moment([moment().year(), n1 - 1, s2]);
                if (mom.isValid() && todayOrFuture(mom)) {
                    suggestions.push(TrivialDateTimeField.createDateParts(mom, "MD"));
                }
                else {
                    mom = moment([moment().year() + 1, n1 - 1, s2]);
                    if (mom.isValid()) {
                        suggestions.push(TrivialDateTimeField.createDateParts(mom, "MD"));
                    }
                }
                mom = moment([moment().year(), n2 - 1, s1]);
                if (mom.isValid() && todayOrFuture(mom)) {
                    suggestions.push(TrivialDateTimeField.createDateParts(mom, "DM"));
                }
                else {
                    mom = moment([moment().year() + 1, n2 - 1, s1]);
                    if (mom.isValid()) {
                        suggestions.push(TrivialDateTimeField.createDateParts(mom, "DM"));
                    }
                }
            }
            else {
                var mom = void 0;
                mom = moment([numberToYear(n1), n2 - 1, s3]);
                if (mom.isValid()) {
                    suggestions.push(TrivialDateTimeField.createDateParts(mom, "YMD"));
                }
                mom = moment([numberToYear(n1), n3 - 1, s2]);
                if (mom.isValid()) {
                    suggestions.push(TrivialDateTimeField.createDateParts(mom, "YDM"));
                }
                mom = moment([numberToYear(n2), n1 - 1, s3]);
                if (mom.isValid()) {
                    suggestions.push(TrivialDateTimeField.createDateParts(mom, "MYD"));
                }
                mom = moment([numberToYear(n2), n3 - 1, s1]);
                if (mom.isValid()) {
                    suggestions.push(TrivialDateTimeField.createDateParts(mom, "DYM"));
                }
                mom = moment([numberToYear(n3), n1 - 1, s2]);
                if (mom.isValid()) {
                    suggestions.push(TrivialDateTimeField.createDateParts(mom, "MDY"));
                }
                mom = moment([numberToYear(n3), n2 - 1, s1]);
                if (mom.isValid()) {
                    suggestions.push(TrivialDateTimeField.createDateParts(mom, "DMY"));
                }
            }
            return suggestions;
        };
        TrivialDateTimeField.timeQueryFunction = function (searchString, resultCallback, timeFormat) {
            var suggestedValues = [];
            var match = searchString.match(/[^\d]/);
            var colonIndex = match != null ? match.index : null;
            if (colonIndex !== null) {
                var hourString = searchString.substring(0, colonIndex);
                var minuteString = searchString.substring(colonIndex + 1);
                suggestedValues = suggestedValues.concat(TrivialDateTimeField.createTimeComboBoxEntries(TrivialDateTimeField.createHourSuggestions(hourString), TrivialDateTimeField.createMinuteSuggestions(minuteString), timeFormat));
            }
            else if (searchString.length > 0) {
                if (searchString.length >= 2) {
                    var hourString_1 = searchString.substr(0, 2);
                    var minuteString_1 = searchString.substring(2, searchString.length);
                    suggestedValues = suggestedValues.concat(TrivialDateTimeField.createTimeComboBoxEntries(TrivialDateTimeField.createHourSuggestions(hourString_1), TrivialDateTimeField.createMinuteSuggestions(minuteString_1), timeFormat));
                }
                var hourString = searchString.substr(0, 1);
                var minuteString = searchString.substring(1, searchString.length);
                if (minuteString.length <= 2) {
                    suggestedValues = suggestedValues.concat(TrivialDateTimeField.createTimeComboBoxEntries(TrivialDateTimeField.createHourSuggestions(hourString), TrivialDateTimeField.createMinuteSuggestions(minuteString), timeFormat));
                }
            }
            else {
                suggestedValues = suggestedValues.concat(TrivialDateTimeField.createTimeComboBoxEntries(TrivialDateTimeField.intRange(6, 24).concat(TrivialDateTimeField.intRange(1, 5)), [0], timeFormat));
            }
            resultCallback(suggestedValues);
        };
        TrivialDateTimeField.intRange = function (fromInclusive, toInclusive) {
            var ints = [];
            for (var i = fromInclusive; i <= toInclusive; i++) {
                ints.push(i);
            }
            return ints;
        };
        TrivialDateTimeField.pad = function (num, size) {
            var s = num + "";
            while (s.length < size)
                s = "0" + s;
            return s;
        };
        TrivialDateTimeField.createTimeComboBoxEntry = function (h, m, timeFormat) {
            return {
                hour: h,
                minute: m,
                hourString: TrivialDateTimeField.pad(h, 2),
                minuteString: TrivialDateTimeField.pad(m, 2),
                displayString: moment().hour(h).minute(m).format(timeFormat),
                hourAngle: ((h % 12) + m / 60) * 30,
                minuteAngle: m * 6,
                isNight: h < 6 || h >= 20
            };
        };
        TrivialDateTimeField.createTimeComboBoxEntries = function (hourValues, minuteValues, timeFormat) {
            var entries = [];
            for (var i = 0; i < hourValues.length; i++) {
                var hour = hourValues[i];
                for (var j = 0; j < minuteValues.length; j++) {
                    var minute = minuteValues[j];
                    entries.push(TrivialDateTimeField.createTimeComboBoxEntry(hour, minute, timeFormat));
                }
            }
            return entries;
        };
        TrivialDateTimeField.createMinuteSuggestions = function (minuteString) {
            var m = parseInt(minuteString);
            if (isNaN(m)) {
                return [0];
            }
            else if (minuteString.length > 1) {
                return [m % 60];
            }
            else if (m < 6) {
                return [m * 10];
            }
            else {
                return [m % 60];
            }
        };
        TrivialDateTimeField.createHourSuggestions = function (hourString) {
            var h = parseInt(hourString);
            if (isNaN(h)) {
                return TrivialDateTimeField.intRange(1, 24);
            }
            else if (h < 12) {
                return [h, (h + 12) % 24];
            }
            else if (h <= 24) {
                return [h % 24];
            }
            else {
                return [];
            }
        };
        TrivialDateTimeField.prototype.getMainDomElement = function () {
            return this.$dateTimeField[0];
        };
        return TrivialDateTimeField;
    }());
    TrivialComponents.TrivialDateTimeField = TrivialDateTimeField;
})(TrivialComponents || (TrivialComponents = {}));

var TrivialComponents;
(function (TrivialComponents) {
    var TrivialEvent = (function () {
        function TrivialEvent(eventSource) {
            this.eventSource = eventSource;
            this.listeners = [];
        }
        TrivialEvent.prototype.addListener = function (fn) {
            this.listeners.push(fn);
        };
        ;
        TrivialEvent.prototype.removeListener = function (fn) {
            var listenerIndex = this.listeners.indexOf(fn);
            if (listenerIndex != -1) {
                this.listeners.splice(listenerIndex, 1);
            }
        };
        ;
        TrivialEvent.prototype.fire = function (eventObject, originalEvent) {
            for (var i = 0; i < this.listeners.length; i++) {
                this.listeners[i].call(this.listeners[i], eventObject, this.eventSource, originalEvent);
            }
        };
        ;
        return TrivialEvent;
    }());
    TrivialComponents.TrivialEvent = TrivialEvent;
})(TrivialComponents || (TrivialComponents = {}));

var TrivialComponents;
(function (TrivialComponents) {
    var TrivialListBox = (function () {
        function TrivialListBox($container, options) {
            if (options === void 0) { options = {}; }
            this.onSelectedEntryChanged = new TrivialComponents.TrivialEvent(this);
            this.config = $.extend({
                entryRenderingFunction: function (entry) {
                    var template = entry.template || TrivialComponents.DEFAULT_TEMPLATES.image2LinesTemplate;
                    return Mustache.render(template, entry);
                },
                selectedEntry: null,
                spinnerTemplate: TrivialComponents.DEFAULT_TEMPLATES.defaultSpinnerTemplate,
                entries: null,
                matchingOptions: {
                    matchingMode: 'contains',
                    ignoreCase: true,
                    maxLevenshteinDistance: 2
                }
            }, options);
            this.$listBox = $('<div class="tr-listbox"/>').appendTo($container);
            var me = this;
            this.$listBox.on("mousedown", ".tr-listbox-entry", function (e) {
                me.setSelectedEntry($(this).data("entry"), e, true);
            }).on("mouseup", ".tr-listbox-entry", function (e) {
                me.$listBox.trigger("mouseup", e);
            }).on("mouseenter", ".tr-listbox-entry", function (e) {
                me.setHighlightedEntry($(this).data("entry"));
            }).on("mouseleave", ".tr-listbox-entry", function (e) {
                if (!$(e.toElement).is('.tr-listbox-entry')) {
                    me.setHighlightedEntry(null);
                }
            });
            this.$entryList = $('<div class="tr-listbox-entry-list"></div>').appendTo(this.$listBox);
            if (this.config.entries) {
                this.entries = this.config.entries;
                this.updateEntryElements(this.entries);
            }
            this.$listBox.data("trivialListBox", this);
        }
        TrivialListBox.prototype.updateEntryElements = function (entries) {
            this.$entryList.detach();
            this.$entryList.empty();
            if (entries.length > 0) {
                for (var i = 0; i < entries.length; i++) {
                    var entry = entries[i];
                    var $entry = void 0;
                    if (!entry._trEntryElement) {
                        var html = this.config.entryRenderingFunction(entry);
                        $entry = $(html).addClass("tr-listbox-entry filterable-item");
                    }
                    else {
                        $entry = entry._trEntryElement;
                    }
                    $entry.appendTo(this.$entryList)
                        .data("entry", entry);
                    entry._trEntryElement = $entry;
                }
            }
            else {
                this.$entryList.append(this.config.noEntriesTemplate);
            }
            this.$entryList.appendTo(this.$listBox);
        };
        TrivialListBox.prototype.updateEntries = function (newEntries) {
            if (newEntries == null) {
                newEntries = [];
            }
            this.setHighlightedEntry(null);
            this.entries = newEntries;
            this.updateEntryElements(this.entries);
        };
        TrivialListBox.prototype.minimallyScrollTo = function ($entryWrapper) {
            this.$listBox.parent().minimallyScrollTo($entryWrapper);
        };
        TrivialListBox.prototype.setHighlightedEntry = function (entry) {
            if (entry !== this.highlightedEntry) {
                this.highlightedEntry = entry;
                this.$entryList.find('.tr-listbox-entry').removeClass('tr-highlighted-entry');
                if (entry != null) {
                    entry._trEntryElement.addClass('tr-highlighted-entry');
                    this.minimallyScrollTo(entry._trEntryElement);
                }
            }
        };
        TrivialListBox.prototype.fireChangeEvents = function (selectedEntry, originalEvent) {
            this.$listBox.trigger("change");
            this.onSelectedEntryChanged.fire(selectedEntry, originalEvent);
        };
        TrivialListBox.prototype.setSelectedEntry = function (entry, originalEvent, fireEvent) {
            if (fireEvent === void 0) { fireEvent = false; }
            this.selectedEntry = entry;
            this.$entryList.find(".tr-selected-entry").removeClass("tr-selected-entry");
            if (entry != null) {
                this.selectedEntry._trEntryElement.addClass("tr-selected-entry");
            }
            if (fireEvent) {
                this.fireChangeEvents(this.selectedEntry, originalEvent);
            }
        };
        TrivialListBox.prototype.highlightNextEntry = function (direction) {
            var newHighlightedEntry = this.getNextHighlightableEntry(direction);
            if (newHighlightedEntry != null) {
                this.setHighlightedEntry(newHighlightedEntry);
            }
        };
        TrivialListBox.prototype.getNextHighlightableEntry = function (direction) {
            var newHighlightedElementIndex;
            if (this.entries == null || this.entries.length == 0) {
                return null;
            }
            else if (this.highlightedEntry == null && direction > 0) {
                newHighlightedElementIndex = -1 + direction;
            }
            else if (this.highlightedEntry == null && direction < 0) {
                newHighlightedElementIndex = this.entries.length + direction;
            }
            else {
                var currentHighlightedElementIndex = this.entries.indexOf(this.highlightedEntry);
                newHighlightedElementIndex = (currentHighlightedElementIndex + this.entries.length + direction) % this.entries.length;
            }
            return this.entries[newHighlightedElementIndex];
        };
        TrivialListBox.prototype.highlightTextMatches = function (searchString) {
            for (var i = 0; i < this.entries.length; i++) {
                var $entryElement = this.entries[i]._trEntryElement;
                $entryElement.trivialHighlight(searchString, this.config.matchingOptions);
            }
        };
        TrivialListBox.prototype.getSelectedEntry = function () {
            if (this.selectedEntry) {
                var selectedEntryToReturn = jQuery.extend({}, this.selectedEntry);
                selectedEntryToReturn._trEntryElement = undefined;
                return selectedEntryToReturn;
            }
            else {
                return null;
            }
        };
        TrivialListBox.prototype.getHighlightedEntry = function () {
            return this.highlightedEntry;
        };
        ;
        TrivialListBox.prototype.navigate = function (direction) {
            if (direction === 'up') {
                this.highlightNextEntry(-1);
            }
            else if (direction === 'down') {
                this.highlightNextEntry(1);
            }
        };
        TrivialListBox.prototype.getMainDomElement = function () {
            return this.$listBox[0];
        };
        return TrivialListBox;
    }());
    TrivialComponents.TrivialListBox = TrivialListBox;
})(TrivialComponents || (TrivialComponents = {}));

var TrivialComponents;
(function (TrivialComponents) {
    var TrivialTagComboBox = (function () {
        function TrivialTagComboBox(originalInput, options) {
            var _this = this;
            this.$spinners = $();
            this.onSelectedEntryChanged = new TrivialComponents.TrivialEvent(this);
            this.onFocus = new TrivialComponents.TrivialEvent(this);
            this.onBlur = new TrivialComponents.TrivialEvent(this);
            this.isDropDownOpen = false;
            this.lastQueryString = null;
            this.lastCompleteInputQueryString = null;
            this.selectedEntries = [];
            this.blurCausedByClickInsideComponent = false;
            this.autoCompleteTimeoutId = -1;
            this.doNoAutoCompleteBecauseBackspaceWasPressed = false;
            this.listBoxDirty = true;
            this.repositionDropDownScheduler = null;
            options = options || {};
            this.config = $.extend({
                valueFunction: function (entries) { return entries.map(function (e) { return e._isFreeTextEntry ? e.displayValue : e.id; }).join(','); },
                entryRenderingFunction: function (entry) {
                    var template = entry.template || TrivialComponents.DEFAULT_TEMPLATES.image2LinesTemplate;
                    return Mustache.render(template, entry);
                },
                selectedEntryRenderingFunction: function (entry) {
                    if (entry.selectedEntryTemplate) {
                        return Mustache.render(entry.selectedEntryTemplate, entry);
                    }
                    else {
                        return TrivialComponents.wrapWithDefaultTagWrapper(_this.config.entryRenderingFunction(entry));
                    }
                },
                spinnerTemplate: TrivialComponents.DEFAULT_TEMPLATES.defaultSpinnerTemplate,
                noEntriesTemplate: TrivialComponents.DEFAULT_TEMPLATES.defaultNoEntriesTemplate,
                textHighlightingEntryLimit: 100,
                entries: null,
                selectedEntries: [],
                maxSelectedEntries: null,
                queryFunction: null,
                autoComplete: true,
                autoCompleteDelay: 0,
                autoCompleteFunction: function (editorText, entry) {
                    if (editorText) {
                        for (var propertyName in entry) {
                            if (entry.hasOwnProperty(propertyName)) {
                                var propertyValue = entry[propertyName];
                                if (propertyValue && propertyValue.toString().toLowerCase().indexOf(editorText.toLowerCase()) === 0) {
                                    return propertyValue.toString();
                                }
                            }
                        }
                        return null;
                    }
                    else {
                        return null;
                    }
                },
                allowFreeText: false,
                freeTextSeparators: [',', ';'],
                freeTextEntryFactory: function (freeText) {
                    return {
                        displayValue: freeText,
                        _isFreeTextEntry: true
                    };
                },
                showTrigger: true,
                distinct: true,
                matchingOptions: {
                    matchingMode: 'contains',
                    ignoreCase: true,
                    maxLevenshteinDistance: 2
                },
                editingMode: "editable",
                showDropDownOnResultsOnly: false
            }, options);
            if (!this.config.queryFunction) {
                this.config.queryFunction = TrivialComponents.defaultListQueryFunctionFactory(this.config.entries || [], this.config.matchingOptions);
                this.usingDefaultQueryFunction = true;
            }
            this.entries = this.config.entries;
            this.$originalInput = $(originalInput).addClass("tr-original-input");
            this.$tagComboBox = $('<div class="tr-tagbox tr-input-wrapper"/>')
                .insertAfter(this.$originalInput);
            this.$originalInput.appendTo(this.$tagComboBox);
            var $tagArea = $('<div class="tr-tagbox-tagarea"/>').appendTo(this.$tagComboBox);
            if (this.config.showTrigger) {
                this.$trigger = $('<div class="tr-trigger"><span class="tr-trigger-icon"/></div>').appendTo(this.$tagComboBox);
                this.$trigger.mousedown(function () {
                    _this.$editor.focus();
                    if (_this.isDropDownOpen) {
                        _this.closeDropDown();
                    }
                    else {
                        setTimeout(function () {
                            _this.$editor.select();
                            _this.openDropDown();
                            _this.query();
                        });
                    }
                });
            }
            this.$dropDown = $('<div class="tr-dropdown"></div>')
                .scroll(function () {
                return false;
            });
            this.$dropDownTargetElement = $("body");
            this.setEditingMode(this.config.editingMode);
            this.$editor = $('<span contenteditable="true" class="tagbox-editor" autocomplete="off"></span>');
            this.$editor.appendTo($tagArea).addClass("tr-tagbox-editor tr-editor")
                .focus(function () {
                if (_this.blurCausedByClickInsideComponent) {
                }
                else {
                    _this.$originalInput.triggerHandler('focus');
                    _this.onFocus.fire();
                    _this.$tagComboBox.addClass('focus');
                }
                setTimeout(function () {
                    $tagArea.minimallyScrollTo(_this.$editor);
                });
            })
                .blur(function () {
                if (_this.blurCausedByClickInsideComponent) {
                    _this.$editor.focus();
                }
                else {
                    _this.$originalInput.triggerHandler('blur');
                    _this.onBlur.fire();
                    _this.$tagComboBox.removeClass('focus');
                    _this.entries = null;
                    _this.closeDropDown();
                    if (_this.config.allowFreeText && _this.$editor.text().trim().length > 0) {
                        _this.setSelectedEntry(_this.config.freeTextEntryFactory(_this.$editor.text()));
                    }
                    _this.$editor.text("");
                }
            })
                .keydown(function (e) {
                if (TrivialComponents.keyCodes.isModifierKey(e)) {
                    return;
                }
                else if (e.which == TrivialComponents.keyCodes.tab) {
                    var highlightedEntry = _this.listBox.getHighlightedEntry();
                    if (_this.isDropDownOpen && highlightedEntry) {
                        _this.setSelectedEntry(highlightedEntry);
                    }
                    return;
                }
                else if (e.which == TrivialComponents.keyCodes.left_arrow || e.which == TrivialComponents.keyCodes.right_arrow) {
                    if (e.which == TrivialComponents.keyCodes.left_arrow && _this.$editor.text().length === 0 && window.getSelection().anchorOffset === 0) {
                        if (_this.$editor.prev()) {
                            _this.$editor.insertBefore(_this.$editor.prev());
                            _this.$editor.focus();
                        }
                    }
                    else if (e.which == TrivialComponents.keyCodes.right_arrow && _this.$editor.text().length === 0 && window.getSelection().anchorOffset === 0) {
                        if (_this.$editor.next()) {
                            _this.$editor.insertAfter(_this.$editor.next());
                            _this.$editor.focus();
                        }
                    }
                    return;
                }
                if (e.which == TrivialComponents.keyCodes.backspace || e.which == TrivialComponents.keyCodes.delete) {
                    if (_this.$editor.text() == "") {
                        var tagToBeRemoved = _this.selectedEntries[_this.$editor.index() + (e.which == TrivialComponents.keyCodes.backspace ? -1 : 0)];
                        if (tagToBeRemoved) {
                            _this.removeTag(tagToBeRemoved);
                            _this.closeDropDown();
                        }
                    }
                    else {
                        _this.doNoAutoCompleteBecauseBackspaceWasPressed = true;
                        _this.query(1);
                    }
                    return;
                }
                if (e.which == TrivialComponents.keyCodes.up_arrow || e.which == TrivialComponents.keyCodes.down_arrow) {
                    _this.openDropDown();
                    var direction = e.which == TrivialComponents.keyCodes.up_arrow ? -1 : 1;
                    if (!_this.isDropDownOpen) {
                        _this.query(direction);
                        if (!_this.config.showDropDownOnResultsOnly) {
                            _this.openDropDown();
                        }
                    }
                    else {
                        _this.listBox.highlightNextEntry(direction);
                        _this.autoCompleteIfPossible(_this.config.autoCompleteDelay);
                    }
                    return false;
                }
                else if (e.which == TrivialComponents.keyCodes.enter) {
                    var highlightedEntry = _this.listBox.getHighlightedEntry();
                    if (_this.isDropDownOpen && highlightedEntry != null) {
                        _this.setSelectedEntry(highlightedEntry);
                        _this.entries = null;
                    }
                    else if (_this.config.allowFreeText && _this.$editor.text().trim().length > 0) {
                        _this.setSelectedEntry(_this.config.freeTextEntryFactory(_this.$editor.text()));
                    }
                    _this.closeDropDown();
                    e.preventDefault();
                }
                else if (e.which == TrivialComponents.keyCodes.escape) {
                    _this.closeDropDown();
                    _this.$editor.text("");
                }
                else {
                    if (!_this.config.showDropDownOnResultsOnly) {
                        _this.openDropDown();
                    }
                    _this.query(1);
                }
            })
                .keyup(function () {
                function splitStringBySeparatorChars(s, separatorChars) {
                    return s.split(new RegExp("[" + TrivialComponents.escapeSpecialRegexCharacter(separatorChars.join()) + "]"));
                }
                if (_this.$editor.find('*').length > 0) {
                    _this.$editor.text(_this.$editor.text());
                }
                if (_this.config.allowFreeText) {
                    var editorValueBeforeCursor = _this.getNonSelectedEditorValue();
                    if (editorValueBeforeCursor.length > 0) {
                        var tagValuesEnteredByUser = splitStringBySeparatorChars(editorValueBeforeCursor, _this.config.freeTextSeparators);
                        for (var i = 0; i < tagValuesEnteredByUser.length - 1; i++) {
                            var value = tagValuesEnteredByUser[i].trim();
                            if (value.length > 0) {
                                _this.setSelectedEntry(_this.config.freeTextEntryFactory(value));
                            }
                            _this.$editor.text(tagValuesEnteredByUser[tagValuesEnteredByUser.length - 1]);
                            TrivialComponents.selectElementContents(_this.$editor[0], _this.$editor.text().length, _this.$editor.text().length);
                            _this.entries = null;
                            _this.closeDropDown();
                        }
                    }
                }
            })
                .mousedown(function () {
                if (!_this.config.showDropDownOnResultsOnly) {
                    _this.openDropDown();
                }
                _this.query();
            });
            if (this.$originalInput.attr("placeholder")) {
                this.$editor.attr("placeholder", this.$originalInput.attr("placeholder"));
            }
            if (this.$originalInput.attr("tabindex")) {
                this.$editor.attr("tabindex", this.$originalInput.attr("tabindex"));
            }
            if (this.$originalInput.attr("autofocus")) {
                this.$editor.focus();
            }
            this.$tagComboBox.add(this.$dropDown).mousedown(function () {
                if (_this.$editor.is(":focus")) {
                    _this.blurCausedByClickInsideComponent = true;
                }
            }).mouseup(function () {
                if (_this.blurCausedByClickInsideComponent) {
                    _this.$editor.focus();
                    setTimeout(function () { return _this.blurCausedByClickInsideComponent = false; });
                }
            }).mouseout(function () {
                if (_this.blurCausedByClickInsideComponent) {
                    _this.$editor.focus();
                    setTimeout(function () { return _this.blurCausedByClickInsideComponent = false; });
                }
            });
            var configWithoutEntries = $.extend({}, this.config);
            configWithoutEntries.entries = [];
            this.listBox = new TrivialComponents.TrivialListBox(this.$dropDown, configWithoutEntries);
            this.listBox.onSelectedEntryChanged.addListener(function (selectedEntry) {
                if (selectedEntry) {
                    _this.setSelectedEntry(selectedEntry);
                    _this.listBox.setSelectedEntry(null);
                    _this.closeDropDown();
                }
            });
            this.setSelectedEntry(this.config.selectedEntry, true);
            $tagArea.click(function (e) {
                if (!_this.config.showDropDownOnResultsOnly) {
                    _this.openDropDown();
                }
                _this.query();
                var $tagWithSmallestDistance = null;
                var smallestDistanceX = 1000000;
                for (var i = 0; i < _this.selectedEntries.length; i++) {
                    var selectedEntry = _this.selectedEntries[i];
                    var $tag = selectedEntry._trEntryElement;
                    var tagBoundingRect = $tag[0].getBoundingClientRect();
                    var sameRow = e.clientY >= tagBoundingRect.top && e.clientY < tagBoundingRect.bottom;
                    var sameCol = e.clientX >= tagBoundingRect.left && e.clientX < tagBoundingRect.right;
                    var distanceX = sameCol ? 0 : Math.min(Math.abs(e.clientX - tagBoundingRect.left), Math.abs(e.clientX - tagBoundingRect.right));
                    if (sameRow && distanceX < smallestDistanceX) {
                        $tagWithSmallestDistance = $tag;
                        smallestDistanceX = distanceX;
                        if (distanceX === 0) {
                            break;
                        }
                    }
                }
                if ($tagWithSmallestDistance) {
                    var tagBoundingRect = $tagWithSmallestDistance[0].getBoundingClientRect();
                    var isRightSide = e.clientX > (tagBoundingRect.left + tagBoundingRect.right) / 2;
                    if (isRightSide) {
                        _this.$editor.insertAfter($tagWithSmallestDistance);
                    }
                    else {
                        _this.$editor.insertBefore($tagWithSmallestDistance);
                    }
                }
                _this.$editor.focus();
            });
            for (var i = 0; i < this.config.selectedEntries.length; i++) {
                this.setSelectedEntry(this.config.selectedEntries[i], true);
            }
            this.$tagComboBox.data("trivialTagComboBox", this);
        }
        TrivialTagComboBox.prototype.updateListBoxEntries = function () {
            this.listBox.updateEntries(this.entries);
            this.listBoxDirty = false;
        };
        TrivialTagComboBox.prototype.updateEntries = function (newEntries, highlightDirection) {
            this.entries = newEntries;
            this.$spinners.remove();
            this.$spinners = $();
            if (this.isDropDownOpen) {
                this.updateListBoxEntries();
            }
            else {
                this.listBoxDirty = true;
            }
            var nonSelectedEditorValue = this.getNonSelectedEditorValue();
            this.listBox.highlightTextMatches(newEntries.length <= this.config.textHighlightingEntryLimit ? nonSelectedEditorValue : null);
            if (highlightDirection) {
                this.listBox.highlightNextEntry(highlightDirection);
            }
            else {
                this.listBox.setHighlightedEntry(null);
            }
            this.autoCompleteIfPossible(this.config.autoCompleteDelay);
            if (this.isDropDownOpen) {
                this.openDropDown();
            }
        };
        TrivialTagComboBox.prototype.removeTag = function (tagToBeRemoved) {
            var index = this.selectedEntries.indexOf(tagToBeRemoved);
            if (index > -1) {
                this.selectedEntries.splice(index, 1);
            }
            tagToBeRemoved._trEntryElement.remove();
            this.$originalInput.val(this.config.valueFunction(this.getSelectedEntries()));
            this.fireChangeEvents(this.getSelectedEntries());
        };
        TrivialTagComboBox.prototype.query = function (highlightDirection) {
            var _this = this;
            setTimeout(function () {
                var queryString = _this.getNonSelectedEditorValue();
                var completeInputString = _this.$editor.text().replace(String.fromCharCode(160), " ");
                if (_this.lastQueryString !== queryString || _this.lastCompleteInputQueryString !== completeInputString) {
                    if (_this.$spinners.length === 0) {
                        var $spinner = $(_this.config.spinnerTemplate).appendTo(_this.$dropDown);
                        _this.$spinners = _this.$spinners.add($spinner);
                    }
                    _this.config.queryFunction(queryString, function (newEntries) {
                        _this.updateEntries(newEntries, highlightDirection);
                        if (_this.config.showDropDownOnResultsOnly && newEntries && newEntries.length > 0 && _this.$editor.is(":focus")) {
                            _this.openDropDown();
                        }
                    });
                    _this.lastQueryString = queryString;
                    _this.lastCompleteInputQueryString = completeInputString;
                }
            }, 0);
        };
        TrivialTagComboBox.prototype.fireChangeEvents = function (entries) {
            this.$originalInput.trigger("change");
            this.onSelectedEntryChanged.fire(entries);
        };
        TrivialTagComboBox.prototype.setSelectedEntry = function (entry, fireEvent) {
            var _this = this;
            if (fireEvent === void 0) { fireEvent = false; }
            if (entry == null) {
                return;
            }
            if (this.config.maxSelectedEntries && this.selectedEntries.length >= this.config.maxSelectedEntries) {
                return;
            }
            if (this.config.distinct && this.selectedEntries.map(function (entry) {
                return _this.config.valueFunction([entry]);
            }).indexOf(this.config.valueFunction([entry])) != -1) {
                return;
            }
            var tag = $.extend({}, entry);
            this.selectedEntries.splice(this.$editor.index(), 0, tag);
            this.$originalInput.val(this.config.valueFunction(this.getSelectedEntries()));
            var $entry = $(this.config.selectedEntryRenderingFunction(tag));
            var $tagWrapper = $('<div class="tr-tagbox-tag"></div>');
            $tagWrapper.append($entry).insertBefore(this.$editor);
            tag._trEntryElement = $tagWrapper;
            if (this.config.editingMode == "editable") {
                $entry.find('.tr-remove-button').click(function (e) {
                    _this.removeTag(tag);
                    return false;
                });
            }
            this.$editor.text("");
            if (fireEvent) {
                this.fireChangeEvents(this.getSelectedEntries());
            }
        };
        TrivialTagComboBox.prototype.repositionDropDown = function () {
            var _this = this;
            this.$dropDown.position({
                my: "left top",
                at: "left bottom",
                of: this.$tagComboBox,
                collision: "flip",
                using: function (calculatedPosition, info) {
                    if (info.vertical === "top") {
                        _this.$tagComboBox.removeClass("dropdown-flipped");
                        _this.$dropDown.removeClass("flipped");
                    }
                    else {
                        _this.$tagComboBox.addClass("dropdown-flipped");
                        _this.$dropDown.addClass("flipped");
                    }
                    _this.$dropDown.css({
                        left: calculatedPosition.left + 'px',
                        top: calculatedPosition.top + 'px'
                    });
                }
            }).width(this.$tagComboBox.width());
        };
        TrivialTagComboBox.prototype.openDropDown = function () {
            var _this = this;
            if (this.isDropDownNeeded()) {
                if (this.listBoxDirty) {
                    this.updateListBoxEntries();
                }
                this.$tagComboBox.addClass("open");
                this.$dropDown.show();
                this.repositionDropDown();
                this.isDropDownOpen = true;
            }
            if (this.repositionDropDownScheduler == null) {
                this.repositionDropDownScheduler = setInterval(function () { return _this.repositionDropDown(); }, 1000);
            }
        };
        TrivialTagComboBox.prototype.closeDropDown = function () {
            this.$tagComboBox.removeClass("open");
            this.$dropDown.hide();
            this.isDropDownOpen = false;
            if (this.repositionDropDownScheduler != null) {
                clearInterval(this.repositionDropDownScheduler);
            }
        };
        TrivialTagComboBox.prototype.getNonSelectedEditorValue = function () {
            var editorText = this.$editor.text().replace(String.fromCharCode(160), " ");
            var selection = window.getSelection();
            if (selection.anchorOffset != selection.focusOffset) {
                return editorText.substring(0, Math.min(window.getSelection().baseOffset, window.getSelection().focusOffset));
            }
            else {
                return editorText;
            }
        };
        TrivialTagComboBox.prototype.autoCompleteIfPossible = function (delay) {
            var _this = this;
            if (this.config.autoComplete) {
                clearTimeout(this.autoCompleteTimeoutId);
                var highlightedEntry_1 = this.listBox.getHighlightedEntry();
                if (highlightedEntry_1 && !this.doNoAutoCompleteBecauseBackspaceWasPressed) {
                    this.autoCompleteTimeoutId = setTimeout(function () {
                        var currentEditorValue = _this.getNonSelectedEditorValue();
                        var autoCompleteString = _this.config.autoCompleteFunction(currentEditorValue, highlightedEntry_1) || currentEditorValue;
                        _this.$editor.text(currentEditorValue + autoCompleteString.replace(' ', String.fromCharCode(160)).substr(currentEditorValue.length));
                        _this.repositionDropDown();
                        if (_this.$editor.is(":focus")) {
                            TrivialComponents.selectElementContents(_this.$editor[0], currentEditorValue.length, autoCompleteString.length);
                        }
                    }, delay || 0);
                }
                this.doNoAutoCompleteBecauseBackspaceWasPressed = false;
            }
        };
        TrivialTagComboBox.prototype.isDropDownNeeded = function () {
            return this.editingMode == 'editable' && (this.config.entries && this.config.entries.length > 0 || !this.usingDefaultQueryFunction || this.config.showTrigger);
        };
        TrivialTagComboBox.prototype.setEditingMode = function (newEditingMode) {
            this.editingMode = newEditingMode;
            this.$tagComboBox.removeClass("editable readonly disabled").addClass(this.editingMode);
            if (this.isDropDownNeeded()) {
                this.$dropDown.appendTo(this.$dropDownTargetElement);
            }
        };
        TrivialTagComboBox.prototype.setSelectedEntries = function (entries) {
            var _this = this;
            this.selectedEntries
                .slice()
                .forEach(function (e) { return _this.removeTag(e); });
            if (entries) {
                for (var i = 0; i < entries.length; i++) {
                    this.setSelectedEntry(entries[i], true);
                }
            }
        };
        TrivialTagComboBox.prototype.getSelectedEntries = function () {
            var selectedEntriesToReturn = [];
            for (var i = 0; i < this.selectedEntries.length; i++) {
                var selectedEntryToReturn = jQuery.extend({}, this.selectedEntries[i]);
                selectedEntryToReturn._trEntryElement = undefined;
                selectedEntriesToReturn.push(selectedEntryToReturn);
            }
            return selectedEntriesToReturn;
        };
        ;
        TrivialTagComboBox.prototype.focus = function () {
            this.$editor.focus();
            TrivialComponents.selectElementContents(this.$editor[0], 0, this.$editor.text().length);
        };
        ;
        TrivialTagComboBox.prototype.destroy = function () {
            this.$originalInput.removeClass('tr-original-input').insertBefore(this.$tagComboBox);
            this.$tagComboBox.remove();
            this.$dropDown.remove();
        };
        ;
        TrivialTagComboBox.prototype.getMainDomElement = function () {
            return this.$tagComboBox[0];
        };
        return TrivialTagComboBox;
    }());
    TrivialComponents.TrivialTagComboBox = TrivialTagComboBox;
})(TrivialComponents || (TrivialComponents = {}));

var TrivialComponents;
(function (TrivialComponents) {
    var TrivialTree = (function () {
        function TrivialTree(originalInput, options) {
            if (options === void 0) { options = {}; }
            var _this = this;
            this.onSelectedEntryChanged = new TrivialComponents.TrivialEvent(this);
            this.onNodeExpansionStateChanged = new TrivialComponents.TrivialEvent(this);
            this.$spinners = $();
            this.config = $.extend({
                valueFunction: function (entry) { return entry ? entry.id : null; },
                childrenProperty: "children",
                lazyChildrenFlagProperty: "hasLazyChildren",
                searchBarMode: 'show-if-filled',
                lazyChildrenQueryFunction: function (node, resultCallback) {
                    resultCallback([]);
                },
                expandedProperty: 'expanded',
                entryRenderingFunction: function (entry, depth) {
                    var defaultTemplates = [TrivialComponents.DEFAULT_TEMPLATES.icon2LinesTemplate, TrivialComponents.DEFAULT_TEMPLATES.iconSingleLineTemplate];
                    var template = entry.template || defaultTemplates[Math.min(depth, defaultTemplates.length - 1)];
                    return Mustache.render(template, entry);
                },
                spinnerTemplate: TrivialComponents.DEFAULT_TEMPLATES.defaultSpinnerTemplate,
                noEntriesTemplate: TrivialComponents.DEFAULT_TEMPLATES.defaultNoEntriesTemplate,
                entries: null,
                queryFunction: null,
                selectedEntryId: null,
                matchingOptions: {
                    matchingMode: 'contains',
                    ignoreCase: true,
                    maxLevenshteinDistance: 2
                },
                directSelectionViaArrowKeys: false,
                performanceOptimizationSettings: {
                    toManyVisibleItemsRenderDelay: 750,
                    toManyVisibleItemsThreshold: 75
                }
            }, options);
            if (!this.config.queryFunction) {
                this.config.queryFunction = TrivialComponents.defaultTreeQueryFunctionFactory(this.config.entries || [], TrivialComponents.defaultEntryMatchingFunctionFactory(["displayValue", "additionalInfo"], this.config.matchingOptions), this.config.childrenProperty, this.config.expandedProperty);
            }
            this.entries = this.config.entries;
            this.$originalInput = $(originalInput).addClass("tr-original-input");
            this.$componentWrapper = $('<div class="tr-tree" tabindex="0"/>').insertAfter(this.$originalInput);
            if (this.config.searchBarMode !== 'always-visible') {
                this.$componentWrapper.addClass("hide-searchfield");
            }
            this.$componentWrapper.keydown(function (e) {
                if (e.which == TrivialComponents.keyCodes.tab || TrivialComponents.keyCodes.isModifierKey(e)) {
                    return;
                }
                if (_this.$editor.is(':visible') && TrivialComponents.keyCodes.specialKeys.indexOf(e.which) === -1) {
                    _this.$editor.focus();
                }
                if (e.which == TrivialComponents.keyCodes.up_arrow || e.which == TrivialComponents.keyCodes.down_arrow) {
                    var direction = e.which == TrivialComponents.keyCodes.up_arrow ? -1 : 1;
                    if (_this.entries != null) {
                        if (_this.config.directSelectionViaArrowKeys) {
                            _this.treeBox.selectNextEntry(direction);
                        }
                        else {
                            _this.treeBox.highlightNextEntry(direction);
                        }
                        return false;
                    }
                }
                else if (e.which == TrivialComponents.keyCodes.left_arrow || e.which == TrivialComponents.keyCodes.right_arrow) {
                    _this.treeBox.setHighlightedNodeExpanded(e.which == TrivialComponents.keyCodes.right_arrow);
                }
                else if (e.which == TrivialComponents.keyCodes.enter) {
                    _this.treeBox.setSelectedEntry(_this.treeBox.getHighlightedEntry());
                }
                else if (e.which == TrivialComponents.keyCodes.escape) {
                    _this.$editor.val("");
                    _this.query();
                    _this.$componentWrapper.focus();
                }
                else {
                    _this.query(1);
                }
            });
            this.$tree = $('<div class="tr-tree-entryTree"></div>').appendTo(this.$componentWrapper);
            this.$editor = $('<input type="text" class="tr-tree-editor tr-editor"/>')
                .prependTo(this.$componentWrapper)
                .attr("tabindex", this.$originalInput.attr("-1"))
                .focus(function () {
                _this.$componentWrapper.addClass('focus');
            })
                .blur(function () {
                _this.$componentWrapper.removeClass('focus');
            })
                .keydown(function (e) {
                if (e.which == TrivialComponents.keyCodes.left_arrow || e.which == TrivialComponents.keyCodes.right_arrow) {
                    var changedExpandedState = _this.treeBox.setHighlightedNodeExpanded(e.which == TrivialComponents.keyCodes.right_arrow);
                    if (changedExpandedState) {
                        return false;
                    }
                    else {
                        return;
                    }
                }
            })
                .on('keyup change', function () {
                if (_this.config.searchBarMode === 'show-if-filled') {
                    if (_this.$editor.val()) {
                        _this.$componentWrapper.removeClass('hide-searchfield');
                    }
                    else {
                        _this.$componentWrapper.addClass('hide-searchfield');
                    }
                }
            });
            if (this.config.searchBarMode === 'none') {
                this.$editor.css("display", "none");
            }
            if (this.$originalInput.attr("placeholder")) {
                this.$editor.attr("placeholder", this.$originalInput.attr("placeholder"));
            }
            if (this.$originalInput.attr("tabindex")) {
                this.$componentWrapper.attr("tabindex", this.$originalInput.attr("tabindex"));
            }
            if (this.$originalInput.attr("autofocus")) {
                this.$componentWrapper.focus();
            }
            this.treeBox = new TrivialComponents.TrivialTreeBox(this.$tree, this.config);
            this.treeBox.onNodeExpansionStateChanged.addListener(function (node) {
                _this.onNodeExpansionStateChanged.fire(node);
            });
            this.treeBox.onSelectedEntryChanged.addListener(function () {
                var selectedTreeBoxEntry = _this.treeBox.getSelectedEntry();
                if (selectedTreeBoxEntry) {
                    _this.setSelectedEntry(selectedTreeBoxEntry);
                }
            });
            this.setSelectedEntry((this.config.selectedEntryId !== undefined && this.config.selectedEntryId !== null) ? this.findEntryById(this.config.selectedEntryId) : null);
        }
        TrivialTree.prototype.updateEntries = function (newEntries) {
            this.entries = newEntries;
            this.$spinners.remove();
            this.$spinners = $();
            this.treeBox.updateEntries(newEntries);
        };
        TrivialTree.prototype.query = function (highlightDirection) {
            var _this = this;
            if (this.config.searchBarMode === 'always-visible' || this.config.searchBarMode === 'show-if-filled') {
                var $spinner = $(this.config.spinnerTemplate).appendTo(this.$tree);
                this.$spinners = this.$spinners.add($spinner);
                setTimeout(function () {
                    _this.config.queryFunction(_this.$editor.val(), function (newEntries) {
                        var processUpdate = function () {
                            _this.updateEntries(newEntries);
                            if (_this.$editor.val().length > 0) {
                                _this.treeBox.highlightTextMatches(_this.$editor.val());
                                if (!_this.config.directSelectionViaArrowKeys) {
                                    _this.treeBox.highlightNextMatchingEntry(highlightDirection);
                                }
                            }
                            _this.treeBox.revealSelectedEntry();
                        };
                        clearTimeout(_this.processUpdateTimer);
                        if (_this.countVisibleEntries(newEntries) < _this.config.performanceOptimizationSettings.toManyVisibleItemsThreshold) {
                            processUpdate();
                        }
                        else {
                            _this.processUpdateTimer = setTimeout(processUpdate, _this.config.performanceOptimizationSettings.toManyVisibleItemsRenderDelay);
                        }
                    });
                }, 0);
            }
        };
        TrivialTree.prototype.countVisibleEntries = function (entries) {
            var _this = this;
            var countVisibleChildrenAndSelf = function (node) {
                if (node[_this.config.expandedProperty] && node[_this.config.childrenProperty]) {
                    return node[_this.config.childrenProperty].map(function (entry) {
                        return countVisibleChildrenAndSelf(entry);
                    }).reduce(function (a, b) {
                        return a + b;
                    }, 0) + 1;
                }
                else {
                    return 1;
                }
            };
            return entries.map(function (entry) {
                return countVisibleChildrenAndSelf(entry);
            }).reduce(function (a, b) {
                return a + b;
            }, 0);
        };
        TrivialTree.prototype.findEntries = function (filterFunction) {
            var _this = this;
            var findEntriesInSubTree = function (node, listOfFoundEntries) {
                if (filterFunction.call(_this, node)) {
                    listOfFoundEntries.push(node);
                }
                if (node[_this.config.childrenProperty]) {
                    for (var i = 0; i < node[_this.config.childrenProperty].length; i++) {
                        var child = node[_this.config.childrenProperty][i];
                        findEntriesInSubTree(child, listOfFoundEntries);
                    }
                }
            };
            var matchingEntries = [];
            for (var i = 0; i < this.entries.length; i++) {
                var rootEntry = this.entries[i];
                findEntriesInSubTree(rootEntry, matchingEntries);
            }
            return matchingEntries;
        };
        TrivialTree.prototype.findEntryById = function (id) {
            var _this = this;
            return this.findEntries(function (entry) {
                return _this.config.valueFunction(entry) === id.toString();
            })[0];
        };
        TrivialTree.prototype.setSelectedEntry = function (entry) {
            this.selectedEntryId = entry ? this.config.valueFunction(entry) : null;
            this.$originalInput.val(entry ? this.config.valueFunction(entry) : null);
            this.fireChangeEvents(entry);
        };
        TrivialTree.prototype.fireChangeEvents = function (entry) {
            this.$originalInput.trigger("change");
            this.$componentWrapper.trigger("change");
            this.onSelectedEntryChanged.fire(entry);
        };
        TrivialTree.prototype.getSelectedEntry = function () {
            this.treeBox.getSelectedEntry();
        };
        ;
        TrivialTree.prototype.updateChildren = function (parentNodeId, children) {
            this.treeBox.updateChildren(parentNodeId, children);
        };
        ;
        TrivialTree.prototype.updateNode = function (node) {
            this.treeBox.updateNode(node);
        };
        ;
        TrivialTree.prototype.removeNode = function (nodeId) {
            this.treeBox.removeNode(nodeId);
        };
        ;
        TrivialTree.prototype.addNode = function (parentNodeId, node) {
            this.treeBox.addNode(parentNodeId, node);
        };
        ;
        TrivialTree.prototype.selectNodeById = function (nodeId) {
            this.treeBox.setSelectedEntryById(nodeId);
        };
        ;
        TrivialTree.prototype.destroy = function () {
            this.$originalInput.removeClass('tr-original-input').insertBefore(this.$componentWrapper);
            this.$componentWrapper.remove();
        };
        ;
        TrivialTree.prototype.getMainDomElement = function () {
            return this.$componentWrapper[0];
        };
        return TrivialTree;
    }());
    TrivialComponents.TrivialTree = TrivialTree;
})(TrivialComponents || (TrivialComponents = {}));

var TrivialComponents;
(function (TrivialComponents) {
    var TrivialTreeBox = (function () {
        function TrivialTreeBox($container, options) {
            if (options === void 0) { options = {}; }
            this.onSelectedEntryChanged = new TrivialComponents.TrivialEvent(this);
            this.onNodeExpansionStateChanged = new TrivialComponents.TrivialEvent(this);
            this.config = $.extend({
                valueFunction: function (entry) { return entry ? entry.id : null; },
                childrenProperty: "children",
                lazyChildrenFlagProperty: "hasLazyChildren",
                lazyChildrenQueryFunction: function (node, resultCallback) {
                    resultCallback(node.children || []);
                },
                expandedProperty: 'expanded',
                entryRenderingFunction: function (entry, depth) {
                    var defaultTemplates = [TrivialComponents.DEFAULT_TEMPLATES.icon2LinesTemplate, TrivialComponents.DEFAULT_TEMPLATES.iconSingleLineTemplate];
                    var template = entry.template || defaultTemplates[Math.min(depth, defaultTemplates.length - 1)];
                    return Mustache.render(template, entry);
                },
                spinnerTemplate: TrivialComponents.DEFAULT_TEMPLATES.defaultSpinnerTemplate,
                noEntriesTemplate: TrivialComponents.DEFAULT_TEMPLATES.defaultNoEntriesTemplate,
                entries: null,
                selectedEntryId: null,
                matchingOptions: {
                    matchingMode: 'contains',
                    ignoreCase: true,
                    maxLevenshteinDistance: 2
                },
                animationDuration: 70,
                showExpanders: true,
                openOnSelection: false,
                enforceSingleExpandedPath: false
            }, options);
            this.entries = this.config.entries;
            this.$componentWrapper = $('<div class="tr-treebox"/>').appendTo($container);
            this.$componentWrapper.toggleClass("hide-expanders", !this.config.showExpanders);
            this.$tree = $('<div class="tr-tree-entryTree"></div>').appendTo(this.$componentWrapper);
            if (this.entries) {
                this.updateEntries(this.entries);
            }
            this.setSelectedEntry((this.config.selectedEntryId !== undefined && this.config.selectedEntryId !== null) ? this.findEntryById(this.config.selectedEntryId) : null);
        }
        TrivialTreeBox.prototype.isLeaf = function (entry) {
            return (entry[this.config.childrenProperty] == null || entry[this.config.childrenProperty].length == 0) && !entry[this.config.lazyChildrenFlagProperty];
        };
        TrivialTreeBox.prototype.createEntryElement = function (entry, depth) {
            var _this = this;
            var leaf = this.isLeaf(entry);
            var $outerEntryWrapper = $('<div class="tr-tree-entry-outer-wrapper ' + (leaf ? '' : 'has-children') + '" data-depth="' + depth + '"></div>');
            entry._trEntryElement = $outerEntryWrapper;
            var $entryAndExpanderWrapper = $('<div class="tr-tree-entry-and-expander-wrapper"></div>')
                .appendTo($outerEntryWrapper);
            for (var k = 0; k < depth; k++) {
                $entryAndExpanderWrapper.append('<div class="tr-indent-spacer"/>');
            }
            var $expander = $('<div class="tr-tree-expander"></div>')
                .appendTo($entryAndExpanderWrapper);
            var $entry = $(this.config.entryRenderingFunction(entry, depth));
            $entry.addClass("tr-tree-entry filterable-item").appendTo($entryAndExpanderWrapper);
            if (this.config.valueFunction(entry) === this.selectedEntryId) {
                $entryAndExpanderWrapper.addClass("tr-selected-entry");
            }
            $entryAndExpanderWrapper
                .mousedown(function (e) {
                _this.$componentWrapper.trigger("mousedown", e);
                _this.setSelectedEntry(entry);
            }).mouseup(function (e) {
                _this.$componentWrapper.trigger("mouseup", e);
            }).mouseenter(function () {
                _this.setHighlightedEntry(entry);
            }).mouseleave(function (e) {
                if (!$(e.toElement).is('.tr-tree-entry-outer-wrapper')) {
                    _this.setHighlightedEntry(null);
                }
            });
            if (!leaf) {
                var $childrenWrapper = $('<div class="tr-tree-entry-children-wrapper"></div>')
                    .appendTo($outerEntryWrapper);
                $expander.mousedown(function () {
                    return false;
                }).click(function (e) {
                    _this.setNodeExpanded(entry, !entry[_this.config.expandedProperty], true);
                });
                if (entry[this.config.childrenProperty]) {
                    if (entry[this.config.expandedProperty]) {
                        for (var i = 0; i < entry[this.config.childrenProperty].length; i++) {
                            this.createEntryElement(entry[this.config.childrenProperty][i], depth + 1).appendTo($childrenWrapper);
                        }
                    }
                }
                else if (entry[this.config.lazyChildrenFlagProperty]) {
                    $childrenWrapper.hide().append(this.config.spinnerTemplate).fadeIn();
                }
                this.setNodeExpanded(entry, entry[this.config.expandedProperty], false);
            }
            return $outerEntryWrapper;
        };
        TrivialTreeBox.prototype.updateTreeEntryElements = function () {
            this.$tree.detach();
            this.$tree = $('<div class="tr-tree-entryTree"></div>');
            if (this.entries.length > 0) {
                for (var i = 0; i < this.entries.length; i++) {
                    this.createEntryElement(this.entries[i], 0).appendTo(this.$tree);
                }
            }
            else {
                this.$tree.append(this.config.noEntriesTemplate);
            }
            this.$tree.appendTo(this.$componentWrapper);
        };
        TrivialTreeBox.prototype.setNodeExpanded = function (node, expanded, animate) {
            var _this = this;
            var wasExpanded = node[this.config.expandedProperty];
            if (expanded && this.config.enforceSingleExpandedPath) {
                var currentlyExpandedNodes = this.findEntries(function (n) {
                    return !!(n[_this.config.expandedProperty]);
                });
                var newExpandedPath = this.findPathToFirstMatchingNode(function (n) {
                    return n === node;
                });
                for (var i = 0; i < currentlyExpandedNodes.length; i++) {
                    var currentlyExpandedNode = currentlyExpandedNodes[i];
                    if (newExpandedPath.indexOf(currentlyExpandedNode) === -1) {
                        this.setNodeExpanded(currentlyExpandedNode, false, true);
                    }
                }
            }
            node[this.config.expandedProperty] = !!expanded;
            node._trEntryElement.toggleClass("expanded", !!expanded);
            var nodeHasUnrenderedChildren = function (node) {
                return node[_this.config.childrenProperty] && node[_this.config.childrenProperty].some(function (child) {
                    return !child._trEntryElement || !jQuery.contains(document.documentElement, child._trEntryElement[0]);
                });
            };
            if (expanded && node[this.config.lazyChildrenFlagProperty] && !node[this.config.childrenProperty]) {
                this.config.lazyChildrenQueryFunction(node, function (children) {
                    _this.setChildren(node, children);
                });
            }
            else if (expanded && nodeHasUnrenderedChildren(node)) {
                this.renderChildren(node);
            }
            if (expanded) {
                this.minimallyScrollTo(node._trEntryElement);
            }
            var childrenWrapper = node._trEntryElement.find("> .tr-tree-entry-children-wrapper");
            if (expanded) {
                if (animate) {
                    childrenWrapper.slideDown(this.config.animationDuration);
                }
                else {
                    childrenWrapper.css("display", "block");
                }
            }
            else {
                if (animate) {
                    childrenWrapper.slideUp(this.config.animationDuration);
                }
                else {
                    childrenWrapper.hide();
                }
            }
            if (!!wasExpanded != !!expanded) {
                this.onNodeExpansionStateChanged.fire(node);
            }
        };
        TrivialTreeBox.prototype.nodeDepth = function (node) {
            return node ? parseInt(node._trEntryElement.attr('data-depth')) : 0;
        };
        TrivialTreeBox.prototype.setChildren = function (node, children) {
            node[this.config.childrenProperty] = children;
            node[this.config.lazyChildrenFlagProperty] = false;
            this.renderChildren(node);
        };
        TrivialTreeBox.prototype.renderChildren = function (node) {
            var $childrenWrapper = node._trEntryElement.find('> .tr-tree-entry-children-wrapper');
            $childrenWrapper.empty();
            var children = node[this.config.childrenProperty];
            if (children && children.length > 0) {
                var depth = this.nodeDepth(node);
                for (var i = 0; i < children.length; i++) {
                    var child = children[i];
                    this.createEntryElement(child, depth + 1).appendTo($childrenWrapper);
                }
            }
            else {
                node._trEntryElement.removeClass('has-children expanded');
            }
        };
        TrivialTreeBox.prototype.updateEntries = function (newEntries) {
            this.highlightedEntry = null;
            this.entries = newEntries;
            this.updateTreeEntryElements();
            var selectedEntry = this.findEntryById(this.selectedEntryId);
            if (selectedEntry) {
                this.markSelectedEntry(selectedEntry);
            }
        };
        TrivialTreeBox.prototype.findEntries = function (filterFunction) {
            var _this = this;
            var findEntriesInSubTree = function (node, listOfFoundEntries) {
                if (filterFunction.call(_this, node)) {
                    listOfFoundEntries.push(node);
                }
                if (node[_this.config.childrenProperty]) {
                    for (var i = 0; i < node[_this.config.childrenProperty].length; i++) {
                        var child = node[_this.config.childrenProperty][i];
                        findEntriesInSubTree(child, listOfFoundEntries);
                    }
                }
            };
            var matchingEntries = [];
            for (var i = 0; i < this.entries.length; i++) {
                var rootEntry = this.entries[i];
                findEntriesInSubTree(rootEntry, matchingEntries);
            }
            return matchingEntries;
        };
        TrivialTreeBox.prototype.findPathToFirstMatchingNode = function (predicateFunction) {
            var _this = this;
            var searchInSubTree = function (node, path) {
                if (predicateFunction.call(_this, node, path)) {
                    path.push(node);
                    return path;
                }
                if (node[_this.config.childrenProperty]) {
                    var newPath = path.slice();
                    newPath.push(node);
                    for (var i = 0; i < node[_this.config.childrenProperty].length; i++) {
                        var child = node[_this.config.childrenProperty][i];
                        var result = searchInSubTree(child, newPath);
                        if (result) {
                            return result;
                        }
                    }
                }
            };
            for (var i = 0; i < this.entries.length; i++) {
                var rootEntry = this.entries[i];
                var path = searchInSubTree(rootEntry, []);
                if (path) {
                    return path;
                }
            }
        };
        TrivialTreeBox.prototype.findEntryById = function (id) {
            var _this = this;
            return this.findEntries(function (entry) {
                return _this.config.valueFunction(entry) == id;
            })[0];
        };
        TrivialTreeBox.prototype.findParentNode = function (childNode) {
            var _this = this;
            return this.findEntries(function (entry) {
                return entry[_this.config.childrenProperty] && entry[_this.config.childrenProperty].indexOf(childNode) != -1;
            })[0];
        };
        TrivialTreeBox.prototype.setSelectedEntry = function (entry) {
            this.selectedEntryId = entry ? this.config.valueFunction(entry) : null;
            this.markSelectedEntry(entry);
            this.setHighlightedEntry(entry);
            this.fireChangeEvents(entry);
            if (entry && this.config.openOnSelection) {
                this.setNodeExpanded(entry, true, true);
            }
        };
        TrivialTreeBox.prototype.setSelectedEntryById = function (nodeId) {
            this.setSelectedEntry(this.findEntryById(nodeId));
        };
        TrivialTreeBox.prototype.minimallyScrollTo = function ($entryWrapper) {
            this.$componentWrapper.parent().minimallyScrollTo($entryWrapper);
        };
        TrivialTreeBox.prototype.markSelectedEntry = function (entry) {
            this.$tree.find(".tr-selected-entry").removeClass("tr-selected-entry");
            if (entry && entry._trEntryElement) {
                var $entryWrapper = entry._trEntryElement.find('>.tr-tree-entry-and-expander-wrapper');
                $entryWrapper.addClass("tr-selected-entry");
            }
        };
        TrivialTreeBox.prototype.fireChangeEvents = function (entry) {
            this.$componentWrapper.trigger("change");
            this.onSelectedEntryChanged.fire(entry);
        };
        TrivialTreeBox.prototype.selectNextEntry = function (direction) {
            var nextVisibleEntry = this.getNextVisibleEntry(this.getSelectedEntry(), direction);
            if (nextVisibleEntry != null) {
                this.setSelectedEntry(nextVisibleEntry);
            }
        };
        TrivialTreeBox.prototype.setHighlightedEntry = function (entry) {
            if (entry !== this.highlightedEntry) {
                this.highlightedEntry = entry;
                this.$tree.find('.tr-highlighted-entry').removeClass('tr-highlighted-entry');
                if (entry != null && entry._trEntryElement) {
                    var $entry = entry._trEntryElement.find('>.tr-tree-entry-and-expander-wrapper');
                    $entry.addClass('tr-highlighted-entry');
                    this.minimallyScrollTo($entry);
                }
                else {
                    var selectedEntry = this.getSelectedEntry();
                    if (selectedEntry) {
                        this.highlightedEntry = selectedEntry;
                    }
                }
            }
        };
        TrivialTreeBox.prototype.getNextVisibleEntry = function (currentEntry, direction, onlyEntriesWithTextMatches) {
            if (onlyEntriesWithTextMatches === void 0) { onlyEntriesWithTextMatches = false; }
            var newSelectedElementIndex;
            var visibleEntriesAsList = this.findEntries(function (entry) {
                if (!entry._trEntryElement) {
                    return false;
                }
                else {
                    if (onlyEntriesWithTextMatches) {
                        return entry._trEntryElement.is(':visible') && entry._trEntryElement.has('>.tr-tree-entry-and-expander-wrapper .tr-highlighted-text').length > 0;
                    }
                    else {
                        return entry._trEntryElement.is(':visible') || entry === currentEntry;
                    }
                }
            });
            if (visibleEntriesAsList == null || visibleEntriesAsList.length == 0) {
                return null;
            }
            else if (currentEntry == null && direction > 0) {
                newSelectedElementIndex = -1 + direction;
            }
            else if (currentEntry == null && direction < 0) {
                newSelectedElementIndex = visibleEntriesAsList.length + direction;
            }
            else {
                var currentSelectedElementIndex = visibleEntriesAsList.indexOf(currentEntry);
                newSelectedElementIndex = (currentSelectedElementIndex + visibleEntriesAsList.length + direction) % visibleEntriesAsList.length;
            }
            return visibleEntriesAsList[newSelectedElementIndex];
        };
        TrivialTreeBox.prototype.highlightTextMatches = function (searchString) {
            this.$tree.detach();
            for (var i = 0; i < this.entries.length; i++) {
                var entry = this.entries[i];
                var $entryElement = entry._trEntryElement.find('.tr-tree-entry');
                $entryElement.trivialHighlight(searchString, this.config.matchingOptions);
            }
            this.$tree.appendTo(this.$componentWrapper);
        };
        TrivialTreeBox.prototype.getSelectedEntry = function () {
            return (this.selectedEntryId !== undefined && this.selectedEntryId !== null) ? this.findEntryById(this.selectedEntryId) : null;
        };
        TrivialTreeBox.prototype.revealSelectedEntry = function (animate) {
            if (animate === void 0) { animate = false; }
            var selectedEntry = this.getSelectedEntry();
            if (!selectedEntry) {
                return;
            }
            var currentEntry = selectedEntry;
            while (currentEntry = this.findParentNode(currentEntry)) {
                this.setNodeExpanded(currentEntry, true, animate);
            }
            this.minimallyScrollTo(selectedEntry._trEntryElement);
        };
        TrivialTreeBox.prototype.highlightNextEntry = function (direction) {
            var nextVisibleEntry = this.getNextVisibleEntry(this.highlightedEntry || this.getSelectedEntry(), direction);
            if (nextVisibleEntry != null) {
                this.setHighlightedEntry(nextVisibleEntry);
            }
        };
        TrivialTreeBox.prototype.highlightNextMatchingEntry = function (direction) {
            var nextMatchingEntry = this.getNextVisibleEntry(this.highlightedEntry || this.getSelectedEntry(), direction, true);
            if (nextMatchingEntry != null) {
                this.setHighlightedEntry(nextMatchingEntry);
            }
        };
        TrivialTreeBox.prototype.selectNextMatchingEntry = function (direction) {
            var nextMatchingEntry = this.getNextVisibleEntry(this.highlightedEntry, direction, true);
            if (nextMatchingEntry != null) {
                this.setSelectedEntry(nextMatchingEntry);
            }
        };
        TrivialTreeBox.prototype.getHighlightedEntry = function () {
            return this.highlightedEntry;
        };
        TrivialTreeBox.prototype.setHighlightedNodeExpanded = function (expanded) {
            if (!this.highlightedEntry || this.isLeaf(this.highlightedEntry)) {
                return false;
            }
            else {
                var wasExpanded = this.highlightedEntry[this.config.expandedProperty];
                this.setNodeExpanded(this.highlightedEntry, expanded, true);
                return !wasExpanded != !expanded;
            }
        };
        TrivialTreeBox.prototype.updateChildren = function (parentNodeId, children) {
            var node = this.findEntryById(parentNodeId);
            if (node) {
                this.setChildren(node, children);
            }
            else {
                void 0;
            }
        };
        ;
        TrivialTreeBox.prototype.updateNode = function (node) {
            var oldNode = this.findEntryById(this.config.valueFunction(node));
            var parent = this.findParentNode(oldNode);
            if (parent) {
                parent[this.config.childrenProperty][parent[this.config.childrenProperty].indexOf(oldNode)] = node;
            }
            else {
                this.entries[this.entries.indexOf(oldNode)] = node;
            }
            this.createEntryElement(node, this.nodeDepth(oldNode)).insertAfter(oldNode._trEntryElement);
            oldNode._trEntryElement.remove();
        };
        ;
        TrivialTreeBox.prototype.removeNode = function (nodeId) {
            var childNode = this.findEntryById(nodeId);
            if (childNode) {
                var parentNode = this.findParentNode(childNode);
                if (parentNode) {
                    parentNode[this.config.childrenProperty].splice(parentNode[this.config.childrenProperty].indexOf(childNode), 1);
                }
                else {
                    this.entries.splice(this.entries.indexOf(childNode), 1);
                }
                childNode._trEntryElement.remove();
            }
        };
        ;
        TrivialTreeBox.prototype.addNode = function (parentNodeId, node) {
            var parentNode = this.findEntryById(parentNodeId);
            if (this.isLeaf(parentNode)) {
                void 0;
            }
            if (!parentNode[this.config.childrenProperty]) {
                parentNode[this.config.childrenProperty] = [];
            }
            parentNode[this.config.childrenProperty].push(node);
            var entryElement = this.createEntryElement(node, this.nodeDepth(parentNode) + 1);
            entryElement
                .appendTo(parentNode._trEntryElement.find('>.tr-tree-entry-children-wrapper'));
            parentNode._trEntryElement.addClass('has-children');
        };
        ;
        TrivialTreeBox.prototype.getMainDomElement = function () {
            return this.$componentWrapper[0];
        };
        return TrivialTreeBox;
    }());
    TrivialComponents.TrivialTreeBox = TrivialTreeBox;
})(TrivialComponents || (TrivialComponents = {}));

var TrivialComponents;
(function (TrivialComponents) {
    var TrivialTreeComboBox = (function () {
        function TrivialTreeComboBox(originalInput, options) {
            if (options === void 0) { options = {}; }
            var _this = this;
            this.isDropDownOpen = false;
            this.isEditorVisible = false;
            this.lastQueryString = null;
            this.lastCompleteInputQueryString = null;
            this.selectedEntry = null;
            this.lastCommittedValue = null;
            this.blurCausedByClickInsideComponent = false;
            this.autoCompleteTimeoutId = -1;
            this.doNoAutoCompleteBecauseBackspaceWasPressed = false;
            this.usingDefaultQueryFunction = false;
            this.$spinners = $();
            this.onSelectedEntryChanged = new TrivialComponents.TrivialEvent(this);
            this.onFocus = new TrivialComponents.TrivialEvent(this);
            this.onBlur = new TrivialComponents.TrivialEvent(this);
            this.config = $.extend({
                valueFunction: function (entry) { return entry ? entry.id : null; },
                entryRenderingFunction: function (entry, depth) {
                    var defaultTemplates = [TrivialComponents.DEFAULT_TEMPLATES.icon2LinesTemplate, TrivialComponents.DEFAULT_TEMPLATES.iconSingleLineTemplate];
                    var template = entry.template || defaultTemplates[Math.min(depth, defaultTemplates.length - 1)];
                    return Mustache.render(template, entry);
                },
                selectedEntryRenderingFunction: function (entry) {
                    if (entry.selectedEntryTemplate) {
                        return Mustache.render(entry.selectedEntryTemplate, entry);
                    }
                    else {
                        return _this.config.entryRenderingFunction(entry, 0);
                    }
                },
                selectedEntry: null,
                spinnerTemplate: TrivialComponents.DEFAULT_TEMPLATES.defaultSpinnerTemplate,
                noEntriesTemplate: TrivialComponents.DEFAULT_TEMPLATES.defaultNoEntriesTemplate,
                textHighlightingEntryLimit: 100,
                entries: null,
                emptyEntry: {
                    _isEmptyEntry: true
                },
                queryFunction: null,
                autoComplete: true,
                autoCompleteDelay: 0,
                entryToEditorTextFunction: function (entry) {
                    return entry["displayValue"];
                },
                autoCompleteFunction: function (editorText, entry) {
                    if (editorText) {
                        for (var propertyName in entry) {
                            var propertyValue = entry[propertyName];
                            if (propertyValue && propertyValue.toString().toLowerCase().indexOf(editorText.toLowerCase()) === 0) {
                                return propertyValue.toString();
                            }
                        }
                        return null;
                    }
                    else {
                        return _this.config.entryToEditorTextFunction(entry);
                    }
                },
                allowFreeText: false,
                freeTextEntryFactory: function (freeText) {
                    return {
                        displayValue: freeText,
                        _isFreeTextEntry: true
                    };
                },
                showClearButton: false,
                showTrigger: true,
                matchingOptions: {
                    matchingMode: 'contains',
                    ignoreCase: true,
                    maxLevenshteinDistance: 2
                },
                childrenProperty: "children",
                lazyChildrenFlagProperty: "hasLazyChildren",
                expandedProperty: 'expanded',
                editingMode: "editable",
                showDropDownOnResultsOnly: false
            }, options);
            if (!this.config.queryFunction) {
                this.config.queryFunction = TrivialComponents.defaultTreeQueryFunctionFactory(this.config.entries || [], TrivialComponents.defaultEntryMatchingFunctionFactory(["displayValue", "additionalInfo"], this.config.matchingOptions), this.config.childrenProperty, this.config.expandedProperty);
                this.usingDefaultQueryFunction = true;
            }
            this.$originalInput = $(originalInput);
            this.$treeComboBox = $('<div class="tr-treecombobox tr-combobox tr-input-wrapper"/>')
                .insertAfter(this.$originalInput);
            this.$selectedEntryWrapper = $('<div class="tr-combobox-selected-entry-wrapper"/>').appendTo(this.$treeComboBox);
            if (this.config.showClearButton) {
                this.$clearButton = $('<div class="tr-remove-button">').appendTo(this.$treeComboBox);
                this.$clearButton.mousedown(function () {
                    _this.$editor.val("");
                    _this.setSelectedEntry(null, true, true);
                });
            }
            if (this.config.showTrigger) {
                this.$trigger = $('<div class="tr-trigger"><span class="tr-trigger-icon"/></div>').appendTo(this.$treeComboBox);
                this.$trigger.mousedown(function () {
                    if (_this.isDropDownOpen) {
                        _this.showEditor();
                        _this.closeDropDown();
                    }
                    else {
                        setTimeout(function () {
                            _this.showEditor();
                            _this.$editor.select();
                            _this.openDropDown();
                            _this.query();
                        });
                    }
                });
            }
            this.$dropDown = $('<div class="tr-dropdown"></div>')
                .scroll(function (e) {
                return false;
            });
            this.$dropDownTargetElement = $("body");
            this.setEditingMode(this.config.editingMode);
            this.$originalInput.addClass("tr-original-input");
            this.$editor = $('<input type="text" autocomplete="off"/>');
            this.$editor.prependTo(this.$treeComboBox).addClass("tr-combobox-editor tr-editor")
                .focus(function () {
                if (_this.blurCausedByClickInsideComponent) {
                }
                else {
                    _this.$originalInput.triggerHandler('focus');
                    _this.onFocus.fire();
                    _this.$treeComboBox.addClass('focus');
                    _this.showEditor();
                }
            })
                .blur(function () {
                if (_this.blurCausedByClickInsideComponent) {
                    _this.$editor.focus();
                }
                else {
                    _this.$originalInput.triggerHandler('blur');
                    _this.onBlur.fire();
                    _this.$treeComboBox.removeClass('focus');
                    if (_this.editorContainsFreeText()) {
                        if (!TrivialComponents.objectEquals(_this.getSelectedEntry(), _this.lastCommittedValue)) {
                            _this.setSelectedEntry(_this.getSelectedEntry(), true, true);
                        }
                    }
                    else {
                        _this.$editor.val("");
                        _this.setSelectedEntry(_this.lastCommittedValue);
                    }
                    _this.hideEditor();
                    _this.closeDropDown();
                }
            })
                .keydown(function (e) {
                if (TrivialComponents.keyCodes.isModifierKey(e)) {
                    return;
                }
                else if (e.which == TrivialComponents.keyCodes.tab) {
                    var highlightedEntry = _this.treeBox.getHighlightedEntry();
                    if (_this.isDropDownOpen && highlightedEntry) {
                        _this.setSelectedEntry(highlightedEntry, true, true);
                    }
                    else if (!_this.$editor.val()) {
                        _this.setSelectedEntry(null, true, true);
                    }
                    else if (_this.config.allowFreeText) {
                        _this.setSelectedEntry(_this.getSelectedEntry(), true, true);
                    }
                    return;
                }
                else if (e.which == TrivialComponents.keyCodes.left_arrow || e.which == TrivialComponents.keyCodes.right_arrow) {
                    if (_this.isDropDownOpen) {
                        var changedExpandedState = _this.treeBox.setHighlightedNodeExpanded(e.which == TrivialComponents.keyCodes.right_arrow);
                        if (changedExpandedState) {
                            return false;
                        }
                    }
                    _this.showEditor();
                    return;
                }
                if (e.which == TrivialComponents.keyCodes.backspace || e.which == TrivialComponents.keyCodes.delete) {
                    _this.doNoAutoCompleteBecauseBackspaceWasPressed = true;
                }
                if (e.which == TrivialComponents.keyCodes.up_arrow || e.which == TrivialComponents.keyCodes.down_arrow) {
                    if (!_this.isEditorVisible) {
                        _this.$editor.select();
                        _this.showEditor();
                    }
                    var direction = e.which == TrivialComponents.keyCodes.up_arrow ? -1 : 1;
                    if (!_this.isDropDownOpen) {
                        _this.query(direction);
                        if (!_this.config.showDropDownOnResultsOnly) {
                            _this.openDropDown();
                        }
                    }
                    else {
                        _this.treeBox.highlightNextEntry(direction);
                        _this.autoCompleteIfPossible(_this.config.autoCompleteDelay);
                    }
                    return false;
                }
                else if (e.which == TrivialComponents.keyCodes.enter) {
                    if (_this.isEditorVisible || _this.editorContainsFreeText()) {
                        e.preventDefault();
                        var highlightedEntry = _this.treeBox.getHighlightedEntry();
                        if (_this.isDropDownOpen && highlightedEntry) {
                            _this.setSelectedEntry(highlightedEntry, true, true);
                        }
                        else if (!_this.$editor.val()) {
                            _this.setSelectedEntry(null, true, true);
                        }
                        else if (_this.config.allowFreeText) {
                            _this.setSelectedEntry(_this.getSelectedEntry(), true, true);
                        }
                        _this.closeDropDown();
                        _this.hideEditor();
                    }
                }
                else if (e.which == TrivialComponents.keyCodes.escape) {
                    e.preventDefault();
                    if (!(_this.editorContainsFreeText() && _this.isDropDownOpen)) {
                        _this.hideEditor();
                        _this.$editor.val("");
                        _this.setSelectedEntry(_this.lastCommittedValue, false);
                    }
                    _this.closeDropDown();
                }
                else {
                    if (!_this.isEditorVisible) {
                        _this.showEditor();
                        _this.$editor.select();
                    }
                    if (!_this.config.showDropDownOnResultsOnly) {
                        _this.openDropDown();
                    }
                    setTimeout(function () {
                        if (_this.$editor.val()) {
                            _this.query(1);
                        }
                        else {
                            _this.query(0);
                            _this.treeBox.setHighlightedEntry(null);
                        }
                    });
                }
            })
                .keyup(function (e) {
                if (!TrivialComponents.keyCodes.isModifierKey(e) && [TrivialComponents.keyCodes.enter, TrivialComponents.keyCodes.escape, TrivialComponents.keyCodes.tab].indexOf(e.which) === -1 && _this.isEntrySelected() && _this.$editor.val() !== _this.config.entryToEditorTextFunction(_this.selectedEntry)) {
                    _this.setSelectedEntry(null, false);
                }
            })
                .mousedown(function () {
                if (!_this.config.showDropDownOnResultsOnly) {
                    _this.openDropDown();
                }
                _this.query();
            });
            if (this.$originalInput.attr("tabindex")) {
                this.$editor.attr("tabindex", this.$originalInput.attr("tabindex"));
            }
            if (this.$originalInput.attr("autofocus")) {
                this.$editor.focus();
            }
            this.$treeComboBox.add(this.$dropDown)
                .mousedown(function () {
                if (_this.$editor.is(":focus")) {
                    _this.blurCausedByClickInsideComponent = true;
                }
            })
                .mouseup(function () {
                if (_this.blurCausedByClickInsideComponent) {
                    _this.$editor.focus();
                    _this.blurCausedByClickInsideComponent = false;
                }
            })
                .mouseout(function () {
                if (_this.blurCausedByClickInsideComponent) {
                    _this.$editor.focus();
                    _this.blurCausedByClickInsideComponent = false;
                }
            });
            this.treeBox = new TrivialComponents.TrivialTreeBox(this.$dropDown, this.config);
            this.treeBox.onSelectedEntryChanged.addListener(function (selectedEntry) {
                if (selectedEntry) {
                    _this.setSelectedEntry(selectedEntry, true, !TrivialComponents.objectEquals(selectedEntry, _this.lastCommittedValue));
                    _this.treeBox.setSelectedEntry(null);
                    _this.closeDropDown();
                }
                _this.hideEditor();
            });
            this.setSelectedEntry(this.config.selectedEntry, true, true);
            this.$selectedEntryWrapper.click(function () {
                _this.showEditor();
                _this.$editor.select();
                if (!_this.config.showDropDownOnResultsOnly) {
                    _this.openDropDown();
                }
                _this.query();
            });
        }
        TrivialTreeComboBox.prototype.query = function (highlightDirection) {
            var _this = this;
            setTimeout(function () {
                var queryString = _this.getNonSelectedEditorValue();
                var completeInputString = _this.$editor.val();
                if (_this.lastQueryString !== queryString || _this.lastCompleteInputQueryString !== completeInputString) {
                    if (_this.$spinners.length === 0) {
                        var $spinner = $(_this.config.spinnerTemplate).appendTo(_this.$dropDown);
                        _this.$spinners = _this.$spinners.add($spinner);
                    }
                    _this.config.queryFunction(queryString, function (newEntries) {
                        _this.updateEntries(newEntries, highlightDirection);
                        if (_this.config.showDropDownOnResultsOnly && newEntries && newEntries.length > 0 && _this.$editor.is(":focus")) {
                            _this.openDropDown();
                        }
                    });
                    _this.lastQueryString = queryString;
                    _this.lastCompleteInputQueryString = completeInputString;
                }
                else {
                    _this.openDropDown();
                }
            }, 0);
        };
        TrivialTreeComboBox.prototype.fireChangeEvents = function (entry) {
            this.$originalInput.trigger("change");
            this.onSelectedEntryChanged.fire(entry);
        };
        TrivialTreeComboBox.prototype.setSelectedEntry = function (entry, commit, fireEvent) {
            if (fireEvent === void 0) { fireEvent = false; }
            if (entry == null) {
                this.$originalInput.val(this.config.valueFunction(null));
                this.selectedEntry = null;
                var $selectedEntry = $(this.config.selectedEntryRenderingFunction(this.config.emptyEntry))
                    .addClass("tr-combobox-entry")
                    .addClass("empty");
                this.$selectedEntryWrapper.empty().append($selectedEntry);
            }
            else {
                this.$originalInput.val(this.config.valueFunction(entry));
                this.selectedEntry = entry;
                var $selectedEntry = $(this.config.selectedEntryRenderingFunction(entry))
                    .addClass("tr-combobox-entry");
                this.$selectedEntryWrapper.empty().append($selectedEntry);
                this.$editor.val(this.config.entryToEditorTextFunction(entry));
            }
            if (commit) {
                this.lastCommittedValue = entry;
                if (fireEvent) {
                    this.fireChangeEvents(entry);
                }
            }
            if (this.$clearButton) {
                this.$clearButton.toggle(entry != null);
            }
            if (this.isEditorVisible) {
                this.showEditor();
            }
            if (this.isDropDownOpen) {
                this.repositionDropDown();
            }
        };
        TrivialTreeComboBox.prototype.isEntrySelected = function () {
            return this.selectedEntry != null && this.selectedEntry !== this.config.emptyEntry;
        };
        TrivialTreeComboBox.prototype.showEditor = function () {
            var $editorArea = this.$selectedEntryWrapper.find(".tr-editor-area");
            if ($editorArea.length === 0) {
                $editorArea = this.$selectedEntryWrapper;
            }
            this.$editor
                .css({
                "width": Math.min($editorArea[0].offsetWidth, this.$trigger ? this.$trigger[0].offsetLeft - $editorArea[0].offsetLeft : 99999999) + "px",
                "height": ($editorArea[0].offsetHeight) + "px"
            })
                .position({
                my: "left top",
                at: "left top",
                of: $editorArea
            });
            this.isEditorVisible = true;
        };
        TrivialTreeComboBox.prototype.editorContainsFreeText = function () {
            return this.config.allowFreeText && this.$editor.val().length > 0 && !this.isEntrySelected();
        };
        ;
        TrivialTreeComboBox.prototype.hideEditor = function () {
            this.$editor.width(0).height(0);
            this.isEditorVisible = false;
        };
        TrivialTreeComboBox.prototype.repositionDropDown = function () {
            var _this = this;
            this.$dropDown
                .show()
                .position({
                my: "left top",
                at: "left bottom",
                of: this.$treeComboBox,
                collision: "flip",
                using: function (calculatedPosition, info) {
                    if (info.vertical === "top") {
                        _this.$treeComboBox.removeClass("dropdown-flipped");
                        _this.$dropDown.removeClass("flipped");
                    }
                    else {
                        _this.$treeComboBox.addClass("dropdown-flipped");
                        _this.$dropDown.addClass("flipped");
                    }
                    _this.$dropDown.css({
                        left: calculatedPosition.left + 'px',
                        top: calculatedPosition.top + 'px'
                    });
                }
            })
                .width(this.$treeComboBox.width());
        };
        ;
        TrivialTreeComboBox.prototype.openDropDown = function () {
            if (this.isDropDownNeeded()) {
                this.$treeComboBox.addClass("open");
                this.repositionDropDown();
                this.isDropDownOpen = true;
            }
        };
        TrivialTreeComboBox.prototype.closeDropDown = function () {
            this.$treeComboBox.removeClass("open");
            this.$dropDown.hide();
            this.isDropDownOpen = false;
        };
        TrivialTreeComboBox.prototype.getNonSelectedEditorValue = function () {
            return this.$editor.val().substring(0, this.$editor[0].selectionStart);
        };
        TrivialTreeComboBox.prototype.autoCompleteIfPossible = function (delay) {
            var _this = this;
            if (this.config.autoComplete) {
                clearTimeout(this.autoCompleteTimeoutId);
                var highlightedEntry_1 = this.treeBox.getHighlightedEntry();
                if (highlightedEntry_1 && !this.doNoAutoCompleteBecauseBackspaceWasPressed) {
                    this.autoCompleteTimeoutId = setTimeout(function () {
                        var currentEditorValue = _this.getNonSelectedEditorValue();
                        var autoCompleteString = _this.config.autoCompleteFunction(currentEditorValue, highlightedEntry_1) || currentEditorValue;
                        _this.$editor.val(currentEditorValue + autoCompleteString.substr(currentEditorValue.length));
                        if (_this.$editor.is(":focus")) {
                            _this.$editor[0].setSelectionRange(currentEditorValue.length, autoCompleteString.length);
                        }
                    }, delay || 0);
                }
                this.doNoAutoCompleteBecauseBackspaceWasPressed = false;
            }
        };
        TrivialTreeComboBox.prototype.updateEntries = function (newEntries, highlightDirection) {
            this.$spinners.remove();
            this.$spinners = $();
            this.treeBox.updateEntries(newEntries);
            var nonSelectedEditorValue = this.getNonSelectedEditorValue();
            this.treeBox.highlightTextMatches(newEntries.length <= this.config.textHighlightingEntryLimit ? nonSelectedEditorValue : null);
            if (highlightDirection == null) {
                if (this.selectedEntry) {
                    this.treeBox.setHighlightedEntry(null);
                }
                else {
                    if (nonSelectedEditorValue.length > 0) {
                        this.treeBox.highlightNextMatchingEntry(1);
                    }
                    else {
                        this.treeBox.highlightNextEntry(1);
                    }
                }
            }
            else if (highlightDirection === 0) {
                this.treeBox.setHighlightedEntry(null);
            }
            else {
                if (nonSelectedEditorValue.length > 0) {
                    this.treeBox.highlightNextMatchingEntry(1);
                }
                else {
                    this.treeBox.highlightNextEntry(1);
                }
            }
            this.autoCompleteIfPossible(this.config.autoCompleteDelay);
            if (this.isDropDownOpen) {
                this.openDropDown();
            }
        };
        TrivialTreeComboBox.prototype.isDropDownNeeded = function () {
            return this.editingMode == 'editable' && (this.config.entries && this.config.entries.length > 0 || !this.usingDefaultQueryFunction || this.config.showTrigger);
        };
        TrivialTreeComboBox.prototype.setEditingMode = function (newEditingMode) {
            this.editingMode = newEditingMode;
            this.$treeComboBox.removeClass("editable readonly disabled").addClass(this.editingMode);
            if (this.isDropDownNeeded()) {
                this.$dropDown.appendTo(this.$dropDownTargetElement);
            }
        };
        TrivialTreeComboBox.prototype.getSelectedEntry = function () {
            if (this.selectedEntry == null && (!this.config.allowFreeText || !this.$editor.val())) {
                return null;
            }
            else if (this.selectedEntry == null && this.config.allowFreeText) {
                return this.config.freeTextEntryFactory(this.$editor.val());
            }
            else {
                var selectedEntryToReturn = jQuery.extend({}, this.selectedEntry);
                selectedEntryToReturn._trEntryElement = undefined;
                return selectedEntryToReturn;
            }
        };
        TrivialTreeComboBox.prototype.updateChildren = function (parentNodeId, children) {
            this.treeBox.updateChildren(parentNodeId, children);
        };
        TrivialTreeComboBox.prototype.updateNode = function (node) {
            this.treeBox.updateNode(node);
        };
        TrivialTreeComboBox.prototype.removeNode = function (nodeId) {
            this.treeBox.removeNode(nodeId);
        };
        TrivialTreeComboBox.prototype.focus = function () {
            this.showEditor();
            this.$editor.select();
        };
        ;
        TrivialTreeComboBox.prototype.getDropDown = function () {
            return this.$dropDown;
        };
        ;
        TrivialTreeComboBox.prototype.destroy = function () {
            this.$originalInput.removeClass('tr-original-input').insertBefore(this.$treeComboBox);
            this.$treeComboBox.remove();
            this.$dropDown.remove();
        };
        ;
        TrivialTreeComboBox.prototype.getMainDomElement = function () {
            return this.$treeComboBox[0];
        };
        return TrivialTreeComboBox;
    }());
    TrivialComponents.TrivialTreeComboBox = TrivialTreeComboBox;
})(TrivialComponents || (TrivialComponents = {}));

var TrivialComponents;
(function (TrivialComponents) {
    var TrivialUnitBox = (function () {
        function TrivialUnitBox(originalInput, options) {
            if (options === void 0) { options = {}; }
            var _this = this;
            this.onChange = new TrivialComponents.TrivialEvent(this);
            this.onSelectedEntryChanged = new TrivialComponents.TrivialEvent(this);
            this.isDropDownOpen = false;
            this.blurCausedByClickInsideComponent = false;
            this.$spinners = $();
            this.config = $.extend({
                unitValueProperty: 'code',
                unitIdProperty: 'code',
                decimalPrecision: 2,
                decimalSeparator: '.',
                thousandsSeparator: ',',
                unitDisplayPosition: 'right',
                allowNullAmount: true,
                entryRenderingFunction: function (entry) {
                    var template = entry.template || TrivialComponents.DEFAULT_TEMPLATES.currency2LineTemplate;
                    return Mustache.render(template, entry);
                },
                selectedEntryRenderingFunction: function (entry) {
                    var template = entry.selectedEntryTemplate || TrivialComponents.DEFAULT_TEMPLATES.currencySingleLineShortTemplate;
                    return Mustache.render(template, entry);
                },
                amount: null,
                selectedEntry: undefined,
                spinnerTemplate: TrivialComponents.DEFAULT_TEMPLATES.defaultSpinnerTemplate,
                noEntriesTemplate: TrivialComponents.DEFAULT_TEMPLATES.defaultNoEntriesTemplate,
                entries: null,
                emptyEntry: {
                    code: '...'
                },
                queryFunction: null,
                queryOnNonNumberCharacters: true,
                openDropdownOnEditorClick: false,
                showTrigger: true,
                matchingOptions: {
                    matchingMode: 'prefix-word',
                    ignoreCase: true,
                    maxLevenshteinDistance: 2
                },
                editingMode: 'editable',
            }, options);
            if (!this.config.queryFunction) {
                this.config.queryFunction = TrivialComponents.defaultListQueryFunctionFactory(this.config.entries || [], this.config.matchingOptions);
                this.usingDefaultQueryFunction = true;
            }
            this.entries = this.config.entries;
            this.numberRegex = new RegExp('\\d*\\' + this.config.decimalSeparator + '?\\d*', 'g');
            this.$originalInput = $(originalInput).addClass("tr-original-input");
            this.$editor = $('<input type="text"/>');
            this.$unitBox = $('<div class="tr-unitbox tr-input-wrapper"/>').insertAfter(this.$originalInput)
                .addClass(this.config.unitDisplayPosition === 'left' ? 'unit-display-left' : 'unit-display-right');
            this.$originalInput.appendTo(this.$unitBox);
            this.$selectedEntryAndTriggerWrapper = $('<div class="tr-unitbox-selected-entry-and-trigger-wrapper"/>').appendTo(this.$unitBox);
            this.$selectedEntryWrapper = $('<div class="tr-unitbox-selected-entry-wrapper"/>').appendTo(this.$selectedEntryAndTriggerWrapper);
            if (this.config.showTrigger) {
                $('<div class="tr-trigger"><span class="tr-trigger-icon"/></div>').appendTo(this.$selectedEntryAndTriggerWrapper);
            }
            this.$selectedEntryAndTriggerWrapper.mousedown(function () {
                if (_this.isDropDownOpen) {
                    _this.closeDropDown();
                }
                else if (_this.editingMode === "editable") {
                    setTimeout(function () {
                        _this.openDropDown();
                        _this.query();
                    });
                }
                _this.$editor.focus();
            });
            this.$dropDown = $('<div class="tr-dropdown"></div>')
                .scroll(function () {
                return false;
            });
            this.$dropDownTargetElement = $("body");
            this.setEditingMode(this.config.editingMode);
            this.$editor.prependTo(this.$unitBox).addClass("tr-unitbox-editor tr-editor")
                .focus(function () {
                if (_this.editingMode !== "editable") {
                    _this.$editor.blur();
                    return false;
                }
                if (_this.blurCausedByClickInsideComponent) {
                }
                else {
                    _this.$unitBox.addClass('focus');
                    _this.cleanupEditorValue();
                }
            })
                .blur(function () {
                if (_this.blurCausedByClickInsideComponent) {
                    _this.$editor.focus();
                }
                else {
                    _this.$unitBox.removeClass('focus');
                    _this.formatEditorValue();
                    _this.closeDropDown();
                }
            })
                .keydown(function (e) {
                if (TrivialComponents.keyCodes.isModifierKey(e)) {
                    return;
                }
                else if (e.which == TrivialComponents.keyCodes.tab) {
                    var highlightedEntry = _this.listBox.getHighlightedEntry();
                    if (_this.isDropDownOpen && highlightedEntry) {
                        _this.setSelectedEntry(highlightedEntry, true);
                    }
                }
                else if (e.which == TrivialComponents.keyCodes.left_arrow || e.which == TrivialComponents.keyCodes.right_arrow) {
                    return;
                }
                if (e.which == TrivialComponents.keyCodes.up_arrow || e.which == TrivialComponents.keyCodes.down_arrow) {
                    var direction = e.which == TrivialComponents.keyCodes.up_arrow ? -1 : 1;
                    if (_this.isDropDownOpen) {
                        _this.listBox.highlightNextEntry(direction);
                    }
                    else {
                        _this.openDropDown();
                        _this.query(direction);
                    }
                    return false;
                }
                else if (_this.isDropDownOpen && e.which == TrivialComponents.keyCodes.enter) {
                    e.preventDefault();
                    _this.setSelectedEntry(_this.listBox.getHighlightedEntry(), true);
                    _this.closeDropDown();
                }
                else if (e.which == TrivialComponents.keyCodes.escape) {
                    _this.closeDropDown();
                    _this.cleanupEditorValue();
                }
                else if (!e.shiftKey && TrivialComponents.keyCodes.numberKeys.indexOf(e.which) != -1) {
                    var numberPart = _this.getEditorValueNumberPart();
                    var numberPartDecimalSeparatorIndex = numberPart.indexOf(_this.config.decimalSeparator);
                    var maxDecimalDigitsReached = numberPartDecimalSeparatorIndex != -1 && numberPart.length - (numberPartDecimalSeparatorIndex + 1) >= _this.config.decimalPrecision;
                    var editorValue = _this.$editor.val();
                    var decimalSeparatorIndex = editorValue.indexOf(_this.config.decimalSeparator);
                    var selectionStart = _this.$editor[0].selectionStart;
                    var selectionEnd = _this.$editor[0].selectionEnd;
                    var wouldAddAnotherDigit = decimalSeparatorIndex !== -1 && selectionEnd > decimalSeparatorIndex && selectionStart === selectionEnd;
                    if (maxDecimalDigitsReached && wouldAddAnotherDigit) {
                        if (/^\d$/.test(editorValue[selectionEnd])) {
                            _this.$editor.val(editorValue.substring(0, selectionEnd) + editorValue.substring(selectionEnd + 1));
                            _this.$editor[0].setSelectionRange(selectionEnd, selectionEnd);
                        }
                        else {
                            return false;
                        }
                    }
                }
            })
                .keyup(function (e) {
                if (TrivialComponents.keyCodes.specialKeys.indexOf(e.which) != -1
                    && e.which != TrivialComponents.keyCodes.backspace
                    && e.which != TrivialComponents.keyCodes.delete) {
                    return;
                }
                var hasDoubleDecimalSeparator = new RegExp("(?:\\" + _this.config.decimalSeparator + ".*)" + "\\" + _this.config.decimalSeparator, "g").test(_this.$editor.val());
                if (hasDoubleDecimalSeparator) {
                    _this.cleanupEditorValue();
                    _this.$editor[0].setSelectionRange(_this.$editor.val().length - _this.config.decimalPrecision, _this.$editor.val().length - _this.config.decimalPrecision);
                }
                if (_this.config.queryOnNonNumberCharacters) {
                    if (_this.getQueryString().length > 0) {
                        _this.openDropDown();
                        _this.query(1);
                    }
                    else {
                        _this.closeDropDown();
                    }
                }
            })
                .mousedown(function () {
                if (_this.config.openDropdownOnEditorClick) {
                    _this.openDropDown();
                    if (_this.entries == null) {
                        _this.query();
                    }
                }
            }).change(function () {
                _this.updateOriginalInputValue();
                _this.fireChangeEvents();
            });
            this.$unitBox.add(this.$dropDown).mousedown(function () {
                if (_this.$editor.is(":focus")) {
                    _this.blurCausedByClickInsideComponent = true;
                }
            }).mouseup(function () {
                if (_this.blurCausedByClickInsideComponent) {
                    _this.$editor.focus();
                    _this.blurCausedByClickInsideComponent = false;
                }
            }).mouseout(function () {
                if (_this.blurCausedByClickInsideComponent) {
                    _this.$editor.focus();
                    _this.blurCausedByClickInsideComponent = false;
                }
            });
            this.listBox = new TrivialComponents.TrivialListBox(this.$dropDown, this.config);
            this.listBox.onSelectedEntryChanged.addListener(function (selectedEntry) {
                if (selectedEntry) {
                    _this.setSelectedEntry(selectedEntry, true);
                    _this.listBox.setSelectedEntry(null);
                    _this.closeDropDown();
                }
            });
            this.$editor.val(this.config.amount || this.$originalInput.val());
            this.formatEditorValue();
            this.setSelectedEntry(this.config.selectedEntry || null, false);
        }
        TrivialUnitBox.prototype.getQueryString = function () {
            return this.$editor.val().replace(this.numberRegex, '');
        };
        TrivialUnitBox.prototype.getEditorValueNumberPart = function (fillupDecimals) {
            var rawNumber = this.$editor.val().match(this.numberRegex).join('');
            var decimalDeparatorIndex = rawNumber.indexOf(this.config.decimalSeparator);
            var integerPart;
            var fractionalPart;
            if (decimalDeparatorIndex !== -1) {
                integerPart = rawNumber.substring(0, decimalDeparatorIndex);
                fractionalPart = rawNumber.substring(decimalDeparatorIndex + 1, rawNumber.length).replace(/\D/g, '');
            }
            else {
                integerPart = rawNumber;
                fractionalPart = "";
            }
            if (integerPart.length == 0 && fractionalPart.length == 0) {
                return "";
            }
            else {
                if (fillupDecimals) {
                    fractionalPart = (fractionalPart + new Array(this.config.decimalPrecision + 1).join("0")).substr(0, this.config.decimalPrecision);
                }
                return integerPart + this.config.decimalSeparator + fractionalPart;
            }
        };
        TrivialUnitBox.prototype.query = function (highlightDirection) {
            var _this = this;
            var $spinner = $(this.config.spinnerTemplate).appendTo(this.$dropDown);
            this.$spinners = this.$spinners.add($spinner);
            setTimeout(function () {
                _this.config.queryFunction(_this.getQueryString(), function (newEntries) {
                    _this.updateEntries(newEntries);
                    var queryString = _this.getQueryString();
                    if (queryString.length > 0) {
                        _this.listBox.highlightTextMatches(queryString);
                    }
                    _this.listBox.highlightNextEntry(highlightDirection);
                    if (_this.isDropDownOpen) {
                        _this.openDropDown();
                    }
                });
            });
        };
        TrivialUnitBox.prototype.fireSelectedEntryChangedEvent = function () {
            this.onSelectedEntryChanged.fire(this.selectedEntry);
        };
        TrivialUnitBox.prototype.fireChangeEvents = function () {
            this.$originalInput.trigger("change");
            this.onChange.fire({
                unit: this.selectedEntry != null ? this.selectedEntry[this.config.unitValueProperty] : null,
                unitEntry: this.selectedEntry,
                amount: this.getAmount(),
                amountAsFloatingPointNumber: parseFloat(this.formatAmount(this.getAmount(), this.config.decimalPrecision, this.config.decimalSeparator, this.config.thousandsSeparator))
            });
        };
        TrivialUnitBox.prototype.setSelectedEntry = function (entry, fireEvent) {
            if (entry == null) {
                this.selectedEntry = null;
                var $selectedEntry = $(this.config.selectedEntryRenderingFunction(this.config.emptyEntry))
                    .addClass("tr-combobox-entry")
                    .addClass("empty");
                this.$selectedEntryWrapper.empty().append($selectedEntry);
            }
            else {
                this.selectedEntry = entry;
                var $selectedEntry = $(this.config.selectedEntryRenderingFunction(entry))
                    .addClass("tr-combobox-entry");
                this.$selectedEntryWrapper.empty().append($selectedEntry);
            }
            this.cleanupEditorValue();
            this.updateOriginalInputValue();
            if (!this.$editor.is(":focus")) {
                this.formatEditorValue();
            }
            if (fireEvent) {
                this.fireSelectedEntryChangedEvent();
                this.fireChangeEvents();
            }
        };
        TrivialUnitBox.prototype.formatEditorValue = function () {
            this.$editor.val(this.formatAmount(this.getAmount(), this.config.decimalPrecision, this.config.decimalSeparator, this.config.thousandsSeparator));
        };
        TrivialUnitBox.prototype.cleanupEditorValue = function () {
            if (this.$editor.val()) {
                this.$editor.val(this.getEditorValueNumberPart(true));
            }
        };
        TrivialUnitBox.prototype.formatAmount = function (integerNumber, precision, decimalSeparator, thousandsSeparator) {
            if (integerNumber == null || isNaN(integerNumber)) {
                return "";
            }
            var amountAsString = "" + integerNumber;
            if (amountAsString.length <= precision) {
                return 0 + decimalSeparator + new Array(precision - amountAsString.length + 1).join("0") + amountAsString;
            }
            else {
                var integerPart = amountAsString.substring(0, amountAsString.length - precision);
                var formattedIntegerPart = integerPart.replace(/\B(?=(\d{3})+(?!\d))/g, thousandsSeparator);
                var fractionalPart = amountAsString.substr(amountAsString.length - precision, precision);
                return formattedIntegerPart + decimalSeparator + fractionalPart;
            }
        };
        TrivialUnitBox.prototype.repositionDropDown = function () {
            var _this = this;
            this.$dropDown
                .show()
                .position({
                my: "left top",
                at: "left bottom",
                of: this.$unitBox,
                collision: "flip",
                using: function (calculatedPosition, info) {
                    if (info.vertical === "top") {
                        _this.$unitBox.removeClass("dropdown-flipped");
                        _this.$dropDown.removeClass("flipped");
                    }
                    else {
                        _this.$unitBox.addClass("dropdown-flipped");
                        _this.$dropDown.addClass("flipped");
                    }
                    _this.$dropDown.css({
                        left: calculatedPosition.left + 'px',
                        top: calculatedPosition.top + 'px'
                    });
                }
            })
                .width(this.$unitBox.width());
        };
        ;
        TrivialUnitBox.prototype.openDropDown = function () {
            this.$unitBox.addClass("open");
            this.repositionDropDown();
            this.isDropDownOpen = true;
        };
        TrivialUnitBox.prototype.closeDropDown = function () {
            this.$unitBox.removeClass("open");
            this.$dropDown.hide();
            this.isDropDownOpen = false;
        };
        TrivialUnitBox.prototype.updateOriginalInputValue = function () {
            if (this.config.unitDisplayPosition === 'left') {
                this.$originalInput.val((this.selectedEntry ? this.selectedEntry[this.config.unitValueProperty] : '') + this.formatAmount(this.getAmount(), this.config.decimalPrecision, this.config.decimalSeparator, ''));
            }
            else {
                this.$originalInput.val(this.formatAmount(this.getAmount(), this.config.decimalPrecision, this.config.decimalSeparator, '') + (this.selectedEntry ? this.selectedEntry[this.config.unitValueProperty] : ''));
            }
        };
        TrivialUnitBox.prototype.getAmount = function () {
            var editorValueNumberPart = this.getEditorValueNumberPart(false);
            if (editorValueNumberPart.length === 0 && this.config.allowNullAmount) {
                return null;
            }
            else if (editorValueNumberPart.length === 0) {
                return 0;
            }
            else {
                return parseInt(this.getEditorValueNumberPart(true).replace(/\D/g, ""));
            }
        };
        TrivialUnitBox.prototype.isDropDownNeeded = function () {
            return this.editingMode == 'editable' && (this.config.entries && this.config.entries.length > 0 || !this.usingDefaultQueryFunction || this.config.showTrigger);
        };
        TrivialUnitBox.prototype.setEditingMode = function (newEditingMode) {
            this.editingMode = newEditingMode;
            this.$unitBox.removeClass("editable readonly disabled").addClass(this.editingMode);
            this.$editor.prop("readonly", newEditingMode !== "editable");
            this.$editor.attr("tabindex", newEditingMode === "editable" ? this.$originalInput.attr("tabindex") : "-1");
            if (this.isDropDownNeeded()) {
                this.$dropDown.appendTo(this.$dropDownTargetElement);
            }
        };
        TrivialUnitBox.prototype.selectUnit = function (unitIdentifier) {
            var _this = this;
            this.setSelectedEntry(this.entries.filter(function (entry) {
                return entry[_this.config.unitIdProperty] === unitIdentifier;
            })[0], false);
        };
        TrivialUnitBox.prototype.updateEntries = function (newEntries) {
            this.entries = newEntries;
            this.$spinners.remove();
            this.$spinners = $();
            this.listBox.updateEntries(newEntries);
        };
        TrivialUnitBox.prototype.getSelectedEntry = function () {
            if (this.selectedEntry == null) {
                return null;
            }
            else {
                var selectedEntryToReturn = jQuery.extend({}, this.selectedEntry);
                selectedEntryToReturn._trEntryElement = undefined;
                return selectedEntryToReturn;
            }
        };
        TrivialUnitBox.prototype.setAmount = function (amount) {
            if (amount != null && amount !== Math.floor(amount)) {
                throw "TrivialUnitBox: You must specify an integer amount!";
            }
            if (amount == null) {
                if (this.config.allowNullAmount) {
                    this.$editor.val("");
                }
                else {
                    this.$editor.val(this.formatAmount(0, this.config.decimalPrecision, this.config.decimalSeparator, ''));
                }
            }
            else if (this.$editor.is(":focus")) {
                this.$editor.val(this.formatAmount(amount, this.config.decimalPrecision, this.config.decimalSeparator, ''));
            }
            else {
                this.$editor.val(this.formatAmount(amount, this.config.decimalPrecision, this.config.decimalSeparator, this.config.thousandsSeparator));
            }
        };
        ;
        TrivialUnitBox.prototype.focus = function () {
            this.$editor.select();
        };
        ;
        TrivialUnitBox.prototype.destroy = function () {
            this.$originalInput.removeClass('tr-original-input').insertBefore(this.$unitBox);
            this.$unitBox.remove();
            this.$dropDown.remove();
        };
        ;
        TrivialUnitBox.prototype.getMainDomElement = function () {
            return this.$unitBox[0];
        };
        return TrivialUnitBox;
    }());
    TrivialComponents.TrivialUnitBox = TrivialUnitBox;
})(TrivialComponents || (TrivialComponents = {}));

var TrivialComponents;
(function (TrivialComponents) {
    (function ($) {
        $.expr[":"].containsIgnoreCase = $.expr.createPseudo(function (arg) {
            return function (elem) {
                return $(elem).text().toUpperCase().indexOf(arg.toUpperCase()) >= 0;
            };
        });
    })(jQuery);
    (function ($) {
        var isIE11 = !(window.ActiveXObject) && "ActiveXObject" in window;
        function normalizeForIE11(node) {
            if (!node) {
                return;
            }
            if (node.nodeType == 3) {
                while (node.nextSibling && node.nextSibling.nodeType == 3) {
                    node.nodeValue += node.nextSibling.nodeValue;
                    node.parentNode.removeChild(node.nextSibling);
                }
            }
            else {
                normalizeForIE11(node.firstChild);
            }
            normalizeForIE11(node.nextSibling);
        }
        $.fn.trivialHighlight = function (searchString, options) {
            options = $.extend({
                highlightClassName: 'tr-highlighted-text',
                matchingMode: 'contains',
                ignoreCase: true,
                maxLevenshteinDistance: 3
            }, options);
            return this.find('*').each(function () {
                var $this = $(this);
                $this.find('.' + options.highlightClassName).contents().unwrap();
                if (isIE11) {
                    normalizeForIE11(this);
                }
                else {
                    this.normalize();
                }
                if (searchString && searchString !== '') {
                    $this.contents().filter(function () {
                        return this.nodeType == 3 && TrivialComponents.trivialMatch(this.nodeValue, searchString, options).length > 0;
                    }).replaceWith(function () {
                        var oldNodeValue = (this.nodeValue || "");
                        var newNodeValue = "";
                        var matches = TrivialComponents.trivialMatch(this.nodeValue, searchString, options);
                        var oldMatchEnd = 0;
                        for (var i = 0; i < matches.length; i++) {
                            var match = matches[i];
                            newNodeValue += this.nodeValue.substring(oldMatchEnd, match.start);
                            newNodeValue += "<span class=\"" + options.highlightClassName + "\">" + oldNodeValue.substr(match.start, match.length) + "</span>";
                            oldMatchEnd = match.start + match.length;
                        }
                        newNodeValue += oldNodeValue.substring(oldMatchEnd, oldNodeValue.length);
                        return newNodeValue;
                    });
                }
            });
        };
    }(jQuery));
})(TrivialComponents || (TrivialComponents = {}));

$.fn.minimallyScrollTo = function (target) {
    return this.each(function () {
        var $this = $(this);
        var $target = $(target);
        var viewPortMinY = $this.scrollTop();
        var viewPortMaxY = viewPortMinY + $this.innerHeight();
        var targetMinY = $($target).offset().top - $(this).offset().top + $this.scrollTop();
        var targetMaxY = targetMinY + $target.height();
        if (targetMinY < viewPortMinY) {
            $this.scrollTop(targetMinY);
        }
        else if (targetMaxY > viewPortMaxY) {
            $this.scrollTop(Math.min(targetMinY, targetMaxY - $this.innerHeight()));
        }
        var viewPortMinX = $this.scrollLeft();
        var viewPortMaxX = viewPortMinX + $this.innerWidth();
        var targetMinX = $($target).offset().left - $(this).offset().left + $this.scrollLeft();
        var targetMaxX = targetMinX + $target.width();
        if (targetMinX < viewPortMinX) {
            $this.scrollLeft(targetMinX);
        }
        else if (targetMaxX > viewPortMaxX) {
            $this.scrollLeft(Math.min(targetMinX, targetMaxX - $this.innerWidth()));
        }
    });
};
