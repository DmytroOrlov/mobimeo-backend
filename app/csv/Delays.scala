package csv

import javax.inject.{Inject, Singleton}
import play.api.Environment

import scala.io.Source

@Singleton
class Delays @Inject()(lines: Lines, env: Environment) {
  private val byLineName: Map[String, Int] = Source.fromURL(env.classLoader.getResource("delays.csv"))
    .getLines()
    .toList
    .tail
    .map { l =>
      val d = l.split(",")
      d(0) -> d(1).toInt
    }.toMap

  val byLineId: Map[Int, Int] = byLineName.map {
    case (lineName, delay) =>
      lines.lineId(lineName) -> delay
  }

  def isDelayed(line: String): Option[Boolean] =
    byLineName.get(line).map(_ > 0)
}
