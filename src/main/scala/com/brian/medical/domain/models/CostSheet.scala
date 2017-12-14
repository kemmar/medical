package com.brian.medical.domain.models

import scala.math.BigDecimal.RoundingMode

case class Charge(service: String, serviceCharge: BigDecimal, doseCharge: BigDecimal = 0.00, dosage: Int = 0) {

  def ageToDiscount(age: Int, hasInsurance: Boolean): BigDecimal => BigDecimal = { total =>
   val perc: BigDecimal = age match {
      case des if des < 5 => 0.6
      case des if des >= 65 && des <= 70 => 0.4
      case des if des > 70 => 0.1
      case _ => 1
    }

    (if(hasInsurance) (total * perc) * 0.85 else total * perc).setScale(2, RoundingMode.UP)
  }

  private def getChargeTotal: BigDecimal = (doseCharge * dosage) + serviceCharge

  def toItemizedCharge(age: Int, hasInsurance: Boolean): ItemCharge =
    ItemCharge(service, ageToDiscount(age, hasInsurance)(getChargeTotal))
}

object CostSheet {
  def findCharge(service: String) = Seq(
    Charge("Diagnosis", 60),
    Charge("X-Ray", 150),
    Charge("Blood Test", 78),
    Charge("ECG", 200.40),
    Charge("Vaccine", 27.50, 15)
  ).find(_.service.equalsIgnoreCase(service))
}
