package com.mpe.admin.data.model

data class AppSettings(
    val phone: String = "212600000000",
    val instagram: String = "",
    val snapchat: String = "",
    val tiktok: String = "",
    val imgbbKey: String = ""
) {
    fun toMap(): Map<String, Any> = mapOf(
        "phone" to phone,
        "instagram" to instagram,
        "snapchat" to snapchat,
        "tiktok" to tiktok,
        "imgbbKey" to imgbbKey
    )

    companion object {
        fun fromMap(map: Map<String, Any>) = AppSettings(
            phone = map["phone"] as? String ?: "212600000000",
            instagram = map["instagram"] as? String ?: "",
            snapchat = map["snapchat"] as? String ?: "",
            tiktok = map["tiktok"] as? String ?: "",
            imgbbKey = map["imgbbKey"] as? String ?: ""
        )
    }
}
