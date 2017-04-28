<%@include file="includes/header.jsp" %>

<div class="panel panel-default">
	<div class="panel-heading">
		<h3 class="panel-title">Please signup</h3>
	</div>
	
	<div class="panel-body">
		<c:if test="${param.error != null}">
			<div class="alert alert-danger">
				Invalid username and password
			</div>
		</c:if>
		
		<c:if test="${param.logut != null}">
			<div class="alert alert-danger">
				You have been logged out
			</div>
		</c:if>
	
		<form:form role="form" method="post">
			
			<form:errors />
			
			<div class="form-group">
				<form:label path="email">Email address</form:label>
				<form:input path="email" type="email" class="form-control" placeholer="Entry email" />
				<p class="help-block">Enter your email address.</p>
			</div>
			
			<div class="form-group">
				<form:label path="password">Password</form:label>
				<form:password path="password" class="form-control" placeholder="Password" />
				<form:errors cssClass="error" path="password"/>
			</div>
			
			<div class="form-group">
				<div class="checkbox">
					<label>
						<input name="remember-me" type="checkbox"> Remember me
					</label>
				</div>
				<form:label path="password">Password</form:label>
				<form:password path="password" class="form-control" placeholder="Password" />
				<form:errors cssClass="error" path="password"/>
			</div>
		
			<button type="submit" class="btn btn-primary">Sign In</button>
			
			<a class="btn btn-default" href="/forgot-password">Forgot password</a>
			
		</form:form>
	</div>
</div>

<%@include file="includes/footer.jsp" %>