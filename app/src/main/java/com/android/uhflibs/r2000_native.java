package com.android.uhflibs;

/* history
1.3	fix set antenna power and cancel wait on inventory
*/

import java.util.ArrayList;
//import java.util.logging.Logger;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.uhf.linkage.Linkage;
import com.uhf.structures.DynamicQParms;
import com.uhf.structures.FixedQParms;
import com.uhf.structures.Rfid_Value;
import com.uhf.structures.Rfid_dValue;
import com.uhf.structures.SelectCriteria;
import com.uhf.structures.Single_Inventory_Time_Config;
import com.uhf.structures.St_Inv_Data;
import com.uhf.structures.TagGroup;
import com.uhf.constants.Constants.InvMode;
import com.uhf.constants.Constants.MemoryBank;
import com.uhf.constants.Constants.RFID_18K6C_COUNTRY_REGION;
import com.uhf.constants.Constants.RFID_18K6C_TAG_MEM_PERM;
import com.uhf.constants.Constants.RFID_18K6C_TAG_PWD_PERM;
import com.uhf.constants.Constants.RFID_INVENTORY_TAG_AREA;
import com.uhf.constants.Constants.Result;

public class r2000_native {

	private static final String TAG = "r2000_native";
	private Linkage lk = new Linkage();
	
	private Handler h = null;
	private boolean inSearch = false;
	private inv_thread invs = null;
	private get_invdata gd;// = new get_invdata();
	
	
	private static final String SERIALPORT = "/dev/ttyMT2";
	private static final String POWERCTL = "/sys/class/misc/mtgpio/pin";
	private static final int PW_GPIO = 94;
	private DeviceControl pw = new DeviceControl(POWERCTL, PW_GPIO);
	
	private class inv_thread extends Thread {
		public void run() {
			super.run();
			int rv = SetAlgorithmDyParameters(lk);
			if(rv != Result.RFID_STATUS_OK.getValue())
			{
				Log.w("r2000_kt45", "set alg failed3333333333333333333333333333333333333333333333333333333333333333333333");
			}
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
			}
			lk.Inventory(1);
			Log.d("r2000_kt45", "inventory thread is stoped************************************************");
		}
	}
	
	private class get_invdata extends Thread {
			@Override
			public void run() {
				super.run();
				while (inSearch) {
					Message msg = new Message();
					ArrayList<Tag_Data> tg = get_inventory_data();
					if (tg != null) {
						msg.what = 1;
						msg.obj = tg;
						h.sendMessage(msg);
					}
				}
				Log.d("r2000_kt45", "get data thread is interrupted*************************************");
			}
	}
	
	public void reg_handler(Handler hd)
	{
		h = hd;
	}

	public void resumeOpenDev(){
		pw.PowerOnDevice();
	}
	public int OpenDev() {
		int fd;
		pw.PowerOffDevice();
		fd = pw.PowerOnDevice();
		if(fd != 0)
		{
			Log.e("r2000_kt45", "power on returns null");
			return -1;
		}
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
		}
		Log.d("r2000_kt45", "power ok");
		fd = lk.Radio_Initialization();
		if(fd != Result.RFID_STATUS_OK.getValue())
		{
			Log.e("r2000_kt45", "native open returns null");
			pw.PowerOffDevice();
			return -1;
		}
		Log.d("r2000_kt45", "init ok");
		fd = lk.open_serial(SERIALPORT);
		if(fd != Result.RFID_STATUS_OK.getValue())
		{
			Log.e("r2000_kt45", "open serial port returns null");
			//lk.DestroyRadioFuncIntegration();
			pw.PowerOffDevice();
		}
		Log.d("r2000_kt45", "serial ok");
		return 0;
	}

	public void CloseDev() {
		pw.PowerOffDevice();
		lk.close_serial();
		lk.DestroyRadioFuncIntegration();


	}

	public void postCloseDev(){
		pw.PowerOffDevice();
	}


	private int btoi(byte a) {
		return (a < 0 ? a + 256 : a);
	}

	public static final int FAST_MODE = 0;
	public static final int SMART_MODE = 1;
	public static final int LOW_POWER_MODE = 2;
	public static final int USER_MODE = 3;
	
	public int set_inventory_mode(int m)
	{
		int res = Result.RFID_ERROR_FAILURE.getValue();
		if((m >= 0) && (m <= 3))
		{
			switch(m)
			{
				case FAST_MODE:
					res = lk.Radio_SetInvMode(InvMode.HighSpeedMode);
					break;
				case SMART_MODE:
					res = lk.Radio_SetInvMode(InvMode.IntelligentMode);
					break;
				case LOW_POWER_MODE:
					res = lk.Radio_SetInvMode(InvMode.LowPowerMode);
					break;
				case USER_MODE:
					res = lk.Radio_SetInvMode(InvMode.UserDefined);
					break;
			}
		}
		if(res != Result.RFID_STATUS_OK.getValue())
		{
			return -1;
		}
		return 0;
	}
	
	public int get_inventory_mode()
	{
		Rfid_Value res = new Rfid_Value();
		int m = lk.Radio_GetInvMode(res);
//		if(res.value != Result.RFID_STATUS_OK.getValue())
		if(m != Result.RFID_STATUS_OK.getValue())
		{
			return -1;
		}
//		if(m == InvMode.HighSpeedMode.getValue())
		if(res.value == InvMode.HighSpeedMode.getValue())
		{
			return FAST_MODE;
		}
//		else if(m == InvMode.IntelligentMode.getValue())
		else if(res.value == InvMode.IntelligentMode.getValue())
		{
			return SMART_MODE;
		}
//		else if(m == InvMode.LowPowerMode.getValue())
		else if(res.value == InvMode.LowPowerMode.getValue())
		{
			return LOW_POWER_MODE;
		}
//		else if(m == InvMode.UserDefined.getValue())
		else if(res.value == InvMode.UserDefined.getValue())
		{
			return USER_MODE;
		}
		else
		{
			return -1;
		}
	}

	public void inventory_start()
	{
		if(inSearch)
		{
			return;
		}
		inSearch = true;
		invs = new inv_thread();
		invs.start();
		gd = new get_invdata();
		gd.start();
	}
	
	public void inventory_stop()
	{
		if(!inSearch)
		{
			return;
		}
		inSearch = false;
		gd.interrupt();
		invs.interrupt();
		lk.CancelOperation();
	}
	
	public static final int KILL_PW_L = 0;
	public static final int ACCESS_PW_L = 1;
	public static final int EPC_L = 2;
	public static final int TID_L = 3;
	public static final int USER_L = 4;
	
	public static final int UNLOCK = 0;
	public static final int LOCK = 1;
	public static final int P_UNLOCK = 2;
	public static final int P_LOCK = 3;
	
	private static final int vp[] = {RFID_18K6C_TAG_PWD_PERM.ACCESSIBLE.getValue(), RFID_18K6C_TAG_PWD_PERM.SECURED_ACCESSIBLE.getValue(), RFID_18K6C_TAG_PWD_PERM.ALWAYS_ACCESSIBLE.getValue(), RFID_18K6C_TAG_PWD_PERM.ALWAYS_NOT_ACCESSIBLE.getValue(),};
	private static final int va[] = {RFID_18K6C_TAG_MEM_PERM.WRITEABLE.getValue(), RFID_18K6C_TAG_MEM_PERM.SECURED_WRITEABLE.getValue(), RFID_18K6C_TAG_MEM_PERM.ALWAYS_WRITEABLE.getValue(), RFID_18K6C_TAG_MEM_PERM.ALWAYS_NOT_WRITEABLE.getValue(),};
		
	public int setlock(int type, int area, int passwd) {
		int kp = RFID_18K6C_TAG_PWD_PERM.NO_CHANGE.getValue();
		int ap = RFID_18K6C_TAG_PWD_PERM.NO_CHANGE.getValue();
		int ta = RFID_18K6C_TAG_MEM_PERM.NO_CHANGE.getValue();
		int ea = RFID_18K6C_TAG_MEM_PERM.NO_CHANGE.getValue();
		int ua = RFID_18K6C_TAG_MEM_PERM.NO_CHANGE.getValue();
		
		int res = Result.RFID_ERROR_FAILURE.getValue();
		if((type >= 0) && (type <= 3) && (area >= 0) && (area <= 4))
		{
			switch(area)
			{
				case KILL_PW_L:
					kp = vp[type];
					break;
				case ACCESS_PW_L:
					ap = vp[type];
					break;
				case EPC_L:
					ea = va[type];
					break;
				case TID_L:
					ta = va[type];
					break;
				case USER_L:
					ua = va[type];
					break;
			}	
			res = lk.Radio_LockTag(passwd, ap, kp, ea, ta, ua, 1);
		}
		if(res != Result.RFID_STATUS_OK.getValue())
		{
			return -1;
		}
		return 0;
	}

	public int setkill(int ap, int kp) {
		int res = lk.Radio_KillTag(ap, kp, 1);
		if(res != Result.RFID_STATUS_OK.getValue())
		{
			return -1;
		}
		return 0;
	}

	public class Tag_Data {
		Tag_Data(byte[] n_tid, byte[] n_epc) {
			tid = n_tid;
			epc = n_epc;
		}

		public byte[] tid;
		public byte[] epc;
	}

	private ArrayList<Tag_Data> get_inventory_data()
	{
		ArrayList<Tag_Data> cx = new ArrayList<Tag_Data>();
		St_Inv_Data[] arg0 = new St_Inv_Data[512];
		int sn = lk.GetInvData(arg0);
		if((sn > 0) && (arg0 != null))
		{
			//Log.d("r2000_kt45", "get " + sn + " cards XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
			for(int i = 0; i < sn; i++)
			{
				//Log.e("r2000_kt45", "epc leng is " + arg0[i].nLength + " tid leng is " + arg0[i].tidLength);
				if((arg0[i].nLength > 0) && (arg0[i].nLength < 66))
				{
					byte[] n_epc = new byte[arg0[i].nLength];
					byte[] n_tid;
					System.arraycopy(arg0[i].INV_Data, 0, n_epc, 0, n_epc.length);
					if(arg0[i].tidLength == 12)
					{
						n_tid = new byte[arg0[i].tidLength];
						System.arraycopy(arg0[i].TID_Data, 0, n_tid, 0, n_tid.length);
					}
					else
					{
						n_tid = null;
					}
					cx.add(new Tag_Data(n_tid, n_epc));
				}
			}
			return cx;
		}
		return null;
	}
	
	public int inventory_show_tid(boolean b)
	{
		int res;
		if(b)
		{
			res = lk.Radio_SetInvTagArea(RFID_INVENTORY_TAG_AREA.RFID_INVENTORY_TAG_TID);
		}
		else
		{
			res = lk.Radio_SetInvTagArea(RFID_INVENTORY_TAG_AREA.RFID_INVENTORY_TAG_EPC);
		}
		if(res != Result.RFID_STATUS_OK.getValue())
		{
			return -1;
		}
		return 0;
	}
	
	public int set_inventory_time(int work_t, int rest_t)
	{
		Single_Inventory_Time_Config t = new Single_Inventory_Time_Config(work_t, rest_t);
		int res = lk.Radio_SetSingleInvTimeConfig(t);
		if(res != Result.RFID_STATUS_OK.getValue())
		{
			return -1;
		}
		return 0;
	}

	public class INV_TIME {
		INV_TIME(int wt, int rt) {
			work_t = wt;
			rest_t = rt;
		}

		public int work_t;
		public int rest_t;
	}

	public INV_TIME get_inventory_time()
	{
		Single_Inventory_Time_Config t = new Single_Inventory_Time_Config();
		int res = lk.Radio_GetSingleInvTimeConfig(t);
		if(res != Result.RFID_STATUS_OK.getValue())
		{
			return null;
		}
		return new INV_TIME(t.iWorkTime, t.iRestTime);
	}
	
	public static final int RESERVED_A = 0;
	public static final int EPC_A = 1;
	public static final int TID_A = 2;
	public static final int USER_A = 3;
	
	public byte[] read_area(int area, int addr, int count, int passwd) {
		Rfid_Value res = new Rfid_Value();
		if((area > 3) || (area < 0) || ((count % 2) != 0))
		{
			return null;
		}
		int bk = USER_A;
		switch(area)
		{
			case RESERVED_A:
				bk = MemoryBank.RESERVED.getValue();
				break;
			case EPC_A:
				bk = MemoryBank.EPC.getValue();
				break;
			case TID_A:
				bk = MemoryBank.TID.getValue();
				break;
			case USER_A:
				bk = MemoryBank.USER.getValue();
			break;
		}
		char[] kn = lk.Radio_ReadTag(count / 2, addr, bk, passwd, res, 1);
		if((res.value == Result.RFID_STATUS_OK.getValue()) && (kn != null))
		{
			byte[] s = new byte[count];
			for(int i = 0; i < count / 2; i++)
			{
				s[i * 2 + 1] = (byte)(kn[i] & 0xff);
				s[i * 2] = (byte)((kn[i] >> 8) & 0xff);
			}
			return s;
		}
		Log.e("r2000_kt45", "reval is " + res.value);
		return null;
	}
	
	public static final int REGION_CHINA_840_845 = 0;
	public static final int REGION_CHINA_920_925 = 1;
	public static final int REGION_CHINA_902_928 = 2;
	public static final int REGION_EURO_865_868 = 3;
	
	public int set_fix_freq(double freq, int region)
	{
		int res = Result.RFID_ERROR_FAILURE.getValue();
		switch(region)
		{
			case REGION_CHINA_840_845:
				if((freq <= 845) && (freq >= 840))
				{
					res = lk.Radio_SetSingleFrequency(freq, RFID_18K6C_COUNTRY_REGION.China840_845);
				}
				break;
			case REGION_CHINA_920_925:
				if((freq >= 920) && (freq <= 925))
				{
					res = lk.Radio_SetSingleFrequency(freq, RFID_18K6C_COUNTRY_REGION.China920_925);
				}
				break;
			case REGION_CHINA_902_928:
				if((freq >= 902) && (freq <= 928))
				{
					res = lk.Radio_SetSingleFrequency(freq, RFID_18K6C_COUNTRY_REGION.Open_Area902_928);
				}
				break;
			case REGION_EURO_865_868:
				if((freq >= 865) && (freq <= 868))
				{
					res = lk.Radio_SetSingleFrequency(freq, RFID_18K6C_COUNTRY_REGION.Europe_Area);
				}
				break;
		}
		if(res != Result.RFID_STATUS_OK.getValue())
		{
			return -1;
		}
		return 0;
	}
	
	public double get_fix_freq()
	{
		Rfid_dValue rv = new Rfid_dValue();
		int x = lk.Radio_GetSingleFrequency(rv);
		if(x != Result.RFID_STATUS_OK.getValue())
		{
			return -1.0;
		}
		return rv.value;
	}
	
	public int MakeSetValid()
	{
		int j = lk.Radio_MacReset();
		if(j != Result.RFID_STATUS_OK.getValue())
		{
			return -1;
		}
		return 0;
	}
	
	public int set_freq_region(int region)
	{
		int res = Result.RFID_ERROR_FAILURE.getValue();
		switch(region)
		{
			case REGION_CHINA_840_845:
				res = lk.Radio_MacSetRegion(RFID_18K6C_COUNTRY_REGION.China840_845);
				break;
			case REGION_CHINA_920_925:
				res = lk.Radio_MacSetRegion(RFID_18K6C_COUNTRY_REGION.China920_925);
				break;
			case REGION_CHINA_902_928:
				res = lk.Radio_MacSetRegion(RFID_18K6C_COUNTRY_REGION.Open_Area902_928);
				break;
			case REGION_EURO_865_868:
				res = lk.Radio_MacSetRegion(RFID_18K6C_COUNTRY_REGION.Europe_Area);
				break;
		}
		if(res != Result.RFID_STATUS_OK.getValue())
		{
			return -1;
		}
		return 0;
	}
	
	public int get_freq_region()
	{
		Rfid_Value rv = new Rfid_Value();
		int rs = lk.Radio_MacGetRegion(rv);
		if(rs != Result.RFID_STATUS_OK.getValue())
		{
			return -1;
		}
		if(rv.value == RFID_18K6C_COUNTRY_REGION.China840_845.getValue())
		{
			return REGION_CHINA_840_845;
		}
		else if(rv.value == RFID_18K6C_COUNTRY_REGION.China920_925.getValue())
		{
			return REGION_CHINA_920_925;
		}
		else if(rv.value == RFID_18K6C_COUNTRY_REGION.Open_Area902_928.getValue())
		{
			return REGION_CHINA_902_928;
		}
		else if(rv.value == RFID_18K6C_COUNTRY_REGION.Europe_Area.getValue())
		{
			return REGION_EURO_865_868;
		}
		else 
		{
			return -1;
		}
	}
	
	public int write_area(int area, int addr, int passwd, byte[] content) {
		
		int bk = USER_A;
		int status = Result.RFID_ERROR_FAILURE.getValue();
		
		if((area >= 0) && (area <= 3) && ((content.length % 2) == 0))
		{
			switch(area)
			{
				case RESERVED_A:
					bk = MemoryBank.RESERVED.getValue();
					break;
				case EPC_A:
					bk = MemoryBank.EPC.getValue();
					break;
				case TID_A:
					bk = MemoryBank.TID.getValue();
					break;
				case USER_A:
					bk = MemoryBank.USER.getValue();
				break;
			}
			char[] s = new char[content.length / 2];
			for(int i = 0; i < content.length / 2; i++)
			{
				s[i] = (char)(btoi(content[i * 2 + 1]) + (btoi(content[i * 2]) << 8));
			}
			status = lk.Radio_WriteTag(content.length / 2, addr, bk, passwd, s, 1);
		}
		if(status != Result.RFID_STATUS_OK.getValue())
		{
			Log.e("r2000_kt45", "return status is " + status);
			return -1;
		}
		return 0;
	}
	
	public static final int ANTENNA_P_MIN = 0;
	public static final int ANTENNA_P_MAX = 30;

	public int set_antenna_power(int power) 
	{
		int res = Result.RFID_ERROR_FAILURE.getValue();
		if((power >= ANTENNA_P_MIN) && (power <= ANTENNA_P_MAX))
		{
			res = lk.Radio_SetAntennaPower(power * 10);
		}
		if(res != Result.RFID_STATUS_OK.getValue())
		{
			return -1;
		}
		return 0;
	}
	
	public int get_antenna_power()
	{
		Rfid_Value rv = new Rfid_Value();
		int p = lk.Radio_GetAntennaPower(rv);
//		if(rv.value != Result.RFID_STATUS_OK.getValue())
		if(p != Result.RFID_STATUS_OK.getValue())
		{
			return -1;
		}
		return rv.value / 10;
	}
	
	public static final int DSB_ASK_M0_40KHZ = 0;
	public static final int PR_ASK_M2_250KHZ = 1;
	public static final int PR_ASK_M2_300KHZ = 2;
	public static final int DSB_ASK_M0_400KHZ = 3;
	
	public int set_link_prof(int pf)
	{
		int res = Result.RFID_ERROR_FAILURE.getValue();
		if((pf >= 0) && (pf <= 4))
		{
			res = lk.Radio_SetCurrentLinkProfile(pf);
		}
		if(res != Result.RFID_STATUS_OK.getValue())
		{
			return -1;
		}
		return 0;
	}
	
	public int get_link_prof()
	{
		Rfid_Value rv = new Rfid_Value();
		int p = lk.Radio_GetCurrentLinkProfile(rv);
		if(p != Result.RFID_STATUS_OK.getValue())
		{
			return -1;
		}
		return rv.value;
	}
	
	public int select_card(byte[] epc)
 	{
		Log.i("r2000_kt45", "epc leng is " + epc.length);
		for(byte i : epc)
		{
			Log.i("r2000_kt45", String.format("%02x", i));
		}
		int rv = SetMask(lk, epc, epc.length * 2);
		if(rv != Result.RFID_STATUS_OK.getValue())
		{
			Log.e("r2000_kt45", "SetMask failed");
			return -1;
		}
		return 0;
	}
	
	private int SetMask(Linkage link, byte[] bytetemp, int length) {
		int status = Result.RFID_STATUS_OK.getValue();
		status = link.Radio_SetCurrentSingulationAlgorithm(0);
		if (status != Result.RFID_STATUS_OK.getValue())
			return status;
		Log.i("r2000_kt45", "setcurrent ok");
		TagGroup group = new TagGroup();
		status = link.Radio_GetQueryTagGroup(group);
		if (status != Result.RFID_STATUS_OK.getValue())
			return status;
		Log.i("r2000_kt45", "getquery ok");
		group.session = 2;
		status = link.Radio_SetQueryTagGroup(group);
		if (status != Result.RFID_STATUS_OK.getValue())
			return status;
		Log.i("r2000_kt45", "setquery ok");
		FixedQParms rsfp = new FixedQParms();
		rsfp.qValue = 4;
		rsfp.retryCount = 0;
		rsfp.toggleTarget = 0;
		rsfp.repeatUntiNoTags = 0;// wxy
		status = link.Radio_SetSingulationAlgorithmFiParameters(rsfp);
		if (status != Result.RFID_STATUS_OK.getValue())
			return status;
		int count = length * 4;
		Log.i("r2000_kt45", "---maskControlloer---" + "-----------------" + bytetemp.length);
		int offset = 32;
		SelectCriteria selectCriteria = new SelectCriteria();
		selectCriteria.countCriteria = 1;
		// bank
		selectCriteria.mask_bank = 1;
		// offset
		selectCriteria.mask_offset = offset;
		// count
		selectCriteria.mask_count = count;
		// mask
		for (int i = 0; i < bytetemp.length; i++) {
			selectCriteria.mask_mask[i] = bytetemp[i];
		}
		// target
		selectCriteria.action_target = 2;
		// action
		selectCriteria.action_action = 0;
		// trunction
		selectCriteria.action_enableTruncate = 0;
		status = link.Radio_SetSelectCriteria(selectCriteria, 0);
		Log.i("r2000_kt45", "=======================================" + status);
		return status;
	}
	
	private int SetAlgorithmDyParameters(Linkage link) {
		int status = Result.RFID_STATUS_OK.getValue();
		status = link.Radio_SetCurrentSingulationAlgorithm(1);
		if (status != Result.RFID_STATUS_OK.getValue())
			return status;
		TagGroup group = new TagGroup();
		status = link.Radio_GetQueryTagGroup(group);
		if (status != Result.RFID_STATUS_OK.getValue())
			return status;
		group.session = 2;
		status = link.Radio_SetQueryTagGroup(group);
		if (status != Result.RFID_STATUS_OK.getValue())
			return status;
		DynamicQParms dynamic = new DynamicQParms();
		dynamic.minQValue = 0;
		dynamic.maxQValue = 15;
		dynamic.retryCount = 0;
		dynamic.startQValue = 4;
		dynamic.thresholdMultiplier = 4;
		dynamic.toggleTarget = 1;
		status = link.Radio_SetSingulationAlgorithmDyParameters(dynamic);
		if (status != Result.RFID_STATUS_OK.getValue())
			return status;
		return status;
	}

	/*public ArrayList<byte[]> GetEpcList()
	{
		invs = new inv_thread();
		invs.start();
		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
		}
		
		ArrayList<byte[]> ns = null;
		ArrayList<Tag_Data> tg = get_inventory_data();
		for(int i = 0; i < 10000; i++)
		{
			if(tg != null)
			{
				Log.i("r2000_kt45", "in " + i + " times");
				break;
			}
			else
			{
				tg = get_inventory_data();
			}
		}
		if(tg != null)
		{
			ns = new ArrayList<byte[]>();
			for(int ss = 0; ss < tg.size(); ss++)
			{
				ns.add(tg.get(ss).epc);
			}
		}
		invs.interrupt();
		lk.CancelOperation();
		return ns;
	}
	
	public byte[] ReadEpc(byte[] epc, int position, int length, byte[] password)
	{
		if(password.length != 4)
		{
			Log.e("r2000", "password length is wrong");
			return null;
		}
		
		if(select_card(epc) < 0)
		{
			Log.e("r2000", "select card failed");
			return null;
		}

		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
		}

		int pswd = (int)(((long)btoi(password[0])) << 24 + btoi(password[1]) << 16 + btoi(password[2]) << 8 + btoi(password[3]));
		
		return read_area(EPC_A, position, length, pswd);
	}
	
	
	public byte[] ReadUser(byte[] epc, int position, int length, byte[] password)
	{
		if(password.length != 4)
		{
			Log.e("r2000", "password length is wrong");
			return null;
		}
		
		if(select_card(epc) < 0)
		{
			Log.e("r2000", "select card failed");
			return null;
		}

		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
		}

		int pswd = (int)(((long)btoi(password[0])) << 24 + btoi(password[1]) << 16 + btoi(password[2]) << 8 + btoi(password[3]));
		
		return read_area(USER_A, position, length, pswd);
	}
	
	public int WriteUser(byte[] epc, int position, int length, byte[] password, byte[] content)
	{
		if(password.length != 4)
		{
			Log.e("r2000", "password length is wrong");
			return -1;
		}
		
		if(content.length < length)
		{
			Log.e("r2000", "input content is shorter than input length");
			return -1;
		}
		
		if(select_card(epc) < 0)
		{
			Log.e("r2000", "select card failed");
			return -1;
		}

		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
		}
		
		byte[] cnt = new byte[length];
		System.arraycopy(content, 0, cnt, 0, cnt.length);

		int pswd = (int)(((long)btoi(password[0])) << 24 + btoi(password[1]) << 16 + btoi(password[2]) << 8 + btoi(password[3]));
		
		return write_area(r2000_native.USER_A, position, pswd, cnt);
	}*/
	
}