package manggo.com.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import manggo.com.R;
import manggo.com.http.OkHttpUtil;
import manggo.com.http.ProgressListener;
import manggo.com.recycleradapter.ChooseImage;
import manggo.com.recycleradapter.ChooseImageAdapter;
import manggo.com.server.ServerInfo;
import manggo.com.util.Issue_Info;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static manggo.com.fragment.Home_Fragment.list;
import static manggo.com.fragment.Home_Fragment.preferences;
import static manggo.com.server.ServerInfo.HOST_PIC;
import static manggo.com.server.ServerInfo.UPDATE_INFO;

public class IssueActvity extends AppCompatActivity implements View.OnClickListener{
    private ActionBar bar;//导航栏

    private Toolbar toolbar;
    private AppCompatSpinner spinner;//定义下拉选择框
    private EditText phone,qq,content;//定义标题和内容框
    private TextView tv;
    //要加载的动画
    private Transition transition;
    //标题
    private String title;
    //图片按钮
    private ImageButton imageButton;
    //底部对话框
    private Dialog dialog;
    //底部对话框三个按钮
    private Button take_photo,take_picture,cancel;
    //选择的图片集合
    public static List<ChooseImage> chooseImages=new ArrayList<>();
    //横向轮播图
    private RecyclerView recyclerView;
    //该Recycler的适配器
    private ChooseImageAdapter adapter;
    //图片Uri
    private Uri uri;
    public static final int TAKE_PHOTO=1;
    public static final int CHOOSE_PHOTO=2;
    //要保存的集合位图
    private Bitmap bitmap;
    private ImageView testview;
    private Toast toast;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        //请求获得动画
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        setContentView(R.layout.activity_issue);
        //显示ActionBar
        showBar();
        initData();//对数据进行初始化
        //执行动画的方法
        changeAnim();
        //添加图片的方法
        recyclerAddImage();
    }
    /**
     * 进行动画的方法
     */
    private void changeAnim(){
        transition= TransitionInflater.from(this).inflateTransition(R.transition.explode);
        getWindow().setEnterTransition(transition);
        getWindow().setExitTransition(transition);
        getWindow().setReenterTransition(transition);
    }
    /**
     * 对数据进行初始化
     */
    private void initData(){
        tv=(TextView)findViewById(R.id.issue_tv_id);
        //初始化Spinner
        spinner=(AppCompatSpinner)findViewById(R.id.spinner_id);
        //初始化EditText
        phone=(EditText)findViewById(R.id.phone_id);
        qq=(EditText)findViewById(R.id.qq_id);
        content=(EditText)findViewById(R.id.content_id);
        //为Spinner添加事件监听
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String select=(String) adapterView.getSelectedItem();
                title=select;
                Log.i("选择的选项:",select);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        //初始化图片按钮
        imageButton=(ImageButton)findViewById(R.id.image_button_addimage_id);
        //设置监听弹出选择对话框
        imageButton.setOnClickListener(this);

        //设置限制输入字数为200字
        content.addTextChangedListener(new TextWatcher(){//EditView自动判断输入长度

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                String contentString =  content.getText().toString();
                int l = contentString.length();
                tv.setText("当前还能输入"+String.valueOf(150-l)+"字");
                Log.e("还能输入:",l + "");//需要将数字转成字符串
                if (l > 150)
                {
                    content.setText(contentString.substring(0, 150));
                    content.setSelection(150);//光标移动到最后
                    Toast.makeText(IssueActvity.this,"最大只能输入150字",Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
    /**
     * 显示ActionBar的方法
     */
    private void showBar(){
        toolbar=(Toolbar)findViewById(R.id.Issue_toolbar_id);
        setSupportActionBar(toolbar);
        bar=getSupportActionBar();//获得actionbar
        if(bar!=null){
            bar.setDisplayShowTitleEnabled(false);
            bar.setDisplayHomeAsUpEnabled(true);//设置该按钮可以点击
            bar.setHomeAsUpIndicator(R.drawable.action_back_30dp);//设置图标
        }
    }
    /**
     * 顶部ActionBar插入文字和tub
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.issueactivity_toolbar,menu);
    return true;
    }
    /**
     * 顶部ActionBar点击的方法重写方法
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //点击返回触发的方法
        switch (item.getItemId()){
            case android.R.id.home:
                if(qqAndPhoneLenth()==0&&chooseImages.size()==0){//如果长度为0当前Activity结束
                    finish();
                }else {//否则就大于0
                    //弹出提示框提示用户
                    tipUser();
                }
                break;
            case R.id.toolbar_right_item:
                if(qqAndPhoneLenth()==0&&chooseImages.size()==0){
                    showToast();
                }else if(hasSen().length()!=0){
                showWarnToast();

                }else{

                    //此处提交数据到服务器，然后结束该Activity
                    dataIsJson();
                    //结束该Activity
                    //循环提交图片到服务器
                    if(chooseImages.size()!=0){
                        for (int i=0;i<chooseImages.size();i++){
                            uploadFile("photo"+i+".jpeg");
                        }
                        finish();
                    }else {
                        finish();
                    }
                    //此处判断提交数据在前面刷新的方法
                    alterPageCount();
                }
                break;
                default:break;


        }
        return true;
    }

    /**
     *
     */
    private void alterPageCount(){
        int count=preferences.getInt("count",0);
        Log.e("当前count为:",count+"");
        int total_page=preferences.getInt("total_page",0);
        Log.e("当前total_page为:",total_page+"");
        if(count!=total_page){//不相等则使他们相等
            count=total_page;
            SharedPreferences.Editor editor=preferences.edit();
            editor.putInt("count",count);
            editor.commit();
            Log.e("修改后count为:",preferences.getInt("count",0)+"");
           Log.e("list集合的长度:",list.size()+"");

        }
    }
    /*
    警告提示框
     */
    private void showWarnToast(){
        Toast toast1=new Toast(this);
        toast1.setDuration(Toast.LENGTH_SHORT);
        View view=LayoutInflater.from(this).inflate(R.layout.toast_wran_view,null);
        TextView textView=(TextView)view.findViewById(R.id.toast_textview_id);
        textView.setText("包含敏感词:"+hasSen());
        toast1.setView(view);
        toast1.show();
    }
    /**
     * 获得电话，QQ内容的长度
     */
    public int qqAndPhoneLenth(){
        int AllLength;
        int phoneLength=phone.getText().toString().trim().length();//获得电话长度
        int qqLength=qq.getText().toString().trim().length();
        int contentLength=content.getText().toString().trim().length();
        AllLength=phoneLength+qqLength+contentLength;
        return AllLength;
    }
    /**
     * 提示用户框
     */
    private void tipUser(){
        View v=getLayoutInflater().inflate(R.layout.dialog_view,null);
        //实列化一个对话框
        AlertDialog.Builder dialog=new AlertDialog.Builder(IssueActvity.this);
        dialog.setView(R.layout.dialog_view);
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //不做任何处理
                //该Activy结束
                finish();
            }
        });
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
              //不做任何处理
            }
        });

        dialog.create();
        dialog.show();
    }
    /**
     * 返回键按下的方法
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode==KeyEvent.KEYCODE_BACK){
            if(qqAndPhoneLenth()==0&&chooseImages.size()==0){//如果长度为0当前Activity结束
                Log.e("当前Activity销毁","");
                finish();
            }else {//否则就大于0
                //弹出提示框提示用户
                tipUser();
            }
        }
        return true;
    }
    /**
     * 处理字符串是否包含敏感词
     */
    private String hasSen(){
        StringBuffer tip;
        //此处获取图片文件的名称
        String contentText=content.getText().toString();//获取内容
        //过滤敏感词
        String[] warn={"寒假工","暑假工","招聘","黄色片","兼职","约炮","日你妈"
                       +"你妈逼","AA","BB"};
        tip=new StringBuffer();
        for(int i=0;i<warn.length;i++){
            //如果包含其中的一个敏感词语,则添加到StringBuffer进行提示不允许包含该敏感词
            if(!(contentText.indexOf(warn[i])==-1)){
                tip.append(warn[i]+" ");
            }
        }
        return  tip.toString();
    }
    /**
     * 处理数据为JSON的方法
     */
    private void dataIsJson(){
        //此处获取图片文件的名称
        //获得当前系统日期
//        String writeTime=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        String phoneText=phone.getText().toString();//获取电话号码
        String qqText=qq.getText().toString();//获取QQ号码
        String contentText=content.getText().toString();//获取内容
        //如果不包含则把内容提交到服务器提交到服务器，把内容转换为JSON类型传输
        Gson gson=new Gson();//实列化一个
        Issue_Info info=new Issue_Info(title,phoneText,qqText,contentText);//封装一个对象
        //转换为JSON对象
        String object=gson.toJson(info);//转换为json对象后提交到服务器
        Log.e("转换后的对象:",object);
        /**
         *   此处把数据提交到服务器的方法
         */
        sendJson(object);





    }
    /**
     * 连接到服务器发送数据的方法的方法
     */
    private void sendJson(final String json){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    //要提交的数据
                    RequestBody requestBody = new FormBody.Builder().add("info",json).build();
                    //获得提交数据，UPDATE_INFO为上传信息的地址
                    Request request = new Request.Builder().url(UPDATE_INFO).post(requestBody).build();
                    //请求得到得到的数据
                    Response response = client.newCall(request).execute();
                    String responseData=response.body().toString();
                    if(response.isSuccessful()){
                        Log.e("发送成功返回的数据",responseData);
                        //进行数据的解析操作






                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }
    /**
     * 显示对话框的方法
     */
    private void showDialog(){
        Transition t=TransitionInflater.from(this).inflateTransition(R.transition.slide);
        //初始化一个对话框
        dialog=new Dialog(this,R.style.BottomDialog);
        //加载线性布局
        LinearLayout root=(LinearLayout) LayoutInflater.from(this).inflate(R.layout.issue_dialog,null);
        //初始化里面的视图，加入按钮点击监听功能
        take_photo=(Button)root.findViewById(R.id.take_photo_id);
        take_picture=(Button)root.findViewById(R.id.take_picture_id);
        cancel=(Button)root.findViewById(R.id.cancel_id);
        //加入事件监听功能
        cancel.setOnClickListener(this);
        take_photo.setOnClickListener(this);
        take_picture.setOnClickListener(this);


        dialog.setContentView(root);//设置视图
        Window window=dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);//设置在底部显示
        //添加动画
        window.setExitTransition(t);
        window.setEnterTransition(t);
        window.setReenterTransition(t);
        //获得当前对话框的参数值n
        WindowManager.LayoutParams lp=window.getAttributes();
        //坐标x
        lp.x=0;
        lp.y=0;
        lp.width=(int)getResources().getDisplayMetrics().widthPixels;
        lp.alpha=9f;
        window.setAttributes(lp);
        dialog.show();
    }
    /**
     * 图片适配器添加到Recycler
     */
    private void recyclerAddImage(){
        //找到该列表
        recyclerView=(RecyclerView)findViewById(R.id.issue_recycler_id);
        adapter=new ChooseImageAdapter(chooseImages);
        LinearLayoutManager ll=new LinearLayoutManager(this);
        ll.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(ll);//设置布局管理
        recyclerView.setAdapter(adapter);

    }
    /**
     * 显示Toast
     */
    private void showToast(){
        toast=new Toast(this);
        toast.setView(LayoutInflater.from(this).inflate(R.layout.toast_view1,null));
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }
        //按钮监听事件的方法

    /**
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.image_button_addimage_id://按下选择图片按钮
                showDialog();
                break;
            case R.id.cancel_id:
                dialog.dismiss();//dialog隐藏
                break;
            case R.id.take_photo_id:
                /**
                 * 相机拍照的方法I
                 */
                takePhoto();
                dialog.dismiss();
                break;
            case R.id.take_picture_id:
                /**
                 * 选择图片的方法
                 */
            applyLimit();

                dialog.dismiss();
                break;

            default:break;
        }
    }
    /**
     * 相机拍照的方法
     */
    private void takePhoto(){
        //创建File对象，用于储存拍照后的图片
        File outputImage=new File(getExternalCacheDir(),"cachePic.jpg");
        try{
            if (outputImage.exists()){
                outputImage.delete();
            }
            outputImage.createNewFile();
        }catch (Exception e){
            e.printStackTrace();
        }
        if(Build.VERSION.SDK_INT>=24){
            //获得图片uri
            uri= FileProvider.getUriForFile(IssueActvity.this,"com.example.camera.fileprovider",outputImage);

        }else {
            uri=Uri.fromFile(outputImage);

        }
        //启动相机程序
        Intent intent=new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT,uri);
        startActivityForResult(intent,TAKE_PHOTO);
    }

    /**
     * 选择图片的方法
     * */
    private void applyLimit(){
        if (ContextCompat.checkSelfPermission(IssueActvity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(IssueActvity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        } else {
            openAlbum();
        }
    }

    /**
     * 打开相册的方法
     *
     */
    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOTO); // 打开相册
    }

    /**
     * 请求权限的方法
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum();
                } else {
                    Toast.makeText(this, "你没有授予访问相册的权限", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }


    //返回该位图
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        switch (requestCode){
            case TAKE_PHOTO:
                if (resultCode==RESULT_OK){
                    try{
                        //将位图保存到集合
                        bitmap= BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
                        Log.e("原始位图的大小:",String.valueOf(bitmap.getByteCount()));
                        chooseImages.add(new ChooseImage(bitmap));//保存位图到集合
                        //把图片先保存到本地
                        compressToSdcard();

                        //主线程通知集合数据发生改变

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //通知适配器数据发生改变
                                adapter.notifyDataSetChanged();
                            }
                        });
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                break;
                //触发的是选择照片的标记
            case CHOOSE_PHOTO:
                if (resultCode == RESULT_OK) {
                    // 判断手机系统版本号
                    if (Build.VERSION.SDK_INT >= 19) {
                        // 4.4及以上系统使用这个方法处理图片
                        handleImageOnKitKat(data);
                    } else {
                        // 4.4以下系统使用这个方法处理图片
                        handleImageBeforeKitKat(data);
                    }
                }
                break;
                default:break;
        }
    }

    /**
     * 安卓4.4版本以上选择图片的方法
     * @param data
     */
    @TargetApi(19)
    private void handleImageOnKitKat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        Log.d("TAG", "handleImageOnKitKat: uri is " + uri);
        if (DocumentsContract.isDocumentUri(this, uri)) {
            // 如果是document类型的Uri，则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1]; // 解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // 如果是content类型的Uri，则使用普通方式处理
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            // 如果是file类型的Uri，直接获取图片路径即可
            imagePath = uri.getPath();
        }
        displayImage(imagePath); // 根据图片路径显示图片
    }

    /**
     * android4.4版本以下选择图片的方法
     * @param data
     */
    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        displayImage(imagePath);
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        // 通过Uri和selection来获取真实的图片路径
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }
    private void displayImage(String imagePath) {
        if (imagePath != null) {
            //压缩格式添加位图到集合
            bitmap = BitmapFactory.decodeFile(imagePath);
            Log.e("原始位图的大小:",String.valueOf(bitmap.getByteCount()));
            chooseImages.add(new ChooseImage(bitmap));//保存位图到集合
            //图片保存到缓存目录
            //图片保存到缓存目录
            compressToSdcard();

            //主线程通知适配器数据已经发生改变
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter.notifyDataSetChanged();
                }
            });

        } else {
            Toast.makeText(this, "图片路径为空", Toast.LENGTH_SHORT).show();
        }

    }
    /**
     * 上传文件的方法
     */
    private void uploadFile(final String fileName){
        final File file = new File(getExternalCacheDir(),fileName);
        //要上传的服务器目录
        String postUrl = HOST_PIC;
        //发送文件
        OkHttpUtil.postFile(postUrl, new ProgressListener() {
            @Override
            public void onProgress(long currentBytes, long contentLength, boolean done) {
                Log.i("当前进度", "==" + currentBytes + "==文件长度==" + contentLength + "是否完成？==" + done);
                //获得上传的进度
                int progress = (int) (currentBytes * 100 / contentLength);
                Log.e("文件进度==========",""+progress);

            }
        }, new Callback() {
            //失败回调的方法
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("失败回调的方法","");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response != null) {
                    Log.e("上传图片:",fileName);
                    String result = response.body().string();
                    Log.i("返回结果", "传输结果:" + result);
                    //传输完成后删除对应的文件
                    file.delete();
                }
            }
        }, file);
    }
    /**
     * 把集合中的位图压缩为图片
     */

    /**
     * 保存位图到指定路径的方法的方法
     * @param
     */
    public void saveBitmapFile(final Bitmap bitmap,String fileName) {
        //图片的缓存目录
        final File file = new File(getExternalCacheDir(),fileName);//将要保存图片的路径
        //启动线程压缩图片并且保存
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
                    //图片质量压缩百分之40
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bos);
                    bos.flush();
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 调用这个方法进行集合图片的压缩和上传
     */
    private void compressToSdcard(){
        if(chooseImages.size()!=0){
            for (int i=0;i<chooseImages.size();i++){//将位图取出来

                //将位图保存到本地
                saveBitmapFile(chooseImages.get(i).getBitmap(),"photo"+i+".jpeg");

            }
        }
    }
    @Override
    public void onDestroy(){
        Log.e("当前activity销毁","当前activity销毁");
        //清空图片集合
        chooseImages.clear();

        super.onDestroy();
    }


}
