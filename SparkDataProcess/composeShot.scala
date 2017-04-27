import org.apache.commons.io.IOUtils
import java.net.URL
import java.nio.charset.Charset

// Zeppelin creates and injects sc (SparkContext) and sqlContext (HiveContext or SqlContext)
// So you don't need create them manually

// load positionshot data
val positionshot = sc.textFile("PositionShot")

// load averageshot data
val averageshot = sc.textFile("averageShot")

val uniondata = positionshot.union(averageshot)
val result = uniondata.map(s => s.split(",")).map(s => (s(0), s(1))).groupByKey.flatMap(a => {
    val list = a._2.toList
    val average = list.find(x => x(0) == 'a').get.drop(1).toDouble
    list.filter(x => x(0) != 'a').map(x => x.split("-")).map(x => a._1 + "," + x(0) + "," + x(1) + "," + x(2) + "," + x(3) + "," + average)
})

result.coalesce(1,true).saveAsTextFile("ResultShot")
