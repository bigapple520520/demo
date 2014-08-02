/*
 * @(#)MainFrame.java    Created on 2014-4-15
 * Copyright (c) 2014 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.xuan.monitor.frame;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeoutException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;

import com.winupon.base.wpcf.util.SecurityUtils;
import com.winupon.base.wpcf.util.UUIDUtils;
import com.xuan.monitor.adapter.MouseClickedkListener;
import com.xuan.monitor.client.MsgClient;
import com.xuan.monitor.utils.DialogUtils;
import com.xuan.monitor.utils.JsonDataUtils;
import com.xuan.weixinserver.entity.Constants;
import com.xuan.weixinserver.entity.Result;
import com.xuan.weixinserver.entity.ServiceData;
import com.xuan.weixinserver.entity.Table;
import com.xuan.weixinserver.entity.TableLine;
import com.xuan.weixinserver.message.FromClientMessage;
import com.xuan.weixinserver.message.FromClientRespMessage;
import com.xuan.weixinserver.message.common.AbstractMessage;

/**
 * 主窗口
 *
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-4-15 上午11:39:20 $
 */
public class MainFrame extends JFrame {
    private static final long serialVersionUID = 8656082015568504447L;

    private DefaultTableModel model;
    private JTable jTable;

    public MainFrame() {
        super();
        initMenu();
        init();
    }

    //创建菜单
    private void initMenu(){
    	JMenuBar jMenuBar = new JMenuBar();
    	JMenu jMenu = new JMenu("操作");

    	JMenuItem loginItem = new JMenuItem("登录");
    	loginItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
		    	String username = "anan";
		    	String password = "123456";

		    	try {
		    		String loginId = SecurityUtils.encodeByMD5(username+password);
		        	String token = SecurityUtils.encodeByMD5(loginId);
		        	MsgClient.getInstance().init("127.0.0.1", 10000, loginId, token);
				} catch (Exception e2) {
					e2.printStackTrace();
					DialogUtils.showInfo("登录失败，原因："+e2.getMessage());
					return;
				}

		    	if(MsgClient.getInstance().isLogined()){
		    		DialogUtils.showInfo("登录成功请操作");
		    	}else{
		    		DialogUtils.showInfo("登录失败，请检查服务器有没有启动什么的");
		    	}
			}
		});

    	JMenuItem disconnectItem = new JMenuItem("断开");
    	disconnectItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MsgClient.getInstance().close();
				DialogUtils.showInfo("断开成功");
			}
		});

    	JMenuItem modifyItem = new JMenuItem("监控修改");
    	modifyItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new OperateFrame();
			}
		});

    	jMenu.add(loginItem);
    	jMenu.add(disconnectItem);
    	jMenu.add(modifyItem);

    	jMenuBar.add(jMenu);
    	setJMenuBar(jMenuBar);
    }

    private void init() {
        setTitle("监控系统");
        setSize(1100, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 总的布局
        JPanel panel = new JPanel();
        panel.setLayout(null);
        getContentPane().add(panel);
        initView(panel);
        setVisible(true);
    }

    private void initView(JPanel panel){
    	initTitle(panel);
    	initQuery(panel);
    	initTable(panel);
    }

    private void initTitle(JPanel panel){
    	JLabel lebel = new JLabel("自动化监控系统");
    	lebel.setFont(new Font("宋体",Font.BOLD,26));
    	lebel.setBounds(400, 10, 300, 50);
    	panel.add(lebel);
    }

    private void initQuery(JPanel panel){
    	JLabel tips1 = new JLabel("输入服务号：");
    	tips1.setBounds(50, 60, 150, 30);
    	panel.add(tips1);

    	final JTextField tableInput1 = new JTextField();
    	tableInput1.setBounds(200, 60, 150, 30);
    	panel.add(tableInput1);

    	JLabel tips2 = new JLabel("输入要查询的表名：");
    	tips2.setBounds(360, 60, 150, 30);
    	panel.add(tips2);

    	final JTextField tableInput2 = new JTextField();
    	tableInput2.setBounds(520, 60, 150, 30);
    	panel.add(tableInput2);

    	JButton button = new JButton("查询");
    	button.setBounds(680, 60, 80, 30);
    	button.addMouseListener(new MouseClickedkListener(){
    		@Override
    		public void mouseClicked(MouseEvent e) {
    			try {
    				if(MsgClient.getInstance().isClosed()){
    					DialogUtils.showInfo("请先在左上角操作菜单项中进行登录操作");
    					return;
    				}

    				String serviceId = tableInput1.getText();
        			String tableName = tableInput2.getText();

        			if(StringUtils.isEmpty(serviceId)){
        				DialogUtils.showInfo("请输入serviceId号，不能为空哦");
        				return;
        			}

        			if(StringUtils.isEmpty(tableName)){
        				DialogUtils.showInfo("请输入要查询的表，不能为空哦");
        				return;
        			}

        			JSONObject object = new JSONObject();
            		object.put("serviceId", serviceId);
            		object.put("tableName", tableName);

        			FromClientMessage fromMessage = new FromClientMessage(FromClientMessage.ACTION_GET_DATA, object.toString());
        			AbstractMessage respMessage = MsgClient.getInstance().sendMessage2WaitResponse(UUIDUtils.createId(), fromMessage, 5000);
        			FromClientRespMessage message = (FromClientRespMessage)respMessage;
        			if(FromClientMessage.ACTION_GET_DATA == message.getType()){
        				Result<ServiceData> result = JsonDataUtils.decodeServiceDataFromJsonStr(message.getMessage());
        				if(Constants.SUCCESS_1.equals(result.getSuccess())){
        					ServiceData serviceData = result.getData();
            				Table table = serviceData.getTable(Constants.TABLE);

            				Map<String,List<String>> name2ColumMap = new HashMap<String, List<String>>();

            				for(Entry<String, TableLine> entry : table.getMap().entrySet()){
            					TableLine tableLine = entry.getValue();
            					for(Entry<String, String> name2ValueEntry : tableLine.getMap().entrySet()){
            						String name = name2ValueEntry.getKey();
            						String value = name2ValueEntry.getValue();

            						List<String> columDataList = name2ColumMap.get(name);
            						if(null == columDataList){
            							columDataList = new ArrayList<String>();
            							name2ColumMap.put(name, columDataList);
            						}
            						columDataList.add(value);
            					}
            				}

            				model = new DefaultTableModel();
            				for(Entry<String, List<String>> entry : name2ColumMap.entrySet()){
            					model.addColumn(entry.getKey(),entry.getValue().toArray(new Object[entry.getValue().size()]));

            				}
            				jTable.setModel(model);
        				}else{
        					//异常提示
            				model = new DefaultTableModel();
            				model.addColumn("错误提示",new Object[]{result.getMessage()});
            				jTable.setModel(model);
        				}
        			}else{
        				//返回类型对不上，一般不会出现
        				model = new DefaultTableModel();
        				model.addColumn("错误提示",new Object[]{"返回类型错误："+message.getType()});
        				jTable.setModel(model);
        			}
				}
    			catch(TimeoutException e1){
    				DialogUtils.showInfo("连接服务器超时，请确保服务器连接成功");
    			}
    			catch (Exception e2) {
					DialogUtils.showInfo("系统异常，原因：" + e2.getMessage());
				}
    		}
    	});
    	panel.add(button);
    }

    private void initTable(JPanel panel){
    	jTable = new JTable(){
			private static final long serialVersionUID = 4887372060511439395L;
			@Override
    		public boolean isCellEditable(int row, int column){
    			return false;
            }
    	};
    	JScrollPane jscrollPane = new JScrollPane(jTable);
    	jscrollPane.setBounds(50, 110, 1000, 500);
    	panel.add(jscrollPane);
    }

}
