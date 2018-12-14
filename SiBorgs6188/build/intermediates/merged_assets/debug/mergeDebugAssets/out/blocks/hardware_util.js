/**
 * @fileoverview Hardware utilities.
 * @author lizlooney@google.com (Liz Looney)
 */

// Note: This file is misnamed. It includes some utilities not related to hardware.

/**
 * Fetches the JavaScript code related to the hardware in the active configuration and calls the
 * callback.
 */
function fetchJavaScriptForHardware(callback) {
  if (typeof blocksIO !== 'undefined') {
    // html/js is within the WebView component within the Android app.
    fetchJavaScriptForHardwareViaBlocksIO(callback);
  } else if (window.location.protocol === 'http:' || window.location.protocol === 'https:') {
    // html/js is in a browser, loaded as an http:// URL.
    fetchJavaScriptForHardwareViaHttp(callback);
  }
}

/**
 * Sends a ping request and calls the callback.
 */
function sendPing(name, callback) {
  if (window.location.protocol === 'http:' || window.location.protocol === 'https:') {
    // html/js is in a browser, loaded as an http:// URL.
    sendPingViaHttp(name, callback);
  } else {
    callback(false);
  }
}

//..........................................................................
// Code used when html/js is within the WebView component within the
// Android app.

function fetchJavaScriptForHardwareViaBlocksIO(callback) {
  var jsHardware = blocksIO.fetchJavaScriptForHardware();
  if (jsHardware) {
    callback(jsHardware, '');
  } else {
    callback(null, 'Fetch JavaScript for Hardware failed.');
  }
}

//..........................................................................
// Code used when html/js is in a browser, loaded as a http:// URL.

// The following are generated dynamically in ProgrammingModeServer.fetchJavaScriptForServer():
// URI_HARDWARE
// URI_PING
// PARAM_NAME

function fetchJavaScriptForHardwareViaHttp(callback) {
  var xhr = new XMLHttpRequest();
  xhr.open('GET', URI_HARDWARE, true);
  xhr.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');
  xhr.onreadystatechange = function() {
    if (xhr.readyState === 4) {
      if (xhr.status === 200) {
        var jsHardware = xhr.responseText;
        callback(jsHardware, '');
      } else {
        // TODO(lizlooney): Use specific error messages for various xhr.status values.
        callback(null, 'Fetch JavaScript for Hardware failed. Error code ' + xhr.status + '. ' + xhr.statusText);
      }
    }
  };
  xhr.send();
}

function sendPingViaHttp(name, callback) {
  var xhr = new XMLHttpRequest();
  xhr.open('POST', URI_PING, true);
  xhr.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');
  xhr.onreadystatechange = function() {
    if (xhr.readyState === 4) {
      if (xhr.status === 200) {
        callback(true);
      } else {
        callback(false);
      }
    }
  };
  var params = PARAM_NAME + '=' + encodeURIComponent(name);
  xhr.send(params);
}
