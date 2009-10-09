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

package net.sf.dvstar.transmission.utils;

import java.awt.Color;
import java.util.List;
import net.sf.dvstar.transmission.protocol.ProtocolConstants;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPasswordField;
import javax.swing.JTextField;


/**
 *
 * @author dstarzhynskyi
 */
public class ConfigStorage implements ProtocolConstants, LocalSettiingsConstants {

    public static String APP_DESCRIPTION = "JTransmission";
    public static String dataFileName = System.getProperties().getProperty("user.home") + "/." + APP_DESCRIPTION + ".dat";
    public static String confFileName = System.getProperties().getProperty("user.home") + "/." + APP_DESCRIPTION + ".xml";
    public static String flogFileName = System.getProperties().getProperty("user.home") + "/." + APP_DESCRIPTION + ".log";
    private String defaultProfile = "";

    private Properties storedConfig = new Properties();
    private String propertiesPrefix = "";


    public ConfigStorage() {
    
    }

    public ConfigStorage(String defaultProfile) {
        setDefaultProfile(defaultProfile);
    }

    public String getDefaultProfile(){
        return defaultProfile;
    }

    public String getProfileProperty(String key){
        return getProfileProperty(key, false);
    }

    public String getProfileProperty(String key, boolean nopref){
        String ret = "";

        if(nopref) ret=storedConfig.getProperty(key);
        else ret = storedConfig.getProperty(propertiesPrefix+key);

        return ret;
    }



    public Properties getStoredConfig() {
        return storedConfig;
    }


    public String getFileConfigName() {
        return confFileName;
    }


    public boolean loadConfig() {
        boolean ret = false;

        File fileConfig = new File(getFileConfigName());
        if (fileConfig.exists()) {
            FileInputStream is;
            try {
                is = new FileInputStream(fileConfig);
                storedConfig.loadFromXML(is);
                if(storedConfig.getProperty(CONF_CLI_CURRENTPROFILE)!=null) {
                    setDefaultProfile(storedConfig.getProperty(CONF_CLI_CURRENTPROFILE));
                }
                ret = true;
            } catch (FileNotFoundException ex) {
                Logger.getLogger(ConfigStorage.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(ConfigStorage.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return (ret);
    }

    public boolean saveConfig(Properties configOptions) {
        boolean ret = false;
        this.storedConfig = configOptions;

        File fileConfig = new File(getFileConfigName());
        FileOutputStream os;
        try {
            os = new FileOutputStream(fileConfig);
            configOptions.storeToXML(os, "comment");
            ret = true;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ConfigStorage.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ConfigStorage.class.getName()).log(Level.SEVERE, null, ex);
        }

        return (ret);
    }

    public void setDefaultProfile(String defaultProfile) {
        this.defaultProfile = defaultProfile;
            if(defaultProfile != null && defaultProfile.length()>0)
                propertiesPrefix = defaultProfile + "_";
            else
                propertiesPrefix = "";
    }

    public List propertiesManager(String vkey, Properties prop, List vprofileList, int direction, boolean usePrefix) {
        String comp = "";
        List profileList = null;
        String key = vkey;
        if(usePrefix) key = propertiesPrefix + vkey;
        switch (direction) {
            case DIR_SET: {
                profileList = new ArrayList();
                comp = prop.getProperty(key, "");
                StringTokenizer token = new StringTokenizer(comp, ",");
                if (token.countTokens() > 0) {
                    while (token.hasMoreElements()) {
                        profileList.add(token.nextToken());
                    }
                } else {
                    profileList.add(comp);
                }
                break;
            }
            case DIR_GET: {
                if (vprofileList == null || vprofileList.size() == 0) {
                    return null;
                }
                for (int i = 0; i < vprofileList.size(); i++) {
                    comp += vprofileList.get(i).toString();
                    if (i < vprofileList.size() - 1) {
                        comp += ",";
                    }
                }
                prop.setProperty(key, comp);
                break;
            }
            default: {
                break;
            }
        }
        return profileList;
    }

    public String propertiesManager(String vkey, Properties prop, String vcomp, int direction, boolean usePrefix) {
        String key = vkey;
        if(usePrefix) key = propertiesPrefix + vkey;
        String comp = "";
        switch (direction) {
            case DIR_SET: {
                comp = prop.getProperty(key, "");
                break;
            }
            case DIR_GET: {
                prop.setProperty(key, vcomp);
                break;
            }
            default: {
                break;
            }
        }
        return comp;
    }

    public void propertiesManager(String vkey, Properties prop, JComponent comp, int direction) {
        propertiesManager(vkey, prop, comp, direction, true);
        return;
    }
    public void propertiesManager(String vkey, Properties prop, JComponent comp, int direction, boolean usePrefix) {
        String key = vkey;
        if(usePrefix) key = propertiesPrefix + vkey;
        switch (direction) {
            case DIR_SET: {
                if (comp instanceof JTextField) {
                    ((JTextField) (comp)).setText(prop.getProperty(key, "none"));
                }
                if (comp instanceof JPasswordField) {
                    ((JPasswordField) (comp)).setText(prop.getProperty(key, "none"));
                }
                if (comp instanceof JCheckBox) {
                    ((JCheckBox) (comp)).setSelected(Boolean.parseBoolean(prop.getProperty(key, "false")));
                }
                if (comp instanceof JComboBox) {
                    ((JComboBox) (comp)).setSelectedItem( prop.getProperty(key, "none") );
                }
                break;
            }
            case DIR_GET: {
                if (comp instanceof JTextField) {
                    prop.setProperty(key, ((JTextField) (comp)).getText());
                }
                if (comp instanceof JPasswordField) {
                    prop.setProperty(key, new String( ((JPasswordField) (comp)).getPassword()) );
                }
                if (comp instanceof JComboBox) {
                    prop.setProperty(key, ((JComboBox) (comp)).getSelectedItem().toString());
                }
                if (comp instanceof JCheckBox) {
                    prop.setProperty(key, "" + ((JCheckBox) (comp)).isSelected());
                }
                break;
            }
            default: {
                break;
            }
        }
    }

    public String getProperty(String key, String deflt, boolean noprofile) {
        String ret = getProfileProperty(key, noprofile);
        if(ret==null)ret=deflt;
        return ret;
    }

    public String getProperty(String key, String deflt) {
        return getProperty(key, deflt, false);
    }
    public Color propertiesManager(String vkey, Properties prop, Color vforeground, int direction) {

        Color foreground = vforeground;
        switch (direction) {
            case DIR_SET: {
                //Color.
                String vval = prop.getProperty(vkey);
                if(vval != null)
                    foreground = Color.decode( vval );
                else
                    foreground = null;
                break;
            }
            case DIR_GET: {
                prop.setProperty(vkey, ""+foreground.getRGB());
                break;
            }
            default: {
                break;
            }
    }
        return foreground;
    }

    public String getProperty(String key) {
        return storedConfig!=null?storedConfig.getProperty(key,""):"";
    }

    public void setProperty(String key, String val) {
        storedConfig.setProperty(key, val);
    }

    public void saveConfig() {
        saveConfig(storedConfig);
    }


}
