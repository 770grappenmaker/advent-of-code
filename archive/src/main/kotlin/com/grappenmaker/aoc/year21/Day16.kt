package com.grappenmaker.aoc.year21

import com.grappenmaker.aoc.PuzzleSet

fun PuzzleSet.day16() = puzzle(day = 16) {
    // Retrieve decoder
    val decoder = Decoder(input.chunked(2).map { it.toInt(16).toByte() }.toByteArray().toBits())
    val packet = parsePacket(decoder)
    partOne = packet.getAllPackets().sumOf { it.version }.s()
    partTwo = packet.value.s()
}

// Parses a packet and the packets it contains
private fun parsePacket(decoder: Decoder): Packet {
    val version = decoder.nextInt(3)
    val type = decoder.nextInt(3)

    return if (type == 4) parseLiteral(version, decoder) else parseOperator(version, type, decoder)
}

// Parses a literal value packet, nothing more than that
private fun parseLiteral(version: Int, decoder: Decoder): LiteralPacket {
    val value = decoder.nextUntil(5) { !it.first() }.flatMap { it.drop(1) }.toBooleanArray().toLong()
    return LiteralPacket(version, value)
}

// Parses an operator packet, and the packets it might contain
private fun parseOperator(version: Int, type: Int, decoder: Decoder) = if (decoder.nextInt(1) == 0) {
    val subPacketsDecoder = Decoder(decoder.next(decoder.nextInt(15)))
    val subPackets = mutableListOf<Packet>()
    while (subPacketsDecoder.hasNext()) subPackets.add(parsePacket(subPacketsDecoder))

    OperatorPacket(version, type, subPackets)
} else {
    OperatorPacket(version, type, (1..decoder.nextInt(11)).map { parsePacket(decoder) })
}

// Abstract class representing a packet
private sealed class Packet(val version: Int) {
    abstract val value: Long // Might get big?
}

// Utility to get all packets that this packet eventually contains
private fun Packet.getAllPackets(): List<Packet> = when (this) {
    is LiteralPacket -> listOf(this)
    is OperatorPacket -> packets.flatMap { it.getAllPackets() } + this
}

// Class representing the literal value packet
private class LiteralPacket(version: Int, override val value: Long) : Packet(version)

// Class representing the operator packet. The value of the packet
// is evaluated after constructing the packet
private class OperatorPacket(
    version: Int,
    operation: Int,
    val packets: List<Packet>
) : Packet(version) {
    override val value: Long = when (operation) {
        0 -> packets.sumOf { it.value }
        1 -> packets.fold(1L) { acc, cur -> acc * cur.value }
        2 -> packets.minOf { it.value }
        3 -> packets.maxOf { it.value }
        5 -> (packets.first().value > packets.last().value).toInt()
        6 -> (packets.first().value < packets.last().value).toInt()
        7 -> (packets.first().value == packets.last().value).toInt()
        else -> error("Impossible... who touched my input?!?!")
    }.toLong()
}

// Utility for decoding bits
// Booleans represent a bit, 1 = true and 0 = false
class Decoder(private val iterator: BooleanIterator) : Iterator<Boolean> by iterator {
    constructor(booleans: BooleanArray) : this(booleans.iterator())

    // Read a boolean array (read a number of bits)
    fun next(width: Int) = (1..width).map { next() }.toBooleanArray()

    // Read a number of bits and parse it to an integer
    fun nextInt(width: Int) = next(width).toInt()

    // Read while condition is met
    inline fun nextWhile(width: Int, condition: (BooleanArray) -> Boolean): List<BooleanArray> {
        val result = mutableListOf<BooleanArray>()

        // Eww i hate do-whiles
        do {
            val newValue = next(width)
            result.add(newValue)
        } while (condition(newValue))

        return result
    }

    // Read until condition is not met
    inline fun nextUntil(width: Int, condition: (BooleanArray) -> Boolean) =
        nextWhile(width) { !condition(it) }
}

// Bits parsing utility
// Tries to shift all bits into an int
private fun BooleanArray.toInt() =
    foldIndexed(0) { idx, acc, cur -> acc or (cur.toInt() shl size - idx - 1) }

// Tries to shift all bits into a long
private fun BooleanArray.toLong() =
    foldIndexed(0L) { idx, acc, cur -> acc or (cur.toInt().toLong() shl size - idx - 1) }

// Converts a boolean to an integer
fun Boolean.toInt() = if (this) 1 else 0

// Converts an integer to a boolean
fun Int.toBoolean() = this == 1

// Gets the bit of a byte at a specific index
fun Byte.getBit(n: Int): Boolean {
    if (n !in (0..7)) error("Invalid index")
    return (this.toInt() shr 7 - n and 1).toBoolean()
}

// Gets all bits of a byte
fun Byte.getBits(): BooleanArray = (0..7).map { getBit(it) }.toBooleanArray()

// Converts all bytes in a byte array to bits
fun ByteArray.toBits(): BooleanArray = flatMap { it.getBits().asIterable() }.toBooleanArray()