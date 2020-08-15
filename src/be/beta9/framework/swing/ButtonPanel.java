// $Id: ButtonPanel.java,v 1.2 2002/10/07 09:18:39 sven Exp $
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

import javax.swing.*;

public class ButtonPanel extends JPanel {

    public ButtonPanel() {
        super(new ExtendedGridBagLayout());
    }

    public JButton newButton(String label) {
        JButton button = new JButton(label);
        add(button, "insets=2,2,2,2");
        return button;
    }

}



