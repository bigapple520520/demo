package com.xuan.weixinclient.task;

import java.util.concurrent.TimeoutException;

import net.zdsoft.keel.util.concurrent.AbstractRunnableTask;

import com.winupon.base.wpcf.util.UUIDUtils;
import com.xuan.weixinclient.client.MsgClient;
import com.xuan.weixinclient.client.ServiceLocator;
import com.xuan.weixinserver.message.CommonMessage;
import com.xuan.weixinserver.message.CommonRespMessage;
import com.xuan.weixinserver.message.common.AbstractMessage;

/**
 * 不断扫描数据任务类
 *
 *
 * @author xuan
 * @version 创建时间：2014-7-25 下午4:16:57
 */
public class ScanDataTask extends AbstractRunnableTask {
	public ScanDataTask(){
		super("扫描数据任务[ScanDataTask]");
	}

	@Override
	public void processTask() throws Exception {
		try {
			//获取全量数据
			String retStr = ServiceLocator.getDataService().getAllDataJsonStr();
			log.debug("发起一次同步，全量数据："+retStr);

			//同步到中央服务器
			CommonMessage commonMessage = new CommonMessage(1, retStr);
			AbstractMessage abstractMessage = MsgClient.getInstance().sendMessage2WaitResponse(UUIDUtils.createId(), commonMessage, 5000);
			CommonRespMessage message = (CommonRespMessage)abstractMessage;

			log.debug("同步成功：" + message.getMessage());
		}
		catch(TimeoutException e1){
			log.error("网络超时，请检查网络设置，原因："+e1.getMessage(),e1);
		}
		catch (Exception e2) {
			log.error("扫描数据异常，原因："+e2.getMessage(),e2);
		}
	}

}
