package id.rrdev.commons.ui.adapter.viewHolder

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.applovin.mediation.nativeAds.MaxNativeAdView
import com.google.android.gms.ads.nativead.NativeAdView
import com.startapp.sdk.ads.nativead.NativeAdDetails
import id.rrdev.commons.databinding.ItemAdNativeListBinding
import id.rrdev.commons.databinding.ItemAdNativeStartioBigBinding
import id.rrdev.commons.databinding.ItemAdViewBinding
import id.rrdev.commons.utils.ads.AdsNative


class AdsViewHolderList(val binding: ItemAdNativeListBinding) : ViewHolder(binding.root) {
    val adView: NativeAdView = binding.root

    init {
        adView.mediaView = binding.adMedia
        adView.headlineView = binding.adHeadline
        adView.bodyView = binding.adBody
        adView.callToActionView = binding.adCallToAction
        adView.iconView = binding.adAppIcon
        adView.priceView = binding.adPrice
        adView.starRatingView = binding.adStars
        adView.storeView = binding.adStore
        adView.advertiserView = binding.adAdvertiser
    }
}

class AddViewStartIoHolder(private val binding: ItemAdNativeStartioBigBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(data: NativeAdDetails) {
        AdsNative.populateStartIoBig(data, binding)
    }
}

class AddViewAppLovinHolder(private val binding: ItemAdViewBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(data: MaxNativeAdView) {
        with(binding.root) {
            removeAllViews()
            addView(data)
        }
    }
}