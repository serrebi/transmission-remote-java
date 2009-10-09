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

package net.sf.dvstar.transmission.protocol;

import java.util.Comparator;

public class TorrentsComparator implements Comparator<Torrent>, ProtocolConstants {

    TorrentSortColumns sortBy;

    public TorrentsComparator(TorrentSortColumns sortBy) {
        this.sortBy = sortBy;
    }


    public TorrentsComparator(int column) {
        this.sortBy = TorrentsTableModel.columnIndexTosortBy[column];
    }


    @Override
    public int compare(Torrent tor1, Torrent tor2) {
            int ret = 0;
            switch (sortBy) {
                case Id:
                    return tor1.getId().compareTo(tor2.getId());
                case Status:
                    return tor1.getStatusDesc().compareTo(tor2.getStatusDesc());
                case Name:
                    return tor1.getTorrentName().compareTo(tor2.getTorrentName());
                case Size:
                    return tor1.getSize().compareTo(tor2.getSize());
                case Upload:
                    return tor1.getUpload().compareTo(tor2.getUpload());
                case DoneProgress:
                    return tor1.getDoneProgress().compareTo(tor2.getDoneProgress());
                case RateDn:
                    return tor1.getRateDn().compareTo(tor2.getRateDn());
                case RateUp:
                    return tor1.getRateUp().compareTo(tor2.getRateUp());
                case Leechs:
                    return tor1.getLeechs().compareTo(tor2.getLeechs());
                case Seeds:
                    return tor1.getSeeds().compareTo(tor2.getSeeds());
                default:
                    return 0;
            }
    }
}
