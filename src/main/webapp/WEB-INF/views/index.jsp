<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<script type="text/javascript"
	src="http://code.jquery.com/jquery-2.1.3.min.js"></script>
<link rel="stylesheet" type="text/css"
	href="resources/lib/bootstrap-3.3.5.min.css">
<script type="text/javascript"
	src="resources/lib/bootstrap-3.3.5.min.js"></script>
<script type="text/javascript"
	src="resources/lib/moment-with-locales-2.22.2.min.js"></script>
<script type="text/javascript"
	src="resources/lib/moment-timezone-with-data-2.22.2.min.js"></script>
<link rel="stylesheet" type="text/css"
	href="resources/lib/bootstrap-datetimepicker-4.17.47.min.css">
<link rel="stylesheet" type="text/css"
	href="resources/lib/bootstrap-datetimepicker-standalone-4.17.47.css">
<script type="text/javascript"
	src="resources/lib/bootstrap-datetimepicker-4.17.47.min.js"></script>
<link rel="stylesheet" type="text/css"
	href="resources/lib/fullcalendar-3.9.0.min.css">
<link rel="stylesheet" type="text/css" media="print"
	href="resources/lib/fullcalendar.print-3.9.0.min.css">
<script type="text/javascript"
	src="resources/lib/fullcalendar-3.9.0.min.js"></script>
<script type="text/javascript"
	src="resources/lib/lodash-4.17.10.min.js"></script>
<script type="text/javascript" src="resources/myscripts.js"></script>

<head>
<title>Laika uzskaites sistēma</title>
<body>
	<h2>Laika uzskaites sistēma</h2>

	<input type="hidden" id="inputUserId" name="inputUserId" value="0">

	<div id="alertSuccess" class="alert alert-success" data-alert="alert">Success</div>
	<div id="alertError" class="alert alert-danger" data-alert="alert">Error</div>

	<!-- Button trigger login modal -->
	<button class="btn btn-primary btn-lg" id="loginButton"
		data-toggle="modal" data-target="#loginModal">Ielogoties</button>

	<!-- Button trigger register modal -->
	<button class="btn btn-primary btn-lg" id="registerButton"
		data-toggle="modal" data-target="#registerModal">
		Reģistrēties</button>

	<!-- Button trigger change password modal -->
	<button class="btn btn-primary btn-lg" id="changePasswordButton"
		data-toggle="modal" data-target="#changePasswordModal">
		Nomainīt paroli</button>

	<!-- Button trigger logout -->
	<button class="btn btn-primary btn-lg" id="logoutButton">
		Izlogoties</button>

	<!-- Calendar -->
	<div id="calendar" style="overflow: auto; margin-top: 20px;"></div>

	<div class="modal fade" id="calendarModal" tabindex="-1" role="dialog"
		aria-labelledby="calendarModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<!-- Modal Header -->
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">&times;</span> <span class="sr-only">Close</span>
					</button>
					<h4 class="modal-title" id="calendarModalLabel">Izvēlēties
						laiku</h4>
				</div>

				<!-- Modal Body -->
				<div class="modal-body">

					<form class="form-horizontal" role="form">
						<input type="hidden" id="inputEventId" name="inputEventId" value="0">
						<div class="form-group">
							<label class="col-sm-2 control-label" for="inputActivity">Aktivitāte</label>
							<div class="col-sm-10">
								<input type="text" class="form-control" id="inputActivity"
									placeholder="Aktivitāte" autofocus/>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label" for="datetimepickerStart">Sākums</label>
							<div class="col-sm-10">
								<div class='input-group date' id='datetimepickerStart'>
									<input type='text' class="form-control" /> <span
										class="input-group-addon"> <span
										class="glyphicon glyphicon-time"></span>
									</span>
								</div>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label" for="datetimepickerEnd">Beigas</label>
							<div class="col-sm-10">
								<div class='input-group date' id='datetimepickerEnd'>
									<input type='text' class="form-control" /> <span
										class="input-group-addon"> <span
										class="glyphicon glyphicon-time"></span>
									</span>
								</div>
							</div>
						</div>
					</form>
				</div>

				<!-- Modal Footer -->
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">
						Aizvērt</button>
					<button id="calendarDeleteButton" type="button"
						class="btn btn-danger">Dzēst</button>
					<button id="calendarSubmitButton" type="button"
						class="btn btn-primary">Saglabāt</button>
				</div>
			</div>
		</div>
	</div>

	<!-- Login Modal -->
	<div class="modal fade" id="loginModal" tabindex="-1" role="dialog"
		aria-labelledby="loginModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<!-- Modal Header -->
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">&times;</span> <span class="sr-only">Close</span>
					</button>
					<h4 class="modal-title" id="loginModalLabel">Ielogoties</h4>
				</div>

				<!-- Modal Body -->
				<div class="modal-body">

					<form class="form-horizontal" role="form">
						<div class="form-group">
							<label class="col-sm-2 control-label" for="inputUsername">Lietotājs</label>
							<div class="col-sm-10">
								<input type="email" class="form-control" id="inputUsername"
									placeholder="Lietotājs" autofocus/>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label" for="inputPassword">Parole</label>
							<div class="col-sm-10">
								<input type="password" class="form-control" id="inputPassword"
									placeholder="Parole" />
							</div>
						</div>
					</form>
				</div>

				<!-- Modal Footer -->
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">
						Aizvērt</button>
					<button id="loginSubmitButton" type="button"
						class="btn btn-primary">Saglabāt</button>
				</div>
			</div>
		</div>
	</div>

	<!-- Register Modal -->
	<div class="modal fade" id="registerModal" tabindex="-1" role="dialog"
		aria-labelledby="registerModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<!-- Modal Header -->
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">&times;</span> <span class="sr-only">Close</span>
					</button>
					<h4 class="modal-title" id="registerModalLabel">Reģistrēties</h4>
				</div>

				<!-- Modal Body -->
				<div class="modal-body">

					<form class="form-horizontal" role="form">
						<div class="form-group">
							<label class="col-sm-2 control-label" for="inputNewUsername">Lietotājs</label>
							<div class="col-sm-10">
								<input type="email" class="form-control" id="inputNewUsername"
									placeholder="Lietotājs" autofocus/>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label" for="inputNewPassword">Parole</label>
							<div class="col-sm-10">
								<input type="password" class="form-control"
									id="inputNewPassword" placeholder="Parole" />
							</div>
						</div>
					</form>
				</div>

				<!-- Modal Footer -->
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">
						Aizvērt</button>
					<button id="registerSubmitButton" type="button"
						class="btn btn-primary">Saglabāt</button>
				</div>
			</div>
		</div>
	</div>

	<!-- Change Password Modal -->
	<div class="modal fade" id="changePasswordModal" tabindex="-1"
		role="dialog" aria-labelledby="changePasswordModalLabel"
		aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<!-- Modal Header -->
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">&times;</span> <span class="sr-only">Close</span>
					</button>
					<h4 class="modal-title" id="changePasswordModalLabel">
						Nomainīt paroli</h4>
				</div>

				<!-- Modal Body -->
				<div class="modal-body">

					<form class="form-horizontal" role="form">
						<div class="form-group">
							<label class="col-sm-2 control-label" for="inputPassword0">Esošā
								parole</label>
							<div class="col-sm-10">
								<input type="password" class="form-control" id="inputPassword0"
									placeholder="Esošā parole" autocomplete="off" autofocus/>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label" for="inputPassword1">Jaunā
								parole</label>
							<div class="col-sm-10">
								<input type="password" class="form-control" id="inputPassword1"
									placeholder="Jaunā parole" autocomplete="off" />
							</div>
							<div class="row">
								<div class="col-sm-4">
									<span id="8char" class="glyphicon glyphicon-remove"
										style="color: #FF0004;"></span> 8 simboli gara<br> <span
										id="ucase" class="glyphicon glyphicon-remove"
										style="color: #FF0004;"></span> Viens lielais burts
								</div>
								<div class="col-sm-4">
									<span id="lcase" class="glyphicon glyphicon-remove"
										style="color: #FF0004;"></span> Viens mazais burts<br> <span
										id="num" class="glyphicon glyphicon-remove"
										style="color: #FF0004;"></span> Viens cipars
								</div>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label" for="inputPassword2">Atkārtot
								paroli</label>
							<div class="col-sm-10">
								<input type="password" class="form-control" id="inputPassword2"
									placeholder="Atkārtot paroli" autocomplete="off" />
							</div>
							<div class="row">
								<div class="col-sm-8">
									<span id="pwmatch" class="glyphicon glyphicon-remove"
										style="color: #FF0004;"></span> Paroles sakrīt
								</div>
							</div>
						</div>
					</form>
				</div>

				<!-- Modal Footer -->
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">
						Aizvērt</button>
					<button id="changePasswordSubmitButton" type="button"
						class="btn btn-primary">Saglabāt</button>
				</div>
			</div>
		</div>
	</div>

</body>
</html>