import java.io.{BufferedWriter, FileWriter}

import scala.collection.mutable.ListBuffer
import scala.io.Source
import scala.util.Try

object CsvParser {

  def readFile(inputFilename: String, parser: Array[String] => Unit, parserFirstLine: Array[String] => Unit): Try[Unit] = Try({
    val buffer = Source.fromFile(inputFilename)

    var firstLine = true

    for (line <- buffer.getLines()) {
      val values = line.split(",").map(_.trim.replace("\"", ""))
      if (firstLine) {
        parserFirstLine(values)
        firstLine = false
      } else {
        parser(values)
      }
    }
    buffer.close()
  })

  def writeFile(outputFilename: String, matrix: ListBuffer[ListBuffer[Double]], header: List[String], rowNames: List[String]): Unit = Try({
    val writer = new BufferedWriter(new FileWriter(outputFilename))
    writer.write( s"${header.mkString(",")}\n" )
    for (i <- rowNames.indices) {
      writer.write(s"${rowNames(i)},${matrix(i).mkString(",")}\n")
    }
    writer.close()
  })

}
