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
	compileSdkVersion(30)

	defaultConfig {
		minSdkVersion(14)
		targetSdkVersion(30)
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

	compileOptions {
		sourceCompatibility = JavaVersion.VERSION_1_8
		targetCompatibility = JavaVersion.VERSION_1_8
	}
	kotlinOptions {
		jvmTarget = "1.8"
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
