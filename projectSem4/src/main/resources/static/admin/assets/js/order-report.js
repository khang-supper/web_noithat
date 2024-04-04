$(function () {
  $(".datepicker").datepicker({
    language: "es",
    autoclose: true,
    format: "dd/mm/yyyy",
  });

  // Lấy danh sách order từ controller
  var orders = /*[[${order}]]*/ [];
  //console.log(orders);

  var labels = []; // Mảng chứa ngày trong khoảng thời gian được chọn
  var data = []; // Mảng chứa số lượng đơn hàng tương ứng với mỗi ngày
  var revenueData = []; //Mảng chứa doanh thu tương ứng với mỗi ngày
  var totalOrders = 0; //Tổng số đơn hàng
  var totalRevenue = 0; //Tổng số Doanh thu

  // Cập nhật tổng số lượng đơn hàng, doanh thu vào thẻ <span id="totalOrders"></span>
  document.getElementById("totalOrders").innerText = totalOrders;
  document.getElementById("totalRevenue").innerText = totalRevenue;

  // Tính toán số lượng đơn hàng theo yêu cầu khi vừa mới load trang
  var ordersData = calculateOrdersLast7Days(orders);

  // Mặc định hiển thị thống kê trong 7 ngày qua
  var today = new Date();
  for (var i = 6; i >= 0; i--) {
    var date = new Date(today);
    date.setDate(today.getDate() - i);
    labels.push(formatDate(date));
    data.push(ordersData[i]);
  }
  var startDate = new Date(today);
  startDate.setDate(today.getDate() - 6); // 7 days ago
  var endDate = new Date();
  // Đặt giá trị cho startDate và endDate ban đầu
  $("#startDate").datepicker("setDate", startDate);
  $("#endDate").datepicker("setDate", endDate);

  // Khởi tạo biểu đồ
  var ctx1 = document.getElementById("lineChart").getContext("2d");
  var ctx2 = document.getElementById("lineChart2").getContext("2d");
  var myLineChart1 = createLineChart(ctx1, labels, data, "Số lượng đơn hàng", "#1d7af3", "#1d7af3");
  var myLineChart2 = createLineChart(ctx2, labels, revenueData, "Doanh thu", "#e83e8c", "#e83e8c");

  // Xử lý sự kiện khi thay đổi chọn Bộ lọc
  $("#squareSelect").change(function () {
    var option = $(this).val();
    if (option === "7days") {
      var endDate = new Date();
      var startDate = new Date();
      startDate.setDate(startDate.getDate() - 6); // 7 days ago
      $("#startDate").datepicker("setDate", startDate);
      $("#endDate").datepicker("setDate", endDate);
    } else if (option === "thisMonth") {
      var endDate = new Date();
      var startDate = new Date(
        endDate.getFullYear(),
        endDate.getMonth(),
        1
      ); // First day of current month
      $("#startDate").datepicker("setDate", startDate);
      $("#endDate").datepicker("setDate", endDate);
    } else if (option === "lastMonth") {
      var endDate = new Date();
      endDate.setDate(0); // Last day of last month
      var startDate = new Date(
        endDate.getFullYear(),
        endDate.getMonth(),
        1
      ); // First day of last month
      $("#startDate").datepicker("setDate", startDate);
      $("#endDate").datepicker("setDate", endDate);
    } else if (option === "custom") {
      $("#startDate").datepicker("setDate", "");
      $("#endDate").datepicker("setDate", "");
    } else {
      // Handle other options
    }
  });
  // Xử lý sự kiện khi gửi form chọn ngày tùy chỉnh
  $("#dateRangeForm").submit(function (e) {
    e.preventDefault(); // Ngăn chặn việc gửi form

    // Lấy giá trị ngày bắt đầu và ngày kết thúc
    var startDate = $("#startDate").val();
    var endDate = $("#endDate").val();

    // Tính toán số lượng đơn hàng trong khoảng thời gian tùy chỉnh
    var customOrder = calculateDataCustomRange(
      orders,
      startDate,
      endDate
    );

    // Cập nhật labels và data cho biểu đồ
    myLineChart1.data.labels = customOrder.labels;
    myLineChart2.data.labels = customOrder.labels;
    myLineChart1.data.datasets[0].data = customOrder.ordersData;
    myLineChart2.data.datasets[0].data = customOrder.revenueData;
    myLineChart1.update();
    myLineChart2.update();
  });
});

// Hàm tạo biểu đồ
function createLineChart(ctx, labels, data, label, borderColor, pointColor) {
  return new Chart(ctx, {
    type: "line",
    data: {
      labels: labels,
      datasets: [
        {
          label: label,
          borderColor: borderColor,
          pointBorderColor: "#FFF",
          pointBackgroundColor: pointColor,
          pointBorderWidth: 2,
          pointHoverRadius: 4,
          pointHoverBorderWidth: 1,
          pointRadius: 4,
          backgroundColor: "transparent",
          fill: true,
          borderWidth: 2,
          data: data,
        },
      ],
    },
    options: {
      responsive: true,
      maintainAspectRatio: false,
      legend: {
        position: "bottom",
        labels: {
          padding: 10,
          fontColor: borderColor,
        },
      },
      tooltips: {
        bodySpacing: 4,
        mode: "nearest",
        intersect: 0,
        position: "nearest",
        xPadding: 10,
        yPadding: 10,
        caretPadding: 10,
      },
      layout: {
        padding: { left: 15, right: 15, top: 15, bottom: 15 },
      },
    },
  });
}

// Chuyển đổi định dạng ngày từ yyyy-mm-dd thành dd/mm/yyyy
function formatDate(dateString) {
  var date = new Date(dateString);
  var day = date.getDate();
  var month = date.getMonth() + 1;
  var year = date.getFullYear();
  return day + "/" + month + "/" + year;
}

// Hàm tính số lượng đơn hàng trong 7 ngày qua
function calculateOrdersLast7Days(orders) {
  var ordersLast7Days = {};
  var today = new Date();
  for (var i = 0; i < 7; i++) {
    var date = new Date(today);
    date.setDate(today.getDate() - i);
    ordersLast7Days[formatDate(date)] = 0; // Khởi tạo số đơn hàng của mỗi ngày là 0
  }
  orders.forEach(function (order) {
    var orderDate = formatDate(order.orderDate);
    if (orderDate in ordersLast7Days) {
      ordersLast7Days[orderDate]++;
    }
  });
  //Tổng số đơn hàng trong 7 ngày
  totalOrders = Object.values(ordersLast7Days).reduce(
    (acc, number) => acc + number,
    0
  );
  document.getElementById("totalOrders").innerText = totalOrders;

  return Object.values(ordersLast7Days);
}

// Hàm tính số lượng đơn hàng trong khoảng thời gian tùy chỉnh theo ngày
function calculateDataCustomRange(orders, startDate, endDate) {
var ordersCustomRange = {};
var revenueCustomRange = {};

// Chuyển đổi định dạng ngày tháng từ dd/mm/yyyy sang mm/dd/yyyy
startDate = convertDateFormat(startDate);
endDate = convertDateFormat(endDate);
var start = new Date(startDate);
var end = new Date(endDate);

// Lặp qua các đơn hàng và tính số lượng đơn hàng và tổng doanh thu từng ngày trong khoảng thời gian
orders.forEach(function (order) {
  var orderDate = new Date(order.orderDate);
  if (orderDate >= start && orderDate <= end) {
    var formattedDate = formatDate(orderDate);
    if (!(formattedDate in ordersCustomRange)) {
      ordersCustomRange[formattedDate] = 0;
      revenueCustomRange[formattedDate] = 0;
    }
    ordersCustomRange[formattedDate]++;
    revenueCustomRange[formattedDate] += order.total; // Tính tổng doanh thu
  }
});

// Tạo labels và data cho số lượng đơn hàng và doanh thu từ kết quả tính toán
var labels = [];
var ordersData = [];
var revenueData = [];
var currentDate = new Date(start);
while (currentDate <= end) {
  var formattedDate = formatDate(currentDate);
  labels.push(formattedDate);
  ordersData.push(ordersCustomRange[formattedDate] || 0);
  revenueData.push(revenueCustomRange[formattedDate] || 0);
  currentDate.setDate(currentDate.getDate() + 1); // Tăng ngày lên 1 để di chuyển đến ngày tiếp theo
}
totalOrders = ordersData.reduce((acc, number) => acc + number, 0 );
totalRevenue = revenueData.reduce((acc, number) => acc + number, 0 );
//console.log(totalOrders);
document.getElementById("totalOrders").innerText = totalOrders;
document.getElementById("totalRevenue").innerText = totalRevenue;

return {
  labels: labels,
  ordersData: ordersData,
  revenueData: revenueData,
};
}

// Hàm chuyển đổi định dạng ngày tháng từ dd/mm/yyyy sang mm/dd/yyyy
function convertDateFormat(dateString) {
  var parts = dateString.split("/");
  return parts[1] + "/" + parts[0] + "/" + parts[2];
}
