var pidCoefficients = PIDCoefficients(0.1, 0.0, 0.0)

fun main(args: Array<String>) {
    val ws = WebSocketServer(5000, "")
    ws.start()

    while (true) {
        waitForMessage(ws)
    }
}

fun waitForMessage(ws: WebSocketServer) {
    while (ws.serverMessage == "") {
        Thread.sleep(100)
    }

    val message = ws.serverMessage.split(":")
    val messageType = message[0]

    when (messageType) {
        "[RPID]" -> runPID(ws)
        "[UPID]" -> updatePID(message[1])
    }
 
    ws.serverMessage = ""
}

fun runPID(ws: WebSocketServer, timeout: Int = 10000) {
    var x = 90.0
    val pid = PID(pidCoefficients, 0.0)

    pid.initController(x)

    val startTime = System.currentTimeMillis()
    while (Math.abs(x) > 2.0 && System.currentTimeMillis() - startTime < timeout) {
        val output = pid.output(x, ws)

        ws.broadcastMessage("[PID]:${pid.desiredVal} $x")
        ws.broadcastMessage("[LOG]:PID updated")
        x += output

        Thread.sleep(100)
    }
}

fun updatePID(valueString: String) {
    val values = valueString.split(",")
    val Kp = values[0].toDouble()
    val Ki = values[1].toDouble()
    val Kd = values[2].toDouble()
    pidCoefficients = PIDCoefficients(Kp, Ki, Kd)
    println("PID updated: $Kp $Ki $Kd")
}