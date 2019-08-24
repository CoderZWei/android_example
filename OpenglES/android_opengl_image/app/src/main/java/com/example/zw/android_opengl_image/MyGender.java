package com.example.zw.android_opengl_image;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.EGL14;
import android.opengl.EGLConfig;
import android.opengl.EGLContext;
import android.opengl.EGLDisplay;
import android.opengl.EGLSurface;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.opengl.Matrix;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.print.PrinterId;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.Surface;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL;
import javax.microedition.khronos.opengles.GL10;

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
public class MyGender extends HandlerThread {
    private Bitmap mBitmap;
    private EGLDisplay mEglDisplay=EGL14.EGL_NO_DISPLAY;
    private EGLContext mEglContext=EGL14.EGL_NO_CONTEXT;
    private EGLConfig mEglConfig=null;
    private int mProgram;

    private float[] mProjectMatrix=new float[16];
    private float[] mViewMatrix=new float[16];
    private float[] mMvpMatrix=new float[16];
    private final String vertexShaderCode=
            "attribute vec4 vPosition;" +
                    "attribute vec2 vCoordinate;"+
                    "uniform mat4 vMatrix;"+
                    "varying vec2 aCoordinate;" +
                    "void main(){" +
                    "    gl_Position=vMatrix*vPosition;" +
                    "    aCoordinate=vCoordinate;" +
                    "}";
    //texture2D是内置函数，用于2D纹理取样，根据纹理取样器和纹理坐标，可以得到当前纹理取样得到的像素颜色
    private final String fragmentShaderCode=
            "precision mediump float;" +
                    "uniform sampler2D vTexture;" +
                    "varying vec2 aCoordinate;" +
                    "void main(){" +
                    "gl_FragColor=texture2D(vTexture,aCoordinate);" +
                    "}";
    //顶点坐标
    private final float[] sCoords={
            -1.0f,1.0f,    //左上角
            -1.0f,-1.0f,   //左下角
            1.0f,1.0f,     //右上角
            1.0f,-1.0f     //右下角
    };
    //纹理坐标
    private final float[] tCoords={
            0.0f,0.0f,
            0.0f,1.0f,
            1.0f,0.0f,
            1.0f,1.0f,
    };
    private FloatBuffer mVertextBuffer;
    private FloatBuffer mTextureBuffer;
    public MyGender(String name) {
        super(name);
        try {
            FileInputStream fis=new FileInputStream(Environment.getExternalStorageDirectory().getAbsolutePath()+name);
            mBitmap=BitmapFactory.decodeStream(fis);
        }catch (Exception e){
           // Toast.makeText(MainActivity.class,e.getMessage(),Toast.LENGTH_LONG).show();
        }
        ByteBuffer bb=ByteBuffer.allocateDirect(sCoords.length*4);
        bb.order(ByteOrder.nativeOrder());
        mVertextBuffer=bb.asFloatBuffer();
        mVertextBuffer.put(sCoords);
        mVertextBuffer.position(0);
        ByteBuffer cc=ByteBuffer.allocateDirect(tCoords.length*4);
        cc.order(ByteOrder.nativeOrder());
        mTextureBuffer=cc.asFloatBuffer();
        mTextureBuffer.put(tCoords);
        mTextureBuffer.position(0);
    }
    private int createTexture(){
        int[] texture=new int[1];
        if(mBitmap!=null && !mBitmap.isRecycled()){
            //生成纹理
            GLES20.glGenTextures(1,texture,0);
            //绑定纹理
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,texture[0]);
            //设置缩小过滤为使用纹理中坐标最接近的一个像素的颜色作为需要绘制的像素颜色
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER,GLES20.GL_NEAREST);
            //设置放大过滤为使用纹理中坐标最接近的若干个颜色，通过加权平均算法得到需要绘制的像素颜色
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_MAG_FILTER,GLES20.GL_LINEAR);
            //设置环绕方向S，截取纹理坐标到[1/2n,1-1/2n]。将导致永远不会与border融合
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,GLES20.GL_CLAMP_TO_EDGE);
            //设置环绕方向T，截取纹理坐标到[1/2n,1-1/2n]。将导致永远不会与border融合
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,GLES20.GL_CLAMP_TO_EDGE);
            //根据以上指定的参数，生成一个2D纹理
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, mBitmap, 0);
            return texture[0];
        }
        return 0;
    }
    public void init(){
        mEglDisplay=EGL14.eglGetDisplay(EGL14.EGL_DEFAULT_DISPLAY);
        int[] version=new int[2];
        if(!EGL14.eglInitialize(mEglDisplay,version,0,version,1)){
            throw  new RuntimeException("EGL error");
        }
        // 获取FrameBuffer格式和能力
        int []configAttribs = {
                EGL14.EGL_BUFFER_SIZE, 32,
                EGL14.EGL_ALPHA_SIZE, 8,
                EGL14.EGL_BLUE_SIZE, 8,
                EGL14.EGL_GREEN_SIZE, 8,
                EGL14.EGL_RED_SIZE, 8,
                EGL14.EGL_RENDERABLE_TYPE, EGL14.EGL_OPENGL_ES2_BIT,
                EGL14.EGL_SURFACE_TYPE, EGL14.EGL_WINDOW_BIT,
                EGL14.EGL_NONE
        };
        int []numConfigs = new int[1];
        EGLConfig[]configs = new EGLConfig[1];
        if (!EGL14.eglChooseConfig(mEglDisplay, configAttribs,0, configs, 0,configs.length, numConfigs,0)) {
            throw new RuntimeException("EGL error "+EGL14.eglGetError());
        }
        mEglConfig = configs[0];
        // 创建OpenGL上下文
        int []contextAttribs = {
                EGL14.EGL_CONTEXT_CLIENT_VERSION, 2,
                EGL14.EGL_NONE
        };
        mEglContext = EGL14.eglCreateContext(mEglDisplay, mEglConfig, EGL14.EGL_NO_CONTEXT, contextAttribs,0);
        if(mEglContext== EGL14.EGL_NO_CONTEXT) {
            throw new RuntimeException("EGL error "+EGL14.eglGetError());
        }

    }

    @Override
    public synchronized void start() {
        super.start();
        new Handler(getLooper()).post(new Runnable() {
            @Override
            public void run() {
                init();
            }
        });
    }

    private int vPosition,vCoordinate,vTexture,vMatrix,vIsHalf,uXY;
    private int textureId;
    private Boolean isHalf=false;
    public void render(Surface surface,int width,int height){
        final int[] surfaceAttribs={EGL14.EGL_NONE};
        EGLSurface eglSurface=EGL14.eglCreateWindowSurface(mEglDisplay, mEglConfig, surface, surfaceAttribs, 0);
        EGL14.eglMakeCurrent(mEglDisplay, eglSurface, eglSurface, mEglContext);

        mProgram = createProgram(vertexShaderCode, fragmentShaderCode);
        vPosition=GLES20.glGetAttribLocation(mProgram,"vPosition");
        vCoordinate=GLES20.glGetAttribLocation(mProgram,"vCoordinate");
        vTexture=GLES20.glGetUniformLocation(mProgram,"vTexture");
        vMatrix=GLES20.glGetUniformLocation(mProgram,"vMatrix");
        vIsHalf=GLES20.glGetUniformLocation(mProgram,"vIsHalf");
        uXY=GLES20.glGetUniformLocation(mProgram,"uXY");

        //设置clear color颜色RGBA(这里仅仅是设置清屏时GLES20.glClear()用的颜色值而不是执行清屏)
        GLES20.glClearColor(1.0f, 0, 0, 1.0f);
        // 设置绘图的窗口(可以理解成在画布上划出一块区域来画图)
        GLES20.glViewport(0,0,width,height);
        //清屏
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
        GLES20.glUseProgram(mProgram);
        GLES20.glUniform1i(vIsHalf,isHalf?1:0);
        GLES20.glUniform1f(uXY,uXY);
        GLES20.glUniformMatrix4fv(vMatrix,1,false,mMvpMatrix,0);
        GLES20.glEnableVertexAttribArray(vPosition);
        GLES20.glEnableVertexAttribArray(vCoordinate);
        GLES20.glUniform1i(vTexture, 0);
        textureId=createTexture();
        GLES20.glVertexAttribPointer(vPosition,2,GLES20.GL_FLOAT,false,0,mVertextBuffer);
        GLES20.glVertexAttribPointer(vCoordinate,2,GLES20.GL_FLOAT,false,0,mTextureBuffer);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP,0,4);

        //禁止顶点数组的句柄
        GLES20.glDisableVertexAttribArray(vPosition);

        // 交换显存(将surface显存和显示器的显存交换)
        EGL14.eglSwapBuffers(mEglDisplay, eglSurface);

        EGL14.eglDestroySurface(mEglDisplay, eglSurface);
    }

    private int createProgram(String vertexShaderCode, String fragmentShaderCode) {
        //加载顶点着色器
        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        if (vertexShader == 0) {
            return 0;
        }
        // 加载片元着色器
        int pixelShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);
        if (pixelShader == 0) {
            return 0;
        }
        int program = GLES20.glCreateProgram();
        // 若程序创建成功则向程序中加入顶点着色器与片元着色器
        if (program != 0) {
            // 向程序中加入顶点着色器
            GLES20.glAttachShader(program, vertexShader);
            // 向程序中加入片元着色器
            GLES20.glAttachShader(program, pixelShader);
            // 链接程序
            GLES20.glLinkProgram(program);
            // 存放链接成功program数量的数组
            int[] linkStatus = new int[1];
            // 获取program的链接情况
            GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, linkStatus, 0);
            // 若链接失败则报错并删除程序
            if (linkStatus[0] != GLES20.GL_TRUE) {
                Log.e("ES20_ERROR", "Could not link program: ");
                Log.e("ES20_ERROR", GLES20.glGetProgramInfoLog(program));
                GLES20.glDeleteProgram(program);
                program = 0;
            }
        }else{
            Log.e("program error","equal error");
        }
        return program;
    }

    private int loadShader(int type, String shaderCode) {
        int shader=GLES20.glCreateShader(type);
        if(shader!=0){
            GLES20.glShaderSource(shader,shaderCode);
            GLES20.glCompileShader(shader);
            int[] compiled = new int[1];
            // 获取Shader的编译情况
            GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compiled, 0);
            if (compiled[0] == 0) {//若编译失败则显示错误日志并删除此shader
                Log.e("ES20_ERROR", "Could not compile shader " + type + ":");
                Log.e("ES20_ERROR", GLES20.glGetShaderInfoLog(shader));
                GLES20.glDeleteShader(shader);
                shader = 0;
            }
        }else {
            Log.e("shader error","equal zero");
        }
        return shader;
    }

    public void adjustView(int width,int height){
        GL10 gl;
        GLES20.glViewport(0,0,width,height);
        int w=mBitmap.getWidth();
        int h=mBitmap.getHeight();
        float sWH=w/(float)h;
        float sWidthHeight=width/(float)height;
        if(width>height){
            if(sWH>sWidthHeight){
                Matrix.orthoM(mProjectMatrix,0,-sWidthHeight*sWH,sWidthHeight*sWH,-1,1,3,7);
            }else {
                Matrix.orthoM(mProjectMatrix, 0, -sWidthHeight/sWH,sWidthHeight/sWH, -1,1, 3, 7);
            }
        }else {
            if(sWH>sWidthHeight){
                Matrix.orthoM(mProjectMatrix, 0, -1, 1, -1/sWidthHeight*sWH, 1/sWidthHeight*sWH,3, 7);
            }else{
                Matrix.orthoM(mProjectMatrix, 0, -1, 1, -sWH/sWidthHeight, sWH/sWidthHeight,3, 7);
            }
        }
        //设置相机位置
        Matrix.setLookAtM(mViewMatrix, 0, 0, 0, 7.0f, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
        //计算变换矩阵
        Matrix.multiplyMM(mMvpMatrix,0,mProjectMatrix,0,mViewMatrix,0);
    }
}
