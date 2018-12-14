/**
 * @fileoverview Project utilities.
 * @author lizlooney@google.com (Liz Looney)
 */

/**
 * Fetches the list of projects (as json) and calls the callback.
 */
function fetchProjects(callback) {
  if (typeof blocksIO !== 'undefined') {
    // html/js is within the WebView component within the Android app.
    fetchProjectsViaBlocksIO(callback);
  } else if (window.location.protocol === 'http:' || window.location.protocol === 'https:') {
    // html/js is in a browser, loaded as an http:// URL.
    fetchProjectsViaHttp(callback);
  }
}

/**
 * Fetches the list of samples (as json) and calls the callback.
 */
function fetchSamples(callback) {
  if (typeof blocksIO !== 'undefined') {
    // html/js is within the WebView component within the Android app.
    fetchSamplesViaBlocksIO(callback);
  } else if (window.location.protocol === 'http:' || window.location.protocol === 'https:') {
    // html/js is in a browser, loaded as an http:// URL.
    fetchSamplesViaHttp(callback);
  }
}

/**
 * Opens the project with the given name.
 */
function openProjectBlocks(projectName) {
  if (typeof blocksIO !== 'undefined') {
    // html/js is within the WebView component within the Android app.
    openProjectBlocksViaBlocksIO(projectName);
  } else if (window.location.protocol === 'http:' || window.location.protocol === 'https:') {
    // html/js is in a browser, loaded as an http:// URL.
    openProjectBlocksViaHttp(projectName);
  }
}

/**
 * Fetches the blocks of an existing project and calls the callback
 */
function fetchBlkFileContent(projectName, callback) {
  if (typeof blocksIO !== 'undefined') {
    // html/js is within the WebView component within the Android app.
    fetchBlkFileContentViaBlocksIO(projectName, callback);
  } else if (window.location.protocol === 'http:' || window.location.protocol === 'https:') {
    // html/js is in a browser, loaded as an http:// URL.
    fetchBlkFileContentViaHttp(projectName, callback);
  }
}

function newProject(projectName, sampleName, callback) {
  if (typeof blocksIO !== 'undefined') {
    // html/js is within the WebView component within the Android app.
    newProjectViaBlocksIO(projectName, sampleName, callback);
  } else if (window.location.protocol === 'http:' || window.location.protocol === 'https:') {
    // html/js is in a browser, loaded as an http:// URL.
    newProjectViaHttp(projectName, sampleName, callback);
  }
}

function saveProject(projectName, blkContent, jsFileContent, flavor, group, enable, callback) {
  if (typeof blocksIO !== 'undefined') {
    // html/js is within the WebView component within the Android app.
    saveProjectViaBlocksIO(projectName, blkContent, jsFileContent, flavor, group, enable, callback);
  } else if (window.location.protocol === 'http:' || window.location.protocol === 'https:') {
    // html/js is in a browser, loaded as an http:// URL.
    saveProjectViaHttp(projectName, blkContent, jsFileContent, flavor, group, enable, callback);
  }
}

function renameProject(oldProjectName, newProjectName, callback) {
  if (typeof blocksIO !== 'undefined') {
    // html/js is within the WebView component within the Android app.
    renameProjectViaBlocksIO(oldProjectName, newProjectName, callback);
  } else if (window.location.protocol === 'http:' || window.location.protocol === 'https:') {
    // html/js is in a browser, loaded as an http:// URL.
    renameProjectViaHttp(oldProjectName, newProjectName, callback);
  }
}

function copyProject(oldProjectName, newProjectName, callback) {
  if (typeof blocksIO !== 'undefined') {
    // html/js is within the WebView component within the Android app.
    copyProjectViaBlocksIO(oldProjectName, newProjectName, callback);
  } else if (window.location.protocol === 'http:' || window.location.protocol === 'https:') {
    // html/js is in a browser, loaded as an http:// URL.
    copyProjectViaHttp(oldProjectName, newProjectName, callback);
  }
}

function enableProject(oldProjectName, enable, callback) {
  if (typeof blocksIO !== 'undefined') {
    // html/js is within the WebView component within the Android app.
    enableProjectViaBlocksIO(oldProjectName, enable, callback);
  } else if (window.location.protocol === 'http:' || window.location.protocol === 'https:') {
    // html/js is in a browser, loaded as an http:// URL.
    enableProjectViaHttp(oldProjectName, enable, callback);
  }
}

function deleteProjects(starDelimitedProjectNames, callback) {
  if (typeof blocksIO !== 'undefined') {
    // html/js is within the WebView component within the Android app.
    deleteProjectsViaBlocksIO(starDelimitedProjectNames, callback);
  } else if (window.location.protocol === 'http:' || window.location.protocol === 'https:') {
    // html/js is in a browser, loaded as an http:// URL.
    deleteProjectsViaHttp(starDelimitedProjectNames, callback);
  }
}

function getBlocksJavaClassName(projectName, callback) {
  if (typeof blocksIO !== 'undefined') {
    // html/js is within the WebView component within the Android app.
    getBlocksJavaClassNameViaBlocksIO(projectName, callback);
  } else if (window.location.protocol === 'http:' || window.location.protocol === 'https:') {
    // html/js is in a browser, loaded as an http:// URL.
    getBlocksJavaClassNameViaHttp(projectName, callback);
  }
}

function saveBlocksJava(relativeFileName, javaContent, callback) {
  if (typeof blocksIO !== 'undefined') {
    // html/js is within the WebView component within the Android app.
    saveBlocksJavaViaBlocksIO(relativeFileName, javaContent, callback);
  } else if (window.location.protocol === 'http:' || window.location.protocol === 'https:') {
    // html/js is in a browser, loaded as an http:// URL.
    saveBlocksJavaViaHttp(relativeFileName, javaContent, callback);
  }
}

//..........................................................................
// Code used when html/js is within the WebView component within the
// Android app.

function fetchProjectsViaBlocksIO(callback) {
  var jsonProjects = blocksIO.fetchProjects();
  if (jsonProjects) {
    callback(jsonProjects, '');
  } else {
    callback(null, 'Fetch projects failed.');
  }
}

function fetchSamplesViaBlocksIO(callback) {
  var jsonSamples = blocksIO.fetchSamples();
  if (jsonSamples) {
    callback(jsonSamples, '');
  } else {
    callback(null, 'Fetch samples failed.');
  }
}

function openProjectBlocksViaBlocksIO(projectName) {
  blocksIO.openProjectBlocks(projectName);
}

function fetchBlkFileContentViaBlocksIO(projectName, callback) {
  var blkFileContent = blocksIO.fetchBlkFileContent(projectName);
  if (blkFileContent) {
    callback(blkFileContent, '');
  } else {
    callback(null, 'Fetch blocks failed.');
  }
}

function newProjectViaBlocksIO(projectName, sampleName, callback) {
  var blkFileContent = blocksIO.newProject(projectName, sampleName, enable);
  if (blkFileContent) {
    callback(blkFileContent, '');
  } else {
    // TODO(lizlooney): Provide more information about the error.
    callback(null, 'New project failed.');
  }
}

function saveProjectViaBlocksIO(projectName, blkContent, jsFileContent, flavor, group, enable, callback) {
  var success = blocksIO.saveProject(projectName, blkContent, jsFileContent, flavor, group, enable);
  if (success) {
    callback(true, '');
  } else {
    // TODO(lizlooney): Provide more information about the error.
    callback(false, 'Save project failed.');
  }
}

function renameProjectViaBlocksIO(oldProjectName, newProjectName, callback) {
  var success = blocksIO.renameProject(oldProjectName, newProjectName);
  if (success) {
    callback(true, '');
  } else {
    // TODO(lizlooney): Provide more information about the error.
    callback(false, 'Rename project failed.');
  }
}

function copyProjectViaBlocksIO(oldProjectName, newProjectName, callback) {
  var success = blocksIO.copyProject(oldProjectName, newProjectName);
  if (success) {
    callback(true, '');
  } else {
    // TODO(lizlooney): Provide more information about the error.
    callback(false, 'Copy project failed.');
  }
}

function enableProjectViaBlocksIO(oldProjectName, enable, callback) {
  var success = blocksIO.enableProject(oldProjectName, enable);
  if (success) {
    callback(true, '');
  } else {
    // TODO(lizlooney): Provide more information about the error.
    callback(false, 'Enable project failed.');
  }
}

function deleteProjectsViaBlocksIO(starDelimitedProjectNames, callback) {
  var success = blocksIO.deleteProjects(starDelimitedProjectNames);
  if (success) {
    callback(true, '');
  } else {
    // TODO(lizlooney): Provide more information about the error.
    callback(false, 'Delete projects failed.');
  }
}

function getBlocksJavaClassNameViaBlocksIO(projectName, callback) {
  var className = blocksIO.getBlocksJavaClassName(projectName);
  if (className) {
    callback(className, '');
  } else {
    // TODO(lizlooney): Provide more information about the error.
    callback(null, 'Get blocks java class name failed.');
  }
}

function saveBlocksJavaViaBlocksIO(relativeFileName, javaContent, callback) {
  var success = blocksIO.saveBlocksJava(relativeFileName, javaContent);
  if (success) {
    callback(true, '');
  } else {
    // TODO(lizlooney): Provide more information about the error.
    callback(false, 'Save Java code failed.');
  }
}

//..........................................................................
// Code used when html/js is in a browser, loaded as an http:// URL.

// The following are generated dynamically in ProgrammingModeServer.fetchJavaScriptForServer():
// URI_LIST_PROJECTS
// URI_LIST_SAMPLES
// URI_FETCH_BLK
// URI_NEW_PROJECT
// URI_SAVE_PROJECT
// URI_RENAME_PROJECT
// URI_COPY_PROJECT
// URI_ENABLE_PROJECT
// URI_DELETE_PROJECTS
// URI_GET_BLOCKS_JAVA_CLASS_NAME
// URI_SAVE_BLOCKS_JAVA
// PARAM_NAME
// PARAM_NEW_NAME
// PARAM_BLK
// PARAM_JS
// PARAM_FLAVOR
// PARAM_GROUP
// PARAM_ENABLE
// PARAM_CONTENT

function fetchProjectsViaHttp(callback) {
  var xhr = new XMLHttpRequest();
  xhr.open('GET', URI_LIST_PROJECTS, true);
  xhr.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');
  xhr.onreadystatechange = function() {
    if (xhr.readyState === 4) {
      if (xhr.status === 200) {
        var jsonProjects = xhr.responseText;
        callback(jsonProjects, '');
      } else {
        // TODO(lizlooney): Use specific error messages for various xhr.status values.
        callback(null, 'Fetch projects failed. Error code ' + xhr.status + '. ' + xhr.statusText);
      }
    }
  };
  xhr.send();
}

function fetchSamplesViaHttp(callback) {
  var xhr = new XMLHttpRequest();
  xhr.open('GET', URI_LIST_SAMPLES, true);
  xhr.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');
  xhr.onreadystatechange = function() {
    if (xhr.readyState === 4) {
      if (xhr.status === 200) {
        var jsonSamples = xhr.responseText;
        callback(jsonSamples, '');
      } else {
        // TODO(lizlooney): Use specific error messages for various xhr.status values.
        callback(null, 'Fetch samples failed. Error code ' + xhr.status + '. ' + xhr.statusText);
      }
    }
  };
  xhr.send();
}

function openProjectBlocksViaHttp(projectName) {
  // Go to FtcBlocks.html?project=<projectName>.
  window.location.href = 'FtcBlocks.html?project=' + encodeURIComponent(projectName);
}

function fetchBlkFileContentViaHttp(projectName, callback) {
  var xhr = new XMLHttpRequest();
  var params = PARAM_NAME + '=' + encodeURIComponent(projectName);
  xhr.open('POST', URI_FETCH_BLK, true);
  xhr.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');
  xhr.onreadystatechange = function() {
    if (xhr.readyState === 4) {
      if (xhr.status === 200) {
        var blkFileContent = xhr.responseText;
        callback(blkFileContent, '');
      } else {
        // TODO(lizlooney): Use specific error messages for various xhr.status values.
        callback(null, 'Fetch blocks failed. Error code ' + xhr.status + '. ' + xhr.statusText);
      }
    }
  };
  xhr.send(params);
}

function newProjectViaHttp(projectName, sampleName, callback) {
  var xhr = new XMLHttpRequest();
  var params = PARAM_NAME + '=' + encodeURIComponent(projectName) +
      '&' + PARAM_SAMPLE_NAME + '=' + encodeURIComponent(sampleName);
  xhr.open('POST', URI_NEW_PROJECT, true);
  xhr.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');
  xhr.onreadystatechange = function() {
    if (xhr.readyState === 4) {
      if (xhr.status === 200) {
        var blkFileContent = xhr.responseText;
        callback(blkFileContent, '');
      } else {
        // TODO(lizlooney): Use specific error messages for various xhr.status values.
        callback(null, 'New project failed. Error code ' + xhr.status + '. ' + xhr.statusText);
      }
    }
  };
  xhr.send(params);
}

function saveProjectViaHttp(projectName, blkContent, jsFileContent, flavor, group, enable, callback) {
  var xhr = new XMLHttpRequest();
  var params = PARAM_NAME + '=' + encodeURIComponent(projectName) +
      '&' + PARAM_BLK + '=' + encodeURIComponent(blkContent) +
      '&' + PARAM_JS + '=' + encodeURIComponent(jsFileContent);
  if (flavor != null) {
    params += '&' + PARAM_FLAVOR + '=' + encodeURIComponent(flavor);
  }
  if (group != null) {
    params += '&' + PARAM_GROUP + '=' + encodeURIComponent(group);
  }
  params += '&' + PARAM_ENABLE + '=' + (enable ? "true" : "false");
  xhr.open('POST', URI_SAVE_PROJECT, true);
  xhr.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');
  xhr.onreadystatechange = function() {
    if (xhr.readyState === 4) {
      if (xhr.status === 200) {
          callback(true, '');
      } else {
        // TODO(lizlooney): Use specific error messages for various xhr.status values.
        callback(false, 'Save project failed. Error code ' + xhr.status + '. ' + xhr.statusText);
      }
    }
  };
  xhr.send(params);
}

function renameProjectViaHttp(oldProjectName, newProjectName, callback) {
  var xhr = new XMLHttpRequest();
  var params = PARAM_NAME + '=' + encodeURIComponent(oldProjectName) +
      '&' + PARAM_NEW_NAME + '=' + encodeURIComponent(newProjectName);
  xhr.open('POST', URI_RENAME_PROJECT, true);
  xhr.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');
  xhr.onreadystatechange = function() {
    if (xhr.readyState === 4) {
      if (xhr.status === 200) {
        callback(true, '');
      } else {
        // TODO(lizlooney): Use specific error messages for various xhr.status values.
        callback(false, 'Rename project failed. Error code ' + xhr.status + '. ' + xhr.statusText);
      }
    }
  };
  xhr.send(params);
}

function copyProjectViaHttp(oldProjectName, newProjectName, callback) {
  var xhr = new XMLHttpRequest();
  var params = PARAM_NAME + '=' + encodeURIComponent(oldProjectName) +
      '&' + PARAM_NEW_NAME + '=' + encodeURIComponent(newProjectName);
  xhr.open('POST', URI_COPY_PROJECT, true);
  xhr.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');
  xhr.onreadystatechange = function() {
    if (xhr.readyState === 4) {
      if (xhr.status === 200) {
        callback(true, '');
      } else {
        // TODO(lizlooney): Use specific error messages for various xhr.status values.
        callback(false, 'Copy project failed. Error code ' + xhr.status + '. ' + xhr.statusText);
      }
    }
  };
  xhr.send(params);
}

function enableProjectViaHttp(oldProjectName, enable, callback) {
  var xhr = new XMLHttpRequest();
  var params = PARAM_NAME + '=' + encodeURIComponent(oldProjectName) +
      '&' + PARAM_ENABLE + '=' + (enable ? "true" : "false");
  xhr.open('POST', URI_ENABLE_PROJECT, true);
  xhr.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');
  xhr.onreadystatechange = function() {
    if (xhr.readyState === 4) {
      if (xhr.status === 200) {
        callback(true, '');
      } else {
        // TODO(lizlooney): Use specific error messages for various xhr.status values.
        callback(false, 'Enable project failed. Error code ' + xhr.status + '. ' + xhr.statusText);
      }
    }
  };
  xhr.send(params);
}

function deleteProjectsViaHttp(starDelimitedProjectNames, callback) {
  var xhr = new XMLHttpRequest();
  var params = PARAM_NAME + '=' + encodeURIComponent(starDelimitedProjectNames);
  xhr.open('POST', URI_DELETE_PROJECTS, true);
  xhr.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');
  xhr.onreadystatechange = function() {
    if (xhr.readyState === 4) {
      if (xhr.status === 200) {
        callback(true, '');
      } else {
        // TODO(lizlooney): Use specific error messages for various xhr.status values.
        callback(false, 'Delete projects failed. Error code ' + xhr.status + '. ' + xhr.statusText);
      }
    }
  };
  xhr.send(params);
}

function getBlocksJavaClassNameViaHttp(projectName, callback) {
  var xhr = new XMLHttpRequest();
  var params = PARAM_NAME + '=' + encodeURIComponent(projectName);
  xhr.open('POST', URI_GET_BLOCKS_JAVA_CLASS_NAME, true);
  xhr.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');
  xhr.onreadystatechange = function() {
    if (xhr.readyState === 4) {
      if (xhr.status === 200) {
        var className = xhr.responseText;
        callback(className, '');
      } else {
        // TODO(lizlooney): Use specific error messages for various xhr.status values.
        callback(null, 'Get blocks java class name failed. Error code ' + xhr.status + '. ' + xhr.statusText);
      }
    }
  };
  xhr.send(params);
}

function saveBlocksJavaViaHttp(relativeFileName, javaContent, callback) {
  var xhr = new XMLHttpRequest();
  var params = PARAM_NAME + '=' + encodeURIComponent(relativeFileName) +
      '&' + PARAM_JAVA + '=' + encodeURIComponent(javaContent);
  xhr.open('POST', URI_SAVE_BLOCKS_JAVA, true);
  xhr.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');
  xhr.onreadystatechange = function() {
    if (xhr.readyState === 4) {
      if (xhr.status === 200) {
        callback(true, '');
      } else {
        // TODO(lizlooney): Use specific error messages for various xhr.status values.
        callback(false, 'Save Java code failed. Error code ' + xhr.status + '. ' + xhr.statusText);
      }
    }
  };
  xhr.send(params);
}
