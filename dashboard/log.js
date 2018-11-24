class Log {
  constructor() {
    this.prevMessage = "";
    this.elem = $("#log");
    this.log("$ Beginning of log $");
  }

  log(message) {
    let logVanilla = document.getElementById("log");
    this.elem.append(`${message} <br />`);
    this.prevMessage = message;
    logVanilla.scrollTop = logVanilla.scrollHeight;
  }
}
