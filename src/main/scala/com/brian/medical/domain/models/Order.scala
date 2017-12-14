package com.brian.medical.domain.models

trait Order {
  def service: String

  def dosage: Int = 0

  def toCharge: Option[Charge] =
    CostSheet
      .findCharge(service)
      .map(_.copy(dosage = dosage))
}

case class Services(service: String) extends Order

case class Prescription(service: String, override val dosage: Int = 1) extends Order