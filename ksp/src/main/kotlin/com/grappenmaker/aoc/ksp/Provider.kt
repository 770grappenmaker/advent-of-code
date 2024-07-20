package com.grappenmaker.aoc.ksp

import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider

class Provider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment) =
        Processor(environment.codeGenerator, environment.options)
}