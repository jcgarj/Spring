package mx.com.aion.data.io;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReadDataTestingFile {

    private List<String> cabeceras;
    private String serviceName;
    private String path;

    public ReadDataTestingFile(List<String> cabeceras, String serviceName, String path) {
        this.cabeceras = cabeceras;
        this.path = path;
        this.serviceName = serviceName;
    }

    public ArrayList<Map> readExcel() throws IOException, InvalidFormatException {

        XSSFWorkbook wb = new XSSFWorkbook(new File(path + serviceName + ".xlsx"));
        XSSFSheet sheet = wb.getSheetAt(0);
        Map<String, String> collection;
        ArrayList<Map> collectionMap = new ArrayList<>();
        int filas = sheet.getLastRowNum();

        for (int i = 1; i <= filas; i++) {
            ArrayList<XSSFCell> datosExcel = new ArrayList<>();
            XSSFRow row = sheet.getRow(i);
            collection = new HashMap<>();
            for (int d = 0; d < cabeceras.size(); d++) {
                datosExcel.add(row.getCell(d));
            }

            for (int r = 0; r < cabeceras.size(); r++) {
                if (datosExcel.get(r) == null) {
                    collection.put(cabeceras.get(r), "");
                } else {
                    collection.put(cabeceras.get(r), datosExcel.get(r).toString());
                }
            }
            collectionMap.add(collection);
        }
        wb.close();
        return collectionMap;
    }
}
