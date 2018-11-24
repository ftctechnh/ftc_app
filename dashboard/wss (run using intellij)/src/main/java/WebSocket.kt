import io.javalin.Javalin
import io.javalin.websocket.WsSession

class WebSocketServer(val port:Int, val path:String) {
    var sessions = arrayListOf<WsSession>()
    var serverMessage = ""
    var ws:Javalin? = Javalin.create().apply {
        ws("/$path") { ws ->
            ws.onConnect { session ->
                broadcastMessage("New connection")
                sessions.add(session)
            }
            ws.onClose { session, status, message ->
                sessions.remove(session)
            }
            ws.onMessage { session, message ->
                println(message)
                serverMessage = message
            }
        }
    }

    fun start(){
        if (ws != null) {
            ws!!.start(port)
        } else {
            throw java.lang.NullPointerException("Must create websocket before starting")
        }
    }

    fun stop(){
        if (ws != null) {
            ws!!.stop()
        } else {
            throw java.lang.NullPointerException("Must create websocket before starting")
        }

    }

    fun broadcastMessage(message: String,allSessions:Boolean=true,session: WsSession?=null) {
        val t = System.currentTimeMillis()
        val toBroadcast = message
        if (!allSessions) {
            if(session==null){
                throw NullPointerException("Websocket specified broadcast to a single session but did not specify a session :(")
            }else{
                session.send(toBroadcast)
            }
        }
        sessions.forEach{
            it.send(toBroadcast)
        }
    }
}