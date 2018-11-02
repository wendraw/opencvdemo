package com.example.james_000.opencvdemo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import org.opencv.core.CvType;
import org.opencv.core.Size;

public class MainActivity extends AppCompatActivity {
    Button btn_grey;
    Button btn_edge;
    Button btn_origin;
//    Button btn_grab;
    Bitmap srcBitmap;
    Bitmap grayBitmap;
    Bitmap edgeBitmap;
    ImageView imgView;
    private static boolean flag = true;
    private static boolean isFirst = true;
    private static final String TAG = "MainActivity";



    //OpenCV库加载并初始化成功后的回调函数
    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {

        @Override
        public void onManagerConnected(int status) {
            // TODO Auto-generated method stub
            switch (status){
                case BaseLoaderCallback.SUCCESS:
                    Log.i(TAG, "成功加载");
                    break;
                default:
                    super.onManagerConnected(status);
                    Log.i(TAG, "加载失败");
                    break;
            }

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
        btn_grey.setOnClickListener(new GreyClickListener());
        btn_edge.setOnClickListener(new EdgeClickListener());
        btn_origin.setOnClickListener(new OriginClickListener());
//        btn_grab.setOnClickListener(new GrabClickListener());
    }
    public void initUI(){
        btn_grey = (Button)findViewById(R.id.btn_gray_process);
        btn_edge  = (Button)findViewById(R.id.btn_edge_process);
        btn_origin = (Button)findViewById(R.id.btn_origin_process);
//        btn_grab = (Button)findViewById(R.id.btn_grab_process);
        imgView = (ImageView)findViewById(R.id.imgview);
        Log.i(TAG, "initUI sucess...");
        srcBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.wujing);
        imgView.setImageBitmap(srcBitmap);


    }

    public void Gray(){
        Mat rgbMat = new Mat();
        Mat grayMat = new Mat();
        Utils.bitmapToMat(srcBitmap, rgbMat);//convert original bitmap to Mat, R G B.
        grayBitmap = Bitmap.createBitmap(srcBitmap.getWidth(), srcBitmap.getHeight(), Bitmap.Config.RGB_565);
        Imgproc.cvtColor(rgbMat, grayMat, Imgproc.COLOR_RGB2GRAY);//rgbMat to gray grayMat
        Utils.matToBitmap(grayMat, grayBitmap); //convert mat to bitmap
    }

    public void Edge(){

        Size dSize = new Size((double) srcBitmap.getWidth(), (double) srcBitmap.getHeight());
        Mat rgbMat = new Mat();
        Mat img = new Mat(dSize, CvType.CV_8SC1);
        Mat edgeMat = new Mat();
        Utils.bitmapToMat(srcBitmap, rgbMat);//convert original bitmap to Mat, R G B.
        edgeBitmap = Bitmap.createBitmap(srcBitmap.getWidth(), srcBitmap.getHeight(), Bitmap.Config.RGB_565);
        rgbMat.convertTo(img, CvType.CV_8SC1);
        Imgproc.Canny(rgbMat, edgeMat, 50, 100);
        Utils.matToBitmap(edgeMat, edgeBitmap); //convert mat to bitmap
    }


//    public void Grab(){
//        Mat img = new Mat();//缩小图片尺寸
//        Bitmap bm = Bitmap.createScaledBitmap(srcBitmap,srcBitmap.getWidth()/10,srcBitmap.getHeight()/10,true);//bitmap->mat
//        Utils.bitmapToMat(bm, img);//转成CV_8UC3格式
//        Imgproc.cvtColor(img, img, Imgproc.COLOR_RGBA2RGB);//设置抠图范围的左上角和右下角
//        Point tl=new Point(64, 10);
//        Point br=new Point(230, 300);
//        Rect rect = new Rect(tl, br);
//        //生成遮板
//        Mat firstMask = new Mat();
//        Mat bgModel = new Mat();
//        Mat fgModel = new Mat();
//        Mat source = new Mat(1, 1, CvType.CV_8U, new Scalar(Imgproc.GC_PR_FGD));
//        Imgproc.grabCut(img, firstMask, rect, bgModel, fgModel,5, Imgproc.GC_INIT_WITH_RECT);
//        Core.compare(firstMask, source, firstMask, Core.CMP_EQ);
//        //抠图
//        Mat foreground = new Mat(img.size(), CvType.CV_8UC3, new Scalar(255, 255, 255));
//        img.copyTo(foreground, firstMask);//mat->bitmap
//        Bitmap b = Bitmap.createBitmap(srcBitmap.getWidth()/10,srcBitmap.getHeight()/10, Bitmap.Config.ARGB_8888);
//        Utils.matToBitmap(foreground,b);
//        imgView.setImageBitmap(b);
//
//    }


    private class EdgeClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Edge();
            imgView.setImageBitmap(edgeBitmap);
        }
    }

    private class OriginClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            imgView.setImageBitmap(srcBitmap);
        }
    }
    private class GreyClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Gray();
            imgView.setImageBitmap(grayBitmap);
        }
    }

//    private class GrabClickListener implements View.OnClickListener {
//
//        @Override
//        public void onClick(View v) {
//            Grab();
//        }
//    }



    @Override
    public void onResume()
    {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_4_0, this, mLoaderCallback);
        } else {
            Log.d(TAG, "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }
}