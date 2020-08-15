// $Id: ReflectionTools.java,v 1.3 2002/10/07 09:18:39 sven Exp $
// Copyright (C) 2002 Beta Nine B.V.B.A.
//
// This program is free software; you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation; either version 2 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

package be.beta9.framework.swing;

import java.lang.reflect.*;

/**
 * ReflectionTools is a collection of Reflection utilities.
 */
public class ReflectionTools {
    
    public static Object perform(Object model, String methodname)
        throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        return perform(model, methodname, new Class[]{}, new Object[]{});
    }
    
    public static Object perform(Object model, String methodname, Class[] signature, Object[] arguments)
        throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Method method = model.getClass().getMethod(methodname, signature);
        return method.invoke(model, arguments);
    }
    
    public static Object performVerbose(Object model, String methodname) {
        return performVerbose(model, methodname, new Class[]{}, new Object[]{});
    }
    
    public static Object performVerbose(Object model, String methodname, Class[] signature, Object[] arguments) {
        try {
            return perform(model, methodname, signature, arguments);
        } catch (NoSuchMethodException e1) {
            error(methodname + " not found in " + model.getClass(), e1);
        } catch (IllegalAccessException e2) {
            error("cannot access " + methodname + " in " + model.getClass(), e2);
        } catch (InvocationTargetException e3) {
            error("failed to perform " + methodname + " in " + model.getClass(), e3);
            error("target exception", e3.getTargetException());
        }
        throw new RuntimeException("ReflectionTools.performVerbose failed");
    }
    
    public static void error(String msg, Throwable t) {
        System.out.println(msg);
        System.out.println(t.getMessage());
        t.printStackTrace();
    }
    
}

