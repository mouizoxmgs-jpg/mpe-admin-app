package com.mpe.admin.data.model

data class LocalizedText(
    val fr: String = "",
    val en: String = ""
)

data class Property(
    val id: String = "",
    val name: LocalizedText = LocalizedText(),
    val bedrooms: Int = 0,
    val bathrooms: Int = 0,
    val kitchen: Int = 0,
    val parking: Int = 0,
    val pool: Int = 0,
    val image: String = "",
    val images: List<String> = emptyList(),
    val badge: LocalizedText = LocalizedText(),
    val price: String = ""
) {
    fun toMap(): Map<String, Any> = mapOf(
        "id" to id,
        "name" to mapOf("fr" to name.fr, "en" to name.en),
        "bedrooms" to bedrooms,
        "bathrooms" to bathrooms,
        "kitchen" to kitchen,
        "parking" to parking,
        "pool" to pool,
        "image" to image,
        "images" to images,
        "badge" to mapOf("fr" to badge.fr, "en" to badge.en),
        "price" to price
    )

    companion object {
        fun fromMap(map: Map<String, Any>, id: String): Property {
            val nameMap = map["name"] as? Map<*, *>
            val badgeMap = map["badge"] as? Map<*, *>
            val imagesList = (map["images"] as? List<*>)?.filterIsInstance<String>() ?: emptyList()
            return Property(
                id = id,
                name = LocalizedText(
                    fr = nameMap?.get("fr") as? String ?: "",
                    en = nameMap?.get("en") as? String ?: ""
                ),
                bedrooms = (map["bedrooms"] as? Long)?.toInt() ?: 0,
                bathrooms = (map["bathrooms"] as? Long)?.toInt() ?: 0,
                kitchen = (map["kitchen"] as? Long)?.toInt() ?: 0,
                parking = (map["parking"] as? Long)?.toInt() ?: 0,
                pool = (map["pool"] as? Long)?.toInt() ?: 0,
                image = map["image"] as? String ?: imagesList.firstOrNull() ?: "",
                images = imagesList,
                badge = LocalizedText(
                    fr = badgeMap?.get("fr") as? String ?: "",
                    en = badgeMap?.get("en") as? String ?: ""
                ),
                price = map["price"] as? String ?: ""
            )
        }
    }
}
