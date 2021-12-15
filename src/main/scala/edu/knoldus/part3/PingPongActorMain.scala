package edu.knoldus.part3
import akka.actor.Status.Success
import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, Props}

import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.language.postfixOps
import scala.util.Try

object PingPongActorMain extends App {

  object PingActor {
    case class CreateChildActor(name: String)
  }

  class PingActor extends Actor with ActorLogging {
    var sum = 0
    var counter = 0
    import PingActor._
    val childRef: ActorRef = context.actorOf(Props[PongActor], "pongActor")
    override def receive: Receive = {
      case StartMessage =>
        println(s"${self.path}: Creating new actor")
        for {
          _ <- 1 to 10000
        } yield childRef ! PingMessage


      case PongMessage =>
        counter += 1
        sum += 1



case SendGetPongSum => childRef ! GetPongSum(None)

case  GetPongSum(pongSum) => println(s"Pong sum: $pongSum")
        sender ! ThrowException
      case End(pingSum) => println(s"Sum: $pingSum Counter: $counter")
        println("end")
            sender ! GetPongSum(None)
    }
  }

  object PongActor {
    case class TellParent(message: String)
  }
  class PongActor extends Actor with ActorLogging {
    var sum: Int = 0
    def doWork(): Int = {
      1
    }

    override def receive: Receive = {
      case GetPongSum(None) =>
        println("pong new")
        val newSum: Option[Int] = Some(sum)
        sender ! GetPongSum(newSum)
      case PingMessage =>
        val getSum = Future {
          sum += doWork()
          sum
        }
        println("Ping")
        Await.result(getSum, Duration.Inf)
        if (sum < 10000) {
          sender ! PongMessage
        } else if (sum == 10000) {
          sender ! End(sum)
        }
      case StopMessage =>
        println("stop Pong")
//        context.stop(self)
      case ThrowException =>
        throw new Exception("New")
    }
  }

  case class End(receivedPings: Int)
  case object PingMessage
  case object PongMessage
  case object StartMessage
  case object StopMessage
  case class ThrowException()
  case object SendGetPongSum
  case class GetPongSum(sum : Option[Int])
  import PingActor._
  val actorSystem: ActorSystem = ActorSystem("actorSystem")
  val pingActor = actorSystem.actorOf(Props[PingActor], "pingActor")
  pingActor ! StartMessage
  pingActor ! SendGetPongSum


}
