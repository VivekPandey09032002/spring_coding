$(function () {
  var myModal = new bootstrap.Modal(
    document.getElementById("attendance-alert"),
    {
      keyboard: true,
    }
  );

  $(".dismiss").click(() => {
    myModal.hide();
  });

  Webcam.set({
    width: 360,
    height: 270,
    image_format: "jpeg",
    jpeg_quality: 100,
  });
  Webcam.attach("#webcam");
  $("#btnCapture").click(function () {
    Webcam.snap(function (data_uri) {
      $("#imgCapture")[0].src = data_uri;
      $("#btnUpload").removeAttr("disabled");
    });
  });
  $("#btnUpload").click(function () {

    $("#btnUpload").html(
      `<div class="spinner-border text-info" role="status" ></div>`
    );
    const image = $("#imgCapture")[0].src;
    const email = $("#email").val();
    if (email === undefined || email.length == 0) {
      $("#btnUpload").text("in-time")
      myModal.show();
      $(".modal-title").text("cannot take attendance");
      $(".modal-body > p").text("email cannot be empty");
      return;
    }
    const basicJson = { data: image, email: email };
    console.log(basicJson.email);
    const stringJson = JSON.stringify(basicJson);
    $.ajax({
      type: "POST",
      url: "http://10.0.61.27:8080/attendance/in-time",
      contentType: "application/json",
      data: JSON.stringify(basicJson),
      dataType: "json",
      success: function (data) {
        $("#btnUpload").text("in-time")
        console.log(data);
        myModal.show();
        $(".modal-title").text("attendance in-time register successfully");
        $(".modal-body > p").text(data.message);
      },
      error: function (data) {
        $("#btnUpload").text("in-time")
        console.log(data);
        myModal.show();
        $(".modal-title").text("attendance in-time register failed");
        $(".modal-body > p").text(JSON.parse(data.responseText).message);
        JSON.parse(data.responseText).errors.forEach((data) =>
          $(".modal-body > p").append(`<br> ${data}`)
        );
      },
    });
  });

  $("#btnOutTime").click(function () {
    $("#btnOutTime").html(
      `<div class="spinner-border text-info" role="status" ></div>`
    );
    const email = $("#email").val();
    if (email === undefined || email.length == 0) {
      $("#btnOutTime").text("out-time")
      myModal.show();
      $(".modal-title").text("cannot take attendance");
      $(".modal-body > p").text("email cannot be empty");
      return;
    }
    $.ajax({
      type: "GET",
      url: "http://10.0.61.27:8080/attendance/out-time/" + email,
      success: function (data) {
        $("#btnOutTime").text("out-time")
        myModal.show();
        $(".modal-title").text("attendance out-time register successfully");
        $(".modal-body > p").text(data.message);
      },
      error: function (data) {
        $("#btnOutTime").text("out-time")
        console.log(data);
        myModal.show();
        $(".modal-title").text("attendance out-time register failed");
        $(".modal-body > p").text(JSON.parse(data.responseText).message);
        JSON.parse(data.responseText).errors.forEach((data) =>
          $(".modal-body > p").append(`<br> ${data}`)
        );
      },
    });
  });
});
