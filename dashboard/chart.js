class Chart {
  constructor() {
    this.times = [];
    this.desired = [];
    this.actual = [];
    this.chart = c3.generate({
      data: {
        x: "times",
        columns: [
          ["times"].concat(this.times),
          ["desired"].concat(this.desired),
          ["actual"].concat(this.actual)
        ]
      }
    });
    this.startTime = undefined;
  }

  updateChart() {
    this.chart.load({
      columns: [
        ["times"].concat(this.times),
        ["desired"].concat(this.desired),
        ["actual"].concat(this.actual)
      ]
    });
  }

  addData(time, d, a) {
    this.times.push(time);
    this.desired.push(d);
    this.actual.push(a);
  }

  addDataFromString(dataString) {
    if (this.startTime === undefined) {
      this.startTime = new Date().getTime();
    }
    let time = new Date().getTime();
    let values = dataString.toString().split(" ");
    this.addData(time - this.startTime, values[0], values[1]);
    this.updateChart();
  }

  reset(update) {
    this.times = [];
    this.desired = [];
    this.actual = [];
    this.startTime = undefined;
    if (update) {
      chart.update();
    }
  }
}
