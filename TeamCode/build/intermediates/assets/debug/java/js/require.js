(function () {
    // Warn users about overwriting _require
    if (typeof this.require !== 'undefined') console.warn('require has been defined previously, overwriting!');

    // Orginally From: https://github.com/Muzietto/TK_require.js/blob/master/TK_require.js
    // Author: Marco Faustinelli, David Sargent
    // equivalent to require from node.js
    var _require = {};
    this.require = function require(url) {
        if (url.toLowerCase().substr(-3) !== '.js') {
            url += '.js';  // To allow loading without js suffix.
        }
        if (!_require.cache) {
            _require.cache = [];  // Init cache.
        }

        if (!_require.relativePath) {
            _require.relativePath = '';
            //console.log("TK_require: initializing relativePath");
        }
        var originalPath = _require.relativePath;
        if ('../' === url.substr(0, '../'.length)) url = './' + url;
        var relativePath = "./" === url.substr(0, './'.length);
        if ("http" === url.substr(0, 4)) {
            // If full href is given, extract relative path, if any.
            var baseDir = window.location.href.substring(0, window.location.href.lastIndexOf('/'));
            var scriptDir = url.substring(0, url.lastIndexOf('/'));
            if (url.indexOf(baseDir) === 0) {
                _require.relativePath = scriptDir.substring(baseDir.length + 1) + '/';
                //console.log("TK_require: extracting relative path" + _require.relativePath);
            }
        } else if (relativePath) {
            _require.relativePath += url.substring('./'.length, url.lastIndexOf('/') + 1);
            //console.log("TK_require: Extending Path : " + _require.relativePath);
        } else {
            var baseDir = window.location.href.substring(window.location.origin.length + 1, window.location.href.lastIndexOf('/'));
            var scriptDir = url.substring(0, url.lastIndexOf('/'));
            if (url.indexOf(baseDir) === 0) {
                _require.relativePath = scriptDir.substring(baseDir.length + 1) + '/';
                //console.log("TK_require: extracting relative path" + _require.relativePath);
            }
        }

        var scriptName = url.substring(url.lastIndexOf('/') + 1);
        //console.log("TK_require: scriptName :" + scriptName);

        var fullOrRelativePath = '';
        if ('http' === url.substr(0, 4)) {
            fullOrRelativePath = url;
        } else if (relativePath) {
            var location = window.location;
            var href = location.href;
            fullOrRelativePath = href.substring(location.origin.length, href.lastIndexOf('/') + 1) +
                _require.relativePath + scriptName;
            fullOrRelativePath = fullOrRelativePath.replace(/([^\/]+\/\.\.\/)/g, '')
        } else {
            fullOrRelativePath = '/' + url;
        }

        if (typeof _require.cache[fullOrRelativePath] === 'undefined') _require.cache[fullOrRelativePath] = {};
        var cachedRequire = _require.cache[fullOrRelativePath];  // Get from cache.

        var exports = cachedRequire.exports;
        if (!exports) {  // Not cached.
            try {
                exports = cachedRequire.exports = {};
                var X = new XMLHttpRequest();

                //console.log("TK_require: including: " + fullOrRelativePath);
                var source;
                if (cachedRequire.src) {
                    source = cachedRequire.src;
                } else {
                    X.open('GET', fullOrRelativePath, false); // Synchronous load.
                    X.send();
                    if (X.status && X.status !== 200) {
                        throw new Error(X.statusText);
                    }
                    source = X.responseText;
                    // Fix (if saved from for Chrome Dev Tools)
                    if (source.substr(0, 10) === "(function(") {
                        var moduleStart = source.indexOf('{');
                        var moduleEnd = source.lastIndexOf('})');
                        var CDTcomment = source.indexOf('//@ ');
                        CDTcomment = CDTcomment > -1 ? CDTcomment : source.indexOf('//# ');
                        if (CDTcomment > -1 && CDTcomment < moduleStart + 6) {
                            moduleStart = source.indexOf('\n', CDTcomment);
                        }
                        source = source.slice(moduleStart + 1, moduleEnd - 1);
                    }
                    // Fix, add comment to show source on Chrome Dev Tools
                    source = "//# sourceURL=" + window.location.origin + fullOrRelativePath + "\n" + source;
                    cachedRequire.src = source;
                }

                //------
                var module = {id: url, uri: url, exports: exports}; // According to node.js modules
                // Create a Fn with module code, and 3 params: _require, exports & module
                var anonFn = new Function("require", "exports", "module", source);
                anonFn(require, exports, module);  // Call the Fn, Execute the module
            } catch (err) {
                throw new Error("Error loading module " + url + ": " + err.stack);
            }
        }
        // Restore the relative path.
        _require.relativePath = originalPath;

        return exports; // require returns object exported by module
    }
})();
