package qc.com.phonetv;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.IOException;

public class PlayActivity extends AppCompatActivity {

    private final static String TAG = "PlayActivity";
    private Channel mChannel;
    private String mChannelSourceAddr;
    private RelativeLayout mPlayerParent;
    private MediaPlayer mPlayer;
    private SurfaceHolder mSurfaceHolder;
    private SurfaceView mSurfaceView;

    private ImageView mPlayView;
    private ImageView mTransfer;
    private ProgressBar mProgressBar;

    private TextView mTxtPercent;
    private TextView mTxtInfo;
    private TextView mTxtError;

    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        Intent intent = getIntent();
        if(intent!=null){
            mChannel = (Channel) intent.getSerializableExtra("CHANNEL");
        }
        mHandler = new Handler();
        mTxtPercent = (TextView) findViewById(R.id.txt_percent);
        mTxtInfo= (TextView) findViewById(R.id.txt_info);
        mTxtError = (TextView) findViewById(R.id.txt_error);
        mTransfer = (ImageView) findViewById(R.id.img_transfer);
        mPlayerParent = (RelativeLayout) findViewById(R.id.player_parent);
        mSurfaceView = (SurfaceView) findViewById(R.id.surface_view);
        mPlayView = (ImageView) findViewById(R.id.ivew_play);
        mProgressBar = (ProgressBar) findViewById(R.id.progressbar);
        mPlayer = new MediaPlayer();
        mSurfaceHolder = mSurfaceView.getHolder();
        if(mChannel!=null){
            mChannelSourceAddr = mChannel.getSuourceAddress();
        }
        if(mChannelSourceAddr!=null){
            mPlayView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPlayView.setVisibility(View.GONE);
                    mProgressBar.setVisibility(View.VISIBLE);
                    try {
                        mPlayer.setDataSource(mChannelSourceAddr);
                        mPlayer.setDisplay(mSurfaceHolder);
                        mPlayer.prepare();
                        mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mp) {
                                mPlayer.start();
                            }
                        });

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        mPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mp, int percent) {
                Log.d(TAG, "onBufferingUpdate: percent="+percent);
                mTxtPercent.setText("percent="+percent);
            }
        });

        mPlayer.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                mTxtInfo.append("what="+what+"extra = "+extra+"\n");
                if(what==MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START||
                        what == MediaPlayer.MEDIA_INFO_BUFFERING_END){
                    mProgressBar.setVisibility(View.INVISIBLE);
                }
                if(what==MediaPlayer.MEDIA_INFO_BUFFERING_START){
                    mProgressBar.setVisibility(View.VISIBLE);
                }

                return false;
                /**
                 * MEDIA_INFO_VIDEO_RENDERING_START=3
                 */
            }
        });

        mPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                mTxtError.append("error what = "+what+"extra="+extra);
                return false;
            }
        });

        mTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getRequestedOrientation()==ActivityInfo.SCREEN_ORIENTATION_PORTRAIT){
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                } else {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                }
            }
        });

        mPlayer.setScreenOnWhilePlaying(true);
        mHandler.postDelayed(mHideMenuTask,5*1000);
        mPlayerParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mTransfer.getVisibility()==View.INVISIBLE){
                    mTransfer.setVisibility(View.VISIBLE);
                    mHandler.postDelayed(mHideMenuTask,5*1000);
                } else {


                }

            }
        });
    }

    private Runnable mHideMenuTask = new Runnable() {
        @Override
        public void run() {
            mTransfer.setVisibility(View.INVISIBLE);
        }
    };

    private Runnable mShowMenuTask = new Runnable() {
        @Override
        public void run() {
            mTransfer.setVisibility(View.VISIBLE);
        }
    };


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.d(TAG, "onConfigurationChanged"+newConfig.orientation);
        if(getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE){
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            mPlayerParent.setLayoutParams(layoutParams);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            int px = dp2px(PlayActivity.this,220);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    px);
            mPlayerParent.setLayoutParams(layoutParams);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN );
        }

    }

    @Override
    public void onBackPressed() {
        if(getRequestedOrientation()==ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            finish();
        }
    }

    /*
        * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
         */
    public int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        super.onDestroy();
        mPlayer.release();
    }
}
