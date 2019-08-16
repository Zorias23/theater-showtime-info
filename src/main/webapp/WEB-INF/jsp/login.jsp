

<!-- written by Angel Laboy https://codepen.io/thebunnyelite1-->    
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
 
         <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
 <link type="text/css" rel="stylesheet" href="<c:url value="/css/login.css" />" />
	<title>
		Login
	</title> 
</head>
<script src="https://gitcdn.link/repo/freeCodeCamp/testable-projects-fcc/master/build/bundle.js"></script>
<body>
<h1 id="title">Login</h1>
<div id="form-outer" style="box-shadow: 20px 15px 20px rgba(0, 0, 0, 0.5);">
  <p id="description">
    Login
  </p>
  <form id="survey-form" method="GET" action="#">
    <div class="rowTab">
      <div class="labels">
        <label id="name-label" for="name"> Email: </label>
      </div>
      <div class="rightTab">
        <input autofocus type="text" name="name" id="name" class="input-field" placeholder="Email" required>
      </div>
    </div>
    <div class="rowTab">
      <div class="labels">
        <label id="email-label" for="Last Name" type="password">Password </label>
      </div>
      <div class="rightTab">
        <input type="text" name="email" id="email" class="input-field" required placeholder="Password">
      </div>
    </div>
    <div class="rowTab">
      <div class="labels">
      <label id="email-label">
      <input type="checkbox" name="remember" > Remember me
    </label>
      </div>
       </div>
     
    <button id="submit" type="submit">Login</button>
  </form>
    <!--/div-->
    <!-- Button to open the modal -->
<button onclick="document.getElementById('id01').style.display='block'">Sign Up</button>

<!-- The Modal (contains the Sign Up form) -->
<div id="id01" class="modal">
  <span onclick="document.getElementById('id01').style.display='none'" class="close" title="Close Modal"></span>
  <form class="modal-content" action="/action_page.php">
    <div class="container">
      <h1>Sign Up</h1>
      <p>Please fill in this form to create an account.</p>
      <hr>
      <label for="email"><b>Email</b></label>
      <input type="text" placeholder="Enter Email" name="email" required>

      <label for="psw"><b>Password</b></label>
      <input type="password" placeholder="Enter Password" name="psw" required>

      <label for="psw-repeat"><b>Repeat Password</b></label>
      <input type="password" placeholder="Repeat Password" name="psw-repeat" required>

      <label>
        <input type="checkbox" checked="checked" name="remember" style="margin-bottom:15px"> Remember me
      </label>

      <p>By creating an account you agree to our <a href="#" style="color:dodgerblue">Terms &amp; Privacy</a>.</p>

      <div class="clearfix">
        <button type="button" onclick="document.getElementById('id01').style.display='none'" class="cancelbtn">Cancel</button>
        <button type="submit" class="signup">Sign Up</button>
      </div>
    </div>
  </form>
</div>

</div>
</body>
</html>
