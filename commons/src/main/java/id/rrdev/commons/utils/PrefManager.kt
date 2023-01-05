package id.rrdev.commons.utils

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.google.gson.GsonBuilder
import com.securepreferences.SecurePreferences


class PrefManager(context: Context, prefName: String) {

    //    encrypt
    val sp: SharedPreferences by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val spec = KeyGenParameterSpec.Builder(
                MasterKey.DEFAULT_MASTER_KEY_ALIAS,
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            )
                .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setKeySize(MasterKey.DEFAULT_AES_GCM_MASTER_KEY_SIZE)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .build()
            val masterKey = MasterKey.Builder(context)
                .setKeyGenParameterSpec(spec)
                .build()

            EncryptedSharedPreferences.create(
                context,
                prefName,
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
        } else {
            SecurePreferences(context, prefName+"_password", prefName)
        }
    }

//    no encrypt
//    private val sp: SharedPreferences by lazy {
//        context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
//    }

    private val spe: SharedPreferences.Editor by lazy {
        sp.edit()
    }

    fun spGetInt(key: String, defValue: Int): Int {
        return sp.getInt(key, defValue)
    }

    fun spPutInt(key: String, value: Int) {
        spe.putInt(key, value)
        spe.apply()
    }

    fun spGetString(key: String, defValue: String): String? {
        return sp.getString(key, defValue)
    }

    fun spPutString(key: String, value: String) {
        spe.putString(key, value)
        spe.apply()
    }

    fun spGetBoolean(key: String, defValue: Boolean): Boolean? {
        return sp.getBoolean(key, defValue)
    }

    fun spPutBoolean(key: String, value: Boolean) {
        spe.putBoolean(key, value)
        spe.apply()
    }

    fun spGetFloat(key: String, defValue: Float): Float? {
        return sp.getFloat(key, defValue)
    }

    fun spPutFloat(key: String, value: Float) {
        spe.putFloat(key, value)
        spe.apply()
    }

    fun spGetLong(key: String, defValue: Long): Long? {
        return sp.getLong(key, defValue)
    }

    fun spPutLong(key: String, value: Long) {
        spe.putLong(key, value)
        spe.apply()
    }

    fun <T> spPutListAny(`object`: T, key: String) {
        val jsonString = GsonBuilder().create().toJson(`object`)
        spe.putString(key, jsonString).apply()
    }

    inline fun <reified T> spGetListAny(key: String): T? {
        val value = sp.getString(key, "")
        return GsonBuilder().create().fromJson(value, T::class.java)
    }

    //Pref AdsAdmob Banner
//    fun spSaveAdsAdmob(list: List<Admob>) {
//        val gson = Gson()
//        val json: String = gson.toJson(list)
//        spe.putString(PREF_ADS_ADMOB, json)
//        spe.apply()
//    }
//
//    fun spGetAdsAdmob(): List<Admob>? {
//        val gson = Gson()
//        val json: String? = sp.getString(PREF_ADS_ADMOB, "")
//        val type: Type = object : TypeToken<List<Admob?>?>() {}.type
//        return gson.fromJson(json, type)
//    }

}