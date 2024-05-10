package project.spring.repositories;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import project.spring.model.Discount;

public final class DiscountRepository {
    private static DiscountRepository _instance = null;
    private JdbcTemplate db;

    public DiscountRepository() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl(
                "jdbc:mysql://localhost:3306/project_sem4?verifyServerCertificate=false&useSSL=false&requireSSL=false");
        dataSource.setUsername("sem4");
        dataSource.setPassword("sem4");
        db = new JdbcTemplate(dataSource);
    }

    public static DiscountRepository Instance() {
        if (_instance == null) {
            _instance = new DiscountRepository();
        }
        return _instance;
    }

    class DiscountRowMapper implements RowMapper<Discount> {
        @Override
        public Discount mapRow(ResultSet rs, int rowNum) throws SQLException {
            Discount item = new Discount();
            item.setId(rs.getInt(Discount.ID));
            item.setName(rs.getString(Discount.NAME));
            item.setPercent(rs.getDouble(Discount.PERCENT));
            item.setStatus(rs.getBoolean(Discount.STATUS));
            return item;
        }
    }

    public List<Discount> findAll() {
        return db.query("select * from discounts order by id desc", new DiscountRowMapper());
    }

    public Discount findById(int id) {
        return db.queryForObject("select * from discounts where Id=?", new DiscountRowMapper(), new Object[] { id });
    
    }

    public int deleteById(int id) {
    return db.update("delete from discounts where Id=?", new Object[] { id });
    }

    public int insert(Discount newDiscount) {
        return db.update("insert into discounts (name, percent, status)" + "value(?,?,?)",
                new Object[] { newDiscount.getName(), newDiscount.getPercent(), newDiscount.getStatus() });
    }

    public int update(Discount upDiscount) {
        return db.update("update discounts set name = ?, percent = ?, status = ? where Id = ?",
                new Object[] { upDiscount.getName(), upDiscount.getPercent(), upDiscount.getStatus(),
                        upDiscount.getId() });
    }

    public int updateDiscountId(int discountId, int productId) {
        return db.update("update products set discountId = ? where Id = ?",
                new Object[] {discountId, productId });
    }

    public boolean isProductAddedToDiscount(int discountId, int productId) {
        try {
            Integer result = db.queryForObject("SELECT COUNT(*) FROM products WHERE discountId = ? AND id = ?",
                    Integer.class, discountId, productId);
            return result != null && result > 0;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }


}