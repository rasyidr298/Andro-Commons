package id.rrdev.commons.utils.ads

import android.util.Log
import com.google.android.gms.ads.nativead.NativeAd
import com.startapp.sdk.ads.nativead.NativeAdDetails
import id.rrdev.commons.model.dataIncome.Admob
import id.rrdev.commons.utils.abstractt.PrefManager

object AdsUtils {

    fun getAdsInPref(
        prefManager: PrefManager,
        idName: String
    ): Admob {
        var resultAdmob = Admob()

        prefManager.spGetAdsAdmob()?.map {
            if (it.name == idName) {
                resultAdmob = it
            }
        }
        return resultAdmob
    }

    fun inputAdToListAdmob(
        sizeItem: Int,
        item: MutableList<Any>,
        itemAd: MutableList<NativeAd>,
        from: String,
        startIndex: Int
    ) {
        Log.e("itemAd", "size $from :" + itemAd.size.toString())

        if (sizeItem >= 4) {
            val offset = sizeItem / (itemAd.size)
            var index = startIndex
            for (ad in itemAd) {
                if (item[index] !is NativeAd) {
                    item.add(index, ad)
                }
                index = index.plus(offset)
            }
        } else {
            val offset: Int = sizeItem / itemAd.size + 1
            var index = startIndex
            for (ad in itemAd) {
                if (item[index] !is NativeAd) {
                    item.add(index, ad)
                }
                index += offset
            }
        }
    }

    fun inputAdToListStartIo(
        sizeItem: Int,
        item: MutableList<Any>,
        itemAd: MutableList<NativeAdDetails>,
        from: String,
        startIndex: Int
    ) {
        Log.e("itemAd", "size $from :" + itemAd.size.toString())

        if (sizeItem >= 4) {
            val offset = sizeItem / (itemAd.size)
            var index = startIndex
            for (ad in itemAd) {
                if (item[index] !is NativeAdDetails) {
                    item.add(index, ad)
                }
                index = index.plus(offset)
            }
        } else {
            val offset: Int = sizeItem / itemAd.size + 1
            var index = startIndex
            for (ad in itemAd) {
                if (item[index] !is NativeAdDetails) {
                    item.add(index, ad)
                }
                index += offset
            }
        }
    }

    fun setMaxLoadAd(
        size: Int,
        process: String,
        nativeAdList: MutableList<NativeAd>?,
        nativeAd: NativeAd?
    ): Int {
        when {
            size <= 5 -> {
                if (process == "setAdToList") {
                    if (nativeAdList?.size!! + 1 <= 1) {
                        nativeAdList.add(nativeAd!!)
                    }
                }
                return 1
            }
            size in 6..11 -> {
                if (process == "setAdToList") {
                    if (nativeAdList?.size!! + 1 <= 2) {
                        nativeAdList.add(nativeAd!!)
                    }
                }
                return 2
            }
            size in 12..19 -> {
                if (process == "setAdToList") {
                    if (nativeAdList?.size!! + 1 <= 3) {
                        nativeAdList.add(nativeAd!!)
                    }
                }
                return 3
            }
            size in 20..29 -> {
                if (process == "setAdToList") {
                    if (nativeAdList?.size!! + 1 <= 4) {
                        nativeAdList.add(nativeAd!!)
                    }
                }
                return 4
            }
            size > 30 -> {
                if (process == "setAdToList") {
                    if (nativeAdList?.size!! + 1 <= 5) {
                        nativeAdList.add(nativeAd!!)
                    }
                }
                return 5
            }
        }
        return 0
    }
}