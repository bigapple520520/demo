/*
 * @(#)MainFrame.java    Created on 2014-4-15
 * Copyright (c) 2014 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.xuan.monitor.frame;

import java.awt.Font;
import java.awt.event.MouseEvent;
import java.util.concurrent.TimeoutException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;

import com.winupon.base.wpcf.util.UUIDUtils;
import com.xuan.monitor.adapter.MouseClickedkListener;
import com.xuan.monitor.client.MsgClient;
import com.xuan.monitor.utils.DialogUtils;
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

    private final DefaultTableModel model = new DefaultTableModel();

    public MainFrame() {
        super();
        init();
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
    				String serviceId = tableInput1.getText();
        			String table = tableInput2.getText();

        			if(StringUtils.isEmpty(serviceId)){
        				DialogUtils.showInfo("请输入serviceId号，不能为空哦");
        				return;
        			}

        			if(StringUtils.isEmpty(table)){
        				DialogUtils.showInfo("请输入要查询的表，不能为空哦");
        				return;
        			}

        			JSONObject object = new JSONObject();
            		object.put("serviceId", serviceId);
            		object.put("table", table);

        			FromClientMessage fromMessage = new FromClientMessage(1, object.toString());
        			AbstractMessage respMessage = MsgClient.getInstance().sendMessage2WaitResponse(UUIDUtils.createId(), fromMessage, 5000);
        			FromClientRespMessage message = (FromClientRespMessage)respMessage;
        			if(1 == message.getType()){
        				model.addColumn("数据", new Object[]{message.getMessage()});
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
    	JTable table = new JTable(model){
			private static final long serialVersionUID = 4887372060511439395L;
			@Override
    		public boolean isCellEditable(int row, int column){
    			return false;
            }
    	};
    	JScrollPane jscrollPane = new JScrollPane(table);
    	jscrollPane.setBounds(50, 110, 1000, 500);
    	panel.add(jscrollPane);
    }

}