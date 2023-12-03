<%@ taglib tagdir="/WEB-INF/tags" prefix="tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:useBean id="ball" scope="request" type="com.bsuir.verghel.basketball.model.entities.ball.Ball"/>
<fmt:setLocale value="${sessionScope.lang}"/>
<fmt:setBundle basename="messages"/>
<tags:master pageTitle="Phohe Details">

    <c:choose>
        <c:when test="${not empty inputErrors}">
            <div class="container">
                <div class="panel panel-danger">
                    <div class="panel-heading"><fmt:message key="error_title" /></div>
                    <div class="panel-body">
                        <fmt:message key="error_updating_cart" />
                        <br>
                        ${inputErrors.get(ball.id)}
                    </div>
                </div>
            </div>
        </c:when>
        <c:otherwise>
            <c:if test="${not empty successMessage}">
                <div class="container">
                    <div class="panel panel-success">
                        <div class="panel-heading"><fmt:message key="success_title" /></div>
                        <div class="panel-body">${successMessage}</div>
                    </div>
                </div>
            </c:if>
        </c:otherwise>
    </c:choose>
    <div class="panel"></div>
    <div class="container">
        <h2>${ball.model}</h2>
        <div class="row">
            <div class="col-6">
                <img class="rounded" src="https://raw.githubusercontent.com/andrewosipenko/ballshop-ext-images/master/${ball.imageUrl}">
                <p class="text-justify">${ball.description}</p>
                <div class="float-right">
                    <p class="text">Price: $${ball.price}</p>
                    <c:choose>
                        <c:when test="${not empty sessionScope.login}">
                            <form action="/" method="post">
                                <input type="hidden" name="command" value="cart_add">
                                <input type="hidden" name="page_type" value="product_details">
                        </c:when>
                        <c:otherwise>
                            <form action="/" method="get">
                                <input type="hidden" name="command" value="authorisation">
                        </c:otherwise>
                    </c:choose>
                        <input type="hidden" name="id" value="${ball.id}">
                        <input type="number" name="quantity" id="quantity${ball.id}" min="1" required>
                        <button class="btn btn-lg btn-outline-light text-dark border-dark float-right" type="submit"><fmt:message key="button_add" /></button>
                    </form>
                </div>
            </div>

            <div class="col-1"></div>

            <div class="col-4">
                <h3><fmt:message key="ball_size" /></h3>
                <table class="table table-bordered table-light container-fluid">
                    <tr>
                        <td><fmt:message key="ball_size" /></td>
                        <td>${ball.size}"</td>
                    </tr>
                </table>
                <h3><fmt:message key="ball_material" /></h3>
                <table class="table table-bordered table-light container-fluid">
                    <tr>
                        <td><fmt:message key="ball_material" /></td>
                        <td>${ball.material} mm</td>
                    </tr>
                </table>
                <h3><fmt:message key="ball_weight" /></h3>
                <table class="table table-bordered table-light container-fluid">
                    <tr>
                        <td><fmt:message key="ball_weight" /></td>
                        <td>${ball.weight}</td>
                    </tr>
                </table>
                <h3><fmt:message key="ball_circumference" /></h3>
                <table class="table table-bordered table-light container-fluid">
                    <tr>
                        <td><fmt:message key="ball_circumference" /></td>
                        <td>${ball.circumference} hours</td>
                    </tr>
                </table>
            </div>

            <div class="col-1"></div>
        </div>
    </div>
</tags:master>