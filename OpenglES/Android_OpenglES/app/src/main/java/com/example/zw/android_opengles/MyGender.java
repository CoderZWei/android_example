package com.example.zw.android_opengles;

import android.opengl.EGL14;
import android.opengl.EGLConfig;
import android.opengl.EGLContext;
import android.opengl.EGLDisplay;
import android.opengl.EGLSurface;
import android.opengl.GLES20;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.Surface;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL;
import javax.microedition.khronos.opengles.GL10;


@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
public class MyGender extends HandlerThread {
    private EGLDisplay mEglDisplay=EGL14.EGL_NO_DISPLAY;
    private EGLContext mEglContext=EGL14.EGL_NO_CONTEXT;
    private EGLConfig mEglConfig=null;
    private int mProgram;

    private int COORDS_PER_VERTEX=3;
    private float triangleCoords[]={
            -0.5f,  0.5f, 0.0f, // top left
            -0.5f, -0.5f, 0.0f, // bottom left
            0.5f, -0.5f, 0.0f, // bottom right
            0.5f,  0.5f, 0.0f  // top right
    };
    private short index[]={
            0,1,2,0,2,3
    };
    private FloatBuffer mVertexBuffer;
    private ShortBuffer mIndexBuffer;
    private final String vertexShaderCode =
            "attribute vec4 vPosition;" +
                    "uniform mat4 vMatrix;"+
                    "void main() {" +
                    "  gl_Position = vMatrix*vPosition;" +
                    "}";

    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}";

    private int mPositionHandle;
    private int mColorHandle;
    private int mMatrixHandler;
    public float[] mViewMatrix=new float[16];
    public float[] mProjectMatrix=new float[16];
    public float[] mMVPMatrix=new float[16];
    //顶点个数
    //private int mVertexCount=triangleCoords.length/COORDS_PER_VERTEX;
    //顶点之间的偏移量
    private int mVertexStride=COORDS_PER_VERTEX*4;//每个顶点四个字节
    //设置颜色，依次为红绿蓝和透明通道
    float color[] = { 1.0f, 1.0f, 1.0f, 1.0f };
    private int createProgram(String vertexSource, String fragmentSource){
        //加载顶点着色器
        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexSource);
        if (vertexShader == 0) {
            return 0;
        }
        // 加载片元着色器
        int pixelShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentSource);
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

    public MyGender(String name) {
        super(name);
        //创建顶点坐标数据缓存
        ByteBuffer bb=ByteBuffer.allocateDirect(triangleCoords.length*4);
        bb.order(ByteOrder.nativeOrder());
        mVertexBuffer=bb.asFloatBuffer();
        mVertexBuffer.put(triangleCoords);
        mVertexBuffer.position(0);

        ByteBuffer cc=ByteBuffer.allocateDirect(index.length*2);
        cc.order(ByteOrder.nativeOrder());
        mIndexBuffer=cc.asShortBuffer();
        mIndexBuffer.put(index);
        mIndexBuffer.position(0);
        /*
        int mVertextShader=loadShader(GLES20.GL_VERTEX_SHADER,vertexShaderCode);
        int mFragmentShader=loadShader(GLES20.GL_FRAGMENT_SHADER,fragmentShaderCode);
        //创建一个空的OpenGLES程序
        mProgram=GLES20.glCreateProgram();
        if(mProgram!=0){
            //将顶点着色器加入到程序
            GLES20.glAttachShader(mProgram,mVertextShader);
            //将片元着色器加入到程序
            GLES20.glAttachShader(mProgram,mFragmentShader);
            //连接到着色器程序
            GLES20.glLinkProgram(mProgram);
            // 存放链接成功program数量的数组
            int[] linkStatus = new int[1];
            // 获取program的链接情况
            GLES20.glGetProgramiv(mProgram, GLES20.GL_LINK_STATUS, linkStatus, 0);
            // 若链接失败则报错并删除程序
            if (linkStatus[0] != GLES20.GL_TRUE) {
                Log.e("ES20_ERROR", "Could not link program: ");
                Log.e("ES20_ERROR", GLES20.glGetProgramInfoLog(mProgram));
                GLES20.glDeleteProgram(mProgram);
                mProgram = 0;
            }
        }else {
            Log.e("mProgram error", "zero ");
        }
        */
    }


    public void init(){
        mEglDisplay=EGL14.eglGetDisplay(EGL14.EGL_DEFAULT_DISPLAY);
        int []version=new int[2];
        if(!EGL14.eglInitialize(mEglDisplay,version,0,version,1)){
            throw new RuntimeException("EGL error");
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

    public int loadShader(int type,String shaderCode){
        int shader=GLES20.glCreateShader(type);
        if (shader != 0) {
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
    public void render(Surface surface, int width, int height){
        final int[] surfaceAttribs = { EGL14.EGL_NONE };
        EGLSurface eglSurface = EGL14.eglCreateWindowSurface(mEglDisplay, mEglConfig, surface, surfaceAttribs, 0);
        EGL14.eglMakeCurrent(mEglDisplay, eglSurface, eglSurface, mEglContext);

        mProgram = createProgram(vertexShaderCode, fragmentShaderCode);
        //获取着色器中的属性引用id(传入的字符串就是我们着色器脚本中的属性名)
        mMatrixHandler= GLES20.glGetUniformLocation(mProgram,"vMatrix");

        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");

        mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
     
        //设置clear color颜色RGBA(这里仅仅是设置清屏时GLES20.glClear()用的颜色值而不是执行清屏)
        GLES20.glClearColor(1.0f, 0, 0, 1.0f);
        // 设置绘图的窗口(可以理解成在画布上划出一块区域来画图)
        GLES20.glViewport(0,0,width,height);
        //清屏
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
        // 使用某套shader程序
        GLES20.glUseProgram(mProgram);
        //传入顶点坐标 为画笔指定顶点位置数据(vPosition)
        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                mVertexStride, mVertexBuffer);
        //传递数据
        GLES20.glUniformMatrix4fv(mMatrixHandler,1,false,mMVPMatrix,0);
        // 允许顶点位置数据数组
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        //设置属性uColor(颜色 索引 R G B A)
        GLES20.glUniform4f(mColorHandle, 0.0f, 1.0f, 0.0f, 1.0f);
        //GLES20.glUniform4fv(mColorHandle, 1,color,0);
        //索引法绘制正方形
        GLES20.glDrawElements(GLES20.GL_TRIANGLES,index.length, GLES20.GL_UNSIGNED_SHORT,mIndexBuffer);
        //GLES20.glDrawArrays是索引法

        //禁止顶点数组的句柄
        GLES20.glDisableVertexAttribArray(mPositionHandle);

        // 交换显存(将surface显存和显示器的显存交换)
        EGL14.eglSwapBuffers(mEglDisplay, eglSurface);

        EGL14.eglDestroySurface(mEglDisplay, eglSurface);

    }
    public void release(){
        new Handler(getLooper()).post(new Runnable() {
            @Override
            public void run() {
                EGL14.eglDestroyContext(mEglDisplay,mEglContext);
                mEglContext=EGL14.EGL_NO_CONTEXT;
                mEglDisplay=EGL14.EGL_NO_DISPLAY;
                quit();
            }
        });
    }

    public void drawFrame(){
        GL10 gl10;
        //将程序加入到OpenglEL2.0环境中
        GLES20.glUseProgram(mProgram);
        //获取变换矩阵vMatrix成员句柄
        mMatrixHandler=GLES20.glGetAttribLocation(mProgram,"vMatrix");
       //指定vMatrix的值
        GLES20.glUniformMatrix4fv(mMatrixHandler,1,false,mMVPMatrix,0);
        //获取顶点着色器的vPosition成员句柄
        mPositionHandle=GLES20.glGetAttribLocation(mProgram,"vPosition");
        //启用三角形顶点的句柄
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        //准备三角形的坐标数据
        GLES20.glVertexAttribPointer(mPositionHandle,COORDS_PER_VERTEX,
                GLES20.GL_FLOAT,false,
                mVertexStride,mVertexBuffer);
        //获取片元着色器的vColor成员句柄
        mColorHandle=GLES20.glGetUniformLocation(mProgram,"vColor");
        //设置绘制三角形的颜色
        GLES20.glUniform4fv(mColorHandle,1,color,0);
        //绘制三角形
        GLES20.glDrawElements(GLES20.GL_TRIANGLES,index.length,
                GLES20.GL_UNSIGNED_SHORT,mIndexBuffer);
        //禁止顶点数据的句柄
        GLES20.glDisableVertexAttribArray(mPositionHandle);
    }
}
