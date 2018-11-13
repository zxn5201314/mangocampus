package manggo.com.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import manggo.com.R;
import manggo.com.recycleradapter.ChooseImageAdapter;

public class FullScreenActivity extends AppCompatActivity {
    private ImageView imageView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setScreenFull();
        setContentView(R.layout.activity_fullscreen);
        init();
    }
    private void init(){
        imageView=(ImageView)findViewById(R.id.full_imageView);
        Intent intent=getIntent();
        int position=intent.getIntExtra("position",0);
        Log.e("ss",position+"");
        imageView.setImageBitmap(ChooseImageAdapter.b.get(position));
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    /**
     * 设置全屏的方法
     */
    public void setScreenFull(){
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
}
