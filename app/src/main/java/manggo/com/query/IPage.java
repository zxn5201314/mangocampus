package manggo.com.query;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

interface IPage<T> extends Serializable {
    default String[] descs() {
        return null;
    }

    default String[] ascs() {
        return null;
    }

    default Map<Object, Object> condition() {
        return null;
    }

    default boolean optimizeCountSql() {
        return true;
    }

    default long offset() {
        return this.getCurrent() > 0L ? (this.getCurrent() - 1L) * this.getSize() : 0L;
    }

    default long getPages() {
        if (this.getSize() == 0L) {
            return 0L;
        } else {
            long pages = this.getTotal() / this.getSize();
            if (this.getTotal() % this.getSize() != 0L) {
                ++pages;
            }

            return pages;
        }
    }

    default IPage<T> setPages(long pages) {
        return this;
    }

    List<T> getRecords();

    IPage<T> setRecords(List<T> var1);

    long getTotal();

    IPage<T> setTotal(long var1);

    long getSize();

    IPage<T> setSize(long var1);

    long getCurrent();

    IPage<T> setCurrent(long var1);
}