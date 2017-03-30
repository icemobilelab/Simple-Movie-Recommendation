import com.typesafe.scalalogging.LazyLogging

object Main extends App with LazyLogging {

  logger.info("Simple Movie Recommendation - v0.1.0")

  if (args.length < 2) {
    println("Missing arguments. Usage:")
    println("\tjava -jar recommender.jar <input> <output>\n")
    println("\t<input>:\tthe filename of the input CSV")
    println("\t<output>:\tthe filename of the output CSV")
    System.exit(0)
  }

  val inputFilename = args(0)
  val outputFilename = args(1)

  logger.info(s"Input CSV is $inputFilename")
  logger.info(s"Output CSV is $outputFilename ")

}
