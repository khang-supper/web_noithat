package project.spring.repositories;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

// import org.springframework.jdbc.support.GeneratedKeyHolder;
// import org.springframework.jdbc.support.KeyHolder;
// import java.sql.PreparedStatement;
// import java.sql.Statement;

import project.spring.model.Category;

public final class CategoryRepository {
    private static CategoryRepository _instance = null;
    private JdbcTemplate db;

    public CategoryRepository() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl(
                "jdbc:mysql://localhost:3306/project_sem4?verifyServerCertificate=false&useSSL=false&requireSSL=false");
        dataSource.setUsername("sem4");
        dataSource.setPassword("sem4");
        db = new JdbcTemplate(dataSource);
    }

    public static CategoryRepository Instance() {
        if (_instance == null) {
            _instance = new CategoryRepository();
        }
        return _instance;
    }

    class CategoryRowMapper implements RowMapper<Category> {
        @Override
        public Category mapRow(ResultSet rs, int rowNum) throws SQLException {
            Category item = new Category();
            item.setId(rs.getInt(Category.ID));
            item.setName(rs.getString(Category.NAME));
            item.setImage(rs.getString(Category.IMAGE));
            item.setPath(rs.getString(Category.PATH));
            item.setIsDelete(rs.getBoolean(Category.ISDELETE));
            return item;
        }
    }

    public List<Category> findAll() {
        return db.query("select * from categories order by id desc", new CategoryRowMapper());
    }

    @SuppressWarnings("deprecation")
    public List<Category> findAllByPage(int page, int pageSize) { // lấy phần tử trong 1 trang
        int start = page * pageSize;
        return db.query("SELECT * FROM categories LIMIT ?, ? ", new Object[] { start, pageSize },
                new CategoryRowMapper());
    }

    public int getTotalPages(int pageSize) { // tổng số trang
        int totalCount = db.queryForObject("SELECT COUNT(*) FROM categories", Integer.class);
        return (int) Math.ceil((double) totalCount / pageSize);
    }

    public Category findById(int id) {
        return db.queryForObject("select * from categories where Id=?", new CategoryRowMapper(), new Object[] { id });
    
    }
    public Category findByPath(String path) {
        return db.queryForObject("select * from categories where path=?", new CategoryRowMapper(), new Object[] { path });
    }

    public int deleteById(int id) {
        return db.update("update categories set IsDelete = true where Id = ?", new Object[] { id });
    }
    // public int deleteById(int id) {
    // return db.update("delete from categories where Id=?", new Object[] { id });
    // }

    public int insert(Category newCategory) {
        return db.update("insert into categories (Name, Path, Image, IsDelete)" + "value(?,?,?,?)",
                new Object[] { newCategory.getName(), newCategory.getPath(), newCategory.getImage(), newCategory.getIsDelete() });
    }

    // public int insert(Category newCategory) { //Thêm mới và gắn id vào path
    //     // Thêm dữ liệu vào cơ sở dữ liệu và lấy ID của bản ghi mới
    //     KeyHolder keyHolder = new GeneratedKeyHolder();
    //     db.update(connection -> {
    //         PreparedStatement ps = connection.prepareStatement(
    //             "INSERT INTO categories (Name, Image, IsDelete) VALUES (?, ?, ?)",
    //             Statement.RETURN_GENERATED_KEYS
    //         );
    //         ps.setString(1, newCategory.getName());
    //         ps.setString(2, newCategory.getImage());
    //         ps.setBoolean(3, newCategory.getIsDelete());
    //         return ps;
    //     }, keyHolder);
    
    //     // Lấy ID của bản ghi mới
    //     int newId = keyHolder.getKey().intValue();
    
    //     // Tạo path mới bằng cách kết hợp path ban đầu với ID của bản ghi mới
    //     String newPath = newCategory.getPath() + "-" + newId;
    
    //     // Cập nhật path của bản ghi mới
    //     updatePath(newId, newPath);
    
    //     return newId;
    // }
    

    public void updatePath(int id, String newPath) {
        db.update("update categories set path = ? where id = ?", newPath, id);
    }

    public int update(Category upCategory) {
        return db.update("update categories set Name = ?, Path = ?, Image = ? where Id = ?",
                new Object[] { upCategory.getName(), upCategory.getPath(), upCategory.getImage(),
                        upCategory.getId() });
    }

}