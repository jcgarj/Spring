package mx.com.aion.data.enums;

public enum CfgQueries {QUERY_ROUTER("GET_PE_CML_OPER_ROUTER"),
						QUERY_ROUTER_OF_SYSTEM("GET_PE_CML_OPER_ROUTER_OF_SYSTEM"),
						QUERY_ROUTER_BY_ID("GET_PE_CML_OPER_ROUTER_BY_ID"),
						QUERY_CLOB("GET_DSB_CFG_CLOB_QUERY"),
						QUERY_PARAMS_GRAL("GET_DSB_CFG_PARAMS_GRAL"),
						QUERY_PARAMS_GRAL_IPAB("GET_DSB_CFG_PARAMS_GRAL_IPAB"),
						QUERY_CHOICE("GET_PE_CML_OPER_CHOICE"),
						UPDATE_PARAMS_GRAL("DO_UPDATE_DSB_CFG_PARAMS_GRAL"),
						GET_SCC_LOAD_TYPE_BY_PARAMS_ID("GET_SCC_LOAD_TYPE_BY_PARAMS_ID"),
						GET_SCC_LOAD_TYPE_BY_PARAMS("GET_SCC_LOAD_TYPE_BY_PARAMS"),
						GET_SCC_LOAD_TYPE_BY_PARAMS2("GET_SCC_LOAD_TYPE_BY_PARAMS2"),
						GET_SCC_LOAD_TYPE_BY_PARAMS3("GET_SCC_LOAD_TYPE_BY_PARAMS3"),
						GET_SCC_LOAD_MAP_BY_ID("GET_SCC_LOAD_MAP_BY_ID"),
						GET_SCC_LOAD_MAP_BANK_STAT_BY_ID("GET_SCC_LOAD_MAP_BANK_STAT_BY_ID"),
						GET_SCC_MOV_BANK_STAT_BY_ID("GET_SCC_MOV_BANK_STAT_BY_ID"),
						GET_SCC_CFG_DETAIL_LOAD_BY_ID("GET_SCC_CFG_DETAIL_LOAD_BY_ID"),
						GET_SCC_CFG_DETAIL_LOAD_BY_TYPE_ID("GET_SCC_CFG_DETAIL_LOAD_BY_TYPE_ID"),
						DO_INSERT_SCC_MOV_LEDGER("DO_INSERT_SCC_MOV_LEDGER"),
						DO_UPDATE_SCC_LOAD_LOG("DO_UPDATE_SCC_LOAD_LOG"),
						QUERY_ROUTER_BY_TYPE("GET_PE_CML_OPER_ROUTER_BY_TYPE"),

						GET_SCC_EXEC_RECONCILIATION("GET_SCC_EXEC_RECONCILIATION"),
						QUERY_UPDATE_CAT_SYSTEM("UPDATE_CAT_SYSTEM"),
						QUERY_CAT_SYSTEM("GET_CAT_SYSTEM"),
						QUERY_CAT_SYSTEM_BY_ID("GET_CAT_SYSTEM_BY_ID"),
						QUERY_CTRL_PROCESS_VALIDATION("GET_CTRL_PROCESS_VALIDATION"),
						QUERY_PROCESS_ID_BY_ROUTER_ID("GET_PROCESS_ID_BY_ROUTER_ID"),
						QUERY_SUBPROCESS_ID_BY_ROUTER_ID("GET_SUBPROCESS_ID_BY_ROUTER_ID"),
						QUERY_SUBPROCESS_REPROCESSING_VALUE("GET_SUBPROCESS_REPROCESSING_VALUE"),
						QUERY_TRANSACTION_DATE_SIBAMEX("GET_TRANSACTION_DATE_SIBAMEX"),
						INSERT_CML_CONTROL_PROCESS("INSERT_CML_CONTROL_PROCESS"),
						QUERY_CANCEL_INTRAHELD("GET_INTRAHELD_TO_CANCEL"),
						QUERY_OUT_PEN_OPERATIONS("GET_OUT_PEN_OPERATIONS"),
						QUERY_PE_CML_PROCESS("GET_PE_CML_PROCESS"),
						QUERY_GET_NEXT_EXEC_ID("GET_NEXT_EXEC_ID"),
						INSERT_CTRL_EXEC("INSERT_CTRL_EXEC"),
						GET_INFO_PIXEL_POINT("QUERY_INFO_PIXEL_POINT"),
						GET_INFO_SANTANDER("QUERY_INFO_SANTANDER"),
						QUERY_GET_SCC_CAT_CALENDAR("GET_SCC_CAT_CALENDAR"),
						UPDATE_CHECKED_ACCT("UPDATE_CHECKED_ACCT"),
						GET_FALSE_CHECKED_ACCT("GET_FALSE_CHECKED_ACCT"),
						GET_VALIDA_DIA("GET_VALIDA_DIA"),

						//OG
						QUERY_GET_SCC_LOAD_TYPE("GET_SCC_LOAD_TYPE"),
						QUERY_GET_SCC_LOAD_LOG("GET_SCC_LOAD_LOG"),
						QUERY_GET_SCC_CAT_CONCIL_TYPE("GET_SCC_CAT_CONCIL_TYPE"), 
						
						//CONEXION A BD
						GET_SCC_CFG_PROPERTIES_CONEXION("GET_SCC_CFG_PROPERTIES_CONEXION"), 						
						GET_INFO_WIGOS_TARJETA("GET_INFO_WIGOS_TARJETA"), 
						GET_INFO_WIGOS_BOVEDA("GET_INFO_WIGOS_BOVEDA"),
						GET_SCC_CAT_BRANCH("GET_SCC_CAT_BRANCH"),
						QUERY_GET_SCC_LOAD_TYPE_BD("GET_SCC_LOAD_TYPE_BD"),
						QUERY_GET_SCC_CFG_ACC_DETAIL_BY_ID("GET_SCC_CFG_ACC_DETAIL_BY_ID"),
						QUERY_GET_SCC_CFG_LOAD_BY_ID("GET_SCC_CFG_LOAD_BY_ID"),
						UPDATE_DATE_PROCESS_LOAD("UPDATE_DATE_PROCESS_LOAD"),
						QUERY_GET_SCC_FINAL_NUMBER_ALL("GET_SCC_FINAL_NUMBER_ALL")
						;

	private String value;
	
	private CfgQueries(String value) {
		this.value = value;
	}
	
	public String getValue(){
		return value;
	}

}