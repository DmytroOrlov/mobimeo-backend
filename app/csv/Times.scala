package csv

import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit.MINUTES

import csv.Times.formatter
import javax.inject.{Inject, Singleton}
import play.api.Environment

import scala.io.Source

@Singleton
class Times @Inject()(delays: Delays, env: Environment) {
  private val times: List[(Int, Int, LocalTime)] = Source.fromURL(env.classLoader.getResource("times.csv"))
    .getLines()
    .toList
    .tail
    .map { l =>
      val d = l.split(",")
      (d(0).toInt, d(1).toInt, LocalTime.parse(d(2), formatter))
    }

  val stopToLines: Map[Int, List[(Int, LocalTime)]] = times.groupBy {
    case (_, stop, _) => stop
  }.mapValues(_.map { case (l, _, t) => (l, t.plus(delays.byLineId(l), MINUTES)) })
}

object Times {
  val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss")
}
