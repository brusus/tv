import org.jetbrains.kotlin.konan.properties.Properties

// use an integer for version numbers
version = 6


cloudstream {
    // All of these properties are optional, you can safely remove them
    description =
        "Torrent da Il Corsaro Nero. If something doesn't work the torrent has probably not enough seeds"
    authors = listOf("doGior")

    /**
     * Status int as the following:
     * 0: Down
     * 1: Ok
     * 2: Slow
     * 3: Beta only
     * */
    status = 0

    tvTypes = listOf("Movie", "Torrent")

    requiresResources = false
    language = "it"

    iconUrl = "https://raw.githubusercontent.com/brusus/tv/main/CorsaroNero/corsaronero_icon.png"
}

android {
    buildFeatures {
        buildConfig = true
        viewBinding = true
    }
    defaultConfig {
        // Legge secrets.properties se presente; in mancanza si ricade
        // sulle variabili d'ambiente e infine su stringa vuota, cosi'
        // che la build funzioni anche senza il file.
        val properties = Properties()
        val secretsFile = project.rootProject.file("secrets.properties")
        if (secretsFile.exists()) {
            secretsFile.inputStream().use { properties.load(it) }
        }
        android.buildFeatures.buildConfig = true
        val tmdbApi = properties.getProperty("TMDB_API")
            ?: System.getenv("TMDB_API")
            ?: ""
        buildConfigField("String", "TMDB_API", "\"$tmdbApi\"")
    }
}
