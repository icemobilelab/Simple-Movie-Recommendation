object Recommender {

  def recommend(input: List[DataRelation]): List[DataRelation] = {

    

    List[DataRelation]()
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
