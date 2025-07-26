package com.grappenmaker.aoc.practice25.everybodycodes

import java.awt.Toolkit
import java.awt.datatransfer.StringSelection
import java.io.ByteArrayOutputStream
import java.net.URI
import java.net.URLConnection
import java.nio.file.Path
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import kotlin.io.path.Path
import kotlin.io.path.createDirectories
import kotlin.io.path.exists
import kotlin.io.path.readLines
import kotlin.io.path.readText
import kotlin.io.path.writeBytes
import kotlin.io.path.writeText
import kotlin.reflect.KProperty
import kotlin.system.measureNanoTime

fun getEveryoneCodesDir(year: Int) = Path("inputs", "everybodycodes", year.toString())
    .also { if (!it.exists()) it.createDirectories() }

fun cursedParseJSONMap(input: String): Map<String, String> {
    if (input.length < 2) return emptyMap()
    if (input[0] != '{' || input[1] != '"') error("Does not appear to be a basic json dict")

    var idx = 1
    fun advance() {
        idx = input.indexOf('"', idx + 1)
    }

    fun parseString(): String {
        if (idx < 0) error("Does not appear to be a basic json dict")

        val start = idx
        advance()
        if (idx < 0) error("Does not appear to be a basic json dict")

        val res = input.substring(start + 1, idx)
        advance()
        return res
    }

    val res = hashMapOf<String, String>()

    while (idx in input.indices) {
        res[parseString()] = parseString()
    }

    return res
}

fun getToken() = Path(".EVERYBODY-CODES-TOKEN").readText()
fun getSeed() = Path(".EVERYBODY-CODES-SEED").readText().toInt()

fun getKeysFile(year: Int, quest: Int) =
    getEveryoneCodesDir(year).resolve("${quest.toString().padStart(2, '0')}-keys.json")

fun fetchKeys(year: Int, quest: Int, token: String) = downloadAndParse(
    getKeysFile(year, quest),
    """https://everybody.codes/api/event/$year/quest/$quest""",
    exists = false
) { setRequestProperty("Cookie", "everybody-codes=$token") }

fun getInputsFile(year: Int, quest: Int) =
    getEveryoneCodesDir(year).resolve("${quest.toString().padStart(2, '0')}-inputs-encoded.json")

fun getInputFile(year: Int, quest: Int, part: Int) =
    getEveryoneCodesDir(year).resolve("${quest.toString().padStart(2, '0')}-input-$part.txt")

fun downloadAndParse(target: Path, url: String, exists: Boolean = true, extra: URLConnection.() -> Unit = {}): Map<String, String> {
    if (exists && target.exists()) return cursedParseJSONMap(target.readText())

    with(URI(url).toURL().openConnection()) {
        extra()
        inputStream.use {
            val baos = ByteArrayOutputStream()
            it.copyTo(baos)

            val bytes = baos.toByteArray()
            target.writeBytes(bytes)

            val text = bytes.decodeToString()
            return cursedParseJSONMap(text)
        }
    }
}

fun fetchInputs(year: Int, quest: Int, seed: Int) = downloadAndParse(
    getInputsFile(year, quest),
    """https://everybody-codes.b-cdn.net/assets/$year/$quest/input/$seed.json?v=${System.currentTimeMillis()}"""
)

val years: Map<Int, Map<Int, ECSolveContext.() -> Unit>> = mapOf(
    2024 to mapOf(
        1 to ECSolveContext::day01,
        2 to ECSolveContext::day02,
        3 to ECSolveContext::day03,
    )
)

fun main(args: Array<String>) {
    val year = if (args.size >= 1) args[0].toInt() else 2024
    val quest = if (args.size >= 2) args[1].toInt() else 1
    val impl = years[year]?.get(quest) ?: error("Could not find impl")

    val puzzle = ECPuzzle(year, quest, impl)
    val context = ECSolveContext(puzzle)

    println("Day ${puzzle.quest}")
    val timeTaken = measureNanoTime { puzzle.implementation(context) }

    println()
    println(
        """
        |Part 1: ${context.partOneDelegate.underlying}
        |Part 2: ${context.partTwoDelegate.underlying}
        |Part 3: ${context.partThreeDelegate.underlying}
        """.trimMargin()
    )

    println()
    println("Took ${timeTaken / 1_000_000}ms to calculate the solution")

    listOf(
        context.partOneDelegate,
        context.partTwoDelegate,
        context.partThreeDelegate
    ).findLast { it.touched }?.let { d ->
        runCatching {
            val selection = StringSelection(d.underlying)
            Toolkit.getDefaultToolkit().systemClipboard.setContents(selection, selection)

            println("Copied \"${d.underlying}\" to clipboard")
        }.onFailure { println("Failed to copy to clipboard!") }
    }
}

// bad copy pasting, this isnt supposed to be particularly good code so it is fine
data class ECPuzzle(
    val year: Int,
    val quest: Int,
    val implementation: ECSolveContext.() -> Unit
)

fun fetchKey(year: Int, quest: Int, part: Int): String {
    val file = getKeysFile(year, quest)

    if (file.exists()) {
        val keys = cursedParseJSONMap(file.readText())
        if ("key$part" in keys) return keys.getValue("key$part")
    }

    val fetched = fetchKeys(year, quest, getToken())
    if ("key$part" in fetched) return fetched.getValue("key$part")

    error("Part not unlocked yet it seems!")
}

class ECInput(private val year: Int, private val quest: Int, private val part: Int) {
    @OptIn(ExperimentalStdlibApi::class)
    val inputLines by lazy {
        val file = getInputFile(year, quest, part)
        if (file.exists()) return@lazy file.readLines()

        val key = fetchKey(year, quest, part)
        val inputs = fetchInputs(year, quest, getSeed())
        val encoded = inputs.getValue(part.toString())

        val decoded = with(Cipher.getInstance("AES/CBC/PKCS5Padding")) {
            init(
                Cipher.DECRYPT_MODE,
                SecretKeySpec(key.encodeToByteArray(), "AES"),
                IvParameterSpec(key.substring(0, 16).encodeToByteArray())
            )

            doFinal(encoded.hexToByteArray()).decodeToString()
        }

        file.writeText(decoded)
        decoded.lines()
    }

    val rawInput by lazy { inputLines.joinToString("\n") }
    val input by lazy { rawInput.trim() }
}

class ECSolveContext(puzzle: ECPuzzle) {
    inner class PartDelegate {
        var underlying = "Not implemented"

        var touched = false
            private set

        operator fun getValue(thisRef: Any?, property: KProperty<*>) = underlying
        operator fun setValue(thisRef: Any?, property: KProperty<*>, value: Any) {
            touched = true
            underlying = value.toString()
        }
    }

    val partOneInput = ECInput(puzzle.year, puzzle.quest, 1)
    val partTwoInput = ECInput(puzzle.year, puzzle.quest, 2)
    val partThreeInput = ECInput(puzzle.year, puzzle.quest, 3)

    val partOneDelegate = PartDelegate()
    val partTwoDelegate = PartDelegate()
    val partThreeDelegate = PartDelegate()

    var partOne: Any by partOneDelegate
    var partTwo: Any by partTwoDelegate
    var partThree: Any by partThreeDelegate
}