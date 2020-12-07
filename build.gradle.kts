import com.github.jengelman.gradle.plugins.processes.tasks.Fork
//import tools.requestShutdown
//import tools.waitUntilServerIsUp

plugins {
	id("org.gradle.kotlin.kotlin-dsl") version "1.4.9" apply false

	kotlin("jvm") version "1.4.20"
//	kotlin("multiplatform") version "1.4.20"

	id("com.github.johnrengelman.processes") version "0.5.0"
}

apply {
//	from("../docker.gradle.kts")
}

repositories {
	mavenLocal()
	jcenter()
}

val boahey64ServerDependency by configurations.creating

dependencies {
	val fluentleniumVersion = "3.8.1"
	val seleniumVersion = "3.141.59"

	testCompile(kotlin("stdlib-jdk8"))
	testCompile(kotlin("test-junit"))
	testCompile(kotlin("reflect"))

	testCompile("org.assertj:assertj-core:3.12.1")
	testCompile("net.wuerl.kotlin:assertj-core-kotlin:0.2.1")

	testCompile("org.fluentlenium:fluentlenium-junit:$fluentleniumVersion")
	testCompile("org.fluentlenium:fluentlenium-assertj:$fluentleniumVersion")

	testCompile("io.github.bonigarcia:webdrivermanager:3.6.1")
	testCompile("org.seleniumhq.selenium:selenium-java:$seleniumVersion")
	testCompile("net.lightbody.bmp:browsermob-core:2.1.5")

	testCompile("org.awaitility:awaitility:3.1.6")

	compile("io.github.microutils:kotlin-logging:1.7.6")

	testCompile("com.googlecode.junit-toolbox:junit-toolbox:2.4")
	testCompile("com.fasterxml.jackson.core:jackson-databind")
	testCompile("com.fasterxml.jackson.module:jackson-module-kotlin") {
		exclude(module = "kotlin-reflect")
	}
	testCompile("com.fasterxml.jackson.core:jackson-annotations")
}

configurations {
	all {
		exclude(module = "slf4j-log4j12")
		exclude(module = "log4j")

		resolutionStrategy {
			eachDependency {
				if (requested.group.startsWith("com.fasterxml")) {
					useVersion("2.9.6")
				}
			}
		}
	}
}

val runSmokeTests = project.hasProperty("RUN-SMOKE-TESTS")

tasks {
	val test by getting(Test::class) {
		if (runSmokeTests) {
			useJUnit {
				includeCategories("boahey64.config.ProductionTests")
			}
		} else {
			useJUnit {
				excludeCategories("boahey64.config.ProductionTests")
			}
		}
		systemProperty("browser", System.getProperty("browser"))

		inputs.files(boahey64ServerDependency)
	}
}


