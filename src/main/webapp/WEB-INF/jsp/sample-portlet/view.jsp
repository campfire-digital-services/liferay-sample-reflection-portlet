<%@ include file="init.jsp"%>

Calendars available: ${calendarsCount}<br/>

<c:forEach var="calendar" items="${calendars}">
<h3>Calendar: ${calendar.name}</h3>
    <ul>
    <c:forEach var="booking" items="${bookings.get(calendar.calendarId)}"> 
        <li>${booking.title} (${booking.start } - ${booking.end })</li>        
    </c:forEach>
    </ul>

</c:forEach>
