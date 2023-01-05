package id.rrdev.commons.ui.adapter.viewHolder

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import id.rrdev.commons.R
import id.rrdev.commons.databinding.ItemEndorseListBinding
import id.rrdev.commons.extenstion.*
import id.rrdev.commons.model.dataIncome.Endorse
import id.rrdev.commons.ui.adapter.OnItemClicked
import id.rrdev.commons.utils.Analytics
import id.rrdev.commons.utils.PrefManager


class EndorseViewHolderList(
    private val context: Context,
    private val binding: ItemEndorseListBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(onItemClicked: OnItemClicked, data: Endorse, prefManager: PrefManager) {
        with(binding) {

            if (!data.isEnable!!) {
                root.hide()
            } else {

                if (data.youtubeUrl != "") {
                    imgBackground.setOnClickListener {
                        context.intentToYoutube(data.youtubeUrl.toString())
                    }
                } else {
                    ivPlay.hide()
                }

                if (data.packageApp != "") {
                    btnInstall.setOnClickListener {
                        Analytics.trackClick(data.packageApp!!)
                        context.intentToPlaystore(data.packageApp.toString())
                    }
                } else {
                    btnInstall.text = context.resources.getString(R.string.lihat)
                    btnInstall.setOnClickListener {
                        if (data.webUrl != "") {
                            context.intentToWeb(data.webUrl.toString())
                        } else {
                            onItemClicked.onEventClick(data)
                        }
                    }
                }

                root.setOnClickListener {
                    Analytics.trackClick("endorse")
                    if (data.webUrl != "") {
                        context.intentToWeb(data.webUrl.toString())
                    } else {
                        onItemClicked.onEventClick(data)
                    }
                }

                if (data.webUrl == "" && data.activityUrl == "") {
                    root.isEnabled = false
                }

                tvName.text = data.title
                tvDescription.text = data.description
                imgBackground.loadImageFull(data.imageUrl.toString())
            }
        }
    }
}