import org.apache.commons.io.IOUtils
import java.net.URL
import java.nio.charset.Charset

// Zeppelin creates and injects sc (SparkContext) and sqlContext (HiveContext or SqlContext)
// So you don't need create them manually

// load player data
val playerText = sc.textFile("allShotsData")

val shotmade = playerText.map(s=>s.split(",")).filter(s => s(0) != "\"\"").map(s=> s(4).replaceAll("\"", "") + "," + s(5).replaceAll("\"", "")).distinct()
shotmade.coalesce(1,true).saveAsTextFile("2015allplayers")
