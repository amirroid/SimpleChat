package ir.amirroid.simplechat

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform