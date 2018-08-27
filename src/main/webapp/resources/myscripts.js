$(document).ready(function(){

    function setupSession() {
        $.ajax({
            type: 'GET',
            url: '/TimeTracker/register/session',
            contentType: 'application/json; charset=utf-8',
            success: function (result) {
                if (result.user) {
                    console.log('Session setup: found session, user already logged in');
                    $('#inputUserId').val(result.user.id);
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
                } else {
                    console.log('Session setup: no session, please login');
                    $('#inputUserId').val(0);
                    $("#loginButton").prop("disabled", false);
                    $("#registerButton").prop("disabled", false);
                    $("#changePasswordButton").prop("disabled", true);
                    $("#logoutButton").prop("disabled", true);
                    $('#calendar').fullCalendar('removeEvents');
                    $("#calendar").hide();
                }
            },
            error: function (x, e) {
                console.log('Session setup: error', x, e);
                if (x.status === 400 && x.responseJSON.msg === "notfound") {
                    console.log('Session setup: no session, please login');
                    $('#inputUserId').val(0);
                    $("#loginButton").prop("disabled", false);
                    $("#registerButton").prop("disabled", false);
                    $("#changePasswordButton").prop("disabled", true);
                    $("#logoutButton").prop("disabled", true);
                    $('#calendar').fullCalendar('removeEvents');
                    $("#calendar").hide();
                } else {
                	alertError("Kļūda: " + x.status);
                }
            }
        });
    }
    
    // Initial setup
    $("#loginButton").prop("disabled", false);
    $("#registerButton").prop("disabled", false);
    $("#changePasswordButton").prop("disabled", true);
    $("#logoutButton").prop("disabled", true);
    $("#calendar").hide();
    $("#calendarModal").hide();
    $("#alertSuccess").hide();
    $("#alertError").hide();   

    // Afterwards setup session
    setupSession();

    // LogoutButton
    $('#logoutButton').click(function () {
    	console.log('Logging out...');
    	var data = {};
        $.ajax({
            type: 'POST',
            url: '/TimeTracker/register/logout',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(data) ,
            dataType: 'json',
            success: function (result) {
                console.log('Logged out');
                $('#inputUserId').val(0);
                $("#loginButton").prop("disabled", false);
                $("#registerButton").prop("disabled", false);
                $("#changePasswordButton").prop("disabled", true);
                $("#logoutButton").prop("disabled", true);
                $('#calendar').fullCalendar('removeEvents');
                $("#calendar").hide();
            },
            error: function (x, e) {
                console.log('Logout: error', x, e);
            	alertError("Kļūda: " + x.status);
            }
        })
    })
    
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
                console.log('LoggedIn', result);
                $('#inputUserId').val(result.user.id);
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
                } else if (x.status === 400 && x.responseJSON.msg === "notfound") {
                    alertError("Lietotājs ar šādu paroli neeksistē.");
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
    
    // ChangePasswordButton
     $('#changePasswordSubmitButton').click(function () {
    	console.log('Updating password...');
    	var data = {};
    	data['id'] = $('#inputUserId').val();
    	data['password'] = $('#inputPassword0').val();
    	data['newPassword'] = $('#inputPassword1').val();
        $.ajax({
            type: 'POST',
            url: '/TimeTracker/register/updatePassword',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(data) ,
            dataType: 'json',
            success: function (result) {
                console.log('Updated password');
                $('#changePasswordModal').modal('hide');
                $("#changePasswordButton").prop("disabled", false);
                alertSuccess("Parole ir nomainīta");
            },
            error: function (x, e) {
                console.log('updatePassword: error', x, e);
                $('#changePasswordModal').modal('hide');
                if (x.status === 400 && x.responseJSON.msg === "empty") {
                    alertError("Parole jābūt aizpildīta");
                } else if (x.status === 400 && x.responseJSON.msg === "wrong") {
                    alertError("Esošā parole neder.");
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
        format: 'DD-MM-YYYY HH:mm:ss',
        timeZone: 'Europe/Riga',
        sideBySide: true
    });
    $('#datetimepickerEnd').datetimepicker({
        format: 'DD-MM-YYYY HH:mm:ss',
        timeZone: 'Europe/Riga',
        sideBySide: true
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
			  $('#calendarModalLabel').text('Pievienot jauno: Izvēlēties laiku');
			  $('#inputEventId').val(0);
			  $('#inputActivity').val('');
			  $('#datetimepickerStart').data("DateTimePicker").date(startDate);
			  $('#datetimepickerEnd').data("DateTimePicker").date(endDate);
	      },
	      eventClick: function (calEvent, jsEvent, view) { // esošs events
			  $('#calendarModal').modal('show');
			  $("#calendarDeleteButton").prop("disabled", false);
			  $('#calendarModalLabel').text('Mainīt esošo: Izvēlēties laiku');
			  $('#inputEventId').val(calEvent.id);
			  $('#inputActivity').val(calEvent.title);
			  $('#datetimepickerStart').data("DateTimePicker").date(calEvent.start);
			  $('#datetimepickerEnd').data("DateTimePicker").date(calEvent.end);
          }

	});
    
    // DeleteCalendarButton
    $('#calendarDeleteButton').click(function () {
    	console.log('Deleting event...', $('#inputEventId').val());
    	var data = {};
    	data['id'] = $('#inputEventId').val();
        $.ajax({
            type: 'POST',
            url: '/TimeTracker/event/delete',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(data) ,
            dataType: 'json',
            success: function (result) {
                console.log('Deleted event', $('#inputEventId').val());
                $('#calendar').fullCalendar('removeEvents', data.id);
                $('#calendarModal').modal('hide');
            },
            error: function (x, e) {
                console.log('Deleting event error', x, e);
                $('#calendarModal').modal('hide');
                if (x.status === 400 && x.responseJSON.msg === "empty") {
                    alertError("Vajag izvēlēties aktivitāti.");
                    //TODO: pielikt validation
                } else if (x.status === 400 && x.responseJSON.msg === "cross") {
                    alertError("Aktivitātes nedrīkst pārklāties.");
                    //TODO: pielikt validation
                } else {
                	alertError("Kļūda: " + x.status);
                }
            }
        })
    })
    
    // UpdateCalendarButton / Create
    $('#calendarSubmitButton').click(function () {
    	var data = {};
    	data['id'] = parseInt($('#inputEventId').val());
    	console.log(data.id === 0 ? 'Creating event...' : 'Updating event...', data.id);
    	data['userId'] = parseInt($('#inputUserId').val());
    	data['title'] = $('#inputActivity').val(); 
    	data['start'] = $('#datetimepickerStart').data("DateTimePicker").date().tz('Europe/Riga').toISOString().substring(0, 19);
    	if (data['start'] && data['start'].length === 10) {
    		data['start'] += 'T00:00:00';
    	}
    	data['end'] = $('#datetimepickerEnd').data("DateTimePicker").date().tz('Europe/Riga').toISOString().substring(0, 19);
    	if (data['end'].length === 10) {
    		data['end'] += 'T00:00:00';
    	}
    	console.log('Data before post', data);
        $.ajax({
            type: 'POST',
            url: '/TimeTracker/event/' + (data.id === 0 ? 'create' : 'update'),
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(data) ,
            dataType: 'json',
            success: function (result) {
                console.log(data.id === 0 ? 'Created event' : 'Updated event', data.id === 0 ? result.event.id : data.id);
                if (data.id) {
                    var events = $('#calendar').fullCalendar('clientEvents');
                    var event = _.find(events, function (e) {
                    	return e.id === data.id;
                    });
                    event.userId = data.userId;
                    event.title = data.title;
                    event.start = data.start;
                    event.end = data.end;
                    $('#calendar').fullCalendar('updateEvent', event, true);
                } else {
                	var event = {};
                    event.id = result.event.id;
                    event.userId = data.userId;
                    event.title = data.title;
                    event.start = data.start;
                    event.end = data.end;
                    $('#calendar').fullCalendar('renderEvent', event, true);
                }
                $('#calendarModal').modal('hide');
            },
            error: function (x, e) {
                console.log(data.id === 0 ? 'Creating' : 'Updating' + ' event error', x, e);
                $('#calendarModal').modal('hide');
                if (x.status === 400 && x.responseJSON.msg === "empty") {
                    alertError("Vajag izvēlēties aktivitāti.");
                    //TODO: pielikt validation
                } else if (x.status === 400 && x.responseJSON.msg === "cross") {
                    alertError("Aktivitātes nedrīkst pārklāties.");
                    //TODO: pielikt validation
                } else {
                	alertError("Kļūda: " + x.status);
                }
            }
        })
    })

});
