package org.firstinspires.ftc.teamcode.Utils

import io.javalin.Javalin
import io.javalin.websocket.WsSession

import org.json.JSONObject
import java.lang.Thread.sleep
import java.util.*

class WSS(val port:Int,val path:String) {
    //WSS = Web Socket Server
    var sessions = arrayListOf<WsSession>();
    val l:Logger = Logger("WEBSOCKET")
    var ws:Javalin? = null
    init{
        l.log("Creating websocket")
        l.logData("Port",port)
        l.logData("Path",path)

        ws = Javalin.create().apply {
            ws("/$path") { ws ->
                ws.onConnect { session ->
                    l.log("Connected - ${session.id}")
                    broadcastMessage("New connection")
                    sessions.add(session)
                }
                ws.onClose { session, status, message ->
                    l.log("Disconnected - ${session.id} with status ${status} & message: $message")
                    sessions.remove(session)
                }
                ws.onMessage { session, message ->
                    l.log("Received message from - ${session.id}: $message")
                }
            }
        }
        l.log("Websocket created.")
    }
    fun start(){
        if (ws != null) {
            l.log("Starting websocket")
            ws!!.start(port)
        }else{
            throw java.lang.NullPointerException("Must create websocket before starting")
        }

    }

    fun stop(){
        if (ws != null) {
            l.log("Stopping websocket")
            ws!!.stop()
        }else{
            throw java.lang.NullPointerException("Must create websocket before starting")
        }

    }

    fun broadcastMessage(message: String,allSessions:Boolean=true,session: WsSession?=null) {
        l.lineBreak()

        val t = System.currentTimeMillis()
        val toBroadcast = message
        l.log("Broadcasting message: $toBroadcast")
        if(!allSessions){
            if(session==null){
                throw NullPointerException("Websocket specified broadcast to a single session but did not specify a session :(")
            }else{
                session.send(toBroadcast)
                l.log("Sent to ${session.id}")
            }
        }
        sessions.forEach{
            it.send(toBroadcast)
            l.log("Sent to ${it.id}")
        }

        l.log("Broadcast took ${System.currentTimeMillis()-t} ms")
        l.lineBreak()
    }
}
