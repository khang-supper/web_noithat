// Xử lý sự kiện khi click vào nút chỉnh sửa
$('.edit-btn').on('click', function() {
    var id = $(this).attr('data-category-id'); // Lấy id từ thuộc tính th:data-category-id
    window.location.href = '/admin/categories/edit?id=' + id; // Chuyển hướng đến trang chỉnh sửa với id tương ứng
});

// Xử lý sự kiện khi click vào nút xóa
$('.delete-btn').on('click', function() {
    var id = $(this).attr('data-category-id'); // Lấy id từ thuộc tính th:data-category-id
    var name = $(this).closest('tr').find('td:nth-child(2)').text(); // Lấy tên loại sản phẩm từ cột thứ 2 của hàng
    $('#deleteModal').modal('show'); // Hiển thị modal xác nhận xóa
    $('#deleteModal .modal-body').text('Bạn có chắc muốn xóa "' + name + '"?'); // Đặt nội dung modal
    // Xác nhận xóa
    $('#deleteModal .btn-danger').on('click', function() { 
        window.location.href = '/admin/categories/delete/' + id; // Gửi yêu cầu xóa với id tương ứng
    });
});
