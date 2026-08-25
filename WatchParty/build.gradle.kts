@file:Suppress("UnstableApiUsage")

import org.jetbrains.kotlin.konan.properties.Properties

plugins {
    id("org.jetbrains.kotlin.plugin.serialization") version "2.4.0"
}

dependencies {
    implementation("com.google.android.material:material:1.4.0")
    implementation("com.squareup.okhttp3:okhttp:4.12.0") // WebSocket puro Kotlin, nessuna libreria nativa
    // compileOnly: solo per compilare contro le classi app; a runtime vengono dall'app
    compileOnly("androidx.navigation:navigation-fragment-ktx:2.7.7")
    compileOnly("com.jaredrummler:colorpicker:1.1.0")
}


version = 3

android {
    defaultConfig {
        // Legge secrets.properties se presente; in mancanza si ricade
        // sulle variabili d'ambiente e infine su stringa vuota, cosi'
        // che la build funzioni anche senza il file.
        val properties = Properties()
        val secretsFile = project.rootProject.file("secrets.properties")
        if (secretsFile.exists()) {
            secretsFile.inputStream().use { properties.load(it) }
        }
        val watchPartyRelay = properties.getProperty("WATCHPARTY_RELAY")
            ?: System.getenv("WATCHPARTY_RELAY")
            ?: ""
        buildConfigField("String", "WATCHPARTY_RELAY", "\"$watchPartyRelay\"")
    }

    buildFeatures {
        buildConfig = true
        viewBinding = true
    }
}

cloudstream {
    authors = listOf("DieGon")

    /**
     * Status int:
     * 0: Down
     * 1: Ok
     * 2: Slow
     * 3: Beta only
     * */
    status = 3 

    tvTypes = listOf(
        "Others",
    )

    iconUrl = "https://raw.githubusercontent.com/brusus/tv/main/WatchParty/WatchParty_icon.png"
    description = "⚠️ BETA ⚠️ Watch movies and TV series together in real-time with live chat (Up to 5 users)."
    requiresResources = true
}
