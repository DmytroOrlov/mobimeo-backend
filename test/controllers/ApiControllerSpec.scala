package controllers

import java.time.LocalTime

import csv.Times.formatter
import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.test.Helpers._
import play.api.test._

class ApiControllerSpec extends PlaySpec with GuiceOneAppPerTest with Injecting {
  "web API" should {
    "for /isDelayed/200 return true" in {
      val request = FakeRequest(GET, "/isDelayed/200")
      val isDelayed = route(app, request).get

      status(isDelayed) mustBe OK
      contentAsString(isDelayed) mustBe "true"
    }
    "for /isDelayed/777 return NotFound" in {
      val request = FakeRequest(GET, "/isDelayed/777")
      val isDelayed = route(app, request).get

      status(isDelayed) mustBe NOT_FOUND
    }
    "for /next/3 at 10:07:00 return M4" in {
      val controller = inject[ApiController]
      val next = controller.nextLine(3, LocalTime.parse("10:07:00", formatter))

      next mustBe Some("M4")
    }
    "for /next/3 at 10:18:00 return S75" in {
      val controller = inject[ApiController]
      val next = controller.nextLine(3, LocalTime.parse("10:18:00", formatter))

      next mustBe Some("S75")
    }
    "for next day /next/3 at 10:19:00 return M4" in {
      val controller = inject[ApiController]
      val next = controller.nextLine(3, LocalTime.parse("10:19:00", formatter))

      next mustBe Some("M4")
    }
    "for unknown stop /next/12 return None" in {
      val controller = inject[ApiController]
      val next = controller.nextLine(12, LocalTime.parse("10:19:00", formatter))

      next mustBe None
    }
    "for /find/10:03:00/1/4 return M4" in {
      val request = FakeRequest(GET, "/find/10:03:00/1/4")
      val vehicle = route(app, request).get

      status(vehicle) mustBe OK
      contentAsString(vehicle) mustBe "M4"
    }
    "for /find/10:06:00/3/4 return 200" in {
      val request = FakeRequest(GET, "/find/10:06:00/3/4")
      val vehicle = route(app, request).get

      status(vehicle) mustBe OK
      contentAsString(vehicle) mustBe "200"
    }
    "for unknown stop /find/10:06:00/1/2 return None" in {
      val request = FakeRequest(GET, "/find/10:06:00/1/2")
      val vehicle = route(app, request).get

      status(vehicle) mustBe NOT_FOUND
    }
  }
}
