package mx.com.aion.data.service;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import bsh.Interpreter;
import mx.com.aion.data.enums.Bandera;
import mx.com.aion.data.models.dao.IDsbCfgAdcQueryDao;
import org.json.JSONObject;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Service;

import javax.servlet.jsp.jstl.sql.Result;
import javax.servlet.jsp.jstl.sql.ResultSupport;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Clase que realiza las validaciones configuradas en la BD a partir de un grupo
 * definido.
 *
 * @author Victor Salinas
 * @version 1.0
 */
@Service
public class ValidationRuleService {

    /**
     * Variables de servicios inyectados
     */
    private JdbcTemplate jdbcTemplate;
    private IDsbCfgAdcQueryDao dsbCfgAdocQueryService;

    public ValidationRuleService(JdbcTemplate jdbcTemplate, IDsbCfgAdcQueryDao dsbCfgAdocQueryService) {
        this.jdbcTemplate = jdbcTemplate;
        this.dsbCfgAdocQueryService = dsbCfgAdocQueryService;
    }

    /**
     * Variables privadas de instancia
     */
    //private SqlConnectionBean isql_ConnBean = null;
    private Connection iconn_Connection = null;
    private Map<String, String> im_fieldErrors = null;
    private Class icls_beanClass = null;
    private Object iobj_newValue = null;
    private Map<String, Object> im_newValues = null;

    /**
     * Aplica las validaciones para cada uno de los valores definidos en el bean
     * a partir de la configuracion generada en la BD.
     *
     * @param as_groupId    Id del grupo donde se encuentran las reglas de
     *                      validacoin
     * @param aobj_beanData
     * @param ai_rowNumber  Numero de registro a procesar
     * @return Verdadero si todas las validaciones fueron correctas
     */
    public boolean generateValidation(String as_groupId, int ai_rowNumber, Object aobj_beanData, String vcDebug) {
        im_fieldErrors = new HashMap<String, String>();
        im_newValues = new HashMap<String, Object>();
        Result lrs_Rules = null;
        iobj_newValue = null;
        int li_Row = 0;
        int li_cond;
        SortedMap lsm_Rules[] = null;
        boolean lb_validation = false;
        HashMap<String, Object> lhm_values = null;
        Vector lv_dependence = new Vector();
        Vector lv_Condition = new Vector();
        Vector lv_Rule = new Vector();
        try {
            // obtiene las reglas configuradas para este bean, de acuerdo al grupo definido
            lrs_Rules = getValidationRules(as_groupId, ai_rowNumber);
            // Recorre cada una de las reglas obtenidas para validarlas con la informacion del bean
            if (lrs_Rules != null) {
                lsm_Rules = lrs_Rules.getRows();
                for (li_Row = 0; li_Row < lsm_Rules.length; li_Row++) {
                    // Se imprime el numero de regla que se va a aplicar
                    if (Bandera.VERDADERO.getValue().equals(vcDebug)) {
                        String orden = lsm_Rules[li_Row].get("N_RULE_ORDER").toString();
                        String descErr = lsm_Rules[li_Row].get("VC_RULE_CONFIG_DESC_ERROR").toString();

                        //System.out.println("\tProcesando "+descErr.substring(descErr.indexOf("REGLA"))+" Orden "+orden );
                    }//VC_RULE_FIELD

                    // Limpia el hashMap de dependencias
                    if (lhm_values != null && lhm_values.size() > 0) {
                        lhm_values.clear();
                    }
                    // verifica si el campo tiene dependencias
                    if (lsm_Rules[li_Row].get("VC_RULE_DEPENDENCE") != null && !lsm_Rules[li_Row].get("VC_RULE_DEPENDENCE").toString().equals("")) {
                        // Obtiene los valores de los campos dependencia
                        lhm_values = getvaluesFromBeanDependence(lsm_Rules[li_Row].get("VC_RULE_DEPENDENCE").toString(), aobj_beanData, lv_dependence, ai_rowNumber);
                    }
                    // Verifica si el campo tiene una regla definida
                    if (lsm_Rules[li_Row].get("VC_RULE_CONFIG_VALUE") != null && !lsm_Rules[li_Row].get("VC_RULE_CONFIG_VALUE").toString().equals("")) {
                        if (validateData(lsm_Rules[li_Row].get("VC_RULE_FIELD").toString(), lsm_Rules[li_Row].get("VC_RULE_CONFIG_VALUE").toString(), lsm_Rules[li_Row].get("VC_RULE_CONFIG_DESC_ERROR").toString(), BeanManipulation.getDataFromBeanMethod(aobj_beanData.getClass(), aobj_beanData, lsm_Rules[li_Row].get("VC_RULE_FIELD").toString()), ai_rowNumber, lhm_values)) {
                            BeanManipulation.setDataToBeanMethod(aobj_beanData.getClass(), aobj_beanData, lsm_Rules[li_Row].get("VC_RULE_FIELD").toString(), iobj_newValue);
                            if (Bandera.VERDADERO.getValue().equals(vcDebug))
                                System.out.println("\t\tRegla aplicada, campo " + lsm_Rules[li_Row].get("VC_RULE_FIELD").toString() + " = " + iobj_newValue);
                        }
                    }
                }
            } else {
                lb_validation = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            getErrors().put(ai_rowNumber + "-Genera Validación", e.toString());
        } finally {
            lrs_Rules = null;
            lsm_Rules = null;
            lhm_values = null;
            lv_dependence = null;
        }
        return lb_validation;
    }

    /**
     * Aplica las validaciones para cada uno de los valores definidos en el hash
     * map, que se convertira en arreglo, a partir de la configuracion generada
     * en la BD.
     *
     * @param as_groupId   Id del grupo donde se encuentran las reglas de
     *                     validacoin
     * @param ai_rowNumber Numero del registro a validar
     * @param ahm_Data     Hash Map con los valores a validar, que se convertira en
     *                     un arreglo
     * @return Verdadero si todas las validaciones son correctas
     */
    public boolean generateValidation(String as_groupId, int ai_rowNumber, HashMap ahm_Data, String vcDebug) {
        return generateValidation(as_groupId, ai_rowNumber, ahm_Data.values().toArray(), vcDebug);
    }

    /**
     * Aplica las validaciones para cada uno de los valores definidos en la
     * cadena, que se convertira en arreglo, a partir de la configuracion
     * generada en la BD.
     *
     * @param as_groupId   Id del grupo donde se encuentran las reglas de
     *                     validacoin
     * @param ai_rowNumber Numero del registro a validar
     * @param as_Data      Los datos a validar formateados en una cadena
     * @param as_separator caracter separador de los datos a validar, para
     *                     geenerar un arreglo
     * @return Verdadero si todas las validaciones son correctas
     */
    public boolean generateValidation(String as_groupId, int ai_rowNumber, String as_Data, String as_separator) {
        return generateValidation(as_groupId, ai_rowNumber, as_Data.split(as_separator));
    }

    /**
     * Aplica las validaciones para cada uno de los valores definidos en el
     * arreglo a partir de la configuracion generada en la BD.
     *
     * @param as_groupId   Id del grupo donde se encuentran las reglas de
     *                     validacoin
     * @param ai_rowNumber Numero del registro a validar
     * @param aas_Data     Arreglo con los valores a validar
     * @return Verdadero si todas las validaciones son correctas
     */
    public boolean generateValidation(String as_groupId, int ai_rowNumber, String[] aas_Data) {
        im_fieldErrors = new HashMap<String, String>();
        Result lrs_Rules = null;
        int li_Row = 0;
        SortedMap lsm_Rules[] = null;
        boolean lb_validation = false;
        HashMap<String, Object> lhm_values = null;
        Vector lv_dependence = new Vector();
        try {
//            if (isql_ConnBean == null) {
//               isql_ConnBean = new SqlConnectionBean();
//                isql_ConnBean.setConnection(jdbcTemplate.getDataSource().getConnection());
//            }
            // obtiene las reglas configuradas para este bean, de acuerdo al grupo definido
            lrs_Rules = getValidationRules(as_groupId, ai_rowNumber);
            // Recorre cada una de las reglas obtenidas para validarlas con la informacion del bean
            if (lrs_Rules != null) {
                lsm_Rules = lrs_Rules.getRows();
                for (li_Row = 0; li_Row < lsm_Rules.length; li_Row++) {
                    // Limpia el hashMap de dependencias
                    if (lhm_values != null) {
                        lhm_values.clear();
                    }
                    // verifica si el campo tiene dependencias
                    if (lsm_Rules[li_Row].get("VC_RULE_DEPENDENCE") != null && !lsm_Rules[li_Row].get("VC_RULE_DEPENDENCE").toString().equals("")) {
                        // Obtiene los valores de los campos dependencia
                        lhm_values = getvaluesFromArrayDependence(lsm_Rules[li_Row].get("VC_RULE_DEPENDENCE").toString(), aas_Data, lv_dependence, ai_rowNumber);
                    }
                    // Verifica si el campo tiene una regla definida
                    if (lsm_Rules[li_Row].get("VC_RULE_CONFIG_VALUE") != null && !lsm_Rules[li_Row].get("VC_RULE_CONFIG_VALUE").toString().equals("")) {
                        // Asigna un valor directamente al campo sin hacer ninguna validacion
                        if (lsm_Rules[li_Row].get("VC_RULE_CONFIG_VALUE").toString().startsWith(":")) {
                            aas_Data[Integer.parseInt(lsm_Rules[li_Row].get("VC_RULE_FIELD").toString().substring(1))] = lsm_Rules[li_Row].get("VC_RULE_CONFIG_VALUE").toString().substring(1);
                        } else if (validateData(lsm_Rules[li_Row].get("VC_RULE_FIELD").toString().substring(0, 1) + (Integer.parseInt(lsm_Rules[li_Row].get("VC_RULE_FIELD").toString().substring(1)) + 1), lsm_Rules[li_Row].get("VC_RULE_CONFIG_VALUE").toString(), lsm_Rules[li_Row].get("VC_RULE_CONFIG_DESC_ERROR").toString(), aas_Data[Integer.parseInt(lsm_Rules[li_Row].get("VC_RULE_FIELD").toString().substring(1))], ai_rowNumber, lhm_values)) {
                            aas_Data[Integer.parseInt(lsm_Rules[li_Row].get("VC_RULE_FIELD").toString().substring(1))] = iobj_newValue.toString();
                        }
                    }
                }
            } else {
                lb_validation = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            getErrors().put(ai_rowNumber + "-Genera Validación", e.toString());
        } finally {
            lrs_Rules = null;
            lsm_Rules = null;
            lhm_values = null;
            lv_dependence = null;
        }
        return lb_validation;
    }

    /**
     * Aplica las validaciones para cada uno de los valores definidos en el
     * objeto Json a partir de la configuracion generada en la BD.
     *
     * @param as_groupId   Id del grupo donde se encuentran las reglas de
     *                     validacoin
     * @param ai_rowNumber Numero del registro a validar
     * @param aas_Data     Objeto Json con los valores a validar
     * @return Verdadero si todas las validaciones son correctas
     */
    public boolean generateValidation(String as_groupId, int ai_rowNumber, JSONObject aas_Data) {
        im_fieldErrors = new HashMap<String, String>();
        Result lrs_Rules = null;
        int li_Row = 0;
        SortedMap lsm_Rules[] = null;
        boolean lb_validation = false;
        HashMap<String, Object> lhm_values = null;
        Vector lv_dependence = new Vector();
        try {
//            if (isql_ConnBean == null) {
//                isql_ConnBean = new SqlConnectionBean();
//                isql_ConnBean.setConnection(jdbcTemplate.getDataSource().getConnection());
//            }
            // obtiene las reglas configuradas para este bean, de acuerdo al grupo definido
            lrs_Rules = getValidationRules(as_groupId, ai_rowNumber);
            // Recorre cada una de las reglas obtenidas para validarlas con la informacion del bean
            if (lrs_Rules != null) {
                lsm_Rules = lrs_Rules.getRows();
                for (li_Row = 0; li_Row < lsm_Rules.length; li_Row++) {
                    // Limpia el hashMap de dependencias
                    if (lhm_values != null) {
                        lhm_values.clear();
                    }
                    // verifica si el campo tiene dependencias
                    if (lsm_Rules[li_Row].get("VC_RULE_DEPENDENCE") != null && !lsm_Rules[li_Row].get("VC_RULE_DEPENDENCE").toString().equals("")) {
                        // Obtiene los valores de los campos dependencia
                        lhm_values = getvaluesFromJsonDependence(lsm_Rules[li_Row].get("VC_RULE_DEPENDENCE").toString(), aas_Data, lv_dependence, ai_rowNumber);
                    }
                    // Verifica si el campo tiene una regla definida
                    if (lsm_Rules[li_Row].get("VC_RULE_CONFIG_VALUE") != null && !lsm_Rules[li_Row].get("VC_RULE_CONFIG_VALUE").toString().equals("")) {
                        // Asigna un valor directamente al campo sin hacer ninguna validacion
                        if (lsm_Rules[li_Row].get("VC_RULE_CONFIG_VALUE").toString().startsWith(":")) {
                            aas_Data.put(lsm_Rules[li_Row].get("VC_RULE_FIELD").toString(), lsm_Rules[li_Row].get("VC_RULE_CONFIG_VALUE").toString());
                        } else if (validateData(lsm_Rules[li_Row].get("VC_RULE_FIELD").toString(), lsm_Rules[li_Row].get("VC_RULE_CONFIG_VALUE").toString(), lsm_Rules[li_Row].get("VC_RULE_CONFIG_DESC_ERROR").toString(), aas_Data.getString(lsm_Rules[li_Row].get("VC_RULE_FIELD").toString()), li_Row, lhm_values)) {
                            aas_Data.put(lsm_Rules[li_Row].get("VC_RULE_FIELD").toString(), iobj_newValue.toString());
                        }
                    }
                }
            } else {
                lb_validation = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            getErrors().put(ai_rowNumber + "-Genera Validación", e.toString());
        } finally {
            lrs_Rules = null;
            lsm_Rules = null;
            lhm_values = null;
            lv_dependence = null;
        }
        return lb_validation;
    }

    /**
     * Metodo que obtiene las validaciones configuradas para un grupo especifico
     *
     * @param as_groupId Id del grupo para obtener las validaciones
     */
    private Result getValidationRules(String as_groupId, int ai_rowNumber) {
        try {
            /* Modificado el 24/05/2016 por Salvador López
             * Se inyecta servicio dsbCfgAdocQueryService y se resuelve la conexión utilizando Spring-JdbcTemplate
             */
            System.out.println(as_groupId + "-" + ai_rowNumber);
            String sql = dsbCfgAdocQueryService.findById("GET_RULE_VALIDATION").get().getVcQueryStatement().replaceAll("%GROUPID", as_groupId);
            Result result = (Result) jdbcTemplate.query(sql, new ResultSetExtractor<Object>() {
                public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
                    return ResultSupport.toResult(rs);
                }
            });
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            getErrors().put(ai_rowNumber + "-Obtiene Reglas", e.toString());
        }
        return null;
    }

    /**
     * @param as_rule
     * @param av_condition
     * @param av_rule
     * @param ai_rowNumber
     */
    private void getparseRules(String as_rule, Vector av_condition, Vector av_rule, int ai_rowNumber) {

        String las_rules[] = null;
        StringBuffer ls_condition = new StringBuffer();
        StringBuffer ls_rule = new StringBuffer();
        int li_idx = 0;
        try {
            las_rules = as_rule.split("%%");
            for (li_idx = 0; li_idx < las_rules.length; li_idx++) {
                if (ls_rule.length() > 0) {
                    ls_rule.delete(0, ls_rule.length());
                }
                if (ls_condition.length() > 0) {
                    ls_condition.delete(0, ls_condition.length());
                }
                ls_condition.append(las_rules[li_idx].substring(0, las_rules[li_idx].indexOf(":")));
                ls_rule.append(las_rules[li_idx].substring(las_rules[li_idx].indexOf(":")).substring(1, las_rules[li_idx].substring(las_rules[li_idx].indexOf(":")).indexOf("/>")));
                av_condition.add(ls_condition.toString());
                av_rule.add(ls_rule.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
            getErrors().put(ai_rowNumber + "-Separa Reglas", e.toString());
        } finally {
            ls_condition = null;
            ls_rule = null;
        }
    }

    /**
     * @param as_condition
     * @param as_dependence
     * @param ai_rowNumber
     * @return
     */
    private boolean getVerifyDependence(String as_condition, String as_dependence, int ai_rowNumber) {
        boolean lb_Ret = false;
        String aas_data[] = null;
        int li_Count;
        try {
            aas_data = as_condition.split(",");
            for (li_Count = 0; li_Count < aas_data.length; li_Count++) {
                if (aas_data[li_Count].equals(as_dependence)) {
                    lb_Ret = true;
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            getErrors().put(ai_rowNumber + "-Verifica Dependencias", e.toString());
        }
        return lb_Ret;
    }

    /**
     * Obtiene los valores de los campos dependencia para aplicar las reglas
     * definidas
     *
     * @param as_dependence Cadena que contiene el nombre de los campos
     *                      separados por "," que indican la dependencia
     * @param aobj_beanData Bean que contiene el valor de los campos a validar
     * @return Objeto HashMap con los valores de los campos de dependencia
     */
    private HashMap<String, Object> getvaluesFromBeanDependence(String as_dependence, Object aobj_beanData, Vector av_dependence, int ai_rowNumber) {
        String aas_dependence[] = null;
        int li_Count;
        HashMap<String, Object> lhm_value = new HashMap<String, Object>();
        try {
            if (!as_dependence.equals("")) {
                aas_dependence = as_dependence.split(",");
                for (li_Count = 0; li_Count < aas_dependence.length; li_Count++) {
                    if (!aas_dependence[li_Count].equals("diasInhabiles") && !aas_dependence[li_Count].equals("fechaSistema")) {
                        lhm_value.put(aas_dependence[li_Count], BeanManipulation.getDataFromBeanMethod(aobj_beanData.getClass(), aobj_beanData, aas_dependence[li_Count]));
                    } else {
                        lhm_value.put(aas_dependence[li_Count], null);
                    }
                    av_dependence.add(aas_dependence[li_Count]);
                }
            }
        } catch (Exception e) {
            getErrors().put(ai_rowNumber + "-Valores Dependencia Bean", e.toString());
            e.printStackTrace();
        } finally {
            aas_dependence = null;
        }
        return lhm_value;
    }

    /**
     * Obtiene los valores de los campos dependencia para aplicar las reglas
     * definidas
     *
     * @param as_dependence Cadena que contiene el nombre de los campos
     *                      separados por "," que indican la dependencia
     * @return Objeto HashMap con los valores de los campos de dependencia
     */
    private HashMap<String, Object> getvaluesFromArrayDependence(String as_dependence, String[] aas_Data, Vector av_dependence, int ai_rowNumber) {
        String aas_dependence[] = null;
        int li_Count;
        HashMap<String, Object> lhm_value = new HashMap<String, Object>();
        try {
            if (!as_dependence.equals("")) {
                aas_dependence = as_dependence.split(",");
                for (li_Count = 0; li_Count < aas_dependence.length; li_Count++) {
                    if (!aas_dependence[li_Count].equals("diasInhabiles") && !aas_dependence[li_Count].equals("fechaSistema")) {
                        lhm_value.put(aas_dependence[li_Count], aas_Data[Integer.parseInt(aas_dependence[li_Count].substring(1))]);
                    } else {
                        lhm_value.put(aas_dependence[li_Count], null);
                    }
                    av_dependence.add(aas_dependence[li_Count]);
                }
            }
        } catch (Exception e) {
            getErrors().put(ai_rowNumber + "-Valores Dependencia Array", e.toString());
            e.printStackTrace();
        } finally {
            aas_dependence = null;
        }
        return lhm_value;
    }

    /**
     * Obtiene los valores de los campos dependencia para aplicar las reglas
     * definidas
     *
     * @param as_dependence Cadena que contiene el nombre de los campos
     *                      separados por "," que indican la dependencia
     * @return Objeto HashMap con los valores de los campos de dependencia
     */
    private HashMap<String, Object> getvaluesFromJsonDependence(String as_dependence, JSONObject aas_Data, Vector av_dependence, int ai_rowNumber) {
        String aas_dependence[] = null;
        int li_Count;
        HashMap<String, Object> lhm_value = new HashMap<String, Object>();
        try {
            if (!as_dependence.equals("")) {
                aas_dependence = as_dependence.split(",");
                for (li_Count = 0; li_Count < aas_dependence.length; li_Count++) {
                    if (!aas_dependence[li_Count].equals("diasInhabiles") && !aas_dependence[li_Count].equals("fechaSistema")) {
                        lhm_value.put(aas_dependence[li_Count], aas_Data.getString(aas_dependence[li_Count].substring(1)));
                    } else {
                        lhm_value.put(aas_dependence[li_Count], null);
                    }
                    av_dependence.add(aas_dependence[li_Count]);
                }
            }
        } catch (Exception e) {
            getErrors().put(ai_rowNumber + "-Valores Dependencia Json", e.toString());
            e.printStackTrace();
        } finally {
            aas_dependence = null;
        }
        return lhm_value;
    }

    /**
     * Define el tipo de validacion que se le tiene que realizar al campo
     * definido respecto al valor que contiene el bean
     *
     * @param as_fieldName Nombre del campo que se esta evaluando
     * @param as_rule      Regla de validacion (Regular Expression o Java Function)
     * @param as_descErr   Descripcion de error en caso de que sea la validacion
     *                     de una exp reg
     * @param aobj_value   Valor del campo a validar
     * @return
     */
    private boolean validateData(String as_fieldName, String as_rule, String as_descErr, Object aobj_value, int ai_rowNumber, HashMap ahm_dependence) {
        boolean lb_Ret = false;
        try {
            // Evalua si la regla es una expresion regular o un pedazo de codigo java
            if (aobj_value == null) {
                aobj_value = "";
            }
            if (as_rule.startsWith("=") && aobj_value.toString().trim() != null /*&& !"".equals(aobj_value.toString().trim())*/) {
                // Se ejecuta un codigo java                
                if (scriptingJavaValidation(as_fieldName, as_rule.substring(1), aobj_value.toString().trim(), ai_rowNumber, ahm_dependence)) {
                    lb_Ret = true;
                }
            } else if (as_rule.startsWith("/")) {
                // Se ejecuta una expresion regular
                lb_Ret = regExpValidation(as_fieldName, as_rule.substring(1), aobj_value.toString().trim(), ai_rowNumber, as_descErr);
            } else if (as_rule.startsWith("?")) {
                // Se ejecuta un query
                lb_Ret = queryValidation(as_fieldName, as_rule.substring(1), aobj_value.toString().trim(), ai_rowNumber, ahm_dependence, as_descErr);
            }
        } catch (Exception e) {
            e.printStackTrace();
            getErrors().put(ai_rowNumber + "-" + as_fieldName, "[" + aobj_value + "] -> " + "Validación de Datos: " + e.toString());
        }
        return lb_Ret;
    }

    /**
     * Metodo que realiza la validacion de un valor dado utillizando un codigo
     * Java <i>Scripting</i>
     *
     * @param as_fieldName Nombre del campo que se esta validando
     * @param as_rule      Codigo Java que se utilizara para la validacion
     * @return Verdadero si la validacion es correcta, falso en caso contrario
     */
    private boolean scriptingJavaValidation(String as_fieldName, String as_rule, Object aobj_data, int ai_rowNumber, HashMap ahm_dependence) {
        boolean lb_Ret = true;
        Iterator lit_key;
        Object lobj_key;
        Interpreter lint_interpreter = new Interpreter();  // Construct an interpreter
        try {
            // Inicia las variables a usar dentro de la funcion
            lint_interpreter.set("validateData", aobj_data);
            if (ahm_dependence != null && ahm_dependence.size() > 0) {
                lit_key = ahm_dependence.keySet().iterator();
                while (lit_key.hasNext()) {
                    lobj_key = lit_key.next();
                    if (lobj_key.toString().equals("diasInhabiles") || lobj_key.toString().equals("fechaSistema")) {
                        if (lobj_key.toString().equals("diasInhabiles")) {
                            lint_interpreter.set(lobj_key.toString(), getDiasInhabiles());
                        }
                        if (lobj_key.toString().equals("fechaSistema")) {
                            lint_interpreter.set(lobj_key.toString(), getFechaSistema());
                        }
                    } else {
                        lint_interpreter.set(lobj_key.toString(), ahm_dependence.get(lobj_key));
                    }
                }
            }
            lint_interpreter.set("resultValidate", lb_Ret);
            lint_interpreter.set("codeError", "");
            // Evalua la funcion de java
            lint_interpreter.eval(as_rule);
            lb_Ret = (Boolean) lint_interpreter.get("resultValidate");
            if (!lb_Ret) {
                getErrors().put(ai_rowNumber + "-" + as_fieldName, "[" + lint_interpreter.get("validateData").toString() + "] -> " + (String) lint_interpreter.get("codeError") + " (Validacion Java)");
            } else {
                iobj_newValue = lint_interpreter.get("validateData");
                im_newValues.put(as_fieldName, lint_interpreter.get("validateData"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            getErrors().put(ai_rowNumber + "-" + as_fieldName, "[" + aobj_data.toString() + "] -> " + "Validacion Java: " + e.toString());
            lb_Ret = false;
        } finally {
            lit_key = null;
            lobj_key = null;
        }
        return lb_Ret;
    }

    /**
     * Metodo que realiza la validacion de un valor dado utillizando una
     * expresion regular
     *
     * @param as_fieldName Nombre del campo que se esta validando
     * @param as_rule      Expresion regular que se utilizara como patron de
     *                     validacion
     * @param as_validate  valor que se desea validar
     * @param as_descErr   Descripcion del error si es que no cumple con la
     *                     validacion
     * @return Verdadero si la validacion es correcta, falso en caso contrario
     */
    private boolean regExpValidation(String as_fieldName, String as_rule, Object as_validate, int ai_rowNumber, String as_descErr) {
        boolean lb_Ret = false;
        Pattern lptt_regExp = null;
        Matcher lmch_Validate = null;
        try {
            lptt_regExp = Pattern.compile(as_rule);
            lmch_Validate = lptt_regExp.matcher(as_validate.toString());
            if (lmch_Validate.matches()) {
                lb_Ret = true;
                iobj_newValue = as_validate;
                im_newValues.put(as_fieldName, as_validate);
            } else {
                getErrors().put(ai_rowNumber + "-" + as_fieldName, "[" + as_validate.toString() + "] -> " + as_descErr + " (Validacion RegExp)");
            }
        } catch (Exception e) {
            e.printStackTrace();
            getErrors().put(ai_rowNumber + "-" + as_fieldName, "[" + as_validate.toString() + "] -> " + "Validacion RegExp: " + e.toString());
        } finally {
            lptt_regExp = null;
            lmch_Validate = null;
        }
        return lb_Ret;
    }

    /**
     * Metodo que realiza la validacion de un campo con base en la ejecucion de
     * un query
     *
     * @param as_fieldName   Nombre del campo que se esta validando
     * @param as_rule        Codigo Java que se utilizara para la validacion
     * @param aobj_data      Valor que se desea validar
     * @param ai_rowNumber   Numero de registro que se esta validando
     * @param ahm_dependence Los campos y valores de los cuales depende la
     *                       validacion
     * @param as_descErr     Descripcion del error en caso de que el query regrese 0
     *                       registros
     * @return Verdadero si la validacion es correcta, falso en caso contrario
     */
    private boolean queryValidation(String as_fieldName, String as_rule, Object aobj_data, int ai_rowNumber, HashMap ahm_dependence, String as_descErr) {
        boolean lb_Ret = false;
        Iterator lit_key;
        Object lobj_key;
        Result lrs_Rules = null;
        StringBuffer lsb_sentence = new StringBuffer();
        try {
            /* Modificado el 24/05/2016 por Salvador López
             * Se inyecta servicio dsbCfgAdocQueryService y se resuelve la conexión utilizando Spring-JdbcTemplate
             */
            lsb_sentence.append(dsbCfgAdocQueryService.findById(as_rule).get().getVcQueryStatement());

            if (ahm_dependence != null && ahm_dependence.size() > 0) {
                lit_key = ahm_dependence.keySet().iterator();
                while (lit_key.hasNext()) {
                    lobj_key = lit_key.next();
                    lsb_sentence.replace(0, lsb_sentence.length(), lsb_sentence.toString().replaceAll("%" + lobj_key.toString(), ahm_dependence.get(lobj_key).toString()));
                }
            }

            /* Modificado el 24/05/2016 por Salvador López
             * Se inyecta servicio dsbCfgAdocQueryService y se resuelve la conexión utilizando Spring-JdbcTemplate
             */
            lrs_Rules = (Result) jdbcTemplate.query(lsb_sentence.toString(), new ResultSetExtractor<Object>() {
                public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
                    return ResultSupport.toResult(rs);
                }
            });

            /* Modificado el 28/07/2011 por Victor Salinas
             * Se agrega una validacion para que cuando venga vacio el query retorne falso en la validacion de la regla
             */
            if (lrs_Rules.getRows().length > 0) {
                /* Modificado el 18/07/2011 por Victor Salinas
                 * Se agrega la logica para considerar si un registro existe dentro de una BD
                 */
                if (lrs_Rules.getRows()[0].get("EXIST") != null) {
                    if (Integer.parseInt(lrs_Rules.getRows()[0].get("EXIST").toString()) == 0) {
                        lb_Ret = true;
                        iobj_newValue = aobj_data;
                        im_newValues.put(as_fieldName, iobj_newValue);
                    } else {
                        //getErrors().put(ai_rowNumber + "-" + as_fieldName, "[" + aobj_data.toString() + "] -> " + as_descErr + " (Validacion Query)");
                        getErrors().put(ai_rowNumber + "-" + as_fieldName, "[" + aobj_data.toString() + "] -> " + as_descErr);
                    }
                } /*
                 * Valida que el registro exista y si el campo DATA existe entonces le asigna el valor al campo, en caso contrario la regla no es valida
                 */ else if (lrs_Rules.getRows()[0].get("COUNTER") != null) {
                    if (Integer.parseInt(lrs_Rules.getRows()[0].get("COUNTER").toString()) == 0) {
                        //getErrors().put(ai_rowNumber + "-" + as_fieldName, "[" + aobj_data.toString() + "] -> " + as_descErr + " (Validacion Query)");
                        getErrors().put(ai_rowNumber + "-" + as_fieldName, "[" + aobj_data.toString() + "] -> " + as_descErr);
                    } else {
                        lb_Ret = true;
                        if (lrs_Rules.getRows()[0].get("DATA") == null) {
                            iobj_newValue = aobj_data;
                            im_newValues.put(as_fieldName, aobj_data);
                        } else {
                            iobj_newValue = lrs_Rules.getRows()[0].get("DATA").toString();
                            im_newValues.put(as_fieldName, lrs_Rules.getRows()[0].get("DATA").toString());
                        }
                    }
                } /* Modificado el 18/07/2011 por Victor Salinas
                 * Se agrega la logica para considerar una regla de asignacion de valores, sin validar nada
                 */ else if (lrs_Rules.getRows()[0].get("SETDATA") != null) {
                    lb_Ret = true;
                    iobj_newValue = lrs_Rules.getRows()[0].get("SETDATA").toString();
                    im_newValues.put(as_fieldName, lrs_Rules.getRows()[0].get("SETDATA").toString());
                }
            } else {
                lb_Ret = false;
                iobj_newValue = aobj_data;
            }
        } catch (Exception e) {
            e.printStackTrace();
            getErrors().put(ai_rowNumber + "-" + as_fieldName, "[" + aobj_data.toString() + "] -> " + "Validacion Query: " + e.toString());
            lb_Ret = false;
        } finally {
            lit_key = null;
            lobj_key = null;
            lrs_Rules = null;
            lsb_sentence = null;
        }
        return lb_Ret;
    }

    public Vector getDiasInhabiles() {
        Vector lv_diasInhabiles = new Vector();
        Result lrs_Dias = null;
        SortedMap[] lsm_Dias = null;
        int li_Row;
        try {
//            isql_ConnBean.setSqlValue(isql_ConnBean.getSqlSentence("GET_DIAS_INHABILES_CAPWIN", "DSB_CFG_ADOC_QUERY"));
//            lrs_Dias = isql_ConnBean.executeQuery();
            if (lrs_Dias != null) {
                lsm_Dias = lrs_Dias.getRows();
                for (li_Row = 0; li_Row < lsm_Dias.length; li_Row++) {
                    lv_diasInhabiles.add(lsm_Dias[li_Row].get("FECHA").toString());
                }
            } else {
                lv_diasInhabiles.add("2000-01-01");
            }
        } catch (Exception e) {
            e.printStackTrace();
            getErrors().put("0-Ontiene Días Inhábiles", "Error java: " + e.toString());
        } finally {
            lrs_Dias = null;
            lsm_Dias = null;
            li_Row = 0;
        }
        return lv_diasInhabiles;
    }

    public String getFechaSistema() {
        String ls_fechaSistema = "";
        Result lrs_Fecha = null;
        SortedMap[] lsm_Fecha = null;
        try {
//            isql_ConnBean.setSqlValue(isql_ConnBean.getSqlSentence("GET_FECHA_SISTEMA", "DSB_CFG_ADOC_QUERY"));
//            lrs_Fecha = isql_ConnBean.executeQuery();
            if (lrs_Fecha != null) {
                lsm_Fecha = lrs_Fecha.getRows();
                ls_fechaSistema = lsm_Fecha[0].get("FECHA_SISTEMA").toString();
            } else {
                ls_fechaSistema = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            }
        } catch (Exception e) {
            e.printStackTrace();
            getErrors().put("0-Ontiene Fecha Sistema", "Error java: " + e.toString());
        } finally {
            lrs_Fecha = null;
            lsm_Fecha = null;
        }
        return ls_fechaSistema;
    }

    private Connection getConnection() {
        return iconn_Connection;
    }

    private void setConnection(Connection iconn_Connection) {
        this.iconn_Connection = iconn_Connection;
    }

//    public SqlConnectionBean getSqlConnBean() {
//        return isql_ConnBean;
//    }

//    public void setSqlConnBean(SqlConnectionBean isql_ConnBean) {
//        this.isql_ConnBean = isql_ConnBean;
//    }

    /**
     * @return Retorna el objeto map con todos los errores generados en la
     * validacion del los datos del bean
     */
    public Map<String, String> getErrors() {
        return im_fieldErrors;
    }

    /**
     * Asigna un objeto Map que contiene los errores generados por la validacion
     * de los datos del Bean
     *
     * @param fieldErrors
     */
    private void setErrors(Map<String, String> fieldErrors) {
        this.im_fieldErrors = fieldErrors;
    }

    /**
     * @return Retorna el modelo de la clase a la que pertenece el bean de datos
     */
    private Class getbeanClass() {
        return icls_beanClass;
    }

    /**
     * Asigna el modelo de clase a la que pertenece el bean de datos
     *
     * @param icls_beanClass Clase del bean
     */
    private void setbeanClass(Class icls_beanClass) {
        this.icls_beanClass = icls_beanClass;
    }

    public Object getIobj_newValue() {
        return iobj_newValue;
    }

    private void setIobj_newValue(Object iobj_newValue) {
        this.iobj_newValue = iobj_newValue;
    }

    private Map<String, Object> getIm_newValues() {
        return im_newValues;
    }

    private void setIm_newValues(Map<String, Object> im_newValues) {
        this.im_newValues = im_newValues;
    }


}
