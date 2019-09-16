$(function(){
	$("#publishBtn").click(publish);
});

function publish() {

	var title = $('#recipient-name').val();
	var content = $('#message-text').val();

	$.post(
		CONTENT_PATH + "/post/create",
		{
			"title": title,
			"content": content
		},
		function (data) {
		    data = $.parseJSON(data);

			$("#publishModal").modal("hide");

			$("#hintBody").text(data.msg);
			$("#hintModal").modal("show");
			setTimeout(function(){
				$("#hintModal").modal("hide");
				if (data.code == 200) window.location.reload();
			}, 2000);
		}
	)

}