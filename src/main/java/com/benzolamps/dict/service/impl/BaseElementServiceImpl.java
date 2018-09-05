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
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
        return super.persist(element);
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
        Library current = libraryService.getCurrent();
        Assert.notNull(current, "未选择词库");
        InputStream stream = tryFunc(resource::getInputStream);
        Workbook workbook = DictExcel.inputStreamToWorkbook(stream);
        Sheet sheet = workbook.getSheetAt(0);
        /* 用List因为equals方法返回值相同 */
        List<T> elements = new LinkedList<>();
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            BaseElementVo<T> baseElementVo = DictExcel.excelRowToObject(sheet.getRow(i), elementVoClass);
            T element = baseElementVo.convertToElement();
            element.setLibrary(current);
            elements.add(element);
        }
        Set<String> notContains = baseElementDao.findNotContainsPrototype(elements.stream()
            .map(BaseElement::getPrototype).collect(Collectors.toSet()), current);
        elements.removeIf(element ->
            !notContains.contains(element.getPrototype()) ||
            elements.stream().filter(e -> e.getPrototype().equals(element.getPrototype())).count() > 1);
        this.persist(elements);
        return elements.size();
    }

    @Override
    public boolean contains(String prototype) {
        Library library = libraryService.getCurrent();
        Assert.notNull(library, "未选择词库");
        return count(Filter.eq("library", library).and(Filter.eq("prototype", prototype))) > 0;
    }
}
