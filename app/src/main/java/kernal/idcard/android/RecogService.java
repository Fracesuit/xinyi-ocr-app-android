
package kernal.idcard.android;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.AssetManager;
import android.content.res.Resources.NotFoundException;
import android.net.Uri;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.os.storage.StorageManager;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.kernal.lisence.CDKey;
import com.kernal.lisence.CTelephoneInfo;
import com.kernal.lisence.Common;
import com.kernal.lisence.DateAuthFileOperate;
import com.kernal.lisence.DeviceFP;
import com.kernal.lisence.MachineCode;
import com.kernal.lisence.ModeAuthFileOperate;
import com.kernal.lisence.ModeAuthFileResult;
import com.kernal.lisence.ProcedureAuthOperate;
import com.kernal.lisence.SqliteHelperUtils;
import com.kernal.lisence.VersionAuthFileOperate;
import com.kernal.lisence.WintoneAuthOperateTools;
import com.kernal.lisence.WintoneLSCOperateTools;
import com.kernal.lisence.WintoneLSCXMLInformation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RecogService extends Service {
    private recogBinder rgBinder;
    private int ReturnInitIDCard = -1;
    public static int nTypeInitIDCard = 3;
    private int ReturnAuthority = -10015;
    private String mcode;
    private Common common = new Common();
    private String idcardpath;
    private String rootpath;
    private ResultMessage resultMessage;
    private Boolean isTF;
    private static Intent service;
    private ModeAuthFileResult mafr;
    private ModeAuthFileResult mafr1;
    public static boolean isRecogByPath = true;
    public static boolean isUpdateLSC = true;
    private static String query_old_lsc = "select * from old_lsc where _id=?";
    public static boolean isOnlyReadSDAuthmodeLSC = false;
    public static int nMainID = 2;
    public long recogTime;
    MathRandom mathRandom;
    public TelephonyManager telephonyManager;
    private String deviceId;
    private String androId;
    private String simId;
    CTelephoneInfo telephonyInfo;
    public String imeiSIM1;
    public String imeiSIM2;
    String[] params;
    public int[] initNSubID;
    private FileUtils myFileUtils;

    public RecogService() {
        this.idcardpath = this.common.getSDPath() + "/AndroidWT/IDCard/";
        this.rootpath = this.common.getSDPath() + "/AndroidWT";
        this.resultMessage = new ResultMessage();
        this.isTF = Boolean.valueOf(false);
        this.mafr = new ModeAuthFileResult();
        this.mafr1 = new ModeAuthFileResult();
        this.mathRandom = new MathRandom();
        this.params = new String[]{"1"};
        this.initNSubID = new int[1];
        this.myFileUtils = new FileUtils();
    }

    private int String2Int(String stri) {
        int nRet = 0;
        if(stri != null && !stri.equals("")) {
            try {
                nRet = Integer.parseInt(stri);
            } catch (Exception var4) {
                var4.printStackTrace();
            }
        }

        return nRet;
    }

    public String readtxt() throws IOException {
        Common common = new Common();
        String paths = common.getSDPath();
        if(paths != null && !paths.equals("")) {
            String fullpath = paths + "/AndroidWT/idcard.cfg";
            File file = new File(fullpath);
            if(!file.exists()) {
                return "";
            } else {
                FileReader fileReader = new FileReader(fullpath);
                BufferedReader br = new BufferedReader(fileReader);
                String str = "";

                for(String r = br.readLine(); r != null; r = br.readLine()) {
                    str = str + r;
                }

                br.close();
                fileReader.close();
                return str;
            }
        } else {
            return "";
        }
    }

    public void onCreate() {
        super.onCreate();
        boolean isSIM = false;
        this.ReturnInitIDCard = -10003;
        this.rgBinder = new recogBinder();
        String miwenxml = null;
        ModeAuthFileOperate mafo = new ModeAuthFileOperate();
        File file;
        if(isOnlyReadSDAuthmodeLSC) {
            miwenxml = this.readSDCardFile();
        } else {
            if(service == null) {
                service = new Intent(this, TimeService.class);
                this.startService(service);
            }

            miwenxml = this.readSDCardFile();
            if(miwenxml != null && isUpdateLSC) {
                this.mafr = mafo.ReadAuthFile(miwenxml);
                this.mafr1 = mafo.ReadAuthFile(this.readAssetFile(this.getAssets(), "authmode.lsc"));
                if(this.mafr1.isCheckPRJMode("11")) {
                    if(!this.mafr.prjmode_version[0].equals(this.mafr1.prjmode_version[0]) || !this.mafr.prjmode_closingdate[0].equals(this.mafr1.prjmode_closingdate[0]) || !this.mafr.prjmode_app_name[0].equals(this.mafr1.prjmode_app_name[0]) || !this.mafr.prjmode_company_name[0].equals(this.mafr1.prjmode_company_name[0]) || !this.mafr.prjmode_packagename[0].equals(this.mafr1.prjmode_packagename[0]) || !this.mafr.devcode.equals(this.mafr1.devcode)) {
                        file = new File(this.idcardpath + "authmode.lsc");
                        if(file.exists()) {
                            file.delete();
                            miwenxml = null;
                        }
                    }
                } else {
                    miwenxml = this.readAssetFile(this.getAssets(), "authmode.lsc");
                }
            }
        }

        if(miwenxml == null) {
            miwenxml = this.readAssetFile(this.getAssets(), "authmode.lsc");
        }

        this.mafr = new ModeAuthFileResult();
        mafo = new ModeAuthFileOperate();
        this.mafr = mafo.ReadAuthFile(miwenxml);
        if(!this.mafr.isCheckPRJMode("11") && !this.mafr.isTF("11")) {
            this.telephonyManager = (TelephonyManager)this.getSystemService("phone");
            StringBuilder sb = new StringBuilder();
            sb.append(this.telephonyManager.getDeviceId());
            this.deviceId = sb.toString();
            StringBuilder sb1 = new StringBuilder();
            sb1.append(Secure.getString(this.getContentResolver(), "android_id"));
            this.androId = sb1.toString();
            StringBuilder sb2 = new StringBuilder();
            sb2.append(this.telephonyManager.getSimSerialNumber());
            this.simId = sb2.toString();
            this.telephonyInfo = CTelephoneInfo.getInstance(this);
            this.telephonyInfo.setCTelephoneInfo();
            this.imeiSIM1 = this.telephonyInfo.getImeiSIM1();
            this.imeiSIM2 = this.telephonyInfo.getImeiSIM2();
        }

        String versionInitFilePatnString;
        String oldDateInitFilePath;
        File oldDateInitFile;
        File newDateInitFile;
        try {
            file = null;
            InputStream iStream = this.getAssets().open("idcardocr/version.txt");
            int size_is = iStream.available();
            byte[] byte_new = new byte[size_is];
            iStream.read(byte_new);
            iStream.close();
            String versionName = new String(byte_new);
            versionInitFilePatnString = "";
            String paths = this.common.getSDPath();
            if(paths != null && !paths.equals("")) {
                oldDateInitFilePath = this.idcardpath + "version.txt";
                File versionfile = new File(oldDateInitFilePath);
                if(versionfile.exists()) {
                    FileReader fileReader = new FileReader(oldDateInitFilePath);
                    BufferedReader br = new BufferedReader(fileReader);

                    for(String r = br.readLine(); r != null; r = br.readLine()) {
                        versionInitFilePatnString = versionInitFilePatnString + r;
                    }

                    br.close();
                    fileReader.close();
                }

                if(!versionName.equals(versionInitFilePatnString) && !isOnlyReadSDAuthmodeLSC) {
                    oldDateInitFile = new File(this.rootpath);
                    if(!oldDateInitFile.exists()) {
                        oldDateInitFile.mkdirs();
                    }

                    newDateInitFile = new File(this.idcardpath + "IDCARDMS/");
                    this.myFileUtils.deleteDirectory(this.idcardpath + "IDCARDMS/");
                    newDateInitFile.mkdirs();
                    if(AuthService.isHolder) {
                        this.copyHolderBigFile();
                    } else {
                        this.copyBigFile(0);
                    }

                    this.copyDataBase(0);
                    System.out.println("Copy assets files. versionName:" + versionName + " versiontxt:" + versionInitFilePatnString);
                }
            }
        } catch (Exception var30) {
            var30.printStackTrace();
        }

        if(miwenxml != null && this.mafr.isTF("11")) {
            this.isTF = Boolean.valueOf(true);
            IDCardAPI idCard = new IDCardAPI();
            DeviceFP deviceFP = new DeviceFP();
            String path = this.getStoragePath(this.getApplicationContext(), true);
            idCard.SetSDCardPath(path);
            this.ReturnInitIDCard = idCard.InitIDCardTF("82514896357821035649", nTypeInitIDCard, this.idcardpath, deviceFP);
            if(this.ReturnInitIDCard == 0) {
                if(isRecogByPath) {
                    idCard.SetParameter(0, nMainID);
                } else {
                    idCard.SetParameter(1, nMainID);
                }

                if(nMainID == 2) {
                    idCard.SetIDCardID(2, this.initNSubID);
                    idCard.AddIDCardID(3, this.initNSubID);
                } else {
                    idCard.SetIDCardID(nMainID, this.initNSubID);
                }

                this.ReturnAuthority = 0;
            }
        } else {
            Common common = new Common();
            String[] str = new String[]{"", "", "", "", "", "", "", "", "", "", "", ""};
            DeviceFP deviceFP = new DeviceFP();
            Boolean bool = Boolean.valueOf(false);
            this.telephonyManager = (TelephonyManager)this.getSystemService("phone");

            try {
                if(miwenxml != null && this.mafr.isSIM("11")) {
                    if(this.simId == null || this.simId.equals("") || this.simId.equals("null")) {
                        this.ReturnInitIDCard = -10501;
                        return;
                    }

                    this.deviceId = this.simId;
                    isSIM = true;
                }

                if(!this.mafr.isCheckPRJMode("11") && !this.mafr.isTF("11")) {
                    MachineCode machineCode = new MachineCode();
                    this.mcode = machineCode.MachineNO("1.0", this.deviceId, this.androId, this.simId);
                }

                if(miwenxml != null && this.mafr.isCheckPRJMode("11")) {
                    bool = Boolean.valueOf(true);
                    deviceFP.deviceid = "DeviceIdIsNull";
                } else {
                    versionInitFilePatnString = Environment.getExternalStorageDirectory() + "/AndroidWT/wtversioninit.lsc";
                    File versionInitFile = new File(versionInitFilePatnString);
                    if(versionInitFile.exists()) {
                        if(this.telephonyManager.getDeviceId() == null) {
                            bool = Boolean.valueOf(true);
                        } else if(this.telephonyManager.getDeviceId().equals(this.readInitFileString(versionInitFilePatnString))) {
                            deviceFP.deviceid = this.readInitFileString(versionInitFilePatnString);
                            bool = Boolean.valueOf(true);
                        }
                    } else {
                        oldDateInitFilePath = Environment.getExternalStorageDirectory() + "/wintone/idcarddateinit.lsc";
                        String newDateInitFilePath = Environment.getExternalStorageDirectory() + "/AndroidWT/wtdateinit.lsc";
                        oldDateInitFile = new File(oldDateInitFilePath);
                        newDateInitFile = new File(newDateInitFilePath);
                        if(!newDateInitFile.exists() && !oldDateInitFile.exists()) {
                            ProcedureAuthOperate pao = new ProcedureAuthOperate(this);
                            String path = pao.getOriginalAuthFilePathByProjectType("11");
                            String wintoneLSCFilePathString = common.getSDPath() + "/AndroidWT/wt.lsc";
                            File oldLscFile = new File(path);
                            File wintoneLSCFile = new File(wintoneLSCFilePathString);
                            CDKey cdKey = new CDKey();
                            if(oldLscFile.exists() || wintoneLSCFile.exists() || !wintoneLSCFile.exists()) {
                                if(oldLscFile.exists()) {
                                    str = pao.readOriginalAuthFileContent(path);
                                    bool = Boolean.valueOf(cdKey.checkjhm(str[2], this.mcode, str[1]));
                                    if(deviceFP.deviceid == null || deviceFP.deviceid.equals(" ") || deviceFP.deviceid.equals("null")) {
                                        if(this.deviceId.equals(str[3])) {
                                            deviceFP.deviceid = str[3];
                                        } else {
                                            bool = Boolean.valueOf(false);
                                        }
                                    }

                                    if(!bool.booleanValue() && str.length >= 8) {
                                        if(str[8] != null && str[7] != null) {
                                            bool = Boolean.valueOf(cdKey.checkjhm(str[8], this.mcode, str[7]));
                                        }

                                        if(deviceFP.deviceid == null || deviceFP.deviceid.equals(" ") || deviceFP.deviceid.equals("null")) {
                                            if(this.deviceId.equals(str[9])) {
                                                deviceFP.deviceid = str[9];
                                            } else {
                                                bool = Boolean.valueOf(false);
                                            }
                                        }
                                    }

                                    if(!bool.booleanValue() && (str[3].equals(this.imeiSIM1) || str[3].equals(this.imeiSIM2))) {
                                        bool = Boolean.valueOf(true);
                                    }
                                } else {
                                    SqliteHelperUtils sqliteHelperUtils = new SqliteHelperUtils(this.getApplicationContext(), "wt.db", 2);
                                    String oldwtlsc = sqliteHelperUtils.queryData(query_old_lsc, this.params);
                                    if(oldwtlsc != null && !oldwtlsc.equals("")) {
                                        try {
                                            common = new Common();
                                            String SysCertVersion = "wtversion5_5";
                                            String aesjie = common.getSrcPassword(oldwtlsc, SysCertVersion);
                                            String[] result = aesjie.split(",");
                                            bool = Boolean.valueOf(cdKey.checkjhm(result[2], this.mcode, result[1]));
                                            if(deviceFP.deviceid == null || deviceFP.deviceid.equals(" ") || deviceFP.deviceid.equals("null")) {
                                                if(this.deviceId.equals(str[3])) {
                                                    deviceFP.deviceid = str[3];
                                                } else {
                                                    bool = Boolean.valueOf(false);
                                                }
                                            }

                                            if(!bool.booleanValue() && str.length >= 8) {
                                                if(str[8] != null && str[7] != null) {
                                                    bool = Boolean.valueOf(cdKey.checkjhm(str[8], this.mcode, str[7]));
                                                }

                                                if(deviceFP.deviceid == null || deviceFP.deviceid.equals(" ") || deviceFP.deviceid.equals("null")) {
                                                    if(this.deviceId.equals(str[9])) {
                                                        deviceFP.deviceid = str[9];
                                                    } else {
                                                        bool = Boolean.valueOf(false);
                                                    }
                                                }
                                            }

                                            if(!bool.booleanValue() && (str[3].equals(this.imeiSIM1) || str[3].equals(this.imeiSIM2))) {
                                                bool = Boolean.valueOf(true);
                                            }

                                            File originalAuthRootFile = new File(Environment.getExternalStorageDirectory().toString() + "/wintone/");
                                            if(!originalAuthRootFile.exists()) {
                                                originalAuthRootFile.mkdirs();
                                            }

                                            FileOutputStream outStream = new FileOutputStream(path);
                                            outStream.write(oldwtlsc.getBytes());
                                            outStream.close();
                                        } catch (Exception var28) {
                                            var28.printStackTrace();
                                        }
                                    } else {
                                        WintoneLSCXMLInformation wlxi = null;
                                        TelephonyManager telephonyManager2 = (TelephonyManager)this.getSystemService("phone");
                                        if(telephonyManager2.getDeviceId() != null && !telephonyManager2.getDeviceId().equals("")) {
                                            wlxi = WintoneLSCOperateTools.ReadAuthFile(telephonyManager2.getDeviceId());
                                        } else {
                                            wlxi = WintoneLSCOperateTools.ReadAuthFile(Secure.getString(this.getContentResolver(), "android_id"));
                                        }

                                        if(wlxi != null) {
                                            bool = Boolean.valueOf(cdKey.checkjhm(wlxi.anoString, this.mcode, wlxi.snoString));
                                            if(deviceFP.deviceid == null || deviceFP.deviceid.equals(" ") || deviceFP.deviceid.equals("null")) {
                                                deviceFP.deviceid = wlxi.deviceIdString;
                                            }

                                            if(!bool.booleanValue() && (wlxi.deviceIdString.equals(this.imeiSIM1) || wlxi.deviceIdString.equals(this.imeiSIM2))) {
                                                bool = Boolean.valueOf(true);
                                            }
                                        }
                                    }
                                }
                            }
                        } else if(this.telephonyManager.getDeviceId() == null) {
                            bool = Boolean.valueOf(true);
                        } else if(oldDateInitFile.exists()) {
                            if(this.telephonyManager.getDeviceId().equals(this.readInitFileString(oldDateInitFilePath))) {
                                deviceFP.deviceid = this.readInitFileString(oldDateInitFilePath);
                                bool = Boolean.valueOf(true);
                            }
                        } else if(this.telephonyManager.getDeviceId().equals(this.readInitFileString(newDateInitFilePath))) {
                            deviceFP.deviceid = this.readInitFileString(newDateInitFilePath);
                            bool = Boolean.valueOf(true);
                        }
                    }
                }
            } catch (Exception var29) {
                bool = Boolean.valueOf(false);
                var29.printStackTrace();
            }

            try {
                if(bool.booleanValue()) {
                    if(!this.mafr.isCheckPRJMode("11") && this.telephonyManager.getDeviceId() == null) {
                        deviceFP.deviceid = "DeviceIdIsNull";
                    }

                    IDCardAPI IDCard = new IDCardAPI();
                    deviceFP.deviceid = "DeviceIdIsNull";
                    this.ReturnInitIDCard = IDCard.InitIDCard("82514896357821035649", nTypeInitIDCard, this.idcardpath, this.telephonyManager, deviceFP);
                    this.ReturnAuthority = 0;
                    if(this.ReturnInitIDCard == 0) {
                        if(isRecogByPath) {
                            IDCard.SetParameter(0, nMainID);
                        } else {
                            IDCard.SetParameter(1, nMainID);
                        }

                        if(nMainID == 2) {
                            IDCard.SetIDCardID(2, this.initNSubID);
                            IDCard.AddIDCardID(3, this.initNSubID);
                        } else {
                            IDCard.SetIDCardID(nMainID, this.initNSubID);
                        }
                    }
                }
            } catch (Exception var27) {
                var27.printStackTrace();
                this.ReturnInitIDCard = -10003;
            }
        }

    }

    public String readInitFileString(String filePathString) {
        String SysCertVersion = "wtversion5_5";
        String deviceidString = "";
        File dateInitFile = new File(filePathString);
        if(dateInitFile.exists()) {
            try {
                BufferedReader bfReader = new BufferedReader(new FileReader(dateInitFile));
                deviceidString = bfReader.readLine();
                bfReader.close();
                deviceidString = this.common.getSrcPassword(deviceidString, SysCertVersion);
            } catch (FileNotFoundException var6) {
                deviceidString = "";
                var6.printStackTrace();
            } catch (IOException var7) {
                deviceidString = "";
                var7.printStackTrace();
            } catch (Exception var8) {
                deviceidString = "";
                var8.printStackTrace();
            }
        }

        return deviceidString;
    }

    private String readAssetFile(AssetManager am, String filename) {
        String typeModeString = null;

        try {
            InputStream iStream = am.open("idcardocr/" + filename);
            int size_is = iStream.available();
            byte[] byte_new = new byte[size_is];
            iStream.read(byte_new);
            iStream.close();
            typeModeString = new String(byte_new);
        } catch (IOException var7) {
            typeModeString = null;
        } catch (Exception var8) {
            typeModeString = null;
        }

        return typeModeString;
    }

    public IBinder onBind(Intent intent) {
        return this.rgBinder;
    }

    public void copyBigFile(int sdkType) throws IOException {
        String[] thocr_sid;
        if(sdkType == 0) {
            thocr_sid = new String[]{"121.xml", "122.xml"};
            String[] pntWTPENPDA = new String[]{"pntWTPENPDA1.lib", "pntWTPENPDA2.lib", "pntWTPENPDA3.lib"};
            String[] thocr_vl_all = new String[]{"thocr_vl_all1.lib", "thocr_vl_all2.lib"};
            thocr_sid = new String[]{"thocr_sid1.lib", "thocr_sid2.lib"};
            this.mergeFile(thocr_sid, "12.xml");
            this.mergeFile(pntWTPENPDA, "pntWTPENPDA.lib");
            this.mergeFile(thocr_sid, "thocr_sid.lib");
            this.mergeFile(thocr_vl_all, "thocr_vl_all.lib");
        } else if(sdkType == 1) {
            thocr_sid = new String[]{"thocr_vl_all1.lib", "thocr_vl_all2.lib"};
            this.mergeFile(thocr_sid, "thocr_vl_all.lib");
        } else if(sdkType == 2) {
            thocr_sid = new String[]{"thocr_sid1.lib", "thocr_sid2.lib"};
            this.mergeFile(thocr_sid, "thocr_sid.lib");
        }

    }

    public void copyHolderBigFile() throws IOException {
        String[] IDCARDANDROID = new String[]{"IDCARDANDROID1.xml", "IDCARDANDROID2.xml", "IDCARDANDROID3.xml", "IDCARDANDROID4.xml", "IDCARDANDROID5.xml", "IDCARDANDROID6.xml"};
        String[] IDCARDANDROIDABROAD = new String[]{"IDCARDANDROIDABROAD1.xml", "IDCARDANDROIDABROAD2.xml", "IDCARDANDROIDABROAD3.xml"};
        String[] pntWTPENPDA = new String[]{"pntWTPENPDA1.lib", "pntWTPENPDA2.lib", "pntWTPENPDA3.lib"};
        this.mergeFile(IDCARDANDROID, "IDCARDANDROID.xml");
        this.mergeFile(pntWTPENPDA, "pntWTPENPDA.lib");
        this.mergeFile(IDCARDANDROIDABROAD, "IDCARDANDROIDABROAD.xml");
    }

    public void mergeFile(String[] file, String filename) throws IOException {
        Common common = new Common();
        String filepath = "";
        if(filename.equals("12.xml")) {
            filepath = common.getSDPath() + "/AndroidWT/IDCard/IDCARDMS/" + filename;
        } else {
            filepath = common.getSDPath() + "/AndroidWT/IDCard/" + filename;
        }

        File newfile = new File(filepath);
        if(newfile != null && newfile.exists() && newfile.isFile()) {
            newfile.delete();
        }

        OutputStream out = new FileOutputStream(filepath);
        byte[] buffer = new byte[1024];

        for(int i = 0; i < file.length; ++i) {
            InputStream in;
            if(filename.equals("12.xml")) {
                in = this.getAssets().open("idcardocr/IDCARDMS/" + file[i]);
            } else {
                in = this.getAssets().open("idcardocr/" + file[i]);
            }

            int readLen;
            while((readLen = in.read(buffer)) != -1) {
                out.write(buffer, 0, readLen);
            }

            out.flush();
            in.close();
        }

        out.close();
    }

    public void copyDataBase(int sdkType) throws IOException {
        Common common = new Common();
        String dst = common.getSDPath() + "/AndroidWT/IDCard/";
        String[] filename = null;
        String[] IDCARDMS = null;
        String[] filename2;
        String[] IDCARDMS0;
        if(sdkType == 0) {
            filename2 = new String[]{"AdminDivCode.txt", "AdminDiv.txt", "Special.txt", "OEMtest.txt", "ProvName.txt", "IDCLASSIFIERANDROID.xml", "THOCR_pspt.lib", "THOCR_LP.lib", "thocr_Driver_License.lib", "IssueAndBirth.txt", "THOCR_Num_Char.lib", "BrandModel.txt", "version.txt", "wtdate.lsc", "SidIssueAuthority.txt", "thocr_vl_digit_capitals.lib", "thocr_vl_province.lib", "IDKLICENSE.dat", "IDKDevice.dat", "IDCARDMS.xml", "IDCARDMS_ABROAD.xml"};
            IDCARDMS0 = new String[]{"2.xml", "3.xml", "4.xml", "5.xml", "6.xml", "7.xml", "8.xml", "9.xml", "10.xml", "11.xml", "13.xml", "14.xml", "15.xml", "16.xml", "17.xml", "18.xml", "19.xml", "20.xml", "21.xml", "22.xml", "23.xml", "24.xml", "25.xml", "26.xml", "27.xml", "28.xml", "29.xml", "1000.xml", "1001.xml", "1002.xml", "1003.xml", "1004.xml", "1005.xml", "1006.xml", "1007.xml", "1008.xml", "1009.xml", "1011.xml", "1012.xml", "1013.xml", "1018.xml", "1019.xml", "1020.xml", "1021.xml", "1030.xml", "1031.xml", "1032.xml", "1033.xml", "1034.xml", "1035.xml", "1036.xml", "1038.xml", "1100.xml", "1101.xml", "1102.xml", "2001.xml", "2002.xml", "2003.xml", "2004.xml", "2006.xml", "2007.xml", "2008.xml", "2009.xml", "2010.xml", "2011.xml", "2020.xml", "IDCARD.dtd"};
            filename = filename2;
            IDCARDMS = IDCARDMS0;
        } else if(sdkType == 1) {
            filename2 = new String[]{"Special.txt", "OEMtest.txt", "ProvName.txt", "IDCLASSIFIERANDROID.xml", "BrandModel.txt", "version.txt", "wtdate.lsc", "thocr_vl_digit_capitals.lib", "thocr_vl_province.lib", "IDCARDMS.xml", "IDCARDMS_ABROAD.xml", "IDKDevice.dat", "IDKLICENSE.dat"};
            IDCARDMS0 = new String[]{"6.xml"};
            filename = filename2;
            IDCARDMS = IDCARDMS0;
        } else if(sdkType == 2) {
            filename2 = new String[]{"Special.txt", "OEMtest.txt", "ProvName.txt", "IDCLASSIFIERANDROID.xml", "BrandModel.txt", "version.txt", "wtdate.lsc", "IDCARDMS.xml", "IDCARDMS_ABROAD.xml", "IDKDevice.dat", "IDKLICENSE.dat"};
            IDCARDMS0 = new String[]{"2.xml", "3.xml"};
            filename = filename2;
            IDCARDMS = IDCARDMS0;
        }

        File file;
        InputStream myInput;
        FileOutputStream myOutput;
        byte[] buffer;
        int length;
        int i;
        String outFileName;
        for(i = 0; i < filename.length; ++i) {
            outFileName = dst + filename[i];
            file = new File(dst);
            if(!file.exists()) {
                file.mkdirs();
            }

            file = new File(outFileName);
            if(file.exists()) {
                file.delete();
            }

            try {
                myInput = this.getAssets().open("idcardocr/" + filename[i]);
                myOutput = new FileOutputStream(outFileName);
                buffer = new byte[1024];

                while((length = myInput.read(buffer)) > 0) {
                    myOutput.write(buffer, 0, length);
                }

                myOutput.flush();
                myOutput.close();
                myInput.close();
            } catch (Exception var14) {
                System.out.println(filename[i] + "is not found");
            }
        }

        for(i = 0; i < IDCARDMS.length; ++i) {
            outFileName = dst + "IDCARDMS/" + IDCARDMS[i];
            file = new File(dst + "IDCARDMS/");
            if(!file.exists()) {
                file.mkdirs();
            }

            file = new File(outFileName);
            if(file.exists()) {
                file.delete();
            }

            try {
                myInput = this.getAssets().open("idcardocr/IDCARDMS/" + IDCARDMS[i]);
                myOutput = new FileOutputStream(outFileName);
                buffer = new byte[1024];

                while((length = myInput.read(buffer)) > 0) {
                    myOutput.write(buffer, 0, length);
                }

                myOutput.flush();
                myOutput.close();
                myInput.close();
            } catch (Exception var13) {
                System.out.println(IDCARDMS[i] + "is not found");
            }
        }

    }

    private String readSDCardFile() {
        String authModeContent = null;

        try {
            File idcardfile = new File(this.idcardpath);
            if(!idcardfile.exists()) {
                return null;
            }

            File file = new File(this.idcardpath + "authmode.lsc");
            if(!file.exists()) {
                return null;
            }

            FileInputStream fileInputStream = new FileInputStream(this.idcardpath + "authmode.lsc");
            int size_is = fileInputStream.available();
            byte[] byte_new = new byte[size_is];
            fileInputStream.read(byte_new);
            fileInputStream.close();
            authModeContent = new String(byte_new);
        } catch (Exception var7) {
            authModeContent = null;
        }

        return authModeContent;
    }

    public String getStoragePath(Context mContext, boolean is_removale) {
        String path = "";
        String temppath = "";
        String lastPath = "";
        StorageManager mStorageManager = (StorageManager)mContext.getSystemService("storage");
        Class storageVolumeClazz = null;

        try {
            storageVolumeClazz = Class.forName("android.os.storage.StorageVolume");
            Method getVolumeList = mStorageManager.getClass().getMethod("getVolumeList", new Class[0]);
            Method getPath = storageVolumeClazz.getMethod("getPath", new Class[0]);
            Method isRemovable = storageVolumeClazz.getMethod("isRemovable", new Class[0]);
            Method getUuid = storageVolumeClazz.getMethod("getUuid", new Class[0]);
            Object result = getVolumeList.invoke(mStorageManager, new Object[0]);
            int length = Array.getLength(result);

            for(int i = 0; i < length; ++i) {
                Object storageVolumeElement = Array.get(result, i);
                temppath = (String)getPath.invoke(storageVolumeElement, new Object[0]);
                boolean removable = ((Boolean)isRemovable.invoke(storageVolumeElement, new Object[0])).booleanValue();
                if(is_removale == removable) {
                    if(path != null && !path.equals("")) {
                        if(!temppath.equals(lastPath)) {
                            path = path + "#" + temppath;
                            lastPath = temppath;
                        }
                    } else if(path != null && path.equals("")) {
                        path = temppath;
                        lastPath = temppath;
                    }
                }
            }
        } catch (ClassNotFoundException var17) {
            var17.printStackTrace();
        } catch (InvocationTargetException var18) {
            var18.printStackTrace();
        } catch (NoSuchMethodException var19) {
            var19.printStackTrace();
        } catch (IllegalAccessException var20) {
            var20.printStackTrace();
        }

        return path;
    }

    public class recogBinder extends Binder {
        IDCardAPI IDCard = new IDCardAPI();

        public recogBinder() {
        }

        public void uninstallEngine() {
            if(this.IDCard != null) {
                this.IDCard.FreeIDCard();
            }

        }

        public ResultMessage getRecogResult(RecogParameterMessage rpm) throws Exception {
            if(rpm == null) {
                return null;
            } else {
                int ReturnAuthority =0;// this.IDCardAuthAndInit(rpm);
                if(RecogService.this.ReturnInitIDCard == 0 && ReturnAuthority == 0) {
                    if(rpm.nMainID == 1020) {
                        this.IDCardLoadAndRecogMRZ(rpm);
                    } else {
                        if((rpm.nMainID == 1100 || rpm.nMainID == 1101) && !rpm.isAutoRecog) {
                            this.IDCardLoadAndCutLineationImage(rpm);
                        } else if(rpm.nMainID == 1102) {
                            this.IDCardLoadNoLineationImage(rpm);
                        } else {
                            this.IDCardLoadNoLineationImage(rpm);
                            this.IDCardCutNoLineationImage(rpm);
                        }

                        this.IDCardRecognitionImage(rpm);
                    }

                    this.IDCardGetRecognitionResult(rpm);
                    this.IDCardReturnRecognitionResult(rpm);
                    RecogService.this.resultMessage.ReturnTimes.clear();
                    RecogService.this.resultMessage.ReturnTimes.add(String.valueOf(RecogService.this.recogTime));
                    return RecogService.this.resultMessage;
                } else {
                    ResultMessage resultMessage = new ResultMessage();
                    resultMessage.ReturnAuthority = this.IDCardAuthAndInit(rpm);
                    resultMessage.ReturnInitIDCard = RecogService.this.ReturnInitIDCard;
                    return resultMessage;
                }
            }
        }

        public int IDCardAuthAndInit(RecogParameterMessage rpm) {
            RecogService.this.resultMessage = new ResultMessage();
            if(RecogService.this.ReturnAuthority != 0) {
                return RecogService.this.ReturnAuthority;
            } else {
                boolean typebool = false;
                String nMainIDString;
                String[] cfgs;
                if(rpm.nMainID == 0) {
                    nMainIDString = "";

                    try {
                        nMainIDString = RecogService.this.readtxt();
                    } catch (Exception var26) {
                        var26.printStackTrace();
                    }

                    cfgs = nMainIDString.split("==##");
                    if(cfgs != null && cfgs.length >= 2) {
                        rpm.nMainID = RecogService.this.String2Int(cfgs[0]);
                    }
                }

                if(rpm.nMainID == 0) {
                    rpm.nMainID = 2;
                }

                nMainIDString = String.valueOf(rpm.nMainID);
                cfgs = null;
                String[] resultStrings = null;
                String SysCertVersion = "wtversion5_5";

                try {
                    InputStream iStream = RecogService.this.getAssets().open("idcardocr/authtype.lsc");
                    int size_is = iStream.available();
                    byte[] byte_new = new byte[size_is];
                    iStream.read(byte_new);
                    iStream.close();
                    String type_result_string = new String(byte_new);
                    type_result_string = RecogService.this.common.getSrcPassword(type_result_string, SysCertVersion);
                    resultStrings = type_result_string.split(";");
                } catch (IOException var24) {
                    resultStrings = null;
                } catch (Exception var25) {
                    resultStrings = null;
                }

                int devCheck;
                if(resultStrings != null) {
                    for(devCheck = 0; devCheck < resultStrings.length; ++devCheck) {
                        if(resultStrings[devCheck].equals(nMainIDString)) {
                            typebool = true;
                        }
                    }
                }

                if(!typebool) {
                    RecogService.this.resultMessage.ReturnAuthority = -10016;
                    return RecogService.this.resultMessage.ReturnAuthority;
                } else {
                    if(!RecogService.this.mafr.isCheckDevType("11") && !rpm.isCheckDevType) {
                        devCheck = 0;
                    } else {
                        devCheck = RecogService.this.mafr.isAllowDevTypeAndDevCode("11", rpm.devcode);
                    }

                    if(rpm.versionfile != null && !rpm.versionfile.equals("")) {
                        if(rpm.versionfile.equals("assets")) {
                            rpm.versionfile = Environment.getExternalStorageDirectory().toString() + "/AndroidWT/IDCard/wtversion.lsc";
                        }

                        VersionAuthFileOperate vafo = new VersionAuthFileOperate();
                        RecogService.this.resultMessage.ReturnAuthority = vafo.verifyVersionAuthFile(rpm.versionfile, rpm.devcode, "11", String.valueOf(rpm.nMainID));
                    } else if(rpm.dateFilePath != null && !rpm.dateFilePath.equals("")) {
                        if(rpm.dateFilePath.equals("assets")) {
                            rpm.dateFilePath = Environment.getExternalStorageDirectory().toString() + "/AndroidWT/IDCard/wtdate.lsc";
                        }

                        File dateFile = new File(rpm.dateFilePath);
                        if(dateFile.exists()) {
                            Boolean bool = DateAuthFileOperate.judgeDateAuthFileBoolean(rpm.dateFilePath);
                            DateAuthFileOperate dafo = new DateAuthFileOperate();
                            if(bool.booleanValue()) {
                                RecogService.this.resultMessage.ReturnAuthority = dafo.verifyDateAuthFile(rpm.dateFilePath, rpm.devcode, "11");
                                if(RecogService.this.resultMessage.ReturnAuthority == -10090) {
                                    RecogService.this.resultMessage.ReturnAuthority = 0;
                                }
                            } else {
                                RecogService.this.resultMessage.ReturnAuthority = dafo.verifyOldDateAuthFile(rpm.dateFilePath, rpm.devcode);
                                if(RecogService.this.resultMessage.ReturnAuthority == -10090) {
                                    RecogService.this.resultMessage.ReturnAuthority = 0;
                                }
                            }
                        }
                    } else if(RecogService.this.isTF.booleanValue()) {
                        RecogService.this.resultMessage.ReturnAuthority = 0;
                    } else if(RecogService.this.mafr.isCheckPRJMode("11")) {
                        String packageName = RecogService.this.getPackageName();
                        PackageInfo pkg = null;

                        try {
                            pkg = RecogService.this.getPackageManager().getPackageInfo(RecogService.this.getApplication().getPackageName(), 0);
                        } catch (NameNotFoundException var23) {
                            ;
                        }

                        String app_name = pkg.applicationInfo.loadLabel(RecogService.this.getPackageManager()).toString();
                        String company_name = null;

                        try {
                            int id_company_name = RecogService.this.getResources().getIdentifier("company_name", "string", RecogService.this.getPackageName());
                            company_name = RecogService.this.getResources().getString(id_company_name);
                        } catch (NotFoundException var22) {
                            var22.printStackTrace();
                            RecogService.this.resultMessage.ReturnAuthority = -10608;
                            System.out.println("在strings文件中未找到company_name字段");
                        }

                        if(app_name != null && company_name != null) {
                            RecogService.this.resultMessage.ReturnAuthority = RecogService.this.mafr.isCheckPRJOK("11", rpm.devcode, packageName, app_name, company_name);
                            if(RecogService.this.resultMessage.ReturnAuthority == -10090 && devCheck == 0) {
                                if(RecogService.this.mathRandom.PercentageRandom() == 5) {
                                    if(RecogService.this.getResources().getConfiguration().locale.getLanguage().equals("zh") && RecogService.this.getResources().getConfiguration().locale.getCountry().equals("CN")) {
                                        Toast.makeText(RecogService.this.getApplicationContext(), "您的授权已于" + RecogService.this.mafr.ExpiratioTime + "到期，请更新授权，否则识别功能将停止使用！", 1).show();
                                    } else {
                                        Toast.makeText(RecogService.this.getApplicationContext(), "Your license has expired in" + RecogService.this.mafr.ExpiratioTime + " please update the authorization, otherwise will stop using the identification function！", 1).show();
                                    }
                                }

                                RecogService.this.resultMessage.ReturnAuthority = 0;
                            }
                        }
                    } else {
                        ProcedureAuthOperate pao = new ProcedureAuthOperate(RecogService.this.getApplicationContext());
                        String originalAuthFilePath = pao.getOriginalAuthFilePathByProjectType("11");
                        File originalAuthFile = new File(originalAuthFilePath);
                        String[] str = new String[12];
                        CDKey cdKey = new CDKey();
                        boolean fleg = false;
                        if(originalAuthFile.exists()) {
                            try {
                                str = pao.readOriginalAuthFileContent(originalAuthFilePath);
                            } catch (Exception var21) {
                                str[1] = "";
                                str[2] = "";
                            }

                            fleg = cdKey.checkjhm(str[2], RecogService.this.mcode, str[1]);
                            if(!fleg && str.length >= 8 && str[8] != null && str[7] != null) {
                                fleg = cdKey.checkjhm(str[8], RecogService.this.mcode, str[7]);
                            }

                            if(!fleg && (str[3].equals(RecogService.this.imeiSIM1) || str[3].equals(RecogService.this.imeiSIM2))) {
                                fleg = true;
                            }

                            if(fleg) {
                                RecogService.this.resultMessage.ReturnAuthority = 0;
                                RecogService.this.resultMessage.ReturnInitIDCard = RecogService.this.ReturnInitIDCard;
                            } else {
                                RecogService.this.resultMessage.ReturnAuthority = -10015;
                                RecogService.this.resultMessage.ReturnInitIDCard = RecogService.this.ReturnInitIDCard;
                            }

                            return RecogService.this.resultMessage.ReturnAuthority;
                        }

                        SqliteHelperUtils sqliteHelperUtils = new SqliteHelperUtils(RecogService.this.getApplicationContext(), "wt.db", 2);
                        String oldwtlsc = sqliteHelperUtils.queryData(RecogService.query_old_lsc, RecogService.this.params);
                        if(oldwtlsc != null && !oldwtlsc.equals("")) {
                            try {
                                Common common = new Common();
                                String aesjie = common.getSrcPassword(oldwtlsc, SysCertVersion);
                                String[] result = aesjie.split(",");
                                fleg = cdKey.checkjhm(result[2], RecogService.this.mcode, result[1]);
                                File originalAuthRootFile = new File(Environment.getExternalStorageDirectory().toString() + "/wintone/");
                                if(!originalAuthRootFile.exists()) {
                                    originalAuthRootFile.mkdirs();
                                }

                                FileOutputStream outStream = new FileOutputStream(originalAuthFilePath);
                                outStream.write(oldwtlsc.getBytes());
                                outStream.close();
                                if(fleg) {
                                    RecogService.this.resultMessage.ReturnAuthority = 0;
                                    RecogService.this.resultMessage.ReturnInitIDCard = RecogService.this.ReturnInitIDCard;
                                } else {
                                    RecogService.this.resultMessage.ReturnAuthority = -10015;
                                    RecogService.this.resultMessage.ReturnInitIDCard = RecogService.this.ReturnInitIDCard;
                                }

                                return RecogService.this.resultMessage.ReturnAuthority;
                            } catch (Exception var27) {
                                var27.printStackTrace();
                            }
                        } else {
                            String wintoneLSCFilePathString = RecogService.this.common.getSDPath() + "/AndroidWT/wt.lsc";
                            File wintoneLSCFile = new File(wintoneLSCFilePathString);
                            if(wintoneLSCFile.exists() && typebool) {
                                WintoneLSCXMLInformation wlxi = null;
                                TelephonyManager telephonyManager1 = (TelephonyManager)RecogService.this.getSystemService("phone");
                                if(telephonyManager1.getDeviceId() != null && !telephonyManager1.getDeviceId().equals("")) {
                                    wlxi = WintoneLSCOperateTools.ReadAuthFile(telephonyManager1.getDeviceId());
                                } else {
                                    wlxi = WintoneLSCOperateTools.ReadAuthFile(Secure.getString(RecogService.this.getContentResolver(), "android_id"));
                                }

                                RecogService.this.resultMessage.ReturnAuthority = WintoneAuthOperateTools.accordTypeDateNumber("11", wlxi.typeStrings, wlxi.duedateStrings, wlxi.sumStrings);
                                if(RecogService.this.resultMessage.ReturnAuthority == 0) {
                                    WintoneLSCOperateTools.ModifyNumberInAuthFileByProjectType("11");
                                }
                            }
                        }
                    }

                    if(RecogService.this.resultMessage.ReturnAuthority == 0) {
                        RecogService.this.resultMessage.ReturnAuthority = devCheck;
                    }

                    RecogService.this.resultMessage.ReturnInitIDCard = RecogService.this.ReturnInitIDCard;
                    return RecogService.this.resultMessage.ReturnAuthority;
                }
            }
        }

        public String IDCardLoadAndCutLineationImage(RecogParameterMessage rpm) {
            if(rpm.GetVersionInfo) {
                RecogService.this.resultMessage.ReturnGetVersionInfo = this.IDCard.GetVersionInfo();
            }

            RecogService.this.resultMessage.lpFileOut = rpm.lpFileName + ".jpg";
            int isProcessImage;
            if(rpm.nMainID == 1100) {
                isProcessImage = this.IDCard.GetRectPosVehicleNum(rpm.lpFileName, rpm.array, rpm.multiRows, RecogService.this.resultMessage.lpFileOut);
            } else {
                isProcessImage = this.IDCard.GetRectPos(rpm.lpFileName, rpm.array, rpm.multiRows, RecogService.this.resultMessage.lpFileOut);
            }

            if(isProcessImage == 0) {
                rpm.lpFileName = RecogService.this.resultMessage.lpFileOut;
                RecogService.this.resultMessage.lpFileName = RecogService.this.resultMessage.lpFileOut;
                RecogService.this.resultMessage.isProcessImage = isProcessImage;
                RecogService.this.resultMessage.ReturnLoadImageToMemory = this.IDCard.LoadImageToMemory(rpm.lpFileName, rpm.nTypeLoadImageToMemory);
            }

            return RecogService.this.resultMessage.lpFileName;
        }

        public void IDCardLoadNoLineationImage(RecogParameterMessage rpm) {
            if(rpm.GetVersionInfo) {
                RecogService.this.resultMessage.ReturnGetVersionInfo = this.IDCard.GetVersionInfo();
            }

            if(RecogService.isRecogByPath) {
                File file = new File(rpm.lpFileName);
                if(file.exists() && !file.isDirectory()) {
                    if(!rpm.lpFileName.substring(rpm.lpFileName.lastIndexOf(46), rpm.lpFileName.length()).equals(".jpg") && !rpm.lpFileName.substring(rpm.lpFileName.lastIndexOf(46), rpm.lpFileName.length()).equals(".JPG")) {
                        RecogService.this.resultMessage.ReturnLoadImageToMemory = 4;
                    } else {
                        this.IDCard.SetParameter(0, RecogService.nMainID);
                        RecogService.this.resultMessage.ReturnLoadImageToMemory = this.IDCard.LoadImageToMemory(rpm.lpFileName, rpm.nTypeLoadImageToMemory);
                    }
                } else {
                    RecogService.this.resultMessage.ReturnLoadImageToMemory = 5;
                }
            } else {
                RecogService.this.resultMessage.ReturnLoadImageToMemory = 0;
            }

        }

        public void IDCardCutNoLineationImage(RecogParameterMessage rpm) {
            if(RecogService.this.resultMessage.ReturnLoadImageToMemory == 0) {
                if(rpm.isCut && (rpm.nMainID == 4 || rpm.nMainID == 13 || rpm.nMainID == 2 || rpm.nMainID == 1013 || rpm.nMainID == 5 || rpm.nMainID == 6 || rpm.nMainID == 9 || rpm.nMainID == 11 || rpm.nMainID == 12 || rpm.nMainID == 22 || rpm.nMainID == 1001 || rpm.nMainID == 1005 || rpm.nMainID == 14 || rpm.nMainID == 15 || rpm.nMainID == 10 || rpm.nMainID == 1030 || rpm.nMainID == 1031 || rpm.nMainID == 1032 || rpm.nMainID == 2001 || rpm.nMainID == 2004 || rpm.nMainID == 2003 || rpm.nMainID == 2002 || rpm.nMainID == 1012 || rpm.nMainID == 1034 || rpm.nMainID == 1036 || rpm.nMainID == 1033 || rpm.nMainID == 2010 || rpm.nMainID == 2011 || rpm.nMainID == 1021 || rpm.nMainID == 28 || rpm.nMainID == 7)) {
                    if(rpm.triggertype == 0) {
                        this.IDCard.SetProcessType(rpm.nProcessType, rpm.nSetType);
                    } else {
                        int iProcessImagex = this.IDCard.SpecialAutoCropImageExt(0);
                        int var5 = this.IDCard.AutoRotateImage(2);
                    }
                }
            }

        }

        public void IDCardRecognitionImage(RecogParameterMessage rpm) {
            int[] nSubID = new int[1];
            if(RecogService.this.resultMessage.ReturnLoadImageToMemory == 0) {
                String cutSavePath;
                if(rpm.isAutoClassify && rpm.nMainID == 3000) {
                    this.IDCard.SetIDCardID(2006, nSubID);
                    this.IDCard.AddIDCardID(2007, nSubID);
                    this.IDCard.AddIDCardID(2008, nSubID);
                    this.IDCard.SpecialAutoCropImageExt(3);
                    this.IDCard.ProcessImage(2);
                    cutSavePath = rpm.lpFileName + ".jpg";
                    this.IDCard.SaveImage(cutSavePath);
                    RecogService.this.resultMessage.ReturnRecogIDCard = this.IDCard.RecogIDCard();
                } else if(rpm.isAutoClassify && rpm.nMainID != 3000) {
                    if(rpm.isOnlyClassIDCard) {
                        this.IDCard.SetIDCardID(2, nSubID);
                        this.IDCard.AddIDCardID(3, nSubID);
                    } else if(rpm.autoRecogCombination == 0) {
                        this.IDCard.SetIDCardID(13, nSubID);
                        this.IDCard.AddIDCardID(2001, nSubID);
                    } else {
                        this.IDCard.SetIDCardID(2, nSubID);
                        this.IDCard.AddIDCardID(5, nSubID);
                        this.IDCard.AddIDCardID(6, nSubID);
                        this.IDCard.AddIDCardID(9, nSubID);
                        this.IDCard.AddIDCardID(10, nSubID);
                        this.IDCard.AddIDCardID(11, nSubID);
                        this.IDCard.AddIDCardID(12, nSubID);
                        this.IDCard.AddIDCardID(13, nSubID);
                        this.IDCard.AddIDCardID(22, nSubID);
                    }

                    RecogService.this.recogTime = System.currentTimeMillis();
                    if(rpm.isSetIDCardRejectType) {
                        if(RecogService.nMainID == 2) {
                            this.IDCard.SetIDCardRejectType(2, true);
                            this.IDCard.SetIDCardRejectType(3, true);
                        } else {
                            this.IDCard.SetIDCardRejectType(RecogService.nMainID, true);
                        }
                    }

                    RecogService.this.resultMessage.ReturnRecogIDCard = this.IDCard.RecogIDCard();
                    RecogService.this.recogTime = System.currentTimeMillis() - RecogService.this.recogTime;
                } else {
                    if(!rpm.isAutoClassify) {
                        this.IDCard.SetIDCardID(RecogService.nMainID, nSubID);
                    }

                    int SubID = 0;
                    if(rpm.nSubID != null && rpm.nSubID.length > 0) {
                        SubID = rpm.nSubID[0];
                    }

                    if(rpm.isSetIDCardRejectType) {
                        if(RecogService.nMainID == 2) {
                            this.IDCard.SetIDCardRejectType(2, true);
                            this.IDCard.SetIDCardRejectType(3, true);
                        } else {
                            this.IDCard.SetIDCardRejectType(RecogService.nMainID, true);
                        }
                    }

                    if(rpm.nMainID == 1102) {
                        RecogService.this.resultMessage.ReturnRecogIDCard = this.IDCard.RecogIDNumber();
                    } else if(rpm.nMainID == 1021) {
                        RecogService.this.resultMessage.ReturnRecogIDCard = this.IDCard.RecogIDCardEX(rpm.nMainID, 1);
                    } else {
                        RecogService.this.resultMessage.ReturnRecogIDCard = this.IDCard.RecogIDCardEX(rpm.nMainID, SubID);
                    }
                }

                if(RecogService.this.resultMessage.ReturnRecogIDCard != -6) {
                    if(rpm.lpHeadFileName != null && !rpm.lpHeadFileName.equals("")) {
                        cutSavePath = rpm.lpHeadFileName.substring(0, rpm.lpHeadFileName.lastIndexOf("/"));
                        File file = new File(cutSavePath);
                        if(!file.exists()) {
                            file.mkdirs();
                        }

                        RecogService.this.resultMessage.ReturnSaveHeadImage = this.IDCard.SaveHeadImage(rpm.lpHeadFileName);
                        RecogService.this.sendBroadcast(new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE", Uri.parse("file://" + rpm.lpHeadFileName)));
                    }

                    if(rpm.isSaveCut) {
                        cutSavePath = "";
                        if(rpm.lpFileName != null && !rpm.lpFileName.equals("")) {
                            cutSavePath = rpm.lpFileName.substring(0, rpm.lpFileName.lastIndexOf(46)) + "Cut.jpg";
                        }

                        if(rpm.cutSavePath != null && !rpm.cutSavePath.equals("")) {
                            cutSavePath = rpm.cutSavePath;
                        }

                        String FileDir = cutSavePath.substring(0, cutSavePath.lastIndexOf("/"));
                        File filex = new File(FileDir);
                        if(!filex.exists()) {
                            filex.mkdirs();
                        }

                        this.IDCard.SaveImage(cutSavePath);
                        RecogService.this.sendBroadcast(new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE", Uri.parse("file://" + cutSavePath)));
                    }
                }
            }

        }

        public void IDCardGetRecognitionResult(RecogParameterMessage rpm) {
            if(rpm.GetSubID) {
                RecogService.this.resultMessage.ReturnGetSubID = this.IDCard.GetSubId();
            }

        }

        public void IDCardLoadAndRecogMRZ(RecogParameterMessage rpm) {
            if(rpm.GetVersionInfo) {
                RecogService.this.resultMessage.ReturnGetVersionInfo = this.IDCard.GetVersionInfo();
            }

            new Date();
            RecogService.this.resultMessage.ReturnLoadImageToMemory = this.IDCard.LoadImageToMemory(rpm.lpFileName, rpm.nTypeLoadImageToMemory);
            new Date();
            if(RecogService.this.resultMessage.ReturnLoadImageToMemory == 0) {
                RecogService.this.resultMessage.ReturnRecogIDCard = this.IDCard.RecogMRZ(rpm.array, rpm.ncheckmrz);
            }

        }

        public ResultMessage IDCardReturnRecognitionResult(RecogParameterMessage rpm) {
            List<int[]> listDate = new ArrayList();
            listDate.clear();
            if(RecogService.this.resultMessage.ReturnAuthority != 0) {
                ResultMessage errorResultMessage = new ResultMessage();
                errorResultMessage.ReturnAuthority = RecogService.this.resultMessage.ReturnAuthority;
                errorResultMessage.ReturnInitIDCard = RecogService.this.resultMessage.ReturnInitIDCard;
                return errorResultMessage;
            } else {
                for(int i = 0; i < 20; ++i) {
                    String buffer = "";
                    int[] textNamePosition = new int[4];
                    buffer = this.IDCard.GetFieldName(i);
                    RecogService.this.resultMessage.GetFieldName[i] = buffer;
                    if(buffer == null) {
                        break;
                    }

                    buffer = this.IDCard.GetRecogResult(i);
                    RecogService.this.resultMessage.GetRecogResult[i] = buffer;
                    if(rpm.isGetRecogFieldPos) {
                        this.IDCard.GetRecogFieldPos(i, textNamePosition);
                        listDate.add(textNamePosition);
                    }
                }

                RecogService.this.resultMessage.textNamePosition = listDate;
                return RecogService.this.resultMessage;
            }
        }

        public int IDCardGetInit() {
            return RecogService.this.ReturnInitIDCard;
        }

        public int SetRotateType(int nType) {
            return RecogService.this.ReturnInitIDCard == 0?this.IDCard.SetRotateType(nType):RecogService.this.ReturnInitIDCard;
        }

        public int SetParameter(int recogType, int nMainID) {
            if(RecogService.this.ReturnInitIDCard == 0) {
                this.IDCard.SetParameter(recogType, nMainID);
                return 0;
            } else {
                return RecogService.this.ReturnInitIDCard;
            }
        }

        public int LoadBufferImageEx(byte[] nv21bytes, int nWidth, int nHeight, int nBitCount, int nImageDataType) {
            return RecogService.this.ReturnInitIDCard == 0?this.IDCard.LoadBufferImageEx(nv21bytes, nWidth, nHeight, nBitCount, nImageDataType):RecogService.this.ReturnInitIDCard;
        }

        public int ConfirmSideLineEx(int nPlatform) {
            return RecogService.this.ReturnInitIDCard == 0?this.IDCard.ConfirmSideLineEx(nPlatform):RecogService.this.ReturnInitIDCard;
        }

        public int SetIDCardRejectType(int nCardType, boolean bSet) {
            return RecogService.this.ReturnInitIDCard == 0?this.IDCard.SetIDCardRejectType(nCardType, bSet):RecogService.this.ReturnInitIDCard;
        }

        public int DetectLightspot() {
            return RecogService.this.ReturnInitIDCard == 0?this.IDCard.DetectLightspot():RecogService.this.ReturnInitIDCard;
        }

        public int CheckPicIsClearEx() {
            return RecogService.this.ReturnInitIDCard == 0?this.IDCard.CheckPicIsClearEx():RecogService.this.ReturnInitIDCard;
        }

        public void SetPixClearEx(int nPix) {
            if(RecogService.this.ReturnInitIDCard == 0) {
                this.IDCard.SetPixClearEx(nPix);
            }

        }

        public void SetVideoStreamCropTypeEx(int nCropType) {
            if(RecogService.this.ReturnInitIDCard == 0) {
                this.IDCard.SetVideoStreamCropTypeEx(nCropType);
            }

        }

        public int SetROI(int nLeft, int nTop, int nRight, int nBottom) {
            return RecogService.this.ReturnInitIDCard == 0?this.IDCard.SetROI(nLeft, nTop, nRight, nBottom):RecogService.this.ReturnInitIDCard;
        }

        public int GetAcquireMRZSignalEx(byte[] nv21bytes, int width, int height, int left, int right, int top, int bottom, int nRotateType) {
            return RecogService.this.ReturnInitIDCard == 0?this.IDCard.GetAcquireMRZSignalEx(nv21bytes, width, height, left, right, top, bottom, nRotateType):RecogService.this.ReturnInitIDCard;
        }

        public void SetConfirmSideMethod(int nMethod) {
            if(RecogService.this.ReturnInitIDCard == 0) {
                this.IDCard.SetConfirmSideMethod(nMethod);
            }

        }

        public void IsDetectRegionValid(int nDetect) {
            if(RecogService.this.ReturnInitIDCard == 0) {
                this.IDCard.IsDetectRegionValid(nDetect);
            }

        }

        public void IsDetect180Rotate(int nDetect) {
            if(RecogService.this.ReturnInitIDCard == 0) {
                this.IDCard.IsDetect180Rotate(nDetect);
            }

        }

        public void SetDetectIDCardType(int nType) {
            if(RecogService.this.ReturnInitIDCard == 0) {
                this.IDCard.SetDetectIDCardType(nType);
            }

        }

        public int GetFourSideLines(Frame frame) {
            int returnFlag = -1000;
            if(RecogService.this.ReturnInitIDCard == 0) {
                int[] fourPositions = new int[8];
                returnFlag = this.IDCard.GetRealTimeFourConersEx(fourPositions);
                frame.ltStartX = fourPositions[0];
                frame.ltStartY = fourPositions[1];
                frame.rtStartX = fourPositions[2];
                frame.rtStartY = fourPositions[3];
                frame.lbStartX = fourPositions[4];
                frame.lbStartY = fourPositions[5];
                frame.rbStartX = fourPositions[6];
                frame.rbStartY = fourPositions[7];
            }

            return returnFlag;
        }
    }
}
