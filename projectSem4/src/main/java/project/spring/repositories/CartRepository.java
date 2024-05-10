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

     @SuppressWarnings("deprecation")
     public List<Cart> findAll(int accountId){
        return db.query("Select * from carts where accountId =? ",new Object[]{accountId}, new CartRowMapper());
    }

    // public List<Map<String, Object>> find2(int accountId) {
    //     String query = "SELECT i.path AS image, " +
    //                    "p.name, " +
    //                    "p.id AS productId, " +
    //                    "p.price, " +
    //                    "c.accountId, " +
    //                    "c.quantity, " +
    //                    "(p.price * c.quantity) AS total " + 
    //                    "FROM carts c " +
    //                    "JOIN products p ON c.productId = p.id " +
    //                    "JOIN image_products ip ON p.id = ip.productId AND ip.status = 1 " +
    //                    "JOIN images i ON ip.imageId = i.id " +
    //                    "WHERE c.accountId = ? order by c.id desc";
    //     return db.queryForList(query, accountId);
    // }

    public List<Map<String, Object>> find(int accountId) {
        String query = "SELECT i.path AS image, " +
                       "p.name, " +
                       "p.id AS productId, " +
                       "p.price, " +
                       "c.accountId, " +
                       "c.quantity, " +
                       "(CASE WHEN d.percent IS NOT NULL THEN (p.price * (1 - d.percent / 100)) ELSE p.price END) AS discountPrice, " +
                       "d.percent AS percent, " +
                       "(p.price * c.quantity) AS total, " + 
                       "(CASE WHEN d.percent IS NOT NULL THEN (p.price * (1 - d.percent / 100)) ELSE p.price END * c.quantity) AS totalDiscount " + 
                       "FROM carts c " +
                       "JOIN products p ON c.productId = p.id " +
                       "JOIN image_products ip ON p.id = ip.productId AND ip.status = 1 " +
                       "JOIN images i ON ip.imageId = i.id " +
                       "LEFT JOIN discounts d ON p.discountId = d.id AND d.status = 1 " +
                       "WHERE c.accountId = ? ORDER BY c.id DESC";
        return db.queryForList(query, accountId);
    }
    
    public double getTotal2(int accountId) {
        String query = "SELECT SUM(p.price * c.quantity) AS total " +
                       "FROM carts c " +
                       "JOIN products p ON c.productId = p.id " +
                       "WHERE c.accountId = ?";
        return db.queryForObject(query, Double.class, accountId);
    }

    public double getTotal(int accountId) {
        String query = "SELECT SUM((CASE WHEN d.percent IS NOT NULL THEN (p.price * (1 - d.percent / 100)) ELSE p.price END) * c.quantity) AS total " +
                       "FROM carts c " +
                       "JOIN products p ON c.productId = p.id " +
                       "LEFT JOIN discounts d ON p.discountId = d.id AND d.status = 1 " +
                       "WHERE c.accountId = ?";
        return db.queryForObject(query, Double.class, accountId);
    }
    
    
    public int deleteById(int id) {
        return db.update("delete from carts where Id=?", new Object[] { id });
    }

    public int deleteByAccountId(int accountId) {
        return db.update("delete from carts where accountId=?", new Object[] { accountId });
    }
    

    public int insert(Cart newCart) {
        return db.update("insert into carts (accountId, productId, quantity) " + "value(?,?,?)",
                new Object[] {newCart.getAccountId(), newCart.getProductId(), newCart.getQuantity() });
    }

    public int update(Cart cart) {
        if (cart.getQuantity() == 0) {
            return db.update("delete from carts where Id=?", new Object[] { cart.getId() });
        } else {
            return db.update("update carts set quantity = ? where Id = ?",
                new Object[] { cart.getQuantity(), cart.getId() });
        }
    }

    @SuppressWarnings("deprecation")
    public Cart findCartByProductIdAndAccountId(int productId, int accountId) {
        String query = "SELECT * FROM carts WHERE productId = ? AND accountId = ?";
        List<Cart> carts = db.query(query, new Object[]{productId, accountId}, new CartRowMapper());
        return carts.isEmpty() ? null : carts.get(0);
    }
    
    public int getCartItemCount(int accountId) {
        String query = "SELECT SUM(quantity) FROM carts WHERE accountId = ?";
        return db.queryForObject(query, Integer.class, accountId);
    }
    
}
