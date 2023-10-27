import java.net.*
import java.io.File
import java.time.OffsetDateTime
import java.time.ZoneOffset

fun main(args: Array<String>) {
    val unlockOffset: ZoneOffset = ZoneOffset.ofHours(-5)
    val token = File(".TOKEN").readText().trim()
    val now = OffsetDateTime.now().withOffsetSameInstant(unlockOffset)
    val day = (args.firstOrNull()?.toIntOrNull() ?: now.dayOfMonth).coerceIn(1..25)
    val year = args.getOrNull(1)?.toIntOrNull() ?: now.year
    println("Fetching day $day year $year")

    val file = File("inputs/$year", "day-${"%02d".format(day)}.txt")
    println("Writing to $file")

    with(URI("https://adventofcode.com/$year/day/$day/input").toURL().openConnection() as HttpURLConnection) {
        requestMethod = "GET"
        setRequestProperty("Cookie", "session=$token")
        setRequestProperty("User-Agent", "HttpURLConnection gh:770grappenmaker")
        inputStream.use { it.copyTo(file.outputStream()) }
    }
}