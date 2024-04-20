package project.spring.repositories;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import project.spring.model.Cart;

public class CartRepository {
    private static CartRepository _instance = null;
    private JdbcTemplate db;

    public CartRepository() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl(
                "jdbc:mysql://localhost:3306/project_sem4?verifyServerCertificate=false&useSSL=false&requireSSL=false");
        dataSource.setUsername("sem4");
        dataSource.setPassword("sem4");
        db = new JdbcTemplate(dataSource);
    }

    public static CartRepository Instance() {
        if (_instance == null) {
            _instance = new CartRepository();
        }
        return _instance;
    }

    class CartRowMapper implements RowMapper<Cart> {
        @Override
        public Cart mapRow(ResultSet rs, int rowNum) throws SQLException {
            Cart item = new Cart();
            item.setId(rs.getInt(Cart.ID));
            item.setAccountId(rs.getInt(Cart.ACCOUNTID));
            item.setProductId(rs.getInt(Cart.PRODUCTID));
            item.setQuantity(rs.getInt(Cart.QUANTITY));
            return item;
        }
    }

    public List<Map<String, Object>> find(int accountId) {
        String query = "SELECT i.path AS image_path, " +
                       "p.name AS product_name," +
                       "p.id AS product_id," +
                       "p.price AS product_price," +
                       "c.accountId AS user_id," +
                       "c.quantity," +
                       "FROM carts c " +
                       "JOIN products p ON c.productId = p.id " +
                       "JOIN image_products ip ON p.id = ip.productId AND ip.status = 1 " +
                       "JOIN images i ON ip.imageId = i.id " +
                       "WHERE c.accountId = ?";
        return db.queryForList(query, accountId);
    }

    public int deleteById(int id) {
        return db.update("delete from carts where Id=?", new Object[] { id });
    }

    public int insert(Cart newCart) {
        return db.update("insert into carts (id, accountId, productId, quantity) " + "value(?,?,?,?)",
                new Object[] { newCart.getId(), newCart.getAccountId(), newCart.getProductId(), newCart.getQuantity() });
    }

    public int update(Cart cart) {
        if (cart.getQuantity() == 0) {
            return db.update("delete from carts where Id=?", new Object[] { cart.getId() });
        } else {
            return db.update("update carts set quantity = ? where Id = ?",
                new Object[] { cart.getQuantity(), cart.getId() });
        }
    }
    
}
