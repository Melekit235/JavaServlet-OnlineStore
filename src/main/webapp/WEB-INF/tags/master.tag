<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ tag isELIgnored="false" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ attribute name="pageTitle" required="true" %>


<html>
<head>
    <meta charset="UTF-8">
    <title>${pageTitle}</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css" integrity="sha384-MCw98/SFnGE8fJT3GXwEOngsV7Zt27NXFoaoApmYm81iuXoPkFOJwJ8ERdknLPMO" crossorigin="anonymous">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Lobster&family=Lobster+Two:ital,wght@0,400;0,700;1,400;1,700&display=swap" rel="stylesheet">
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f5f5f5;
            margin: 0;
            padding: 0;
        }
        header {
            background-color: #343a40;
            color: white;
            padding: 10px;
            text-align: center;
        }
        .container {
            margin-top: 20px;
            padding: 20px;
            background-color: white;
            border-radius: 8px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }
    </style>
</head>
<body>
<header>
    <fmt:setLocale value="${sessionScope.lang}"/>
    <fmt:setBundle basename="messages"/>
    <div class="container">
        <div class="row">
            <div class="col-6">
                <h1 style="font-family: 'Lobster'">
                    <a class="text-dark" href="<c:url value="/?command=Product_List"/>">
                        BALLBASKET
                    </a>
                    <br>
                    <span style="font-family: 'Ubuntu'" class="text-dark">
                        <c:if test="${not empty sessionScope.login}">
                            <fmt:message key="master_username" />: ${sessionScope.login}
                        </c:if>
                    </span>
                </h1>
            </div>
            <div class="col-6">
                <div class="float-right">
                    <c:choose>
                        <c:when test="${not empty sessionScope.login}">
                            <form action="<c:url value="/"/>" method="get">
                                <input type="hidden" name="command" value="cart">
                                <button class="btn btn-light"> <fmt:message key="master_cart" />:
                                    <span id="cartTotalCost"><c:out value="${cart.totalCost}"/></span>$
                                </button>
                            </form>
                        </c:when>
                        <c:otherwise>
                            <form action="<c:url value="/"/>" method="get">
                                <input type="hidden" name="command" value="authorisation">
                                <button class="btn btn-light"> <fmt:message key="master_cart" />:</button>
                            </form>
                        </c:otherwise>
                    </c:choose>
                    <c:if test="${role.equals('Admin')}">
                    <form action="<c:url value="/"/>">
                        <input type="hidden" name="command" value="admin_orders">
                        <button class="btn btn-light"> <fmt:message key="master_orders_page" /> </button>
                    </form>
                    <form action="<c:url value="/"/>">
                        <input type="hidden" name="command" value="admin_users">
                        <button class="btn btn-light"> <fmt:message key="master_users_page" /> </button>
                    </form>
                    </c:if>
                    <c:if test="${role.equals('User')}">
                        <form action="<c:url value="/"/>">
                            <input type="hidden" name="command" value="user_orders">
                            <button class="btn btn-light"> <fmt:message key="master_orders_page" /> </button>
                        </form>
                    </c:if>
                </div>
            </div>
            <div class="col-6">
                <a href="<c:url value="/?command=registration"/>" class="btn btn-primary"><fmt:message key="master_registration" /></a>
                <c:choose>
                <c:when test="${not empty sessionScope.login}">
                    <a href="<c:url value="/?command=logout"/>" class="btn btn-danger"><fmt:message key="master_logout" /></a>
                </c:when>
                <c:otherwise>
                    <a href="<c:url value="/?command=authorisation"/>" class="btn btn-success"><fmt:message key="master_login" /></a>
                </c:otherwise>
                </c:choose>
            </div>
        </div>
        <div align="right">
        <li style="color: white"><a class="btn btn-light" href="?sessionLocale=en"><fmt:message key="label.lang.en" /></a></li>
        <br>
        <li style="color: white"><a class="btn btn-light" href="?sessionLocale=ru"><fmt:message key="label.lang.ru" /></a></li>
        <br>
        </div>
    </div>

</header>
<main>
    <p></p>
    <jsp:doBody/>
</main>
</body>
</html>