<?php require_once '../api/dbUtils.php' ?>

<?php
$tableArray = array();

$dataArray = array();
$queryTableName = '';
$columArray = array();
?>

<?php
//表示添加数据库表
if(isset($_POST['addTableName'])){
	createTable($_POST['addTableName']);
}

//表示删除数据库表
if(isset($_GET['deleteTableName'])){
    deleteTable($_GET['deleteTableName']);
}

//表示要查看的表的数据
if(isset($_GET['queryTableName'])){
	$queryTableName = $_GET['queryTableName'];
	$dataArray = queryAll($queryTableName);
	$columArray = getColumns($dataArray);
}

//获取所有表
$tableArray = getTables();
?>

<?php
function getColumns($dataArray){
	if(count($dataArray) == 0){
		return array();
	}

	$columnArray = array();
	$i = 0;
	foreach($dataArray[0] as $key => $value){
		$columnArray[$i++] = $key;
	}
	return $columnArray;
}

function getTables(){
	$retArray = scandir('../data');
	arrayRemove($retArray, 0);
	arrayRemove($retArray, 0);
	return $retArray;
}
?>