package com.oliverClimbs.climbersAssistant.data

import com.oliverClimbs.climbersAssistant.centimetersInInches
import com.oliverClimbs.climbersAssistant.data.Fitness.Gender.Male
import com.oliverClimbs.climbersAssistant.data.Fitness.Gender.Unspecified
import com.oliverClimbs.climbersAssistant.kilogramsInPounds
import java.time.Duration
import kotlin.math.roundToInt

object Fitness
{
  /*************************************
   * Source: https://sites.google.com/site/compendiumofphysicalactivities/
   *************************************/
  const val MET_SITTING_STILL = 1.0
  const val MET_BELAYING = 2.0
  const val MET_RAPPELLING = 5.0
  const val MET_CLIMBING_EASY = 5.8
  const val MET_CLIMBING_MODERATE = 7.5
  const val MET_CLIMBING_HARD = 8.0

  enum class Gender
  {
    Unspecified,
    Male,
    Female
  }

  var gender = Unspecified
  var age = 0
  var kilograms = 0.0
  var centimeters = 0.0
  var restingHeartRate = 0

  /*************************************
   * Source: https://www.openfit.com/max-heart-rate
   *************************************/
  var maxHeartRate = 0.0
    get() = if (field != 0.0 || age == 0) field else maxHeartRate()

  fun maxHeartRate(age: Int = this.age) = 205.8 - (0.685 * age)

  /*************************************
   * Source: https://www.omnicalculator.com/sports/vo2-max
   *************************************/
  var vO2max = 0.0
    get() = if (field != 0.0 || (field == 0.0 && age == 0)) field else vO2max()

  fun vO2max(restingHeartRate: Int = this.restingHeartRate,
             maxHeartRate: Double = this.maxHeartRate) =
    if (restingHeartRate != 0) 15.3 * (maxHeartRate / restingHeartRate) else 0.0

  var pounds
    set(value)
    {
      kilograms = value / kilogramsInPounds

    }
    get() = kilograms * kilogramsInPounds

  var inches
    set(value)
    {
      centimeters = value * centimetersInInches

    }
    get() = centimeters / centimetersInInches

  /*************************************
   * Source: https://sendedition.com/how-many-calories-do-you-burn-from-climbing/
   *************************************/
  fun calories(duration: Duration,
               averageHeartRate: Int,
               metabolicEquivalentOfTask: Double = 0.0): Int
  {
    if (kilograms == 0.0) return 0

    return if (averageHeartRate != 0 && gender != Unspecified && age != 0)
    {
      (if (vO2max != 0.0)
      {
        if (gender == Male)
          (-95.7735 + (0.634 * averageHeartRate) + (0.404 * vO2max) + (0.394 * kilograms) + (0.271 * age)) / 4.184
        else
          (-59.3954 + (0.450 * averageHeartRate) + (0.380 * vO2max) + (0.103 * kilograms) + (0.274 * age)) / 4.184
      }
      else
      {
        if (gender == Male)
          (-55.0969 + (0.6309 * averageHeartRate) + (0.1988 * kilograms) + (0.2017 * age)) / 4.184
        else
          (-20.4022 + (0.4472 * averageHeartRate) - (0.1263 * kilograms) + (0.074 * age)) / 4.184

      } * (duration.seconds / 60.0)).roundToInt()
    }
    else
      calories(duration, metabolicEquivalentOfTask)

  }

  fun calories(duration: Duration, metabolicEquivalentOfTask: Double): Int
  {
    return (kilograms * metabolicEquivalentOfTask * (duration.seconds / 3600.0)).roundToInt()

  }

  fun clear()
  {
    gender = Unspecified
    age = 0
    kilograms = 0.0
    centimeters = 0.0
    restingHeartRate = 0
    maxHeartRate = 0.0
    vO2max = 0.0

  }
}