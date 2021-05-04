plugins {
	id("com.android.library")
	kotlin("android")
}

android {
	compileSdkVersion(30)

	defaultConfig {
		minSdkVersion(14)
		targetSdkVersion(30)
		versionCode(1)

		val VERSION_NAME: String by project
		versionName(VERSION_NAME)
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
