<!-- written by Angel Laboy https://codepen.io/thebunnyelite1-->    
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
   <%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
<head>
 
         <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
 <link type="text/css" rel="stylesheet" href="<c:url value="/css/login.css" />" />
	<title>
		Sign Up
	</title> 
</head>
<script src="https://gitcdn.link/repo/freeCodeCamp/testable-projects-fcc/master/build/bundle.js"></script>
<body>
<h1 id="title">Sign Up</h1>
<div id="form-outer" style="box-shadow: 20px 15px 20px rgba(0, 0, 0, 0.5);">
  <p id="description">
    Sign Up
  </p>
  <div id="id01" class="modal">

  <form:form modelAttribute="user" class="modal-content" method="POST">
    <div class="container">
      <h1>Sign Up</h1>
      <p>Please fill in this form to create an account.</p>
      <hr>
      <label for="email"><b>UserName</b></label>
      <form:input path="userName" type="text" placeholder="Enter UserName"/>

      <label for="psw"><b>Password</b></label>
      <form:input path="password" type="password" placeholder="Enter Password"/>
      <label>
        <input type="checkbox" checked="checked" name="remember" style="margin-bottom:15px"> Remember me
      </label>

      <p>By creating an account you agree to our <a href="#" style="color:dodgerblue">Terms &amp; Privacy</a>.</p>

      <div class="clearfix">
        <button type="button"  class="cancelbtn">Cancel</button>
        <button type="submit" class="signup">Sign Up</button>
      </div>
    </div>
  </form:form>
</div>
</div>
</body>
</html>