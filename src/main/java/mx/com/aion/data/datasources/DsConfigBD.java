package mx.com.aion.data.datasources;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.SQLException;

@Configuration
@Primary
@Component
@ComponentScan("mx.com.aion.data")
public class DsConfigBD {

    @Bean("easytransfer")
    public DataSource dsEasyTransfer(){
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://192.168.2.96:3306/EasyTransfer?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC");
        dataSource.setUsername("EasyDBA");
        dataSource.setPassword("3asy_Transfer");
        try {
            dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println(dataSource);

        return dataSource;
    }

}
