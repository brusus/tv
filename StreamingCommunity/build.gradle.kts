import org.jetbrains.kotlin.konan.properties.Properties
// use an integer for version numbers

version = 54


cloudstream {
    // All of these properties are optional, you can safely remove them

    description = "Film e SerieTV da StreamingCommunity"
    authors = listOf("Nanduuu03","DieGon")

    /**
    * Status int as the following:
    * 0: Down
    * 1: Ok
    * 2: Slow
    * 3: Beta only
    * */
    status = 1
    tvTypes = listOf(
        "TvSeries",
        "Movie",
        "Documentary",
        "Cartoon"
    )

    requiresResources = true
    language = "it"

    iconUrl = "https://raw.githubusercontent.com/brusus/tv/main/StreamingCommunity/streamingunity_icon.png"
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

dependencies {
    implementation("com.google.android.material:material:1.12.0")
}
