<?php require_once 'dbConfig.php'?>
<?php
//-------------------------------------------操作数据库前的验证部分-------------------------------------
function check($username, $password){
	if($dbConfig['username'] == $username && $dbConfig['password'] == $password){
		return true;
	}else{
		echo '数据库用户名或者密码不正确！';
		return false;
	}
}

//-------------------------------------------操作表部分-----------------------------------------------
//创建表
function createTable($tableName){
	$fileName = getFileName($tableName);
	
	if(file_exists($fileName)){
		echo '注意：['.$tableName.']已经存在，请手工处理删除，再重新创建。';
	}else{
		$fp=fopen($fileName, "w+");
		if (!is_writable($fileName) ){
			echo '注意：['.$tableName.']不可写，请给文件配置相应的写权限。';
		}
		fclose($fp);
	}
}

//删除表
function deleteTable($tableName){
	unlink(getFileName($tableName));
}

//-------------------------------------------查询部分-----------------------------------------------
//查询所有数据
function queryAll($tableName){
	$fileName = getFileName($tableName);
	if(!file_exists($fileName)){
		return array();
	}

	$dataArrayStr = file_get_contents(getFileName($tableName));
	
	if($dataArrayStr == ''){
		return array();
	}
	
	return json_decode($dataArrayStr);
}

//根据单个条件查询
function query($tableName, $queryKey, $queryValue){
	$dataArray = queryAll($tableName);
	$ret = array();
	
	//遍历删除
	$i = 0;
	foreach($dataArray as $key=>$value){
	    $tempValue = $value -> $queryKey;
	    if($tempValue == $queryValue){
			$ret[$i++] = $value;
		}
	}
	return $ret;
}

//-------------------------------------------插入数据部分-----------------------------------------------
//整表插入
function insertAll($tableName, $dataArray){
	file_put_contents(getFileName($tableName),json_encode($dataArray));
}

//插入单条数据
function insert($tableName, $dataArrayLine){
	$dataArray = queryAll($tableName);
	array_push($dataArray, $dataArrayLine);
	insertAll($tableName, $dataArray);
}

//-------------------------------------------删除数据部分-----------------------------------------------
//删除所有
function deleteAll($tableName){
	$dataArray = queryAll($tableName);
	file_put_contents(getFileName($tableName),'');
	return $dataArray;
}

//根据字段条件删除
function delete($tableName, $deleteKey, $deleteValue){
	$dataArray = queryAll($tableName);
	//遍历删除
	foreach($dataArray as $key=>$value){
	    $tempValue = $value -> $deleteKey;
	    if($tempValue == $deleteValue){
			arrayRemove($dataArray, $key);
		}
	}
	insertAll($tableName, $dataArray);
}

//-------------------------------------------内部使用方法-----------------------------------------------
//根据表名拼接文件地址
function getFileName($tableName){
	return '../data/'.$tableName.'.db';
}

//删除数组元素
function arrayRemove(&$arr, $offset){ 
	array_splice($arr, $offset, 1); 
}

function test(){
	$dataArrayLine0['name'] = 'xuan';
	$dataArrayLine0['age'] = '19';

	$dataArrayLine1['name'] = 'anan';
	$dataArrayLine1['age'] = '21';

	$dataArrayLine2['name'] = 'hhhhh';
	$dataArrayLine2['age'] = '20';

	$dataArray[0] = $dataArrayLine0;
	$dataArray[1] = $dataArrayLine1;
	$dataArray[2] = $dataArrayLine2;

	//insertAll('111',$dataArray);
	//$dataArray = queryAll('111');
	//insertAll('111',$dataArray);
	deleteLine('111','name','xuan');
}

?>