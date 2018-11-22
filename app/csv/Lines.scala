package csv

import javax.inject.{Inject, Singleton}
import play.api.Environment

import scala.io.Source

@Singleton
class Lines @Inject()(env: Environment) {
  private val ls = Source.fromURL(env.classLoader.getResource("lines.csv"))
    .getLines()
    .toList
    .tail
    .map(_.split(","))

  val lineName: Map[Int, String] =
    ls.map { d =>
      d(0).toInt -> d(1)
    }.toMap

  val lineId: Map[String, Int] = ls.map { d =>
    d(1) -> d(0).toInt
  }.toMap
}
