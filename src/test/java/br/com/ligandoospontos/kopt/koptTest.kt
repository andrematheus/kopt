package br.com.ligandoospontos.kopt

import org.testng.annotations.Test

/**
 * Copyright © 2016 amatheus.
 * See LICENSE.md for more information.
 */

@Test
fun testOneFlagPassed() {
    var result = false
    commandLine(arrayOf("-x")) {
        options {
            flag("x")
        }
        execute { options ->
            val (x) = options
            result = x as Boolean
        }
    }
    assert(result)
}