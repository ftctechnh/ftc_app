/**
 * @fileoverview Clipboard utilities.
 * @author lizlooney@google.com (Liz Looney)
 */

/**
 * Saves the clipboard content and calls the callback.
 */
function saveClipboardContent(clipboardContent, callback) {
  if (typeof blocksIO !== 'undefined') {
    // html/js is within the WebView component within the Android app.
    saveClipboardContentViaBlocksIO(clipboardContent, callback);
  } else if (window.location.protocol === 'http:') {
    // html/js is in a browser, loaded as an http:// URL.
    saveClipboardContentViaHttp(clipboardContent, callback);
  }
}

/**
 * Fetches the previously saved clipboard content and calls the callback.
 */
function fetchClipboardContent(callback) {
  if (typeof blocksIO !== 'undefined') {
    // html/js is within the WebView component within the Android app.
    fetchClipboardContentViaBlocksIO(callback);
  } else if (window.location.protocol === 'http:') {
    // html/js is in a browser, loaded as an http:// URL.
    fetchClipboardContentViaHttp(callback);
  }
}

//..........................................................................
// Code used when html/js is within the WebView component within the
// Android app.

function saveClipboardContentViaBlocksIO(clipboardContent, callback) {
  var success = blocksIO.saveClipboardContent(clipboardContent);
  if (success) {
    callback(true, '');
  } else {
    // TODO(lizlooney): Provide more information about the error.
    callback(false, 'Save clipboard failed.');
  }
}

function fetchClipboardContentViaBlocksIO(callback) {
  var clipboardContent = blocksIO.fetchClipboardContent();
  if (clipboardContent) {
    callback(clipboardContent, '');
  } else {
    callback(null, 'Fetch clipboard failed.');
  }
}

//..........................................................................
// Code used when html/js is in a browser, loaded as an http:// URL.

// The following are generated dynamically in ProgrammingModeServer.fetchJavaScriptForServer():
// URI_SAVE_CLIPBOARD
// URI_FETCH_CLIPBOARD
// PARAM_CLIPBOARD

function saveClipboardContentViaHttp(clipboardContent, callback) {
  var xhr = new XMLHttpRequest();
  var params = PARAM_CLIPBOARD + '=' + encodeURIComponent(clipboardContent);
  xhr.open('POST', URI_SAVE_CLIPBOARD, true);
  xhr.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');
  xhr.onreadystatechange = function() {
    if (xhr.readyState === 4) {
      if (xhr.status === 200) {
        callback(true, '');
      } else {
        // TODO(lizlooney): Use specific error messages for various xhr.status values.
        callback(false, 'Save clipboard failed. Error code ' + xhr.status + '. ' + xhr.statusText);
      }
    }
  };
  xhr.send(params);
}

function fetchClipboardContentViaHttp(callback) {
  var xhr = new XMLHttpRequest();
  xhr.open('POST', URI_FETCH_CLIPBOARD, true);
  xhr.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');
  xhr.onreadystatechange = function() {
    if (xhr.readyState === 4) {
      if (xhr.status === 200) {
        var clipboardContent = xhr.responseText;
        callback(clipboardContent, '');
      } else {
        // TODO(lizlooney): Use specific error messages for various xhr.status values.
        callback(null, 'Fetch clipboard failed. Error code ' + xhr.status + '. ' + xhr.statusText);
      }
    }
  };
  xhr.send();
}
