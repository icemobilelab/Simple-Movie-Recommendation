import java.io.{BufferedWriter, FileWriter}

import scala.collection.mutable.ListBuffer
import scala.io.Source
import scala.util.Try

object CsvParser {

  def readFile[T](inputFilename: String, parser: (Array[String]) => T ): Try[List[T]] = Try({
    val buffer = Source.fromFile(inputFilename)
    val objectList = ListBuffer[T]()

    for (line <- buffer.getLines()) {
      val values = line.split(",").map(_.trim)
      objectList += parser(values)
    }
    buffer.close()

    objectList.toList
  })

  def writeFile[T](outputFilename: String, objectList: List[T], parser: T => String): Unit = Try({
    val writer = new BufferedWriter(new FileWriter(outputFilename))
    objectList.foreach(obj => writer.write(s"${parser(obj)}\n"))
    writer.close()
  })

}
