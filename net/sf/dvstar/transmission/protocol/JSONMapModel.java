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

import java.util.ArrayList;
import java.util.List;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import net.sf.dvstar.transmission.protocol.Torrent.Files;
import net.sf.dvstar.transmission.protocol.Torrent.Peers;
import net.sf.dvstar.transmission.utils.LocalSettiingsConstants;

/**
 *
 * @author dstarzhynskyi
 */
public abstract class JSONMapModel implements TableModel, ProtocolConstants, LocalSettiingsConstants {

    private final Torrent torrent;
    private final TransmissionManager manager;
    private final ColumnsDescriptor columnsDescriptor;

    public ColumnsDescriptor getColumnsDescriptor() {
        return columnsDescriptor;
    }

    public JSONMapModel(TransmissionManager manager, Torrent torrent, ColumnsDescriptor columnsDescriptor) {
        this.manager= manager;
        this.torrent = torrent;
        this.columnsDescriptor = columnsDescriptor;
    }

    @Override
    public int getColumnCount() {
        return columnsDescriptor.getColumnCount();
    }

    @Override
    public String getColumnName(int columnIndex) {
        return columnsDescriptor.getColumn(columnIndex).columnName;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
    }

    @Override
    public void addTableModelListener(TableModelListener l) {
    }

    @Override
    public void removeTableModelListener(TableModelListener l) {
    }


    public static class ColumnDescriptor {
        int    columnIndex;
        Class<?> columnType;
        String columnName;
        String columnField;

        public ColumnDescriptor(
                int columnIndex,
                Class<?> columnType,
                String columnName,
                String columnIdent)
        {
            this.columnIndex = columnIndex;
            this.columnType  = columnType;
            this.columnField = columnIdent;
            this.columnName  = columnName;
        }
    }

    public static class ColumnsDescriptor {
        List<ColumnDescriptor> columns = new ArrayList();

        public ColumnsDescriptor(ColumnDescriptor columnDescriptor[]){
            columns = new ArrayList<ColumnDescriptor>();
            if(columnDescriptor!=null){
                for(int i=0;i<columnDescriptor.length;i++){
                    addColumn(columnDescriptor[i]);
                }
            }
        }

        public void addColumn(ColumnDescriptor column){
            columns.add(column);
        }

        public ColumnDescriptor getColumn(int index){
            return columns.get(index);
        }

        public int getColumnCount(){
            return columns.size();
        }

        public ColumnDescriptor getColumn(String key) throws TorrentsCommonException{
            ColumnDescriptor ret = null;
            int index = findIndex( key );
            if( index >=0 ) ret = columns.get(index);
            else throw new TorrentsCommonException( "Key ["+key+"]not found !");
            return ret;
        }

        private int findIndex(String key) {
            return -1;
        }

    }


    //--------------------------------------------------------------------------

    public static class JSONMapModelFiles extends JSONMapModel {

        List<Files> filesList;

        public JSONMapModelFiles(TransmissionManager manager, Torrent torrent, ColumnsDescriptor columnsDescriptor) {
            super(manager, torrent, columnsDescriptor);
            filesList = torrent.getFilesList();
        }

        @Override
        public int getRowCount() {
            int ret = 0; 
            if(filesList!=null) {
                ret = filesList.size();
            }
            return ret;
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return String.class;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Files files = filesList.get(rowIndex);
            Object ret = files.getValue( getColumnsDescriptor().getColumn(columnIndex).columnField );
            return ret;
        }
    }

    //--------------------------------------------------------------------------

    public static class JSONMapModelPeers extends JSONMapModel {
        List<Peers> peersList;

        public JSONMapModelPeers(TransmissionManager manager, Torrent torrent, ColumnsDescriptor columnsDescriptor) {
            super(manager, torrent, columnsDescriptor);
            peersList = torrent.getPeersList();
        }

        @Override
        public int getRowCount() {
            int ret = 0;
            if(peersList!=null) {
                ret = peersList.size();
            }
            return ret;
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return String.class;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Peers files = peersList.get(rowIndex);
            //Object ret = files.getValue(columns[columnIndex]);
            Object ret = files.getValue( getColumnsDescriptor().getColumn(columnIndex).columnField );
            return ret;
        }
    }
}
