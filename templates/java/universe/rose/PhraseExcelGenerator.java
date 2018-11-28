package rose;

import com.benzolamps.dict.bean.Phrase;
import com.benzolamps.dict.component.IStreamDocumentGenerator;
import com.benzolamps.dict.util.DictExcel;
import com.benzolamps.dict.util.lambda.Lambdas;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Value;

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
public class PhraseExcelGenerator implements IStreamDocumentGenerator<Phrase> {

    @Value("file:templates/xlsx/phrase-excel.xlsx")
    private org.springframework.core.io.Resource sampleExcelResource;

    @Override
    public String getExt() {
        return "xlsx";
    }

    @Override
    public void generate(OutputStream outputStream, Collection<Phrase> phrases, String title) {
        Workbook workbook = DictExcel.inputStreamToWorkbook(Lambdas.tryFunc(sampleExcelResource::getInputStream));
        Sheet sheet = workbook.getSheetAt(0);
        workbook.setSheetName(0, title);
        List<CellStyle> cellStyles = IntStream.range(0, 3).mapToObj(i -> sheet.getRow(1).getCell(i).getCellStyle()).peek(cellStyle -> {
            cellStyle.setBorderRight(CellStyle.BORDER_THIN);
            cellStyle.setBorderLeft(cellStyle.BORDER_THIN);
            cellStyle.setBorderBottom(cellStyle.BORDER_THIN);
            cellStyle.setBorderTop(cellStyle.BORDER_THIN);
        }).collect(Collectors.toList());
        Iterator<Phrase> phraseIterator = phrases.iterator();
        for (int i = 0; i < phrases.size(); i++) {
            Phrase phrase = phraseIterator.next();
            Row row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(phrase.getIndex() == null ? i + 1 : phrase.getIndex());
            row.createCell(1).setCellValue(phrase.getPrototype());
            row.createCell(2).setCellValue(phrase.getDefinition());
            IntStream.range(0, 3).forEach(j -> row.getCell(j).setCellStyle(cellStyles.get(j)));
        }
        Lambdas.tryAction(() -> workbook.write(outputStream));
    }
}
