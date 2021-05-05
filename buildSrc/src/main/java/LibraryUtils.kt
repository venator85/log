import org.gradle.api.publish.maven.MavenPublication

fun MavenPublication.configureMavenPublication() {
	pom {
		description.set("A lightweight logger for Android.")
		url.set("https://github.com/venator85/log")
		licenses {
			license {
				name.set("The Apache License, Version 2.0")
				url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
			}
		}
		developers {
			developer {
				id.set("alessiobianchi")
				name.set("Alessio Bianchi")
				url.set("https://github.com/venator85/")
			}
		}
		scm {
			connection.set("scm:git:git://github.com/venator85/log.git")
			developerConnection.set("scm:git:ssh://git@github.com/venator85/log.git")
			url.set("https://github.com/venator85/log")
		}
	}
}
