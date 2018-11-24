let socket = undefined;
let chart = new Chart();
let telemetry = new Telemetry();
let log = new Log();

$(document).ready(() => {
  initSocket();
});

$("#config").submit(event => {
  event.preventDefault();

  $.ajax({
    success: function(data) {
      let formData = $("#config").serializeArray();
      values = [];
      for (let i = 0; i < formData.length; i++) {
        valueStr = formData[i].value;
        if (valueStr == "") {
          values.push(0);
        } else {
          values.push(parseFloat(valueStr));
        }
      }
      console.log(values);
      socket.send("[UPID]:" + values.join(","));
    },
    error: function() {
      console.log("Error submitting config.");
    }
  });
});

$("#runPID").click(() => {
  chart.reset(false);
  socket.send("[RPID]");
});

$("#clearLog").click(() => {
  $("#log").html("");
});
