package project.spring.repositories;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

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

    

    public int insert(Comment newComment) {
        return db.update("insert into comments (ratingStar, content, accountId, producId)" + "value(?,?,?,?)",
                new Object[] { newComment.getRatingStar(), newComment.getContent(), newComment.getAccountId(), newComment.getProductId() });
    }
}
