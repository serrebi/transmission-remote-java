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

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Rectangle;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import net.sf.dvstar.transmission.utils.ConfigStorage;
import net.sf.dvstar.transmission.utils.LocalSettiingsConstants;
import net.sf.dvstar.transmission.utils.Tools;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.jdesktop.application.ResourceMap;

/**
 *
 * @author dstarzhynskyi
 */
public class TorrentsTableModel implements TableModel, ProtocolConstants, LocalSettiingsConstants {

    private List<Torrent> tableDataTorrents;
    JSONObject result;
    private org.jdesktop.application.ResourceMap resourceMap;

    public static final double[] prefWidths = new double[]{
        2, 48, 10, 10, 10, 5, 5, 5, 5, 5
    };

    private String[] columnNames = null;
    /*
    {
        "№", "Name", "Size", "Progress", "Status", "Seed", "Leech", "Down Speed", "Up Speed","Upload"
    };
    */
    public static final String[] columnIndexNames = {
        FIELD_ID, FIELD_NAME, 
        FIELD_TOTALSIZE, FIELD_DONEPROGRESS,
        FIELD_STATUS, 
        FIELD_SEEDERS, FIELD_LEECHERS,
        FIELD_RATEDOWNLOAD_VIS,FIELD_RATEUPLOAD_VIS,
        FIELD_UPLOADEDEVER_VIS
    };


    public static final TorrentSortColumns[] columnIndexTosortBy = {
        TorrentSortColumns.Id,
        TorrentSortColumns.Name,
        TorrentSortColumns.Size,
        TorrentSortColumns.DoneProgress,
        TorrentSortColumns.Status,
        TorrentSortColumns.Seeds,
        TorrentSortColumns.Leechs,
        TorrentSortColumns.RateDn,
        TorrentSortColumns.RateUp,
        TorrentSortColumns.Upload
    };


    public TorrentsTableModel(JSONObject result, TransmissionManager manager) {
        this.resourceMap = manager.getGlobalResourceMap();
        fillLocaleColumnNames();
        if (result != null) {
            this.result = result;
            tableDataTorrents = new ArrayList();
            parseResult();
        } else {
            tableDataTorrents = null;
        }
    }

    public TorrentsTableModel(TransmissionManager manager) {
        this(null, manager);
    }

    @Override
    public int getRowCount() {
        int ret = 0;
        if (getTableDataTorrents() != null) {
            ret = getTableDataTorrents().size();
        }
        return ret;
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int columnIndex) {
        String ret = columnNames[columnIndex];
        return ret;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (columnIndex == 0 || columnIndex == 7 || columnIndex == 8) {
            return Integer.class;
        }

        return String.class;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Object ret = "";
        if (getTableDataTorrents() != null) {
            if (rowIndex < getTableDataTorrents().size()) {

                Torrent torrent = getTableDataTorrents().get(rowIndex);

                ret = torrent.getValue( columnIndexNames[columnIndex]);

            }
        }
        return ret;
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

    /**
     * @todo make Exceprions on error
     *
     */
    private void parseResult() {
        JSONObject arguments = result.getJSONObject("arguments");
        JSONArray jsonArray = arguments.getJSONArray("torrents");

//System.out.println("[parseResult][size]=" + jsonArray.size());

        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject torrentObject = jsonArray.getJSONObject(i);

            Torrent torrentItem = new Torrent(torrentObject);
            getTableDataTorrents().add(torrentItem);

        }
//System.out.println("[parseResult][load]=" + getTableDataTorrents().size());
    }

    public static void setPreferredColumnWidths(JTable table) {
        setPreferredColumnWidths(table, prefWidths);
    }

    public static void setPreferredColumnWidths(JTable table, double[] percentages) {
        Dimension tableDim = table.getPreferredSize();

        double total = 0;
        for (int i = 0; i < table.getColumnModel().getColumnCount(); i++) {
            if (table.getColumnModel().getColumn(i).getResizable()) {
                total += percentages[i];
            }
        }
//System.out.print("[Table] ");
        for (int i = 0; i < table.getColumnModel().getColumnCount(); i++) {
            TableColumn column = table.getColumnModel().getColumn(i);
            int width = (int) (tableDim.width * (percentages[i] / total));
            if (column.getResizable()) {
                column.setPreferredWidth(width);
            }
//System.out.print("[" + i + "][" + column.getResizable() + "][" + width + "]");
        }
//System.out.println();
    }

    boolean isCellChanged(int row, int column) {
        return false;
    }

    /**
     * @return the tableDataTorrents
     */
    public List<Torrent> getTableDataTorrents() {
        return tableDataTorrents;
    }

    private void fillLocaleColumnNames() {

        String namesList = resourceMap.getString("tblTorrentList.columnModel.column.titles");
        if(namesList!=null) {
            columnNames = Tools.getStringArray(namesList, ",");
        }
        
    }

    public static class CustomRenderer extends DefaultTableCellRenderer {

        DateFormat dateFormatter = SimpleDateFormat.getDateInstance(DateFormat.MEDIUM);
        NumberFormat numberFormatter = NumberFormat.getInstance();
        Color colorOdd = new Color(0xe1e1e1);
        private final TorrentsTableModel loader;
        private boolean colorStatus = true;

        /**
         *
         * @param loader
         * @param noColor no color for rows by status
         */
        public CustomRenderer(TorrentsTableModel loader) {
            this(loader, true);
        }

        public CustomRenderer(TorrentsTableModel loader, boolean colorStatus) {
            this.loader = loader;
            this.colorStatus = colorStatus;
            prepareData();
        }

        public static Rectangle getColumnBounds(JTable table, int column) {
            //checkColumn(table, column);

            Rectangle result = table.getCellRect(-1, column, true);
            Insets i = table.getInsets();

            result.y = i.top;
            result.height = table.getVisibleRect().height;//   table.getHeight() - i.top - i.bottom;

            return result;
        }

        @Override
        public Component getTableCellRendererComponent(
                JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {

            super.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, column);

            Component comp = this;

            //  Code for data formatting
            Border selected = new LineBorder(Color.blue);


            setHorizontalAlignment(SwingConstants.LEFT);

            
            if ( table.getColumnClass(column).getName().equals( JProgressBar.class.getName() ) )   {

            }
            
            if ( column == 3 && table.getModel() instanceof TorrentsTableModel)   {
                comp = new JProgressBar(0,100);
                ((JProgressBar)comp).setStringPainted(true);
                ((JProgressBar)comp).setForeground(Color.green);
                ((JProgressBar)comp).setBorder( new EmptyBorder(0,0,0,0) );
                TorrentsTableModel model=((TorrentsTableModel)table.getModel());
                Torrent torrent = model.getTableDataTorrents().get( table.convertRowIndexToModel(row) );
                ((JProgressBar)comp).setValue((int) torrent.getDoneProgress().doubleValue() );
            }
            

            /*
            if (value instanceof Date) {
            setText(dateFormatter.format((Date) value));
            }

            if (value instanceof Number) {
            setHorizontalAlignment(SwingConstants.RIGHT);

            if (value instanceof Double) {
            setText(numberFormatter.format(((Number) value).floatValue()));
            }
            }
             */
            //  Code for highlighting

            if (!isSelected) {
                //    String type = (String) table.getModel().getValueAt(row, 0);
                setBackground(row % 2 == 0 ? null : colorOdd);
            }

            /*
            System.out.println("TR_STATUS_CHECK_WAIT="+TR_STATUS_CHECK_WAIT);
            System.out.println("TR_STATUS_CHECK="+TR_STATUS_CHECK);
            System.out.println("TR_STATUS_DOWNLOAD="+TR_STATUS_DOWNLOAD);
            System.out.println("TR_STATUS_SEED="+TR_STATUS_SEED);
            System.out.println("TR_STATUS_STOPPED="+TR_STATUS_STOPPED);
             */

            if (colorStatus) {

                //int mrow = table.getRowSorter().convertRowIndexToModel(row);
                int mrow = table.convertRowIndexToModel(row);
                //if(table.getRowSorter()!=null) {
                //    mrow = table.getRowSorter().convertRowIndexToModel(row);
                //}
                
                int statusCode = loader.getTableDataTorrents().get(mrow).getStatusCode();
                boolean hasError = loader.getTableDataTorrents().get(mrow).isHasError();

                if (statusCode == TR_STATUS_PAUSED) {
                    setForeground(colCONF_CLI_COL_STATUS_STOPPED);
                } else if (statusCode == TR_STATUS_DOWNLOAD) {
                    setForeground(colCONF_CLI_COL_STATUS_DOWNLOAD);
                } else if (statusCode == TR_STATUS_CHECK_WAIT) {
                    setForeground(colCONF_CLI_COL_STATUS_CHECK_WAIT);
                } else if (statusCode == TR_STATUS_CHECK) {
                    setForeground(colCONF_CLI_COL_STATUS_CHECK);
                } else if (statusCode == TR_STATUS_SEED) {
                    setForeground(colCONF_CLI_COL_STATUS_SEED);
                } else {
                    setForeground(Color.black);
                }

                if (hasError) {
                        setForeground(colCONF_CLI_COL_STATUS_ERROR);
                }

            }

            /*
            if (loader.isCellChanged(row, column)) {
            setForeground(Color.red);
            } else {
            setForeground(Color.black);
            }
             */

            /*
            if(table.isColumnSelected(column)){
            setBackground(colorOdd);
            //System.out.println("----getColumnBounds(table, column)"+getColumnBounds(table, column));
            table.repaint(   getColumnBounds(table, column)   );
            }
             */

            if (
                    table.isRowSelected(row)
                    //&& table.isColumnSelected(column)
               ) {
                setBorder( selected );
            }

            return comp;
        }
        
        Color colCONF_CLI_COL_STATUS_CHECK_WAIT = Color.darkGray;
        Color colCONF_CLI_COL_STATUS_CHECK = Color.GRAY;
        Color colCONF_CLI_COL_STATUS_DOWNLOAD = Color.GREEN;
        Color colCONF_CLI_COL_STATUS_SEED = Color.BLUE;
        Color colCONF_CLI_COL_STATUS_STOPPED = Color.ORANGE;
        Color colCONF_CLI_COL_STATUS_ERROR = Color.RED;

        private void prepareData() {
            
            ConfigStorage configStorage = new ConfigStorage();
            configStorage.loadConfig();
            Properties prop = configStorage.getStoredConfig();

            colCONF_CLI_COL_STATUS_CHECK_WAIT = configStorage.propertiesManager(CONF_CLI_COL_STATUS_CHECK_WAIT, prop, colCONF_CLI_COL_STATUS_CHECK_WAIT, DIR_SET);
            colCONF_CLI_COL_STATUS_CHECK = configStorage.propertiesManager(CONF_CLI_COL_STATUS_CHECK, prop, colCONF_CLI_COL_STATUS_CHECK, DIR_SET);
            colCONF_CLI_COL_STATUS_DOWNLOAD = configStorage.propertiesManager(CONF_CLI_COL_STATUS_DOWNLOAD, prop, colCONF_CLI_COL_STATUS_DOWNLOAD, DIR_SET);
            colCONF_CLI_COL_STATUS_SEED = configStorage.propertiesManager(CONF_CLI_COL_STATUS_SEED, prop, colCONF_CLI_COL_STATUS_SEED, DIR_SET);
            colCONF_CLI_COL_STATUS_STOPPED = configStorage.propertiesManager(CONF_CLI_COL_STATUS_STOPPED, prop, colCONF_CLI_COL_STATUS_STOPPED, DIR_SET);

        }
    }
}


