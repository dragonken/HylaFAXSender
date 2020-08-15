// $Id: GenericActionListener.java,v 1.2 2002/10/07 09:18:39 sven Exp $
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


import java.awt.event.*;

/**
 * ActionListener that invokes a method on a target using reflection
 */
public class GenericActionListener implements ActionListener {

    private Object target;
    private String action;
    private Class[] argumentTypes;
    private Object[] arguments;


    public GenericActionListener(Object target, String action) {
        this.target = target;
        this.action = action;
    }

    public GenericActionListener(Object target, String action, Class[] argumentTypes, Object[] arguments) {
        this.target = target;
        this.action = action;
        this.argumentTypes = argumentTypes;
        this.arguments = arguments;
    }

    public void actionPerformed(ActionEvent event) {
        if (argumentTypes != null) {
            ReflectionTools.performVerbose(target, action, argumentTypes, arguments);
        } else {
            ReflectionTools.performVerbose(target, action);
        }
    }

}


