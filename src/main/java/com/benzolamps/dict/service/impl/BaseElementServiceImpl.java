package com.benzolamps.dict.service.impl;

import com.benzolamps.dict.bean.BaseElement;
import com.benzolamps.dict.bean.Library;
import com.benzolamps.dict.controller.vo.BaseElementVo;
import com.benzolamps.dict.dao.base.BaseElementDao;
import com.benzolamps.dict.dao.core.Filter;
import com.benzolamps.dict.dao.core.Order;
import com.benzolamps.dict.dao.core.Page;
import com.benzolamps.dict.dao.core.Pageable;
import com.benzolamps.dict.service.base.BaseElementService;
import com.benzolamps.dict.service.base.LibraryService;
import com.benzolamps.dict.util.DictExcel;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ResolvableType;
import org.springframework.core.io.Resource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.io.InputStream;
import java.util.*;

import static com.benzolamps.dict.util.DictLambda.tryFunc;

/**
 * 单词或短语类的基类Service接口实现类
 * @param <T> 实体类类型
 * @param <R> Vo类型
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-9-5 21:06:39
 */
public abstract class BaseElementServiceImpl<T extends BaseElement, R extends BaseElementVo<T>>
    extends BaseServiceImpl<T> implements BaseElementService<T> {
    @javax.annotation.Resource
    private LibraryService libraryService;

    private Class<R> elementVoClass;

    private BaseElementDao<T> baseElementDao;

    @Autowired
    @Transactional(readOnly = true)
    protected void setBaseElementDao(BaseElementDao<T> baseElementDao) {
        this.baseElementDao = baseElementDao;
    }

    @SuppressWarnings("unchecked")
    public BaseElementServiceImpl() {
        ResolvableType resolvableType = ResolvableType.forClass(getClass());
        elementVoClass = (Class<R>) resolvableType.getSuperType().getGeneric(1).resolve();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<T> findPage(Pageable pageable) {
        pageable.getOrders().add(Order.asc("index"));
        return super.findPage(pageable);
    }

    @Override
    public T persist(T element) {
        Library current = libraryService.getCurrent();
        Assert.notNull(current, "未选择词库");
        element.setLibrary(current);
        Filter filter = Filter.eq("library", current).and(Filter.eq("prototype", element.getPrototype()));
        int count = count(filter).intValue();
        if (count == 0) {
            return super.persist(element);
        } else {
            BaseElement ref = findSingle(filter);
            element.setId(ref.getId());
            return update(element);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void persist(T... elements) {
        this.persist(Arrays.asList(elements));
    }

    @Override
    public void persist(Collection<T> elements) {
        Library current = libraryService.getCurrent();
        Assert.notNull(current, "未选择词库");
        List<T> elementList = new LinkedList<>(elements);
        elementList.removeIf(element -> baseElementDao.findPrototypes(current).contains(element.getPrototype()));
        elementList.removeIf(element -> elementList.stream().filter(e -> e.getPrototype().equals(element.getPrototype())).count() > 1);
        elementList.forEach(element -> element.setLibrary(current));
        elements.removeAll(elementList);
        // elements.forEach(this::persist);
        super.persist(elementList);
    }

    @Override
    public void update(Collection<T> entities, String... ignoreProperties) {
        super.update(entities, ignoreProperties);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void remove(T... entities) {
        super.remove(entities);
    }

    @Override
    public void remove(Integer... ids) {
        super.remove(ids);
    }

    @Override
    public void remove(Collection<T> entities) {
        super.remove(entities);
    }

    @Override
    public T update(T element, String... ignoreProperties) {
        Library current = libraryService.getCurrent();
        Assert.notNull(current, "未选择词库");
        element.setLibrary(current);
        return super.update(element, ignoreProperties);
    }

    @Override
    @Transactional
    public int imports(Resource resource) {
        InputStream stream = tryFunc(resource::getInputStream);
        Workbook workbook = DictExcel.inputStreamToWorkbook(stream);
        Sheet sheet = workbook.getSheetAt(0);
        /* 用List因为equals方法返回值相同 */
        List<T> elements = new LinkedList<>();
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            BaseElementVo<T> baseElementVo = DictExcel.excelRowToObject(sheet.getRow(i), elementVoClass);
            T element = baseElementVo.convertToElement();
            elements.add(element);
        }
        try {
            return elements.size();
        } finally {
            this.persist(elements);
        }
    }
}
