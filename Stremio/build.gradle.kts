import org.jetbrains.kotlin.konan.properties.Properties

version = 14

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
        val tmdbApi3 = properties.getProperty("TMDB_API3")
            ?: System.getenv("TMDB_API3")
            ?: ""
        buildConfigField("String", "TMDB_API3", "\"$tmdbApi3\"")
    }
}

dependencies {
    implementation("com.google.android.material:material:1.13.0")
}

cloudstream {
    language = "it"
    description = "Addon di Stremio su Cloudstream"
    authors = listOf("Hexated, phisher98, DieGon")
    status = 3
    tvTypes = listOf(
        "TvSeries",
        "Movie",
    )
    requiresResources = true
    iconUrl = "https://raw.githubusercontent.com/brusus/tv/main/Stremio/stremio_icon.png"
}
