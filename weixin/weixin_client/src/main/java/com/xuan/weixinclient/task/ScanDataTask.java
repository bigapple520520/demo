package com.xuan.weixinclient.task;

import java.util.concurrent.TimeoutException;

import net.zdsoft.keel.util.Validators;
import net.zdsoft.keel.util.concurrent.AbstractRunnableTask;

import com.winupon.base.wpcf.util.UUIDUtils;
import com.xuan.weixinclient.client.ApplicationConfigHelper;
import com.xuan.weixinclient.client.MsgClient;
import com.xuan.weixinclient.client.ServiceLocator;
import com.xuan.weixinserver.message.FromClientMessage;
import com.xuan.weixinserver.message.FromClientRespMessage;
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
			/*获取客户端的全量数据，并编码成Json格式，方便传输*/
			String retStr = ServiceLocator.getDataService().getAllDataJsonStr();

			if(Validators.isEmpty(retStr)){
				log.debug("未找到数据，请确保该目录下的文件存在：" + ApplicationConfigHelper.getDataFilePath());
				return;
			}

			log.debug("发起一次同步，全量数据：" + retStr);

			/*构造消息把数据发送到服务器端，对服务器端返回暂时不处理，只打印日志*/
			FromClientMessage fromClientMessage = new FromClientMessage(FromClientMessage.ACTION_SYNC_DATA, retStr);
			AbstractMessage abstractMessage = MsgClient.getInstance().sendMessage2WaitResponse(UUIDUtils.createId(), fromClientMessage, 5000);
			FromClientRespMessage message = (FromClientRespMessage)abstractMessage;

			if(FromClientMessage.ACTION_SYNC_DATA == message.getType()){
				log.debug("同步结果：" + message.getMessage());
			}else{
				log.debug("发送和返回的消息类型不匹配，请联系开发");
			}
		}
		catch(TimeoutException e1){
			log.error("网络超时，请检查网络设置，原因："+e1.getMessage(),e1);
		}
		catch (Exception e2) {
			log.error("扫描数据异常，原因："+e2.getMessage(),e2);
		}
	}

}
