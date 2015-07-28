if (typeof butter === 'undefined') {
    butter = {};
}

/**
 * The object with all message properties for the different locales.
 *
 * ===============================
 * ENTER YOUR OWN MESSAGES HERE!
 * ===============================
 *
 * @private
 */
butter._msgProperties = {
    //this is the leading language local and is used as fallback!
    en: {
        "combobox.noResultsFound": "No entries found!"
    },

    de: {
        "combobox.noResultsFound": "Keine Einträge gefunden!"
    }
};

butter.message = {
    _fallbackLocale: "en",

    _locale: null,

    _detectBrowserLocale: function () {
        var detectedLocale = window.navigator.userLanguage || window.navigator.language;
        if (butter._msgProperties[detectedLocale] !== undefined) {
            //console.log("_detectBrowserLocale - found locale '%s' in message properties, setting it as locale", detectedLocale);
            butter.message._locale = detectedLocale;
        } else {
            // couldn't find locale in the exisiting message properties, setting to fallback locale
            //console.log("_detectBrowserLocale - couldn't find locale '%s 'in the exisiting message properties, setting to fallback locale", detectedLocale);
            butter.message._locale = butter.message._fallbackLocale;
        }
    },

    /**
     * Returns the localized message to the given key. If the key doesn't exist in the detected language it tries to
     * find the key in the fallback locale (en). Otherwise the key will be returned.
     *
     * @param {String} key the message key
     * @returns {String} the message to the key
     */
    get: function (key) {
        //try to get key from detected locale
        var message = butter._msgProperties[butter.message._locale][key];

        if (message === undefined) {
            //key not found in detected locale, try to get it from fallback locale
            message = butter._msgProperties[butter.message._fallbackLocale][key];
        }

        if (message === undefined) {
            //key not found in fallback locale returning key
            message = key;
        }

        return message;
    }
};

//run locale detection initally
butter.message._detectBrowserLocale();