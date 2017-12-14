package com.brian.medical.domain.models

case class Patient(age: Int, order: Seq[Order], hasInsurance: Boolean = false) {

  def toChargeTotals: ChargeTotals = {
    val charges: Seq[ItemCharge] = order.flatMap(_.toCharge.map(_.toItemizedCharge(age, hasInsurance)))

    ChargeTotals(charges.map(_.charge).sum, charges)
  }
}