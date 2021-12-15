package edu.knoldus.part1

import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, Props}

object PingPongActorMain extends App {

  class PingActor extends Actor with ActorLogging {
    println(s"${self.path}: Creating new actor")
    val childRef: ActorRef = context.actorOf(Props[PongActor], "pongActor")
    override def receive: Receive = {
      case StartMessage =>
        childRef ! PingMessage
      case PongMessage => log.info("pong")
    }
  }

  class PongActor extends Actor with ActorLogging {
    override def receive: Receive = {
      case PingMessage =>
        log.info("ping")
        sender ! PongMessage
    }
  }

  case object PingMessage
  case object PongMessage
  case object StartMessage
  val actorSystem: ActorSystem = ActorSystem("actorSystem")
  val pingActor = actorSystem.actorOf(Props[PingActor], "pingActor")

  pingActor ! StartMessage
}
