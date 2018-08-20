$(document).ready(function(){

    $("#loginButton").prop("disabled", false);
    $("#registerButton").prop("disabled", false);
    $("#changePasswordButton").prop("disabled", true);
    $("#logoutButton").prop("disabled", true);
    $("#calendar").hide();
    $("#calendarModal").hide();
    $("#alertSuccess").hide();
    $("#alertError").hide();   
    
    // LogoutButton
    
    
    // Change password
    $("input[type=password]").keyup(function(){
    	console.log('keyup');
	    var ucase = new RegExp("[A-Z]+");
		var lcase = new RegExp("[a-z]+");
		var num = new RegExp("[0-9]+");
		var save = true;
		
		if($("#inputPassword1").val().length >= 8){
			$("#8char").removeClass("glyphicon-remove");
			$("#8char").addClass("glyphicon-ok");
			$("#8char").css("color","#00A41E");
		}else{
			save = false;
			$("#8char").removeClass("glyphicon-ok");
			$("#8char").addClass("glyphicon-remove");
			$("#8char").css("color","#FF0004");
		}
		
		if(ucase.test($("#inputPassword1").val())){
			$("#ucase").removeClass("glyphicon-remove");
			$("#ucase").addClass("glyphicon-ok");
			$("#ucase").css("color","#00A41E");
		}else{
			save = false;
			$("#ucase").removeClass("glyphicon-ok");
			$("#ucase").addClass("glyphicon-remove");
			$("#ucase").css("color","#FF0004");
		}
		
		if(lcase.test($("#inputPassword1").val())){
			$("#lcase").removeClass("glyphicon-remove");
			$("#lcase").addClass("glyphicon-ok");
			$("#lcase").css("color","#00A41E");
		}else{
			save = false;
			$("#lcase").removeClass("glyphicon-ok");
			$("#lcase").addClass("glyphicon-remove");
			$("#lcase").css("color","#FF0004");
		}
		
		if(num.test($("#inputPassword1").val())){
			$("#num").removeClass("glyphicon-remove");
			$("#num").addClass("glyphicon-ok");
			$("#num").css("color","#00A41E");
		}else{
			save = false;
			$("#num").removeClass("glyphicon-ok");
			$("#num").addClass("glyphicon-remove");
			$("#num").css("color","#FF0004");
		}
		
		if($("#inputPassword1").val() == $("#inputPassword2").val() && $("#inputPassword1").val() && $("#inputPassword2").val()){
			$("#pwmatch").removeClass("glyphicon-remove");
			$("#pwmatch").addClass("glyphicon-ok");
			$("#pwmatch").css("color","#00A41E");
		}else{
			save = false;
			$("#pwmatch").removeClass("glyphicon-ok");
			$("#pwmatch").addClass("glyphicon-remove");
			$("#pwmatch").css("color","#FF0004");
		}
		
	    $("#changePasswordSubmitButton").prop("disabled", !save);
    })
    
    // LoginSubmitButton
    $('#loginSubmitButton').click(function () {
    	console.log('Login...');
    	var data = {};
    	data['username'] = $('#inputUsername').val();
    	data['password'] = $('#inputPassword').val();
        $.ajax({
            type: 'POST',
            url: '/TimeTracker/register/login',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(data) ,
            dataType: 'json',
            success: function (result) {
                console.log('LoggedIn');
                $("#loginButton").prop("disabled", true);
                $("#registerButton").prop("disabled", true);
                $("#changePasswordButton").prop("disabled", false);
                $("#logoutButton").prop("disabled", false);
                $('#loginModal').modal('hide');
                $("#calendar").show();
                var events = _.map(result.user.events, function(event) { 
                    return _.extend({}, event, {allDay: false }); // lodash
                });
                console.log('Events', events);
                $('#calendar').fullCalendar('renderEvents', events, true);
                alertSuccess("Lietotājs " + result.user.username + " ir ienācis.");
            },
            error: function (x, e) {
                console.log('Login: error', x, e);
                $('#loginModal').modal('hide');
                if (x.status === 400 && x.responseJSON.msg === "empty") {
                    alertError("Lietotājs un parole jābūt aizpildītiem.");
                } else {
                	alertError("Kļūda: " + x.status);
                }
            }
        })
    })
    
    // RegisterSubmitbutton
    $('#registerSubmitButton').click(function () {
    	console.log('Register...');
    	var data = {};
    	data['username'] = $('#inputNewUsername').val();
    	data['password'] = $('#inputNewPassword').val();
        $.ajax({
            type: 'POST',
            url: '/TimeTracker/register/new',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(data) ,
            dataType: 'json',
            success: function (result) {
                console.log('Registered', result);
                $("#loginButton").prop("disabled", false);
                $("#registerButton").prop("disabled", true);
                $("#changePasswordButton").prop("disabled", true);
                $("#logoutButton").prop("disabled", true);
                $('#registerModal').modal('hide');
                alertSuccess("Lietotājs " + result.user.username + " ir izveidots.");
            },
            error: function (x, e) {
                console.log('Register: error', x, e);
                $('#registerModal').modal('hide');
                if (x.status === 400 && x.responseJSON.msg === "empty") {
                    alertError("Lietotājs un parole jābūt aizpildītiem.");
                } else if (x.status === 400 && x.responseJSON.msg === "exists") {
                    alertError("Lietotājs ar tādu lietotāja vārdu jau eksistē.");
                } else {
                	alertError("Kļūda: " + x.status);
                }
            }
        })
    })
    
    function alertSuccess(msg) {
    	$("#alertSuccess").text(msg);
    	$("#alertSuccess").fadeTo(2000, 500).slideUp(500, function(){
    	    $("#alertSuccess").slideUp(500);
    	});
    }
    
    function alertError(msg) {
    	$("#alertError").text(msg);
    	$("#alertError").fadeTo(2000, 500).slideUp(500, function(){
    	    $("#alertError").slideUp(500);
    	});
    }
    
    // Calendar
    $('#datetimepickerStart').datetimepicker({
        format: 'DD-MM-YYYY HH:mm:ss'
    });
    $('#datetimepickerEnd').datetimepicker({
        format: 'DD-MM-YYYY HH:mm:ss'
    });
    $('#calendar').fullCalendar({
    	header: {
            left: 'prev,next today',
            center: 'title',
            right: 'month,basicWeek,basicDay'
    	  },
          defaultDate: moment().format('YYYY-MM-DD'),
          selectable: true,
          editable: false,
          eventLimit: true, // allow "more" link when too many events
	      select: function(startDate, endDate) { // neesošs events
			  $('#calendarModal').modal('show');
			  $("#calendarDeleteButton").prop("disabled", true);
	          //alert('selected ' + startDate.format() + ' to ' + endDate.format());
	      },
	      eventClick: function (calEvent, jsEvent, view) { // esošs events
			  $('#calendarModal').modal('show');
			  $("#calendarDeleteButton").prop("disabled", false);
              //alert('You clicked on event id: ' + calEvent.id + "\nSpecial ID: " + calEvent.someKey + "\nAnd the title is: " + calEvent.title);

          }

	});

});