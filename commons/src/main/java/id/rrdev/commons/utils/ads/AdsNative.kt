package id.rrdev.commons.utils.ads

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import com.airbnb.lottie.LottieAnimationView
import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxError
import com.applovin.mediation.nativeAds.MaxNativeAdListener
import com.applovin.mediation.nativeAds.MaxNativeAdLoader
import com.applovin.mediation.nativeAds.MaxNativeAdView
import com.applovin.mediation.nativeAds.MaxNativeAdViewBinder
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
import com.startapp.sdk.ads.nativead.NativeAdDetails
import com.startapp.sdk.ads.nativead.NativeAdPreferences
import com.startapp.sdk.ads.nativead.StartAppNativeAd
import com.startapp.sdk.adsbase.Ad
import com.startapp.sdk.adsbase.adlisteners.AdEventListener
import id.rrdev.commons.R
import id.rrdev.commons.databinding.ItemAdNativeStartioBigBinding
import id.rrdev.commons.databinding.ItemAdNativeStartioSmallBinding
import id.rrdev.commons.extenstion.hide
import id.rrdev.commons.extenstion.show
import id.rrdev.commons.model.dataIncome.Admob

object AdsNative {

    //admob
    private fun populateAdmob(
        nativeAd: NativeAd,
        adView: NativeAdView
    ) {
        // Set the media view.
        adView.mediaView = adView.findViewById(R.id.ad_media)

        // Set other ad assets.
        adView.headlineView = adView.findViewById(R.id.ad_headline)
        adView.bodyView = adView.findViewById(R.id.ad_body)
        adView.callToActionView = adView.findViewById(R.id.ad_call_to_action)
        adView.iconView = adView.findViewById(R.id.ad_app_icon)
        adView.priceView = adView.findViewById(R.id.ad_price)
        adView.starRatingView = adView.findViewById(R.id.ad_stars)
        adView.storeView = adView.findViewById(R.id.ad_store)
        adView.advertiserView = adView.findViewById(R.id.ad_advertiser)

        // The headline and media content are guaranteed to be in every UnifiedNativeAd.
        (adView.headlineView as TextView).text = nativeAd.headline
        adView.mediaView!!.setMediaContent(nativeAd.mediaContent!!)

        // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
        // check before trying to display them.
        if (nativeAd.body == null) {
            adView.bodyView!!.visibility = View.INVISIBLE
        } else {
            adView.bodyView!!.visibility = View.VISIBLE
            (adView.bodyView as TextView).text = nativeAd.body
        }

        if (nativeAd.callToAction == null) {
            adView.callToActionView!!.visibility = View.INVISIBLE
        } else {
            adView.callToActionView!!.visibility = View.VISIBLE
            (adView.callToActionView as Button).text = nativeAd.callToAction
        }

        if (nativeAd.icon == null) {
            adView.iconView!!.visibility = View.GONE
        } else {
            (adView.iconView as ImageView).setImageDrawable(
                nativeAd.icon!!.drawable
            )
            adView.iconView!!.visibility = View.VISIBLE
        }

        if (nativeAd.price == null) {
            adView.priceView!!.visibility = View.INVISIBLE
        } else {
            adView.priceView!!.visibility = View.VISIBLE
            (adView.priceView as TextView).text = nativeAd.price
        }

        if (nativeAd.store == null) {
            adView.storeView!!.visibility = View.INVISIBLE
        } else {
            adView.storeView!!.visibility = View.VISIBLE
            (adView.storeView as TextView).text = nativeAd.store
        }

        if (nativeAd.starRating == null) {
            adView.starRatingView!!.visibility = View.INVISIBLE
        } else {
            (adView.starRatingView as RatingBar).rating = nativeAd.starRating!!.toFloat()
            adView.starRatingView!!.visibility = View.VISIBLE
        }

        if (nativeAd.advertiser == null) {
            adView.advertiserView!!.visibility = View.INVISIBLE
        } else {
            (adView.advertiserView as TextView).text = nativeAd.advertiser
            adView.advertiserView!!.visibility = View.VISIBLE
        }

        // This method tells the Google Mobile Ads SDK that you have finished populating your
        // native ad view with this native ad.
        adView.setNativeAd(nativeAd)
    }


    //stario
    fun populateStartIoBig(
        data: NativeAdDetails,
        binding: ItemAdNativeStartioBigBinding
    ) {
        with(binding) {
            tvTitle.text = data.title
            tvDescription.text = data.description
            if (data.imageBitmap != null) {
                ivIcon.setImageBitmap(data.imageBitmap)
            } else {
                ivIcon.setImageBitmap(data.secondaryImageBitmap)
            }
            data.registerViewForInteraction(root)
            btnInstall.text = if (data.isApp) "Install" else "Open"
            btnInstall.setOnClickListener { root.performClick() }
        }
    }

    private fun populateStarIoSmall(
        data: NativeAdDetails,
        binding: ItemAdNativeStartioSmallBinding
    ) {
        with(binding) {
            tvTitle.text = data.title
            tvDescription.text = data.description
            if (data.imageBitmap != null) {
                ivIcon.setImageBitmap(data.imageBitmap)
            } else {
                ivIcon.setImageBitmap(data.secondaryImageBitmap)
            }
            data.registerViewForInteraction(root)
            btnInstall.text = if (data.isApp) "Install" else "Open"
            btnInstall.setOnClickListener { root.performClick() }
        }
    }


    //applovin
    private fun populateApplovin(
        activity: Activity
    ): MaxNativeAdView {
        val binder: MaxNativeAdViewBinder =
            MaxNativeAdViewBinder.Builder(R.layout.item_ad_native_applovin)
                .setTitleTextViewId(R.id.title_text_view)
                .setBodyTextViewId(R.id.body_text_view)
                .setAdvertiserTextViewId(R.id.advertiser_textView)
                .setIconImageViewId(R.id.icon_image_view)
                .setMediaContentViewGroupId(R.id.media_view_container)
                .setOptionsContentViewGroupId(R.id.options_view)
                .setCallToActionButtonId(R.id.cta_button)
                .build()
        return MaxNativeAdView(binder, activity)
    }

    private fun populateCarouselApplovin(
        activity: Activity
    ): MaxNativeAdView {
        val binder: MaxNativeAdViewBinder =
            MaxNativeAdViewBinder.Builder(R.layout.item_ad_native_applovin_carousel)
                .setTitleTextViewId(R.id.ad_headline)
                .setBodyTextViewId(R.id.ad_body)
                .setAdvertiserTextViewId(R.id.ad_advertiser)
                .setIconImageViewId(R.id.ad_app_icon)
                .setMediaContentViewGroupId(R.id.ad_media)
                .setOptionsContentViewGroupId(R.id.linearLayout3)
                .setCallToActionButtonId(R.id.ad_call_to_action)
                .build()
        return MaxNativeAdView(binder, activity)
    }


    //native in RV
    fun showAdmobInRv(
        size: Int,
        dataNativeAds: Admob,
        nativeAdList: MutableList<NativeAd>?,
        context: Context,
        dataList: MutableList<Any>,
        adRequest: AdRequest,
        from: String,
        startIndex: Int,
        activity: Activity,
        onSubmit: () -> Unit
    ) {
        if (dataNativeAds.adsIdAdmob == "") {
            showApplovinInRv(
                activity,
                context,
                dataNativeAds,
                dataList,
                startIndex,
                from
            ) {
                onSubmit.invoke()
            }
        }else {
            nativeAdList?.clear()
            var adLoader: AdLoader? = null
            val maxLoadAd = AdsUtils.setMaxLoadAd(size, "", null, null)

            val builder = AdLoader.Builder(context, dataNativeAds.adsIdAdmob!!)
            adLoader = builder.forNativeAd { nativeAd ->
                AdsUtils.setMaxLoadAd(size, "setAdToList", nativeAdList, nativeAd)

                if (!adLoader!!.isLoading) {
                    try {
                        AdsUtils.inputAdToListAdmob(size, dataList, nativeAdList!!, from, startIndex)
                        onSubmit.invoke()
                    } catch (e: Exception) {
                    }
                }

            }.withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    Log.e("ads native $from", adError.message)
                    showApplovinInRv(
                        activity,
                        context,
                        dataNativeAds,
                        dataList,
                        startIndex,
                        from
                    ) {
                        onSubmit.invoke()
                    }
                }
            }
            ).build()

            if (dataNativeAds.isMultipleLoad!!) {
                adLoader.loadAds(adRequest, maxLoadAd)
            } else {
                adLoader.loadAd(adRequest)
            }
        }
    }

    fun showApplovinInRv(
        activity: Activity,
        context: Context,
        dataNativeAds: Admob,
        dataList: MutableList<Any>,
        startIndex: Int,
        from: String? = "",
        onSubmit: () -> Unit
    ) {
        if (dataNativeAds.adsIdApplovin == "") {
            showStartIoInRv(
                dataList.size, dataNativeAds, context, dataList, from!!, startIndex, 4
            ) {
                onSubmit.invoke()
            }
        }else {

            var nativeAd: MaxAd? = null
            val nativeAdLoader = MaxNativeAdLoader(dataNativeAds.adsIdApplovin, activity)
            nativeAdLoader.setNativeAdListener(object : MaxNativeAdListener() {

                override fun onNativeAdLoaded(nativeAdView: MaxNativeAdView?, ad: MaxAd) {
                    if (nativeAd != null) {
                        nativeAdLoader.destroy(nativeAd)
                    }
                    nativeAd = ad
                    if (dataList[startIndex] !is MaxNativeAdView) {
                        dataList.add(startIndex, nativeAdView!!)
                    }
                    onSubmit.invoke()

                    if (dataNativeAds.isMultipleLoad!!) {
                        showApplovinInRv(
                            activity,
                            context,
                            dataNativeAds,
                            dataList,
                            startIndex+5,
                            from
                        ) {
                            onSubmit.invoke()
                        }
                    }
                }

                override fun onNativeAdLoadFailed(adUnitId: String, error: MaxError) {
                    Log.e("ads native $from", error.toString())
                    showStartIoInRv(
                        dataList.size, dataNativeAds, context, dataList, from!!, startIndex, 4
                    ) {
                        onSubmit.invoke()
                    }
                }
            })

            if (from == "carousel") {
                nativeAdLoader.loadAd(populateCarouselApplovin(activity))
            } else {
                nativeAdLoader.loadAd(populateApplovin(activity))
            }
        }
    }

    fun showStartIoInRv(
        size: Int,
        dataNativeAds: Admob,
        context: Context,
        dataList: MutableList<Any>,
        from: String,
        startIndex: Int,
        sizeImageAsset: Int,
        onSubmit: () -> Unit
    ) {

        val nativeAd = StartAppNativeAd(context)
        val maxLoadAd = if (dataNativeAds.isMultipleLoad!!) AdsUtils.setMaxLoadAd(size, "", null, null) else 1
        nativeAd.loadAd(
            NativeAdPreferences()
                .setAdsNumber(maxLoadAd)
                .setAutoBitmapDownload(true)
                .setPrimaryImageSize(sizeImageAsset), object : AdEventListener {
                override fun onReceiveAd(ad: Ad) {
                    AdsUtils.inputAdToListStartIo(size, dataList, nativeAd.nativeAds, from, startIndex)
                    onSubmit.invoke()
                }

                override fun onFailedToReceiveAd(ad: Ad?) {
                    Log.e("ads native $from", ad!!.errorMessage!!)
                }
            }
        )
    }



    //native In Paging
    fun showAdmobInPaging(
        context: Context,
        activity: Activity,
        countItem: Int,
        dataNativeAds: Admob,
        adRequest: AdRequest,
        onSubmitAdmob: (native: NativeAd) -> Unit,
        onSubmitApplovin: (maxNativeAdView: MaxNativeAdView) -> Unit,
        onSubmitStartIo: (ArrayList<NativeAdDetails>) -> Unit
    ) {
        if (dataNativeAds.adsIdAdmob == "") {
            showApplovinInPaging(context, activity, countItem, dataNativeAds,{
                onSubmitApplovin.invoke(it)
            }) {
                onSubmitStartIo.invoke(it)
            }
        }else {
            var adLoader: AdLoader? = null
            val builder = AdLoader.Builder(context, dataNativeAds.adsIdAdmob!!)
            adLoader = builder.forNativeAd { nativeAd ->

                if (!adLoader!!.isLoading) {
                    try {
                        onSubmitAdmob.invoke(nativeAd)
                    } catch (e: Exception) {}
                }

            }.withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    Log.e("setupAdAdmobInPaging", adError.message)
                    showApplovinInPaging(context, activity, countItem, dataNativeAds,{
                        onSubmitApplovin.invoke(it)
                    }) {
                        onSubmitStartIo.invoke(it)
                    }
                }
            }).build()

            if (dataNativeAds.isMultipleLoad!!) {
                adLoader.loadAds(adRequest, 5)
            } else  {
                adLoader.loadAd(adRequest)
            }
        }
    }

    fun showApplovinInPaging(
        context: Context,
        activity: Activity,
        countItem: Int,
        dataNativeAds: Admob,
        onSubmitApplovin: (maxNativeAdView: MaxNativeAdView) -> Unit,
        onSubmitStartIo: (ArrayList<NativeAdDetails>) -> Unit
    ){
        if (dataNativeAds.adsIdApplovin == "") {
            showStartIoInPaging(context, countItem, dataNativeAds) {
                onSubmitStartIo.invoke(it)
            }
        }else {
            var nativeAd: MaxAd? = null
            val nativeAdLoader = MaxNativeAdLoader(dataNativeAds.adsIdApplovin, activity)
            nativeAdLoader.setNativeAdListener(object : MaxNativeAdListener() {

                override fun onNativeAdLoaded(nativeAdView: MaxNativeAdView?, ad: MaxAd) {
                    if (nativeAd != null) {
                        nativeAdLoader.destroy(nativeAd)
                    }
                    nativeAd = ad
                    onSubmitApplovin.invoke(nativeAdView!!)

                    if (dataNativeAds.isMultipleLoad!!) {
                        showStartIoInPaging(context, countItem, dataNativeAds) {
                            onSubmitStartIo.invoke(it)
                        }
                    }
                }

                override fun onNativeAdLoadFailed(adUnitId: String, error: MaxError) {
                    Log.e("setupAdApplovinPaging", error.message)
                    showStartIoInPaging(context, countItem, dataNativeAds) {
                        onSubmitStartIo.invoke(it)
                    }
                }
            })

            nativeAdLoader.loadAd(populateApplovin(activity))

        }
    }

    fun showStartIoInPaging(
        context: Context,
        countItem: Int,
        dataNativeAds: Admob,
        onSubmitStartIo: (ArrayList<NativeAdDetails>) -> Unit
    ){
        val nativeAd = StartAppNativeAd(context)
        val maxLoadAd = if (dataNativeAds.isMultipleLoad!!) AdsUtils.setMaxLoadAd(countItem, "setupAdStartIoPaging", null, null) else 1
        nativeAd.loadAd(
            NativeAdPreferences()
                .setAdsNumber(maxLoadAd)
                .setAutoBitmapDownload(true)
                .setPrimaryImageSize(4), object :
                AdEventListener {
                override fun onReceiveAd(ad: Ad) {
                    onSubmitStartIo.invoke(nativeAd.nativeAds)
                }

                override fun onFailedToReceiveAd(ad: Ad?) {
                    Log.e("setupAdStartioPaging", ad!!.errorMessage!!)
                }
            }
        )
    }


    //native Big
    fun showAdmobBig(
        dataNativeAds: Admob,
        context: Context,
        activity: Activity,
        fragmentActivity: FragmentActivity,
        dialogFragment: DialogFragment,
        nativeAdView: NativeAdView,
        frameLayout: FrameLayout,
        binding: ItemAdNativeStartioBigBinding,
        adRequest: AdRequest,
        lottieAnimationView: LottieAnimationView?,
        button: AppCompatButton?
    ) {
        if (dataNativeAds.adsIdAdmob == "") {
            showApplovinBig(
                dataNativeAds,
                context,
                fragmentActivity,
                dialogFragment,
                frameLayout,
                activity,
                lottieAnimationView,
                button,
                binding
            )
        }else {
            val builder = AdLoader.Builder(context, dataNativeAds.adsIdAdmob!!)
                .forNativeAd { nativeAd ->

                    if (fragmentActivity.isFinishing || fragmentActivity.isChangingConfigurations || dialogFragment.isRemoving || dialogFragment.activity == null || dialogFragment.isDetached || !dialogFragment.isAdded || dialogFragment.view == null) {
                        nativeAd.destroy()
                    } else {
                        populateAdmob(nativeAd, nativeAdView)
                        with(frameLayout) {
                            removeAllViews()
                            addView(nativeAdView)
                        }
                    }
                }
                .withAdListener(object : AdListener() {
                    override fun onAdLoaded() {
                        super.onAdLoaded()
                        lottieAnimationView?.hide()
                        button?.show()
                    }
                    override fun onAdFailedToLoad(adError: LoadAdError) {
                        Log.e("ads_native_big", adError.toString())
                        showApplovinBig(
                            dataNativeAds,
                            context,
                            fragmentActivity,
                            dialogFragment,
                            frameLayout,
                            activity,
                            lottieAnimationView,
                            button,
                            binding
                        )
                    }
                }).build()
            builder.loadAd(adRequest)
        }
    }

    fun showApplovinBig(
        dataNativeAds: Admob,
        context: Context,
        fragmentActivity: FragmentActivity,
        dialogFragment: DialogFragment,
        frameLayout: FrameLayout,
        activity: Activity,
        lottieAnimationView: LottieAnimationView?,
        button: AppCompatButton?,
        binding: ItemAdNativeStartioBigBinding
    ) {
        if (dataNativeAds.adsIdApplovin == "") {
            showStartIoBig(context, 4, binding, frameLayout, lottieAnimationView, button)
        }else {
            var nativeAd: MaxAd? = null
            val nativeAdLoader = MaxNativeAdLoader(dataNativeAds.adsIdApplovin, fragmentActivity)
            nativeAdLoader.setNativeAdListener(object : MaxNativeAdListener() {

                override fun onNativeAdLoaded(nativeAdView: MaxNativeAdView?, ad: MaxAd) {
                    if (nativeAd != null) {
                        nativeAdLoader.destroy(nativeAd)
                    }
                    nativeAd = ad

                    if (fragmentActivity.isFinishing || fragmentActivity.isChangingConfigurations || dialogFragment.isRemoving || dialogFragment.activity == null || dialogFragment.isDetached || !dialogFragment.isAdded || dialogFragment.view == null) {
                        nativeAdLoader.destroy(ad)
                    } else {
                        with(frameLayout) {
                            removeAllViews()
                            addView(nativeAdView)
                            lottieAnimationView?.hide()
                            button?.show()
                        }
                    }
                }

                override fun onNativeAdLoadFailed(adUnitId: String, error: MaxError) {
                    Log.e("ads_native_big", error.toString())
                    showStartIoBig(context, 4, binding, frameLayout, lottieAnimationView, button)
                }
            })
            nativeAdLoader.loadAd(populateApplovin(activity))

        }
    }

    fun showStartIoBig(
        context: Context,
        sizeImageAsset: Int,
        binding: ItemAdNativeStartioBigBinding,
        frameLayout: FrameLayout,
        lottieAnimationView: LottieAnimationView?,
        button: AppCompatButton?
    ) {
        val nativeAd = StartAppNativeAd(context)
        nativeAd.loadAd(
            NativeAdPreferences()
                .setAdsNumber(1)
                .setAutoBitmapDownload(true)
                .setPrimaryImageSize(sizeImageAsset), object : AdEventListener {
                override fun onReceiveAd(p0: Ad) {
                    populateStartIoBig(nativeAd.nativeAds.first(), binding)
                    with(frameLayout) {
                        removeAllViews()
                        addView(binding.root)
                    }
                    lottieAnimationView?.hide()
                    button?.show()
                }

                override fun onFailedToReceiveAd(p0: Ad?) {
                    Log.e("ads_native_big", p0!!.errorMessage!!)
                    button?.show()
                }
            }
        )

    }


    //Native Small
    fun showApplovinSmall(
        context: Context,
        binding: ItemAdNativeStartioSmallBinding,
        dataNativeAds: Admob,
        fragmentActivity: FragmentActivity,
        dialogFragment: DialogFragment? = null,
        frameLayout: FrameLayout
    ) {
        if (dataNativeAds.adsIdApplovin == "") {
            showStartIoSmall(context, binding, frameLayout)
        }else {
            var nativeAd: MaxAd? = null
            val nativeAdLoader = MaxNativeAdLoader(dataNativeAds.adsIdApplovinSmall, fragmentActivity)
            nativeAdLoader.setNativeAdListener(object : MaxNativeAdListener() {

                override fun onNativeAdLoaded(nativeAdView: MaxNativeAdView?, ad: MaxAd) {
                    if (nativeAd != null) {
                        nativeAdLoader.destroy(nativeAd)
                    }
                    nativeAd = ad

                    if (fragmentActivity.isFinishing || fragmentActivity.isChangingConfigurations || dialogFragment!!.isRemoving || dialogFragment.activity == null || dialogFragment.isDetached || !dialogFragment.isAdded || dialogFragment.view == null) {
                        nativeAdLoader.destroy(ad)
                    } else {
                        with(frameLayout) {
                            removeAllViews()
                            addView(nativeAdView)
                        }
                    }
                }

                override fun onNativeAdLoadFailed(adUnitId: String, error: MaxError) {
                    Log.e("ads_native_small", error.toString())
                    showStartIoSmall(context, binding, frameLayout)
                }
            })
            nativeAdLoader.loadAd()
        }
    }

    fun showStartIoSmall(
        context: Context,
        binding: ItemAdNativeStartioSmallBinding,
        frameLayout: FrameLayout
    ) {

        val nativeAd = StartAppNativeAd(context)
        nativeAd.loadAd(
            NativeAdPreferences()
                .setAdsNumber(1)
                .setAutoBitmapDownload(true)
                .setPrimaryImageSize(3), object : AdEventListener {
                override fun onReceiveAd(ad: Ad) {
                    populateStarIoSmall(nativeAd.nativeAds.first(), binding)
                    with(frameLayout) {
                        removeAllViews()
                        addView(binding.root)
                    }
                }

                override fun onFailedToReceiveAd(p0: Ad?) {
                    Log.e("native_startio", p0!!.errorMessage!!)
                }
            }
        )
    }
}