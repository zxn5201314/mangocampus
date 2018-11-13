package manggo.com.server;

import android.util.Log;

import com.google.gson.Gson;

import java.util.Map;
import java.util.Set;

import manggo.com.query.Page;
import manggo.com.query.ResponseResult;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PostServer {

    //启动一个线程发起请求
    public void sendRequest(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    //构件请求对象
                    OkHttpClient client=new OkHttpClient();
                    //请求体
                    Request request=new Request.Builder()//构件请求体
                            .url("http://139.199.4.125/dev/info/findList").build();
                    Response response=client.newCall(request).execute();//请求返回的数据对象
                    if(response.isSuccessful()){//如果请求成功
                        String responseData=response.body().string();//获得返回的数据
                        Log.e("返回的数据:",responseData);
                        parseJSON(responseData);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }
    private void  parseJSON(String jsonData) {
        Gson gson = new Gson();
        //取出JSON解析对象
        //取出JSON解析对象
        //取出JSON解析对象
        //转换为当前对象
        ResponseResult result=gson.fromJson(jsonData,ResponseResult.class);
        Page page=result.getData();
        for (Object obj:page.getRecords()){
            Map map=(Map)obj;//强制转换为集合
            System.out.print(map);
            Set<String> s=map.keySet();//把所有的key转换为集合
            Log.e("key为:",s+"");
            Log.e("标题:",""+map.get("title"));
            Log.e("内容:",""+map.get("context"));
        }


    }
}
