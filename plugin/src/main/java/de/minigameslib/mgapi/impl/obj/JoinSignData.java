/*
    Copyright 2016 by minigameslib.de
    All rights reserved.
    If you do not own a hand-signed commercial license from minigames.de
    you are not allowed to use this software in any way except using
    GPL (see below).

------

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.

*/

package de.minigameslib.mgapi.impl.obj;

import de.minigameslib.mclib.shared.api.com.PersistentField;
import de.minigameslib.mgapi.api.obj.AbstractObjectData;
import de.minigameslib.mgapi.api.rules.SignRuleSetType;

/**
 * @author mepeisen
 *
 */
public class JoinSignData extends AbstractObjectData<SignRuleSetType>
{

    // TODO editor for component data objects
    
    /**
     * line 1 of sign.
     */
    @PersistentField
    protected String line1;

    /**
     * line 2 of sign.
     */
    @PersistentField
    protected String line2;

    /**
     * line 3 of sign.
     */
    @PersistentField
    protected String line3;
    
    /**
     * line 4 of sign.
     */
    @PersistentField
    protected String line4;

    /**
     * Returns the first line.
     * 
     * @return the line1
     */
    public String getLine1()
    {
        return this.line1;
    }

    /**
     * Sets the first line.
     * 
     * @param line1 the line1 to set
     */
    public void setLine1(String line1)
    {
        this.line1 = line1;
    }

    /**
     * Returns the second line.
     * 
     * @return the line2
     */
    public String getLine2()
    {
        return this.line2;
    }

    /**
     * Sets the second line.
     * 
     * @param line2 the line2 to set
     */
    public void setLine2(String line2)
    {
        this.line2 = line2;
    }

    /**
     * Returns the third line.
     * 
     * @return the line3
     */
    public String getLine3()
    {
        return this.line3;
    }

    /**
     * Sets the third line.
     * 
     * @param line3 the line3 to set
     */
    public void setLine3(String line3)
    {
        this.line3 = line3;
    }

    /**
     * Returns the fourth line.
     * 
     * @return the line4
     */
    public String getLine4()
    {
        return this.line4;
    }

    /**
     * Sets the fourth line.
     * 
     * @param line4 the line4 to set
     */
    public void setLine4(String line4)
    {
        this.line4 = line4;
    }
}
