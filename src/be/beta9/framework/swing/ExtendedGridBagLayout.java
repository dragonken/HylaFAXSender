// $Id: ExtendedGridBagLayout.java,v 1.2 2002/10/07 09:18:39 sven Exp $
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

import java.awt.*;
import java.util.*;

/**
 * A small extension to GridBagLayout to allow GridBagConstraints to be specified as strings.
 * The default GridBagConstraints expressed as a specification strings is
 *  "anchor=CENTER;fill=NONE;gridx=RELATIVE;gridy=RELATIVE;gridwidth=1;gridheight=1;ipadx=0;ipady=0;insets=0,0,0,0;weightx=0.0;weighty=0.0"
 * which could be abbreviated as
 *  "anchor=C;fill=N;grid=REL,REL,1,1;ipad=0,0;insets=0,0,0,0;weight=0.0,0.0"
 * other abbreviations are REM for REMAINDER, S, SE, etc.. for SOUTH, SOUTHEAST,
 * and H, V for HORIZONTAL and VERTICAL
 */
public class ExtendedGridBagLayout extends GridBagLayout {

    public ExtendedGridBagLayout() {
        super();
    }

    public void addLayoutComponent(Component component, Object constraints) {
        if (constraints instanceof String) {
            constraints = parseGridBagConstraints((String) constraints);
        }
        super.addLayoutComponent(component, constraints);
    }

    public void setConstraints(Component component, String gridBagConstraintsSpecification) {
        super.setConstraints(component, parseGridBagConstraints(gridBagConstraintsSpecification));
    }

    private static String[] ANCHOR_CONSTANT_NAMES = new String[] {
        "CENTER", "EAST", "NORTH",
            "NORTHEAST", "NORTHWEST", "SOUTH",
            "SOUTHEAST", "SOUTHWEST", "WEST",
            "C", "E", "N",
            "NE", "NW", "S",
            "SE", "SW", "W" };

    private static int[] ANCHOR_CONSTANT_VALUES = new int[] {
        GridBagConstraints.CENTER, GridBagConstraints.EAST, GridBagConstraints.NORTH,
            GridBagConstraints.NORTHEAST, GridBagConstraints.NORTHWEST, GridBagConstraints.SOUTH,
            GridBagConstraints.SOUTHEAST, GridBagConstraints.SOUTHWEST, GridBagConstraints.WEST,
            GridBagConstraints.CENTER, GridBagConstraints.EAST, GridBagConstraints.NORTH,
            GridBagConstraints.NORTHEAST, GridBagConstraints.NORTHWEST, GridBagConstraints.SOUTH,
            GridBagConstraints.SOUTHEAST, GridBagConstraints.SOUTHWEST, GridBagConstraints.WEST,
    };

    private static String[] FILL_CONSTANT_NAMES = new String[] {
        "BOTH", "HORIZONTAL", "VERTICAL",
            "NONE",
            "B", "H", "V",
            "N" };

    private static int[] FILL_CONSTANT_VALUES = new int[] {
        GridBagConstraints.BOTH, GridBagConstraints.HORIZONTAL, GridBagConstraints.VERTICAL,
            GridBagConstraints.NONE,
            GridBagConstraints.BOTH, GridBagConstraints.HORIZONTAL, GridBagConstraints.VERTICAL,
            GridBagConstraints.NONE };

    private static String[] GRID_POSITION_CONSTANT_NAMES = new String[] {
        "RELATIVE",
            "REL" };

    private static int[] GRID_POSITION_CONSTANT_VALUES = new int[] {
        GridBagConstraints.RELATIVE,
            GridBagConstraints.RELATIVE };

    private static String[] GRID_SIZE_CONSTANT_NAMES = new String[] {
        "RELATIVE", "REMAINDER",
            "REL", "REM" };

    private static int[] GRID_SIZE_CONSTANT_VALUES = new int[] {
        GridBagConstraints.RELATIVE, GridBagConstraints.REMAINDER,
            GridBagConstraints.RELATIVE, GridBagConstraints.REMAINDER };

    public static GridBagConstraints parseGridBagConstraints(String gridBagConstraintsSpecification) {
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        StringTokenizer tokens = new StringTokenizer(gridBagConstraintsSpecification, ";");
        while (tokens.hasMoreTokens()) {
            String token = tokens.nextToken();
            int indexOfEqualSign = token.indexOf('=');
            if (indexOfEqualSign == -1) throwException("missing '='");
            if (indexOfEqualSign == 0) throwException("missing property");
            String property = token.substring(0, indexOfEqualSign).toLowerCase();
            if (indexOfEqualSign + 1 >= token.length()) throwException("missing property value");
            String value = token.substring(indexOfEqualSign + 1).toUpperCase();
            if ("anchor".equals(property)) {
                gridBagConstraints.anchor = getInteger(value,
                                                       ANCHOR_CONSTANT_NAMES,
                                                       ANCHOR_CONSTANT_VALUES);
            } else if ("fill".equals(property)) {
                gridBagConstraints.fill = getInteger(value,
                                                     FILL_CONSTANT_NAMES,
                                                     FILL_CONSTANT_VALUES);
            } else if ("gridx".equals(property)) {
                gridBagConstraints.gridx = getInteger(value,
                                                      GRID_POSITION_CONSTANT_NAMES,
                                                      GRID_POSITION_CONSTANT_VALUES);
            } else if ("gridy".equals(property)) {
                gridBagConstraints.gridy = getInteger(value,
                                                      GRID_POSITION_CONSTANT_NAMES,
                                                      GRID_POSITION_CONSTANT_VALUES);
            } else if ("gridwidth".equals(property)) {
                gridBagConstraints.gridwidth = getInteger(value,
                                                          GRID_SIZE_CONSTANT_NAMES,
                                                          GRID_SIZE_CONSTANT_VALUES);
            } else if ("gridheight".equals(property)) {
                gridBagConstraints.gridheight = getInteger(value,
                                                           GRID_SIZE_CONSTANT_NAMES,
                                                           GRID_SIZE_CONSTANT_VALUES);
            } else if ("ipadx".equals(property)) {
                gridBagConstraints.ipadx = getInteger(value);
            } else if ("ipady".equals(property)) {
                gridBagConstraints.ipady = getInteger(value);
            } else if ("weightx".equals(property)) {
                gridBagConstraints.weightx = getDouble(value);
            } else if ("weighty".equals(property)) {
                gridBagConstraints.weighty = getDouble(value);
            } else if ("insets".equals(property)) {
                StringTokenizer valueTokens = new StringTokenizer(value, ",");
                try {
                    int top = getInteger(valueTokens.nextToken());
                    int left = getInteger(valueTokens.nextToken());
                    int bottom = getInteger(valueTokens.nextToken());
                    int right = getInteger(valueTokens.nextToken());
                    Insets insets = new Insets(top, left, bottom, right);
                    gridBagConstraints.insets = insets;
                } catch (NoSuchElementException e) {
                    throwException("not enough value elements for insets");
                }
            } else if ("grid".equals(property)) {
                StringTokenizer valueTokens = new StringTokenizer(value, ",");
                try {
                    gridBagConstraints.gridx = getInteger(valueTokens.nextToken(),
                                                          GRID_POSITION_CONSTANT_NAMES,
                                                          GRID_POSITION_CONSTANT_VALUES);
                    gridBagConstraints.gridy = getInteger(valueTokens.nextToken(),
                                                          GRID_POSITION_CONSTANT_NAMES,
                                                          GRID_POSITION_CONSTANT_VALUES);
                    gridBagConstraints.gridwidth = getInteger(valueTokens.nextToken(),
                                                              GRID_SIZE_CONSTANT_NAMES,
                                                              GRID_SIZE_CONSTANT_VALUES);
                    gridBagConstraints.gridheight = getInteger(valueTokens.nextToken(),
                                                               GRID_SIZE_CONSTANT_NAMES,
                                                               GRID_SIZE_CONSTANT_VALUES);
                } catch (NoSuchElementException e) {
                    throwException("not enough value elements for grid");
                }
            } else if ("ipad".equals(property)) {
                StringTokenizer valueTokens = new StringTokenizer(value, ",");
                try {
                    gridBagConstraints.ipadx = getInteger(valueTokens.nextToken());
                    gridBagConstraints.ipady = getInteger(valueTokens.nextToken());
                } catch (NoSuchElementException e) {
                    throwException("not enough value elements for ipad");
                }
            } else if ("weight".equals(property)) {
                StringTokenizer valueTokens = new StringTokenizer(value, ",");
                try {
                    gridBagConstraints.weightx = getDouble(valueTokens.nextToken());
                    gridBagConstraints.weightx = getDouble(valueTokens.nextToken());
                } catch (NoSuchElementException e) {
                    throwException("not enough value elements for ipad");
                }
            } else {
                throwException("unknown property '" + property + "'");
            }
        }
        return gridBagConstraints;
    }

    private static void throwException(String msg) throws RuntimeException {
        throw new RuntimeException("Illegal GridBagConstraints specification: " + msg);
    }

    private static double getDouble(String string) {
        try {
            return Double.parseDouble(string);
        } catch (NumberFormatException e) {
            throwException("not a double: '" + string + "'");
            return 0.0;
        }
    }

    private static int getInteger(String string, String[] constants, int[] values) {
        try {
            for (int i = 0; i < constants.length; i++) {
                if (string.equals(constants[i]))
                    return values[i];
            }
            return Integer.parseInt(string);
        } catch (NumberFormatException e) {
            throwException("not an integer: '" + string + "'");
            return 0;
        }
    }

    private static int getInteger(String string) {
        try {
            return Integer.parseInt(string);
        } catch (NumberFormatException e) {
            throwException("not an integer: '" + string + "'");
            return 0;
        }
    }

}



