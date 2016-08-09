package br.com.ligandoospontos.kopt

/**
 * Copyright © 2016 amatheus.
 * See LICENSE.md for more information.
 */

interface Option {
    fun parse(args: Array<String>): OptionParseResult
}

class FlagOption(val shortName: String): Option {
    override fun parse(args: Array<String>): OptionParseResult {
        val arg = args.firstOrNull { it == "-" + shortName }
        if (arg != null) {
            return OptionParseResult.OK(true)
        } else {
            return OptionParseResult.OK(false)
        }
    }
}

sealed class OptionParseResult(val valid: Boolean) {
    class OK(val value: Any) : OptionParseResult(true)
    class Error : OptionParseResult(false)
}

class OptionsBuilder {
    var options: MutableList<Option> = mutableListOf()

    fun flag(shortName: String) {
        options.add(FlagOption(shortName))
    }

    fun options(): List<Option> {
        return options
    }
}

class CommandLineBuilder {
    var options: List<Option> = listOf()
    var mainBody: ((List<Any>) -> Unit)? = null

    fun options(init: OptionsBuilder.() -> Unit) {
        val builder = OptionsBuilder()
        builder.init()
        options = builder.options()
    }

    fun execute(body: (List<Any>) -> Unit) {
        mainBody = body
    }

    fun tryParse(args: Array<String>) {
        val results = options.map { it.parse(args) }
        if (results.all { it is  OptionParseResult.OK }) {
            val parameters = results.map { (it as OptionParseResult.OK).value }
            val body = mainBody
            if (body != null) {
                body(parameters)
            } else {
                throw IllegalArgumentException("execute body must not be null.")
            }
        } else {
            println("Oops!")
        }
    }
}

fun commandLine(args: Array<String>, init: CommandLineBuilder.() -> Unit) {
    val builder = CommandLineBuilder()
    builder.init()
    builder.tryParse(args)
}