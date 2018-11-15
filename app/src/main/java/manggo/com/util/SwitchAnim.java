package manggo.com.util;

import android.content.Context;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.Window;

import manggo.com.R;

public class SwitchAnim {
    public static void switchAnimUtil(Context context, Window window){
        Transition transition= TransitionInflater.from(context).inflateTransition(R.transition.explode);
        //退出的动画
        window.setExitTransition(transition);
        //进入的动画
        window.setEnterTransition(transition);
        //再次进入的动画
        window.setReenterTransition(transition);
    }
}
