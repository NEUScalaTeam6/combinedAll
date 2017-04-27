import org.apache.commons.io.IOUtils
import java.net.URL
import java.nio.charset.Charset

// Zeppelin creates and injects sc (SparkContext) and sqlContext (HiveContext or SqlContext)
// So you don't need create them manually

// load play data
val playerText = sc.textFile("allPlayerID.csv")

case class Player(id:String,  firstlast : String, toyear : String)

// split each line, filter out header, and map it into player case class  
val player = playerText.map(s=>s.split("\",\"")).filter(s=>s(0)!="\"").map(
    s=>Player(s(1), 
            s(3).replaceAll("\"", ""),
            s(4).replaceAll("\"", "").split(",")(1)
        )
)

// to filter all the players from season 1990
val player2 = player.filter(s => s.toyear.toInt >= 1990)

// convert to DataFrame and create temporal table
player2.toDF().registerTempTable("player")
