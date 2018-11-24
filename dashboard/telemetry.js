class Telemetry {
  constructor() {
    this.items = {};
    this.elem = $("#telemetry");
  }

  addItem(name, value) {
    this.items[name] = String(value);
    this.updateTelemetry();
  }

  addItemFromString(telemString) {
    let telemItems = telemString.split(" ");
    telemItems.forEach(telemItem => {
      let [item, value] = telemItem.split("=");
      this.addItem(item, value);
    });
  }

  getTelemetry() {
    let telemStr = "";
    for (var itemName in this.items) {
      telemStr += `${itemName}: ${this.items[itemName]}<br />`;
    }
    return telemStr;
  }

  updateTelemetry() {
    this.elem.html(telemetry.getTelemetry());
  }
}
