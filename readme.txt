HylaFAXSender -- a simple frontend to send documents 
to a HylaFAX server (http://www.hylafax.org).

http://www.beta9.be/hylafax/ -- hylafax@beta9.be

COPYING

This Java application requires and uses the GNU HylaFAX Java
library, see http://net-foundry.com/java/gnu/hylafax for
more details.

This program is free software; you can redistribute it
and/or modify it under the terms of the GNU General Public
License as published by the Free Software Foundation (see
gpl.txt or http://www.gnu.org/copyleft/gpl.html).

WHY

This software was developed to scratch an itch. There are no
Java clients for HylaFAX that do their job well. Most of the
time they are overkill and not that well designed.
HylaFAXSender is designed to send a simple fax job to the
HylaFAX server without too much hassle. Basic configuration
of the server and jobs are implemented in the preferences
panel. You enter a faxnumber, choose a document to fax and
click fax - that's all.

INSTALLATION

Make sure "java" is in your execution path. If not edit the
script "start.HylaFAXSender". Run it to enjoy our little app.
Jar files and libraries are located in dist. Mac OS X users can
use the app bundle included in app directory of the .dmg file

COMPILATION / HACKING / BUG REPORTS

Use the Source ! We welcome your bug fixes and/or
improvements or feature requests. You are free to modify
this program as you see fit, provided you do it under the
terms of the GPL (see COPYING above). Send all bugs(-fixes)
and other stuff to : hylafax@beta9.be

Full source tree is available in src. We use ant as the
default build system (build.xml), see
http://jakarta.apache.org/ant/ for more info. Read the
todo.txt if you want to help us improve.


MAC OS X Version

This application is tailored to give an almost native Mac OS
X user experience. See http://homepage.mac.com/svc/ for more
info on how this was done. Mac OS X developers should check
out the "run-mac" build.xml target ;-)

LATEST VERSION

You can always obtain the latest version at
http://www.beta9.be/hylafax/ -- hylafax@beta9.be

$Id: readme.txt,v 1.7 2002/10/08 14:44:59 nicky Exp $
