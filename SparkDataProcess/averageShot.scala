import org.apache.commons.io.IOUtils
import java.net.URL
import java.nio.charset.Charset

// Zeppelin creates and injects sc (SparkContext) and sqlContext (HiveContext or SqlContext)
// So you don't need create them manually

// load player data
val playerText = sc.textFile("allShotsData")

case class Player(id: String, shots: Double)

// split each line, filter out header, and map it into Player case class
val shotmade = playerText.map(s=>s.split(",")).filter(s => s(0) != "\"\"").map(s=>(s(4).replaceAll("\"", ""),s(25).toInt)).reduceByKey(_ + _)

// split each line, filter out header, and map it into Player case class
val allshot = playerText.map(s=>s.split(",")).filter(s => s(0) != "\"\"").map(s=>(s(4).replaceAll("\"", ""),1)).reduceByKey(_ + _)

val uniondata = allshot.union(shotmade)

val resultdata = uniondata.map(s => (s._1, s._2.toDouble)).reduceByKey((a,b) => if(a >= b) b/a else a/b)

val result = resultdata.map(s => s._1.toString + ",a" + s._2.toString).coalesce(1,true).saveAsTextFile("averageShot")
