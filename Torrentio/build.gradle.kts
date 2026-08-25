import org.jetbrains.kotlin.konan.properties.Properties

// use an integer for version numbers
version = 10

cloudstream {
    description =
        "Torrent da Torrentio"
    authors = listOf("doGior","DieGon")

    // Status int as the following:
    // 0: Down
    // 1: Ok
    // 2: Slow
    // 3: Beta only
    status = 1

    tvTypes = listOf("Movie", "TvSeries", "Torrent", "Documentary")

    // TODO: when I find a fix for the SearchResult, I will add back the search function
    // setDataTypeLink = true (crossplatform) is needed for that
    // I also removed that function due to incompatibility with openSettings
    requiresResources = true
    language = "it"

    iconUrl = "https://torrentio.strem.fun/images/logo_v1.png"
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
    implementation("androidx.appcompat:appcompat:1.7.1")
    implementation("com.google.android.material:material:1.13.0")
}
