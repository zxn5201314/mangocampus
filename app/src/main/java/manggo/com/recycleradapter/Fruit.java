package manggo.com.recycleradapter;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import java.util.List;

public class Fruit extends LitePalSupport{
    private String id;//该条内容的id
    private String name;//标题
    @Column(unique = true)
    private String content;//内容
    private String visitCount;//访问数量
    private String time;//发布时间
    private List<String> imageList;//图片集合
    private String image;

    public Fruit(String id,String name,String content,List<String> imageList,String visitCount,String time){
        this.id=id;
        this.name=name;
        this.content=content;
        this.visitCount=visitCount;
        this.time=time;
        this.imageList=imageList;
    }
    //set方法用于获取当前要显示的名称并且添加到数据库
    public String getContent(){
        return content;
    }
    public String getName() {
        return name;
    }
    //get方法用于获取内容
    public void setName(String name) {
        this.name = name;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public String getVisitCount() {
        return visitCount;
    }

    public void setVisitCount(String visitCount) {
        this.visitCount = visitCount;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<String> getImageList() {
        return imageList;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    //重写对象的方法出去重复的元素
    //重写方法判断元素是否重复
    @Override
    public String toString() {
        return "姓名:"+this.name+"\t年龄:"+this.id+"\t内容:"+this.content;

    }
    @Override
    public boolean equals(Object object) {
        if(this==object) {
            return true;
        }
        if(!(object instanceof Fruit)) {
            return false;
        }
        Fruit f=(Fruit) object;//向下转型
        //比较名字和内容是否相等是否相等
        if(this.id.equals(f.id)) {
            return true;//是同一个对象
        }else {
            return false;
        }

    }
    //覆写hasCode方法
    @Override
    public int hashCode() {
        return this.name.hashCode()*this.content.hashCode();//指定编码公式
    }
}
