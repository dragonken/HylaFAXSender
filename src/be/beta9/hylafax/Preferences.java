// $Id: Preferences.java,v 1.3 2002/10/07 09:18:39 sven Exp $
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

package be.beta9.hylafax;

import java.io.*;
import java.util.*;
import com.apple.mrj.*;

public class Preferences {
    
    private Properties properties;
    private File file;
    
    public Preferences(String name) {
        this.properties = new Properties();
        if (System.getProperty("mrj.version") != null) {
            try {
                File dir = MRJFileUtils.findFolder(MRJFileUtils.kPreferencesFolderType);
                file = new File(dir, name + ".properties");
            } catch (FileNotFoundException ignore) {
            }
        }
        if (file == null) {
            File dir = new File(System.getProperty("user.home"));
            file = new File(dir, "." + name + ".properties");
        }
        load();
    }
    
    public String get(String key) {
        return properties.getProperty(key);
    }
    
    public void put(String key, String value) {
        properties.put(key, value);
        save();
    }
    
    public void putIfAbsent(String key, String value) {
        if (!properties.containsKey(key)) {
            properties.put(key, value);
            save();
        }
    }
    
    private void save() {
        try {
            OutputStream out = new BufferedOutputStream(new FileOutputStream(file));
            properties.save(out, getClass().getName() + "(" + file.getAbsolutePath() + ")");
            out.close();
        } catch (Exception ignore) {
        }
    }
    
    private void load() {
        try {
            InputStream in = new BufferedInputStream(new FileInputStream(file));
            properties.load(in);
            in.close();
        } catch (Exception ignore) {
        }
    }
    
}
