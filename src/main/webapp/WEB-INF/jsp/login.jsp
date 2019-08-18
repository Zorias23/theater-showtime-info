

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
  <form:form modelAttribute="user" id="survey-form" method="POST">
    <div class="rowTab">
      <div class="labels">
        <label id="name-label" for="name"> UserName: </label>
      </div>
      <div class="rightTab">
        <form:input path="userName" type="text" id="name" class="input-field" placeholder="UserName"/>
      </div>
    </div>
    <div class="rowTab">
      <div class="labels">
        <label id="email-label" for="Last Name" type="password">Password </label>
      </div>
      <div class="rightTab">
        <form:input type="password" path="password" id="email" class="input-field"  placeholder="Password"/>
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
  </form:form>
<h2>Already a user? <a href="<c:url value="/signUp.html" />"><u>Sign up!</u></a> </h2>
</div>
</body>
</html>
