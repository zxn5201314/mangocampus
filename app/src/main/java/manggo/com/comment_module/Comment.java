package manggo.com.comment_module;

public class Comment {//实体类
    private String name;//评论者名称
    private String content;//评论者内容
    private String headUri;//头像uri
    public Comment(){}
    public Comment(String name,String content,String headUri){
        this.name=name;
        this.content=content;
        this.headUri=headUri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getHeadUri() {
        return headUri;
    }

    public void setHeadUri(String headUri) {
        this.headUri = headUri;
    }
}
