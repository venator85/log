plugins {
	id("com.android.application")
	kotlin("android")
}

android {
	compileSdkVersion(30)

	defaultConfig {
		applicationId("eu.alessiobianchi.log.demo")
		minSdkVersion(14)
		targetSdkVersion(30)
		versionCode(1)
		versionName("1.0")
	}

	buildTypes {
		getByName("release") {
			isMinifyEnabled = false
		}
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
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.4.3")
	implementation("androidx.appcompat:appcompat:1.3.0-rc01")
	implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.3.1")
	implementation(project(path = ":log"))
}
