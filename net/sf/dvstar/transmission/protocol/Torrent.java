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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import net.sf.dvstar.transmission.utils.Tools;
import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import org.apache.commons.codec.binary.Base64;

/**
 *
 * @author dstarzhynskyi
 */
public class Torrent implements ProtocolConstants {

    private JSONObject jsonTorrent = null;
    //private Map mapTorrent = null;
    private byte[] piecesArray;
    private int piecesCount;
    private int statusCode = 0;
    private int id = 0;
    private boolean hasError = false;
    private String errorString;
    private ResourceBundle resourceBundle = null;
    private String torrentName;

    public Torrent(JSONObject jsonTorrent) {
        this.jsonTorrent = jsonTorrent;
        parseResult();
    }

    /**
     * @todo implement method !!
     * @return
     */
    public boolean isHasError() {
        return hasError;
    }

    private void parseResult() {

        String val;

        val = jsonTorrent.getString(FIELD_ID);
        id = Integer.parseInt(val);

        val = jsonTorrent.getString(FIELD_NAME);
        torrentName = val;

        val = jsonTorrent.getString(FIELD_STATUS);
        statusCode = Integer.parseInt(val);


        val = jsonTorrent.getString(FIELD_PIECECOUNT);
        piecesCount = Integer.parseInt(val);

        val = jsonTorrent.getString(FIELD_ERRORSTRING);
        errorString = val;
        if (errorString.length() > 0) {
            hasError = true;
        }

        val = safeJsonGetString(jsonTorrent, FIELD_PIECES);
        if (val != null) {
            piecesArray = Base64.decodeBase64(val.getBytes());
        }

        //val = jsonTorrent.getString(ProtocolConstants.FIELD_FILES);
        val = safeJsonGetString(jsonTorrent, FIELD_FILES);
        if (val != null) {
            filesList = new ArrayList<Files>();
            JSONArray filesJsonArray = jsonTorrent.getJSONArray(FIELD_FILES);
            for (int i = 0; i < filesJsonArray.size(); i++) {
                Files files = new Files();
                JSONObject obj = filesJsonArray.getJSONObject(i);
                String key = "";
                String fld = "";
                for (int j = 0; j < Files.fields.length; j++) {
                    key = Files.fields[j];
                    fld = getValueFrom(obj, key).toString();
                    //Object ofd = obj.get(key);
                    //String fld = ofd != null ? ofd.toString() : "";
                    files.setValue(key, fld);
                }

                getFilesList().add(files);
            }
        }

        //val = jsonTorrent.getString(ProtocolConstants.FIELD_PEERS);
        val = safeJsonGetString(jsonTorrent, FIELD_PEERS);
        if (val != null) {
            peersList = new ArrayList<Peers>();
            JSONArray filesJsonArray = jsonTorrent.getJSONArray(FIELD_PEERS);

            for (int i = 0; i < filesJsonArray.size(); i++) {
                Peers peers = new Peers();
                JSONObject obj = filesJsonArray.getJSONObject(i);
                String key = "";
                String fld = "";
                try {
                    for (int j = 0; j < Peers.fields.length; j++) {
                        key = Peers.fields[j];
                        fld = getValueFrom(obj, key).toString();
                        peers.setValue(key, fld);
                    }
                } catch (Exception ex) {
                    System.err.println("fail to get " + key);
                    ex.printStackTrace();
                }

                getPeersList().add(peers);
            }
        }


    }



    public String getDownloadDir() {
        return getValue(FIELD_DOWNLOADDIR).toString();
    }

    public Object getValueFrom(JSONObject jobj, String key) {
        Object ret = "";

        if (key.equals(FIELD_ID)) {
            ret = new Integer(jobj.getString(key));
        } else if (key.equals(FIELD_DONEPROGRESS)) {
            ret = Tools.calcPercentage(getHaveTotal(), getSize()) + " %";
            if( getStatus() == STATUS_CHECK)
                ret = getRecheckPercentage() + " %";
        } else if (key.equals(FIELD_STATUS)) {
            ret = getStatusDesc(jobj.getInt(key));
        } else if (key.equals(FIELD_TOTALSIZE)) {
            ret = Tools.getFileSize((float) jobj.getDouble(key));
        } else if (key.equals(FIELD_SEEDERS)) {
            int i = jobj.getInt(key);
            ret = i >= 0 ? i : 0;
        } else if (key.equals(FIELD_LEECHERS)) {
            int i = jobj.getInt(key);
            ret = i >= 0 ? i : 0;
        } else if (key.equals(FIELD_UPLOADEDEVER)) {
            ret = Tools.getFileSize((float) jobj.getDouble(key));
        } else if (key.equals(FIELD_LENGTH_VIS)) {
            ret = Tools.getFileSize((float) jobj.getDouble(FIELD_LENGTH));
        } else if (key.equals(FIELD_BYTESCOMPLETED_VIS)) {
            ret = Tools.getFileSize((float) jobj.getDouble(FIELD_BYTESCOMPLETED));
        } else if (key.equals(FIELD_UPLOAD_SPEED_VIS)) {
            ret = Tools.getVisibleSpeed(jobj.getInt(FIELD_UPLOAD_SPEED));
        } else if (key.equals(FIELD_DOWNLOAD_SPEED_VIS)) {
            ret = Tools.getVisibleSpeed(jobj.getInt(FIELD_DOWNLOAD_SPEED));
        } else if (key.equals(FIELD_RATEDOWNLOAD_VIS)) {
            ret = Tools.getVisibleSpeed(jobj.getInt(FIELD_RATEDOWNLOAD));
        } else if (key.equals(FIELD_RATEUPLOAD_VIS)) {
            ret = Tools.getVisibleSpeed(jobj.getInt(FIELD_RATEUPLOAD));
        } else if (key.equals(FIELD_RATETOCLIENT_VIS)) {
            ret = Tools.getVisibleSpeed(jobj.getInt(FIELD_RATETOCLIENT));
        } else if (key.equals(FIELD_RATETOPEER_VIS)) {
            ret = Tools.getVisibleSpeed(jobj.getInt(FIELD_RATETOPEER));
        } else if (key.equals(FIELD_UPLOADEDEVER_VIS)) {
            ret = Tools.getFileSize((float) jobj.getDouble(FIELD_UPLOADEDEVER));
        } else if (key.equals(FIELD_COUNTRY)) {
            ret = getCountryByIP(jobj.getString(FIELD_ADDRESS));
        } else if (key.equals(FIELD_TYPE)) {
            ret = getFileType(jobj.getString(FIELD_NAME));
        } else if (key.equals(FIELD_RATETOCLIENT_VIS)) {
            ret = getCountryByIP(jobj.getString(FIELD_RATETOCLIENT));
        } else if (key.equals(FIELD_RATETOPEER_VIS)) {
            ret = getCountryByIP(jobj.getString(FIELD_RATETOPEER));
        } else if (key.equals(FIELD_PROGRESS_VIS)) {
            ret = Tools.formatDouble(jobj.getDouble(FIELD_PROGRESS)*100.0, 2) + " %";
        } else if (key.equals(FIELD_FILE_PROGRESS)){
            ret = Tools.calcPercentage(jobj.getLong(FIELD_BYTESCOMPLETED ), jobj.getLong(FIELD_LENGTH)) + " %";
        } else {
            ret = jobj.getString(key);
        }

        return ret;
    }

    public Object getValue(String key) {
        Object ret = "";

        ret = getValueFrom(jsonTorrent, key);

        return ret;
    }

    /**
     * @return the piecesArray
     */
    public byte[] getPiecesArray() {
        return piecesArray;
    }

    /**
     * @return the pieceCount
     */
    public int getPiecesCount() {
        return piecesCount;
    }

    public void parseResult(JSONObject result, int id) {
        if(result==null) {
            return;
        }
        JSONObject arguments = result.getJSONObject("arguments");
        JSONArray jsonArray = arguments.getJSONArray("torrents");

        //System.out.println("[parseResult][size]=" + jsonArray.size());

        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject torrentObject = jsonArray.getJSONObject(i);
            int nid = Integer.parseInt(torrentObject.get(FIELD_ID).toString());
            if (nid == id) {
                jsonTorrent = torrentObject;
                parseResult();
            }
        }
    }

    private List<Files> filesList;
    private List<Peers> peersList;

    /**
     * @return the filesList
     */
    public List<Files> getFilesList() {
        return filesList;
    }

    /**
     * @return the peersList
     */
    public List<Peers> getPeersList() {
        return peersList;
    }

    private String safeJsonGetString(JSONObject jsonTorrent, String FIELD_PIECES) {
        String val = null;
        try {
            val = jsonTorrent.getString(ProtocolConstants.FIELD_PIECES);
        } catch (JSONException ex) {
            val = null;
        }
        return val;
    }

    private String getStatusDesc(int status) {
        String ret = "";
        switch (status) {
            case STATUS_CHECK: {
                ret = resourceBundle != null ? resourceBundle.getString(STATUS_CHECK_DESC) : STATUS_CHECK_DESC;
            }
            break;
            case STATUS_CHECK_WAIT: {
                ret = resourceBundle != null ? resourceBundle.getString(STATUS_CHECK_WAIT_DESC) : STATUS_CHECK_WAIT_DESC;
            }
            break;
            case STATUS_DOWNLOAD: {
                ret = resourceBundle != null ? resourceBundle.getString(STATUS_DOWNLOAD_DESC) : STATUS_DOWNLOAD_DESC;
            }
            break;
            case STATUS_PAUSED: {
                ret = resourceBundle != null ? resourceBundle.getString(STATUS_PAUSED_DESC) : STATUS_PAUSED_DESC;
            }
            break;
            case STATUS_SEED: {
                ret = resourceBundle != null ? resourceBundle.getString(STATUS_SEED_DESC) : STATUS_SEED_DESC;
            }
            break;

        }
        return ret;
    }

    //----------------------- Torrent getters ----------------------------------
    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    public Integer getStatus() {
        return jsonTorrent.getInt(FIELD_STATUS);
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public String getTorrentName() {
        return torrentName;
    }


    public double getRecheckPercentage() {
        double ret = jsonTorrent.getDouble(FIELD_RECHECKPROGRESS);
        ret = Tools.calcPercentage( ret, 0);
        return ret;
    }

    public Double getDoneProgress() {
        double ret = 0.00;
        //ret = Tools.calcPercentage(jsonTorrent.getLong(FIELD_BYTESCOMPLETED ), jsonTorrent.getLong(FIELD_LENGTH));
        ret = Tools.calcPercentage(getHaveTotal(), getSize());
        if( getStatus() == STATUS_CHECK){
            ret = getRecheckPercentage();
        }
        return ret;
    }



    public Long getHaveTotal() {
        long ret =
                Tools.toLong(jsonTorrent.getString(FIELD_HAVEVALID)) +
                Tools.toLong(jsonTorrent.getString(FIELD_HAVEUNCHECKED));
        return ret;
    }

    public Long getHaveValid() {
        return Tools.toLong(jsonTorrent.getString(FIELD_HAVEVALID));
    }

    public Long getSize() {
        Long ret = Tools.toLong(jsonTorrent.getString(FIELD_SIZEWHENDONE));
        return ret;
    }

    /**
     * @return the errorString
     */
    public String getErrorString() {
        return errorString;
    }

    public String getStatusDesc() {
        return getStatusDesc(getStatus());
    }

    public Long getUpload() {
        Long ret = Tools.toLong(jsonTorrent.getString(FIELD_UPLOADEDEVER));
        return ret;
    }
/*
    public Double getDone() {
        Double ret = Tools.calcPercentage(getHaveTotal(), getSize());
        return ret;
    }
*/
    public Double getRateDn() {
        Double ret = Tools.toDouble(jsonTorrent.getString(FIELD_RATEDOWNLOAD));
        return ret;
    }

    public Double getRateUp() {
        Double ret = Tools.toDouble(jsonTorrent.getString(FIELD_RATEUPLOAD));
        return ret;
    }

    public Long getLeechs() {
        Long ret = Tools.toLong(jsonTorrent.getString(FIELD_LEECHERS));
        return ret;
    }

    public Long getSeeds() {
        Long ret = Tools.toLong(jsonTorrent.getString(FIELD_SEEDERS));
        return ret;
    }

    private String getCountryByIP(String address) {
        String ret = address;

        return ret;
    }

    private Object getFileType(String string) {
        String ret="-";

        return ret;
    }

    //----------------------- Torrent getters ----------------------------------
    public static class MapContainer {

        protected Map values;

        public void setValue(String key, String val) {
            values.put(key, val);
        }

        public String getValue(String key) {
            String ret = "";
            ret = (String) values.get(key);
            return ret;
        }
    }

    //--------------------------------------------------------------------------
    public static class Files extends MapContainer {

        public static String names[] = {
            "Path",
            "Type",
            "Size",
            "Complete",
            "Done"};
        public static String fields[] = {
            FIELD_NAME,
            FIELD_TYPE,
            FIELD_LENGTH_VIS,        // VISUAL
            FIELD_BYTESCOMPLETED_VIS,// VISUAL
            FIELD_FILE_PROGRESS // VISUAL
        };

        public Files() {
            super();
            super.values = new HashMap();
        }
    }

    //--------------------------------------------------------------------------
    public static class Peers extends MapContainer {

        //IP,Country,Flags,Client,Port,Progress,Download,Upload
        public static String names[] = {
            "Address",
            "Country",
            "Flags",
            "Client",
            "Port",
            "Progress",
            "Down Rate",
            "UP Rate"
        };
        public static String fields[] = {
            FIELD_ADDRESS,
            FIELD_COUNTRY,
            FIELD_FLAGSTR,
            FIELD_CLIENTNAME,
            FIELD_PORT,
            FIELD_PROGRESS_VIS, // VISUAL
            FIELD_RATETOCLIENT_VIS, // VISUAL
            FIELD_RATETOPEER_VIS // VISUAL
        //            "clientIsChoked",
        //            "clientIsInterested",
        //            "isDownloadingFrom",
        //            "isEncrypted",
        //            "isIncoming",
        //            "isUploadingTo",
        //            "peerIsChoked",
        //            "peerIsInterested",
        };

        public Peers() {
            super();
            super.values = new HashMap();
        }
    }
    //--------------------------------------------------------------------------
} //~

