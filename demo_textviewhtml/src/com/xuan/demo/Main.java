package com.xuan.demo;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.xuan.demo.textviewhtml.HtmlTextUtils;
import com.xuan.demo.textviewhtml.ImgSpanUtils;
import com.xuan.demo.textviewhtml.TextViewHtmlUtils;
import com.xuan.demo.textviewhtml.helper.LoveAnTagHandler;

/**
 * 记得在加载网络图片是要加上网络访问的权限<br>
 * 
 * <uses-permission android:name="android.permission.INTERNET" />
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2013-7-1 上午10:36:56 $
 */
public class Main extends Activity {

    private TextView text1;
    private TextView text2;
    private TextView text3;
    private TextView text4;
    private TextView text5;
    private EditText edit6;
    private TextView text7;
    private TextView text8;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        text1 = (TextView) findViewById(R.id.text1);
        text2 = (TextView) findViewById(R.id.text2);
        text3 = (TextView) findViewById(R.id.text3);
        text4 = (TextView) findViewById(R.id.text4);
        text5 = (TextView) findViewById(R.id.text5);
        edit6 = (EditText) findViewById(R.id.edit6);
        text7 = (TextView) findViewById(R.id.text7);
        text8 = (TextView) findViewById(R.id.text8);

        // 用html加载一般文字
        TextViewHtmlUtils.setTextByHtml(text1, "1、<h1>我是被html用户h1加粗过的字</h1>");

        // 用html加载本地resid的图片,高宽用原图
        TextViewHtmlUtils.setTextByHtml4ImgByResid(text2,
                "2、用html加载本地resid的图片,高宽用原图" + HtmlTextUtils.getImgHtml(R.drawable.ic_launcher), Main.this);

        // 用html加载本地resid的图片,高宽设置：30*30
        TextViewHtmlUtils.setTextAndImgByHtml4ResidBySize(text3,
                "3、用html加载本地resid的图片,高宽设置：30*30" + HtmlTextUtils.getImgHtml(R.drawable.ic_launcher), Main.this, 30, 30);

        // 用html加载网络图片，高宽用原图
        TextViewHtmlUtils.setTextByHtml4ImgByUrl(text4,
                "4、用html加载网络图片，高宽用原图" + HtmlTextUtils.getImgHtml("http://www.baidu.com/img/bdlogo.gif"));

        // 用html加载本地图片，用的是路劲不是resid哦
        String fileName = Environment.getExternalStorageDirectory().getPath() + "/xuantest/11.png";
        TextViewHtmlUtils.setTextByHtml4ImgByPath(text5,
                "5、用html加载本地图片，用的是路劲不是resid哦" + HtmlTextUtils.getImgHtml(fileName));

        // 下面是EditText显示图片，但其实上是用text来代替的
        SpannableString spannableStr = ImgSpanUtils.getSpannableStrByTextReplaceImg(Main.this, "xuanxuan",
                R.drawable.ic_launcher);
        edit6.setText(spannableStr);
        text7.setText("6、上面那个图片，其实我在程序里用edit6.getText().toString()方法拿到的是：" + edit6.getText().toString());

        // 自定义tag，点击可以退出
        LoveAnTagHandler tag = new LoveAnTagHandler(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Main.this, "扯淡，没有惊喜", Toast.LENGTH_SHORT).show();
            }
        });

        TextViewHtmlUtils.setTextByHtml(text8, "7、<lovean>点击我有惊喜哦，亲</lovean><br /><br /><br /><br /><br />", null, tag);
        text8.setMovementMethod(LinkMovementMethod.getInstance());// 注意要加这个点击才有用
    }

}
