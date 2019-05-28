package mx.com.aion.data.util;

import mx.com.aion.data.models.entity.ServiceTagsParameters;
import java.util.Map;

public class ValidateTags {

    private Map<String, ServiceTagsParameters> dataParameters;
    private Map<String, String> dataValues;

    public ValidateTags(Map<String, ServiceTagsParameters> dataParameters, Map<String, String> dataValues) {
        this.dataParameters = dataParameters;
        this.dataValues = dataValues;
    }

    public boolean isACorrectValue(String tagName, String tagValue){
        //Validamos si el campo es obligatorio y está vacío
        String listTagValues = dataParameters.get(tagName).getTagValues();
        if(dataParameters.get(tagName).isObligatory()){
            if(tagValue.isEmpty() || tagValue == null){
                return false;
            }
            //Validamos longitud minima y máxima
            if (tagValue.length() < dataParameters.get(tagName).getMinLength() || tagValue.length() > dataParameters.get(tagName).getMaxLength()){
                return false;
            }
            if(dataParameters.get(tagName).getTagTypeDesc().contentEquals("INT")){
                if (containsLetters(tagValue)){
                    return false;
                }
            }
            if(dataParameters.get(tagName).getTagTypeDesc().contentEquals("ALFABETIC")){
                if (containsNumbers(tagValue)){
                    return false;
                }
            }
            //Validamos si tiene una lista de valores predeterminados
            if(listTagValues != null && !listTagValues.isEmpty()){
                if(!validateListOfValues(tagValue, listTagValues)){
                    return false;
                }
            }
            if (dataParameters.get(tagName).getFormat() != null && !dataParameters.get(tagName).getFormat().isEmpty()){
                ValidateRegularExp validateRegularExp = new ValidateRegularExp();
                return validateRegularExp.validateRegExp(tagValue, dataParameters.get(tagName).getFormat());
            }
            /*if(dataParameters.get(tagName).getDataType().contentEquals("DATE")){

            }*/
        }
        else {
            if (dataParameters.get(tagName).getDepStructure() != null && !dataParameters.get(tagName).getDepStructure().isEmpty()){
                ParseDependencies parseDependencies = new ParseDependencies();
                if(parseDependencies.validateDepTag(dataParameters.get(tagName).getDepStructure(), dataValues)){
                    if (tagValue == null || tagValue.isEmpty()){
                        return false;
                    }
                }
            }
            if (!tagValue.isEmpty()){
                //Validamos longitud minima y máxima
                if (tagValue.length() < dataParameters.get(tagName).getMinLength() || tagValue.length() > dataParameters.get(tagName).getMaxLength()){
                    return false;
                }
                if(dataParameters.get(tagName).getTagTypeDesc().contentEquals("INT")){
                    if (containsLetters(tagValue)){
                        return false;
                    }
                }
                if(dataParameters.get(tagName).getTagTypeDesc().contentEquals("ALFABETIC")){
                    if (containsNumbers(tagValue)){
                        return false;
                    }
                }
                if(listTagValues != null && !listTagValues.isEmpty()){
                    if(!validateListOfValues(tagValue, listTagValues)){
                        return false;
                    }
                }
                if (dataParameters.get(tagName).getFormat() != null && !dataParameters.get(tagName).getFormat().isEmpty()){
                    ValidateRegularExp validateRegularExp = new ValidateRegularExp();
                    return validateRegularExp.validateRegExp(tagValue, dataParameters.get(tagName).getFormat());
                }
                /*if(dataParameters.get(tagName).getDataType().contentEquals("DATE")){

                }*/
            }
        }
        return true;
    }

    private boolean validateListOfValues(String tagValue, String listTagValues){
        return listTagValues.contains(tagValue);
    }

    private static boolean containsNumbers(String tagValue){
        char[] cadena_div = tagValue.toCharArray();
        for(int i = 0; i < tagValue.length(); i++){
            if(Character.isDigit(cadena_div[i])){
                return true;
            }
        }
        return false;
    }

    private static boolean containsLetters(String tagValue){
        char[] cadena_div = tagValue.toCharArray();
        for(int i = 0; i < tagValue.length(); i++){
            if(Character.isAlphabetic(cadena_div[i])){
                return true;
            }
        }
        return false;
    }

}
