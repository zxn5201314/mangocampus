package manggo.com.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import manggo.com.recycleradapter.Fruit;

public class RemoveDuplicateUtil {
    /**
     * 移除重复元素
     */
    public static  void removeDuplicateWithOrder(List<Fruit> removelist) {
        Set<Fruit> set = new HashSet<>();
        List<Fruit> newList = new ArrayList<>();
        for (Iterator<Fruit> iter = removelist.iterator(); iter.hasNext();) {
            Object e = iter.next();
            if (set.add((Fruit)e))
                newList.add((Fruit)e);
        }
        removelist.clear();
        removelist.addAll(newList);
    }
}
