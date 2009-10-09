/*
 * transmission-remote-java remote control for transmission daemon
 *
 * Copyright (C) 2009  dvstar
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.sf.dvstar.transmission.spmeter;

/**
 *
 * @author dstarzhynskyi
 */
public interface SpeedMetersInterface {
    public int getMaxValue();
    public void setMaxValue(int max);
    public void addValue();
    public void clearValue();
    public String printArray();
    public int getLength();
    public float getCurValue(int pos);
}
