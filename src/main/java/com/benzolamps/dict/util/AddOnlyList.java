package com.benzolamps.dict.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

/**
 * 只能添加的List
 * @param <E> 元素类型
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-9-9 16:22:47
 */
public class AddOnlyList<E> extends ArrayList<E> {

    private static final long serialVersionUID = 6961383520787064079L;

    @Override
    public final E set(int index, E element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final E remove(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final void clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    protected final void removeRange(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final boolean removeIf(Predicate<? super E> filter) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final void replaceAll(UnaryOperator<E> operator) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final void sort(Comparator<? super E> c) {
        throw new UnsupportedOperationException();
    }
}
