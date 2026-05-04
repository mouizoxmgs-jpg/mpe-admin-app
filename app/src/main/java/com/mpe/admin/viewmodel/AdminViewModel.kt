package com.mpe.admin.viewmodel

import android.app.Application
import android.content.Context
import android.net.Uri
import android.util.Base64
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.mpe.admin.data.model.*
import com.mpe.admin.data.repository.FirebaseRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

private const val ADMIN_PASSWORD = "MPEADMIN@MOUIZ"
private const val AUTH_KEY = "mpe_admin_auth"

class AdminViewModel(application: Application) : AndroidViewModel(application) {

    private val prefs = application.getSharedPreferences("mpe_prefs", Context.MODE_PRIVATE)
    private val repository = FirebaseRepository()

    private val _isAuthenticated = MutableStateFlow(prefs.getBoolean(AUTH_KEY, false))
    val isAuthenticated: StateFlow<Boolean> = _isAuthenticated.asStateFlow()

    private val _villas = MutableStateFlow<List<Property>>(emptyList())
    val villas: StateFlow<List<Property>> = _villas.asStateFlow()

    private val _apartments = MutableStateFlow<List<Property>>(emptyList())
    val apartments: StateFlow<List<Property>> = _apartments.asStateFlow()

    private val _cars = MutableStateFlow<List<Car>>(emptyList())
    val cars: StateFlow<List<Car>> = _cars.asStateFlow()

    private val _settings = MutableStateFlow(AppSettings())
    val settings: StateFlow<AppSettings> = _settings.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _firebaseConnected = MutableStateFlow(false)
    val firebaseConnected: StateFlow<Boolean> = _firebaseConnected.asStateFlow()

    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage: StateFlow<String?> = _toastMessage.asStateFlow()

    init {
        startListeners()
    }

    private fun startListeners() {
        viewModelScope.launch {
            try {
                launch {
                    repository.observeVillas().collect {
                        _villas.value = it
                        _firebaseConnected.value = true
                        _isLoading.value = false
                    }
                }
                launch { repository.observeApartments().collect { _apartments.value = it } }
                launch { repository.observeCars().collect { _cars.value = it } }
                launch { repository.observeSettings().collect { _settings.value = it } }
            } catch (e: Exception) {
                _isLoading.value = false
                showToast("Firebase non disponible")
            }
        }
    }

    fun login(password: String): Boolean {
        return if (password == ADMIN_PASSWORD) {
            _isAuthenticated.value = true
            prefs.edit().putBoolean(AUTH_KEY, true).apply()
            true
        } else false
    }

    fun logout() {
        _isAuthenticated.value = false
        prefs.edit().remove(AUTH_KEY).apply()
    }

    // Villas
    fun addVilla(property: Property) {
        val p = property.copy(id = System.currentTimeMillis().toString())
        viewModelScope.launch {
            runCatching { repository.saveVilla(p) }
                .onFailure { showToast("Erreur ajout villa: ${it.message}") }
        }
    }

    fun updateVilla(property: Property) {
        viewModelScope.launch {
            runCatching { repository.saveVilla(property) }
                .onFailure { showToast("Erreur modification: ${it.message}") }
        }
    }

    fun deleteVilla(id: String) {
        viewModelScope.launch {
            runCatching { repository.deleteVilla(id) }
                .onFailure { showToast("Erreur suppression: ${it.message}") }
        }
    }

    // Apartments
    fun addApartment(property: Property) {
        val p = property.copy(id = System.currentTimeMillis().toString())
        viewModelScope.launch {
            runCatching { repository.saveApartment(p) }
                .onFailure { showToast("Erreur ajout appartement: ${it.message}") }
        }
    }

    fun updateApartment(property: Property) {
        viewModelScope.launch {
            runCatching { repository.saveApartment(property) }
                .onFailure { showToast("Erreur modification: ${it.message}") }
        }
    }

    fun deleteApartment(id: String) {
        viewModelScope.launch {
            runCatching { repository.deleteApartment(id) }
                .onFailure { showToast("Erreur suppression: ${it.message}") }
        }
    }

    // Cars
    fun addCar(car: Car) {
        val c = car.copy(id = System.currentTimeMillis().toString())
        viewModelScope.launch {
            runCatching { repository.saveCar(c) }
                .onFailure { showToast("Erreur ajout véhicule: ${it.message}") }
        }
    }

    fun updateCar(car: Car) {
        viewModelScope.launch {
            runCatching { repository.saveCar(car) }
                .onFailure { showToast("Erreur modification: ${it.message}") }
        }
    }

    fun deleteCar(id: String) {
        viewModelScope.launch {
            runCatching { repository.deleteCar(id) }
                .onFailure { showToast("Erreur suppression: ${it.message}") }
        }
    }

    // Settings
    fun saveSettings(s: AppSettings) {
        viewModelScope.launch {
            runCatching {
                repository.saveSettings(s)
                _settings.value = s
                showToast("Paramètres sauvegardés ✓")
            }.onFailure { showToast("Erreur sauvegarde: ${it.message}") }
        }
    }

    // Image Upload
    suspend fun uploadImageToImgBB(imageUri: Uri): String? {
        val key = _settings.value.imgbbKey
        if (key.isEmpty()) {
            showToast("Clé ImgBB non configurée dans les paramètres")
            return null
        }
        return runCatching {
            val inputStream = getApplication<Application>().contentResolver.openInputStream(imageUri)
                ?: return null
            val bytes = inputStream.readBytes()
            val base64 = Base64.encodeToString(bytes, Base64.DEFAULT)
            repository.uploadToImgBB(key, base64)
        }.onFailure { showToast("Erreur upload: ${it.message}") }.getOrNull()
    }

    fun showToast(msg: String) { _toastMessage.value = msg }
    fun clearToast() { _toastMessage.value = null }
}
