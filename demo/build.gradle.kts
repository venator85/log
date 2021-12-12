plugins {
	id("com.android.application")
	kotlin("android")
}

android {
	compileSdk = 31

	defaultConfig {
		applicationId = "eu.alessiobianchi.log.demo"
		minSdk = 14
		targetSdk = 30
		versionCode = 1
		versionName = "1.0"
	}

	buildTypes {
		getByName("release") {
			isMinifyEnabled = false
		}
	}
}

dependencies {
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.5.2")
	implementation("androidx.appcompat:appcompat:1.4.0")
	implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.4.0")
	implementation(project(path = ":log"))

	testImplementation("junit:junit:4.13.2")
}
