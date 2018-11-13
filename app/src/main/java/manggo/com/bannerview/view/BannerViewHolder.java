package manggo.com.bannerview.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import manggo.com.R;
import manggo.com.bannerview.holder.MViewHolder;

public  class BannerViewHolder implements MViewHolder<LoopImage> {
    private ImageView mImageView;

    @Override
    public View createView(Context context) {
        // 返回页面布局
        View view = LayoutInflater.from(context).inflate(R.layout.banner_item, null);
        mImageView = (ImageView) view.findViewById(R.id.banner_image);
        return view;
    }

    @Override
    public void onBind(Context context, int position, LoopImage loopImage) {
        // 数据绑定
        Glide.with(context).load(loopImage.getUri()).crossFade().into(mImageView);
    }
}