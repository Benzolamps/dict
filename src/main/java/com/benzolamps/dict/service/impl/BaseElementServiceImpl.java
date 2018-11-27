package com.benzolamps.dict.service.impl;

import com.benzolamps.dict.bean.BaseElement;
import com.benzolamps.dict.bean.Library;
import com.benzolamps.dict.bean.Student;
import com.benzolamps.dict.controller.vo.BaseElementVo;
import com.benzolamps.dict.dao.base.BaseElementDao;
import com.benzolamps.dict.dao.core.Filter;
import com.benzolamps.dict.dao.core.Order;
import com.benzolamps.dict.dao.core.Page;
import com.benzolamps.dict.dao.core.Pageable;
import com.benzolamps.dict.service.base.BaseElementService;
import com.benzolamps.dict.service.base.LibraryService;
import com.benzolamps.dict.util.DictArray;
import com.benzolamps.dict.util.DictExcel;
import lombok.SneakyThrows;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.intellij.lang.annotations.Language;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ResolvableType;
import org.springframework.core.io.Resource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Collections.singletonMap;

/**
 * 单词或短语类的基类Service接口实现类
 * @param <T> 实体类类型
 * @param <R> Vo类型
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-9-5 21:06:39
 */
@Transactional
public abstract class BaseElementServiceImpl<T extends BaseElement, R extends BaseElementVo<T>> extends BaseServiceImpl<T> implements BaseElementService<T> {

    @javax.annotation.Resource
    private LibraryService libraryService;

    private Class<R> elementVoClass;

    @Autowired
    private BaseElementDao<T> baseElementDao;

    @SuppressWarnings("unchecked")
    protected BaseElementServiceImpl() {
        ResolvableType resolvableType = ResolvableType.forClass(getClass());
        elementVoClass = (Class<R>) resolvableType.getSuperType().getGeneric(1).resolve();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<T> findPage(Pageable pageable) {
        pageable.getOrders().add(Order.asc("index"));
        pageable.getFilter().and(Filter.eq("library", libraryService.getCurrent()));
        return super.findPage(pageable);
    }

    @Override
    public T persist(T element) {
        Library current = libraryService.getCurrent();
        Assert.notNull(current, "未选择词库");
        element.setLibrary(current);
        if (!prototypeExists(element.getPrototype())) {
            if (element.getIndex() == null) element.setIndex(baseElementDao.findMaxIndex(current) + 1);
            return super.persist(element);
        } else {
            BaseElement ref = findByPrototype(element.getPrototype());
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
        elementList.removeIf(element -> elementList.stream().filter(e -> e.getPrototype().equals(element.getPrototype())).count() > 1);
        int index = baseElementDao.findMaxIndex(current);
        for (T element : elementList) {
            if (element.getIndex() == null) element.setIndex(++index);
        }
        Collection<T> exists = findByPrototypes(elementList.stream().map(BaseElement::getPrototype).collect(Collectors.toSet()));
        elementList.removeIf(element -> {
            exists.stream().filter(ele -> element.getPrototype().equalsIgnoreCase(ele.getPrototype())).findAny().ifPresent(exist -> element.setId(exist.getId()));
            return element.getId() != null;
        });
        elementList.forEach(element -> element.setLibrary(current));
        elements.removeAll(elementList);
        this.update(elements);
        super.persist(elementList);
    }

    @Override
    public void update(Collection<T> elements, String... ignoreProperties) {
        Library current = libraryService.getCurrent();
        Assert.notNull(current, "未选择词库");
        int index = baseElementDao.findMaxIndex(current);
        for (T element : elements) {
            if (element.getIndex() == null) element.setIndex(++index);
        }
        super.update(elements, DictArray.concat(ignoreProperties, new String[] {"library", "frequency", "frequencyInfo"}));
    }

    @Override
    public T update(T element, String... ignoreProperties) {
        Library current = libraryService.getCurrent();
        Assert.notNull(current, "未选择词库");
        if (element.getIndex() == null) element.setIndex(baseElementDao.findMaxIndex(current) + 1);
        return super.update(element, DictArray.concat(ignoreProperties, new String[] {"library", "frequency", "frequencyInfo"}));
    }

    @Override
    @Transactional
    @SneakyThrows(IOException.class)
    public int imports(Resource resource) {
        InputStream stream = resource.getInputStream();
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

    @Override
    public boolean prototypeExists(String prototype) {
        Library current = libraryService.getCurrent();
        return count(Filter.eqic("prototype", prototype).and(Filter.eq("library", current))) > 0;
    }

    @Override
    public T findByPrototype(String prototype) {
        Library current = libraryService.getCurrent();
        return findSingle(Filter.eqic("prototype", prototype).and(Filter.eq("library", current)));
    }

    @Override
    public Collection<T> findByPrototypes(Collection<String> prototypes) {
        Library current = libraryService.getCurrent();
        return findList(Filter.inIgnoreCase("prototype", prototypes).and(Filter.eq("library", current)));
    }

    @Override
    public Map<String, Number> findMaxInfo() {
        Library current = libraryService.getCurrent();
        return baseElementDao.findMaxInfo(current);
    }
}
