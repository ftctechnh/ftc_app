/*
 * Copyright (c) 2017 David Sargent
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of NAME nor the names of its contributors may be used to
 * endorse or promote products derived from this software without specific prior
 * written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
 * TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
(function ($, angular, ace, console) {
    'use strict';
    angular.module('editor', ['ui.ace', 'ngMessages'])
        .controller('EditorController', ['$scope', function ($scope) {
            $scope.editorReady = false;
            $scope.editorTheme = env.editorTheme;
            $scope.newFile = {
                file: {
                    name: '',
                    ext: 'java',
                    location: ''
                },
                type: 'none',
                templates: [{name: 'None', value: 'none'}],
                valid: true,
                newFolder: function () {
                    var location = prompt('Enter the new folder name', env.tools.selectedLocation($scope.newFile.file.location));
                    if (location === null) return;
                    $scope.newFile.file.location = location;
                    if (location.lastIndexOf('/') !== location.length - 1) location += '/';
                    if (location.indexOf('/') === 0) location = location.substr(1);
                    var fileNewUri = env.urls.URI_FILE_NEW + '?' + env.urls.REQUEST_KEY_FILE + '=' + '/src/' + location;
                    var dataString = env.urls.REQUEST_KEY_NEW + '=1';
                    $.post(fileNewUri, dataString).fail(function () {
                        window.alert("Could not create new folder!");
                    });
                    $scope.newFile.regenerateLocationPicker();
                },
                regenerateLocationPicker: function () {
                    env.setup.projectView(function () {
                        function filterFolders(tree) {
                            var newTree = tree.filter(function (value) {
                                return value.folder;
                            });
                            newTree.forEach(function (value) {
                                if (Array.isArray(value.nodes)) {
                                    value.nodes = filterFolders(value.nodes);
                                }
                            });
                            return newTree;
                        }

                        var parameters = env.trees.defaultParameters(filterFolders(env.trees.srcFiles.slice(0)), function (event, node) {
                            var location = node.parentFile.substr('src/'.length) + node.file;
                            console.log('User selected location: ' + location);
                            $scope.newFile.file.location = location;
                            $scope.$apply();
                        }, 'white', undefined);
                        parameters.multiSelect = false;
                        $('#save-as-file-tree').treeview(parameters);
                    });
                }
            };
            $scope.settings = (function (settings) {
                settings.implementedSettings.forEach(function (p1) {
                    settings[p1] = env.settings.get(p1);
                });

                return settings;
            })({
                themes: env.installedEditorThemes,
                implementedSettings: [
                    'autocompleteEnabled', 'autocompleteForceEnabled',  'autoImportEnabled',
                    'defaultPackage', 'font', 'fontSize', 'keybinding',  'invisibleChars',
                    'printMargin', 'softWrap', 'spacesToTab', 'theme', 'whitespace'
                ]
            });
            $scope.filesToUpload = [];
            $scope.building = false;
            $scope.blurred = false;
            $scope.outsideSource = false;
            $scope.changed = false;
            $scope.code = "";
            $scope.projectViewHasNodesSelected = false;
            env.loadError = false;

            $scope.isAutocompleteDisabledForPerformance = function () {
                return env.ftcLangTools.autoCompleteDisabled && env.ftcLangTools.autoCompleteDisabledReason === 'perf';
            };

            function configureEditorLang(url, _editor) {
                var ext = url.substr(url.lastIndexOf('.') + 1);
                if (ext === "js") {
                    _editor.getSession().setMode("ace/mode/javascript");
                } else if (ext === "md") {
                    _editor.getSession().setMode("ace/mode/markdown");
                } else if (ext === "java") {
                    _editor.getSession().setMode("ace/mode/java");
                } else if (ext === "groovy" || ext === "gradle") {
                    _editor.getSession().setMode("ace/mode/groovy");
                }
            }

            function getFromRawSource(url, _editor, whitespace) {
                if (url === null) {
                    console.error("Cowardly refusing to attempt to fetch from self!");
                    return;
                }

                console.log("Getting " + url);
                function setupEditorWithData(data, readonly) {
                    if (readonly === undefined) readonly = false;
                    // Normalize any Windows line-endings to Nix style, otherwise our auto-complete parser is thrown off
                    data = data.replace(/\r\n/g, '\n');

                    // Normalize any Windows line-endings to Nix style, otherwise our auto-complete parser is thrown off
                    data = data.replace(/\r\n/g, '\n');

                    _editor.setValue(data);
                    _editor.getSession().setUndoManager(new ace.UndoManager());
                    _editor.clearSelection();

                    $scope.code = data;
                    _editor.setReadOnly(readonly);
                }

                $.get(url, function (data) {
                    if (data !== null) { // Sanity check
                        setupEditorWithData(data);

                        whitespace.detectIndentation(_editor.session);
                        if (document.URL.indexOf(env.javaUrlRoot) === -1) $scope.outsideSource = true;
                    } else {
                        setupEditorWithData('// An unknown error occurred', true);
                    }
                }).fail(function (jqxhr, status, error) {
                    setupEditorWithData('// An error occurred trying to fetch:\n' +
                        "\t// '" + url + "'\n" +
                        "\t//\n" +
                        "\t//The server responded with the status '" + error + "'", true);
                    env.loadError = true;
                });
            }

            $scope.aceLoaded = function (_editor) {
                var whitespace = ace.require("ace/ext/whitespace");
                _editor.$blockScrolling = Infinity;
                function detectAndLoadJavaProgrammingModeFile() {
                    var documentId = env.documentId;
                    if (document.URL.indexOf(env.urls.URI_JAVA_EDITOR) + env.urls.URI_JAVA_EDITOR.length !== document.URL.length && documentId !== null) {
                        var fetchLocation = env.urls.URI_FILE_GET + '?' + env.urls.REQUEST_KEY_FILE + '=' + documentId;
                        loadFromUrlAddress(fetchLocation);
                        var fileName = documentId.substring(documentId.lastIndexOf("/") + 1);
                        if (typeof setDocumentTitle === 'undefined') {
                            document.title = fileName + " | FTC Code Editor";
                            console.warn('util.js not loaded correctly');
                        } else {
                            setDocumentTitle(fileName + ' | FTC Code Editor');
                        }
                    } else {
                        configureEditorToDefaults();
                        configureEditorLang(".md", _editor);
                        env.loadError = true;
                    }
                }

                function configureEditorToDefaults() {
                    _editor.setValue('\
# Welcome to the OnBotJava Code Editor\n\
\n\
If you are just starting out, click the \'+\' (Add File) icon in the top left corner.\n\
Enter your new file name, and then choose one of the many samples.\n\
If you just want to drive a basic robot, select the \"BasicOpMode_Linear\" sample.\n\
Select the \"TeleOp\" radio button, and then click \"OK\".\n\
\n\
The sample you chose will be renamed to match the name you entered, and it \n\
will appear on the \"project files\" list in the left pane.\n\
\n\
To edit your code, just click on the desired file in the left hand pane, \n\
and it will be loaded into this Code Editor window. Make any changes.\n\
\n\
Once you are done, click the \"Build Everything\" icon at the bottom of this pane.\n\
This will build your OpModes and report any errors.\n\
If there are no errors, the OpModes will be stored on the Robot for immediate use.\n\
\n\
## Samples\n\
\n\
There are a range of different samples to choose from.\n\
Sample names use a convention which helps to indicate their general, and specific, purpose.\n\
\n\
eg: The name\'s prefix describes the general purpose, which can be one of the following:\n\
\n\
* Basic:    This is a minimally functional OpMode used to illustrate the skeleton\/structure\n\
            of a particular style of OpMode.  These are bare bones examples.\n\
* Sensor:   This is a Sample OpMode that shows how to use a specific sensor.\n\
            It is not intended as a functioning robot, it is simply showing the minimal code\n\
            required to read and display the sensor values.\n\
* Pushbot:  This is a Sample OpMode that uses the Pushbot robot structure as a base.\n\
* Concept:	This is a sample OpMode that illustrates performing a specific function or concept.\n\
            These may be complex, but their operation will be explained clearly in the comments,\n\
            or the header should reference an external doc., guide or tutorial.\n\
* Hardware: This is not an actual OpMode, but a helper class that is used to describe\n\
            one particular robot\'s hardware devices. eg: A Pushbot.  Look at any\n\
            Pushbot sample to see how this can be used in an OpMode.\n\
            If you add a Hardware sample to your project, you MUST use the identical name.\n\
\n\
For more help, visit the FTC Control System Wiki (https://github.com/ftctechnh/ftc_app/wiki) \n\
\n\
\n\
\n\
');
                    _editor.getSession().setUndoManager(new ace.UndoManager());
                    _editor.setReadOnly(true);
                }

                function loadFromUrlAddress(url) {
                    getFromRawSource(url, _editor, whitespace);
                    configureEditorLang(url, _editor);
                }

                function loadFromGitHub(gitHub, gitHubBranch) {
                    function getBranch() {
                        if (gitHub.includes('!')) {
                            gitHubBranch = gitHub.substr(gitHub.lastIndexOf('!') + 1);
                            if (gitHubBranch.includes('>')) {
                                gitHubBranch = gitHubBranch.substr(0, gitHubBranch.lastIndexOf('>'));
                            }

                            gitHub = gitHub.replace('!' + gitHubBranch, "");
                        } else {
                            console.log("No branch was specified! Defaulting to 'master'");
                            gitHubBranch = 'master';
                        }
                    }

                    function resolvePath() {
                        if (gitHub.includes('>')) {
                            path = gitHub.substr(gitHub.lastIndexOf('>') + 1);
                            gitHub = gitHub.replace('>' + path, '');
                        }

                        if (path === null || path === "") {
                            path = 'README.md';
                        }

                        return path;
                    }

                    if (gitHub === null && gitHubBranch !== null) {
                        _editor.setValue("// A Git branch was specified, but no repo. I also need the repo! Thanks ;)");
                    } else if (gitHub !== null && gitHubBranch === null) {
                        gitHub = gitHub.replace('%3E', '>'); // Resolve the html escape sequence to the proper symbol
                        console.log(gitHub);
                        getBranch();
                    }

                    if (gitHub !== null && gitHubBranch !== null) {
                        var path = env.getParameterByName('gp');
                        if (path === null) {
                            path = resolvePath();
                        }
                        // url = 'https://raw.githubusercontent.com/' + gitHub + "/" + gitHubBranch + "/" + path
                        getFromRawSource('https://raw.githubusercontent.com/' + gitHub + "/" + gitHubBranch + "/" + path,
                            _editor, whitespace);
                        configureEditorLang(path, _editor);

                        return path;
                    }

                    return null;
                }

                $scope.editorReady = true;
                $scope.editor = _editor;
                env.editor = _editor;
                $('#left-pane').addClass('ace-' + env.editorTheme);
                $('#build-log-pane').addClass('ace-' + env.editorTheme);

                detectAndLoadJavaProgrammingModeFile();

                var gitHub = env.getParameterByName('gh');
                var gitHubBranch = env.getParameterByName('br');
                var url = env.getParameterByName('url');
                if (url !== null) {
                    loadFromUrlAddress(url);
                } else if (gitHub !== null || gitHubBranch !== null) {
                    loadFromGitHub(gitHub, gitHubBranch);
                }

                _editor.clearSelection();
                _editor.commands.addCommand({
                    name: 'save',
                    bindKey: {win: 'Ctrl-S', mac: 'Command-S'},
                    exec: function (editor) {
                        $scope.tools.saveCode(editor);
                    },
                    readOnly: false // false if this command should not apply in readOnly mode
                });
                env.langTools = ace.require("ace/ext/language_tools");

                _editor.commands.addCommands(whitespace.commands);
                if (env.settings.get('autocompleteEnabled')) {
                    _editor.setOptions({
                        enableBasicAutocompletion: true,
                        enableSnippets: true,
                        enableLiveAutocompletion: true
                    });

                    // Replace the default set of completers with the ones we specify so that we can remove the 'local' completer which
                    // does is redundant with our better completer
                    env.langTools.setCompleters([env.ftcLangTools.completer,
                        {
                            getCompletions: function (editor, session, pos, prefix, callback) {
                                if (!env.ftcLangTools.completer.engaged) {
                                    return env.langTools.keyWordCompleter.getCompletions(editor, session, pos, prefix, callback);
                                } else {
                                    return callback(null, []);
                                }
                            }
                        },
                        {
                            getCompletions: function (editor, session, pos, prefix, callback) {
                                if (!env.ftcLangTools.completer.engaged) {
                                    return env.langTools.snippetCompleter.getCompletions(editor, session, pos, prefix, callback);
                                } else {
                                    return callback(null, []);
                                }
                            }
                        }
                    ]);
                }

                var keyBindings = {
                    OnBotJava: null,
                    vim: ace.require('ace/keyboard/vim').handler,
                    emacs: 'ace/keyboard/emacs'
                };

                _editor.setKeyboardHandler(keyBindings[env.settings.get('keybinding')]);
                _editor.renderer.setShowPrintMargin(env.settings.get('printMargin'));
                _editor.setShowInvisibles(env.settings.get('invisibleChars'));
                _editor.setOption('wrap', env.settings.get('softWrap'));
                _editor.setOption('tabSize', env.settings.get('spacesToTab'));
                _editor.session.setUseSoftTabs(env.settings.get('whitespace') === 'space');

                $.get(env.urls.URI_FILE_TEMPLATES, function (data) {
                    var templatesArr = angular.fromJson(data);
                    templatesArr.forEach(function (value) {
                        $scope.newFile.templates.push({
                            name: value,
                            value: value
                        });

                        $scope.$apply();
                    });
                });

                _editor.on('focus', function () {
                    $scope.blurred = false;
                });

                setInterval(function () {
                    if (!$scope.blurred && $scope.changed) {
                        save();
                        $scope.changed = false;
                    }
                }, 5000);

                $(window).on('unload', function () {
                    if ($scope.changed) {
                        if ($scope.outsideSource && confirm('Do you want to save this file?')) {
                            $scope.tools.saveNewCode();
                        } else {
                            $scope.tools.saveCode($scope.editor);
                        }
                    }
                });

                var updateEditorToolboxDisabledStates = function updateEditorToolboxDisabledStates() {
                    if ($scope.projectViewHasNodesSelected) {
                        $('[disabled-no-project-view-node-selected]').removeClass('disabled');
                    } else {
                        $('[disabled-no-project-view-node-selected]').addClass('disabled');
                    }
                    $('ul.nav li').each(function () {
                        var $element = $(this);
                        if (typeof $element.attr('disabled-tooltip') === 'string') {
                            var hasBeenDisabled = $element.data('disabled');
                            if ($element.hasClass('disabled') && !hasBeenDisabled) {
                                $element.data('disabled', true);
                                $element = $element.children('a');
                                if (typeof $element.data('old-title') === 'undefined') {
                                    var title = $element.attr('data-original-title');
                                    $element.data('old-title', title);
                                }
                                $element.attr('data-original-title', $element.attr('disabled-title'));
                            } else if (hasBeenDisabled) {
                                $element.data('disabled', false);
                                $element = $element.children('a');
                                $element.attr('data-original-title', $element.data('old-title'));
                            }
                        }
                    });
                };

                env.trees.callbacks.projectView.nodeSelected = env.trees.callbacks.projectView.nodeUnselected = function() {
                    $scope.projectViewHasNodesSelected = $('#file-tree').treeview('getSelected', 0).length !== 0;
                    updateEditorToolboxDisabledStates();
                };
                updateEditorToolboxDisabledStates();

                $scope.code = $scope.editor.getValue();
            };

            function save() {
                $scope.tools.saveCode($scope.editor);
            }

            $scope.aceBlurred = function () {
                $scope.blurred = true;

                save();
            };

            $scope.aceChanged = function () {
                $scope.changed = true;
            };

            env.tools = $scope.tools = {
                add: function () {
                    $('#add-button').tooltip('hide');
                    $scope.newFile.file.location = env.tools.selectedLocation($scope.settings.defaultPackage.replace(/\./g, '/'));
                    // Prevent the okay button function from re-calling itself
                    var entryGuard = false;
                    var okayF = function () {
                        if (entryGuard) return;
                        entryGuard = true;
                        if (env.debug) {
                            console.log('New File Create button clicked');
                            console.log(angular.toJson($scope.newFile));
                        }

                        var file = $scope.newFile.file;
                        if (!file.name || file.name === '') {
                            console.error('An attempt has been made to create a file with no name');
                            return;
                        }
                        var name = file.name + '.' + file.ext;
                        var location = file.location;
                        if (location.lastIndexOf('/') !== location.length - 1) location += '/';
                        if (location.indexOf('/') === 0) location = location.substr(1);
                        var editorUri = env.urls.URI_JAVA_EDITOR + '?/src/' + location + name;
                        var fileNewUri = env.urls.URI_FILE_NEW + '?' + env.urls.REQUEST_KEY_FILE + '=/src/' + location + name;
                        var template = $scope.newFile.hasOwnProperty('template') ? $scope.newFile.template : 'none';
                        var opModeType = $scope.newFile.hasOwnProperty('type') ? $scope.newFile.type : 'none';
                        var opModeDisable = $scope.newFile.hasOwnProperty('disabled') ? $scope.newFile.disabled : false;
                        var setupHardware = $scope.newFile.hasOwnProperty('setupHardware') ? $scope.newFile.setupHardware : false;
                        var dataString = env.urls.REQUEST_KEY_NEW + '=1' +
                            (template === 'none' ? '' : ('&' + env.urls.REQUEST_KEY_TEMPLATE + '=templates/' + template)) +
                            (opModeType === 'nc' ? '&' + env.urls.REQUEST_KEY_PRESERVE + '=1' : '') +
                            (opModeDisable || opModeType !== "none" ? '&' + env.urls.REQUEST_KEY_OPMODE_ANNOTATIONS + '=' : '') +
                            (opModeDisable ? '@Disabled\n' : '') +
                            (opModeType === 'auto' ? '@Autonomous\n' : '') +
                            (opModeType === 'teleop' ? '@TeleOp\n' : '') +
                            (setupHardware ? '&' + env.urls.REQUEST_KEY_SETUP_HARDWARE + '=1' : '') +
                            ('&' + env.urls.REQUEST_KEY_TEAM_NAME + '=' + env.teamName);
                        if (env.debug) {
                            console.log(editorUri);
                            console.log(dataString);
                        }

                        var $new = $('#new-file-modal');
                        $new.modal('hide');

                        if (file.ext === '') {
                            alert('The file name specified does not have an extension. Please include an extension, and' +
                                ' try again.');
                            $new.modal('show').one('#new-file-okay', okayF);
                            return;
                        }

                        if (!(file.ext === 'java' || confirm('The file you trying to' +
                                ' create does not end a \'java\' extension. If you are trying to make an OpMode, the ' +
                                '\'java\' extension is recommended.\n\nDo you wish to continue?'))) {
                            $new.modal('show').one('#new-file-okay', okayF);
                            return;
                        }

                        $.post(fileNewUri, dataString, function () {
                            window.location = editorUri;
                        }).fail(function () {
                            window.alert("Could not create new file!");
                        });

                        $scope.newFile.file = {
                            name: '',
                            ext: 'java',
                            location: ''
                        };
                    };

                    $scope.newFile.regenerateLocationPicker();
                    $('#new-file-modal').modal('show')
                        .one('click', '#new-file-okay', okayF);
                },
                buildCode: function () {
                    $scope.disabled = true;
                    $('#build-button').tooltip('hide');
                    $scope.tools.saveCode($scope.editor);
                    var buildStartUrl = env.urls.URI_BUILD_LAUNCH;
                    if (typeof buildStartUrl === 'undefined' || buildStartUrl === null) {
                        console.error('Build Start URL is invalid');
                        alert('An application error occurred\n\nDetails: \'start build uri is invalid\'');
                        return;
                    }

                    $.get(buildStartUrl)
                        .done(function () {
                            console.log('Build started!');
                            var $build = $('#build-button');
                            $build.html('<i class="fa fa-3x fa-spin fa-circle-o-notch"></i>');
                            $build.addClass('disabled');
                            var startDate = new Date();
                            var $buildContent = $('#build-log-content');
                            $buildContent.html("Build started at " + new Date());
                            $.get(env.urls.URI_BUILD_WAIT, function (data) {
                                var buildLogContent = $('#build-log-content');
                                buildLogContent.append('\n');
                                var dataString = data.toString();
                                dataString = dataString.replace(/[<>]/g, function (a) {
                                    return {'<': '&lt;', '>': '&gt;'}[a];
                                });
                                var srcDir = env.urls.URI_JAVA_EDITOR + '?/src/';
                                dataString = dataString
                                    .replace(/([\w/\d.]+)\((\d+):(\d+)\): ([^:]+):/g,
                                        '<a href="' + srcDir + '$1">$1</a> line $2, column $3: $4:')
                                    .replace(/ERROR/g, '<span class="error">ERROR</span>')
                                    .replace(/WARNING/g, '<span class="warning">WARNING</span>');

                                buildLogContent.append(dataString);
                                // Now get build status
                                $.get(env.urls.URI_BUILD_STATUS, function (data) {
                                    var buildStatus = angular.fromJson(data);
                                    if (buildStatus.successful) {
                                        $buildContent.append("\nBuild succeeded!");
                                    } else {
                                        $buildContent.append('\nBuild <span class="error">FAILED!</span>');
                                    }
                                });
                            }).fail(function () {
                                $buildContent.append("\nCould not access build system URL.\n" +
                                    "Please verify you are still connected to the Robot Controller and check your logs.");
                            }).always(function () {
                                $build.removeClass('disabled');
                                $build.html('<i class="fa fa-2x fa-wrench"></i>');
                                $buildContent.append("\n\nBuild finished in " + Math.round((new Date() - startDate) / 100) / 10 + " seconds");
                                $scope.building = false;
                            });
                        });
                },
                _copy: null,
                copyFiles: function (params) {
                    if (typeof params === 'undefined') params = {silent: false};
                    var to = params.to;
                    var nodes = params.nodes;
                    var silent = params.silent;
                    if (typeof silent === 'undefined') silent = false;

                    var requests = [];
                    var didARequestFail = false;
                    nodes.forEach(function (value) {
                        var oldFile = value.parentFile + value.file;
                        if (value.folder) oldFile += '/';
                        if (oldFile.indexOf('/') !== 0) oldFile = '/' + oldFile;
                        if (to.indexOf('/') !== 0) to = '/' + to;
                        var postData = env.urls.REQUEST_KEY_COPY_FROM + '=' + oldFile + '&' + env.urls.REQUEST_KEY_COPY_TO + '=' + to;
                        var request = $.post(env.urls.URI_FILE_COPY, postData);
                        request.fail(function () {
                            if (!silent) {
                                alert('Copy of file \'' + oldFile + '\' failed!');
                            }
                        }).fail(function () {
                            didARequestFail = true;
                        });

                        requests.push(request);
                    });

                    return $.when.apply($, requests).then(function () {
                        if (typeof params.callback === 'function') {
                            params.callback(didARequestFail);
                        }
                    });
                },
                delete: function (opt) {
                    if (typeof opt === 'undefined') opt = {};
                    if (typeof opt.silent === 'undefined') opt.silent = false;
                    $('#delete-button').tooltip('hide');
                    var fileTree = $('#file-tree').treeview('getSelected', 0);
                    var filesToDelete = [];
                    var deleteSelf = false;
                    fileTree.forEach(function (value) {
                        var fileName = value.file;

                        // Check if we are going to delete the current file
                        if (env.documentId.indexOf('/' + value.parentFile + fileName) === 0) {
                            deleteSelf = true;
                            // Guard against things about to change a file (which could undo the delete)
                            $scope.changed = false;
                            $scope.editor.setReadOnly(true);
                            $scope.editor.session.setUndoManager(new ace.UndoManager());
                        }

                        filesToDelete.push(value.parentFile + fileName);
                    });

                    var message;
                    if (filesToDelete.length === 0)
                        return null;
                    else if (filesToDelete.length === 1)
                        message = "Are you sure you want to delete '" +
                            filesToDelete[0].substr(filesToDelete[0].lastIndexOf('/') + 1) + "'?";
                    else
                        message = "Are you sure you want to delete " + filesToDelete.length + " files?";

                    if (filesToDelete.length > 0 && (opt.silent || window.confirm(message))) {
                        var deleteData = env.urls.REQUEST_KEY_DELETE + '=' + JSON.stringify(filesToDelete);
                        return $.post(env.urls.URI_FILE_DELETE, deleteData, function () {
                            console.log("delete finished!");
                        }).fail(function () {
                            if (!opt.silent)
                                alert("Delete failed!");
                        }).always(function () {
                            console.log(deleteData);
                            if (deleteSelf) {
                                if (typeof opt.callback === 'function') {
                                    opt.callback(deleteSelf);
                                } else {
                                    window.location = env.urls.URI_JAVA_EDITOR;
                                }
                            } else {
                                env.setup.projectView();
                            }
                        });
                    }

                    return null;
                },
                filesSelect: function (files) {
                    console.log(files);
                    var failed = function(file) {
                        return function () {
                            console.log(file.name + ' failed to upload.');
                        };
                    };
                    var alwaysCallback = function () {
                        env.setup.projectView();
                    };

                    for (var i = 0; i < files.length; i++) {
                        var file = files.item(i);
                        var fd = new FormData();
                        fd.append('file', file);
                        var failedCallback = failed(file);
                        $.ajax({
                            url: env.urls.URI_FILE_UPLOAD,
                            contentType: false,
                            cache: false,
                            data: fd,
                            processData: false,
                            type: 'POST'
                        }).fail(failedCallback).always(alwaysCallback);
                    }
                },
                minimizePane: function () {
                    $('#minimize-button').tooltip('hide');
                    env.resizeLeftHandPanel($('#left-pane-handle'), 0, $('#main-window'), $('#left-pane'), true);
                },
                newFolder: $scope.newFile.newFolder,
                saveCode: function (_editor, documentId, callback) {
                    if (env.loadError) return; // become no-op if there was an error loading
                    if (typeof documentId === 'undefined') documentId = env.documentId;
                    var code = _editor.getValue();
                    var tabWidthSpaces = (function () {
                        var x = '';
                        for (var i = 0; i < env.settings.get('spacesToTab'); i++) {
                            x += ' ';
                        }
                        return x;
                    })();
                    if (env.settings.get('whitespace') === 'space') {
                        code = code.replace(/\t/g, tabWidthSpaces);
                    } else if (env.settings.get('whitespace') === 'tab') {
                        code = code.replace(new RegExp(tabWidthSpaces, 'g'), '\t');
                    }
                    var encodedCode = env.fixedUriEncode(code);
                    var saveData = env.urls.REQUEST_KEY_SAVE + '=' + encodedCode;
                    if (documentId === null || documentId === '') return;
                    var fileSaveUri = env.urls.URI_FILE_SAVE + '?' + env.urls.REQUEST_KEY_FILE + '=' + documentId;
                    $.post(fileSaveUri, saveData)
                        .always(function () {
                            console.log("Save of " + documentId + " finished");
                            if (typeof callback === 'function') callback();
                        })
                        .fail(function (data) {
                            console.log("Server responded with: " + data.toString());
                        });
                },
                saveNewCode: function () {
                    $('#save-as-modal').modal('show')
                        .one('click', '#save-as-okay', function () {
                            var name = $scope.newFile.file.name + '.' + $scope.newFile.file.ext;
                            var location = $scope.newFile.file.location;
                            var documentId = name + '/' + location;
                            $scope.tools.saveCode($scope.editor, documentId, function () {
                                window.location = env.urls.URI_JAVA_EDITOR + '?/src/ + documentId';
                            });
                            $('#save-as-modal').modal('hide');
                        });
                },
                selectedLocation: function (defaultSelectLocation) {
                    var selectedNodes = $('#file-tree').treeview('getSelected', 0);
                    selectedNodes.forEach(function (p1) {
                        if (p1.hasOwnProperty('nodes')) {
                            defaultSelectLocation = p1.parentFile.substr('src/'.length) + p1.file;
                        } else {
                            defaultSelectLocation = p1.parentFile.substr('src/'.length);
                        }
                    });

                    return defaultSelectLocation;
                },
                settingsAction: function () {
                    $('#settings-button').tooltip('hide');
                    $('#settings-modal').modal('show');
                    $('#settings-okay').one('click', function () {
                        var settingRequests = [
                            env.settings.put('autocompleteEnabled', $scope.settings.autocompleteEnabled),
                            env.settings.put('autocompleteForceEnabled', $scope.settings.autocompleteForceEnabled),
                            env.settings.put('autoImportEnabled', $scope.settings.autoImportEnabled),
                            env.settings.put('defaultPackage', $scope.settings.defaultPackage),
                            env.settings.put('font', $scope.settings.font),
                            env.settings.put('fontSize', $scope.settings.fontSize),
                            env.settings.put('keybinding', $scope.settings.keybinding),
                            env.settings.put('invisibleChars', $scope.settings.invisibleChars),
                            env.settings.put('printMargin', $scope.settings.printMargin),
                            env.settings.put('softWrap', $scope.settings.softWrap),
                            env.settings.put('spacesToTab', $scope.settings.spacesToTab),
                            env.settings.put('theme', $scope.settings.theme),
                            env.settings.put('whitespace', $scope.settings.whitespace)
                        ];

                        $.when.apply(this, settingRequests).then(function () {
                            $('#settings-modal').modal('hide');
                            window.location.reload(true);
                        });
                    });
                    $('#settings-reset').one('click', function () {
                        $('#settings-modal').modal('hide');
                        $.get(env.urls.URI_ADMIN_SETTINGS_RESET, function () {
                            window.location.reload(true);
                        }).fail(function () {
                            alert("Failed to reset settings!");
                        });
                    });
                    $('#settings-advanced-cache-clear').one('click', function () {
                        $.get(env.urls.URI_ADMIN_CLEAN, function () {
                            alert('Finished clearing the cache!');
                        }).fail(function () {
                            alert('Failed to clear the cache!');
                        });
                    });
                    $('#settings-advanced-verify').one('click', function () {
                        $.get(env.urls.URI_ADMIN_REARM, function () {
                            alert('Finished OnBotJava verification!');
                        }).fail(function () {
                            alert('Failed to verify OnBotJava!\n\nCheck that the robot controller is connected,  and consult robot controller logs for more details.');
                        });
                    });
                    $('#settings-advanced-factory-reset').one('click', function () {
                        if (confirm('Are you ABSOLUTELY sure that you want to do this?') &&
                            prompt("Type in 'Yes I want to' (without quotes, case-sensitive) if you wish to proceed") === 'Yes I want to') {
                            $.get(env.urls.URI_ADMIN_RESET_ONBOTJAVA, function (data) {
                                $.get(env.urls.URI_ADMIN_RESET_ONBOTJAVA + '?' + env.urls.REQUEST_KEY_ID + '=' + data).done(function() {
                                    alert('Reset finished!');
                                    window.location = env.urls.URI_JAVA_EDITOR;
                                });
                            }).fail(function () {
                               alert('Failed to completely reset OnBotJava, please see controller logs as manual intervention is likely required');
                            });
                        }
                    });
                },
                undo: function () {
                    $('#undo-button').tooltip('hide');
                    $scope.editor.undo();
                },
                upload: function () {
                    $('#upload-button').tooltip('hide');
                    var $file = $('#file-upload-form');
                    $file.attr('action', env.urls.URI_FILE_UPLOAD);
                    $file.click();
                }
            };

            env.waitForFtcRobotControllerDetect(function() {
                env.tools.uploadTaskName = env.whenFtcRobotController('Copy Files to OnBotJava', 'Upload');
                $scope.$apply();
                env.setup.displayOnBotJava();
            });
        }]);
})(jQuery, angular, ace, console);
