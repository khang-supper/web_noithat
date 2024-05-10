package project.spring.repositories;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import project.spring.model.Comment;

public class CommentRepository {
	private static CommentRepository _instance = null;
    private JdbcTemplate db;

    public CommentRepository() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl(        
                "jdbc:mysql://localhost:3306/project_sem4?verifyServerCertificate=false&useSSL=false&requireSSL=false");
        dataSource.setUsername("sem4");
        dataSource.setPassword("sem4");
        db = new JdbcTemplate(dataSource);
    }

    public static CommentRepository Instance() {
        if (_instance == null) {
            _instance = new CommentRepository();
        }
        return _instance;
    }

    class CommentRowMapper implements RowMapper<Comment> {
        @Override
        public Comment mapRow(ResultSet rs, int rowNum) throws SQLException {
        	Comment item = new Comment();
            item.setId(rs.getInt(Comment.ID));
            item.setRatingStar(rs.getInt(Comment.RATINGSTAR));
            item.setContent(rs.getString(Comment.CONTENT));
            item.setAccountId(rs.getInt(Comment.ACCOUNTID));
            item.setProductId(rs.getInt(Comment.PRODUCTID));
            return item;
        }
    }

    public List<Comment> findAll() {
        return db.query("select * from comments order by id desc", new CommentRowMapper());
    }

    public List<Map<String, Object>> findByProduct(int productId) {
        String sql =   "SELECT c.*, a.fullName " +
                        "FROM comments c " +
                        "INNER JOIN accounts a ON c.accountId = a.id " +
                        "WHERE c.productId = ? " +
                        "ORDER BY c.id DESC";
                        
        return db.queryForList(sql, new Object[] { productId });
    }

    public double avgStart(int productId) {
        String sql = "SELECT ROUND(AVG(ratingStar), 1) AS avgStar FROM comments WHERE productId = ?";
        List<Double> results = db.queryForList(sql, Double.class, productId);
        if (results.isEmpty()) {
            return 0.0; 
        }
        return results.get(0);
    }
    

    public int insert(Comment newComment) {
        return db.update("insert into comments (ratingStar, content, accountId, productId) values (?,?,?,?)",
                new Object[] { newComment.getRatingStar(), newComment.getContent(), newComment.getAccountId(), newComment.getProductId() });
    }

    //kiểm tra xem người dùng có mua sản phẩm không
    public boolean hasPurchasedProduct(int accountId, int productId) {
        // Viết truy vấn SQL để kiểm tra xem người dùng có mua sản phẩm không
        String sql = "SELECT COUNT(*) FROM orders o " +
                     "JOIN order_details od ON o.id = od.orderId " +
                     "WHERE o.accountId = ? AND od.productId = ?";
        
        // Thực hiện truy vấn và kiểm tra kết quả
        int count = db.queryForObject(sql, Integer.class, accountId, productId);
        
        // Trả về true nếu người dùng đã mua sản phẩm, ngược lại trả về false
        return count > 0;
    }

    //kiểm tra xem người dùng đã đánh giá sản phẩm này chưa
    public boolean hasReviewedProduct(int accountId, int productId) {
        String sql = "SELECT COUNT(*) FROM comments WHERE accountId = ? AND productId = ?";
        int count = db.queryForObject(sql, Integer.class, accountId, productId);
        return count > 0;
    }
    
    
    
}
