package manggo.com.bannerview.holder;

/**
 * Created by zhouwei on 17/5/26.
 */

public interface HolderCreator<VH extends MViewHolder> {
    /**
     * 创建ViewHolder
     * @return
     */
    public VH createViewHolder();
}
