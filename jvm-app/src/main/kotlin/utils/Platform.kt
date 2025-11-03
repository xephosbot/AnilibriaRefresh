package utils

enum class Platform {
    Windows, Linux, MacOS;

    companion object {
        fun getCurrent(): Platform {
            val os = System.getProperty("os.name").lowercase()
            return when {
                "win" in os -> Windows
                "mac" in os -> MacOS
                "nux" in os || "nix" in os -> Linux
                else -> throw IllegalStateException("Unsupported operating system: $os")
            }
        }
    }
}
