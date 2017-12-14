package com.brian.medical.controller

import com.brian.medical.controller.utils.TestCommons
import com.brian.medical.domain.models._

class TestCharges extends TestCommons {

  it should "return None if service is not in cost sheet" in {
    CostSheet.findCharge("missing") shouldBe None
  }

  it should "return X-Ray charge from the cost sheet" in {
    val service = "X-Ray"

    CostSheet.findCharge(service) shouldBe Some(
      Charge(service, 150, 0, 0)
    )
  }

  it should "return charges for patients with services" in {
    val service = "X-Ray"

    val patient = Patient(
      60,
      Seq(
        Services(service)
      )
    )

    patient.toChargeTotals shouldBe ChargeTotals(150.00,List(ItemCharge("X-Ray",150.00)))
  }


  it should "return charges for patients with services and dosage" in {
    val service = "Vaccine"

    val patient = Patient(
      60,
      Seq(
        Prescription(service, 4)
      )
    )

    patient.toChargeTotals shouldBe ChargeTotals(87.5, Seq(
      ItemCharge(service, 87.5)
    ))
  }

  it should "return handle different order types (not case sensitive)" in {
    val service = "Vaccine"

    val patient = Patient(
      60,
      Seq(
        Prescription(service, 4),
        Services("x-ray")
      )
    )

    patient.toChargeTotals shouldBe ChargeTotals(237.5, Seq(
      ItemCharge(service, 87.5),
      ItemCharge("X-Ray",150.00)
    ))
  }

  it should "return charges for patients with services and dosage with 15% discount for insurance" in {
    val service = "Vaccine"

    val patient = Patient(
      60,
      Seq(
        Prescription(service, 4)
      ),
      true
    )

    patient.toChargeTotals shouldBe ChargeTotals(74.38, Seq(
      ItemCharge(service, 74.38)
    ))
  }

  it should "return charges for patients with with correct discounts" in {
    val service = "Vaccine"

    def patient(age: Int, insurance: Boolean) = Patient(
      age,
      Seq(
        Prescription(service)
      ),
      insurance
    )

    Seq(
      (patient(60, false), 42.5),
      (patient(65, false), 17.0),
      (patient(70, false), 17.0),
      (patient(71, false), 4.25),
      (patient(60, true), 36.13),
      (patient(65, true), 14.45),
      (patient(70, true), 14.45),
      (patient(71, true), 3.62)
    )
      .foreach { check =>
        val (pat, total) = check

        pat.toChargeTotals shouldBe ChargeTotals(total, Seq(
          ItemCharge(service, total)
        ))
      }
  }

}
