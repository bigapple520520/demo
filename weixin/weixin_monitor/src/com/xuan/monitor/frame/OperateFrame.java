package com.xuan.monitor.frame;

import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.json.JSONObject;

import com.winupon.base.wpcf.util.UUIDUtils;
import com.xuan.monitor.adapter.MouseClickedkListener;
import com.xuan.monitor.client.MsgClient;
import com.xuan.monitor.utils.DialogUtils;
import com.xuan.monitor.utils.JsonUtils;
import com.xuan.utils.Validators;
import com.xuan.weixinserver.entity.Constants;
import com.xuan.weixinserver.message.FromClientMessage;
import com.xuan.weixinserver.message.FromClientRespMessage;
import com.xuan.weixinserver.message.common.AbstractMessage;

/**
 * 操作界面
 *
 * @author xuan
 * @version 创建时间：2014-7-31 下午3:00:52
 */
public class OperateFrame extends JFrame {
	private static final long serialVersionUID = -6055990768990222439L;

	public OperateFrame() {
        super();
        init();
    }

	private void init() {
        setTitle("监控操作");
        setSize(1100, 700);
        //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 总的布局
        JPanel panel = new JPanel();
        panel.setLayout(null);
        getContentPane().add(panel);
        initView(panel);
        setVisible(true);
    }

	private void initView(JPanel panel){
		JLabel tips1 = new JLabel("输入服务号[serviceId]：");
    	tips1.setBounds(50, 60, 300, 30);
    	panel.add(tips1);

    	final JTextField tableInput1 = new JTextField();
    	tableInput1.setBounds(350, 60, 200, 30);
    	panel.add(tableInput1);

    	JLabel tips2 = new JLabel("输入数据表名，现在默认root[table]：");
    	tips2.setBounds(50, 100, 300, 30);
    	panel.add(tips2);

    	final JTextField tableInput2 = new JTextField();
    	tableInput2.setBounds(350, 100, 200, 30);
    	panel.add(tableInput2);

    	JLabel tips3 = new JLabel("输入要查询的变量名的值[queryKey]：");
    	tips3.setBounds(50, 140, 300, 30);
    	panel.add(tips3);

    	final JTextField tableInput3 = new JTextField();
    	tableInput3.setBounds(350, 140, 200, 30);
    	panel.add(tableInput3);

    	JLabel tips4 = new JLabel("输入修改字段名[name]：");
    	tips4.setBounds(50, 180, 300, 30);
    	panel.add(tips4);

    	final JTextField tableInput4 = new JTextField();
    	tableInput4.setBounds(350, 180, 200, 30);
    	panel.add(tableInput4);

    	JLabel tips5 = new JLabel("输入修改字段值[value]：");
    	tips5.setBounds(50, 220, 300, 30);
    	panel.add(tips5);

    	final JTextField tableInput5 = new JTextField();
    	tableInput5.setBounds(350, 220, 200, 30);
    	panel.add(tableInput5);

    	JButton button = new JButton("提交修改");
    	button.setBounds(50, 300, 120, 30);
    	button.addMouseListener(new MouseClickedkListener(){
    		@Override
    		public void mouseClicked(MouseEvent e) {
    			if(MsgClient.getInstance().isClosed()){
    				DialogUtils.showInfo("未登录，请先登录");
    				return;
    			}

    			String serviceId = tableInput1.getText();
    			String tableName = tableInput2.getText();
    			String queryKey = tableInput3.getText();
    			String name = tableInput4.getText();
    			String value = tableInput5.getText();

    			if(Validators.isEmpty(serviceId)){
    				DialogUtils.showInfo("请输入serviceId");
    				return;
    			}

    			if(Validators.isEmpty(tableName)){
    				DialogUtils.showInfo("请输入tableName");
    				return;
    			}

    			if(Validators.isEmpty(queryKey)){
    				DialogUtils.showInfo("请输入queryKey");
    				return;
    			}

    			if(Validators.isEmpty(name)){
    				DialogUtils.showInfo("请输入name");
    				return;
    			}

    			if(Validators.isEmpty(value)){
    				DialogUtils.showInfo("请输入value");
    				return;
    			}

    			try {
    				JSONObject messageObj = new JSONObject();
        			messageObj.put("serviceId", serviceId);
        			messageObj.put("tableName", tableName);
        			messageObj.put("queryKey", queryKey);
        			messageObj.put("name", name);
        			messageObj.put("value", value);

        			FromClientMessage fromClientMessage = new FromClientMessage(FromClientMessage.ACTION_MODIFY_DATA,messageObj.toString());
        			AbstractMessage abstractMessage = MsgClient.getInstance().sendMessage2WaitResponse(UUIDUtils.createId(), fromClientMessage, 5000);
        			FromClientRespMessage message = (FromClientRespMessage)abstractMessage;

        			JSONObject object = new JSONObject(message.getMessage());
        			if(Constants.SUCCESS_1.equals(JsonUtils.getString(object, "success"))){
        				DialogUtils.showInfo("修改成功："+JsonUtils.getString(object, "message"));
        			}else{
        				DialogUtils.showInfo("提交修改服务器返回错误，原因："+JsonUtils.getString(object, "message"));
        			}
    			} catch (Exception e2) {
					e2.printStackTrace();
					DialogUtils.showInfo("提交异常，原因："+e2.getMessage());
				}
    		}
    	});
    	panel.add(button);
	}

}
