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
                JFROG_CLI_OFFER_CONFIG=false jfrog rt u target/artifact-1.0-SNAPSHOT.jar generic-local/artifact/%build.number%/ --build-number=$buildNumber --build-name=$buildName --url=http://localhost:8040/artifactory/ --user=admin --password=password
            """.trimIndent()
        }
        if (command == PUBLISHBUILDINFO) {
            scriptContent = """
                JFROG_CLI_OFFER_CONFIG=false jfrog rt bp --url=http://localhost:8040/artifactory/ --user=admin --password=password $buildName $buildNumber
            """.trimIndent()

        }
    }
}

fun BuildSteps.jfrog(block: JFrogCLI.() -> Unit): BuildStep {
    val result = JFrogCLI(block)
    step(result)
    return result
}