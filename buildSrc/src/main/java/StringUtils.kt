val kebabCase = """\-[a-zA-Z]""".toRegex()

fun String.kebabCaseToLowerCamelCase(): String {
	return kebabCase.replace(this) {
		it.value.replace("-", "").toUpperCase()
	}
}
