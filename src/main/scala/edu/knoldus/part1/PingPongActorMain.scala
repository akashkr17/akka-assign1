package edu.knoldus.part1

import akka.actor.{Actor, ActorLogging, ActorSystem, Props}

object PingPongActorMain extends App {

  object PingActor {
    case class CreateChildActor(name: String)
  }

  class PingActor extends Actor with ActorLogging {
    import PingActor._
    override def receive: Receive = {
      case CreateChildActor(name) =>
        println(s"${self.path}: Creating new actor")
        val childRef = context.actorOf(Props[PongActor], name)
        childRef ! "ping"
      case message => log.info(message.toString)
    }
  }

  class PongActor extends Actor with ActorLogging {
    override def receive: Receive = {
      case message =>
        log.info(message.toString)
        sender() ! "pong"
    }
  }

  val actorSystem: ActorSystem = ActorSystem("actorSystem")
  val pingActor = actorSystem.actorOf(Props[PingActor], "pingActor")

  import PingActor._
  pingActor ! CreateChildActor("pongActor")
}
