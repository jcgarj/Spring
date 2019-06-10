package mx.com.aion.data.util;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

class ParseDependencies {

    boolean validateDepTag(String depStructure, Map<String, String> dataValues) {

        int count = StringUtils.countMatches(depStructure, "[");
        if (count == 1) {

            Map<String, String> dataParametersMap = new HashMap<>();
            depStructure = depStructure.replace("{", "");
            depStructure = depStructure.replace("}", "");
            depStructure = depStructure.replace("[", "");
            depStructure = depStructure.replace("]", "");

            int countSplitsOr = StringUtils.countMatches(depStructure, "|");
            int countSplitsAnd = StringUtils.countMatches(depStructure, "&");

            if (countSplitsAnd == 0 && countSplitsOr != 0) {
                depStructure = depStructure.replace("|", ".");
                String[] parameters = depStructure.split("\\.");
                for (int i = 0; i < parameters.length - 1; i += 2) {
                    dataParametersMap.put(parameters[i], parameters[i + 1]);
                }

                Iterator<String> iterador = dataParametersMap.keySet().iterator();
                while (iterador.hasNext()) {
                    String key = iterador.next();
                    if (dataParametersMap.get(key).compareTo(dataValues.get(key)) == 0) {
                        return true;
                    }
                }
                return false;
            } else if (countSplitsAnd != 0 && countSplitsOr == 0) {
                depStructure = depStructure.replace("&", ".");
                String[] parameters = depStructure.split("\\.");
                for (int i = 0; i < parameters.length - 1; i += 2) {
                    dataParametersMap.put(parameters[i], parameters[i + 1]);
                }

                Iterator<String> iterador = dataParametersMap.keySet().iterator();
                int countExp = 0;
                while (iterador.hasNext()) {
                    String key = iterador.next();
                    if (dataParametersMap.get(key).compareTo(dataValues.get(key)) == 0) {
                        countExp++;
                    }
                }
                return countExp == (countSplitsAnd + 1);
            }
        }
        return false;
    }
}
