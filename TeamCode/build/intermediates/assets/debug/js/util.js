/*
 * util.js
 */

//----------------------------------------------------------------------------------------------
// Variables
//----------------------------------------------------------------------------------------------

// defined in rc_config (import that script before this one)
var URI_ANON_PING;
var URI_PING;
var URI_LIST_LOG_FILES;
var URI_DOWNLOAD_FILE;
var URI_RENAME_RC;
var URI_UPLOAD_EXPANSION_HUB_FIRMWARE;
var URI_UPDATE_CONTROL_HUB_APK;
var PARAM_NAME;
var PARAM_NEW_NAME;
var URI_NAV_HOME;
var URI_NAV_MANAGE;
var URI_NAV_HELP;
var URI_RC_INFO;
var URI_REBOOT;

var URI_NAV_BLOCKS;
var URI_NAV_ONBOTJAVA;

//----------------------------------------------------------------------------------------------
// Class attribute management
//----------------------------------------------------------------------------------------------

var addClass = function(element, classToAdd) {
    element.className += (" " + classToAdd);
}

var hasClass = function(element, classToTest) {
    var expr = '(?:^|\\s)' + classToTest + '(?!\\S)';
    return element.className.match(new RegExp(expr, ''));
}

var ensureClass = function(element, classToAdd) {
    if (!hasClass(element, classToAdd)) {
        addClass(element, classToAdd);
    }
}

var removeClass = function(element, classToRemove) {
    // https://stackoverflow.com/questions/195951/change-an-elements-class-with-javascript
    var expr = '(?:^|\\s)' + classToRemove + '(?!\\S)';
    element.className = element.className.replace(new RegExp(expr, 'g'), '');
}

//----------------------------------------------------------------------------------------------
// UI
//----------------------------------------------------------------------------------------------

// Scales the font-size of an element over what it otherwise would be. Is idempotent.
var scaleFontSize = function(element, fraction) {
    // originalSize will always be in 'px'. I think.
    var originalSize = element.getAttribute("data-orig-font-size");
    if (!originalSize) originalSize = window.getComputedStyle(element, null).getPropertyValue("font-size");

    var value = parseFloat(originalSize);
    value = value * fraction;
    value = value + "px";

    // console.log("originalSize=" + originalSize + "new size=" + value);
    element.style.fontSize = value;
    element.setAttribute("data-orig-font-size", originalSize);
}

var scaleTextInDocumentBody = function (rcInfo) {
    // The scaling adaptation is experimental, can likely use polish. You might think we'd
    // scale the whole view etc, but that seems to complication button layout etc. As I said,
    // this is kind of experimental.

    var uaIsAndroid = rcInfo.ftcUserAgentCategory !== "OTHER";

    var width = window.screen.width;
    var height = window.screen.height;

    // These are the values observed given the scaling WebView does
    var widthZTESpeed = 320;            var heightZTESpeed = 570;       // actual: 540x960
    var widthAmazonFire7 = 600;         var heightAmazonFire7 = 1024;   // actual: 800x1280

    var minWidth = widthZTESpeed;       var minScale = 1.0;
    var maxWidth = widthAmazonFire7;    var maxScale = 1.2;

    var dw = maxWidth - minWidth;
    var ds = maxScale - minScale;

    var scale = 1.0;
    if (uaIsAndroid) {
        scale = minScale + (width - minWidth) / dw * ds;
        if (scale < minScale) scale = minScale;
        if (scale > maxScale) scale = maxScale;
    }

    console.log("width=" + width + " height=" + height + " scale=" + scale);
    scaleFontSize(document.getElementsByTagName("BODY")[0], scale);
}

var fetchRcInfoAndScaleText = function () {
    loadRcInfo(function (rcInfo) {
        if (rcInfo) {
            scaleTextInDocumentBody(rcInfo);
        }
    });
};

var hideElement = function(element, hide) {
    if (hide) {
        addClass(element, "hidden");
    } else {
        removeClass(element, "hidden");
    }
}

var showToast = function(id, message) {
    if (!isFtcRobotController()) {
        // fade the toast element in and out
        var toastElement = $('#' + id);
        toastElement.html(message);
        toastElement.fadeIn('slow');
        window.setTimeout(function() { toastElement.fadeOut('slow'); }, 2000);
    } else {
        // Show toast on the Android display
        var url = URI_TOAST + '?' + PARAM_MESSAGE + '=' + encodeURIComponent(message);
        simpleGet(url, null);
    }
}

//----------------------------------------------------------------------------------------------
// Script management
//----------------------------------------------------------------------------------------------

// synchronously loads an array of scripts by name, calling the continuation when all are complete
var syncLoadScripts = function (scripts, continuation) {
    var scriptToLoad = scripts.shift();
    var script = document.createElement('script');
    script.type = 'text/javascript';
    script.src = scriptToLoad;
    script.onload = function () {
        if (0 == scripts.length) {
            console.log("all sync scripts loaded");
            continuation();
        } else {
            syncLoadScripts(scripts, continuation);
        }
    };
    document.head.appendChild(script);
}

// asynchronously loads an array of scripts by name, calling the continuation when all are complete
var asyncLoadScripts = function (scripts, continuation) {
    var scriptsLoaded = 0;
    for (var i = 0; i < scripts.length; i++) {
        var scriptToLoad = scripts[i];
        var script = document.createElement('script');
        script.type = 'text/javascript';
        script.src = scriptToLoad;
        script.onload = function () {
            scriptsLoaded++;
            if (scriptsLoaded == scripts.length) {
                console.log("all async scripts loaded");
                continuation();
            }
        };
        document.head.appendChild(script);
    }
}

//----------------------------------------------------------------------------------------------
// File management
//----------------------------------------------------------------------------------------------

var simpleGet = function(uri, callback) {
    var xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4) {
            if (xhr.status === 200) {
                // console.log("uri=" + uri + " json=" + xhr.responseText);
                if(callback) callback(xhr.responseText);
            }
            else {
                if(callback) callback(null);
            }
        }
    };
    xhr.open("GET", uri);
    xhr.send();
}

var loadJsonObject = function(uri, callback) {
    simpleGet(uri, function (responseText) {
        if (responseText) {
            callback(JSON.parse(responseText));
        } else {
            callback(null);
        }
    });
};

// A single global that we initialize on the side
var ftcUserAgentCategory = null;

var loadRcInfo = function(callback) {
    loadJsonObject(URI_RC_INFO, function (rcInfo) {
        if (rcInfo) {
            ftcUserAgentCategory = rcInfo.ftcUserAgentCategory;
        }
        callback(rcInfo);
    });
};

// undoubtedly this could be sped up :-)
var makeAndroidSafeFileName = function(fileName) {
        return fileName.replace(/[^\w\d_\-+,@£\$€!½§~'=()[]{}]/g, '_');
};

// Uploads the file by posting to the indicted URL. progress is periodically
// called while this occurs
// https://en.wikipedia.org/wiki/XMLHttpRequest
// https://xhr.spec.whatwg.org/
// https://developer.mozilla.org/en-US/docs/Web/API/XMLHttpRequest/Using_XMLHttpRequest
// https://developer.mozilla.org/en-US/docs/Web/Events/progress
// https://developer.mozilla.org/en-US/docs/Web/API/File
var uploadFile = function (localFile /*a File*/, url, progress, success, failure) {
    var xhr = new XMLHttpRequest();

    if (progress) {
        // Note: on Chrome, the progress events do eventually fire, but only, it seems,
        // after the upload is complete. Not very useful. IE and other browsers are fine.
        var progressEvent = function (e) { progress(xhr, e); }
        xhr.upload.addEventListener("progress", progressEvent, false /*useCapture*/);
    }
    if (success) {
        var successEvent = function (e) { success(xhr, e); }
        xhr.upload.addEventListener("load", successEvent);
    }
    if (failure) {
        var failureEvent = function (e) { failure(xhr, e); }
        xhr.upload.addEventListener("error", failureEvent);
        xhr.upload.addEventListener("abort", failureEvent);
        xhr.upload.addEventListener("timeout", failureEvent);
    }

    xhr.open("POST", url);
    xhr.setRequestHeader('Content-type', 'application/octet-stream');
    xhr.overrideMimeType('application/octet-stream');
    xhr.setRequestHeader('Content-Disposition', 'attachment; filename=' + '"' + makeAndroidSafeFileName(localFile.name) + '"');  // TODO: do we need to URI-encode this?

    // On Chrome, give the wait cursor a chance to show up, dang it.
    addClass(document.documentElement, "wait");
    xhr.upload.addEventListener("loadend", function(e) { removeClass(document.documentElement, "wait"); })

    window.setTimeout(function() {
        var reader = new FileReader();
        reader.onload = function (evt) {
            console.log("sending " + localFile.name);
            xhr.send(evt.target.result);
         };
        console.log("reading " + localFile.name);
        reader.readAsArrayBuffer(localFile);
    }, hasFunctionalProgress() ? 0 : 1000);
}

//----------------------------------------------------------------------------------------------
// System
//----------------------------------------------------------------------------------------------

var isFireFox = function() {
    var userAgent = navigator.userAgent.toLowerCase();
    return userAgent.indexOf('firefox') > -1;
}

var isInternetExplorer = function() {
    var userAgent = navigator.userAgent.toLowerCase();
    return userAgent.indexOf('trident') > -1 || userAgent.indexOf('edge') > -1;
}

window.isFtcRobotController = function isFtcRobotController() {
    return ftcUserAgentCategory !== null && ftcUserAgentCategory !== 'OTHER';
};

// Returns whether we get progress callbacks in a reasonable time or not. Chrome is notorious:
// it defers all the progress callbacks until the end, which is of course, useless. We haven't
// tested (yet) on Safari.
var hasFunctionalProgress = function() {
    return isInternetExplorer() || isFireFox();
};

// The endsWith() method is not supported in IE 11 (and earlier versions). So we roll our own.
var endsWith = function (str, suffix) {
    var cch = suffix.length;
    if (str.length >= cch) {
        var test = str.substring(str.length - cch, str.length);
        return test === suffix;
    } else {
        return false;
    }
};

// Polyfill the String prototype with the required methods
(function () {
    if (!String.prototype.endsWith) {

        String.prototype.endsWith = function (suffix) {
            var cch = suffix.length;
            if (this.length >= cch) {
                var test = this.substring(this.length - cch, this.length);
                return test === suffix;
            } else {
                return false;
            }
        };
    }
}).call(String.prototype);

var isInIFrame = function isInIFrame() {
    return window.self !== window.top
};

var setDocumentTitle = function setDocumentTitle(title) {
    window.top.document.title = title
};