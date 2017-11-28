/**
 * @fileoverview FTC robot blocks related to VuforiaLocalizer.Parameters
 * @author lizlooney@google.com (Liz Looney)
 */

// The following are generated dynamically in HardwareUtil.fetchJavaScriptForHardware():
// vuforiaLocalizerParametersIdentifier
// The following are defined in vars.js:
// createNonEditableField
// getPropertyColor
// functionColor

Blockly.Blocks['vuforiaLocalizerParameters_getProperty'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['VuforiaLicenseKey', 'VuforiaLicenseKey'],
        ['CameraDirection', 'CameraDirection'],
        ['UseExtendedTracking', 'UseExtendedTracking'],
        ['EnableCameraMonitoring', 'EnableCameraMonitoring'],
        ['CameraMonitorFeedback', 'CameraMonitorFeedback'],
        ['FillCameraMonitorViewParent', 'FillCameraMonitorViewParent'],
    ];
    this.setOutput(true); // no type, for compatibility
    this.appendDummyInput()
        .appendField(createNonEditableField('VuforiaLocalizer.Parameters'))
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    this.appendValueInput('VUFORIA_LOCALIZER_PARAMETERS').setCheck('VuforiaLocalizer.Parameters')
        .appendField('vuforiaLocalizerParameters')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['VuforiaLicenseKey',
            'Returns the Vuforia license key of the given VuforiaLocalizer.Parameters.'],
        ['CameraDirection',
            'Returns the CameraDirection of the given VuforiaLocalizer.Parameters.'],
        ['UseExtendedTracking',
            'Returns true if Vuforia\'s extended tracking mode will be used. Extended tracking ' +
            'allows tracking to continue even once an image tracker has gone out of view.'],
        ['EnableCameraMonitoring', 'Returns true if live camera monitoring is enabled.'],
        ['CameraMonitorFeedback', 'Returns the style of camera monitoring feedback to use.'],
        ['FillCameraMonitorViewParent',
            'Returns  whether the camera monitor should entirely fill the camera monitor view ' +
            'parent or not.'],
    ];
    this.setTooltip(function() {
      var key = thisBlock.getFieldValue('PROP');
      for (var i = 0; i < TOOLTIPS.length; i++) {
        if (TOOLTIPS[i][0] == key) {
          return TOOLTIPS[i][1];
        }
      }
      return '';
    });
  }
};

Blockly.JavaScript['vuforiaLocalizerParameters_getProperty'] = function(block) {
  var property = block.getFieldValue('PROP');
  var vuforiaLocalizerParameters = Blockly.JavaScript.valueToCode(
      block, 'VUFORIA_LOCALIZER_PARAMETERS', Blockly.JavaScript.ORDER_NONE);
  var code = vuforiaLocalizerParametersIdentifier + '.get' + property + '(' +
      vuforiaLocalizerParameters + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['vuforiaLocalizerParameters_getProperty_String'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['VuforiaLicenseKey', 'VuforiaLicenseKey'],
    ];
    this.setOutput(true, 'String');
    this.appendDummyInput()
        .appendField(createNonEditableField('VuforiaLocalizer.Parameters'))
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    this.appendValueInput('VUFORIA_LOCALIZER_PARAMETERS').setCheck('VuforiaLocalizer.Parameters')
        .appendField('vuforiaLocalizerParameters')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['VuforiaLicenseKey',
            'Returns the Vuforia license key of the given VuforiaLocalizer.Parameters.'],
    ];
    this.setTooltip(function() {
      var key = thisBlock.getFieldValue('PROP');
      for (var i = 0; i < TOOLTIPS.length; i++) {
        if (TOOLTIPS[i][0] == key) {
          return TOOLTIPS[i][1];
        }
      }
      return '';
    });
  }
};

Blockly.JavaScript['vuforiaLocalizerParameters_getProperty_String'] =
    Blockly.JavaScript['vuforiaLocalizerParameters_getProperty'];

Blockly.Blocks['vuforiaLocalizerParameters_getProperty_CameraDirection'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['CameraDirection', 'CameraDirection'],
    ];
    this.setOutput(true, 'CameraDirection');
    this.appendDummyInput()
        .appendField(createNonEditableField('VuforiaLocalizer.Parameters'))
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    this.appendValueInput('VUFORIA_LOCALIZER_PARAMETERS').setCheck('VuforiaLocalizer.Parameters')
        .appendField('vuforiaLocalizerParameters')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['CameraDirection',
            'Returns the CameraDirection of the given VuforiaLocalizer.Parameters.'],
    ];
    this.setTooltip(function() {
      var key = thisBlock.getFieldValue('PROP');
      for (var i = 0; i < TOOLTIPS.length; i++) {
        if (TOOLTIPS[i][0] == key) {
          return TOOLTIPS[i][1];
        }
      }
      return '';
    });
  }
};

Blockly.JavaScript['vuforiaLocalizerParameters_getProperty_CameraDirection'] =
    Blockly.JavaScript['vuforiaLocalizerParameters_getProperty'];

Blockly.Blocks['vuforiaLocalizerParameters_getProperty_Boolean'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['UseExtendedTracking', 'UseExtendedTracking'],
        ['EnableCameraMonitoring', 'EnableCameraMonitoring'],
        ['FillCameraMonitorViewParent', 'FillCameraMonitorViewParent'],
    ];
    this.setOutput(true, 'Boolean');
    this.appendDummyInput()
        .appendField(createNonEditableField('VuforiaLocalizer.Parameters'))
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    this.appendValueInput('VUFORIA_LOCALIZER_PARAMETERS').setCheck('VuforiaLocalizer.Parameters')
        .appendField('vuforiaLocalizerParameters')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['UseExtendedTracking',
            'Returns true if Vuforia\'s extended tracking mode will be used. Extended tracking ' +
            'allows tracking to continue even once an image tracker has gone out of view.'],
        ['EnableCameraMonitoring', 'Returns true if live camera monitoring is enabled.'],
        ['FillCameraMonitorViewParent',
            'Returns  whether the camera monitor should entirely fill the camera monitor view ' +
            'parent or not.'],
    ];
    this.setTooltip(function() {
      var key = thisBlock.getFieldValue('PROP');
      for (var i = 0; i < TOOLTIPS.length; i++) {
        if (TOOLTIPS[i][0] == key) {
          return TOOLTIPS[i][1];
        }
      }
      return '';
    });
  }
};

Blockly.JavaScript['vuforiaLocalizerParameters_getProperty_Boolean'] =
    Blockly.JavaScript['vuforiaLocalizerParameters_getProperty'];

Blockly.Blocks['vuforiaLocalizerParameters_getProperty_CameraMonitorFeedback'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['CameraMonitorFeedback', 'CameraMonitorFeedback'],
    ];
    this.setOutput(true, 'CameraMonitorFeedback');
    this.appendDummyInput()
        .appendField(createNonEditableField('VuforiaLocalizer.Parameters'))
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    this.appendValueInput('VUFORIA_LOCALIZER_PARAMETERS').setCheck('VuforiaLocalizer.Parameters')
        .appendField('vuforiaLocalizerParameters')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['CameraMonitorFeedback', 'Returns the style of camera monitoring feedback to use.'],
    ];
    this.setTooltip(function() {
      var key = thisBlock.getFieldValue('PROP');
      for (var i = 0; i < TOOLTIPS.length; i++) {
        if (TOOLTIPS[i][0] == key) {
          return TOOLTIPS[i][1];
        }
      }
      return '';
    });
  }
};

Blockly.JavaScript['vuforiaLocalizerParameters_getProperty_CameraMonitorFeedback'] =
    Blockly.JavaScript['vuforiaLocalizerParameters_getProperty'];

// Functions

Blockly.Blocks['vuforiaLocalizerParameters_create'] = {
  init: function() {
    this.setOutput(true, 'VuforiaLocalizer.Parameters');
    this.appendDummyInput()
        .appendField('new')
        .appendField(createNonEditableField('VuforiaLocalizer.Parameters'));
    this.setColour(functionColor);
    this.setTooltip('Create a new VuforiaLocalizer.Parameters object.');
  }
};

Blockly.JavaScript['vuforiaLocalizerParameters_create'] = function(block) {
  var code = vuforiaLocalizerParametersIdentifier + '.create()';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['vuforiaLocalizerParameters_setVuforiaLicenseKey'] = {
  init: function() {
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('VuforiaLocalizer.Parameters'))
        .appendField('.')
        .appendField(createNonEditableField('setVuforiaLicenseKey'));
    this.appendValueInput('VUFORIA_LOCALIZER_PARAMETERS').setCheck('VuforiaLocalizer.Parameters')
        .appendField('vuforiaLocalizerParameters')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('VUFORIA_LICENSE_KEY').setCheck('String')
        .appendField('vuforiaLicenseKey')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setColour(functionColor);
    this.setTooltip('Sets the Vuforia license key of the given VuforiaLocalizer.Parameters.');
  }
};

Blockly.JavaScript['vuforiaLocalizerParameters_setVuforiaLicenseKey'] = function(block) {
  var vuforiaLocalizerParameters = Blockly.JavaScript.valueToCode(
      block, 'VUFORIA_LOCALIZER_PARAMETERS', Blockly.JavaScript.ORDER_COMMA);
  var vuforiaLicenseKey = Blockly.JavaScript.valueToCode(
      block, 'VUFORIA_LICENSE_KEY', Blockly.JavaScript.ORDER_COMMA);
  return vuforiaLocalizerParametersIdentifier + '.setVuforiaLicenseKey(' +
      vuforiaLocalizerParameters + ', ' + vuforiaLicenseKey + ');\n';
};

Blockly.Blocks['vuforiaLocalizerParameters_setCameraDirection'] = {
  init: function() {
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('VuforiaLocalizer.Parameters'))
        .appendField('.')
        .appendField(createNonEditableField('setCameraDirection'));
    this.appendValueInput('VUFORIA_LOCALIZER_PARAMETERS').setCheck('VuforiaLocalizer.Parameters')
        .appendField('vuforiaLocalizerParameters')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('CAMERA_DIRECTION').setCheck('CameraDirection')
        .appendField('cameraDirection')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setColour(functionColor);
    this.setTooltip('Indicates the camera which Vuforia should use.');
  }
};

Blockly.JavaScript['vuforiaLocalizerParameters_setCameraDirection'] = function(block) {
  var vuforiaLocalizerParameters = Blockly.JavaScript.valueToCode(
      block, 'VUFORIA_LOCALIZER_PARAMETERS', Blockly.JavaScript.ORDER_COMMA);
  var cameraDirection = Blockly.JavaScript.valueToCode(
      block, 'CAMERA_DIRECTION', Blockly.JavaScript.ORDER_COMMA);
  return vuforiaLocalizerParametersIdentifier + '.setCameraDirection(' +
      vuforiaLocalizerParameters + ', ' + cameraDirection + ');\n';
};

Blockly.Blocks['vuforiaLocalizerParameters_setUseExtendedTracking'] = {
  init: function() {
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('VuforiaLocalizer.Parameters'))
        .appendField('.')
        .appendField(createNonEditableField('setUseExtendedTracking'));
    this.appendValueInput('VUFORIA_LOCALIZER_PARAMETERS').setCheck('VuforiaLocalizer.Parameters')
        .appendField('vuforiaLocalizerParameters')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('USE_EXTENDED_TRACKING').setCheck('Boolean')
        .appendField('useExtendedTracking')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setColour(functionColor);
    this.setTooltip('Indicates whether to use Vuforia\'s extended tracking mode. Extended ' +
        'tracking allows tracking to continue even once an image tracker has gone out of view.');
  }
};

Blockly.JavaScript['vuforiaLocalizerParameters_setUseExtendedTracking'] = function(block) {
  var vuforiaLocalizerParameters = Blockly.JavaScript.valueToCode(
      block, 'VUFORIA_LOCALIZER_PARAMETERS', Blockly.JavaScript.ORDER_COMMA);
  var useExtendedTracking = Blockly.JavaScript.valueToCode(
      block, 'USE_EXTENDED_TRACKING', Blockly.JavaScript.ORDER_COMMA);
  return vuforiaLocalizerParametersIdentifier + '.setUseExtendedTracking(' +
      vuforiaLocalizerParameters + ', ' + useExtendedTracking + ');\n';
};

Blockly.Blocks['vuforiaLocalizerParameters_setEnableCameraMonitoring'] = {
  init: function() {
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('VuforiaLocalizer.Parameters'))
        .appendField('.')
        .appendField(createNonEditableField('setEnableCameraMonitoring'));
    this.appendValueInput('VUFORIA_LOCALIZER_PARAMETERS').setCheck('VuforiaLocalizer.Parameters')
        .appendField('vuforiaLocalizerParameters')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('ENABLE_CAMERA_MONITORING').setCheck('Boolean')
        .appendField('enableCameraMonitoring')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setColour(functionColor);
    this.setTooltip('Controls whether live camera monitoring is enabled.');
  }
};

Blockly.JavaScript['vuforiaLocalizerParameters_setEnableCameraMonitoring'] = function(block) {
  var vuforiaLocalizerParameters = Blockly.JavaScript.valueToCode(
      block, 'VUFORIA_LOCALIZER_PARAMETERS', Blockly.JavaScript.ORDER_COMMA);
  var enableCameraMonitoring = Blockly.JavaScript.valueToCode(
      block, 'ENABLE_CAMERA_MONITORING', Blockly.JavaScript.ORDER_COMMA);
  return vuforiaLocalizerParametersIdentifier + '.setEnableCameraMonitoring(' +
      vuforiaLocalizerParameters + ', ' + enableCameraMonitoring + ');\n';
};

Blockly.Blocks['vuforiaLocalizerParameters_setCameraMonitorFeedback'] = {
  init: function() {
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('VuforiaLocalizer.Parameters'))
        .appendField('.')
        .appendField(createNonEditableField('setCameraMonitorFeedback'));
    this.appendValueInput('VUFORIA_LOCALIZER_PARAMETERS').setCheck('VuforiaLocalizer.Parameters')
        .appendField('vuforiaLocalizerParameters')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('CAMERA_MONITOR_FEEDBACK').setCheck('CameraMonitorFeedback')
        .appendField('cameraMonitorFeedback')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setColour(functionColor);
    this.setTooltip('Indicates the style of camera monitoring feedback to use. ' +
        'CameraMonitorFeedback.DEFAULT indicates that a default feedback style is to be used. ' +
        'CameraMonitorFeedback.NONE indicates that the camera monitoring is to be provided, but ' +
        'no feedback is to be drawn thereon.');
  }
};

Blockly.JavaScript['vuforiaLocalizerParameters_setCameraMonitorFeedback'] = function(block) {
  var vuforiaLocalizerParameters = Blockly.JavaScript.valueToCode(
      block, 'VUFORIA_LOCALIZER_PARAMETERS', Blockly.JavaScript.ORDER_COMMA);
  var cameraMonitorFeedback = Blockly.JavaScript.valueToCode(
      block, 'CAMERA_MONITOR_FEEDBACK', Blockly.JavaScript.ORDER_COMMA);
  return vuforiaLocalizerParametersIdentifier + '.setCameraMonitorFeedback(' +
      vuforiaLocalizerParameters + ', ' + cameraMonitorFeedback + ');\n';
};

Blockly.Blocks['vuforiaLocalizerParameters_setFillCameraMonitorViewParent'] = {
  init: function() {
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('VuforiaLocalizer.Parameters'))
        .appendField('.')
        .appendField(createNonEditableField('setFillCameraMonitorViewParent'));
    this.appendValueInput('VUFORIA_LOCALIZER_PARAMETERS').setCheck('VuforiaLocalizer.Parameters')
        .appendField('vuforiaLocalizerParameters')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('FILL_CAMERA_MONITOR_VIEW_PARENT').setCheck('Boolean')
        .appendField('fillCameraMonitorViewParent')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setColour(functionColor);
    this.setTooltip('Controls whether the camera monitor should entirely fill the camera ' +
        'monitor view parent or not. If true, then, depending on the aspect ratios of the camera ' +
        'and the view, some of the camera\'s data might not be seen. The default is false, which ' +
        'renders the entire camera image in the camera monitor view.');
  }
};

Blockly.JavaScript['vuforiaLocalizerParameters_setFillCameraMonitorViewParent'] = function(block) {
  var vuforiaLocalizerParameters = Blockly.JavaScript.valueToCode(
      block, 'VUFORIA_LOCALIZER_PARAMETERS', Blockly.JavaScript.ORDER_COMMA);
  var fillCameraMonitorViewParent = Blockly.JavaScript.valueToCode(
      block, 'FILL_CAMERA_MONITOR_VIEW_PARENT', Blockly.JavaScript.ORDER_COMMA);
  return vuforiaLocalizerParametersIdentifier + '.setFillCameraMonitorViewParent(' +
      vuforiaLocalizerParameters + ', ' + fillCameraMonitorViewParent + ');\n';
};
