package com.mpe.admin.data.model

data class CarSpecs(
    val engine: String = "",
    val power: String = "",
    val seats: String = ""
)

data class Car(
    val id: String = "",
    val name: String = "",
    val category: String = "luxury",
    val image: String = "",
    val images: List<String> = emptyList(),
    val specs: CarSpecs = CarSpecs(),
    val tagline: LocalizedText = LocalizedText(),
    val badge: LocalizedText = LocalizedText()
) {
    fun toMap(): Map<String, Any> = mapOf(
        "id" to id,
        "name" to name,
        "category" to category,
        "image" to image,
        "images" to images,
        "specs" to mapOf("engine" to specs.engine, "power" to specs.power, "seats" to specs.seats),
        "tagline" to mapOf("fr" to tagline.fr, "en" to tagline.en),
        "badge" to mapOf("fr" to badge.fr, "en" to badge.en)
    )

    companion object {
        fun fromMap(map: Map<String, Any>, id: String): Car {
            val specsMap = map["specs"] as? Map<*, *>
            val taglineMap = map["tagline"] as? Map<*, *>
            val badgeMap = map["badge"] as? Map<*, *>
            val imagesList = (map["images"] as? List<*>)?.filterIsInstance<String>() ?: emptyList()
            return Car(
                id = id,
                name = map["name"] as? String ?: "",
                category = map["category"] as? String ?: "luxury",
                image = map["image"] as? String ?: imagesList.firstOrNull() ?: "",
                images = imagesList,
                specs = CarSpecs(
                    engine = specsMap?.get("engine") as? String ?: "",
                    power = specsMap?.get("power") as? String ?: "",
                    seats = specsMap?.get("seats") as? String ?: ""
                ),
                tagline = LocalizedText(
                    fr = taglineMap?.get("fr") as? String ?: "",
                    en = taglineMap?.get("en") as? String ?: ""
                ),
                badge = LocalizedText(
                    fr = badgeMap?.get("fr") as? String ?: "",
                    en = badgeMap?.get("en") as? String ?: ""
                )
            )
        }
    }
}
