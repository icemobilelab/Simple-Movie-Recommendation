import scala.collection.mutable.ListBuffer

object Recommender {

  def runRecommendations(rateMatrix: List[List[Double]]): ListBuffer[ListBuffer[Double]] = {

    val simMatrix = calculateSimMatrix(rateMatrix)

    val recommendations = ListBuffer[ListBuffer[Double]]()

    for (userId <- rateMatrix.indices) {
      recommendations += ListBuffer[Double]()
      for (productId <- rateMatrix(userId).indices) {
        recommendations(userId) += recommend(userId, productId, simMatrix, rateMatrix)
      }
    }

    recommendations
  }

  def calculateSimMatrix(rateMatrix: List[List[Double]]): ListBuffer[ListBuffer[Double]] = {
    val simMatrix = ListBuffer[ListBuffer[Double]]()

    val transposedRateMatrix = rateMatrix.transpose

    for (i <- transposedRateMatrix.indices) {
      simMatrix += ListBuffer[Double]()
      for (j <- transposedRateMatrix.indices) {
        simMatrix(i) += cosineSimilarity(transposedRateMatrix(i), transposedRateMatrix(j))
      }
    }

    simMatrix
  }

  def recommend(userId: Int, productId: Int, simMatrix: ListBuffer[ListBuffer[Double]], rateMatrix: List[List[Double]]): Double = {
    var numerator:Double = 0
    var denominator:Double = 0

    for (i <- simMatrix.indices) {
      val sim = simMatrix(i)(productId)
      val rate = rateMatrix(userId)(i)
      numerator += sim * rate
      denominator += sim
    }

    numerator / denominator
  }

  def cosineSimilarity(l1: List[Double], l2: List[Double]): Double = {
    val numerator = l1.indices.foldLeft[Double](0)((acc, x) => acc + l1(x)*l2(x))
    val denominator = vectorMagnitude(l1) * vectorMagnitude(l2)
    numerator / denominator
  }

  def vectorMagnitude(l: List[Double]): Double = {
    Math.sqrt( l.fold[Double](0)((acc, l) => acc + Math.pow(l, 2)) )
  }

}
