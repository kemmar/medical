package com.brian.medical.domain.models

case class ChargeTotals(chargeTotal: BigDecimal, itemisedCharges: Seq[ItemCharge])