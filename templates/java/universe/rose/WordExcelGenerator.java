package rose;

import com.benzolamps.dict.bean.BaseElement;
import com.benzolamps.dict.bean.Word;
import com.benzolamps.dict.bean.WordClazz;
import com.benzolamps.dict.component.IStreamDocumentGenerator;
import com.benzolamps.dict.util.DictExcel;
import com.benzolamps.dict.util.lambda.Lambdas;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;

import java.io.OutputStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 单词生成Excel表格
 * @author Benzolamps
 * @version 2.3.5
 * @datetime 2018-11-28 15:44:26
 */
public class WordExcelGenerator implements IStreamDocumentGenerator<Word> {


    @Value("file:templates/xlsx/word-excel.xlsx")
    private org.springframework.core.io.Resource sampleExcelResource;

    @Override
    public String getExt() {
        return "xlsx";
    }

    @Override
    public void generate(OutputStream outputStream, Collection<Word> words) {
        Workbook workbook = DictExcel.inputStreamToWorkbook(Lambdas.tryFunc(sampleExcelResource::getInputStream));
        Sheet sheet = workbook.getSheetAt(0);
        List<CellStyle> cellStyles = IntStream.range(0, 6).mapToObj(i -> sheet.getRow(1).getCell(i).getCellStyle()).peek(cellStyle -> {
            cellStyle.setBorderRight(CellStyle.BORDER_THIN);
            cellStyle.setBorderLeft(cellStyle.BORDER_THIN);
            cellStyle.setBorderBottom(cellStyle.BORDER_THIN);
            cellStyle.setBorderTop(cellStyle.BORDER_THIN);
        }).collect(Collectors.toList());
        Iterator<Word> wordIterator = words.iterator();
        for (int i = 0; i < words.size(); i++) {
            Word word = wordIterator.next();
            Row row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(word.getIndex() == null ? i + 1 : word.getIndex());
            row.createCell(1).setCellValue(word.getPrototype());
            row.createCell(2).setCellValue(word.getBritishPronunciation());
            row.createCell(3).setCellValue(word.getAmericanPronunciation());
            row.createCell(4).setCellValue(
                CollectionUtils.isEmpty(word.getClazzes()) ?
                    null :
                    word.getClazzes().stream().map(WordClazz::getName).collect(Collectors.joining("，", "", ""))
            );
            row.createCell(5).setCellValue(word.getDefinition());
            IntStream.range(0, 6).forEach(j -> row.getCell(j).setCellStyle(cellStyles.get(j)));
        }
        Lambdas.tryAction(() -> workbook.write(outputStream));
    }
}
