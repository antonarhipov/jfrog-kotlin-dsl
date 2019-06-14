import jetbrains.buildServer.configs.kotlin.v2018_2.BuildStep
import jetbrains.buildServer.configs.kotlin.v2018_2.BuildSteps
import jetbrains.buildServer.configs.kotlin.v2018_2.buildSteps.ScriptBuildStep

class JFrogCLI() : ScriptBuildStep() {

    var command: String? = null
    var source: String? = null
    var target: String? = null
    var buildName: String? = null
    var buildNumber: String? = null

    val UPLOAD = "u"
    val DOWNLOAD = "dl"
    val SAERCH = "s"
    val PUBLISHBUILDINFO = "bp"

    constructor(block: JFrogCLI.() -> Unit) : this() {
        block()
        if (command == UPLOAD) {
            scriptContent = """
            echo 'Hello!'
            JFROG_CLI_OFFER_CONFIG=false
            jfrog rt u $source $target --build-name=$buildName --buildNumber=$buildNumber --url=http://localhost:8040/artifactory/ --user=admin --password=password
            """.trimIndent()
        }
    }
}

fun BuildSteps.jfrog(block: JFrogCLI.() -> Unit): BuildStep {
    val result = JFrogCLI(block)
    step(result)
    return result
}