package mx.com.aion.data.models.dao;

public class ObtainParametersProcessDAO {

    public static String obtainServiceId(String serviceName){
        return "SELECT SERVICE_ID FROM WM_SERVICE WHERE SERVICE_NAME = '" + serviceName + "'";
    }

    public static String obtainServiceId(){
        return "SELECT SERVICE_ID FROM WM_SERVICE WHERE SERVICE_NAME = ?";
    }

    public static String obtainAllInfoByServiceName(){
        return  "SELECT DISTINCT RST.TAG_NAME_ID,\n" +
                "       T.MIN_LENGTH,\n" +
                "       T.MAX_LENGTH,\n" +
                "       CTY.TAG_TYPE_DESC,\n" +
                "       RST.OBLIGATORY,\n" +
                "       CTA.DAY_LENGTH, \n" +
                "       CTA.MONTH_LENGTH, \n" +
                "       CTA.YEAR_LENGTH, \n" +
                "       CTA.AMOUT_INT_LENGTH, \n" +
                "       CTA.AMOUT_DEC_LENGTH, \n" +
                "       CTA.SPLIT, \n" +
                "       CTA.FORMAT, \n" +
                "       CTA.TAG_VALUES,\n" +
                "       D.DEP_STRUCTURE\n" +
                "  FROM WM_SERVICE S\n" +
                "  JOIN WM_CAT_SERVICE_TYPE CST\n" +
                "    ON S.SERVICE_TYPE_ID = CST.SERVICE_TYPE_ID\n" +
                "  JOIN WM_REL_SERVICE_TAG RST\n" +
                "    ON S.SERVICE_ID = RST.SERVICE_ID\n" +
                "  JOIN WM_TAG T\n" +
                "    ON T.TAG_NAME_ID = RST.TAG_NAME_ID\n" +
                "  JOIN WM_CAT_TAG_TYPE CTY\n" +
                "    ON CTY.TAG_TYPE_ID = T.TAG_TYPE_ID\n" +
                "LEFT JOIN WM_CONFIG_TAG_ADITIONAL CTA\n" +
                "    ON T.TAG_NAME_ID = CTA.TAG_NAME_ID\n" +
                "LEFT JOIN WM_DEPENDENCY D\n" +
                "    ON D.DEPENDENCY_ID = RST.DEPENDENCY_ID\n" +
                " WHERE S.SERVICE_ID = (" + obtainServiceId() + ")\n" +
                "   AND S.PROJECT_ID = 1 ";
    }

    public static String obtainTagsNamesByServiceName(String serviceName){
        return  "SELECT DISTINCT RST.TAG_NAME_ID\n" +
                "  FROM WM_SERVICE S\n" +
                "  JOIN WM_REL_SERVICE_TAG RST\n" +
                "    ON S.SERVICE_ID = RST.SERVICE_ID\n" +
                "  JOIN WM_TAG T\n" +
                "    ON T.TAG_NAME_ID = RST.TAG_NAME_ID\n" +
                " WHERE S.SERVICE_ID = (" + obtainServiceId(serviceName) + ")\n" +
                "   AND S.PROJECT_ID = 1 \n";
    }

    public static String obtainPathMatrixByServiceName(String serviceName){
        return  "SELECT SERVICE_MATRIZ_URL URL_MATRIZ\n" +
                "  FROM WM_SERVICE \n" +
                " WHERE SERVICE_ID = (" + obtainServiceId(serviceName) + ")\n" +
                "   AND PROJECT_ID = 1";
    }

    public static String obtainUrlServiceByServiceName(String serviceName){
        return  "SELECT SERVICE_URL URL_SERVICE\n" +
                "  FROM WM_SERVICE \n" +
                " WHERE SERVICE_ID = (" + obtainServiceId(serviceName) + ")\n" +
                "   AND PROJECT_ID = 1";
    }

}
