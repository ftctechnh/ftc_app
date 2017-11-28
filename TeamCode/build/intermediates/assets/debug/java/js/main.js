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
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESSFOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
 * TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
(function (env, $, console) {
    'use strict';
    // Add a change element type to jQuery
    // By jakov, from SO post "how to change an element type using jquery"
    $.fn.changeElementType = function (newType) {
        this.each(function () {
            var attrs = {};
            $.each(this.attributes, function (idx, attribute) {
                var nodeName = attribute.nodeName;
                if (nodeName === 'href' && newType !== 'a') return;

                attrs[nodeName] = attribute.value;
            });

            $(this).replaceWith($('<' + newType + ' />', attrs).append($(this).contents()));
        });
    };

    env.installedEditorThemes = ['chaos', 'chrome', 'github', 'monokai', 'solarized-dark', 'solarized-light'];
    env.editorTheme = env.settings.get('theme');

    env.getParameterByName = function getParameterByName(name) {
        const url = window.location.href;
        name = name.replace(/[\[\]]/g, "\\$&");
        const regex = new RegExp("[?&]" + name + "(=([^&#]*)|&|#|$)");
        var results = regex.exec(url);
        if (!results) return null;
        if (!results[2]) return '';
        return decodeURIComponent(results[2].replace(/\+/g, " "));
    };

    env.fixedUriEncode = function fixedUriEncode(code) {
        return encodeURIComponent(code).replace(/[!'()*]/g, function (c) {
            return '%' + c.charCodeAt(0).toString(16);
        });
    };

    env.baseUrlRoot = (function baseUrlRoot() {
        return document.URL.substring(0, document.URL.indexOf(env.urls.URI_JAVA_PREFIX));
    })();

    env.javaUrlRoot = (function getJavaUrlRoot() {
        const currentUri = document.URL;
        return currentUri.substring(0, currentUri.indexOf(env.urls.URI_JAVA_PREFIX) + env.urls.URI_JAVA_PREFIX.length);
    })();

    env.documentId = (function getDocumentId() {
        const currentUri = document.URL;
        const javaEditorWithParamQuery = env.urls.URI_JAVA_EDITOR + '?';
        if (currentUri.indexOf(javaEditorWithParamQuery) !== -1) {
            var paramFileName = env.getParameterByName("f");
            if (paramFileName) return paramFileName;
            paramFileName = currentUri.substring(currentUri.indexOf(javaEditorWithParamQuery) + (javaEditorWithParamQuery).length);
            if (paramFileName !== '.java') return paramFileName;
        }

        return '';
    })();

    env._isFtcRobotController = (function () {
        if (typeof window.isFtcRobotController !== 'function') {
            console.warn('util.js has not been loaded');
            return false;
        }

        var promise = $.Deferred();
        loadRcInfo(function () {
           env._isFtcRobotController = window.isFtcRobotController();
           promise.resolve(env._isFtcRobotController);
        });

        return promise;
    })();

    env.whenFtcRobotController = function(yes, no) {
        if (typeof env._isFtcRobotController !== 'boolean') {
            console.warn('not ready for when, you must call this is from the callback in waitForRobotControllerDetect()');
        }
        return env._isFtcRobotController ? yes : no;
    };

    env.waitForFtcRobotControllerDetect = function(callback) {
        if (typeof callback !== 'function') return;
        $.when(env._isFtcRobotController).then(callback);
    };

    //noinspection JSUnusedGlobalSymbols
    env.trees = {
        defaultParameters: function defaultTreeParameters(dataTree, nodeSelected, nodeUnselected, backColor, hoverColor) {
            return {
                data: dataTree,
                expandIcon: 'fa fa-caret-right',
                collapseIcon: 'fa fa-caret-down',
                backColor: backColor,
                onhoverColor: hoverColor,
                enableLinks: true,
                showBorder: false,
                multiSelect: true,
                onNodeSelected: nodeSelected,
                onNodeUnselected: nodeUnselected
            };
        },
        callbacks: { // for the UI system
            projectView: {
                nodeSelected: function () {

                },
                nodeUnselected: function () {

                }
            }
        },
        parse: {
            sourceTree: function parseSourceTree(fileTree) {
                // Filter the jars directory out of the file tree data src file data before being parsed
                const JARS_DIR = '/jars';
                fileTree.src = fileTree.src.filter(function (node) {
                    return node.indexOf(JARS_DIR) !== 0;
                });

                env.trees.srcFiles = env.trees.parse._tree(fileTree.src, 'src');
                return env.trees.srcFiles;
            },
            librariesTree: function parseLibrariesTree(fileTree) {
                env.trees.jarFiles = env.trees.parse._tree(fileTree.jars, 'jars');
                return env.trees.jarFiles;
            },
            _tree: function parseFileTree(fileTree, fileNamespace) {
                const stage1Output = {};

                fileTree.forEach(function (element) {
                    var array = element.substr(1).split('/');
                    var lastState2 = stage1Output;

                    array.forEach(function(value) {
                        if (!lastState2.hasOwnProperty(value)) lastState2[value] = {};
                        lastState2 = lastState2[value];
                    });
                });

                function stage2Parser(prop, href, fileNamespace, parentNode) {
                    const resultsFiles = [];
                    const resultsFolder = [];

                    function hasExtension(ext) {
                        return attr.indexOf(ext, attr.length - ext.length) !== -1;
                    }

                    var iconForFile = function () {
                        if (hasExtension('.java')) {
                            return 'fa fa-file-code-o';
                        } else if (hasExtension('.jar') || hasExtension('.zip')) {
                            return 'fa fa-archive';
                        } else if (hasExtension('.txt') || hasExtension('.md')) {
                            return 'fa fa-file-text';
                        } else {
                            return 'fa fa-question';
                        }
                    };

                    for (var attr in prop) {
                        if (!prop.hasOwnProperty(attr)) continue; // skip if not a property we should worry about

                        if (attr === '' || hasExtension('.tmp')) continue;

                        const location = fileNamespace + '/';
                        var items = {
                            text: attr,
                            file: attr,
                            folder: false,
                            icon: iconForFile(),
                            parentFile: location,
                            parent: JSON.stringify(parentNode)
                        };

                        if (!(Object.keys(prop[attr]).length === 0 && prop[attr].constructor === Object)) { // detect folders
                            const children = stage2Parser(prop[attr], href, location + attr, items);
                            // We don't want the default '#' behavior, so literally do nothing
                            items.href = 'javaScript:void(0);';
                            items.file = items.text;
                            // Change item to have a folder icon
                            items.icon = 'fa fa-folder';
                            items.folder = true;
                            items.nodes = children;

                            // Check if nodes has only one child, if so rename to package layout
                            if (items.nodes.length === 1) {
                                const nodeToMerge = items.nodes[0];
                                if (nodeToMerge.hasOwnProperty('nodes')) { // don't merge if nodeToMerge isn't also a parent
                                    items = {
                                        text: items.text + '.' + nodeToMerge.text,
                                        nodes: nodeToMerge.nodes,
                                        href: nodeToMerge.href,
                                        parentFile: nodeToMerge.parentFile,
                                        file: nodeToMerge.file,
                                        icon: 'fa fa-folder',
                                        parentNode: nodeToMerge.parentNode,
                                        folder: true,
                                        parent: nodeToMerge.parent
                                    };
                                }
                            }

                            resultsFolder.push(items);
                        } else { // file
                            items.href = href + '/' + location + attr;
                            resultsFiles.push(items);
                        }
                    }

                    const nodeSortFn = function (a, b) {
                        return a.text.localeCompare(b.text);
                    };

                    resultsFolder.sort(nodeSortFn);
                    resultsFiles.sort(nodeSortFn);
                    return resultsFolder.concat(resultsFiles);
                }

                const fileGetUri = env.javaUrlRoot + '/editor.html?';
                return stage2Parser(stage1Output, fileGetUri, fileNamespace, null);
            }
        },
        get: function getTree(callback, callbackFinished) {
            // Some logic to retrieve, or generate tree structure
            const treeUri = env.urls.URI_FILE_TREE;

            $.get(treeUri, function (data) {
                callback(env.trees.parse.sourceTree(data)/*, env.parseLibrariesTree(data)*/, callbackFinished);
            }).fail(function () {
                console.error("Getting file tree failed!");
                //callCallback(fileTree);
            });
        },
        srcFiles: null,
        jarFiles: null
    };

    env.resizeLeftHandPanel = function resizeLeftHandPanel(handle, leftAmt, $editor, $file, shown) {
        handle.offset({
            //top: e.pageY,
            left: leftAmt
        });

        $editor.css('width', (window.innerWidth - leftAmt) + 'px');
        $file.css('width', leftAmt + 'px');
        shown = leftAmt !== 0;
        ($('#file-tree-container')).perfectScrollbar('update');

        $('#left-pane').attr('min-width', function () {
            var minWidths = [300];
            var attrs = '';

            minWidths.forEach(function (minWidth) {
                if (minWidth < $('#left-pane').width()) {
                    attrs += minWidth + "px ";
                }
            });

            if (attrs.lastIndexOf(' ') === attrs.length - 1) attrs = attrs.substr(0, attrs.length - 1);
            return attrs;
        });

        shown = !shown;
        return shown;
    };

    env.resizeBuildLogPane = function resizeBuildLogPane(y, buildLogHandle) {
        const editor = $('#editor-component-container');
        const buildLogPane = $('#build-log-pane');
        buildLogHandle.offset({top: y});
        editor.css('height', y + 'px');
        buildLogPane.css('height', (window.innerHeight - y) + 'px');
        if (typeof env.editor !== 'undefined') env.editor.resize();
    };

    env.resizeProjectFilesTree = function resizeProjectFilesTree(projectFileTree) {
        var number = ($('#left-pane').height() - $('#menu-container').height() -
        $('span.file-tree-title').height() - 15);
        number += 10;
        if (number > 10) {
            //noinspection JSJQueryEfficiency
            if ($('#left-pane[min-width~="300px"]').length) number += 49;
            projectFileTree.css('height', number + 'px');
        }
    };

    env.colors = (function() {
        var cssEngine = function cssEngine(rule) {
            const css = document.createElement('style'); // Creates <style></style>
            css.type = 'text/css'; // Specifies the type
            if (css.styleSheet) css.styleSheet.cssText = rule; // Support for IE
            else css.appendChild(document.createTextNode(rule)); // Support for the rest
            document.getElementsByTagName('head')[0].appendChild(css); // Specifies where to place the css
        };

        const intervalId = window.setInterval(function () {
            var backColor = $('.ace-' + env.editorTheme).css('background-color');
            if (typeof backColor !== 'undefined') {
                window.clearInterval(intervalId);
                env.colors.backColor = backColor;
            }
        });

        $('<div class="ace-' + env.editorTheme + '" style="display:none;"> \
          <div class="ace_marker-layer"> \
          <div class="ace_selection"></div></div></div>').appendTo('body');
        const intervalIdHoverColor = window.setInterval(function () {
            var hoverColor = $('.ace_selection').css('background-color');
            if ((typeof hoverColor === 'undefined' || hoverColor === 'rgba(0, 0, 0, 0)')) return;
            window.clearInterval(intervalIdHoverColor);

            env.colors.hoverColor = hoverColor;
            cssEngine('#menu-container .nav>li>a:hover { background-color: ' + hoverColor + '; }');
        }, 10);

        // Setup colors to an empty object while we wait for the DOM modifications to take place, and the colors to be calculated
        return {};
    })();

    env.setup = {
        projectView: function setupFileBrowsers(callback) {
            const treeSetupCallback = function treeSetupCallback(srcTree/*, jarsTree*/, callback) {
                var selectedNodes = function selectedNodes(treeView) {
                    return treeView.treeview('getSelected', 0);
                };

                var deselectValue = function deselectValue(value, treeView) {
                    treeView.treeview('unselectNode', [value, {silent: true}]);
                };

                var isSameNode = function isSameNode(node, value, treeView) {
                    if (node.text !== value.text) {
                        deselectValue(value, treeView);
                    } else {
                        const testNodeHasNodes = node.hasOwnProperty('nodes');
                        if (testNodeHasNodes === value.hasOwnProperty('nodes')) {
                            if (testNodeHasNodes) {
                                if (node.nodes.length === value.nodes.length) {
                                    for (var i = 0; i < node.nodes.length; i++) {
                                        isSameNode(node.nodes[i], value.nodes[i], treeView);
                                    }
                                } else {
                                    deselectValue(value, treeView);
                                }
                            }
                        } else {
                            deselectValue(value, treeView);
                        }
                    }
                };

                var generateTreeViewNodeUnselectionHandler = function generateTreeViewNodeUnselectionHandler(treeViews) {
                    return function (event, node) {
                        if (typeof env.trees.callbacks.projectView.nodeUnselected === 'function')
                            env.trees.callbacks.projectView.nodeUnselected();

                        if (env.keys.ctrl.pressed) return;
                        treeViews.forEach(function(treeView) {
                            var selected = selectedNodes(treeView);
                            if (selected.length < 1)  return;

                            selected.forEach(function (value) {
                                deselectValue(value, treeView);
                            });

                            treeView.treeview('selectNode', [node.nodeId, { silent: true}]);
                        });
                    };
                };

                var generateTreeViewNodeSelectionHandler = function generateTreeViewNodeSelectionHandler(treeViews) {
                    return function (event, node) {
                        if (typeof env.trees.callbacks.projectView.nodeSelected === 'function')
                            env.trees.callbacks.projectView.nodeSelected();

                        if (env.keys.ctrl.pressed) return;
                        treeViews.forEach(function(treeView) {
                            var selected = selectedNodes(treeView);

                            selected.forEach(function (value) {
                                isSameNode(node, value, treeView);
                                env.trees.callbacks.projectView.nodeSelected();
                            });
                        });
                    };
                };

                const intervalId = window.setInterval(function () {
                    var backColor = env.colors.backColor;
                    var hoverColor = env.colors.hoverColor;
                    if (typeof backColor !== 'undefined' && typeof hoverColor !== 'undefined') {
                        window.clearInterval(intervalId);
                        const $file = $('#file-tree');
                        //var $library = $('#library-tree');
                        const nodeSelected = generateTreeViewNodeSelectionHandler([$file]);
                        const nodeUnselected = generateTreeViewNodeUnselectionHandler([$file]);
                        $file.treeview(env.trees.defaultParameters(srcTree, nodeSelected, nodeUnselected, backColor, hoverColor));
                        //$library.treeview(defaultTreeParameters(jarsTree, nodeSelected));

                        if (typeof callback === 'function') callback();
                    }
                }, 20);
            };
            env.trees.get(treeSetupCallback, callback);
        },
        windowHandles: function setupWindowHandleEvents() {
            var $dragging = null;
            const $editor = $('#main-window');
            const $file = $('#left-pane');
            var target = '';
            var shown = true;

            var resizeTimeout;
            $(window).on('resize', function () {
                (function resizeThrottler() {
                    // ignore resize events as long as an actualResizeHandler execution is in the queue
                    if (!resizeTimeout) {
                        resizeTimeout = setTimeout(function () {
                            resizeTimeout = null;
                            actualResizeHandler();

                            // The actualResizeHandler will execute at a rate of 15fps
                        }, 66);
                    }
                })();

                function actualResizeHandler() {
                    const currentLeftPaneWidth = $file.width();
                    const width = env.screen.width;
                    const height = env.screen.height;
                    const actualPercentWidth = currentLeftPaneWidth / width;
                    env.resizeLeftHandPanel($('#left-pane-handle'), actualPercentWidth * env.screen.currentWidth(),
                        $editor, $file, true);
                    env.resizeProjectFilesTree($('#file-tree-container'));

                    const editor = $('#editor-component-container');
                    const currentEditorHeight = editor.height();

                    const actualPercentHeight = currentEditorHeight / height;
                    env.resizeBuildLogPane(actualPercentHeight * env.screen.currentHeight(), $('#build-log-handle'));
                }
            });

            $('#navbarMenuTarget').on('shown.bs.collapse', function () {
                const $left = $('#left-pane');
                const navHeight = $left.find('#menu-container .collapse.in ul').height() + 16;
                const viewHeight = $left.height() - $('div.navbar-header').height();
                //noinspection JSJQueryEfficiency
                $('#left-pane #menu-container .collapse.in')
                    .css('height', Math.min(navHeight, viewHeight) + 'px')
                    .css('overflow-y', 'visible') // NOT A BUG, this forces the browser to decide if scrollbars are needed
                    .css('overflow-y', 'auto');
                env.resizeProjectFilesTree($('#file-tree-container'));
            }).on('hidden.bs.collapse', function () {
                env.resizeProjectFilesTree($('#file-tree-container'));
            });

            const touchHandler = function touchHandlerF(event) {
                const touches = event.changedTouches,
                    first = touches[0];
                var type = "";
                switch (event.type) {
                    case "touchstart":
                        type = "mousedown";
                        break;
                    case "touchmove":
                        type = "mousemove";
                        break;
                    case "touchend":
                        type = "mouseup";
                        break;
                    default:
                        return;
                }

                const mouseEvent = new MouseEvent(type, {
                    'screenX': first.screenX,
                    'screenY': first.screenY,
                    'clientX': first.clientX,
                    'clientY': first.clientY,
                    'buttons': 0
                });

                if (type === 'mousemove' || type === 'mouseup') {
                    document.body.dispatchEvent(mouseEvent);
                } else {
                    first.target.dispatchEvent(mouseEvent);
                }
                event.preventDefault();
            };

            var elementIds = ['left-pane-handle', 'build-log-handle'];
            elementIds.forEach(function (elementId) {
                document.getElementById(elementId).addEventListener("touchstart", touchHandler, true);
                document.getElementById(elementId).addEventListener("touchmove", touchHandler, true);
                document.getElementById(elementId).addEventListener("touchend", touchHandler, true);
                document.getElementById(elementId).addEventListener("touchcancel", touchHandler, true);
            });

            // https://jsfiddle.net/Jge9z/
            $(document.body).on("mousemove", function (e) {
                //e.preventDefault();
                //console.log('Touch Move');
                //console.log(e.changedTouches);
                const x = e.clientX;
                const y = e.clientY;
                if ($dragging) {
                    const projectFileTree = ($('#file-tree-container'));
                    //var libraryFileTree = ($('#library-tree-container'));
                    const buildLogHandler = $('#build-log-handle');
                    if (target === "left-pane-handle") {
                        shown = env.resizeLeftHandPanel($dragging, x, $editor, $file, shown);
                        projectFileTree.perfectScrollbar('update');
                        buildLogHandler.perfectScrollbar('update');
                        env.resizeProjectFilesTree(projectFileTree);
                    } else if (target === 'build-log-handle') {
                        env.resizeBuildLogPane(y, buildLogHandler);
                    }
                }
            }).on('mouseup', function () {
                //e.preventDefault();
                //console.log('Touch End');
                $dragging = null;
                target = "";
            });

            $('#left-pane-handle').on('mousedown', function (e) {
                $dragging = $(e.target);
                target = e.target.id;
                // verify shown is correct
                shown = e.pageX !== 0;
            }).on('dblclick', function () {
                const leftPane = $('#left-pane-handle');
                const handle = leftPane;
                shown = leftPane.offset().left > 5;
                var leftAmt;
                if (shown) {
                    leftAmt = 0;
                } else {
                    leftAmt = 0.3 * window.innerWidth;
                }
                shown = env.resizeLeftHandPanel(handle, leftAmt, $editor, $file, shown);
            });

            $('#project-files-handle').on("mousedown", function (e) {
                $dragging = $(e.target);
                target = e.target.id;
            });

            $('#build-log-handle').on('mousedown', function (e) {
                $dragging = $(e.target);
                target = e.target.id;
            });

            const normalizeEditorComponents = function () {
                // Normalize the editor to px from %
                const editorContainer = $('#editor-component-container');
                editorContainer.css('height', editorContainer.height() + 'px');
                const leftPane = $('#left-pane');
                env.resizeLeftHandPanel($('#left-pane-handle'), leftPane.width(), $editor, $file, true);
            };
            if (typeof less.pageLoadFinished !== 'undefined') {
                less.pageLoadFinished.then(normalizeEditorComponents);
            } else {
                normalizeEditorComponents();
            }
        },
        scrollbars: function setupCustomScrollbars() {
            const $perfect = $('[perfect-scrollbar]');
            $perfect.perfectScrollbar();
            $perfect.perfectScrollbar('update');
        },
        fonts: function setupFonts() {
            $('.editor-font').css('font-family', "'" + env.settings.get('font') + "', 'Source Code Pro', monospace");
            fetchRcInfoAndScaleText();
            $('#editor').flowtype({
                minFont: env.settings.get('fontSize'),
                maxFont: 36,
                fontRatio: 80
            });
        },
        tooltips: function setupToolTips() {
            env.waitForFtcRobotControllerDetect(function() {
                (env.whenFtcRobotController(function () {
                    $('[rc-title]').each(function() {
                        $(this).attr('title', $(this).attr('rc-title'));
                    });
                }, $.noop))();$('[data-toggle="tooltip"]').on('taphold', function (e) {
                const target = $(e.target);
                target.tooltip('show');
                setTimeout(function () {
                    target.tooltip('hide');
                }, 3000);
            }).tooltip();});
        },
        contextMenus: function setupContextMenus() {
            // Set up context menus
            $('#menu-container').on('contextmenu', function (e) {
                e.preventDefault();
            });
            $('#editor-toolbox').on('contextmenu', function (e) {
                e.preventDefault();
            });

            const $file = $('#file-tree');
            const nodeItemRequired = function (trigger) {
                if (typeof trigger === 'undefined' || typeof trigger === 'string') trigger = this;
                return typeof $(trigger).data('nodeid') === 'undefined';
            };
            const fetchNodeFromTreeViewDom = function (item) {
                const nodeId = fetchNodeIdFromTreeViewDom(item);
                return $file.treeview('getNode', nodeId);
            };
            var fetchNodeIdFromTreeViewDom = function (item) {
                return $(item).data('nodeid');
            };
            const getSelected = function () {
                return $file.treeview('getSelected', 0);
            };

            const requireSingleNodeSelected = function (trigger) {
                if (typeof trigger === 'undefined' || typeof trigger === 'string') trigger = this;
                return nodeItemRequired(trigger) || getSelected().length !== 1;
            };

            var nodesToCopy = null;
            const markFilesToBeCut = function () {
                $('#file-tree').find('a[href]').each(function () {
                    var $this = $(this);
                    if ($this.attr('href') === 'javaScript:void(0);')
                        $this.changeElementType('span');
                });
                if (nodesToCopy === null || typeof nodesToCopy.nodes === 'undefined' ||
                    typeof nodesToCopy.op === 'undefined' || nodesToCopy.op !== 'cut') return;
                var nodes = nodesToCopy.nodes;
                nodes.forEach(function (node) {
                    var nodeId = node.nodeId;
                    $('#file-tree').find('li[data-nodeid=' + nodeId + ']').addClass('cut');
                });

            };
            const fileTreeUpdateObserver = new MutationObserver(markFilesToBeCut);
            //noinspection JSCheckFunctionSignatures
            fileTreeUpdateObserver.observe($file[0], {childList: true, subtree: true });
            $.contextMenu({
                // define which elements trigger this menu
                selector: "#file-tree-container, #file-tree li.list-group-item",
                // define the elements of the menu
                build: function ($trigger) {
                    if (!nodeItemRequired($trigger))
                        $file.treeview('selectNode', [fetchNodeIdFromTreeViewDom($trigger)]);

                    return {
                        items: {
                            open: {
                                name: 'Open',
                                icon: 'fa-pencil',
                                callback: function () {
                                    window.location = fetchNodeFromTreeViewDom(this).href;
                                },
                                disabled: function () {
                                    return nodeItemRequired(this) || fetchNodeFromTreeViewDom(this).folder;
                                }
                            },
                            new: {
                                name: 'New', icon: 'fa-plus', items: {
                                    folder: {
                                        name: 'Folder',
                                        icon: 'fa-folder',
                                        callback: function () {
                                            env.tools.newFolder();
                                        }
                                    },
                                    file: {
                                        name: 'File',
                                        icon: 'fa-file',
                                        callback: function () {
                                            env.tools.add();
                                        }
                                    }
                                }
                            },
                            rename: {
                                name: 'Rename', callback: function () {
                                    const selectedNode = fetchNodeFromTreeViewDom(this);
                                    var folder = selectedNode.folder;
                                    var newFileName = prompt('Enter a new filename', (folder ? selectedNode.parentFile : '') + selectedNode.file);
                                    if (folder) {
                                        if (newFileName === selectedNode.parentFile + selectedNode.file) return;
                                    } else {
                                        if (newFileName === selectedNode.file) return;
                                    }
                                    var to = folder ? newFileName : selectedNode.parentFile + newFileName;
                                    if (folder) to += '/';
                                    env.tools.copyFiles({
                                        silent: true,
                                        callback: function (failed) {
                                            if (failed) {
                                                alert('Failed to rename file "' + selectedNode.file + '"!');
                                            } else {
                                                $file.treeview('selectNode', selectedNode);
                                                env.tools.delete({
                                                    silent: true, callback: function (opDeletedSelf) {
                                                        if (opDeletedSelf) {
                                                            if (to.indexOf('/') !== 0) to = '/' + to;
                                                            var newCurrentFileName = to;
                                                            if (selectedNode.folder) { // check if we renamed a folder
                                                                newCurrentFileName = env.documentId.substr(
                                                                    ('/' + selectedNode.parentFile + selectedNode.file).length + 1);
                                                                newCurrentFileName = to + newCurrentFileName;
                                                            }
                                                            window.location = env.javaUrlRoot + '/editor.html?' + newCurrentFileName;
                                                        }

                                                        env.setup.projectView();
                                                    }
                                                });
                                            }
                                        },
                                        nodes: [selectedNode],
                                        to: to
                                    });
                                },
                                disabled: requireSingleNodeSelected
                            },
                            sep1: '---------',
                            cut: {
                                name: 'Cut', icon: 'fa-scissors', callback: function () {
                                    nodesToCopy = {
                                        nodes: getSelected(),
                                        op: 'cut'
                                    };
                                    env.tools._copy = nodesToCopy;
                                    markFilesToBeCut();
                                },
                                disabled: nodeItemRequired
                            },
                            copy: {
                                name: 'Copy', icon: 'fa-files-o', callback: function () {
                                    nodesToCopy = {
                                        nodes: getSelected(),
                                        op: 'copy'
                                    };
                                    env.tools._copy = nodesToCopy;
                                },
                                disabled: nodeItemRequired
                            },
                            paste: {
                                name: 'Paste', icon: 'fa-clipboard', callback: function () {
                                    const selectedNode = fetchNodeFromTreeViewDom(this);
                                    const newParentFile = selectedNode.parentFile +
                                        (selectedNode.folder ? selectedNode.file + '/' : '');
                                    const deleteAfterCopy = nodesToCopy.op === 'cut';
                                    nodesToCopy.op = 'copy';
                                    var deletedSelf = false;
                                    var results = [];
                                    var to;
                                    nodesToCopy.nodes.forEach(function(value) {
                                        to = newParentFile + value.file;
                                        if (value.folder) to += '/';
                                        var copyPromise = env.tools.copyFiles({ // copy file blocks until the operation is complete
                                            silent: true,
                                            callback: function (failed) {
                                                if (failed) {
                                                    alert('Failed to copy files!');
                                                } else {
                                                    if (deleteAfterCopy) {
                                                        $file.treeview('selectNode', value);
                                                        const deletePromise = env.tools.delete({
                                                            silent: true, callback: function (opDeletedSelf) {
                                                                if (!deletedSelf) deletedSelf = opDeletedSelf;
                                                            }
                                                        });
                                                        results.push(deletePromise);
                                                    }
                                                }
                                            },
                                            nodes: [value],
                                            to: to
                                        });

                                        results.push(copyPromise);
                                    });

                                    $.when.apply($, results).then(function () {
                                        if (deletedSelf) {
                                            if (nodesToCopy.nodes.length === 1) {
                                                const selectedNode = nodesToCopy.nodes[0];
                                                if (to.indexOf('/') !== 0) to = '/' + to;
                                                var newCurrentFileName = to;
                                                if (selectedNode.folder) { // check if we renamed a folder
                                                    newCurrentFileName = env.documentId.substr(
                                                        ('/' + selectedNode.parentFile + selectedNode.file).length + 1);
                                                    newCurrentFileName = to + newCurrentFileName;
                                                }
                                                window.location = env.javaUrlRoot + '/editor.html?' + newCurrentFileName;
                                            } else {
                                                window.location = env.javaUrlRoot + '/editor.html';
                                            }
                                        }

                                        env.setup.projectView();
                                    });
                                }, disabled: function () {
                                    return nodesToCopy === null || requireSingleNodeSelected(this);
                                }
                            },
                            sep2: '---------',
                            download: {
                                name: (function() {
                                    // we are not going to bother with type detection, because this is generated on the fly, so the new value
                                    // will eventually get to this point
                                    if (typeof env._isFtcRobotController === 'boolean') {
                                        return env.whenFtcRobotController('Copy File From OnBotJava', 'Download');
                                    } else {
                                        return 'Download';
                                    }
                                })(), icon: 'fa-download', callback: function () {
                                    var url;
                                    if (nodeItemRequired(this)) {
                                        url = env.urls.URI_FILE_DOWNLOAD + '?' + env.urls.REQUEST_KEY_FILE + '=/src/';
                                    } else {
                                        var selectedNode = fetchNodeFromTreeViewDom(this);
                                        url = env.urls.URI_FILE_DOWNLOAD + '?' + env.urls.REQUEST_KEY_FILE + '=/' + selectedNode.parentFile + selectedNode.file;
                                        if (selectedNode.folder) url += '/';
                                    }

                                    window.open(url, '_blank');
                                },
                                disabled: function () {
                                    return false;
                                }
                            },
                            delete: {
                                name: 'Delete', icon: 'fa-trash', callback: function () {
                                    env.tools.delete();
                                },
                                disabled: nodeItemRequired
                            }
                        }
                    };
                }
            });
        },
        langTools: function () {
            env.ftcLangTools = new FtcLangTools();
            env.ftcLangTools.autoImport.updateClassToPackageMap();
        },
        keyboardShortcuts: function () {
            var debug = false;

            env.keys.monitorKeyCombo(['ctrl', 'space'], function () {
                if (!env.debug) return;
                var stringify = 'Current type under cursor: ' + JSON.stringify(env.ftcLangTools.detectTypeUnderCursor());
                if (debug) {
                    var token = env.ftcLangTools.tokenUnderCursor();
                    if (token !== null) {
                        var currentLine = token.line;
                        var currentIndex = token.index;
                        var startIndex = currentIndex;
                        var currentTokens = env.ftcLangTools.currentTokens;
                        for (; startIndex >= 0 && currentTokens[startIndex].line === currentLine; startIndex--) {}
                        for (; currentIndex < currentTokens.length && currentTokens[currentIndex].line === currentLine; currentIndex++) {}
                        var tokensForCurrentLine = currentTokens.slice(startIndex, currentIndex);
                        stringify += '\nCurrent tokens on line: ';
                        stringify += JSON.stringify(tokensForCurrentLine);
                    }
                }
                alert(stringify);
            });

            env.keys.monitorKeyCombo(['ctrl','alt','space'], function () {
                if (!env.debug) return;

                debug = !debug;
            });
        },
        teamInformation: function () {
            loadRcInfo(function (rcInfo) {
                env.deviceName = rcInfo.deviceName;
                const defaultTeamPrefix = 'FIRST Tech Challenge Team ';
                env.teamName = defaultTeamPrefix + env.deviceName.substr(0, env.deviceName.indexOf('-'));
                if (env.teamName === defaultTeamPrefix) { // the team does not have a valid RC name, so we can't calculate the team name
                    env.teamName = '';
                }
            });
        },
        print: function () {
            // From https://www.tjvantoll.com/2012/06/15/detecting-print-requests-with-javascript/
            // Accessed on 8/3/2017
            (function() {
                var beforePrint = function() {
                    const $print = $('#print-container');
                    if (!$print.hasClass('ace-' + env.editorTheme)) {
                        $print.addClass('ace-' + env.editorTheme);
                    }

                    const $layer = $print.find('.ace_layer.ace_text-layer');
                    $layer.html('');
                    const session = env.editor.session;
                    for (var i = 0; i < session.getLength(); i++) {
                        const $line = $('<div />').addClass('ace_line');
                        const tokens = session.getTokens(i);
                        tokens.forEach(function(value) {
                            if (value.type === 'text') {
                                $line.append(value.value);
                            } else {
                                $('<span />', {
                                    "class": (function () {
                                        var classes = '';
                                        value.type.split('.').forEach(function(type) {
                                            classes += 'ace_' + type + ' ';
                                        });

                                        return classes;
                                    }),
                                    text: value.value
                                }).appendTo($line);
                            }
                        });

                        // force an empty line during print if there is supposed to be an empty line
                        if (tokens.length === 0) {
                            $line.html('<br />');
                        }

                        $layer.append($line);
                    }
                };

                if (window.matchMedia) {
                    var mediaQueryList = window.matchMedia('print');
                    mediaQueryList.addListener(function(mql) {
                        if (mql.matches) {
                            beforePrint();
                        }
                    });
                }

                window.onbeforeprint = beforePrint;
            }());
        },
         // Should only be called when app.js is ready
         displayOnBotJava: function () {
             $('#page-load-container').css('display', 'none');
             $('#editor-container').css('visibility', 'visible');
         }
    };

    env.keys = (function () {
        const keys = {
            monitorKey: function monitorKey(keyCode, keyName) {
                keys[keyName] = {
                    pressed: false,
                    code: keyCode
                };
                keys.monitoredKeys.push(keyName);
            },
            monitorKeyCombo: function (keysInCombo, callback) {
                if (!Array.isArray(keysInCombo) || typeof callback !== 'function') return false;
                keys.keyCombos.push({keys: keysInCombo, callback: callback});
                return true;
            },
            monitoredKeys: [],
            keyCombos: [],
            numberOfKeysPressed: 0
        };

        function normalizeModifierKey(key, isPressed, down) {
            if (isPressed === down && key.pressed !== down) {
                key.pressed = isPressed;
                if (down) {
                    keys.numberOfKeysPressed++;
                } else {
                    keys.numberOfKeysPressed--;
                }
            }
        }

        $(window).on('keydown', function (evt) {
            // Normalize the modifiers key, in case we have not received the keydown events
            normalizeModifierKey(keys.ctrl, evt.ctrlKey, true);
            normalizeModifierKey(keys.alt, evt.altKey, true);
            normalizeModifierKey(keys.shift, evt.shiftKey, true);

            for (var i = 0; i < keys.monitoredKeys.length; i++) {
                var key = keys.monitoredKeys[i];
                if (key === 'ctrl' || key === 'alt' || key === 'shift') continue;
                // todo: change evt.which to evt.key, when evt.key support is better
                if (evt.which === keys[key].code) {
                    keys[key].pressed = true;
                    keys.numberOfKeysPressed++;
                    break;
                }
            }

            for (var j = 0; j < env.keys.keyCombos.length; j++) {
                var combo = env.keys.keyCombos[j];
                var isValidCombo = true;

                for (var k = 0; k < keys.monitoredKeys.length; k++) {
                    var testKey = keys.monitoredKeys[k];
                    var indexOfComboKey = combo.keys.indexOf(testKey);
                    if ((indexOfComboKey < 0 && keys[testKey].pressed) || // key not wanted
                        (indexOfComboKey >= 0 && !keys[testKey].pressed)) { // wanted key
                        isValidCombo = false;
                        break;
                    }
                }

                if (isValidCombo) {
                    combo.callback(evt);
                    break;
                }
            }
        });

        $(window).on('keyup', function (evt) {
            for (var i = 0; i < keys.monitoredKeys.length; i++) {
                var key = keys.monitoredKeys[i];
                if (!keys.hasOwnProperty(key) || typeof keys[key] !== 'object' ||
                    key === 'ctrl' || key === 'alt' || key === 'shift') continue;
                if (evt.which === keys[key].code) {
                    keys[key].pressed = false;
                    keys.numberOfKeysPressed--;
                    break;
                }
            }

            // Normalize the modifiers key, in case we have not received the keyup events
            normalizeModifierKey(keys.ctrl, evt.ctrlKey, false);
            normalizeModifierKey(keys.alt, evt.altKey, false);
            normalizeModifierKey(keys.shift, evt.shiftKey, false);
        });

        keys.monitorKey(16, 'shift');
        keys.monitorKey(17, 'ctrl');
        keys.monitorKey(18, 'alt');
        keys.monitorKey(32, 'space');
        keys.monitorKey(13, 'enter');
        return keys;
    })();

    env.screen = (function (env) {
        env.screen = {};
        const currentWidth = function () {
            env.screen.width = $(window).width();
            return env.screen.width;
        };
        const currentHeight = function () {
            env.screen.height = $(window).height();
            return env.screen.height;
        };
        return {
            currentWidth: currentWidth,
            currentHeight: currentHeight,
            height: currentHeight(),
            width: currentWidth()
        };
    })(env);

    // Start everything up
    (function init() {
        env.setup.langTools();
        env.setup.windowHandles();
        env.setup.scrollbars();
        env.setup.fonts();
        env.setup.projectView();
        env.setup.tooltips();
        env.setup.contextMenus();
        env.setup.teamInformation();
        env.setup.keyboardShortcuts();
        env.setup.print();
    })();
})(env, jQuery, console);
