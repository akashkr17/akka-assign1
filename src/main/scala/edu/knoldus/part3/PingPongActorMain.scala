package edu.knoldus.part3
import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, Props}

import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.language.postfixOps

object PingPongActorMain extends App {

  class PingActor extends Actor with ActorLogging {
    var sum = 0
    var counter = 0
    println(s"${self.path}: Creating new actor")
    val childRef: ActorRef = context.actorOf(Props[PongActor], "pongActor")
    override def receive: Receive = {
      case StartMessage =>
        for {
          _ <- 1 to 10000
        } yield childRef ! PingMessage

      case PongMessage =>
        counter += 1
        sum += 1

      case SendGetPongSum => childRef ! GetPongSum(None)

      case GetPongSum(pongSum) =>
        println(s"Pong sum: $pongSum")
        sender ! ThrowException
      case End(pingSum) =>
        println(s"Sum: $pingSum Counter: $counter")
        println("end")
        sender ! GetPongSum(None)
    }
  }

  class PongActor extends Actor with ActorLogging {
    var sum: Int = 0
    def doWork(): Int = {
      1
    }

    override def receive: Receive = {
      case GetPongSum(None) =>
        val newSum: Option[Int] = Some(sum)
        sender ! GetPongSum(newSum)
      case PingMessage =>
        val getSum = Future {
          sum += doWork()
          sum
        }
        Await.result(getSum, Duration.Inf)
        if (sum < 10000) {
          sender ! PongMessage
        } else if (sum == 10000) {
          sender ! End(sum)
        }
      case ThrowException =>
        throw new Exception("New")
    }
  }

  case class End(receivedPings: Int)
  case object PingMessage
  case object PongMessage
  case object StartMessage
  case class ThrowException()
  case object SendGetPongSum
  case class GetPongSum(sum: Option[Int])

  val actorSystem: ActorSystem = ActorSystem("actorSystem")
  val pingActor = actorSystem.actorOf(Props[PingActor], "pingActor")
  pingActor ! StartMessage
  pingActor ! SendGetPongSum

}
