import org.apache.commons.io.IOUtils
import java.net.URL
import java.nio.charset.Charset

// Zeppelin creates and injects sc (SparkContext) and sqlContext (HiveContext or SqlContext)
// So you don't need create them manually

// load player data
val playerText = sc.textFile("shot_logs.csv")

case class Player(id:String,  firstlast : String, toyear : String)

// split each line, filter out header, and map it into Player case class  
val player = playerText.map(s=>s.split(",")).filter(s=>s(0)!="GAME_ID").map(
    s=>Player(s(1), 
            s(3).replaceAll("\"", ""),
            s(4).replaceAll("\"", "").split(",")(1)
        )
)

// convert to DataFrame and create temporal table
player.toDF().registerTempTable("player")
