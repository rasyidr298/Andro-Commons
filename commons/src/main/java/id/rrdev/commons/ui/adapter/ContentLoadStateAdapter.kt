package id.rrdev.commons.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import id.rrdev.commons.databinding.ItemLoadingStateBinding

class ContentLoadStateAdapter(
    private val retry: () -> Unit
): LoadStateAdapter<ContentLoadStateAdapter.ContentLoadStateAdapterViewHolder>() {

    inner class ContentLoadStateAdapterViewHolder(
        private val binding: ItemLoadingStateBinding,
        private val retry: () -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(loadState: LoadState) {
            if (loadState is LoadState.Error) {
                binding.tvError.text = loadState.error.localizedMessage
            }

            binding.progress.isVisible = loadState is LoadState.Loading
            binding.btnRetry.isVisible = loadState is LoadState.Error
            binding.tvError.isVisible = loadState is LoadState.Error

            binding.btnRetry.setOnClickListener {
                retry()
            }
        }
    }

    override fun onBindViewHolder(holder: ContentLoadStateAdapterViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ) = ContentLoadStateAdapterViewHolder(
        ItemLoadingStateBinding.inflate(LayoutInflater.from(parent.context), parent, false),
        retry
    )
}