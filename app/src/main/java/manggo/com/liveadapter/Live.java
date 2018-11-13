package manggo.com.liveadapter;

public class Live {
    private String title;//底部文字
    private int imageId;//图片id
    //构造方法
    public Live(int imageId,String title){
        this.imageId=imageId;
        this.title=title;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
