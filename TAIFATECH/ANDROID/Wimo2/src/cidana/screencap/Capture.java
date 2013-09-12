package cidana.screencap;

import android.util.Log;

public class Capture
{    
	private static final String TAG = "Capture";
	private static int mLibStatus = 0;
	static {
		try{
			System.loadLibrary("screencapture");
		}catch (UnsatisfiedLinkError ule){
			Log.d(TAG,"WARNING: Capture Could not load screencapture library!\n");
			setCapStatus(-1);
			ule.printStackTrace();
		}
	}
	public byte[] mEncodedBuf = null;
	public byte[] mAudioData = null;
	
	public int cValPointer;
	public int udpPort = 0;
	public long ipAddr = 0;

	public final int
		HW_VIDEO_INTERFACE = 0,
		SW_VIDEO_FRAMEBUFFER = 1,
		SW_VIDEO_SYSTEM = 2,
		TS_STREAM_INTERFACE = 3,
		LNV_VIDEO_INTERFACE = 4,
		HS_VIDEO_INTERFACE = 5,
		ZTE_VIDEO_INTERFACE = 6;
	
	public final int
		AUDIO_METHOD_DEFAULT = 0,
		AUDIO_METHOD_SPEAKER = 1,
		AUDIO_METHOD_LNV = 2,
		AUDIO_METHOD_HS = 3,
		AUDIO_METHOD_ZTE = 4;
	
	public final int
		CURRENT_TV_ROTATE_0 = 0,
		CURRENT_TV_ROTATE_90 = 1,//CW:clockwise
		CURRENT_TV_ROTATE_270 = 2;//CCW:anticlockwise
	
	public final int
		ROTATION_0 = 0,
		ROTATION_90 = 90,
		ROTATION_180 = 180,
		ROTATION_270 = 270;
	
	public native int jniInit(int videoMode, int audioMode);
	public native int jniUnInit();
	public native int startWiMo();
	public native int stopWiMo();
	public native int sendVideoData(byte[] data, int size, int width, int height, int rotate); 
	public native int sendAudioData(byte[] data, int size, int sampleRate, int bitRate, int channels);
	public native int setTVRotate(int nTvRotate);
	public native int setVideoRotate(int nRotate);
	public native int checkFb0(); // return value: 0, means fb0 readable; !0, means fb0 can't readable.
	public native int checkSDKForHWorSW(); //return value: 0, means the SDK for HW; 1, means SDK for SW.
	
	public int javaCallback(int nOpen) {
		Log.d(TAG,"javaCallback");
		if(mListener!=null)
			mListener.onCheckChange(nOpen);
		return 0;
	}

	private WimoCheckListener mListener = null;

	public interface WimoCheckListener {
		public void onCheckChange(int nOpen);
	};

	public void setWimoCheckListener(WimoCheckListener listener) {
		mListener = listener;
	}
	public static void setCapStatus(int mLib){mLibStatus = mLib;};
	public int getCapStatus(){return mLibStatus;};
}