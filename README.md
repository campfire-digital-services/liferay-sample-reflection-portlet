liferay-sample-reflection-portlet
=================================

Sample Liferay portlet to demonstrate invoking services in another portlet using reflection

Assume that we want to invoke the services within the Calendar portlet from another portlet.  And also assume that we do not have the class files available (we can't re-package them).

Using the PortletBeanLoacatorUtil we are able to use reflection to invoke operations.

Pre-Requisites
==============
To build this portlet, you need [Apache Maven](http://maven.apache.org/) and JDK 1.6+

Setup
=====
* [Install Liferay 6.2 CE](https://www.liferay.com/downloads/liferay-portal/available-releases)
* [Deploy the Calendar Portlet](http://www.liferay.com/marketplace/-/mp/application/31070085)

Installation
============
Checkout the project and build it using

    mvn clean install
    
Copy the built WAR file to your Liferay deploy folder.

Use
===
* Add the Calendar Portlet to a page
* Add this portlet to the page (Sample > reflection-sample)
* Add one or more events to the Calendar portlet and refresh the page
* The events should be listed under the reflection-sample portlet.