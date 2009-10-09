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
import javax.swing.table.TableRowSorter;
import javax.swing.table.TableModel;

/**
 *
 * @author dstarzhynskyi
 */
public class TorrentListRowSorter extends TableRowSorter {
    private TorrentsTableModel tableModel = null;

    public TorrentListRowSorter(TorrentsTableModel model) {
        this.tableModel = model;
        setModelWrapper( new TorrentModelWrapper() );
    }

    public void setModel(TorrentsTableModel model) {
        tableModel = model;
        setModelWrapper(new TorrentModelWrapper());
    }

    @Override
    public Comparator<?> getComparator(int column) {
        checkColumn(column);
        return new TorrentsComparator(column);
    }

    private void checkColumn(int column) {
        if (column < 0 || column >= getModelWrapper().getColumnCount()) {
            throw new IndexOutOfBoundsException(
                    "column beyond range of TableModel");
        }
    }

    private class TorrentModelWrapper extends ModelWrapper {

        @Override
        public TableModel getModel() {
            return tableModel;
        }

        @Override
        public int getColumnCount() {
            return (tableModel == null) ? 0 : tableModel.getColumnCount();
        }

        @Override
        public int getRowCount() {
            return (tableModel == null) ? 0 : tableModel.getRowCount();
        }

        @Override
        public Object getValueAt(int row, int column) {
            return tableModel.getTableDataTorrents().get(row);
        }

        @Override
        public Integer getIdentifier(int row) {
            return row;
        }

        @Override
        public String getStringValueAt(int row, int column){
            String ret = null;

            ret = tableModel.getTableDataTorrents().get(row).getValue( TorrentsTableModel.columnIndexNames[column]).toString();

            return ret;
        }


    }

}
