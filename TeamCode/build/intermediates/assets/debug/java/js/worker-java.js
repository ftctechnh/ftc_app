importScripts('worker-base.js');

// load nodejs compatible require
const ace_require = require;
require = undefined;
importScripts('require.js');
const antlr4_require = require;
require = ace_require;

ace.define('ace/worker/java-worker',["require","exports","module","ace/lib/oop","ace/worker/mirror"], function(require, exports) {
    "use strict";

    const oop = require("ace/lib/oop");
    const Mirror = require("ace/worker/mirror").Mirror;

    const JavaWorker = function (sender) {
        Mirror.call(this, sender);
        this.setTimeout(200);
        this.$dialect = null;
    };

    oop.inherits(JavaWorker, Mirror);

    const validate = (function () {
        // load antlr4 and the Java parser
        var antlr4, javaAntlr;
        var TerminalNode, TerminalNodeImpl, ClassDeclarationContext, PackageDeclarationContext, QualifiedNameContext, TypeTypeContext, TypeListContext, FormalParameterContext, FormalParameterListContext;
        try {
            require = antlr4_require;
            antlr4 = require('java/js/antlr4/index');
            TerminalNode = antlr4.tree.Tree.TerminalNode;
            TerminalNodeImpl = antlr4.tree.Tree.TerminalNodeImpl;
            javaAntlr = require('java/js/java/index');
            ClassDeclarationContext = javaAntlr.JavaParser.ClassDeclarationContext;
            PackageDeclarationContext = javaAntlr.JavaParser.PackageDeclarationContext;
            QualifiedNameContext = javaAntlr.JavaParser.QualifiedNameContext;
            TypeListContext = javaAntlr.JavaParser.TypeListContext;
            TypeTypeContext = javaAntlr.JavaParser.TypeTypeContext;
            FormalParameterContext = javaAntlr.JavaParser.FormalParameterContext;
            FormalParameterListContext = javaAntlr.JavaParser.FormalParameterListContext;
        } finally {
            require = ace_require;
        }

        // class for gathering errors and posting them to ACE editor
        const AnnotatingErrorListener = function (annotations) {
            antlr4.error.ErrorListener.call(this);
            this.annotations = annotations;
            return this;
        };

        AnnotatingErrorListener.prototype = Object.create(antlr4.error.ErrorListener.prototype);
        AnnotatingErrorListener.prototype.constructor = AnnotatingErrorListener;

        AnnotatingErrorListener.prototype.syntaxError = function (recognizer, offendingSymbol, line, column, msg) {
            this.annotations.push({
                row: line - 1,
                column: column,
                text: msg,
                type: "error"
            });
        };

        const AceJavaListener = function () {
            javaAntlr.JavaListener.call(this);
            this.imports = [];
            this.typesRequired = [];
            this.variables = [];
            this.methods = {};
            this.scope = {
                current: {
                    level: 0, id: 0
                },
                usedIds: [0],
                parent: []
            };
            this.scope.parent.push(this.scope.current);
            this.tokens = [];
            this.packageName = null;
            this.className = null;
            this.parentClassName = null;
            this.interfaceNames = [];
            return this;
        };
        AceJavaListener.prototype = Object.create(javaAntlr.JavaListener.prototype);
        AceJavaListener.constructor = AceJavaListener;
        AceJavaListener.prototype.exitPackageDeclaration = function (ctx) {
            for (var i = 0; i < ctx.children.length; i++) {
                const child = ctx.children[i];
                if (child instanceof QualifiedNameContext) {
                    this.packageName = child.getText();
                    break;
                }
            }
        };

        AceJavaListener.prototype.exitImportDeclaration = function (ctx) {
            var cImport = ctx.getText().substr('import'.length);
            cImport = cImport.substr(0, cImport.length - 1);
            this.imports.push(cImport);
        };

        AceJavaListener.prototype.exitClassDeclaration = function (ctx) {
            if (this.scope.current.level !== 0) return;

            this.className = ctx.children[1].getText();
            for (var i = 2; i < ctx.children.length; i++) {
                const child = ctx.children[i];
                if (child instanceof TypeTypeContext) { // for the "extends" part of the declaration
                    this.parentClassName = child.getText();
                } else if (child instanceof TypeListContext) { // far the "implements" part of the declaration
                    for (var j = 0; j < child.children; j++) {
                        const typeListChild = child.children[j];
                        if (typeListChild instanceof TypeTypeContext) {
                            this.interfaceNames.push(typeListChild);
                        }
                    }
                }
            }
        };

        AceJavaListener.prototype.exitTypeType = function (ctx) {
            this.typesRequired.push(ctx.getText());
        };

        AceJavaListener.prototype.exitAnnotationName = function (ctx) {
            const text = ctx.getText();
            if (text === 'Override' || text === 'Deprecated') return;
            this.typesRequired.push(ctx.getText());
        };

        AceJavaListener.prototype.exitPrimary = function (ctx) {
            var child = ctx.getChild(0);
            if (typeof child === 'undefined') return;
            if (child instanceof TerminalNode) {
                // Try to verify that the name should be a Type name
                if (child.getText().match(/^[A-Z_$]/)) this.typesRequired.push(child.getText());
            }
        };

        AceJavaListener.prototype.exitLocalVariableDeclaration = function (ctx) {
            this.handleVariableDeclaration(ctx, 'local');
        };

        AceJavaListener.prototype.exitFieldDeclaration = function (ctx) {
            if (this.scope.current.level !== 0) return;
            this.handleVariableDeclaration(ctx, 'field');
        };

        AceJavaListener.prototype.handleVariableDeclaration = function (ctx, decType) {
            const type = ctx.children[0].getText();
            const id = ctx.children[1].children[0].children[0].getText();
            this.variables.push({scope: this.scope.current, type: type, id: id, decType: decType, line: ctx.stop.line});
        };

        AceJavaListener.prototype.exitMethodDeclaration = function (ctx) {
            if (this.scope.current.level !== 0) return;
            // something is not right and ctx.children[1] is important to proceed so do no-op
            if (!ctx.children[1]) return;
            const name = ctx.children[1].getText();
            if (!Array.isArray(this.methods[name])) this.methods[name] = [];
            const modifier = ctx.parentCtx.parentCtx.children[0].getText().toUpperCase();
            var params;
            const paramChildren = ctx.children[2].children;
            if (paramChildren) {
                params = paramChildren.filter(function (value) {
                    return value instanceof FormalParameterListContext;
                }).map(function (t) {
                    var paramCtxs = t.children.filter(function (t) {
                        return t instanceof FormalParameterContext;
                    });
                    return paramCtxs.map(function (value) {
                        return value.children.filter(function (t) {
                            return t instanceof TypeTypeContext;
                        }).map(function (t) {
                            return t.getText();
                        });
                    });
                });
            } else {
                // try for a sensible default if params is unknown
                params = [];
            }

            this.methods[name].push({
                type: ctx.children[0].getText(),
                name: name,
                modifier: modifier,
                params: params
            });
        };

        AceJavaListener.prototype.enterBlock = function () {
            const parentScope = {
                id: this.scope.current.id,
                level: this.scope.current.level,
                parent: this.scope.current.parent
            };
            this.scope.parent.push(parentScope);
            var searchElement = this.scope.current.id;
            while (this.scope.usedIds.indexOf(searchElement) >= 0) {
                searchElement++;
            }
            this.scope.current = {id: searchElement, level: this.scope.current.level + 1, parent: parentScope};
            this.scope.usedIds.push(this.scope.current.id);
        };

        AceJavaListener.prototype.exitBlock = function (ctx) {
            this.scope.current.start = ctx.start.start;
            this.scope.current.stop = ctx.stop.stop;
            this.scope.current = this.scope.parent.pop();
        };

        AceJavaListener.prototype.exitCompilationUnit = function (ctx) {
            const parser = ctx.parser,
                tokens = parser.getTokenStream().tokens;

            // last token is always "fake" EOF token
            if (tokens.length > 1) {
                for (var i = 0; i < tokens.length; i++) {
                    var token = tokens[i];
                    var tokenCopy = {
                        column: token.column,
                        line: token.line,
                        start: token.start,
                        stop: token.stop,
                        type: parser.symbolicNames[token.type],
                        text: token.text,
                        index: i
                    };
                    this.tokens.push(tokenCopy);
                }
            }
        };

        function processVariables(variablesToProcess) {
            const variableScopePicture = {start: 0, stop: -1, id: 0, level: 0, variables: [], scopes: []};
            for (var i = 0; i < variablesToProcess.length; i++) {
                var variable = variablesToProcess[i];
                const findMatchingScope = function findMatchingScope(id) {
                    if (id === variableScopePicture.id) return variableScopePicture;
                    for (var j = 0; j < variableScopePicture.scopes; j++) {
                        var testScope = variableScopePicture.scopes[j];
                        if (id === testScope.id) {
                            return testScope;
                        } else if (testScope.scopes.length > 0) {
                            const test = findMatchingScope(id);
                            if (test !== null) {
                                return test;
                            }
                        }
                    }

                    return null;
                };

                var testScope = variable.scope;
                var closetParent = null;
                const nonMatchedScopes = [];
                var maxScopeLevel = testScope.level;
                var k;
                for (k = 0; k <= maxScopeLevel; k++) {
                    closetParent = findMatchingScope(testScope.id);
                    if (closetParent === null) {
                        nonMatchedScopes.push(testScope);
                        testScope = testScope.parent;
                    } else {
                        // Speed up the loop, because we already know the rest of info
                        break;
                    }
                }

                const numberOfNonMatchedScopes = nonMatchedScopes.length;
                for (k = 0; k < numberOfNonMatchedScopes; k++) {
                    var newElement = nonMatchedScopes.pop();
                    newElement = {
                        id: newElement.id,
                        level: newElement.level,
                        start: newElement.start,
                        stop: newElement.stop,
                        scopes: [],
                        variables: []
                    };
                    closetParent.scopes.push(newElement);
                    closetParent = newElement;
                }

                closetParent.variables.push({
                    id: variable.id,
                    type: variable.type,
                    decType: variable.decType,
                    line: variable.line,
                    level: variable.scope.level
                });
            }

            return variableScopePicture;
        }

        return function (input) {
            const stream = antlr4.CharStreams.fromString(input);
            const lexer = new javaAntlr.JavaLexer(stream);
            const tokens = new antlr4.CommonTokenStream(lexer);
            const parser = new javaAntlr.JavaParser(tokens);
            const parseResults = {
                annotations: []
            };
            const listener = new AnnotatingErrorListener(parseResults.annotations);
            parser.removeErrorListeners();
            parser.addErrorListener(listener);
            const javaListener = new AceJavaListener();
            parser.addParseListener(javaListener);

            parser.buildParseTrees = true;
            parser.compilationUnit();
            parseResults.imports = javaListener.imports;
            parseResults.typesRequired = javaListener.typesRequired;
            parseResults.tokens = javaListener.tokens;
            parseResults.className = javaListener.className;
            parseResults.packageName = javaListener.packageName;
            parseResults.interfaces = javaListener.interfaceNames;
            parseResults.parentClass = javaListener.parentClassName;
            parseResults.variables = processVariables(javaListener.variables);
            parseResults.methods = javaListener.methods;

            return parseResults;
        };
    })();

    (function() {
        this.onUpdate = function() {
            const value = this.doc.getValue();
            const annotations = validate(value);
            this.sender.emit('annotate', annotations);
        };

    }).call(JavaWorker.prototype);

    exports.JavaWorker = JavaWorker;
});
