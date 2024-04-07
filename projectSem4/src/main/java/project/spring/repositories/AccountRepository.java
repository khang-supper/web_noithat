package project.spring.repositories;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import project.spring.model.Account;

public class AccountRepository {
    private static AccountRepository _instance = null;
    private JdbcTemplate db;

    public AccountRepository() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl(
                "jdbc:mysql://localhost:3306/project_sem4?verifyServerCertificate=false&useSSL=false&requireSSL=false");
        dataSource.setUsername("sem4");
        dataSource.setPassword("sem4");
        db = new JdbcTemplate(dataSource);
    }

    public static AccountRepository Instance() {
        if (_instance == null) {
            _instance = new AccountRepository();
        }
        return _instance;
    }

    class AccountRowMapper implements RowMapper<Account> {
        @Override
        public Account mapRow(ResultSet rs, int rowNum) throws SQLException {
            Account item = new Account();
            item.setId(rs.getInt("id"));
            item.setusername(rs.getString("username"));
            item.setPassword(rs.getString("password"));
            item.setFullName(rs.getString("fullName"));
            item.setEmail(rs.getString("email"));
            item.setPhone(rs.getInt("phone"));
            item.setAddress(rs.getString("address"));
            item.setAvatar(rs.getString("avatar"));
            item.setStatus(rs.getBoolean("status"));
            item.setRole(rs.getBoolean("role"));
            return item;
        }
    }

    public List<Account> findAll() {
        return db.query("select * from accounts order by id desc", new AccountRowMapper());
    }

    public Account findById(int id) {
        return db.queryForObject("select * from accounts where Id=?", new AccountRowMapper(), new Object[] { id });
    }

}
