//package com.grappenmaker.aoc
//
//// Template for a VM
//data class Insn(val opcode: String, val params: List<Parameter>)
//private val Insn.a get() = params[0]
//private val Insn.b get() = params[1]
//private val Insn.c get() = params[2]
//
//sealed interface Parameter {
//    operator fun get(vm: VM): Int
//    operator fun set(vm: VM, value: Int)
//}
//
//data class RegisterParameter(val register: Int) : Parameter {
//    override fun get(vm: VM) = vm.registers[register]
//    override fun set(vm: VM, value: Int) {
//        vm.registers[register] = value
//    }
//}
//
//data class LiteralParameter(val value: Int) : Parameter {
//    override fun get(vm: VM) = value
//    override fun set(vm: VM, value: Int) = error("Cannot set a literal value!")
//}
//
//fun launchVM(input: List<String>) = VM(input.map {
//    val s = it.split(" ")
//    Insn(s.first(), s.drop(1).map { p ->
//        if (p.first().isDigit()) LiteralParameter(p.toInt()) else RegisterParameter(p.single() - 'a')
//    })
//})
//
//class VM(private val instructions: List<Insn>) {
//    var ptr = 0
//    var halted = false
//        private set
//
//    val registers = MutableList(10) { 0 }
//    var cycles = 0
//        private set
//
//    fun step() {
//        if (halted) return
//
//        val curr = instructions[ptr]
//        interpret(curr)
//
//        ptr += curr.params.size + 1
//        cycles++
//        if (ptr !in instructions.indices) halt()
//    }
//
//    fun interpret(insn: Insn) {
//        // Implementation
//        when (insn.opcode) {
//            else -> error("Invalid/unexpected opcode ${insn.opcode}")
//        }
//    }
//
//    fun halt() {
//        halted = true
//    }
//fun jump(offset: Int) {
//    ptr += offset - 1
//}
//
//    fun run(onStep: VM.() -> Unit = {}) {
//        while (!halted) {
//            step()
//            onStep()
//        }
//    }
//}