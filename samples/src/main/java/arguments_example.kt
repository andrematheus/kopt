import br.com.ligandoospontos.kopt.commandLine

fun main(args: Array<String>) {
    commandLine(args) {
        options {
            flag("v")
            flag("q")
            flag("r")
        }

        execute { options ->
            val (v, q, r) = options
            print("Got arguments: v='$v', q='$q', r='$r'")
        }
    }
}