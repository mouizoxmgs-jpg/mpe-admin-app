package com.mpe.admin.data.repository

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mpe.admin.data.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

class FirebaseRepository {
    private val db = Firebase.firestore
    private val httpClient = OkHttpClient()

    fun observeVillas(): Flow<List<Property>> = callbackFlow {
        val listener = db.collection("villas").addSnapshotListener { snap, error ->
            if (error != null) { close(error); return@addSnapshotListener }
            trySend(snap?.documents?.mapNotNull { doc ->
                doc.data?.let { Property.fromMap(it, doc.id) }
            } ?: emptyList())
        }
        awaitClose { listener.remove() }
    }

    suspend fun saveVilla(p: Property) =
        db.collection("villas").document(p.id).set(p.toMap()).await()

    suspend fun deleteVilla(id: String) =
        db.collection("villas").document(id).delete().await()

    fun observeApartments(): Flow<List<Property>> = callbackFlow {
        val listener = db.collection("apartments").addSnapshotListener { snap, error ->
            if (error != null) { close(error); return@addSnapshotListener }
            trySend(snap?.documents?.mapNotNull { doc ->
                doc.data?.let { Property.fromMap(it, doc.id) }
            } ?: emptyList())
        }
        awaitClose { listener.remove() }
    }

    suspend fun saveApartment(p: Property) =
        db.collection("apartments").document(p.id).set(p.toMap()).await()

    suspend fun deleteApartment(id: String) =
        db.collection("apartments").document(id).delete().await()

    fun observeCars(): Flow<List<Car>> = callbackFlow {
        val listener = db.collection("cars").addSnapshotListener { snap, error ->
            if (error != null) { close(error); return@addSnapshotListener }
            trySend(snap?.documents?.mapNotNull { doc ->
                doc.data?.let { Car.fromMap(it, doc.id) }
            } ?: emptyList())
        }
        awaitClose { listener.remove() }
    }

    suspend fun saveCar(c: Car) =
        db.collection("cars").document(c.id).set(c.toMap()).await()

    suspend fun deleteCar(id: String) =
        db.collection("cars").document(id).delete().await()

    fun observeSettings(): Flow<AppSettings> = callbackFlow {
        val listener = db.document("settings/main").addSnapshotListener { snap, error ->
            if (error != null) { close(error); return@addSnapshotListener }
            trySend(snap?.data?.let { AppSettings.fromMap(it) } ?: AppSettings())
        }
        awaitClose { listener.remove() }
    }

    suspend fun saveSettings(s: AppSettings) =
        db.document("settings/main").set(s.toMap()).await()

    suspend fun uploadToImgBB(key: String, base64Image: String): String = withContext(Dispatchers.IO) {
        val body = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("image", base64Image)
            .build()
        val request = Request.Builder()
            .url("https://api.imgbb.com/1/upload?key=$key")
            .post(body)
            .build()
        val response = httpClient.newCall(request).execute()
        val json = JSONObject(response.body?.string() ?: error("Réponse vide"))
        json.getJSONObject("data").getString("url")
    }
}
