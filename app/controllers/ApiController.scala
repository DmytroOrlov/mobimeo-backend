package controllers

import java.time.LocalTime
import java.time.temporal.ChronoUnit.MINUTES

import csv.Times.formatter
import csv._
import javax.inject._
import play.api.mvc._

@Singleton
class ApiController @Inject()(ls: Lines, delays: Delays, times: Times, stops: Stops, cc: ControllerComponents) extends AbstractController(cc) {

  def isDelayed(line: String): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    delays.isDelayed(line)
      .fold(NotFound("")) { b =>
        Ok(b.toString)
      }
  }

  def find(time: String, x: Int, y: Int): Action[AnyContent] = Action {
    val localTime = LocalTime.parse(time, formatter)
    val stop = stops.stops(x, y)
    stop.flatMap { s =>
      times.stopToLines(s).collectFirst {
        case (line, t) if t.compareTo(localTime) == 0 => line
      }
    }.fold(NotFound("")) { l => Ok(ls.lineName(l)) }
  }

  def next(stop: Int): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    nextLine(stop, LocalTime.now())
      .fold(NotFound(""))(Ok.apply)
  }

  private[controllers] def nextLine(stop: Int, now: LocalTime): Option[String] = {
    val nowSeconds = now.toSecondOfDay
    val lineTime = times.stopToLines.getOrElse(stop, Nil)
    if (lineTime.nonEmpty) {
      val (l, _) = lineTime.minBy {
        case (_, time) =>
          val seconds = time.toSecondOfDay
          if (time.isBefore(now)) seconds + nowSeconds
          else seconds
      }

      Some(ls.lineName(l))
    } else None
  }
}
