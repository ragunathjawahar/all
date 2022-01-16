package xyz.ragunath.all.fixtures

enum class Roast {
  Light, Medium, Dark
}

enum class GrindSize {
  Fine, MediumFine, Medium, Coarse
}

data class Coffee(
  val estate: String,
  val roast: Roast,
  val grindSize: GrindSize
)
