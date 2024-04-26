package project.spring.repositories;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import project.spring.model.Account;

@Service
public class AccountRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    class AccountRowMapper implements RowMapper<Account> {
        @Override
        public Account mapRow(ResultSet rs, int rowNum) throws SQLException {
            Account item = new Account();
            item.setId(rs.getInt(Account.ID));
            item.setusername(rs.getString(Account.USERNAME));
            item.setPassword(rs.getString(Account.PASSWORD));
            item.setEmail(rs.getString(Account.EMAIL));
            item.setPhone(rs.getInt(Account.PHONE));
            item.setAddress(rs.getString(Account.ADDRESS));
            item.setFullName(rs.getString(Account.FULLNAME));
            item.setAvatar(rs.getString(Account.AVATAR));
            item.setStatus(rs.getBoolean(Account.STATUS));
            item.setRole(rs.getBoolean(Account.ROLE));
            
            return item;
        }
    }

    public List<Account> findAll() {
        return jdbcTemplate.query("select * from accounts where role = 0 ", new AccountRowMapper());
    }

    public List<Account> findAllrole() {
        return jdbcTemplate.query("select * from accounts where role = 1 ", new AccountRowMapper());
    }

    @SuppressWarnings("deprecation")
    public List<Account> findAllByPage(int page, int pageSize) {
        int start = page * pageSize;
        return jdbcTemplate.query("SELECT * FROM accounts LIMIT ?, ? ", new Object[] { start, pageSize }, new AccountRowMapper());
    }


    public int getTotalPages(int pageSize) {
        int totalCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM accounts", Integer.class);
        return (int) Math.ceil((double) totalCount / pageSize);
    }

    public Account findById(int id) {
        return jdbcTemplate.queryForObject("select * from accounts where Id=?", new AccountRowMapper(), new Object[] { id });
    }

    public int insert(Account newAccount) {
    return jdbcTemplate.update("INSERT INTO accounts (Username, Password, Email, FullName, Phone,Address, Avatar, Role) VALUES (?,?, ?, ?, ?, ?, ?, ?)",
            new Object[] { newAccount.getUsername(), newAccount.getPassword(), newAccount.getEmail(), newAccount.getFullName(), newAccount.getPhone(),newAccount.getAddress(), newAccount.getAvatar(), newAccount.getRole() });
    }

    public int update(Account upPro) {
        return jdbcTemplate.update("UPDATE accounts SET Username = ?, Email = ?, FullName = ?, Phone = ?,Address=?, Role = ? WHERE Id = ?",
                new Object[] { upPro.getUsername(), upPro.getEmail(), upPro.getFullName(), upPro.getPhone(),upPro.getAddress(), upPro.getRole(), upPro.getId() });
    }

    public int deleteById(int id) {
        return jdbcTemplate.update("UPDATE accounts SET Status = false WHERE Id = ?", new Object[] { id });
    }

    public Account findByUserName(String username) {
        return jdbcTemplate.queryForObject("select * from accounts where username=?", new AccountRowMapper(), new Object[] { username });
    }

    public List<Account> findAccountsByUsername(String username) {
        return jdbcTemplate.query("select * from accounts where username=?", new AccountRowMapper(), new Object[] { username });
    }
    
    public int updateUser(Account upPro) {
        return jdbcTemplate.update("UPDATE accounts SET  Email = ?, FullName = ?, Phone = ?,Address=?, Role = ? WHERE Username = ?",
                new Object[] { upPro.getUsername(), upPro.getEmail(), upPro.getFullName(), upPro.getPhone(),upPro.getAddress(), upPro.getRole(), upPro.getId() });
    }

}
