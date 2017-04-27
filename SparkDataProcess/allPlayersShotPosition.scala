import org.apache.commons.io.IOUtils
import java.net.URL
import java.nio.charset.Charset

// Zeppelin creates and injects sc (SparkContext) and sqlContext (HiveContext or SqlContext)
// So you don't need create them manually

// load player data
val playerText = sc.textFile("allShotsData")

case class Player(id:String, shots: Double)

def mapPos(x: Double, y: Double) : String = {
return ((x + 25) / 2 + 0.5).toInt.toString + "-" + (y / 2 + 0.5).toInt.toString
}

// split each line, filter out header, and map it into Player case class  
val shotmade = playerText.map(s=>s.split(",")).filter(s => s(0) != "\"\"").map(s => (s(4).replaceAll("\"", "") + "-" + mapPos(s(18).toDouble, s(19).toDouble), s(25).toInt)).reduceByKey(_ + _)

val allshot = playerText.map(s=>s.split(",")).filter(s => s(0) != "\"\"").map(s => (s(4).replaceAll("\"", "") + "-" + mapPos(s(18).toDouble, s(19).toDouble), 1)).reduceByKey(_ + _)

val uniondata = allshot.union(shotmade)
val resultdata = uniondata.map(s => (s._1, s._2.toDouble)).groupByKey.map(a => {
    var list = a._2.toList
    var idxy = a._1.split("-")
    idxy(0) + "," + idxy(1) + "-" + idxy(2) + "-" + list.max.toInt + "-" + list.min/list.max 
})

resultdata.coalesce(1,true).saveAsTextFile("PositionShot")
