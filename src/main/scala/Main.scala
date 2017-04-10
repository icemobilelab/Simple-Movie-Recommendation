import com.typesafe.scalalogging.LazyLogging
import org.joda.time.DateTime

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

  val inputData = CsvParser.readFile[DataRelation](inputFilename, line => DataRelation(line(0).toInt, line(1).toInt, line(2).toDouble, DateTime.parse(line(4)))) match {
    case Success(dataList) => dataList
    case Failure(exception) => {
      logger.error("Error: ", exception)
      List[DataRelation]()
    }
  }

  // Recommender
  // val outputData = something...


  // Write output
  CsvParser.writeFile[DataRelation](outputFilename, inputData, d => s"${d.movieId},${d.userId},${d.rate}")

}
