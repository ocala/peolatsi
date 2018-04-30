package wise.gui.util;

// Source File Name:   schemFilter.java
//
//  Author/Copyright: Wolfgang Scherr
//
//  $Id: jschematic.java,v 1.1.1.1 2002/09/03 19:30:57 hoidain Exp $
//
//     Webbased Interactive Simulation Environment
//                             __     __
//               ||    // ||  // \\ ||
//               ||//|//  ||  \\__  ||_
//               |// |/   ||     \\ ||
//               |/  |    || \\__// ||___
//
//  Fachhochschule TECHNIKUM english semester project
//
//  Semester: 4ebb
//

/*
   ------------------------------------------------------------------------

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.

   ------------------------------------------------------------------------
*/

import java.awt.*;
import java.io.*;

public class schemFilter implements FilenameFilter {

  final static String pident = ".gra";

  public schemFilter() {
  }
  public boolean accept(File f,String ss) {
        System.out.println("Filter: "+f+" "+ss);
        if (f.isDirectory())
            return true;

        String s = f.getName();
        if (s.length()<5) return false;

        if (s.lastIndexOf(pident)==(s.length()-4))
            return true;

        return false;
  }
  public String getDescription() {
        return "JSCHEMATIC code (*.gra)";
  }
}
