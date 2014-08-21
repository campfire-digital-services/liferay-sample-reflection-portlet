package au.com.permeance.liferay.portlet.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.portlet.RenderRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import au.com.permeance.liferay.portlet.controller.FluentReflection.FluentReflectionInvokation;

import com.liferay.portal.kernel.bean.BeanLocator;
import com.liferay.portal.kernel.bean.PortletBeanLocatorUtil;
import com.liferay.portal.kernel.exception.SystemException;

/**
 * Copyright (C) 2014 Permeance Technologies
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see <http://www.gnu.org/licenses/>.
 *
 * @author tim.myerscough
 *
 */
@Controller("sampleportletController")
@RequestMapping("view")
public class SamplePortletController {

    private static Logger _log = LoggerFactory.getLogger(SamplePortletController.class);

    @RenderMapping
    public String handleViewPage(final RenderRequest request, final ModelMap model) throws SystemException {
        _log.info("In View");

        BeanLocator bl = PortletBeanLocatorUtil.getBeanLocator("calendar-portlet");

        FluentReflectionInvokation calendarSvc = FluentReflection.within(bl).on("com.liferay.calendar.service.CalendarLocalService");
        FluentReflectionInvokation bookingsSvc = FluentReflection.within(bl).on("com.liferay.calendar.service.CalendarBookingLocalService");
        Object result = calendarSvc.invoke("getCalendarsCount");

        List<?> calendars = (List<?>) calendarSvc.invoke("getCalendars", 0, 100);

        Map<Object, Object> calIdToBookings = new HashMap<Object, Object>();

        for (Object cal : calendars) {
            FluentReflectionInvokation calendar = FluentReflection.on(cal);
            Object calendarId = calendar.invoke("getCalendarId");

            List<BookingVO> bookingVos = new LinkedList<SamplePortletController.BookingVO>();
            List<?> bookings = (List<?>) bookingsSvc.invoke("getCalendarBookings", calendarId);
            for (Object bookingObj : bookings) {
                FluentReflectionInvokation booking = FluentReflection.on(bookingObj);
                String title = (String) booking.invoke("getTitle", "en-US");
                long startDate = (Long) booking.invoke("getStartTime");
                long endDate = (Long) booking.invoke("getEndTime");

                BookingVO vo = new BookingVO();
                vo.title = title;
                vo.start = new Date(startDate);
                vo.end = new Date(endDate);

                bookingVos.add(vo);
            }

            calIdToBookings.put(calendarId, bookingVos);
        }

        String[] names;
        if (bl != null) {

            names = bl.getNames();
        } else {
            names = new String[] {"not found"};
        }

        model.put("calendarsCount", result);
        model.put("calendars", calendars);
        model.put("beanNames", names);
        model.put("bookings", calIdToBookings);

        return "view";
    }

    public static class BookingVO {
        public String title;
        public Date start;
        public Date end;

        public String getTitle() {
            return title;
        }

        public void setTitle(final String title) {
            this.title = title;
        }

        public Date getStart() {
            return start;
        }

        public void setStart(final Date start) {
            this.start = start;
        }

        public Date getEnd() {
            return end;
        }

        public void setEnd(final Date end) {
            this.end = end;
        }

    }

}
