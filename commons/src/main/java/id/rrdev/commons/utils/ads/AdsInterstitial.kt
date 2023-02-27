package id.rrdev.commons.utils.ads

import android.app.Activity
import android.content.Context
import android.util.Log
import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxAdListener
import com.applovin.mediation.MaxError
import com.applovin.mediation.ads.MaxInterstitialAd
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.startapp.sdk.adsbase.StartAppAd
import id.rrdev.commons.model.dataIncome.Admob
import id.rrdev.commons.utils.abstractt.PrefManager


object AdsInterstitial {
    var mAdmob: InterstitialAd? = null
    private var mApplovin: MaxInterstitialAd? = null
    private var isFailApplovin = false


    /*-------LOAD INTERSTISIAL------*/
    private fun loadAdmob(
        context: Context,
        activity: Activity,
        adRequest: AdRequest,
        dataAdsInters: Admob
    ) {
        InterstitialAd.load(
            context,
            dataAdsInters.adsIdAdmob!!,
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    Log.e("loadAdsAdmob", adError.message)
                    mAdmob = null
                    loadApplovin(activity, dataAdsInters)
                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    Log.e("onAdLoaded", interstitialAd.toString())
                    mAdmob = interstitialAd
                }
            })
    }

    private fun loadApplovin(
        activity: Activity,
        dataAdsInters: Admob
    ) {
        mApplovin = MaxInterstitialAd(dataAdsInters.adsIdApplovin, activity)
        mApplovin!!.loadAd()
        mApplovin!!.setListener(object : MaxAdListener {
            override fun onAdLoaded(ad: MaxAd?) {
                Log.e("onAdLoaded", ad.toString())
            }

            override fun onAdLoadFailed(adUnitId: String?, error: MaxError?) {
                Log.e("loadAdsIntersAppLovin", error!!.message)
                mApplovin = null
                isFailApplovin = true
            }

            override fun onAdDisplayed(ad: MaxAd?) {
                Log.e("onAdDisplayed", ad.toString())
            }

            override fun onAdHidden(ad: MaxAd?) {
                Log.e("onAdHidden", ad.toString())
                mApplovin!!.loadAd()
            }

            override fun onAdClicked(ad: MaxAd?) {}
            override fun onAdDisplayFailed(ad: MaxAd?, error: MaxError?) {
                Log.e("onAdDisplayFailed", error!!.message)
                mApplovin!!.loadAd()
            }
        })
    }

    private fun loadMaster(
        context: Context,
        activity: Activity,
        adRequest: AdRequest,
        prefManager: PrefManager,
        idName: String
    ): Admob {
        val dataAdsInters = AdsUtils.getAdsInPref(prefManager, idName)
        if (dataAdsInters.isEnable!!) {
            if (mAdmob == null) {
                loadAdmob(context, activity, adRequest, dataAdsInters)
            }
        }
        return dataAdsInters
    }

    fun loadInActivityOrFragment(
        context: Context,
        activity: Activity,
        adRequest: AdRequest,
        prefManager: PrefManager
    ) {
        loadMaster(context, activity, adRequest, prefManager, "interstisial_activity")
    }

    fun loadInDialog(
        context: Context,
        activity: Activity,
        adRequest: AdRequest,
        prefManager: PrefManager
    ) {
        loadMaster(context, activity, adRequest, prefManager, "interstisial_all_dialog")
    }

    fun isNotLoad(): Boolean {
        if (mAdmob == null && mApplovin == null)
            return true
        return false
    }


    /*---------SHOW INTERSTISIAL-------*/
    fun showInActivity(
        context: Context,
        activity: Activity,
        prefManager: PrefManager
    ) {
        val dataAdsIntersDetail = AdsUtils.getAdsInPref(prefManager, "interstisial_activity")
        if (dataAdsIntersDetail.isEnable!!) {
            if (mAdmob != null) {
                showAdmob(activity)
            } else if (mApplovin != null) {
                showApplovin()
            } else if (isFailApplovin) {
                showStartio(context)
            }
        }
    }

    fun showInDialog(
        context: Context,
        activity: Activity,
        prefManager: PrefManager
    ) {
        val dataAdsIntersDialog = AdsUtils.getAdsInPref(prefManager, "interstisial_all_dialog")
        if (dataAdsIntersDialog.isEnable!!) {
            if (mAdmob != null) {
                showAdmob(activity)
            } else if (mApplovin != null) {
                showApplovin()
            } else if (isFailApplovin) {
                showStartio(context)
            }
        }
    }


    /*----------INTERSTISIAL OPERATION-------*/
    private fun showAdmob(
        activity: Activity
    ) {
        if (mAdmob != null) {
            mAdmob?.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    mAdmob = null
                }

                override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                    super.onAdFailedToShowFullScreenContent(p0)
                    Log.e("onAdFailedToShowFull", p0.message)
                }
            }

            mAdmob?.show(activity)
        } else {
            Log.e("showAdsIntersAdmob", "ads inters not show")
        }
    }

    private fun showApplovin() {
        if (mApplovin!!.isReady) {
            mApplovin!!.showAd()
            mApplovin = null
        }
    }

    private fun showStartio(
        context: Context
    ) {
        StartAppAd.AdMode.AUTOMATIC
        StartAppAd.showAd(context)
        isFailApplovin = false
    }

}