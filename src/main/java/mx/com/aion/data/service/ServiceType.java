package mx.com.aion.data.service;

import com.google.gson.Gson;
import mx.com.aion.data.models.entity.EtGetCatalog;
import mx.com.aion.data.models.entity.EtToken;
import org.json.JSONObject;

public class ServiceType {

    public Object obtainPojoByServiceName(String serviceName, JSONObject json) {
        switch (serviceName){
            case "etGetCatalog":
                return obtainPojoEtGetCatalog(json);
            case "etToken":
                return obtainPojoEtToken(json);
        }
        return "No existe el servicio";
    }

    private EtGetCatalog obtainPojoEtGetCatalog(JSONObject json){
        return new Gson().fromJson(json.toString(), EtGetCatalog.class);
    }

    private EtToken obtainPojoEtToken(JSONObject json){
        return new Gson().fromJson(json.toString(), EtToken.class);
    }

}
