// use an integer for version numbers
version = 7

cloudstream {
    // All of these properties are optional, you can safely remove them

    description = "SerieTV da GuardaSerie"
    authors = listOf("DieGon")

    /**
    * Status int as the following:
    * 0: Down
    * 1: Ok
    * 2: Slow
    * 3: Beta only
    * */
    status = 1

    tvTypes = listOf("TvSeries","Cartoon")

    language = "it"

    iconUrl = "https://raw.githubusercontent.com/brusus/tv/main/GuardaSerie/GuardaSerie_icon.png"
}
