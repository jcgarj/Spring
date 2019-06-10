package mx.com.aion.data.datasources;

import org.springframework.jdbc.core.JdbcTemplate;

public interface TipoJDBCTemplate {
    JdbcTemplate getDS();
}