package com.grappenmaker.aoc.year17

import com.grappenmaker.aoc.*
import com.grappenmaker.aoc.ksp.PuzzleEntry

@PuzzleEntry
fun PuzzleSet.day20() = puzzle(day = 20) {
    data class Particle(val pos: Point3D, val vel: Point3D, val acc: Point3D)

    fun Particle.step(): Particle {
        val newVel = vel + acc
        return copy(pos = pos + newVel, vel = newVel)
    }

    val particles = inputLines.map { l ->
        val (pos, vel, acc) = l.splitInts().windowed(3, 3) { (a, b, c) -> Point3D(a, b, c) }
        Particle(pos, vel, acc)
    }

    partOne = particles.withIndex().minBy { it.value.acc.manhattanDistance }.index.s()
    partTwo = generateSequence(particles) { curr ->
        val newParticles = curr.map(Particle::step)
        val positions = newParticles.map { it.pos }.frequencies()
        newParticles.filter { positions.getValue(it.pos) == 1 }
    }.drop(30).windowed(10).first { w -> w.allIdenticalBy { it.size } }.first().size.s()
}