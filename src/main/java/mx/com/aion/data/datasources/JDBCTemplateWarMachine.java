package mx.com.aion.data.datasources;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Component;

@Component
@Qualifier("warmachine")
public class JDBCTemplateWarMachine  implements TipoJDBCTemplate {

    @Override
    public JdbcTemplate getDS() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://192.168.2.39:3306/Automation?useSSL=false");
        dataSource.setUsername("TestAuto");
        dataSource.setPassword("Automation#1");
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.setDataSource(dataSource);
        return jdbcTemplate;
    }
}
