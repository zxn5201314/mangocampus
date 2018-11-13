package manggo.com.recycleradapter;

import android.graphics.Bitmap;

public class ChooseImage {
    private Bitmap bitmap;
        //构造方法传递需要显示的图片
        public ChooseImage(Bitmap bitmap){
            this.bitmap=bitmap;
        }
    public Bitmap getBitmap() {
        return bitmap;
    }




}
