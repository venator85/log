plugins {
	id("com.android.library")
	kotlin("android")
	id("org.jetbrains.kotlinx.binary-compatibility-validator") version "0.5.0"
	`maven-publish`
}

group = "eu.alessiobianchi"
val LIBRARY_VERSION: String by project
version = LIBRARY_VERSION

android {
	compileSdk = 30

	defaultConfig {
		minSdk = 14
		targetSdk = 30
	}
	buildTypes {
		getByName("release") {
			isMinifyEnabled = false
		}
	}
	buildFeatures {
		buildConfig = false
		resValues = false
		shaders = false
		viewBinding = false
	}
}

dependencies {
}

afterEvaluate {
	publishing {
		repositories {
			maven {
				val REPO_PATH: String by project
				url = uri(REPO_PATH)
			}
		}
		publications {
			create<MavenPublication>(project.name.kebabCaseToLowerCamelCase()) {
				from(project.components["release"])
				configureMavenPublication()
			}
		}
	}
}
