package project.spring.repositories;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import project.spring.model.ContactMessage;

public class ContactMessageRepository {
	private static ContactMessageRepository _instance = null;
    private JdbcTemplate db;

    public ContactMessageRepository() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl(        
                "jdbc:mysql://localhost:3306/project_sem4?verifyServerCertificate=false&useSSL=false&requireSSL=false");
        dataSource.setUsername("sem4");
        dataSource.setPassword("sem4");
        db = new JdbcTemplate(dataSource);
    }

    public static ContactMessageRepository Instance() {
        if (_instance == null) {
            _instance = new ContactMessageRepository();
        }
        return _instance;
    }

    class ContactMessageRowMapper implements RowMapper<ContactMessage> {
        @Override
        public ContactMessage mapRow(ResultSet rs, int rowNum) throws SQLException {
            ContactMessage item = new ContactMessage();
            item.setId(rs.getInt(ContactMessage.ID));
            item.setName(rs.getString(ContactMessage.NAME));
            item.setEmail(rs.getString(ContactMessage.EMAIL));
            item.setPhone(rs.getInt(ContactMessage.PHONE));
            item.setMessage(rs.getString(ContactMessage.MESSAGE));
            return item;
        }
    }

    public List<ContactMessage> findAll() {
        return db.query("select * from contact_messages order by id desc", new ContactMessageRowMapper());
    }


    public int insert(ContactMessage newContactMessage) {
        return db.update("insert into contact_messages (name, email, phone, message)" + "value(?,?,?,?)",
                new Object[] { newContactMessage.getName(), newContactMessage.getEmail(), newContactMessage.getPhone(), newContactMessage.getMessage() });
    }

}
