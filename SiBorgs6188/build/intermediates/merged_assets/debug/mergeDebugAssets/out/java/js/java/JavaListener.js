// Generated from .\Java.g4 by ANTLR 4.7
// jshint ignore: start
var antlr4 = require('../antlr4/index');

// This class defines a complete listener for a parse tree produced by JavaParser.
function JavaListener() {
	antlr4.tree.ParseTreeListener.call(this);
	return this;
}

JavaListener.prototype = Object.create(antlr4.tree.ParseTreeListener.prototype);
JavaListener.prototype.constructor = JavaListener;

// Enter a parse tree produced by JavaParser#compilationUnit.
JavaListener.prototype.enterCompilationUnit = function(ctx) {
};

// Exit a parse tree produced by JavaParser#compilationUnit.
JavaListener.prototype.exitCompilationUnit = function(ctx) {
};


// Enter a parse tree produced by JavaParser#packageDeclaration.
JavaListener.prototype.enterPackageDeclaration = function(ctx) {
};

// Exit a parse tree produced by JavaParser#packageDeclaration.
JavaListener.prototype.exitPackageDeclaration = function(ctx) {
};


// Enter a parse tree produced by JavaParser#importDeclaration.
JavaListener.prototype.enterImportDeclaration = function(ctx) {
};

// Exit a parse tree produced by JavaParser#importDeclaration.
JavaListener.prototype.exitImportDeclaration = function(ctx) {
};


// Enter a parse tree produced by JavaParser#typeDeclaration.
JavaListener.prototype.enterTypeDeclaration = function(ctx) {
};

// Exit a parse tree produced by JavaParser#typeDeclaration.
JavaListener.prototype.exitTypeDeclaration = function(ctx) {
};


// Enter a parse tree produced by JavaParser#modifier.
JavaListener.prototype.enterModifier = function(ctx) {
};

// Exit a parse tree produced by JavaParser#modifier.
JavaListener.prototype.exitModifier = function(ctx) {
};


// Enter a parse tree produced by JavaParser#classOrInterfaceModifier.
JavaListener.prototype.enterClassOrInterfaceModifier = function(ctx) {
};

// Exit a parse tree produced by JavaParser#classOrInterfaceModifier.
JavaListener.prototype.exitClassOrInterfaceModifier = function(ctx) {
};


// Enter a parse tree produced by JavaParser#variableModifier.
JavaListener.prototype.enterVariableModifier = function(ctx) {
};

// Exit a parse tree produced by JavaParser#variableModifier.
JavaListener.prototype.exitVariableModifier = function(ctx) {
};


// Enter a parse tree produced by JavaParser#classDeclaration.
JavaListener.prototype.enterClassDeclaration = function(ctx) {
};

// Exit a parse tree produced by JavaParser#classDeclaration.
JavaListener.prototype.exitClassDeclaration = function(ctx) {
};


// Enter a parse tree produced by JavaParser#typeParameters.
JavaListener.prototype.enterTypeParameters = function(ctx) {
};

// Exit a parse tree produced by JavaParser#typeParameters.
JavaListener.prototype.exitTypeParameters = function(ctx) {
};


// Enter a parse tree produced by JavaParser#typeParameter.
JavaListener.prototype.enterTypeParameter = function(ctx) {
};

// Exit a parse tree produced by JavaParser#typeParameter.
JavaListener.prototype.exitTypeParameter = function(ctx) {
};


// Enter a parse tree produced by JavaParser#typeBound.
JavaListener.prototype.enterTypeBound = function(ctx) {
};

// Exit a parse tree produced by JavaParser#typeBound.
JavaListener.prototype.exitTypeBound = function(ctx) {
};


// Enter a parse tree produced by JavaParser#enumDeclaration.
JavaListener.prototype.enterEnumDeclaration = function(ctx) {
};

// Exit a parse tree produced by JavaParser#enumDeclaration.
JavaListener.prototype.exitEnumDeclaration = function(ctx) {
};


// Enter a parse tree produced by JavaParser#enumConstants.
JavaListener.prototype.enterEnumConstants = function(ctx) {
};

// Exit a parse tree produced by JavaParser#enumConstants.
JavaListener.prototype.exitEnumConstants = function(ctx) {
};


// Enter a parse tree produced by JavaParser#enumConstant.
JavaListener.prototype.enterEnumConstant = function(ctx) {
};

// Exit a parse tree produced by JavaParser#enumConstant.
JavaListener.prototype.exitEnumConstant = function(ctx) {
};


// Enter a parse tree produced by JavaParser#enumBodyDeclarations.
JavaListener.prototype.enterEnumBodyDeclarations = function(ctx) {
};

// Exit a parse tree produced by JavaParser#enumBodyDeclarations.
JavaListener.prototype.exitEnumBodyDeclarations = function(ctx) {
};


// Enter a parse tree produced by JavaParser#interfaceDeclaration.
JavaListener.prototype.enterInterfaceDeclaration = function(ctx) {
};

// Exit a parse tree produced by JavaParser#interfaceDeclaration.
JavaListener.prototype.exitInterfaceDeclaration = function(ctx) {
};


// Enter a parse tree produced by JavaParser#typeList.
JavaListener.prototype.enterTypeList = function(ctx) {
};

// Exit a parse tree produced by JavaParser#typeList.
JavaListener.prototype.exitTypeList = function(ctx) {
};


// Enter a parse tree produced by JavaParser#classBody.
JavaListener.prototype.enterClassBody = function(ctx) {
};

// Exit a parse tree produced by JavaParser#classBody.
JavaListener.prototype.exitClassBody = function(ctx) {
};


// Enter a parse tree produced by JavaParser#interfaceBody.
JavaListener.prototype.enterInterfaceBody = function(ctx) {
};

// Exit a parse tree produced by JavaParser#interfaceBody.
JavaListener.prototype.exitInterfaceBody = function(ctx) {
};


// Enter a parse tree produced by JavaParser#classBodyDeclaration.
JavaListener.prototype.enterClassBodyDeclaration = function(ctx) {
};

// Exit a parse tree produced by JavaParser#classBodyDeclaration.
JavaListener.prototype.exitClassBodyDeclaration = function(ctx) {
};


// Enter a parse tree produced by JavaParser#memberDeclaration.
JavaListener.prototype.enterMemberDeclaration = function(ctx) {
};

// Exit a parse tree produced by JavaParser#memberDeclaration.
JavaListener.prototype.exitMemberDeclaration = function(ctx) {
};


// Enter a parse tree produced by JavaParser#methodDeclaration.
JavaListener.prototype.enterMethodDeclaration = function(ctx) {
};

// Exit a parse tree produced by JavaParser#methodDeclaration.
JavaListener.prototype.exitMethodDeclaration = function(ctx) {
};


// Enter a parse tree produced by JavaParser#genericMethodDeclaration.
JavaListener.prototype.enterGenericMethodDeclaration = function(ctx) {
};

// Exit a parse tree produced by JavaParser#genericMethodDeclaration.
JavaListener.prototype.exitGenericMethodDeclaration = function(ctx) {
};


// Enter a parse tree produced by JavaParser#constructorDeclaration.
JavaListener.prototype.enterConstructorDeclaration = function(ctx) {
};

// Exit a parse tree produced by JavaParser#constructorDeclaration.
JavaListener.prototype.exitConstructorDeclaration = function(ctx) {
};


// Enter a parse tree produced by JavaParser#genericConstructorDeclaration.
JavaListener.prototype.enterGenericConstructorDeclaration = function(ctx) {
};

// Exit a parse tree produced by JavaParser#genericConstructorDeclaration.
JavaListener.prototype.exitGenericConstructorDeclaration = function(ctx) {
};


// Enter a parse tree produced by JavaParser#fieldDeclaration.
JavaListener.prototype.enterFieldDeclaration = function(ctx) {
};

// Exit a parse tree produced by JavaParser#fieldDeclaration.
JavaListener.prototype.exitFieldDeclaration = function(ctx) {
};


// Enter a parse tree produced by JavaParser#interfaceBodyDeclaration.
JavaListener.prototype.enterInterfaceBodyDeclaration = function(ctx) {
};

// Exit a parse tree produced by JavaParser#interfaceBodyDeclaration.
JavaListener.prototype.exitInterfaceBodyDeclaration = function(ctx) {
};


// Enter a parse tree produced by JavaParser#interfaceMemberDeclaration.
JavaListener.prototype.enterInterfaceMemberDeclaration = function(ctx) {
};

// Exit a parse tree produced by JavaParser#interfaceMemberDeclaration.
JavaListener.prototype.exitInterfaceMemberDeclaration = function(ctx) {
};


// Enter a parse tree produced by JavaParser#constDeclaration.
JavaListener.prototype.enterConstDeclaration = function(ctx) {
};

// Exit a parse tree produced by JavaParser#constDeclaration.
JavaListener.prototype.exitConstDeclaration = function(ctx) {
};


// Enter a parse tree produced by JavaParser#constantDeclarator.
JavaListener.prototype.enterConstantDeclarator = function(ctx) {
};

// Exit a parse tree produced by JavaParser#constantDeclarator.
JavaListener.prototype.exitConstantDeclarator = function(ctx) {
};


// Enter a parse tree produced by JavaParser#interfaceMethodDeclaration.
JavaListener.prototype.enterInterfaceMethodDeclaration = function(ctx) {
};

// Exit a parse tree produced by JavaParser#interfaceMethodDeclaration.
JavaListener.prototype.exitInterfaceMethodDeclaration = function(ctx) {
};


// Enter a parse tree produced by JavaParser#genericInterfaceMethodDeclaration.
JavaListener.prototype.enterGenericInterfaceMethodDeclaration = function(ctx) {
};

// Exit a parse tree produced by JavaParser#genericInterfaceMethodDeclaration.
JavaListener.prototype.exitGenericInterfaceMethodDeclaration = function(ctx) {
};


// Enter a parse tree produced by JavaParser#variableDeclarators.
JavaListener.prototype.enterVariableDeclarators = function(ctx) {
};

// Exit a parse tree produced by JavaParser#variableDeclarators.
JavaListener.prototype.exitVariableDeclarators = function(ctx) {
};


// Enter a parse tree produced by JavaParser#variableDeclarator.
JavaListener.prototype.enterVariableDeclarator = function(ctx) {
};

// Exit a parse tree produced by JavaParser#variableDeclarator.
JavaListener.prototype.exitVariableDeclarator = function(ctx) {
};


// Enter a parse tree produced by JavaParser#variableDeclaratorId.
JavaListener.prototype.enterVariableDeclaratorId = function(ctx) {
};

// Exit a parse tree produced by JavaParser#variableDeclaratorId.
JavaListener.prototype.exitVariableDeclaratorId = function(ctx) {
};


// Enter a parse tree produced by JavaParser#variableInitializer.
JavaListener.prototype.enterVariableInitializer = function(ctx) {
};

// Exit a parse tree produced by JavaParser#variableInitializer.
JavaListener.prototype.exitVariableInitializer = function(ctx) {
};


// Enter a parse tree produced by JavaParser#arrayInitializer.
JavaListener.prototype.enterArrayInitializer = function(ctx) {
};

// Exit a parse tree produced by JavaParser#arrayInitializer.
JavaListener.prototype.exitArrayInitializer = function(ctx) {
};


// Enter a parse tree produced by JavaParser#enumConstantName.
JavaListener.prototype.enterEnumConstantName = function(ctx) {
};

// Exit a parse tree produced by JavaParser#enumConstantName.
JavaListener.prototype.exitEnumConstantName = function(ctx) {
};


// Enter a parse tree produced by JavaParser#typeType.
JavaListener.prototype.enterTypeType = function(ctx) {
};

// Exit a parse tree produced by JavaParser#typeType.
JavaListener.prototype.exitTypeType = function(ctx) {
};


// Enter a parse tree produced by JavaParser#classOrInterfaceType.
JavaListener.prototype.enterClassOrInterfaceType = function(ctx) {
};

// Exit a parse tree produced by JavaParser#classOrInterfaceType.
JavaListener.prototype.exitClassOrInterfaceType = function(ctx) {
};


// Enter a parse tree produced by JavaParser#primitiveType.
JavaListener.prototype.enterPrimitiveType = function(ctx) {
};

// Exit a parse tree produced by JavaParser#primitiveType.
JavaListener.prototype.exitPrimitiveType = function(ctx) {
};


// Enter a parse tree produced by JavaParser#typeArguments.
JavaListener.prototype.enterTypeArguments = function(ctx) {
};

// Exit a parse tree produced by JavaParser#typeArguments.
JavaListener.prototype.exitTypeArguments = function(ctx) {
};


// Enter a parse tree produced by JavaParser#typeArgument.
JavaListener.prototype.enterTypeArgument = function(ctx) {
};

// Exit a parse tree produced by JavaParser#typeArgument.
JavaListener.prototype.exitTypeArgument = function(ctx) {
};


// Enter a parse tree produced by JavaParser#qualifiedNameList.
JavaListener.prototype.enterQualifiedNameList = function(ctx) {
};

// Exit a parse tree produced by JavaParser#qualifiedNameList.
JavaListener.prototype.exitQualifiedNameList = function(ctx) {
};


// Enter a parse tree produced by JavaParser#formalParameters.
JavaListener.prototype.enterFormalParameters = function(ctx) {
};

// Exit a parse tree produced by JavaParser#formalParameters.
JavaListener.prototype.exitFormalParameters = function(ctx) {
};


// Enter a parse tree produced by JavaParser#formalParameterList.
JavaListener.prototype.enterFormalParameterList = function(ctx) {
};

// Exit a parse tree produced by JavaParser#formalParameterList.
JavaListener.prototype.exitFormalParameterList = function(ctx) {
};


// Enter a parse tree produced by JavaParser#formalParameter.
JavaListener.prototype.enterFormalParameter = function(ctx) {
};

// Exit a parse tree produced by JavaParser#formalParameter.
JavaListener.prototype.exitFormalParameter = function(ctx) {
};


// Enter a parse tree produced by JavaParser#lastFormalParameter.
JavaListener.prototype.enterLastFormalParameter = function(ctx) {
};

// Exit a parse tree produced by JavaParser#lastFormalParameter.
JavaListener.prototype.exitLastFormalParameter = function(ctx) {
};


// Enter a parse tree produced by JavaParser#methodBody.
JavaListener.prototype.enterMethodBody = function(ctx) {
};

// Exit a parse tree produced by JavaParser#methodBody.
JavaListener.prototype.exitMethodBody = function(ctx) {
};


// Enter a parse tree produced by JavaParser#constructorBody.
JavaListener.prototype.enterConstructorBody = function(ctx) {
};

// Exit a parse tree produced by JavaParser#constructorBody.
JavaListener.prototype.exitConstructorBody = function(ctx) {
};


// Enter a parse tree produced by JavaParser#qualifiedName.
JavaListener.prototype.enterQualifiedName = function(ctx) {
};

// Exit a parse tree produced by JavaParser#qualifiedName.
JavaListener.prototype.exitQualifiedName = function(ctx) {
};


// Enter a parse tree produced by JavaParser#literal.
JavaListener.prototype.enterLiteral = function(ctx) {
};

// Exit a parse tree produced by JavaParser#literal.
JavaListener.prototype.exitLiteral = function(ctx) {
};


// Enter a parse tree produced by JavaParser#annotation.
JavaListener.prototype.enterAnnotation = function(ctx) {
};

// Exit a parse tree produced by JavaParser#annotation.
JavaListener.prototype.exitAnnotation = function(ctx) {
};


// Enter a parse tree produced by JavaParser#annotationName.
JavaListener.prototype.enterAnnotationName = function(ctx) {
};

// Exit a parse tree produced by JavaParser#annotationName.
JavaListener.prototype.exitAnnotationName = function(ctx) {
};


// Enter a parse tree produced by JavaParser#elementValuePairs.
JavaListener.prototype.enterElementValuePairs = function(ctx) {
};

// Exit a parse tree produced by JavaParser#elementValuePairs.
JavaListener.prototype.exitElementValuePairs = function(ctx) {
};


// Enter a parse tree produced by JavaParser#elementValuePair.
JavaListener.prototype.enterElementValuePair = function(ctx) {
};

// Exit a parse tree produced by JavaParser#elementValuePair.
JavaListener.prototype.exitElementValuePair = function(ctx) {
};


// Enter a parse tree produced by JavaParser#elementValue.
JavaListener.prototype.enterElementValue = function(ctx) {
};

// Exit a parse tree produced by JavaParser#elementValue.
JavaListener.prototype.exitElementValue = function(ctx) {
};


// Enter a parse tree produced by JavaParser#elementValueArrayInitializer.
JavaListener.prototype.enterElementValueArrayInitializer = function(ctx) {
};

// Exit a parse tree produced by JavaParser#elementValueArrayInitializer.
JavaListener.prototype.exitElementValueArrayInitializer = function(ctx) {
};


// Enter a parse tree produced by JavaParser#annotationTypeDeclaration.
JavaListener.prototype.enterAnnotationTypeDeclaration = function(ctx) {
};

// Exit a parse tree produced by JavaParser#annotationTypeDeclaration.
JavaListener.prototype.exitAnnotationTypeDeclaration = function(ctx) {
};


// Enter a parse tree produced by JavaParser#annotationTypeBody.
JavaListener.prototype.enterAnnotationTypeBody = function(ctx) {
};

// Exit a parse tree produced by JavaParser#annotationTypeBody.
JavaListener.prototype.exitAnnotationTypeBody = function(ctx) {
};


// Enter a parse tree produced by JavaParser#annotationTypeElementDeclaration.
JavaListener.prototype.enterAnnotationTypeElementDeclaration = function(ctx) {
};

// Exit a parse tree produced by JavaParser#annotationTypeElementDeclaration.
JavaListener.prototype.exitAnnotationTypeElementDeclaration = function(ctx) {
};


// Enter a parse tree produced by JavaParser#annotationTypeElementRest.
JavaListener.prototype.enterAnnotationTypeElementRest = function(ctx) {
};

// Exit a parse tree produced by JavaParser#annotationTypeElementRest.
JavaListener.prototype.exitAnnotationTypeElementRest = function(ctx) {
};


// Enter a parse tree produced by JavaParser#annotationMethodOrConstantRest.
JavaListener.prototype.enterAnnotationMethodOrConstantRest = function(ctx) {
};

// Exit a parse tree produced by JavaParser#annotationMethodOrConstantRest.
JavaListener.prototype.exitAnnotationMethodOrConstantRest = function(ctx) {
};


// Enter a parse tree produced by JavaParser#annotationMethodRest.
JavaListener.prototype.enterAnnotationMethodRest = function(ctx) {
};

// Exit a parse tree produced by JavaParser#annotationMethodRest.
JavaListener.prototype.exitAnnotationMethodRest = function(ctx) {
};


// Enter a parse tree produced by JavaParser#annotationConstantRest.
JavaListener.prototype.enterAnnotationConstantRest = function(ctx) {
};

// Exit a parse tree produced by JavaParser#annotationConstantRest.
JavaListener.prototype.exitAnnotationConstantRest = function(ctx) {
};


// Enter a parse tree produced by JavaParser#defaultValue.
JavaListener.prototype.enterDefaultValue = function(ctx) {
};

// Exit a parse tree produced by JavaParser#defaultValue.
JavaListener.prototype.exitDefaultValue = function(ctx) {
};


// Enter a parse tree produced by JavaParser#block.
JavaListener.prototype.enterBlock = function(ctx) {
};

// Exit a parse tree produced by JavaParser#block.
JavaListener.prototype.exitBlock = function(ctx) {
};


// Enter a parse tree produced by JavaParser#blockStatement.
JavaListener.prototype.enterBlockStatement = function(ctx) {
};

// Exit a parse tree produced by JavaParser#blockStatement.
JavaListener.prototype.exitBlockStatement = function(ctx) {
};


// Enter a parse tree produced by JavaParser#localVariableDeclarationStatement.
JavaListener.prototype.enterLocalVariableDeclarationStatement = function(ctx) {
};

// Exit a parse tree produced by JavaParser#localVariableDeclarationStatement.
JavaListener.prototype.exitLocalVariableDeclarationStatement = function(ctx) {
};


// Enter a parse tree produced by JavaParser#localVariableDeclaration.
JavaListener.prototype.enterLocalVariableDeclaration = function(ctx) {
};

// Exit a parse tree produced by JavaParser#localVariableDeclaration.
JavaListener.prototype.exitLocalVariableDeclaration = function(ctx) {
};


// Enter a parse tree produced by JavaParser#statement.
JavaListener.prototype.enterStatement = function(ctx) {
};

// Exit a parse tree produced by JavaParser#statement.
JavaListener.prototype.exitStatement = function(ctx) {
};


// Enter a parse tree produced by JavaParser#catchClause.
JavaListener.prototype.enterCatchClause = function(ctx) {
};

// Exit a parse tree produced by JavaParser#catchClause.
JavaListener.prototype.exitCatchClause = function(ctx) {
};


// Enter a parse tree produced by JavaParser#catchType.
JavaListener.prototype.enterCatchType = function(ctx) {
};

// Exit a parse tree produced by JavaParser#catchType.
JavaListener.prototype.exitCatchType = function(ctx) {
};


// Enter a parse tree produced by JavaParser#finallyBlock.
JavaListener.prototype.enterFinallyBlock = function(ctx) {
};

// Exit a parse tree produced by JavaParser#finallyBlock.
JavaListener.prototype.exitFinallyBlock = function(ctx) {
};


// Enter a parse tree produced by JavaParser#resourceSpecification.
JavaListener.prototype.enterResourceSpecification = function(ctx) {
};

// Exit a parse tree produced by JavaParser#resourceSpecification.
JavaListener.prototype.exitResourceSpecification = function(ctx) {
};


// Enter a parse tree produced by JavaParser#resources.
JavaListener.prototype.enterResources = function(ctx) {
};

// Exit a parse tree produced by JavaParser#resources.
JavaListener.prototype.exitResources = function(ctx) {
};


// Enter a parse tree produced by JavaParser#resource.
JavaListener.prototype.enterResource = function(ctx) {
};

// Exit a parse tree produced by JavaParser#resource.
JavaListener.prototype.exitResource = function(ctx) {
};


// Enter a parse tree produced by JavaParser#switchBlockStatementGroup.
JavaListener.prototype.enterSwitchBlockStatementGroup = function(ctx) {
};

// Exit a parse tree produced by JavaParser#switchBlockStatementGroup.
JavaListener.prototype.exitSwitchBlockStatementGroup = function(ctx) {
};


// Enter a parse tree produced by JavaParser#switchLabel.
JavaListener.prototype.enterSwitchLabel = function(ctx) {
};

// Exit a parse tree produced by JavaParser#switchLabel.
JavaListener.prototype.exitSwitchLabel = function(ctx) {
};


// Enter a parse tree produced by JavaParser#forControl.
JavaListener.prototype.enterForControl = function(ctx) {
};

// Exit a parse tree produced by JavaParser#forControl.
JavaListener.prototype.exitForControl = function(ctx) {
};


// Enter a parse tree produced by JavaParser#forInit.
JavaListener.prototype.enterForInit = function(ctx) {
};

// Exit a parse tree produced by JavaParser#forInit.
JavaListener.prototype.exitForInit = function(ctx) {
};


// Enter a parse tree produced by JavaParser#enhancedForControl.
JavaListener.prototype.enterEnhancedForControl = function(ctx) {
};

// Exit a parse tree produced by JavaParser#enhancedForControl.
JavaListener.prototype.exitEnhancedForControl = function(ctx) {
};


// Enter a parse tree produced by JavaParser#forUpdate.
JavaListener.prototype.enterForUpdate = function(ctx) {
};

// Exit a parse tree produced by JavaParser#forUpdate.
JavaListener.prototype.exitForUpdate = function(ctx) {
};


// Enter a parse tree produced by JavaParser#parExpression.
JavaListener.prototype.enterParExpression = function(ctx) {
};

// Exit a parse tree produced by JavaParser#parExpression.
JavaListener.prototype.exitParExpression = function(ctx) {
};


// Enter a parse tree produced by JavaParser#expressionList.
JavaListener.prototype.enterExpressionList = function(ctx) {
};

// Exit a parse tree produced by JavaParser#expressionList.
JavaListener.prototype.exitExpressionList = function(ctx) {
};


// Enter a parse tree produced by JavaParser#statementExpression.
JavaListener.prototype.enterStatementExpression = function(ctx) {
};

// Exit a parse tree produced by JavaParser#statementExpression.
JavaListener.prototype.exitStatementExpression = function(ctx) {
};


// Enter a parse tree produced by JavaParser#constantExpression.
JavaListener.prototype.enterConstantExpression = function(ctx) {
};

// Exit a parse tree produced by JavaParser#constantExpression.
JavaListener.prototype.exitConstantExpression = function(ctx) {
};


// Enter a parse tree produced by JavaParser#expression.
JavaListener.prototype.enterExpression = function(ctx) {
};

// Exit a parse tree produced by JavaParser#expression.
JavaListener.prototype.exitExpression = function(ctx) {
};


// Enter a parse tree produced by JavaParser#primary.
JavaListener.prototype.enterPrimary = function(ctx) {
};

// Exit a parse tree produced by JavaParser#primary.
JavaListener.prototype.exitPrimary = function(ctx) {
};


// Enter a parse tree produced by JavaParser#creator.
JavaListener.prototype.enterCreator = function(ctx) {
};

// Exit a parse tree produced by JavaParser#creator.
JavaListener.prototype.exitCreator = function(ctx) {
};


// Enter a parse tree produced by JavaParser#createdName.
JavaListener.prototype.enterCreatedName = function(ctx) {
};

// Exit a parse tree produced by JavaParser#createdName.
JavaListener.prototype.exitCreatedName = function(ctx) {
};


// Enter a parse tree produced by JavaParser#innerCreator.
JavaListener.prototype.enterInnerCreator = function(ctx) {
};

// Exit a parse tree produced by JavaParser#innerCreator.
JavaListener.prototype.exitInnerCreator = function(ctx) {
};


// Enter a parse tree produced by JavaParser#arrayCreatorRest.
JavaListener.prototype.enterArrayCreatorRest = function(ctx) {
};

// Exit a parse tree produced by JavaParser#arrayCreatorRest.
JavaListener.prototype.exitArrayCreatorRest = function(ctx) {
};


// Enter a parse tree produced by JavaParser#classCreatorRest.
JavaListener.prototype.enterClassCreatorRest = function(ctx) {
};

// Exit a parse tree produced by JavaParser#classCreatorRest.
JavaListener.prototype.exitClassCreatorRest = function(ctx) {
};


// Enter a parse tree produced by JavaParser#explicitGenericInvocation.
JavaListener.prototype.enterExplicitGenericInvocation = function(ctx) {
};

// Exit a parse tree produced by JavaParser#explicitGenericInvocation.
JavaListener.prototype.exitExplicitGenericInvocation = function(ctx) {
};


// Enter a parse tree produced by JavaParser#nonWildcardTypeArguments.
JavaListener.prototype.enterNonWildcardTypeArguments = function(ctx) {
};

// Exit a parse tree produced by JavaParser#nonWildcardTypeArguments.
JavaListener.prototype.exitNonWildcardTypeArguments = function(ctx) {
};


// Enter a parse tree produced by JavaParser#typeArgumentsOrDiamond.
JavaListener.prototype.enterTypeArgumentsOrDiamond = function(ctx) {
};

// Exit a parse tree produced by JavaParser#typeArgumentsOrDiamond.
JavaListener.prototype.exitTypeArgumentsOrDiamond = function(ctx) {
};


// Enter a parse tree produced by JavaParser#nonWildcardTypeArgumentsOrDiamond.
JavaListener.prototype.enterNonWildcardTypeArgumentsOrDiamond = function(ctx) {
};

// Exit a parse tree produced by JavaParser#nonWildcardTypeArgumentsOrDiamond.
JavaListener.prototype.exitNonWildcardTypeArgumentsOrDiamond = function(ctx) {
};


// Enter a parse tree produced by JavaParser#superSuffix.
JavaListener.prototype.enterSuperSuffix = function(ctx) {
};

// Exit a parse tree produced by JavaParser#superSuffix.
JavaListener.prototype.exitSuperSuffix = function(ctx) {
};


// Enter a parse tree produced by JavaParser#explicitGenericInvocationSuffix.
JavaListener.prototype.enterExplicitGenericInvocationSuffix = function(ctx) {
};

// Exit a parse tree produced by JavaParser#explicitGenericInvocationSuffix.
JavaListener.prototype.exitExplicitGenericInvocationSuffix = function(ctx) {
};


// Enter a parse tree produced by JavaParser#arguments.
JavaListener.prototype.enterArguments = function(ctx) {
};

// Exit a parse tree produced by JavaParser#arguments.
JavaListener.prototype.exitArguments = function(ctx) {
};



exports.JavaListener = JavaListener;