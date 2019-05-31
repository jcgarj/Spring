package mx.com.aion.data.io;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

//import static com.sun.xml.internal.ws.spi.db.BindingContextFactory.LOGGER;

public class WriteResultsTestingFile {

    private FileOutputStream elFichero;
    private XSSFWorkbook libro;
    private ArrayList<String> resultsCabeceras = new ArrayList<>();
    private String servicio;
    private String path;

    public WriteResultsTestingFile(){}

    WriteResultsTestingFile(String servicio, String path){
        this.servicio = servicio;
        this.path = path;
        resultsCabeceras.add("Request enviado");
        resultsCabeceras.add("Resultado esperado");
        resultsCabeceras.add("Servicio");
        resultsCabeceras.add("Response del servicio");
        resultsCabeceras.add("Analisis del resultado");
        resultsCabeceras.add("Fecha de la prueba");
    }

    XSSFSheet createExcel(){
        int j = 0;
        libro = new XSSFWorkbook();
        XSSFSheet hoja = libro.createSheet("Results "+ servicio);
        XSSFRow fila = hoja.createRow(0);
        fila.createCell(j++).setCellValue("Caso de prueba");
        for (String resultsCabecera : resultsCabeceras) {
            fila.createCell(j++).setCellValue(resultsCabecera);
        }
        try
        {
            String absolutePath = path + "\\Results " + servicio +".xlsx";
            System.out.println(absolutePath);
            elFichero = new FileOutputStream(absolutePath);
            libro.write(elFichero);
            elFichero.close();
        }
        catch (Exception e)
        {
            //LOGGER.log(Level.SEVERE, "Error al obtener la información de la matriz de pruebas ", e);
        }
        return hoja;
    }

    void whriteExcel(ArrayList<Map> resultados, XSSFSheet hoja){
        int i = 1;
        for (Map<String, String> dato: resultados) {
            int j = 0;
            XSSFRow fila = hoja.createRow(i++);

            fila.createCell(j++).setCellValue(i - 1);
            fila.createCell(j++).setCellValue(dato.get("sentRequest"));
            fila.createCell(j++).setCellValue(dato.get("expectedResult"));
            fila.createCell(j++).setCellValue(dato.get("service"));
            fila.createCell(j++).setCellValue(dato.get("obtainedResult"));
            fila.createCell(j++).setCellValue(dato.get("analysisResult"));
            fila.createCell(j).setCellValue(dato.get("dateTest"));

            try
            {
                elFichero = new FileOutputStream(path + "\\Results " + servicio +".xlsx");
                libro.write(elFichero);
                elFichero.close();
            }
            catch (IOException e)
            {
              //  LOGGER.log(Level.SEVERE, "Error al registrar los resultados en el archivo excel ", e);
            }
        }

    }
}
