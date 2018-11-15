package manggo.com.recycleradapter;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import java.io.Serializable;
import java.util.List;
//序列化进行Intent的对象传递
    public class Fruit extends LitePalSupport implements Serializable{
        @Column(unique = true)
        private String fruitId;//内容的id
        private String qq;
        private String phone;
        private String name;//标题
        private String content;//内容
        private String visitCount;//访问数量
        private String time;//发布时间
        private List<String> imageList;//图片集合
        private String imageNames;
        public Fruit(String fruitId,String name,String content,String qq,String phone,List<String> imageList,String visitCount,String time){
            this.fruitId=fruitId;
            this.name=name;
            this.content=content;
            this.qq=qq;
            this.phone=phone;
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

        public String getImageNames() {
            return imageNames;
        }

        public void setImageNames(String imageNames) {
            this.imageNames = imageNames;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getQq() {
            return qq;
        }

        public void setQq(String qq) {
            this.qq = qq;
        }
        public String getFruitId() {
            return fruitId;
        }

    public void setFruitId(String fruitId) {
        this.fruitId = fruitId;
    }

    //重写对象的方法出去重复的元素
        //重写方法判断元素是否重复
        @Override
        public String toString() {
            return "姓名:"+this.name+"\t年龄:"+this.fruitId+"\t内容:"+this.content;

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
            if(this.fruitId.equals(f.fruitId)) {
                return true;//是同一个对象
            }else {
                return false;
            }

        }
        //覆写hasCode方法
        @Override
        public int hashCode() {
            return this.fruitId.hashCode();//指定编码公式
        }
    }
