<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page isELIgnored="false"%>
<!doctype html>
<html lang="en">
<head>
<meta charset="utf-8">
<meta name="viewport"
	content="width=device-width, user-scalable=no, initial-scale=1">
<meta http-equiv="X-UA-Compatible" content="ie=edge">
<title>User Form</title>
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css"
	integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3"
	crossorigin="anonymous">
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/@fortawesome/fontawesome-free@6.2.1/css/fontawesome.min.css"
	integrity="sha384-QYIZto+st3yW+o8+5OHfT6S482Zsvz2WfOzpFSXMF9zqeLcFV0/wlZpMtyFcZALm"
	crossorigin="anonymous">
<style>
#form-header {
	transition: all 1s;
}

#form-header:hover {
	opacity: 0.5;
}

.input {
	transition: all 0.5s;
}

.input:hover {
	background-color: var(- -bs-gray-200);
}
</style>
</head>
<body class="bg-light">


	<div class="container-md">

		<div class="row">
			<div class="col-12">
				<div class="card shadow-lg">
					<div id="form-header"
						class="card-header fs-1 text-center bg-dark text-light p-2 ">
						UserForm</div>
					<div class="card-body">
						<form:form action="/spring_advance/user/register" method="post"
							modelAttribute="userInfo">
							<div class="row">
								<p class="fs-1">User Details</p>
								<div class="col-6 form-group">
									<label class="p-1 fs-5">User Name:</label>
									<form:input class="input form-control" path="userName"
										placeholder="enter your name" />
									<div class="form-text text-danger">
										<form:errors path="userName" /><br>
									</div>
								</div>
								<div class="col-6 form-group">
									<label class="p-1 fs-5">Password:</label>
									<form:input class="input form-control" path="password"
										placeholder="enter your password" />
									<div class="form-text text-danger">
										<form:errors path="password" /><br>
									</div>
								</div>
								<div class="col-6 form-group">
									<label class="p-1 fs-5">Designation:</label>
									<form:input class="input form-control" path="dob"
										placeholder="enter your dob" />
									<div class="form-text text-danger">
										<form:errors path="dob" /><br>
										<form:errors path="age"/><br>
									</div>
								</div>
								<div class="col-6 form-group">
									<label class="p-1 fs-5">Hobbies:</label>
									<div class="form-control row gap-3 fs-5">
										<form:checkboxes class="m-2" path="hobbies"
											items="${hobbiesList}" />
										<div class="form-text text-danger">
											<form:errors path="hobbies" /> <br>
										</div>
									</div>
								</div>

								<div class="col-6 form-group">
									<label class="p-1 fs-5">Preferred Location:</label>
									<form:select class="form-control text-center"
										path="preferredHolidayLocation">
										<form:options class="p-1 fs-5" items="${locationList}" />
									</form:select>
								</div>

								<div class="col-6 form-group">
									<div>
										<label class="p-1 fs-5">Gender:</label>
									</div>

									<label class="p-1 fs-5">Male:</label>
									<form:radiobutton path="gender" value="male" />
									<label class="p-1 fs-5">Female:</label>
									<form:radiobutton path="gender" value="female" />
									<div class="form-text text-danger">
										<form:errors path="gender" /><br>
									</div>
								</div>
								<div class="btn-group col-12 mt-3 gap-4 ">
									<button name="add" type="submit"
										class="btn btn-default btn-success">Register User</button>
								</div>
							</div>
						</form:form>
					</div>
				</div>
			</div>
		</div>

	</div>


	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"
		integrity="sha384-ka7Sk0Gln4gmtz2MlQnikT1wXgYsOg+OMhuP+IlRH9sENBO0LRn5q+8nbTov4+1p"
		crossorigin="anonymous"></script>
</body>
</html>