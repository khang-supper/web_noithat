package project.spring.repositories;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import project.spring.model.Order;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

public class OrderRepository {
    private static OrderRepository _instance = null;
    private JdbcTemplate db;

    public OrderRepository() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl(
                "jdbc:mysql://localhost:3306/project_sem4?verifyServerCertificate=false&useSSL=false&requireSSL=false");
        dataSource.setUsername("sem4");
        dataSource.setPassword("sem4");
        db = new JdbcTemplate(dataSource);
    }

    public static OrderRepository Instance() {
        if (_instance == null) {
            _instance = new OrderRepository();
        }
        return _instance;
    }

    class OrderRowMapper implements RowMapper<Order> {
        @Override
        public Order mapRow(ResultSet rs, int rowNum) throws SQLException {
            Order item = new Order();
            item.setId(rs.getInt(Order.ID));
            item.setCode(rs.getString(Order.CODE));
            item.setOrderDate(rs.getDate(Order.ORDERDATE));
            item.setCustomerName(rs.getString(Order.CUSTOMERNAME));
            item.setShippingAddress(rs.getString(Order.SHIPPINGADDRESS));
            item.setShippingPhone(rs.getString(Order.SHIPPINGPHONE));
            item.setTotal(rs.getInt(Order.TOTAL));
            item.setStatus(rs.getString(Order.STATUS));
            item.setAccountId(rs.getInt(Order.ACCOUNTID));
            item.setPaymentStatus(rs.getBoolean(Order.PAYMENTSTATUS));
            item.setPaymentGateway(rs.getString(Order.PAYMENTGATEWAY));
            return item;
        }
    }

    public List<Order> findAll() {
        return db.query("select * from orders order by id desc", new OrderRowMapper());
    }

    @SuppressWarnings("deprecation")
    public List<Order> findAllByPage(int page, int pageSize) { // lấy phần tử trong 1 trang
        int start = page * pageSize;
        return db.query("SELECT * FROM orders LIMIT ?, ?", new Object[] { start, pageSize }, new OrderRowMapper());
    }

    public int getTotalPages(int pageSize) { // tổng số trang
        int totalCount = db.queryForObject("SELECT COUNT(*) FROM orders", Integer.class);
        return (int) Math.ceil((double) totalCount / pageSize);
    }

    public Order findById(int id) {
        return db.queryForObject("select * from orders where Id=?", new OrderRowMapper(), new Object[] { id });
    }

    @SuppressWarnings("deprecation")
    public List<Order> search(String keyword) {
        String sql = "SELECT * FROM orders WHERE Name LIKE ?";
        String searchKeyword = "%" + keyword + "%"; // Thêm ký tự % để tìm kiếm một phần của tên
        return db.query(sql, new Object[] { searchKeyword }, new OrderRowMapper());
    }

    public int deleteById(int id) {
        return db.update("delete from orders where Id=?", new Object[] { id });
    }

    public int insert(Order newOrder) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
    
        db.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    "insert into orders (`code`, `accountId`, `orderDate`, `customerName`, `shippingAddress`, `shippingPhone`, `total`, `status`, `paymentStatus`, `paymentGateway`) values (?, ?, NOW(), ?, ?, ?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, newOrder.getCode());
            ps.setInt(2, newOrder.getAccountId());
            ps.setString(3, newOrder.getCustomerName());
            ps.setString(4, newOrder.getShippingAddress());
            ps.setString(5, newOrder.getShippingPhone());
            ps.setDouble(6, newOrder.getTotal());
            ps.setString(7, newOrder.getStatus());
            ps.setBoolean(8, newOrder.getPaymentStatus());
            ps.setString(9, newOrder.getPaymentGateway());
            return ps;
        }, keyHolder);
    
        return keyHolder.getKey().intValue();
    }
    

    public int update(Order upOrder) {
        return db.update(
                "update orders set shippingAddress = ?, shippingPhone = ?, total = ?, status = ?, paymentStatus = ?, paymentGateway = ? where Id = ?",
                new Object[] { upOrder.getShippingAddress(), upOrder.getShippingPhone(), upOrder.getTotal(),
                        upOrder.getStatus(), upOrder.getPaymentStatus(), upOrder.getPaymentGateway(),
                        upOrder.getId() });
    }

    public int updateShipmentDetails(Order upOrder) {
        return db.update(
                "update orders set shippingAddress = ?, shippingPhone = ?, customerName = ? where Id = ?",
                new Object[] { upOrder.getShippingAddress(), upOrder.getShippingPhone(), upOrder.getCustomerName(), upOrder.getId() });
    }

    public int updateStatus(Order upOrder) {
        return db.update(
                "update orders set status = ? where Id = ?", new Object[] {  upOrder.getStatus(), upOrder.getId() });
    }

    public int updatePaymentOrder(int orderId, String paymentGateway) {
        return db.update(
                "update orders set paymentGateway = ?, paymentStatus = true where Id = ?", new Object[] {  paymentGateway, orderId });
    }

    //lấy ra các order + các sản phẩm thuộc order
    public List<Map<String, Object>> getAllOrdersWithProducts() {
        String query = "SELECT o.*, GROUP_CONCAT(p.name SEPARATOR ',') AS product_names " +
                       "FROM orders o " +
                       "JOIN order_details od ON o.id = od.orderId " +
                       "JOIN products p ON od.productId = p.id " +
                       "GROUP BY o.id";
        return db.queryForList(query);
    }
    
    //Lấy ra đơn hàng theo account
    public List<Map<String, Object>> getOrdersAccount(String username){
        String query =  "SELECT o.*, GROUP_CONCAT(p.name SEPARATOR ',') AS product_names " +
                        "FROM orders o " +
                        "JOIN order_details od ON o.id = od.orderId " +
                        "JOIN products p ON od.productId = p.id " +
                        "JOIN accounts a ON o.accountId = a.id " +
                        "WHERE a.username = ? " +
                        "GROUP BY o.id Order by id desc";
                return db.queryForList(query, username);
    }
    //lấy ra chi tiết order, tên mã sản phẩm, ảnh, kích thước trog chi tiết đó
    public List<Map<String, Object>> getProuctWithOrderDetail(int id){
        String query = "SELECT " +
                            "od.id AS order_detail_id, " +
                            "od.orderId, " +
                            "od.productId, " +
                            "od.price, " +
                            "od.quantity, " +
                            "od.totalPrice, " +
                            "p.name AS product_name, " +
                            "p.code AS product_code, " +
                            "p.size AS product_size, " +
                            "CASE WHEN ip.status = 1 THEN i.path ELSE NULL END AS product_image " +
                        "FROM " +
                            "order_details od " +
                        "JOIN " +
                            "products p ON od.productId = p.id " +
                        "LEFT JOIN " +
                            "image_products ip ON p.id = ip.productId AND ip.status = 1 " +
                        "LEFT JOIN " +
                            "images i ON ip.imageId = i.id " +
                        "WHERE " +
                            "od.orderId = ?";
    
        return db.queryForList(query, id);
    }

    // Tính toán số đơn hàng hàng ngày trong tuần
    public int getDailyOrderCount(LocalDate date) {
        String sql = "SELECT COUNT(*) FROM orders WHERE DATE(orderDate) = ?";
        return db.queryForObject(sql, Integer.class, date);
    }
    // Tính toán số đơn hàng hàng ngày trong tháng này
    public int getTotalOrdersThisMonth(LocalDate date) {
        LocalDate firstDayOfMonth = date.withDayOfMonth(1);
        LocalDate lastDayOfMonth = date.withDayOfMonth(date.lengthOfMonth());
        String sql = "SELECT COUNT(*) FROM orders WHERE orderDate BETWEEN ? AND ?";
        return db.queryForObject(sql, Integer.class, firstDayOfMonth, lastDayOfMonth);
    }   
    // Tính toán số đơn hàng hàng ngày trong tháng trước
    public int getTotalOrdersLastMonth(LocalDate date) {
        LocalDate firstDayOfLastMonth = date.minusMonths(1).withDayOfMonth(1);
        LocalDate lastDayOfLastMonth = date.minusMonths(1).withDayOfMonth(date.minusMonths(1).lengthOfMonth());
        String sql = "SELECT COUNT(*) FROM orders WHERE orderDate BETWEEN ? AND ?";
        return db.queryForObject(sql, Integer.class, firstDayOfLastMonth, lastDayOfLastMonth);
    }
    // Tính toán số đơn hàng hàng ngày theo tùy chọn
    public int getTotalOrdersByDateRange(LocalDate startDate, LocalDate endDate) {
        String sql = "SELECT COUNT(*) FROM orders WHERE orderDate BETWEEN ? AND ?";
        return db.queryForObject(sql, Integer.class, startDate, endDate);
    }
    
}
