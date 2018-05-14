package com.example.mizuk.bluetoothdemo;

import android.media.MediaPlayer;

import java.io.File;

public class music {
    private MediaPlayer myPlayer;
    private boolean flag=true;
    public void playmusic(){
        /*因为没有sd卡 暂时*/
        //myPlayer=MediaPlayer.create(this,R.raw.test1);
        if(new File("/storage/emulated/0/test1.mp3").exists()){
            myPlayer=new MediaPlayer();

            try{

                //将信息重置
                myPlayer.reset();
                //指定音乐文件路径
                //tv.setText("进去了");
                myPlayer.setDataSource("/storage/emulated/0/test1.mp3");
                //tv.setText("进去了");
                //完成一些预备工作
                myPlayer.prepare();
                //音乐开始
                //myPlayer.start();

            }
            catch(Exception e){
                //tv.setText("进去了");
                e.printStackTrace();
            }
        }
    }
    //停止播放
    public  void pause(){
        if(myPlayer!=null&&myPlayer.isPlaying())
            myPlayer.pause();
    }
    //播放
    public void play(){
        if(myPlayer!=null)
            myPlayer.start();
    }
}
