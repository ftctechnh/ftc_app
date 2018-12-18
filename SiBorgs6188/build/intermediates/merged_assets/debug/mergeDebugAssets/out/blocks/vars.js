var currentProjectName;

var setPropertyColor = 147;
var getPropertyColor = 151;
var functionColor = 289;
var commentColor = 200;

var identifierFieldNames = ['IDENTIFIER', 'IDENTIFIER1', 'IDENTIFIER2'];

function createNonEditableField(label) {
  var field = new Blockly.FieldTextInput(label);
  field.CURSOR = '';
  field.showEditor_ = function(opt_quietInput) {};
  return field;
}

function createFieldDropdown(choices) {
  if (choices.length == 0) {
    return createNonEditableField('');
  }
  return new Blockly.FieldDropdown(choices);
}
