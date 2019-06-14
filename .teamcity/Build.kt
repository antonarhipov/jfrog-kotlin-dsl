import jetbrains.buildServer.configs.kotlin.v2018_2.BuildType
import jetbrains.buildServer.configs.kotlin.v2018_2.DslContext
import jetbrains.buildServer.configs.kotlin.v2018_2.buildSteps.MavenBuildStep
import jetbrains.buildServer.configs.kotlin.v2018_2.buildSteps.maven
import jetbrains.buildServer.configs.kotlin.v2018_2.buildSteps.script
import jetbrains.buildServer.configs.kotlin.v2018_2.triggers.vcs

object Build : BuildType({
    name = "Build"

    vcs {
        root(DslContext.settingsRoot)
    }

    val artifact = "target/artifact-1.0-SNAPSHOT.jar"

    steps {
        maven {
            goals = "clean package"
            runnerArgs = "-Dmaven.test.failure.ignore=true"
            localRepoScope = MavenBuildStep.RepositoryScope.MAVEN_DEFAULT
            jdkHome = "%env.JDK_18%"
        }
        jfrog {
            command = UPLOAD
            source = artifact
            target = "generic-local/artifact/%build.number%/"
            buildName = "artifact"
            buildNumber = "%build.number%"
        }
        jfrog {
            command = PUBLISHBUILDINFO
            buildName = "artifact"
            buildNumber = "%build.number%"
        }
    }

    triggers {
        vcs {
        }
    }

})
