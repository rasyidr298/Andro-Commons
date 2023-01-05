package id.rrdev.commons.extenstion

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.facebook.shimmer.ShimmerFrameLayout
import eightbitlab.com.blurview.BlurView
import eightbitlab.com.blurview.RenderScriptBlur
import id.rrdev.commons.R

//show lottie animation
fun LottieAnimationView.lottie(name: String, repeat: Int) {
    with(this) {
        setAnimation("${name}.json")
        playAnimation()
        repeatCount = repeat
    }
}

//show shimmer
fun ShimmerFrameLayout.showShimmer(view: View) {
    with(this) {
        show()
        startShimmer()
    }
    view.hide()
}

//hide shimmer
fun ShimmerFrameLayout.hideShimmer(view: View) {
    with(view) {
        hide()
        stopShimmer()
    }
    view.hide()
}

//load image
fun ImageView.loadImage(url: String) {
    Glide.with(this)
        .load(url)
//        .placeholder(R.drawable.loading_anim)
        .error(R.drawable.ic_broken_image)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .centerCrop()
        .override(150, 100)
        .thumbnail(0.25f)
        .into(this)
}

//load image full
fun ImageView.loadImageFull(url: String) {
    Glide.with(this)
        .load(url)
//        .placeholder(R.drawable.loading_anim)
        .error(R.drawable.ic_broken_image)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .centerCrop()
        .thumbnail(0.25f)
        .into(this)
}

//load image resource
fun ImageView.loadImageRes(context: Context, assetName: String) {
    Glide.with(this)
        .load(resources.getIdentifier(assetName, "drawable", context.packageName))
        .centerCrop()
        .into(this)
}

//load color
fun ImageView.loadColor(context: Context, color: Int) {
    Glide.with(this)
        .load("")
        .placeholder(ColorDrawable(ContextCompat.getColor(context, color)))
        .into(this)
}

//blur view
fun BlurView.buildBlurView(activity: FragmentActivity) {
    val decorView = activity.window.decorView
    val rootView = decorView.findViewById<View>(android.R.id.content) as ViewGroup
    this.setupWith(rootView)
        .setFrameClearDrawable(decorView.background)
        .setBlurAlgorithm(RenderScriptBlur(activity))
        .setBlurRadius(20f)
        .setBlurAutoUpdate(true)
        .setHasFixedTransformationMatrix(true)
}