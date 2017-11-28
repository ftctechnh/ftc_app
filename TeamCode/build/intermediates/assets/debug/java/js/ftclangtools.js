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

window.FtcLangTools = (function () {
    'use strict';

    const Identifier = 'Identifier';
    const DOT = 'DOT';
    const LPAREN = 'LPAREN';
    const RPAREN = 'RPAREN';
    const COMMA = 'COMMA';

    var checkArgs = function (expr, name) {
        if (!expr) throw new Error(name + ' is not valid');
    };

    var buildAutocompleter = function (langTools) {
        var lastPos = 0;
        var cache = [];
        var firstRun = false;
        return {
            engaged: false,
            _findCompletionsFor: function (priorType, prefix, validCompletions) {
                if (!(priorType !== null && typeof priorType !== 'undefined')) return;

                const possibleFieldsForType = langTools.possibleFieldsForType(priorType);
                for (var field in possibleFieldsForType) {
                    if (!possibleFieldsForType.hasOwnProperty(field)) continue;
                    if (field.indexOf(prefix) === 0) {
                        validCompletions.push({
                            name: field,
                            value: field,
                            score: 800,
                            meta: langTools.classNameFromFullClassName(possibleFieldsForType[field].type).replace(/\$/g, '.')
                        });
                    }
                }

                const possibleMethodsForType = langTools.possibleMethodsForType(priorType);
                for (var method in possibleMethodsForType) {
                    if (!possibleMethodsForType.hasOwnProperty(method)) continue;
                    if (method.indexOf(prefix) === 0) {
                        possibleMethodsForType[method].forEach(function (value) {
                            const params = value.params.map(function (fullClass) {
                                return langTools.classNameFromFullClassName(fullClass).replace(/\$/g, '.');
                            }).join(', ');
                            var snippet = method + '(' + value.params.map(function (t, index) {
                                return '${' + (index + 1) + '}';
                            }).join(', ') + ')';
                            // Add a semicolon and go to the next line if it is not possible to add anything else to the statement
                            if (value.type === 'void') snippet += ';\n${' + value.params.length + 1 + '}';
                            validCompletions.push({
                                caption: method + '(' + params + ')',
                                snippet: snippet,
                                //value: method + '(' + (params.length === 0 ? ')' : ''),
                                name: method,
                                score: 800,
                                type: 'snippet',
                                meta: langTools.classNameFromFullClassName(value.type).replace(/\$/g, '.')
                            });
                        });
                    }
                }

                langTools.autoImport.knownTypeIds.filter(function (t) {
                    return (t.indexOf(priorType.name + '$') === 0 || t.indexOf('$' + priorType.name + '$') > 0) &&
                        t.lastIndexOf('$' + prefix) === t.lastIndexOf('$');
                }).forEach(function (value) {
                    const start = value.lastIndexOf('$' + prefix);
                    var name = value.substr(start + 1);
                    const nextSeparator = name.indexOf('$');
                    name = name.substring(0, nextSeparator < 0 ? name.length : nextSeparator);
                    validCompletions.push({
                        name: name,
                        caption: name,
                        value: name,
                        score: 700,
                        meta: name
                    });
                });
            }, getCompletions: function (editor, session, pos, prefix, callback) {
                try {
                    var timeStart = performance.now();
                    const index = env.editor.session.getDocument().positionToIndex(pos, 0);
                    var currentVariables = langTools.validVariablesUnderIndex(index);
                    var currentToken = langTools.tokenUnderIndex(index);
                    // If nothing has changed, why are we being called? Return whatever we did last time
                    if (index === lastPos) {
                        callback(null, cache);
                        return;
                    }

                    lastPos = index;

                    langTools.completer.engaged = false;
                    if (langTools.autoCompleteDisabled || currentToken === null || !(currentToken.type === Identifier || currentToken.type === DOT || currentToken.type === COMMA || currentToken.type === RPAREN)) {
                        callback(null, []);
                        cache = [];
                        return;
                    }

                    var validCompletions = [];
                    var field;
                    var method;
                    var priorType;
                    if (langTools.currentTokens[currentToken.index - 1].type === DOT) {
                        langTools.completer.engaged = true;
                        priorType = langTools.detectTypeForToken(langTools.currentTokens[currentToken.index - 2]);
                        this._findCompletionsFor(priorType, prefix, validCompletions);
                    } else if (currentToken.type === DOT) {
                        priorType = langTools.detectTypeForToken(langTools.currentTokens[currentToken.index - 1]);
                        this._findCompletionsFor(priorType, prefix, validCompletions);
                    } else if (currentToken.type === COMMA || currentToken.type === RPAREN) {
                        if (langTools.currentTokens[currentToken.index - 1].type === Identifier && langTools.currentTokens[currentToken.index - 2].type === DOT) {
                            priorType = langTools.detectTypeForToken(langTools.currentTokens[currentToken.index - 3]);
                            this._findCompletionsFor(priorType, prefix, validCompletions);
                        }
                    } else {
                        var completedFields = [];
                        currentVariables.forEach(function (value) {
                            if (value.id.indexOf(prefix) === 0) {
                                completedFields.push(value.id);
                                validCompletions.push({name: value.id, value: value.id, score: 1000, meta: value.type});
                            }
                        });

                        const possibleFieldsForType = langTools.possibleFieldsForType(langTools.thisType);
                        for (field in possibleFieldsForType) {
                            if (!possibleFieldsForType.hasOwnProperty(field)) continue;
                            if (field.indexOf(prefix) !== 0 || completedFields.indexOf(field) >= 0) continue;
                            completedFields.push(field);
                            validCompletions.push({
                                name: field,
                                value: field, score: 900,
                                meta: langTools.classNameFromFullClassName(possibleFieldsForType[field].type)
                            });

                            const possibleMethodsForType = langTools.possibleMethodsForType(langTools.thisType);
                            for (method in possibleMethodsForType) {
                                if (!possibleMethodsForType.hasOwnProperty(method)) continue;
                                if (method.indexOf(prefix) === 0) {
                                    possibleMethodsForType[method].forEach(function (value) {
                                        const params = value.params.map(function (fullClass) {
                                            return langTools.classNameFromFullClassName(fullClass).replace(/\$/g, '.');
                                        }).join(', ');
                                        var snippet = method + '(' + value.params.map(function (t, index) {
                                            return '${' + (index + 1) + '}';
                                        }).join(', ') + ')';
                                        // Add a semicolon and go to the next line if it is not possible to add anything else to the statement
                                        if (value.type === 'void') snippet += ';\n${' + value.params.length + 1 + '}';
                                        validCompletions.push({
                                            caption: method + '(' + params + ')',
                                            snippet: snippet,
                                            //value: method + '(' + (params.length === 0 ? ')' : ''),
                                            name: method,
                                            score: 800,
                                            type: 'snippet',
                                            meta: langTools.classNameFromFullClassName(value.type).replace(/\$/g, '.')
                                        });
                                    });
                                }
                            }
                        }

                        validCompletions = validCompletions.concat(
                            langTools.autoImport.knownTypeIds.filter(function (value) {
                                return value.indexOf(prefix) === 0;
                            }).map(function (value) {
                                return {name: value, value: value, score: 500, meta: value};
                            })
                        );
                    }

                    const sortedCompletions = validCompletions.sort(function (lhs, rhs) {
                        if (lhs.score === rhs.score) {
                            return (lhs.caption || lhs.value).localeCompare(rhs.caption || rhs.value);
                        }

                        return lhs.score - rhs.score;
                    });
                    cache = sortedCompletions;
                    langTools.completer.engaged = sortedCompletions.length !== 0;
                    callback(null, sortedCompletions);

                    if (!firstRun) {
                        var timeToCompletion = performance.now() - timeStart;
                        firstRun = true;
                        // timeToCompletion should be less than 50 ms, otherwise this editor is going to be unusably slow.
                        // 50ms is roughly double the time this function should actually take
                        if (!env.settings.get('autocompleteForceEnabled') && timeToCompletion > 50) {
                            langTools.autoCompleteDisabled = true;
                            langTools.autoCompleteDisabledReason = 'perf';
                            console.warn('Disabling OnBotJava autocomplete, due to performance reasons');
                            env.editor.setOption('enableBasicAutocompletion', false);
                            env.editor.setOption('enableSnippets', false);
                            env.editor.setOption('enableLiveAutocompletion', false);
                            env.langTools.setCompleters([]);
                        }
                    }
                } catch (ex) {
                    console.error(ex);
                }
            }
        };
    };

    function AutoImportTools() {}

    AutoImportTools.prototype.classToPackageMap = {};

    AutoImportTools.prototype.knownTypeIds = [];

    AutoImportTools.prototype.updateClassToPackageMap = function () {
        var instance = this;
        $.get(env.urls.URI_JS_AUTOCOMPLETE, function (data) {
            if (typeof data === 'string') {
                data = JSON.parse(data);
            }
            instance.classToPackageMap = data;
            instance.knownTypeIds = [];
            for (var classOrInterfaceType in instance.classToPackageMap) {
                if (!instance.classToPackageMap.hasOwnProperty(classOrInterfaceType)) continue;
                instance.knownTypeIds.push(classOrInterfaceType);
                instance.classToPackageMap[classOrInterfaceType].forEach(function (type) {
                    type.name = classOrInterfaceType;
                    const subclassNameIndex = classOrInterfaceType.lastIndexOf($);
                    if (subclassNameIndex >= 0) {
                        // if (!Array.isArray(instance.classToPackageMap[classOrInterfaceType.substr(subclassNameIndex)]))
                        //     instance.classToPackageMap[classOrInterfaceType.substr(subclassNameIndex)] = [];
                        // instance.classToPackageMap[classOrInterfaceType.substr(subclassNameIndex)].push(type);
                        // instance.packageName = type.packageName = classOrInterfaceType
                    }
                });
            }
        });
    };

    function FtcLangTools() {
        this.autoImport = new AutoImportTools();
        this.completer = buildAutocompleter(this);
    }

    FtcLangTools.prototype.currentVariables = null;
    FtcLangTools.prototype.currentTokens = null;
    FtcLangTools.prototype.currentImports = null;
    FtcLangTools.prototype.thisType = {};
    FtcLangTools.prototype._cache = {};
    FtcLangTools.prototype.autoCompleteDisabled = !env.settings.get('autocompleteEnabled');
    FtcLangTools.prototype.autoCompleteDisabledReason = env.settings.get('autocompleteEnabled') ? null : 'user';

    FtcLangTools.prototype.tokenUnderIndex = function (index) {
        if (this.currentTokens === null) return null;
        var tokenUnderCursor;
        for (var i = 0; i < this.currentTokens.length; i++) {
            var token = this.currentTokens[i];
            if (token.start < index) tokenUnderCursor = token;
            if (token.stop >= index) return tokenUnderCursor;
        }

        return null;
    };

    FtcLangTools.prototype.tokenUnderCursor = function (offset) {
        if (!offset) offset = 0;
        const index = env.editor.session.getDocument().positionToIndex(env.editor.getCursorPosition(), 0) + offset;
        return this.tokenUnderIndex(index - 1);
    };

    FtcLangTools.prototype.validVariablesUnderIndex = function (index, line) {
        if (this.currentVariables === null) return [];
        if (!line) line = env.editor.session.getDocument().indexToPosition(index, 0).row;
        var currentVariables = this.currentVariables.variables;
        const findScopeInIndex = function (currentScopes) {
            currentScopes.forEach(function (scope) {
                if (scope.start < index && scope.stop >= index) {
                    currentVariables = currentVariables.concat(scope.variables);
                    findScopeInIndex(scope.scopes);
                }
            });
        };

        findScopeInIndex(this.currentVariables.scopes);
        return this._cleanupVariables(currentVariables, line);
    };

    FtcLangTools.prototype.validVariablesUnderCursor = function (offset) {
        if (!offset) offset = 0;
        var cursorPosition = env.editor.getCursorPosition();
        const index = env.editor.session.getDocument().positionToIndex(cursorPosition, 0) + offset;
        return this.validVariablesUnderIndex(index, cursorPosition.row + 1);
    };

    FtcLangTools.prototype.validVariablesForToken = function (token) {
        return this.validVariablesUnderIndex(token.start, token.line);
    };

    FtcLangTools.prototype._cleanupVariables = function (possibleVariables, forLine) {
        return possibleVariables.filter(function (possible) {
            return possible.decType === 'field' || possible.line < forLine;
        });
    };

    FtcLangTools.prototype.methodFromPossibleMethods = function (methodCallParts, possibleMethods) {
        var paramTypes = this.getArgumentTypesFromMethod(methodCallParts.lparen, methodCallParts.rparen);
        var selectedMethod = null;
        possibleMethods.forEach(function (value) {
            if (paramTypes.length !== value.params.length) return;

            if (paramTypes.length === 0) selectedMethod = value;
            var validSolution = true;
            for (var i = 0; i < paramTypes.length; i++) {
                if (!this.isTypeInstanceOf(value.params[i], paramTypes[i])) {
                    validSolution = false;
                    break;
                }
            }

            if (validSolution) selectedMethod = value;
        });
        return selectedMethod;
    };

    FtcLangTools.prototype.detectTypeForToken = function (token, variables) {
        if (token === null) return null;
        if (variables === null || typeof variables === 'undefined') variables = this.validVariablesForToken(token);
        if (this.isPartOfMethod(token)) {
            const methodIdentifier = this.methodIdentifierFor(token);
            const currentTokens = this.currentTokens;
            const index = methodIdentifier.index;
            const resolveAgainstThis = (currentTokens[index - 1].type === DOT && currentTokens[index - 2].type === 'THIS') || currentTokens[index - 1].type !== DOT;
            var prefixType;
            var possibleMethods;
            var method;
            var methodCallParts;
            if ((token.type === LPAREN || token.type === RPAREN) && !this.isPartOfMethod(token)) {
                var lparen = token.type === LPAREN ? token : this.findMatchingToken(token);
                var rparen = token.type === RPAREN ? token : this.findMatchingToken(token);
                // Check for a cast at this point
                if (currentTokens[lparen.index + 1].type === LPAREN) {
                    var leftCastParen = currentTokens[lparen.index + 1];
                    var rightCastParen = this.findMatchingToken(currentTokens[lparen.index + 1]);
                    if (rightCastParen === rparen || rightCastParen === null) return null;

                    if (currentTokens[lparen.index + 2].type === LPAREN) {
                        return this.detectTypeForToken(currentTokens[lparen.index + 2]);
                    }

                    var typeString = '';
                    var typeId = null;
                    for (var i = leftCastParen.index + 1; i < rightCastParen.index; i++) {
                        if (currentTokens[i].type === DOT) {
                            typeString += '.';
                        } else if (currentTokens[i].type === Identifier) {
                            typeString += currentTokens[i].text;
                            if (typeString.indexOf('.') < 0) {
                                typeId = this.typeForClassName(typeId);
                            } else {
                                typeComps = typeId.split('.');
                                var possibleTrys = typeComps.length;
                                while (possibleTrys > 0 && (typeId === null || typeof typeId === 'undefined')) {
                                    var test = '';
                                    for (var j = typeComps.length - possibleTrys - 1; j < typeComps.length; j++) {
                                        test += typeComps[j];
                                        if (j + 1 !== typeComps.length) test += '.';
                                    }
                                    typeId = this.typeForClassName(test);
                                    if (typeId === null || typeof typeId === 'undefined') {
                                        typeId = this.typeFromFullClassName(test);
                                    }
                                    possibleTrys--;
                                }
                            }
                        } else {
                            typeId = null;
                            break;
                        }
                    }
                }

                // Jump to the end to see if we can get a type from just inside the expression, basically by getting the type of the next-to-last token
                return this.detectTypeForToken(currentTokens[rparen.index - 1]);
            } else if (!resolveAgainstThis && this.isPartOfMethod(currentTokens[index - 2]) && currentTokens[this.methodIdentifierFor(currentTokens[index - 2]).index - 1].type === DOT) {
                methodCallParts = this._getPartsOfMethodCall(currentTokens[index - 2]);
                prefixType = this.detectTypeForToken(this.methodIdentifierFromMethodCallParts(methodCallParts), variables);
                if (prefixType !== null && typeof prefixType !== 'undefined') {
                    possibleMethods = this.possibleFieldsForType(prefixType)[methodIdentifier.text];
                    // We don't know the valid methods for this type by name
                    if (typeof possibleMethods === 'undefined') return null;

                    method = this.methodFromPossibleMethods(methodCallParts, possibleMethods);
                    return this.typeFromFullPackageId(method.type);
                }
            } else if (!resolveAgainstThis && currentTokens[index - 1].type === DOT && currentTokens[index - 2].type === Identifier) {
                methodCallParts = this._getPartsOfMethodCall(currentTokens[index - 2]);
                prefixType = this.detectTypeForToken(this.methodIdentifierFromMethodCallParts(methodCallParts), variables);
                if (prefixType !== null && typeof prefixType !== 'undefined') {
                    possibleMethods = this.possibleFieldsForType(prefixType)[methodIdentifier.text];
                    // If true, we don't know the valid methods for this type by name
                    if (typeof possibleMethods === 'undefined') return null;

                    method = this.methodFromPossibleMethods(methodCallParts, possibleMethods);
                    return this.typeFromFullPackageId(method.type);
                }
            } else if (resolveAgainstThis) {
                prefixType = this.thisType;
                methodCallParts = this._getPartsOfMethodCall(currentTokens[index - 2]);
                possibleMethods = this.possibleFieldsForType(prefixType)[methodIdentifier.text];
                // We don't know the valid methods for this type by name
                if (typeof possibleMethods === 'undefined') return null;
                possibleMethods = this.possibleFieldsForType(prefixType)[methodIdentifier.text];
                method = this.methodIdentifierFromMethodCallParts(methodCallParts, possibleMethods);
                return this.typeFromFullPackageId(method.type);
            } else {
                // return this.detectTypeForToken(methodIdentifier, variables);
                return null;
            }
        } else if (token.type === Identifier) {
            if (this.currentTokens[token.index - 1].type === DOT) { // This should a field of a type
                prefixType = this.detectTypeForToken(this.currentTokens[token.index - 2], variables);
                // This is a field of the type found in prefixType, because we have verified that this is not a method, so what else can it be?
                const instance = this;
                if (prefixType !== null && typeof prefixType !== 'undefined') {
                    var field = this.possibleFieldsForType(prefixType)[token.text];
                    if (field) {
                        return this.typeFromFullPackageId(field.type);
                    } else {
                        const possibleSubclasses = this.autoImport.knownTypeIds.filter(function (t) {
                            return (t.indexOf(prefixType.name + '$') === 0 || t.indexOf('$' + prefixType.name + '$') > 0) &&
                                t.lastIndexOf('$' + token.text) === t.lastIndexOf('$');
                        });

                        if (possibleSubclasses.length > 1) console.warn('Multiple subclasses possible for ' + prefixType.name + '.' + token.text);
                        if (possibleSubclasses[0])
                            return instance.typeForClassName(possibleSubclasses[0]);
                    }
                } else {
                    return null;
                }
            } else {
                var searchElement = token.text;
                var matchingVariables = variables.filter(function (value) {
                    return value.id === token.text;
                });

                var maxLevel = 0;
                matchingVariables.forEach(function (variable) {
                   if (variable.level > maxLevel) maxLevel = variable.level;
                });

                matchingVariables = matchingVariables.filter(function (variable) {
                   return maxLevel === variable.level;
                });
                // There should only be one left with proper Java
                if (matchingVariables.length > 0) {
                    searchElement = matchingVariables[0].type;
                    if (this.autoImport.knownTypeIds.indexOf(searchElement) >= 0) {
                        return this.typeForClassName(searchElement);
                    } else {
                        return null;
                    }
                }

                const field = this.possibleFieldsForType(this.thisType)[token.text];
                if (field) {
                    return this.typeFromFullClassName(field.type);
                }

                var lastAttempt = this.typeForClassName(token.text) || this.typeFromFullClassName(token.text);
                if (lastAttempt) {
                    return lastAttempt;
                }
            }
        } else if (token.type === 'THIS') {
            return this.thisType;
        } else if (token.type === 'SUPER') {
            return this.typeFromFullClassName(this.thisType.parentClass);
        }

        return null;
    };

    FtcLangTools.prototype.detectTypeUnderCursor = function () {
        const variables = this.validVariablesUnderCursor();
        const tokenUnderCursor = this.tokenUnderCursor();
        return this.detectTypeForToken(tokenUnderCursor, variables);
    };

    FtcLangTools.prototype.findMatchingToken = function (token) {
        var depth = 0;
        var selectedToken = null;
        var i = token.index;
        var left = token.type.charAt(0) === 'L';
        // We need to swap left and right to get the matching element
        var matchTo = (left ? 'R' : 'L') + token.type.substr(1);

        if (left) {
            for (; i < this.currentTokens.length; i++) {
                selectedToken = this.currentTokens[i];
                if (selectedToken.type === matchTo) {
                    ++depth;
                } else if (selectedToken.type === matchTo) {
                    if (depth === 0) break;
                    --depth;
                }
            }
        } else {
            for (; i >= 0; i--) {
                selectedToken = this.currentTokens[i];
                if (selectedToken.type === matchTo) {
                    if (depth === 0) break;
                    --depth;
                } else if (selectedToken.type === matchTo) {
                    ++depth;
                }
            }
        }

        return selectedToken;
    };

    FtcLangTools.prototype.isPartOfMethod = function (startToken) {
        const index = startToken.index;
        const currentTokens = this.currentTokens;
        var matching;

        if (startToken.type === Identifier) {
            if (index - 1 < 0) return false;
            if (currentTokens[index - 1].type === DOT && currentTokens[index + 1].type === LPAREN) {
                matching = this.findMatchingToken(currentTokens[index + 1]);
                return matching !== null;
            }
        }

        if (startToken.type === LPAREN) {
            if (index - 2 < 0) return false;
            if (currentTokens[index - 2].type === DOT && currentTokens[index - 1].type === Identifier) {
                matching = this.findMatchingToken(currentTokens[index]);
                return matching !== null;
            }
        }


        if (startToken.type === RPAREN) {
            matching = this.findMatchingToken(currentTokens[index]);
            if (matching !== null) {
                var matchedIndex = matching.index;
                if (matchedIndex - 2 < 0) return false;
                return currentTokens[matchedIndex - 1].type === Identifier && currentTokens[matchedIndex - 2].type === DOT;

            }
        }

        return false;
    };

    FtcLangTools.prototype.methodIdentifierFor = function (startToken) {
        var partsOfMethod = this._getPartsOfMethodCall(startToken);
        return this.methodIdentifierFromMethodCallParts(partsOfMethod);
    };

    FtcLangTools.prototype.methodIdentifierFromMethodCallParts = function (methodCall) {
        if (methodCall.identifier !== null && methodCall.identifier.type === Identifier) {
            return methodCall.identifier;
        }

        return null;
    };

    FtcLangTools.prototype._getPartsOfMethodCall = function (startToken) {
        const index = startToken.index;
        const currentTokens = this.currentTokens;
        var matching;
        var result = {
            identifier: null,
            lparen: null,
            params: [],
            rparen: null
        };

        if (startToken.type === Identifier) {
            matching = this.findMatchingToken(currentTokens[index + 1]);
            result.identifier = startToken;
            result.lparen = currentTokens[index + 1];
            result.rparen = matching;
        }

        if (startToken.type === LPAREN) {
            if (index - 2 < 0) return false;
            if (currentTokens[index - 2].type === DOT && currentTokens[index - 1].type === Identifier) {
                matching = this.findMatchingToken(currentTokens[index]);
                if (matching.index < 1) return result;
                result.identifier = currentTokens[index - 1];
                result.lparen = startToken;
                result.rparen = matching;
            }
        }

        if (startToken.type === RPAREN) {
            matching = this.findMatchingToken(currentTokens[index]);
            if (matching !== null) {
                if (matching.index < 1) return result;
                result.identifier = currentTokens[matching.index - 1];
                result.lparen = matching;
                result.rparen = startToken;
            }
        }

        return result;
    };
    FtcLangTools.prototype.getArgumentTypesFromMethod = function (methodCallLparen, methodCallRparen) {
        var paramTypes = [];
        for (var i = methodCallLparen.index + 1; i < methodCallRparen.index && i < this.currentTokens.length; i++)  {
            if (this.currentTokens[i + 1].type === COMMA || this.currentTokens[i + 1] === methodCallRparen) {
                paramTypes.push(this.detectTypeForToken(token));
            }
        }

        return paramTypes;
    };

    function sanitizePrimitiveTypes(fullClassName) {
        if (fullClassName === 'void') return null;
        var classNameToUse = fullClassName;
        var primitive = (['int', 'boolean', 'long', 'float', 'double', 'byte', 'short', 'char'].indexOf(fullClassName) >= 0);
        if (primitive) {
            switch (fullClassName) {
                case 'int':
                    classNameToUse = 'Integer';
                    break;
                case 'boolean':
                    classNameToUse = 'Boolean';
                    break;
                case 'long':
                    classNameToUse = 'Long';
                    break;
                case 'float':
                    classNameToUse = 'Float';
                    break;
                case 'double':
                    classNameToUse = 'Double';
                    break;
                case 'byte':
                    classNameToUse = 'Byte';
                    break;
                case 'short':
                    classNameToUse = 'Short';
                    break;
                case 'char':
                    classNameToUse = 'Character';
                    break;
            }
        }
        return {classNameToUse: classNameToUse, primitive: primitive};
    }

    FtcLangTools.prototype.fullClassNameFromType = function (type) {
        checkArgs(type, 'type');
        if (type.packageName === '' || !type.packageName) return type.name;
        return type.packageName + '.' + type.name;
    };

    FtcLangTools.prototype.isFullPackageName = function isFullPackageName(classNameToUse) {
        checkArgs(classNameToUse, 'classNameToUse');
        return classNameToUse.indexOf('.') >= 0;
    };

    FtcLangTools.prototype.typeFromFullPackageId = function (fullClassName) {
        checkArgs(fullClassName, 'fullClassName');
        var className;
        var packageName;
        var primitiveCheck = sanitizePrimitiveTypes(fullClassName);
        if (primitiveCheck === null) return null;
        var classNameToUse = primitiveCheck.classNameToUse;
        var primitive = primitiveCheck.primitive;

        if (this.isFullPackageName(classNameToUse)) {
            className = this.classNameFromFullClassName(classNameToUse);
            packageName = this.packageNameFromFullClassName(classNameToUse);
        } else {
            className = classNameToUse;
            // All Java primitive types are in the java.lang package
            if (primitive) {
                packageName = 'java.lang';
            } else {
                packageName = '';
            }
        }

        var possibleClasses = this.autoImport.classToPackageMap[className];
        if (!possibleClasses) {
            console.log('Failed to lookup class "' + className + '". Please open an issue, if you think it should be resolved');
            return null;
        }

        for (var i = 0; i < possibleClasses.length; i++) {
            if (possibleClasses[i].packageName === packageName) {
                var correctClass = possibleClasses[i];
                if (primitive) {
                    if (!correctClass.primitiveClass) {
                        correctClass.primitiveClass = {
                            methods: correctClass.methods,
                            fields: [],
                            primitive: true,
                            packageName: 'java.lang',
                            name: fullClassName,
                            modifier: 'PUBLIC'
                        };
                    }

                    return correctClass.primitiveClass;
                }

                return correctClass;
            }
        }

        return undefined;
    };
    FtcLangTools.prototype.typeFromPossibleTypes = function (possibleTypes, className, packageName) {
        checkArgs(possibleTypes, 'possibleTypes');
        checkArgs(className, 'className');

        if (!packageName && packageName !== '') {
            var importedClassPackagesNames = {};
            var instance = this;
            this.currentImports.forEach(function (importClass) {
                importedClassPackagesNames[instance.classNameFromFullClassName(importClass)] = instance.packageNameFromFullClassName(importClass);
            });

            const $index = className.indexOf('$');
            packageName = importedClassPackagesNames[$index >= 0 ? className.substr(0, $index) : className];
        }

        if (!packageName) {
            return this.typeFromPossibleTypes(this.autoImport.classToPackageMap[className], className, 'java.lang');
        }

        for (var i = 0; i < possibleTypes.length; i++) {
            const possibleType = possibleTypes[i];
            if (possibleType.packageName === packageName) {
                return possibleType;
            }
        }

        return null;
    };
    FtcLangTools.prototype.typeForClassName = function (className) {
        checkArgs(className, 'className');
        var primitiveCheck = sanitizePrimitiveTypes(className);
        if (primitiveCheck === null) return null;
        if (className.indexOf(']') >= 0) {
            return {
                fields: {
                    length: {
                        modifier: 'PUBLIC',
                        type: 'int'
                    }
                },
                interfaces: [],
                methods: {},
                modifier: 'PUBLIC',
                name: className,
                packageName: 'java.lang',
                parentClass: 'java.lang.Object'
            };
        }
        return this.typeFromPossibleTypes(this.autoImport.classToPackageMap[primitiveCheck.classNameToUse], primitiveCheck.classNameToUse);
    };

    // an alias todo: remove
    FtcLangTools.prototype.typeFromFullClassName = FtcLangTools.prototype.typeFromFullPackageId;

    FtcLangTools.prototype.possibleMethodsForClassName = function (className) {
        return this.possibleMethodsForType(this.typeForClassName(className));
    };

    FtcLangTools.prototype.possibleMethodsForType = function (type) {
        if (type !== this.thisType && this.cacheForType(type).methods) {
            return this.cacheForType(type).methods;
        }

        var parentType = this.typeFromFullClassName(type.parentClass);
        if (type === this.typeFromFullClassName('java.lang.Object')) {
            return type.methods;
        }

        var parentMethods = this.possibleMethodsForType(parentType);
        var instance = this;
        var usedInterfaces = type.interfaces.map(function (value) {
            return instance.possibleMethodsForType(instance.typeFromFullClassName(value));
        });

        var addElementIfMissing = function (array1, element) {
            if (array1.filter(function (value) {
                    return value === element;
                }).length === 0) {
                array1.push(element);
            }
        };

        var possibleMethods = {};
        var method;
        for (method in type.methods) {
            if (!type.methods.hasOwnProperty(method)) continue;
            possibleMethods[method] = type.methods[method];
        }

        for (method in parentMethods) {
            if (!parentMethods.hasOwnProperty(method)) continue;
            if (Array.isArray(possibleMethods[method])) {
                parentMethods[method].forEach(function (value) {
                    addElementIfMissing(possibleMethods[method], value);
                });
            } else {
                possibleMethods[method] = parentMethods[method];
            }
        }

        usedInterfaces.forEach(function (usedInterface) {
            for (method in usedInterface) {
                if (!usedInterface.hasOwnProperty(method)) continue;
                if (typeof possibleMethods[method] === 'undefined') {
                    possibleMethods[method] = usedInterface[method];
                } else {
                    usedInterface[method].forEach(function (value) {
                        addElementIfMissing(possibleMethods[method], value);
                    });
                }
            }
        });

        // Never cache the object we are working on
        if (this.thisType !== type)
            this.cacheForType(type).methods = possibleMethods;

        return possibleMethods;
    };

    FtcLangTools.prototype.possibleMethodsOfSuperForType = function (type) {
        return this.possibleMethodsForClassName(type.parentClass);
    };

    FtcLangTools.prototype.possibleFieldsForClassName = function (className) {
        return this.possibleFieldsForType(this.typeForClassName(className));
    };

    FtcLangTools.prototype.possibleFieldsForType = function (type) {
        if (type !== this.thisType && this.cacheForType(type).fields) {
            return this.cacheForType(type).fields;
        }

        var parentType = this.typeFromFullClassName(type.parentClass);
        if (type === this.typeFromFullClassName('java.lang.Object')) {
            return type.fields;
        }

        var parentFields = this.possibleFieldsForType(parentType);
        var instance = this;
        var usedInterfaces = type.interfaces.map(function (value) {
            return instance.possibleFieldsForType(instance.typeFromFullClassName(value));
        });

        var fields = {};
        var field;

        usedInterfaces.forEach(function (usedInterface) {
            for (field in usedInterface) {
                if (!usedInterface.hasOwnProperty(field)) continue;
                fields[field] = usedInterface[field];
            }
        });

        for (field in parentFields) {
            if (!parentFields.hasOwnProperty(field)) continue;
            fields[field] = parentFields[field];
        }

        for (field in type.fields) {
            if (!type.fields.hasOwnProperty(field)) continue;
            fields[field] = type.fields[field];
        }

        // Never cache the object we are working on
        if (this.thisType !== type)
            this.cacheForType(type).fields = fields;

        return fields;
    };

    FtcLangTools.prototype.possibleFieldsOfSuperForType = function (type) {
        return this.possibleFieldsForClassName(type.parentClass);
    };

    FtcLangTools.prototype.packageNameFromFullClassName = function (fullClassName) {
        if (this.isFullPackageName(fullClassName)) {
            return fullClassName.substring(0, fullClassName.lastIndexOf('.'));
        } else {
            return '';
        }
    };

    FtcLangTools.prototype.classNameFromFullClassName = function (fullClassName) {
        if (this.isFullPackageName(fullClassName)) {
            return fullClassName.substr(fullClassName.lastIndexOf('.') + 1);
        } else {
            return fullClassName;
        }
    };

    FtcLangTools.prototype.isTypeInstanceOf = function (testType, testParentType) {
        if (testType === testParentType) return true;

        var parentType = this.typeFromFullClassName(testType.parentClass);
        if (parentType === testParentType) {
            return true;
        } else if (this.isTypeInstanceOf(parentType, testParentType)) {
            return true;
        } else {
            var instance = this;
            var usedInterfaces = type.interfaces.map(function (value) {
                return instance.possibleFieldsForType(instance.typeFromFullClassName(value));
            });
            for (var i = 0; i < usedInterfaces.length; i++) {
                if (this.isTypeInstanceOf(usedInterfaces[i])) return true;
            }
        }

        return false;
    };

    FtcLangTools.prototype.updateThisProps = function (superClassName, interfaceNames, methods) {
        checkArgs(superClassName, 'superClassName');
        checkArgs(interfaceNames, 'interfaceNames');
        checkArgs(methods, 'methods');

        var thisFields = {};
        const instance = this;
        this.currentVariables.variables.filter(function (variable) {
            return variable.decType === 'field';
        }).forEach(function (value) {
            var type = instance.typeForClassName(value.type);
            thisFields[value.id] = {
                type: type.packageName + '.' + type.name,
                name: value.id,
                modifier: 'PUBLIC'
            };
        });
        this.thisType.fields = thisFields;
        this.thisType.packageName = this.currentPackageName;
        this.thisType.name = this.currentClassName;
        this.thisType.parentClass = this.fullClassNameFromType(this.typeForClassName(superClassName));

        this.thisType.interfaces = interfaceNames.map(function (t) {
            var type = instance.typeForClassName(t);
            return instance.fullClassNameFromType(type);
        });

        var thisMethods = {};
        for (var method in methods) {
            if (!methods.hasOwnProperty(method)) continue;
            thisMethods[method] = methods[method].map(function (t) {
                if (t.type !== 'void') {
                    var type = instance.typeForClassName(t.type);
                    t.type = instance.fullClassNameFromType(type);
                }

                return t;
            });
        }

        this.thisType.methods = thisMethods;

        // For some reason, our autocompleter needs to be manually re-called more often then it actually does, so we force it to update
        if (env.editor.completer && env.editor.completer.activated) {
            env.editor.completer.updateCompletions();
        }
    };

    FtcLangTools.prototype.cacheForType = function (type) {
        const key = this.fullClassNameFromType(type);
        if (this._cache.hasOwnProperty(key)) {
            if (typeof this._cache[key] !== 'object') {
              this._cache[key] = {};
            }
        } else {
            this._cache[key] = {};
        }

        return this._cache[key];
    };

    return FtcLangTools;
})();
