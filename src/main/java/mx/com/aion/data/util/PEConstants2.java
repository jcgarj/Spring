package mx.com.aion.data.util;

/**
 * @author Slopez
 * @version 1.0 Slopez    Clase con catálogo de errores controlados
 * 			1.1 Agonzalez Integracion de mensajes para notificaciones.
 *
 */

import java.util.HashMap;
import java.util.Map;

public class PEConstants2 {
	
	
	public static final Map<Integer, String> codigosError;
	
	public static final String EXCHANGE_PROPERTY_GRUPO_CAMEL = "appCamel";
	public static final String EXCHANGE_PROPERTY_ESTATUS = "status";
	public static final String EXCHANGE_PROPERTY_USUARIO = "usuario";
	public static final String EXCHANGE_PROPERTY_ERROR_CODE = "codigoError";
	public static final String EXCHANGE_PROPERTY_ERROR_DESC = "descError";
	public static final String EXCHANGE_PROPERTY_ROUTER_ID = "routerId";

	public static final String EXCHANGE_PROPERTY_EJECUTA_SUBPROCESO = "subprocess";
	public static final String EXCHANGE_PROPERTY_EXEC_ID = "execId";
	
	public static final Integer SUBPROCESS_OPER_CONCILIACION = 1;
	public static final Integer SUBPROCESS_OPER_EDO_CUENTA = 2;
	public static final Integer SUBPROCESS_OPER_MATRIZ = 3;
	public static final Integer SUBPROCESS_OPER_PASE_HISTOR = 4;

	public static final String EXCHANGE_PROPERTY_FECHA_EDO_CTA = "EDO_CTA";
	public static final String PROCESS_BEGIN = "Iniciando ejecucion de proceso";
	public static final String PROCESS_OK = "Proceso ejecutado correctamente";
	public static final String PROCESS_ERR = "Ha ocurrido un error al ejecutar el proceso, revise con su administrador";
	
	public static final String EXCHANGE_PROPERTY_RECORDS_TO_INSERT = "RECORDS_TO_INSERT";

	public static final String EXCHANGE_PROPERTY_FILE_NAME_WIGOST = null;
	public static final String EXCHANGE_PROPERTY_FILE_PARENT_WIGOST = null;
	
	public static final String EXCHANGE_PROPERTY_FILE_NAME_WIGOSB = null;
	public static final String EXCHANGE_PROPERTY_FILE_PARENT_WIGOSB = null;

	public static final String EXCHANGE_PROPERTY_URL_SECURITY_WS = "URL_SECURITY_WS";
	public static final String EXCHANGE_PROPERTY_DEFAULT_USER_NOTIFY = "DEFAULT_USER_NOTIFY";

	public static final String EXCHANGE_PROPERTY_FILES_NOTIFY = "filesNotify";
	public static final String EXCHANGE_PROPERTY_CONCIL_NOTIFY = "concilNotify";
	
	public static final String EXCHANGE_PROPERTY_FECHA_SMARTCONCIL = "SMARTCONCIL";
	public static final String EXCHANGE_PROPERTY_FECHA_PROCESO_CARGA = "FechaProcesoCarga";
	
	static
    {
		codigosError = new HashMap<Integer, String>();
		
		//Errores controlados
		codigosError.put(-1, "Se presentó un problema al consultar los ruteos");
		codigosError.put(-2, "Se presentó un problema al consultar las posibilidades de seleccion (choice)");
		codigosError.put(-3, "La respuesta esta vacía");
		codigosError.put(-4, "Se presentó un problema al parsear la respuesta");
		codigosError.put(-7, "Se presentó un problema al consultar la tabla de queries");
		codigosError.put(-8, "Se presentó un problema al consultar la tabla del pago correspondiente");
		codigosError.put(-9, "Se presentó un problema en la ejecución de PL");
		codigosError.put(-10, "Error al obtener query de la tabla DSB_CFG_CLOB_QUERY");
		codigosError.put(-11, "Se presentó un problema al obtener valor de la tabla DSB_CFG_PARAMS_GRAL");
		codigosError.put(-21, "El ruteo es de tipo Sistema");
		codigosError.put(-22, "No coincide el número de parametros enviados con los que requiere el ruteo");
		codigosError.put(-23, "Se presentó un problema de parseo en los parametros de entrada");
		codigosError.put(-24, "No existe Ruteo");
		codigosError.put(-25, "Se presentó un problema al consultar tabla");
		
		codigosError.put(-26, "No existe proceso para la extensión del archivo");
		codigosError.put(-27, "No existe proceso para la carga");
		codigosError.put(-28, "Se presentó un problema en la consulta de mapeo de archivo");
		codigosError.put(-29, "Se presentó un problema en el calculo del balance");
		
		codigosError.put(-30, "No existe informacion sobre el establecimiento");
		codigosError.put(-31, "No se puede establecer una conexion");
		codigosError.put(-32, "El tipo de archivo no es valido");
		codigosError.put(-33, "El identificador del establecimiento no tiene el formato correcto");
		codigosError.put(-34, "El ID de carga es incorrecto");
		codigosError.put(-35, "La carga se realizo anteriormente y ya fue conciliada");
		codigosError.put(-36, "No se ha configurado la conexion a la Base de Datos correspondiente");
		codigosError.put(-37, "Se presento un problema al eliminar datos de conexion\n");
		codigosError.put(-38, "Se presento un problema al en la generacion de reportes\n");
		codigosError.put(-39, "Se presento un problema al agregar datos de conexion\n");
		codigosError.put(-40, "No se encontró información para la carga automatica de base de datos\n");
		
		codigosError.put(-41, "No se cuenta con información de la carga de archivo\n");
		codigosError.put(-42, "No se cuenta con información del detalle de la cuenta\n");
		codigosError.put(-43, "No se cuenta con fecha de carga\n");
		codigosError.put(-44, "Excepción en la carga de información de base de datos automática\n");
		codigosError.put(-45, "Se obtuvo 0 registros en consulta realizada\n");
		
		
		
		//Errores de conciliacion
		codigosError.put(-50, "Se presentó un problema al actualizar status de conciliacion\n");
		codigosError.put(-51, "Se presentó un problema en la ejecucion de conciliaciones\n");
		codigosError.put(-52, "Se presentó un problema con el proceso de cierre\n");
		//Excepciones no controladas
		
		codigosError.put(-98, "Se presentó un problema en el parseo con JAXB");
		codigosError.put(-99, "Se presentó un problema en el parseo con Unmarshaller");
		codigosError.put(-100, "Error no controlado");
		codigosError.put(-101, "Se presentó un problema al parsear los parametros de entrada");				
    }
	
	public static final Map<Integer, String> paramsNotity;
	static{
		paramsNotity = new HashMap<Integer, String>();
		
		paramsNotity.put(1, "DESCRIPTION");
		paramsNotity.put(2, "EMAIL_LIST_ID");
		paramsNotity.put(3, "EVENT_TYPE");
		paramsNotity.put(4, "ROL_ID");
		paramsNotity.put(5, "SEND_MAIL");
		paramsNotity.put(6, "USER_ID");
	}
}
