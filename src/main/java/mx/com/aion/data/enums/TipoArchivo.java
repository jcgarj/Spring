package mx.com.aion.data.enums;

public enum TipoArchivo {
						 PPNT("pixelpoint","PPOINT"),
						 BNTE("banorte","BNTE"),
						 STDR("santander","STDR"),
						 WIGOST("WigosTarjeta","WIGOST"),
						 WIGOSB("WigosBoveda","WIGOSB"),
	                     ;

	private String value;
	private String fileID;
	
	private TipoArchivo(String value, String fileID) {
		this.value = value;
		this.fileID = fileID;
	}
	
	public String getValue(){
		return value;
	}
	
	public String getFileID(){
		return fileID;
	}

}
