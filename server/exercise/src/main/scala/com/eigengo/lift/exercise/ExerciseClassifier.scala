package com.eigengo.lift.exercise

import akka.actor.Actor
import com.eigengo.lift.exercise.ExerciseClassifier._

import scala.util.Random

/**
 * The exercise classification model
 */
sealed trait ExerciseModel {
  def apply(classify: Classify): ClassifiedExercise
}

/**
 * Implementation left as an exercise
 */
case object WaveletModel extends ExerciseModel {
  val metadata = ModelMetadata(1)
  override def apply(classify: Classify): ClassifiedExercise = UnclassifiedExercise(metadata, classify.session)
}

/**
 * Implementation left as an exercise
 */
case object DynamicTimeWrappingModel extends ExerciseModel {
  val metadata = ModelMetadata(1)
  override def apply(classify: Classify): ClassifiedExercise = UnclassifiedExercise(metadata, classify.session)
}

/**
 * This is the only implementation I can have a go at!
 */
case object NaiveModel extends ExerciseModel {
  val metadata = ModelMetadata(2)
  override def apply(classify: Classify): ClassifiedExercise =
    FullyClassifiedExercise(metadata, classify.session, 1.0, "Goku was your spotter, man!", Some(Random.nextDouble()))
}

/**
 * Companion object for the classifier
 */
object ExerciseClassifier {

  /**
   * Classify the given accelerometer data together with session information
   * @param session the session
   * @param ad the accelerometer data
   */
  case class Classify(session: Session, ad: AccelerometerData)

  /**
   * Model version and other metadata
   * @param version the model version
   */
  case class ModelMetadata(version: Int)

  /**
   * ADT holding the classification result
   */
  sealed trait ClassifiedExercise

  /**
   * Known exercise with the given confidence, name and optional intensity
   * @param metadata the model metadata
   * @param session the session
   * @param confidence the confidence
   * @param name the exercise name
   * @param intensity the intensity, if known
   */
  case class FullyClassifiedExercise(metadata: ModelMetadata, session: Session, confidence: Double, name: ExerciseName, intensity: Option[ExerciseIntensity]) extends ClassifiedExercise

  /**
   * Unknown exercise
   * @param metadata the model
   * @param session the session
   */
  case class UnclassifiedExercise(metadata: ModelMetadata, session: Session) extends ClassifiedExercise

}

/**
 * Match the received exercise data using the given model
 * @param model the model
 */
class ExerciseClassifier(model: ExerciseModel) extends Actor {

  override def receive: Receive = {
    case c@Classify(_, _) =>
      Thread.sleep(300 + Random.nextInt(1000)) // Is complicated, no? :)

      sender() ! model(c)
  }

}
