/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mx.com.aion.data.service;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.HashMap;

/**
 * Clase que permite la manipulacion de Java Beans utilizando la tecnologia
 * de introspeccion contenida en el core de Java para poder realizar las siguientes tareas:
 * <i>agregar metodos</i>
 * <i>modificar metodos</i>
 * <i>obtener descripcion de los metodos</i>
 * <i>conversion de metodos a objeto Map</i>
 *
 * @author Victor Salinas
 */
public class BeanManipulation {

    /**
     * Convierte un objeto Bean a un objeto HashMap donde el key son los nombres
     * de los metods contenidos dentro del Bean, y los value son los valores
     * de cada metodo
     *
     * @param acls_beanType Class del objeto bean que se desea convertir
     * @param aobj_beanData Objeto bean que se desea convertir
     * @return Objeto HashMap como los metodos y valores del Bean
     */
    public static HashMap<String, String> convertBeantoMap(Class acls_beanType, Object aobj_beanData) {
        HashMap<String, String> lhm_Method = new HashMap<String, String>();
        BeanInfo lbean_info = null;
        try {
            lbean_info = Introspector.getBeanInfo(acls_beanType, Object.class);
            for (PropertyDescriptor lprtpd : lbean_info.getPropertyDescriptors()) {
                try {
                    lhm_Method.put(lprtpd.getName(), lprtpd.getReadMethod().invoke(aobj_beanData).toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lbean_info = null;
        }
        return lhm_Method;
    }

    /**
     * Obtiene el valor de un metodo definido dentro de un Bean
     *
     * @param acls_beanType Class del objeto bean
     * @param aobj_beanData Objeto bean
     * @param as_method     metodo del cual se desea obtener un valor
     * @return Objeto con el valor que retorna el metodo del Bean, null si no encuentra el metodo
     */
    public static Object getDataFromBeanMethod(Class acls_beanType, Object aobj_beanData, String as_method) {
        BeanInfo lbean_info = null;
        Object lobj_Data = null;
        try {
            lbean_info = Introspector.getBeanInfo(acls_beanType, Object.class);
            for (PropertyDescriptor lprtpd : lbean_info.getPropertyDescriptors()) {
                if (lprtpd.getName().equals(as_method)) {
                    lobj_Data = lprtpd.getReadMethod().invoke(aobj_beanData);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lbean_info = null;
        }
        return lobj_Data;
    }

    /**
     * asigna un valor a una propiedad de un Bean atraves del metodo Set
     *
     * @param acls_beanType Class del objeto bean
     * @param aobj_beanData Objeto bean
     * @param as_method     metodo al cual se le va a asignar un valor
     * @param acls_objValue es la clase del objeto que se desea asignar
     * @param aobj_value    valor que se desea asignar al metodo del bean
     * @return Verdadero si pudo hacer las asignacion del valor, falso en caso contrario
     */
    public static boolean setDataToBeanMethod(Class acls_beanType, Object aobj_beanData, String as_method, Object aobj_value) {
        BeanInfo lbean_info = null;
        boolean lb_Ret = false;
        try {
            lbean_info = Introspector.getBeanInfo(acls_beanType, Object.class);
            for (PropertyDescriptor lprtpd : lbean_info.getPropertyDescriptors()) {
                if (lprtpd.getName().equals(as_method)) {

                    if (lprtpd.getReadMethod().getReturnType().equals(Integer.TYPE) || lprtpd.getReadMethod().getReturnType().isInstance(java.lang.Integer.class)) {
                        if (aobj_value == null) {
                            aobj_value = "0";
                        }
                        lprtpd.getWriteMethod().invoke(aobj_beanData, Integer.parseInt(aobj_value.toString()));
                    } else {
                        lprtpd.getWriteMethod().invoke(aobj_beanData, aobj_value);
                    }
                    lb_Ret = true;
                    break;
                }
            }
        } catch (Exception e) {
            //e.printStackTrace();
        } finally {
            lbean_info = null;
        }
        return lb_Ret;
    }

}
