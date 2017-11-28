/**
 * @fileoverview Toolbox utilities.
 * @author lizlooney@google.com (Liz Looney)
 */

/**
 * Fetches the toolbox (as xml) and calls the callback.
 */
function fetchToolbox(callback) {
  if (typeof blocksIO !== 'undefined') {
    // html/js is within the WebView component within the Android app.
    fetchToolboxViaBlocksIO(callback);
  } else if (window.location.protocol === 'http:') {
    // html/js is in a browser, loaded as an http:// URL.
    fetchToolboxViaHttp(callback);
  }
}

function addToolboxIcons(workspace) {
  addToolboxIconsForChildren(workspace.toolbox_.tree_.getChildren());
}

function addToolboxIconsForChildren(children) {
  for (var i = 0, child; child = children[i]; i++) {
    if (child.getChildCount() > 0) {
      addToolboxIconsForChildren(child.getChildren());
    } else {
      var iconClass = getIconClass(child.getText());
      if (iconClass) {
        child.setIconClass('toolbox-node-icon ' + iconClass);
      }
    }
  }
}

//..........................................................................
// Code used when html/js is within the WebView component within the
// Android app.

function fetchToolboxViaBlocksIO(callback) {
  var xmlToolbox = blocksIO.fetchToolbox();
  if (xmlToolbox) {
    callback(xmlToolbox, '');
  } else {
    callback(null, 'Fetch toolbox failed.');
  }
}

//..........................................................................
// Code used when html/js is in a browser, loaded as an http:// URL.

// The following are generated dynamically in ProgrammingModeServer.fetchJavaScriptForServer():
// URI_TOOLBOX

function fetchToolboxViaHttp(callback) {
  var xhr = new XMLHttpRequest();
  xhr.open('GET', URI_TOOLBOX, true);
  xhr.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');
  xhr.onreadystatechange = function() {
    if (xhr.readyState === 4) {
      if (xhr.status === 200) {
        var xmlToolbox = xhr.responseText;
        callback(xmlToolbox, '');
      } else {
        // TODO(lizlooney): Use specific error messages for various xhr.status values.
        callback(null, 'Fetch toolbox failed. Error code ' + xhr.status + '. ' + xhr.statusText);
      }
    }
  };
  xhr.send();
}
