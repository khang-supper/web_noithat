package project.spring.repositories;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import project.spring.model.OrderDetail;


public class OrderDetailRepository {
    private static OrderDetailRepository _instance = null;
    private JdbcTemplate db;

    public OrderDetailRepository() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl(
                "jdbc:mysql://localhost:3306/project_sem4?verifyServerCertificate=false&useSSL=false&requireSSL=false");
        dataSource.setUsername("sem4");
        dataSource.setPassword("sem4");
        db = new JdbcTemplate(dataSource);
    }

    public static OrderDetailRepository Instance() {
        if (_instance == null) {
            _instance = new OrderDetailRepository();
        }
        return _instance;
    }

    class OrderDetailRowMapper implements RowMapper<OrderDetail> {
        @Override
        public OrderDetail mapRow(ResultSet rs, int rowNum) throws SQLException {
            OrderDetail item = new OrderDetail();
            item.setId(rs.getInt(OrderDetail.ID));
            item.setPrice(rs.getDouble(OrderDetail.PRICE));
            item.setQuantity(rs.getInt(OrderDetail.QUANTITY));
            item.setTotalPrice(rs.getDouble(OrderDetail.TOTALPRICE));
            item.setOrderId(rs.getInt(OrderDetail.ORDREID));
            item.setProductId(rs.getInt(OrderDetail.PRODUCTID));
            return item;
        }
    }

    public List<OrderDetail> findAll() {
        return db.query("select * from order_details order by id desc", new OrderDetailRowMapper());
    }

    public OrderDetail findById(int id) {
        return db.queryForObject("select * from order_details where Id=?", new OrderDetailRowMapper(), new Object[] { id });
    }

    public List<OrderDetail> findByOrderId(int orderId){
        return db.query("SELECT * FROM order_details where orderId = ?", new OrderDetailRowMapper(), new Object[] { orderId });
    }

    public int deleteOrderDetails(int orderId) {
        return db.update("delete from order_details where orderId=?", new Object[] { orderId });
    }

    public int insertDetail(OrderDetail newOrder) {
        return db.update(
                "insert into order_details (`orderId`, `productId`, `price`, `quantity`, `totalPrice`) values (?, ?, ?, ?, ?)",
                new Object[] { newOrder.getOrderId(), newOrder.getProductId(), newOrder.getPrice(), newOrder.getQuantity(), newOrder.getTotalPrice()});
    }

   

  

    
}
