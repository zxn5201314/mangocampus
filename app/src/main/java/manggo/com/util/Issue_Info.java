package manggo.com.util;

public class Issue_Info {
    private String userId;
    private String title;//标题
    private String phone;//电话
    private String qq;
    private String content;
//    private String writeTime;//上传时间
    //构造方法赋值
    public Issue_Info(String title,String phone,String qq,String content){
        this.title=title;
        this.phone=phone;
        this.qq=qq;
        this.content=content;
    }
}
