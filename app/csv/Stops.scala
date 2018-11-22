package csv

import javax.inject.{Inject, Singleton}
import play.api.Environment

import scala.io.Source

@Singleton
class Stops @Inject()(env: Environment) {
  private val XtoYtoStop: Map[Int, Map[Int, Int]] = Source.fromURL(env.classLoader.getResource("stops.csv"))
    .getLines()
    .toList
    .tail
    .map { l =>
      val d = l.split(",")
      (d(1).toInt, d(2).toInt, d(0).toInt)
    }
    .groupBy(_._1)
    .mapValues(_.groupBy(_._2).mapValues(_.head._3))

  def stops(x: Int, y: Int): Option[Int] = XtoYtoStop.get(x).flatMap(_.get(y))
}
