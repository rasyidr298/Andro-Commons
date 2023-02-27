package id.rrdev.commons.utils.abstractt

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.securepreferences.SecurePreferences
import id.rrdev.commons.model.dataIncome.Admob
import java.lang.reflect.Type


class PrefManager(
    context: Context,
    prefName: String
) {

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

    val spe: SharedPreferences.Editor by lazy {
        sp.edit()
    }

    //Pref AdsAdmob Banner
    fun spSaveAdsAdmob(list: List<Admob>) {
        val gson = Gson()
        val json: String = gson.toJson(list)
        spe.putString("spSaveAdsAdmob", json)
        spe.apply()
    }

    fun spGetAdsAdmob(): List<Admob>? {
        val gson = Gson()
        val json: String? = sp.getString("spSaveAdsAdmob", "")
        val type: Type = object : TypeToken<List<Admob?>?>() {}.type
        return gson.fromJson(json, type)
    }

}