import com.typesafe.scalalogging.LazyLogging
import org.joda.time.DateTime

import scala.collection.mutable.ListBuffer
import scala.util.{Failure, Success}

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

  val users = ListBuffer[String]()
  val header = ListBuffer[String]()

  val matrix = ListBuffer[List[Double]]()

  CsvParser.readFile(inputFilename, line => {
    users += line(1)
    matrix += (2 until line.length).map(i => line(i).toDouble).toList
  }, firstLine => {
    header ++= firstLine.slice(1, firstLine.length)
  }) match {
    case Success(dataList) => dataList
    case Failure(exception) =>
      logger.error("Error: ", exception)
  }

  // Recommender
  val outputMatrix = Recommender.runRecommendations(matrix.toList)

  // Write output
  CsvParser.writeFile(outputFilename, outputMatrix, header.toList, users.toList)

  logger.info("Done")
}
