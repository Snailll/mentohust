package com.snail.mentohust;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;

import com.trilead.ssh2.ChannelCondition;
import com.trilead.ssh2.Connection;

import java.io.InputStream;
import java.io.OutputStream;


public class MainActivity extends ActionBarActivity {
    TextView textView1;
    ScrollView scrollView;
    Button button1;
    Button button2;
    Button button3;
    Handler handler = new Handler();
    ProgressDialog dialog;
    Notification n;
    NotificationManager nm;
    //Notification的标示ID
    private static final int ID = 10086;

    private String S_IP = "";
    private String S_Uname = "";
    private String S_Pwd = "";
    private String M_A,M_B,M_D,M_N,M_P,M_U,M_V;
    boolean M_W,M_Enable;
    public static final int DEFAULT_SSH_PORT = 22;

    @Override
    protected void onResume() {
        super.onResume();
        getInfo();
        //Toast.makeText(this, S_IP + S_Uname + S_Pwd, Toast.LENGTH_LONG).show();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getInfo();


        nm = (NotificationManager) this.getSystemService(NOTIFICATION_SERVICE);
        n = new Notification();
        n.icon = R.drawable.wifi_router;
        n.tickerText = "Mentohust Running";
        n.when = System.currentTimeMillis();
        n.flags = Notification.FLAG_NO_CLEAR;
        n.flags = Notification.FLAG_ONGOING_EVENT;

        //Toast.makeText(this, S_IP + S_Uname + S_Pwd, Toast.LENGTH_LONG).show();
        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);
        button3 = (Button) findViewById(R.id.button3);
        textView1 = (TextView) findViewById(R.id.textView1);
        scrollView =(ScrollView)findViewById(R.id.scrollview);
        button1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                textView1.setText("");
                dialog=ProgressDialog.show(MainActivity.this, "Mentohust", "正在认证", true, true);
                dialog.show();

//                Intent intent = new Intent(MainActivity.this,MainActivity.class);
//                PendingIntent pi = PendingIntent.getActivity(MainActivity.this,0,intent,0);
//                n.setLatestEventInfo(MainActivity.this,"Mentohust","Mentohust is running",pi);
//                nm.notify(ID,n);

                final StringBuffer sb = new StringBuffer();
                sb.append("mentohust");
                if(M_Enable){
                    sb.append(" -u"+M_U);
                    sb.append(" -p"+M_P);
                    sb.append(" -a"+M_A);
                    sb.append(" -b"+M_B);
                    sb.append(" -d"+M_D);
                    sb.append(" -n"+M_N);
                    sb.append(" -v"+M_V);
                    if(M_W)
                        sb.append(" -w");
                }
                sb.append("\n");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        execute(sb.toString());
                    }
                }).start();
            }
        });
        button2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                textView1.setText("");
                dialog=ProgressDialog.show(MainActivity.this,"Mentohust","正在退出认证",true,true);
                dialog.show();
                // nm.cancel(ID);
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        execute("mentohust -k\n");
                        System.out.print("why cannot exit????");

                    }
                }).start();
            }
        });
        button3.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                startActivity(new Intent(MainActivity.this, SettingActivity.class));
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(MainActivity.this, SettingActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        new AlertDialog.Builder(this).setTitle("退出Mentohust").setNegativeButton("否", null).setPositiveButton("是", new AlertDialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                dialog=ProgressDialog.show(MainActivity.this,"Mentohust","正在退出认证",true,true);
                dialog.show();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //execute("mentohust -k\n");
                        Intent exit = new Intent(Intent.ACTION_MAIN);
                        exit.addCategory(Intent.CATEGORY_HOME);
                        exit.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(exit);
                        System.exit(0);
                        System.out.print("why cannot exit????");

                    }
                }).start();
            }
        }).show();
    }

    void getInfo(){

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        S_IP = prefs.getString("server_ip", "192.168.1.1");
        S_Uname = prefs.getString("server_username", "root");
        S_Pwd = prefs.getString("server_password", "admin");
        M_U = prefs.getString("mentohust_u","");
        M_P = prefs.getString("mentohust_p","");
        M_N = prefs.getString("mentohust_n","");
        M_B = prefs.getString("mentohust_b","");
        M_D = prefs.getString("mentohust_d","");
        M_V = prefs.getString("mentohust_v","");
        M_A = prefs.getString("mentohust_a","");
        M_W = prefs.getBoolean("mentohust_w", false);
        M_Enable = prefs.getBoolean("mentohust_enable",false);
    }


    public void execute(final String command) {
        Connection conn=null;
        com.trilead.ssh2.Session sess=null;
        InputStream stdout=null;
        OutputStream stdin=null;
        InputStream stderr;
        final int conditions = ChannelCondition.STDOUT_DATA
                | ChannelCondition.STDERR_DATA
                | ChannelCondition.CLOSED
                | ChannelCondition.EOF;

        try {
            conn = new Connection(S_IP,DEFAULT_SSH_PORT);
            conn.connect();
            boolean isAuth = conn.authenticateWithPassword(S_Uname, S_Pwd);
            if(!isAuth){
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                        Toast.makeText(MainActivity.this,"路由器用户名或密码错误",Toast.LENGTH_LONG).show();
                    }
                });

            }else{
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                    }
                });
                sess = conn.openSession();
                sess.requestPTY("term");
                sess.startShell();

                stdin = sess.getStdin();

                stderr = sess.getStderr();
                stdout = sess.getStdout();

                sess.waitForCondition(conditions,0);

                stdin.write(command.getBytes());
                stdin.write(10);
                stdin.flush();

                stdout = sess.getStdout();


                byte[] b = new byte[1024];
                int j;

                while((j=stdout.read(b))>0){
                    System.out.println("j--->"+j);
                    final String temp =new String(b,0,j);
                    final String tempGbk = new String(b,0,j,"gbk");
                    //final String a = temp;
                    String result = temp;

                    if(temp.contains("认证成功")){
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                PackageManager pm = MainActivity.this.getPackageManager();
                                Intent notificationIntent = pm.getLaunchIntentForPackage(MainActivity.this.getPackageName());
                                notificationIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                notificationIntent.setAction(Intent.ACTION_MAIN);
                                notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                                PendingIntent pi = PendingIntent.getActivity(MainActivity.this, 0, notificationIntent, 0);
                                n.setLatestEventInfo(MainActivity.this,"Mentohust","Mentohust is running",pi);
                                nm.notify(ID,n);
                                Toast.makeText(MainActivity.this,"Mentohust认证成功",Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                    if(temp.contains("已发送退出信号")){
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                nm.cancel(ID);
                                Toast.makeText(MainActivity.this,"Mentohust认证已退出",Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                    if(temp.contains("已经运行")){
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                PackageManager pm = MainActivity.this.getPackageManager();
                                Intent notificationIntent = pm.getLaunchIntentForPackage(MainActivity.this.getPackageName());
                                notificationIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                notificationIntent.setAction(Intent.ACTION_MAIN);
                                notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                                PendingIntent pi = PendingIntent.getActivity(MainActivity.this, 0, notificationIntent, 0);
                                n.setLatestEventInfo(MainActivity.this,"Mentohust","Mentohust is running",pi);
                                nm.notify(ID,n);
                                Toast.makeText(MainActivity.this,"Mentohust已经运行",Toast.LENGTH_LONG).show();
                            }
                        });
                    }

                    if(tempGbk.contains("计费")||tempGbk.contains("账户余额")||temp.contains("系统提示"))
                        result=tempGbk;
                    //System.out.println(detectEncode(s)+"-->"+s);
                    final String s = result;
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            textView1.setText(textView1.getText()+"\n"+s);
                            System.out.println("--->"+temp);
                            scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                        }
                    });
                }
                System.out.println("j--end-->"+j);


            }
        }catch(Exception e){
            e.printStackTrace();
        }finally {

            if (stdout!=null) {
                try {
                    stdout.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (stdin!=null) {
                try {
                    stdin.flush();
                    stdin.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (sess!=null)
                sess.close();
            if (conn!=null)
                conn.close();
        }
    }
}
