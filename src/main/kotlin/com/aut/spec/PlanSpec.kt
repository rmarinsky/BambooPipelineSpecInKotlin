package com.aut.spec

import com.atlassian.bamboo.specs.api.BambooSpec
import com.atlassian.bamboo.specs.api.builders.notification.Notification
import com.atlassian.bamboo.specs.api.builders.permission.PlanPermissions
import com.atlassian.bamboo.specs.api.builders.plan.Job
import com.atlassian.bamboo.specs.api.builders.plan.Plan
import com.atlassian.bamboo.specs.api.builders.plan.Stage
import com.atlassian.bamboo.specs.api.builders.plan.artifact.Artifact
import com.atlassian.bamboo.specs.api.builders.project.Project
import com.atlassian.bamboo.specs.api.builders.requirement.Requirement
import com.atlassian.bamboo.specs.builders.notification.JobFailedNotification
import com.atlassian.bamboo.specs.builders.notification.ResponsibleRecipient
import com.atlassian.bamboo.specs.builders.task.ScriptTask
import com.atlassian.bamboo.specs.builders.task.TestParserTask
import com.atlassian.bamboo.specs.model.task.TestParserTaskProperties.TestType.JUNIT
import com.atlassian.bamboo.specs.util.BambooServer
import com.atlassian.bamboo.specs.api.builders.docker.DockerConfiguration as docker


/**
 * Plan configuration for Bamboo.
 *
 * @see [Bamboo Specs](https://docs.atlassian.com/bamboo-specs-docs/latest/specs.html?java#project)
 */
@BambooSpec
class PlanSpec {

    var defaultPermission: PlanPermissions =
        PlanPermissions(runApiTestsPlan().identifier).addDefaultPermissions()

    private fun aut(): Project = Project().key("AUT")

    private fun runApiTestsPlan(): Plan =
        Plan(aut(), "Run API tests for application under test", "RATAUT")

    fun createPlanToRunTests(): Plan {
        return runApiTestsPlan()
            .stages(
                Stage("Run API tests stage")
                    .jobs(
                        Job("Run tests and report ones", "RTR")
                            .dockerConfiguration(docker().image("gradle:7.2.0-jdk11"))
                            .requirements(Requirement("system.docker.executable"))
                            .tasks(
                                scriptRunApiTestsAndReport(),
                                junitTestParser()
                            )
                            .artifacts(
                                allureReportArtifact()
                            )
                    )
            )
            .notifications(
                Notification()
                    .recipients(ResponsibleRecipient())
                    .type(JobFailedNotification())
            )
    }

    private fun scriptRunApiTestsAndReport(): ScriptTask {
        return ScriptTask()
            .inlineBody("gradle apiTests allureReport --clean")
            .interpreterShell()
    }

    private fun junitTestParser(): TestParserTask {
        return TestParserTask(JUNIT)
            .resultDirectories("build/test-results/**/*.xml")
            .description("Pick up JUnit 5 test results")
    }

    private fun allureReportArtifact(): Artifact {
        return Artifact("Allure Report")
            .location("build/reports/allure-report/allureReport/")
            .copyPattern("**/*")
            .required(true)
    }

    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
            val bambooServer = BambooServer("http://localhost")

            val plan = PlanSpec().createPlanToRunTests()

            bambooServer.publish(plan)

            val planPermission = PlanSpec().defaultPermission
            bambooServer.publish(planPermission)
        }
    }
}
